package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.AllBlockEntities;
import i.malding.hard.maldingreactors.multiblock.ReactorMultiblock;
import me.alphamode.star.transfer.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class ReactorControllerBlockEntity extends BlockEntity implements ReactorMultiblock {

    private SingleVariantStorage<FluidVariant> fuel = new FluidTank(FluidConstants.BUCKET * 8);
    private EnergyStorage energyStorage = new SimpleEnergyStorage(4000 * 50, 0, Long.MAX_VALUE);
    private int coreHeat, castingHeat;

    public ReactorControllerBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.REACTOR_CONTROLLER, pos, state);
    }

    public void tick() {

    }

    @Override
    public SingleVariantStorage<FluidVariant> getFuel() {
        return fuel;
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
}
