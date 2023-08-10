package i.malding.hard.maldingreactors.util;

import i.malding.hard.maldingreactors.content.MaldingBlocks;
import i.malding.hard.maldingreactors.content.reactor.ReactorBaseBlockEntity;
import i.malding.hard.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import i.malding.hard.maldingreactors.content.reactor.ReactorFuelRodBlockEntity;
import i.malding.hard.maldingreactors.content.reactor.ReactorFuelRodControllerBlockEntity;
import i.malding.hard.maldingreactors.data.MaldingTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ReactorValidator {

    public static int MAX_REACTOR_SIZE = 100;

    public final BlockPos controllerPos;
    public final World world;

    @Nullable
    private BlockBox bounds;

    public Set<BlockPos> cachedRodControllerPositions = new HashSet<>();

    public ReactorValidator(World world, BlockPos controllerPos) {
        this.controllerPos = controllerPos;
        this.world = world;
    }

    public boolean validateReactor(BlockState controllerState) {
        Direction facing = controllerState.get(Properties.FACING);
        BlockPos.Mutable pos = new BlockPos.Mutable(controllerPos.getX(), controllerPos.getY(), controllerPos.getZ());

        if (facing == Direction.DOWN || facing == Direction.UP) {
            int northAmount = findDirectionalBound(pos, Direction.NORTH);

            pos.move(Direction.NORTH, northAmount);
            facing = Direction.NORTH;
        }

        int bottomBound = findDirectionalBound(pos, Direction.DOWN);
        int topBound = findDirectionalBound(pos, Direction.UP);

        Direction leftDir = facing.rotateCounterclockwise(Direction.Axis.Y);
        int leftBound = findDirectionalBound(pos, leftDir);

        Direction rightDir = facing.rotateClockwise(Direction.Axis.Y);
        int rightBound = findDirectionalBound(pos, rightDir);

        int backBound = findDirectionalBound(pos.mutableCopy().move(Direction.DOWN, bottomBound), facing.getOpposite());

        BlockPos bottomLeft = pos.mutableCopy().move(Direction.DOWN, bottomBound).move(leftDir, leftBound);
        BlockPos topFarRight = pos.mutableCopy().move(Direction.UP, topBound).move(rightDir, rightBound).move(facing.getOpposite(), backBound);

        BlockBox unvalidatedReactorBounds = BlockBox.create(bottomLeft, topFarRight);

        //Must be a minimum of 3x3x3 blocks
        if (unvalidatedReactorBounds.getBlockCountX() < 3 || unvalidatedReactorBounds.getBlockCountY() < 3 || unvalidatedReactorBounds.getBlockCountZ() < 3) {
            return false;
        }

        //Can be a maximum of 100x100x100 blocks
        if (unvalidatedReactorBounds.getBlockCountX() > MAX_REACTOR_SIZE || unvalidatedReactorBounds.getBlockCountY() > MAX_REACTOR_SIZE || unvalidatedReactorBounds.getBlockCountZ() > MAX_REACTOR_SIZE) {
            return false;
        }

        //Validate that all sides and edges checkout
        if (!validateSidesAndEdges(unvalidatedReactorBounds)) {
            return false;
        }

        //Check if we found any reactor rod controllers
        if (this.cachedRodControllerPositions.isEmpty()) {
            return false;
        }

        //Confirm that the rods for the reactor rod controllers are good
        if (!validateFuelRodsAndControllers(unvalidatedReactorBounds)) {
            return false;
        }

        this.bounds = unvalidatedReactorBounds;

        unvalidatedReactorBounds.forEachVertex(blockPos -> {
            System.out.println(blockPos);
            world.setBlockState(blockPos, Blocks.REDSTONE_BLOCK.getDefaultState());
        });

        world.setBlockState(new BlockPos(unvalidatedReactorBounds.getMinX(), unvalidatedReactorBounds.getMinY(), unvalidatedReactorBounds.getMinZ()), Blocks.GOLD_BLOCK.getDefaultState());
        world.setBlockState(new BlockPos(unvalidatedReactorBounds.getMaxX(), unvalidatedReactorBounds.getMaxY(), unvalidatedReactorBounds.getMaxZ()), Blocks.DIAMOND_BLOCK.getDefaultState());

        return true;
    }

    //----------------------------------------------------------------------------------------------------------------

    private int findDirectionalBound(BlockPos.Mutable pos, Direction direction) {
        int i = 1;
        boolean foundTop = false;

        while (!foundTop) {
            if (!isReactorBlock(world.getBlockState(pos.mutableCopy().move(direction, i)))) {
                foundTop = true;
            } else {
                i += 1;
            }
        }

        return i - 1;
    }

    //----------------------------------------------------------------------------------------------------------------

    private boolean validateSidesAndEdges(BlockBox blockBox) {
        int maxX = blockBox.getBlockCountX();
        int maxY = blockBox.getBlockCountY();
        int maxZ = blockBox.getBlockCountZ();

        BlockPos.Mutable minPos = new BlockPos.Mutable(blockBox.getMinX(), blockBox.getMinY(), blockBox.getMinZ()); //Gold Block

        boolean side1Check = validateSide(maxX, Direction.EAST, maxY, Direction.UP, minPos.mutableCopy());
        boolean side2Check = validateSide(maxZ, Direction.SOUTH, maxY, Direction.UP, minPos.mutableCopy());
        boolean side3Check = validateSide(maxZ, Direction.SOUTH, maxX, Direction.EAST, minPos.mutableCopy());

        BlockPos.Mutable maxPos = new BlockPos.Mutable(blockBox.getMaxX(), blockBox.getMaxY(), blockBox.getMaxZ());

        boolean side4Check = validateSide(maxX, Direction.WEST, maxY, Direction.DOWN, maxPos.mutableCopy());
        boolean side5Check = validateSide(maxZ, Direction.NORTH, maxY, Direction.DOWN, maxPos.mutableCopy());
        boolean side6Check = validateSide(maxZ, Direction.NORTH, maxX, Direction.WEST, maxPos.mutableCopy());

        return (side1Check && side2Check && side3Check && side4Check && side5Check && side6Check);
    }

    public boolean validateSide(int maxA, Direction aDirection, int maxB, Direction bDirection, BlockPos.Mutable startPos) {
        BlockPos.Mutable pos = startPos.mutableCopy();
        for (int a = 0; a < maxA; a++) {
            for (int b = 0; b < maxB; b++) {
                if (a == 0 || b == 0 || a == (maxA - 1) || b == (maxB - 1)) {
                    if (!isCasing(pos, world.getBlockState(pos))) {
                        return false;
                    }
                } else {
                    if (!isReactorBlock(pos, world.getBlockState(pos))) {
                        return false;
                    }
                }

                pos.move(bDirection);
            }

            pos = startPos.mutableCopy().move(aDirection, a + 1);
        }

        return true;
    }

    //----------------------------------------------------------------------------------------------------------------

    public boolean validateFuelRodsAndControllers(BlockBox blockBox) {
        int allowedRodHeight = blockBox.getBlockCountY() - 2;

        Set<ReactorFuelRodControllerBlockEntity> rodControllers = new HashSet<>();

        for (BlockPos rodControllerPos : cachedRodControllerPositions) {
            Set<BlockPos> fuelRodPositions = new HashSet<>();

            for (int i = 1; i <= allowedRodHeight; i++) {
                BlockPos possibleRod = rodControllerPos.down(i);

                BlockState state = world.getBlockState(possibleRod);

                if (state.isOf(MaldingBlocks.REACTOR_FUEL_ROD)) {
                    ReactorFuelRodBlockEntity reactorFuelRod = (ReactorFuelRodBlockEntity) world.getBlockEntity(possibleRod);

                    reactorFuelRod.setRodControllerPos(rodControllerPos);
                    reactorFuelRod.setControllerPos(this.controllerPos);

                    fuelRodPositions.add(possibleRod);
                } else {
                    return false;
                }
            }

            ((ReactorFuelRodControllerBlockEntity) world.getBlockEntity(rodControllerPos)).setAdjourningFuelRods(fuelRodPositions);
        }

        ((ReactorControllerBlockEntity) world.getBlockEntity(this.controllerPos)).setRodControllers(rodControllers);

        return true;
    }

    //----------------------------------------------------------------------------------------------------------------


    public boolean isCasing(BlockPos.Mutable pos, BlockState state) {
        if (state.isOf(MaldingBlocks.REACTOR_CASING)) {
            if (pos != null) {
                ((ReactorBaseBlockEntity) world.getBlockEntity(pos)).setControllerPos(this.controllerPos);
            }

            return true;
        }

        return state.isOf(Blocks.REDSTONE_BLOCK) || state.isOf(Blocks.GOLD_BLOCK) || state.isOf(Blocks.DIAMOND_BLOCK) || state.isOf(Blocks.ANCIENT_DEBRIS);
    }

    public boolean isReactorBlock(BlockState state) {
        return this.isReactorBlock(null, state);
    }

    public boolean isReactorBlock(BlockPos.Mutable pos, BlockState state) {
        if (state.isIn(MaldingTags.BASE_REACTOR_BLOCKS)) {
            if (pos != null) {
                //Check if the block is somehow a second controller block
                if (state.isOf(MaldingBlocks.REACTOR_CONTROLLER)) {
                    if (!compareBlockPos(pos, this.controllerPos)) {
                        return false;
                    }
                } else {
                    if (state.isOf(MaldingBlocks.REACTOR_FUEL_ROD_CONTROLLER)) {
                        cachedRodControllerPositions.add(pos.toImmutable());
                    }

                    ((ReactorBaseBlockEntity) world.getBlockEntity(pos)).setControllerPos(this.controllerPos);
                }
            }

            return true;
        }

        return isCasing(null, state);
    }

    /**
     * Safe way to compare BlockPos if you think one will be a mutable variant!
     *
     * @return if the block cords are both the same
     */
    private static boolean compareBlockPos(BlockPos pos1, BlockPos pos2) {
        return pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ();
    }
}
