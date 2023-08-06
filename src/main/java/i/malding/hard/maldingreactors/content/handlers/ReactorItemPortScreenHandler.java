package i.malding.hard.maldingreactors.content.handlers;

import i.malding.hard.maldingreactors.content.reactor.ReactorItemPortBlockEntity;
import i.malding.hard.maldingreactors.data.MaldingTags;
import io.wispforest.owo.client.screens.ScreenUtils;
import io.wispforest.owo.client.screens.SlotGenerator;
import io.wispforest.owo.client.screens.ValidatingSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class ReactorItemPortScreenHandler extends ScreenHandler {

    public final SimpleInventory fuelSlot;
    public final SimpleInventory wasteSlot;

    private final ScreenHandlerContext context;

    public ReactorItemPortScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, new SimpleInventory(1), new SimpleInventory(1));
    }

    public ReactorItemPortScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, SimpleInventory fuelSlot, SimpleInventory wasteSlot) {
        super(MaldingScreenHandlers.REACTOR_ITEM_PORT, syncId);

        this.context = context;

        this.fuelSlot = fuelSlot;
        this.wasteSlot = wasteSlot;

        this.addSlot(new ValidatingSlot(this.fuelSlot, 0, 0, 0,
                stack -> stack.isIn(MaldingTags.REACTOR_FUEL)));

        this.addSlot(new Slot(this.fuelSlot, 0, 0, 0));

        SlotGenerator.begin(this::addSlot, 8, 122)
                        .playerInventory(playerInventory);
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
