package im.malding.maldingreactors.content.handlers;

import im.malding.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import im.malding.maldingreactors.content.reactor.ReactorItemPortBlockEntity;
import im.malding.maldingreactors.data.MaldingTags;
import io.wispforest.owo.client.screens.ScreenUtils;
import io.wispforest.owo.client.screens.SlotGenerator;
import io.wispforest.owo.client.screens.ValidatingSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class ReactorItemPortScreenHandler extends ScreenHandler {

    public final ReactorItemPortBlockEntity reactorItemPort;

    public final SimpleInventory fuelSlot;
    public final SimpleInventory wasteSlot;

    private final ScreenHandlerContext context;

    public ReactorItemPortScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf) {
        this(syncId, ScreenHandlerContext.EMPTY, inv, buf, new SimpleInventory(1), new SimpleInventory(1));
    }

    public ReactorItemPortScreenHandler(int syncId, ScreenHandlerContext context, PlayerInventory inv, PacketByteBuf buf, SimpleInventory fuelSlot, SimpleInventory wasteSlot) {
        super(MaldingScreenHandlers.REACTOR_ITEM_PORT, syncId);

        this.context = context;

        this.fuelSlot = fuelSlot;
        this.wasteSlot = wasteSlot;

        addSlots(inv);

        this.reactorItemPort = (ReactorItemPortBlockEntity) inv.player.getWorld().getBlockEntity(buf.readBlockPos());
    }

    private void addSlots(PlayerInventory inv){
        this.addSlot(new ValidatingSlot(this.fuelSlot, 0, 0, 0,
                stack -> stack.isIn(MaldingTags.REACTOR_FUEL)));

        this.addSlot(new ValidatingSlot(this.wasteSlot, 0, 0, 0, stack -> false));

        SlotGenerator.begin(this::addSlot, 8, 60)
                .playerInventory(inv);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ScreenUtils.handleSlotTransfer(this, slot, 2);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, blockPos) -> {
            ReactorItemPortBlockEntity itemPort = (ReactorItemPortBlockEntity) world.getBlockEntity(blockPos);

            if (itemPort != null) {
                itemPort.fuelSlot.setStack(0, this.fuelSlot.getStack(0));
                itemPort.wasteSlot.setStack(0, this.wasteSlot.getStack(0));
            } else {
                this.dropInventory(player, this.fuelSlot);
                this.dropInventory(player, this.wasteSlot);
            }
        });
    }
}
