package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import i.malding.hard.maldingreactors.util.GuiUtil;
import io.wispforest.owo.network.ClientAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ReactorControllerBlock extends ReactorSingleFaceBlock {

    public ReactorControllerBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorControllerBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ReactorControllerBlockEntity controllerBlock = ((ReactorControllerBlockEntity) world.getBlockEntity(pos));

        if (!world.isClient && hand == Hand.MAIN_HAND) {
            boolean isMultipart = controllerBlock.getValidator().validateReactor(state);

            controllerBlock.setValid(true);
            controllerBlock.markDirty();
        }

        ReactorControllerBlockEntity controllerBlockEntity = (ReactorControllerBlockEntity) world.getBlockEntity(pos);

        final var factory = state.createScreenHandlerFactory(world, pos);
        if (player instanceof ServerPlayerEntity serverPlayer) {
            GuiUtil.openGui(serverPlayer, factory, (buf -> buf.writeBlockPos(pos)));
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, MaldingBlockEntities.REACTOR_CONTROLLER,
                ((world1, pos, state1, blockEntity) -> {
                    if (world1.isClient) {
                        ((ReactorControllerBlockEntity) blockEntity).clientTick();
                    } else {
                        ((ReactorControllerBlockEntity) blockEntity).serverTick();
                    }
                }));
    }

    public record MultiBlockUpdatePacket(BlockPos pos, boolean isMultiPart) {

        public static void setControllersMultipartState(MultiBlockUpdatePacket packet, ClientAccess access) {
            ReactorControllerBlockEntity controllerBlockEntity = (ReactorControllerBlockEntity) access.runtime().world.getBlockEntity(packet.pos());

            if (controllerBlockEntity != null) {
                controllerBlockEntity.setValid(packet.isMultiPart());
            }
        }
    }
}
