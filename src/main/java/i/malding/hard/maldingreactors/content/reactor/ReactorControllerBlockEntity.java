package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.handlers.ReactorScreenHandler;
import i.malding.hard.maldingreactors.content.AllBlockEntities;
import i.malding.hard.maldingreactors.multiblock.ReactorMultiblock;
import me.alphamode.star.transfer.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class ReactorControllerBlockEntity extends BlockEntity implements ReactorMultiblock, NamedScreenHandlerFactory {

    private static final String FUEL_TANK_KEY = "FuelTank";
    private static final String WASTE_TANK_KEY = "WasteTank";

    private SingleVariantStorage<FluidVariant> fuel = new FluidTank(FluidConstants.BUCKET * 8);
    private SingleVariantStorage<FluidVariant> waste = new FluidTank(FluidConstants.BUCKET * 8);

    private EnergyStorage energyStorage = new SimpleEnergyStorage(4000 * 50, 0, Long.MAX_VALUE);
    private int coreHeat, castingHeat;

    private boolean isMultiBlock = false;

    public ReactorControllerBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.REACTOR_CONTROLLER, pos, state);
    }

    public void tick() {

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        readTankInfoFromNbt(nbt, fuel, FUEL_TANK_KEY);
        readTankInfoFromNbt(nbt, waste, WASTE_TANK_KEY);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        writeTankInfoToNbt(nbt, fuel, FUEL_TANK_KEY);
        writeTankInfoToNbt(nbt, waste, WASTE_TANK_KEY);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public SingleVariantStorage<FluidVariant> getFuel() {
        return fuel;
    }

    public SingleVariantStorage<FluidVariant> getWaste(){
        return waste;
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

    private static final String VARIANT_KEY = "Variant";
    private static final String TANK_AMOUNT_KEY = "Amount";

    public static void writeTankInfoToNbt(NbtCompound nbtCompound, SingleVariantStorage<FluidVariant> tank, @Nullable String nbtKey){
        if(nbtKey != null) {
            NbtCompound tankTag = new NbtCompound();

            tankTag.put(VARIANT_KEY, tank.getResource().toNbt());
            tankTag.putLong(TANK_AMOUNT_KEY, tank.getAmount());

            nbtCompound.put(nbtKey, tankTag);
        }else{
            nbtCompound.put(VARIANT_KEY, tank.getResource().toNbt());
            nbtCompound.putLong(TANK_AMOUNT_KEY, tank.getAmount());
        }
    }

    public static void readTankInfoFromNbt(NbtCompound nbtCompound, SingleVariantStorage<FluidVariant> tank, @Nullable String nbtKey){
        if(nbtKey != null) {
            NbtCompound tankTag = (NbtCompound) nbtCompound.get(nbtKey);

            if(tankTag != null) {
                tank.amount = tankTag.getLong(TANK_AMOUNT_KEY);
                tank.variant = FluidVariant.fromNbt((NbtCompound) tankTag.get(VARIANT_KEY));
            }
        }else {
            tank.amount = nbtCompound.getLong(TANK_AMOUNT_KEY);
            tank.variant = FluidVariant.fromNbt((NbtCompound) nbtCompound.get(VARIANT_KEY));
        }
    }
}
