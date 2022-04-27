package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class ReactorPowerPortBlockEntity extends ReactorBaseBlockEntity implements EnergyStorage {

    public ReactorPowerPortBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_POWER_PORT, pos, state);
    }

    @Override
    public boolean supportsInsertion() {
        return false;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        ReactorControllerBlockEntity controller = this.getController();

        if(controller != null) {
            return controller.energyStorage.extract(maxAmount, transaction);
        }else{
            return 0;
        }
    }

    @Override
    public long getAmount() {
        ReactorControllerBlockEntity controller = this.getController();

        if(controller != null) {
            return controller.energyStorage.getAmount();
        }else{
            return 0;
        }
    }

    @Override
    public long getCapacity() {
        ReactorControllerBlockEntity controller = this.getController();

        if(controller != null) {
            return controller.energyStorage.getCapacity();
        }else{
            return 0;
        }
    }
}
