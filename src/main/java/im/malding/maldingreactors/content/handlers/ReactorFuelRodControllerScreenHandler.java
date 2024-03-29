package im.malding.maldingreactors.content.handlers;

import im.malding.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import im.malding.maldingreactors.content.reactor.ReactorFuelRodControllerBlockEntity;
import io.wispforest.owo.client.screens.ScreenUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class ReactorFuelRodControllerScreenHandler extends ScreenHandler {

    public final ReactorFuelRodControllerBlockEntity reactorFuelRodController;

    private final ScreenHandlerContext context;

    public ReactorFuelRodControllerScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf) {
        this(syncId, ScreenHandlerContext.EMPTY, inv, buf);
    }

    public ReactorFuelRodControllerScreenHandler(int syncId, ScreenHandlerContext context, PlayerInventory inv, PacketByteBuf buf) {
        super(MaldingScreenHandlers.REACTOR_ROD_CONTROLLER, syncId);

        this.context = context;

        this.reactorFuelRodController = (ReactorFuelRodControllerBlockEntity) inv.player.getWorld().getBlockEntity(buf.readBlockPos());
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ScreenUtils.handleSlotTransfer(this, slot, 0);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, blockPos) -> {
            ReactorFuelRodControllerBlockEntity itemPort = (ReactorFuelRodControllerBlockEntity) world.getBlockEntity(blockPos);

        });
    }
}
