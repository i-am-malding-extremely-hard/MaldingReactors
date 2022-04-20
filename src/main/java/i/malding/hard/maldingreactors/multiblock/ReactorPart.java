package i.malding.hard.maldingreactors.multiblock;

import net.minecraft.util.math.BlockPos;

public interface ReactorPart {
    boolean isController();

    BlockPos getControllerPos();
}
