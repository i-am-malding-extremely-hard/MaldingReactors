package im.malding.maldingreactors.multiblock;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public interface ReactorMultiblock extends ReactorPart {

    @NotNull SingleVariantStorage<FluidVariant> getFuelTank();

    @NotNull EnergyStorage getEnergyStorage();

    int getCoreHeat();

    int getCasingHeat();
}
