package i.malding.hard.maldingreactors.util;

import i.malding.hard.maldingreactors.content.MaldingBlocks;
import i.malding.hard.maldingreactors.content.reactor.ReactorBaseBlockEntity;
import i.malding.hard.maldingreactors.data.MaldingTags;
import net.fabricmc.loader.api.FabricLoader;
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

    public static int MIN_REACTOR_SIZE = 3;
    public static int MAX_REACTOR_SIZE = 100;

    public final BlockPos controllerPos;
    public final World world;

    @Nullable
    private BlockBox bounds;

    public Set<BlockPos> rodControllers = new HashSet<>();
    public Set<BlockPos> fuelRods = new HashSet<>();

    public Set<BlockPos> itemPorts = new HashSet<>();
    public Set<BlockPos> powerPorts = new HashSet<>();

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

        BlockBox bounds = BlockBox.create(bottomLeft, topFarRight);

        boolean isValidBounds = isValidBounds(bounds.getBlockCountX())
                && isValidBounds(bounds.getBlockCountY())
                && isValidBounds(bounds.getBlockCountZ());

        //Must be a minimum of 3x3x3 blocks or a maximum of 100x100x100 blocks
        if (!isValidBounds) return false;

        //Validate that all sides and edges checkout
        if (!validateSidesAndEdges(bounds)) return false;

        //Check if there are either no rod controllers, power ports or item ports
        if (this.rodControllers.isEmpty() || this.powerPorts.isEmpty() || this.itemPorts.isEmpty()) return false;

        //Confirm that the rods for the reactor rod controllers are good
        if (!validateFuelRodsAndControllers(bounds)) return false;

        this.bounds = bounds;

        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
            bounds.forEachVertex(blockPos -> {
                System.out.println(blockPos);
                world.setBlockState(blockPos, Blocks.REDSTONE_BLOCK.getDefaultState());
            });

            world.setBlockState(new BlockPos(bounds.getMinX(), bounds.getMinY(), bounds.getMinZ()), Blocks.GOLD_BLOCK.getDefaultState());
            world.setBlockState(new BlockPos(bounds.getMaxX(), bounds.getMaxY(), bounds.getMaxZ()), Blocks.DIAMOND_BLOCK.getDefaultState());
        }

        return true;
    }

    private boolean isValidBounds(int i){
        return i >= MIN_REACTOR_SIZE || i <= MAX_REACTOR_SIZE;
    }

    //----------------------------------------------------------------------------------------------------------------

    private int findDirectionalBound(BlockPos startingPos, Direction direction) {
        BlockPos.Mutable endPos = startingPos.mutableCopy();

        while(isReactorBlock(world.getBlockState(endPos.move(direction))));

        int diff = switch (direction.getAxis()){
            case X -> endPos.getX() - startingPos.getX();
            case Y -> endPos.getY() - startingPos.getY();
            case Z -> endPos.getZ() - startingPos.getZ();
        };

        return (diff * direction.getDirection().offset()) - 1;
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

    public boolean validateSide(int maxA, Direction aDirection, int maxB, Direction bDirection, BlockPos startPos) {
        BlockPos.Mutable pos = startPos.mutableCopy();

        for (int a = 0; a < maxA; a++) {
            for (int b = 0; b < maxB; b++) {
                //Checks if we are on the perimeter of the square
                boolean valid = (a == 0 || b == 0 || a == (maxA - 1) || b == (maxB - 1))
                        ? isCasing(pos, world.getBlockState(pos))
                        : isReactorBlock(pos, world.getBlockState(pos));

                if(!valid) return false;

                pos.move(bDirection);
            }

            pos = startPos.mutableCopy().move(aDirection, a + 1);
        }

        return true;
    }

    //----------------------------------------------------------------------------------------------------------------

    public boolean validateFuelRodsAndControllers(BlockBox blockBox) {
        int allowedRodHeight = blockBox.getBlockCountY() - 2;

        for (BlockPos rodControllerPos : this.rodControllers) {
            for (int i = 1; i <= allowedRodHeight; i++) {
                BlockPos possibleRod = rodControllerPos.down(i);

                if(!isFuelRod(possibleRod, world.getBlockState(possibleRod))) return false;
            }
        }

        return true;
    }

    //----------------------------------------------------------------------------------------------------------------


    public boolean isCasing(BlockPos pos, BlockState state) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment() && !state.isOf(MaldingBlocks.REACTOR_CASING)) {
            return state.isOf(Blocks.REDSTONE_BLOCK) || state.isOf(Blocks.GOLD_BLOCK) || state.isOf(Blocks.DIAMOND_BLOCK) || state.isOf(Blocks.ANCIENT_DEBRIS);
        }

        if (pos != null) ((ReactorBaseBlockEntity) world.getBlockEntity(pos)).setControllerPos(this.controllerPos);

        return true;
    }

    public boolean isReactorBlock(BlockState state) {
        return this.isReactorBlock(null, state);
    }

    public boolean isReactorBlock(BlockPos pos, BlockState state) {
        boolean bl = state.isIn(MaldingTags.BASE_REACTOR_BLOCKS);

        if(!bl) return isCasing(null, state);

        if (pos != null) {
            //Check if the block is somehow a second controller block
            if (state.isOf(MaldingBlocks.REACTOR_CONTROLLER) && !compareBlockPos(pos, this.controllerPos)) return false;

            if (state.isOf(MaldingBlocks.REACTOR_FUEL_ROD_CONTROLLER)) {
                this.rodControllers.add(pos.toImmutable());
            } else if(state.isOf(MaldingBlocks.REACTOR_ITEM_PORT)){
                this.itemPorts.add(pos);
            } else if(state.isOf(MaldingBlocks.REACTOR_POWER_PORT)){
                this.powerPorts.add(pos);
            }

            ((ReactorBaseBlockEntity) world.getBlockEntity(pos)).setControllerPos(this.controllerPos);
        }

        return true;
    }

    public boolean isFuelRod(BlockPos pos, BlockState state){
        boolean isFuelRod = state.isOf(MaldingBlocks.REACTOR_FUEL_ROD);

        if(isFuelRod){
            this.fuelRods.add(pos);

            ((ReactorBaseBlockEntity) world.getBlockEntity(pos)).setControllerPos(this.controllerPos);
        }

        return isFuelRod;
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
