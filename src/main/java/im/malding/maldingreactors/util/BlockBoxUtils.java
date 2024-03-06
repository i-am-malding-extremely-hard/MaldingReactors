package im.malding.maldingreactors.util;

import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BlockBoxUtils {

    public static void surfaceAreaAction(BlockBox blockBox, SideAction action) {
        int maxX = blockBox.getBlockCountX();
        int maxY = blockBox.getBlockCountY();
        int maxZ = blockBox.getBlockCountZ();

        BlockPos.Mutable minPos = new BlockPos.Mutable(blockBox.getMinX(), blockBox.getMinY(), blockBox.getMinZ()); //Gold Block

        sideAction(maxX, Direction.EAST, maxY, Direction.UP, minPos.mutableCopy(), action);
        sideAction(maxZ, Direction.SOUTH, maxY, Direction.UP, minPos.mutableCopy(), action);
        sideAction(maxZ, Direction.SOUTH, maxX, Direction.EAST, minPos.mutableCopy(), action);

        BlockPos.Mutable maxPos = new BlockPos.Mutable(blockBox.getMaxX(), blockBox.getMaxY(), blockBox.getMaxZ());

        sideAction(maxX, Direction.WEST, maxY, Direction.DOWN, maxPos.mutableCopy(), action);
        sideAction(maxZ, Direction.NORTH, maxY, Direction.DOWN, maxPos.mutableCopy(), action);
        sideAction(maxZ, Direction.NORTH, maxX, Direction.WEST, maxPos.mutableCopy(), action);
    }

    public static void sideAction(int maxA, Direction aDirection, int maxB, Direction bDirection, BlockPos startPos, SideAction action) {
        BlockPos.Mutable pos = startPos.mutableCopy();

        for (int a = 0; a < maxA; a++) {
            for (int b = 0; b < maxB; b++) {
                //Checks if we are on the perimeter of the square
                boolean isEdge = (a == 0 || b == 0 || a == (maxA - 1) || b == (maxB - 1));

                action.apply(pos, isEdge);

                pos.move(bDirection);
            }

            pos = startPos.mutableCopy().move(aDirection, a + 1);
        }
    }

    public interface SideAction {
        void apply(BlockPos pos, boolean isEdge);
    }
}
