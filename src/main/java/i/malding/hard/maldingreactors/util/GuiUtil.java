package i.malding.hard.maldingreactors.util;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class GuiUtil {
    public static void openGui(ServerPlayerEntity serverPlayer, NamedScreenHandlerFactory factory, Consumer<PacketByteBuf> bufConsumer) {
        serverPlayer.openHandledScreen(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                bufConsumer.accept(buf);
            }

            @Override
            public Text getDisplayName() {
                return factory.getDisplayName();
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return factory.createMenu(syncId, inv, player);
            }
        });
    }
}
