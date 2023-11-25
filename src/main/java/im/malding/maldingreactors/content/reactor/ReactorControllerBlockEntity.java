package im.malding.maldingreactors.content.reactor;

import im.malding.maldingreactors.content.MaldingBlockEntities;
import im.malding.maldingreactors.content.MaldingFluids;
import im.malding.maldingreactors.content.handlers.ReactorScreenHandler;
import im.malding.maldingreactors.multiblock.ReactorMultiblock;
import im.malding.maldingreactors.util.ReactorValidator;
import io.wispforest.owo.ops.WorldOps;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.impl.KeyedEndec;
import im.malding.maldingreactors.content.fluids.FluidTank;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class ReactorControllerBlockEntity extends ReactorBaseBlockEntity implements ReactorMultiblock, NamedScreenHandlerFactory {

    //TODO: Use BuiltinEndec version when fixed
    public static final Endec<List<BlockPos>> BLOCK_POS_LIST = Endec.INT.listOf().xmap(
            ints -> new BlockPos(ints.get(0), ints.get(1), ints.get(2)),
            blockpos -> List.of(blockpos.getX(), blockpos.getY(), blockpos.getZ())
    ).listOf();

    public static final KeyedEndec<List<BlockPos>> FUEL_RODS_KEY = BLOCK_POS_LIST.keyed("FuelRods", new ArrayList<>());
    public static final KeyedEndec<List<BlockPos>> FUEL_RODS_CONTROLLERS_KEY = BLOCK_POS_LIST.keyed("FuelRodControllers", new ArrayList<>());

    public static final KeyedEndec<List<BlockPos>> ITEM_PORTS_KEY = BLOCK_POS_LIST.keyed("ItemPorts", new ArrayList<>());
    public static final KeyedEndec<List<BlockPos>> POWER_PORTS_KEY = BLOCK_POS_LIST.keyed("PowerPorts", new ArrayList<>());

    public static final KeyedEndec<Boolean> IS_REACTOR_ACTIVE = Endec.BOOLEAN.keyed("IsReactorActive", false);
    public static final KeyedEndec<Boolean> IS_MULTI_BLOCK = Endec.BOOLEAN.keyed("IsMultiBlock", false);

    //Droplets per tick
    private static final int reactionRate = 1;

    //Energy Per Droplet
    private static final int energyConversionAmount = 50;

    private final FluidTank fuelTank = new FluidTank(FluidConstants.BUCKET * 8);
    private final FluidTank wasteTank = new FluidTank(FluidConstants.BUCKET * 8);

    protected final EnergyStorage energyStorage = new SimpleEnergyStorage(4000 * 50, Long.MAX_VALUE, Long.MAX_VALUE);
    private int coreHeat, casingHeat;

    public List<BlockPos> fuelRods = new ArrayList<>();
    public List<BlockPos> rodControllers = new ArrayList<>();

    public List<BlockPos> itemPorts = new ArrayList<>();
    public List<BlockPos> powerPorts = new ArrayList<>();

    private ReactorValidator validator = null;
    private boolean isMultiBlock = false;

    private boolean isReactorActive = false;

    public ReactorControllerBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_CONTROLLER, pos, state);
    }

    public void attemptToggle(boolean turnOn){
        if(isMultiBlock && turnOn){
            this.isReactorActive = true;
        } else if(!turnOn){
            this.isReactorActive = false;
        }

        this.markDirty();
    }

    public void clientTick() {}

    public void serverTick() {
        if (validator == null) {
            validator = new ReactorValidator(this.world, this.pos);
        }

        if (isValid()) {
            if (isReactorActive) {
                float totalRodAbsorptionRate = 0f;

                int totalReatorRods = 0;
                int totalReactorControlRods = 0;

                for (BlockPos rodControllerPos : rodControllers) {
                    Optional<ReactorFuelRodControllerBlockEntity> possibleBlockEntity = world.getBlockEntity(rodControllerPos, MaldingBlockEntities.REACTOR_FUEL_ROD_CONTROLLER);

                    if(possibleBlockEntity.isEmpty()){
                        continue;
                    }

                    ReactorFuelRodControllerBlockEntity rodController = possibleBlockEntity.get();

                    totalRodAbsorptionRate += rodController.reactionRate / 100f;

                    totalReactorControlRods++;
                }

                totalReatorRods += this.fuelRods.size();

                long consumedFuel = convertFuelToWaste(MathHelper.floor(reactionRate * (totalRodAbsorptionRate / totalReactorControlRods)) * totalReatorRods);

                if (consumedFuel != 0) {
                    long energyCreated = consumedFuel * energyConversionAmount;

                    try (Transaction t = Transaction.openOuter()) {
                        energyStorage.insert(energyCreated, t);

                        t.commit();
                    }
                }
            }
        }
    }

    public long convertFuelToWaste(int maxFuelConsumption) {
        if (fuelTank.getAmount() == 0) {
            return 0;
        }

        try (Transaction t = Transaction.openOuter()) {
            long amountExtracted = fuelTank.extract(FluidVariant.of(MaldingFluids.COPIUM.still()), maxFuelConsumption, t);

            wasteTank.insert(FluidVariant.of(MaldingFluids.MALDING_COPIUM.still()), amountExtracted, t);

            return amountExtracted;
        }
    }

    public void handleBlockRemovable(BlockEntityType<?> type, BlockPos pos){
        if(type == MaldingBlockEntities.REACTOR_FUEL_ROD_CONTROLLER){
            rodControllers.remove(pos);
        } else if(type == MaldingBlockEntities.REACTOR_FUEL_ROD){
            fuelRods.remove(pos);
        } else if(type == MaldingBlockEntities.REACTOR_ITEM_PORT){
            itemPorts.remove(pos);
        } else if(type == MaldingBlockEntities.REACTOR_POWER_PORT){
            powerPorts.remove(pos);
        }

        this.setValid(false);
    }

    //---------------------------------------------------

    public ReactorValidator getValidator() {
        return this.validator;
    }

    public void setRodControllers(List<BlockPos> reactorRods) {
        this.rodControllers = reactorRods;
    }

    @Override
    public @NotNull FluidTank getFuelTank() {
        return fuelTank;
    }

    public FluidTank getWasteTank() {
        return wasteTank;
    }

    @Override
    public @NotNull EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public int getCoreHeat() {
        return coreHeat;
    }

    @Override
    public int getCasingHeat() {
        return casingHeat;
    }

    @Override
    public void setValid(boolean result) {
        this.isMultiBlock = result;
    }

    @Override
    public boolean isValid() {
        return this.isMultiBlock;
    }

    public boolean isReactorActive() {
        return isValid() && this.isReactorActive;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        fuelRods = nbt.get(FUEL_RODS_KEY);
        rodControllers = nbt.get(FUEL_RODS_CONTROLLERS_KEY);

        itemPorts = nbt.get(ITEM_PORTS_KEY);
        powerPorts = nbt.get(POWER_PORTS_KEY);

        fuelTank.fromNbt(nbt, "FuelTank");
        wasteTank.fromNbt(nbt, "WasteTank");

        this.setValid(nbt.get(IS_MULTI_BLOCK));
        isReactorActive = nbt.get(IS_REACTOR_ACTIVE);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put(FUEL_RODS_KEY, fuelRods);
        nbt.put(FUEL_RODS_CONTROLLERS_KEY, rodControllers);

        nbt.put(ITEM_PORTS_KEY, itemPorts);
        nbt.put(POWER_PORTS_KEY, powerPorts);

        fuelTank.toNbt(nbt, "FuelTank");
        wasteTank.toNbt(nbt, "WasteTank");

        nbt.put(IS_MULTI_BLOCK, this.isValid());
        nbt.put(IS_REACTOR_ACTIVE, isReactorActive);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        WorldOps.updateIfOnServer(this.world, this.pos);
    }

    //--------------------------------------------------------------------------------------------------------------

    @Override
    public Text getDisplayName() {
        return Text.of("");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        PacketByteBuf openingData = PacketByteBufs.create();
        openingData.writeBlockPos(pos);
        return new ReactorScreenHandler(syncId, inv, openingData);
    }
}
