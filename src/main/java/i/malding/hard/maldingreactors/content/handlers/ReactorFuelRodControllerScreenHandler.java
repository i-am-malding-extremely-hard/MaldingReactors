package i.malding.hard.maldingreactors.content.handlers;

import i.malding.hard.maldingreactors.content.reactor.ReactorFuelRodController;
import i.malding.hard.maldingreactors.content.reactor.ReactorFuelRodControllerBlockEntity;
import i.malding.hard.maldingreactors.content.reactor.ReactorItemPortBlockEntity;
import i.malding.hard.maldingreactors.data.MaldingTags;
import io.wispforest.owo.client.screens.ScreenUtils;
import io.wispforest.owo.client.screens.ValidatingSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class ReactorFuelRodControllerScreenHandler extends ScreenHandler {

    private final ScreenHandlerContext context;

    public ReactorFuelRodControllerScreenHandler(int syncId){
        this(syncId, ScreenHandlerContext.EMPTY);
    }

    public ReactorFuelRodControllerScreenHandler(int syncId, ScreenHandlerContext context){
        super(AllScreenHandlerTypes.REACTOR_ROD_CONTROLLER, syncId);

        this.context = context;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, blockPos) -> {
            ReactorFuelRodControllerBlockEntity itemPort = (ReactorFuelRodControllerBlockEntity) world.getBlockEntity(blockPos);

        });
    }
}
