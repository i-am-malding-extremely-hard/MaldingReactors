package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import i.malding.hard.maldingreactors.util.GuiUtil;
import i.malding.hard.maldingreactors.util.ReactorValidator;
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

import java.util.Set;

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
        ReactorControllerBlockEntity controllerBlockEntity = ((ReactorControllerBlockEntity) world.getBlockEntity(pos));

        if (!world.isClient && hand == Hand.MAIN_HAND && !controllerBlockEntity.isValid()) {
            ReactorValidator validator = controllerBlockEntity.getValidator();

            boolean isMultipart = controllerBlockEntity.getValidator().validateReactor(state);

            if(isMultipart){
                controllerBlockEntity.rodControllers = Set.copyOf(validator.rodControllers);
                controllerBlockEntity.fuelRods = Set.copyOf(validator.fuelRods);

                controllerBlockEntity.itemPorts = Set.copyOf(validator.itemPorts);
                controllerBlockEntity.powerPorts = Set.copyOf(validator.powerPorts);
            } else {
                controllerBlockEntity.rodControllers.clear();
                controllerBlockEntity.fuelRods.clear();

                controllerBlockEntity.itemPorts.clear();
                controllerBlockEntity.powerPorts.clear();
            }

            controllerBlockEntity.setValid(isMultipart);
            controllerBlockEntity.markDirty();
        }

        final var factory = state.createScreenHandlerFactory(world, pos);

        if (player instanceof ServerPlayerEntity serverPlayer) {
            GuiUtil.openGui(serverPlayer, factory, (buf -> buf.writeBlockPos(pos)));
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    public <T extends BlockEntity> BlockEntityType<T> getType() {
        return (BlockEntityType<T>) MaldingBlockEntities.REACTOR_CONTROLLER;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(getType() != type) return null;

        return ((world1, pos, state1, blockEntity) -> {
            if (world1.isClient) {
                ((ReactorControllerBlockEntity) blockEntity).clientTick();
            } else {
                ((ReactorControllerBlockEntity) blockEntity).serverTick();
            }
        });
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
