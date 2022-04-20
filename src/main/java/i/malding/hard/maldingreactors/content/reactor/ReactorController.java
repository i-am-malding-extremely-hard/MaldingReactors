package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.AllFluids;
import i.malding.hard.maldingreactors.data.MaldingTags;
import i.malding.hard.maldingreactors.util.ReactorUtil;
import io.wispforest.owo.network.ClientAccess;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class ReactorController extends Block implements BlockEntityProvider {
    public ReactorController(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(Properties.FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorControllerBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ReactorControllerBlockEntity controllerBlock = ((ReactorControllerBlockEntity) world.getBlockEntity(pos));

        if (!world.isClient && hand == Hand.MAIN_HAND && !controllerBlock.isMultiBlockStructure()) {
            boolean isMultipart = ReactorUtil.checkReactorStructure(state, pos, world);

            controllerBlock.setMultiBlockCheck(true);
            controllerBlock.markDirty();
        }

        ReactorControllerBlockEntity controllerBlockEntity = (ReactorControllerBlockEntity) world.getBlockEntity(pos);

        if (controllerBlockEntity != null && controllerBlockEntity.isMultiBlockStructure()) {
            if (player.shouldCancelInteraction()) {
                return ReactorItemPort.extractWasteAmount(world, pos, player);
            } else {
                return ReactorItemPort.insertFuelAmount(world, pos, player, hand);
            }
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return ((world1, pos, state1, blockEntity) -> ((ReactorControllerBlockEntity) blockEntity).tick());
    }

    public record MultiBlockUpdatePacket(BlockPos pos, boolean isMultiPart) {

        public static void setContollersMultipartState(MultiBlockUpdatePacket packet, ClientAccess access) {
            ReactorControllerBlockEntity controllerBlockEntity = (ReactorControllerBlockEntity) access.runtime().world.getBlockEntity(packet.pos());

            if (controllerBlockEntity != null) {
                controllerBlockEntity.setMultiBlockCheck(packet.isMultiPart());
            }
        }
    }
}
