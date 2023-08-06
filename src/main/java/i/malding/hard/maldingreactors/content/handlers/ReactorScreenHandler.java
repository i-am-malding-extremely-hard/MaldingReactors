package i.malding.hard.maldingreactors.content.handlers;

import i.malding.hard.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import io.wispforest.owo.client.screens.ScreenUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

public class ReactorScreenHandler extends ScreenHandler {

    public final ReactorControllerBlockEntity reactorControllerBlockEntity;

    public ReactorScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf) {
        super(MaldingScreenHandlers.REACTOR_CONTROLLER, syncId);
        this.reactorControllerBlockEntity = (ReactorControllerBlockEntity) inv.player.getWorld().getBlockEntity(buf.readBlockPos());
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ScreenUtils.handleSlotTransfer(this, slot, 0);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getInventory().canPlayerUse(player);
    }
}
