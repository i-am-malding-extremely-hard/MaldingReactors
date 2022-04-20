package i.malding.hard.maldingreactors.util;

import com.google.common.collect.Lists;
import i.malding.hard.maldingreactors.content.AllBlocks;
import i.malding.hard.maldingreactors.content.reactor.ReactorController;
import i.malding.hard.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import i.malding.hard.maldingreactors.data.MaldingTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class ReactorUtil {

    public static int MAX_REACTOR_SIZE = 100;

    public static boolean checkReactorStructure(BlockState controllerState, BlockPos controllerPos, World world) {
        Direction facing = controllerState.get(Properties.FACING);
        BlockPos.Mutable pos = new BlockPos.Mutable(controllerPos.getX(), controllerPos.getY(), controllerPos.getZ());
        for (int i = 0; i < MAX_REACTOR_SIZE; i++) {
            pos.setY(pos.getY() - 1);
            if(isCasing(world.getBlockState(pos))) {
                facing = facing.rotateYClockwise();
                Pair<BlockPos, BlockPos> ends = getRow(world, pos, facing);

                List<BlockPos> current = Lists.newArrayList(ends.getLeft());
                for (int j = 0; j < 4; j++) {
                    facing = facing.rotateYClockwise();
                    current.add(findRowEnd(world, current.get(j), facing));
                    world.setBlockState(current.get(j), Blocks.REDSTONE_BLOCK.getDefaultState());
                }
                if(ends.getLeft().equals(current.get(current.size() - 1)))
                for (int height = 0; height < MAX_REACTOR_SIZE; height++) {
                    if(!(isCasing(world.getBlockState(current.get(0).offset(Direction.UP, height)))
                            && isCasing(world.getBlockState(current.get(1).offset(Direction.UP, height)))
                            && isCasing(world.getBlockState(current.get(2).offset(Direction.UP, height)))
                            && isCasing(world.getBlockState(current.get(3).offset(Direction.UP, height))))) {
                        world.setBlockState(current.get(0).offset(Direction.UP, height - 1), Blocks.REDSTONE_BLOCK.getDefaultState());
                        world.setBlockState(current.get(1).offset(Direction.UP, height - 1), Blocks.REDSTONE_BLOCK.getDefaultState());
                        world.setBlockState(current.get(2).offset(Direction.UP, height - 1), Blocks.REDSTONE_BLOCK.getDefaultState());
                        world.setBlockState(current.get(3).offset(Direction.UP, height - 1), Blocks.REDSTONE_BLOCK.getDefaultState());
                        for (int j = 0; j < 4; j++) {
                            facing = facing.rotateYClockwise();
                            current.add(findRowEnd(world, current.get(j), facing));
                            world.setBlockState(current.get(j), Blocks.REDSTONE_BLOCK.getDefaultState());
                        }
                        ((ReactorControllerBlockEntity)world.getBlockEntity(controllerPos)).setMultiBlockCheck(true);
                        break;
                    }

                }
                break;
            }
        }
        return false;
    }

    public static BlockPos findRowEnd(World world, BlockPos startPos, Direction direction) {
        BlockPos pos = new BlockPos.Mutable(startPos.getX(), startPos.getY(), startPos.getZ());
        for (int i = 0; i < MAX_REACTOR_SIZE; i++) {
            pos = pos.offset(direction, 1);
            if(!isCasing(world.getBlockState(pos))) {
                return pos.offset(direction.getOpposite(), 1);
            }
        }
        return startPos;
    }

    public static Pair<BlockPos, BlockPos> getRow(World world, BlockPos startPos, Direction direction) {
        BlockPos start = startPos;
        BlockPos end = startPos;
        BlockPos pos = new BlockPos.Mutable(startPos.getX(), startPos.getY(), startPos.getZ());
        for (int i = 0; i < MAX_REACTOR_SIZE; i++) {
            pos = pos.offset(direction, 1);
            if(!isCasing(world.getBlockState(pos))) {
                start = pos.offset(direction, -1);
                end = start;
                break;
            }
        }
        for (int i = 0; i < MAX_REACTOR_SIZE; i++) {
            end = end.offset(direction.getOpposite(), 1);
            if(!isCasing(world.getBlockState(end))) {
                end = end.offset(direction.getOpposite(), -1);
                break;
            }
        }
        return new Pair<>(start, end);
    }

    public static boolean isCasing(BlockState state) {
        return state.isOf(AllBlocks.REACTOR_CASING) || state.isOf(Blocks.REDSTONE_BLOCK);
    }

    public static boolean isReactorBlock(BlockState state){
        return state.isIn(MaldingTags.BASE_REACTOR_BLOCKS);
    }
}
