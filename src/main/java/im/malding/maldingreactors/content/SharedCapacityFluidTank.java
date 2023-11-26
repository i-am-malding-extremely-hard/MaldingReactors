package im.malding.maldingreactors.content;

import im.malding.maldingreactors.content.fluids.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import java.util.function.Supplier;

public class SharedCapacityFluidTank extends FluidTank {

    private final Supplier<Long> linkedTankAmount;

    public SharedCapacityFluidTank(long capacity, Runnable onCommitAction, Supplier<Long> linkedTankAmount) {
        super(capacity, onCommitAction);

        this.linkedTankAmount = linkedTankAmount;
    }

    @Override
    public long getCapacity(FluidVariant variant) {
        return Math.max(0, super.getCapacity() - linkedTankAmount.get());
    }
}
