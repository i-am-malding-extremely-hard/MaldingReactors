package im.malding.maldingreactors.content.reactor;

import im.malding.maldingreactors.util.GuiUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ReactorFuelRodControllerBlock extends ReactorBaseBlock {

    public ReactorFuelRodControllerBlock(Settings settings) {
        super(settings, ReactorFuelRodControllerBlockEntity::new);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        final var factory = state.createScreenHandlerFactory(world, pos);

        if (player instanceof ServerPlayerEntity serverPlayer) {
            GuiUtil.openGui(serverPlayer, factory, (buf -> buf.writeBlockPos(pos)));
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        ReactorBaseBlockEntity portBlockEntity = (ReactorBaseBlockEntity) world.getBlockEntity(pos);

        return portBlockEntity != null && portBlockEntity.isFullMultipartStructure() ? super.createScreenHandlerFactory(state, world, pos) : null;
    }
}
