package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.AllFluids;
import i.malding.hard.maldingreactors.content.handlers.ReactorScreenHandler;
import i.malding.hard.maldingreactors.content.AllBlockEntities;
import i.malding.hard.maldingreactors.multiblock.ReactorMultiblock;
import io.wispforest.owo.ops.WorldOps;
import me.alphamode.star.transfer.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;

import net.minecraft.util.math.MathHelper;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class ReactorControllerBlockEntity extends BlockEntity implements ReactorMultiblock, NamedScreenHandlerFactory {

    private static final String FUEL_TANK_KEY = "FuelTank";
    private static final String WASTE_TANK_KEY = "WasteTank";

    private final FluidTank fuelTank = new FluidTank(FluidConstants.BUCKET * 8);
    private final FluidTank wasteTank = new FluidTank(FluidConstants.BUCKET * 8);

    private EnergyStorage energyStorage = new SimpleEnergyStorage(4000 * 50, 0, Long.MAX_VALUE);
    private int coreHeat, castingHeat;

    private static final String MULTIBLOCK_CHECK_KEY = "IsMultiBlock";

    private boolean isMultiBlock = false;

    public ReactorControllerBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.REACTOR_CONTROLLER, pos, state);
    }

    public void tick() {

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        fuelTank.fromNbt(nbt, FUEL_TANK_KEY);
        wasteTank.fromNbt(nbt, WASTE_TANK_KEY);

        this.setMultiBlockCheck(nbt.getBoolean(MULTIBLOCK_CHECK_KEY));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        fuelTank.toNbt(nbt, FUEL_TANK_KEY);
        wasteTank.toNbt(nbt, WASTE_TANK_KEY);

        nbt.putBoolean(MULTIBLOCK_CHECK_KEY, this.isMultiBlockStructure());

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
        WorldOps.updateIfOnServer(this.world, this.pos);

        super.markDirty();
    }

    @Override
    public FluidTank getFuelTank() {
        return fuelTank;
    }

    public FluidTank getWasteTank(){
        return wasteTank;
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public int getCoreHeat() {
        return coreHeat;
    }

    @Override
    public int getCastingHeat() {
        return castingHeat;
    }

    public void setMultiBlockCheck(boolean result){
        this.isMultiBlock = result;
    }

    public boolean isMultiBlockStructure(){
        return this.isMultiBlock;
    }

    //--------------------------------------------------------------------------------------------------------------

    @Override
    public Text getDisplayName() {
        return Text.of("");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ReactorScreenHandler(syncId, inv);
    }

    //--------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isController() {
        return true;
    }

    @Override
    public BlockPos getControllerPos() {
        return pos;
    }
}
