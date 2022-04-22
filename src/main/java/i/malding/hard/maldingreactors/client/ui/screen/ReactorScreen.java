package i.malding.hard.maldingreactors.client.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import i.malding.hard.maldingreactors.client.MaldingTextures;
import i.malding.hard.maldingreactors.client.ui.MaldingButton;
import i.malding.hard.maldingreactors.content.handlers.ReactorScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@SuppressWarnings("UnstableApiUsage")
public class ReactorScreen extends HandledScreen<ReactorScreenHandler> {

    public ReactorScreen(ReactorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, new TranslatableText(""));
        this.backgroundWidth = 224;
    }

    @Override
    protected void init() {
        super.init();
        final ButtonWidget.PressAction noop = button -> {};

        this.addDrawableChild(new MaldingButton(this.x + 10, this.y + 40, 30, 25, Text.of("very epic"), noop));
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, MaldingTextures.REACTOR_BACKGROUND);
        drawTexture(matrices, this.x, this.y, 0, 0, backgroundWidth, backgroundHeight);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        textRenderer.draw(matrices, "Fuel: " + handler.reactorControllerBlockEntity.getFuelTank().getAmount(), this.x + 10, this.y + 90, 0xFFFFFF);
        textRenderer.draw(matrices, "Waste: " + handler.reactorControllerBlockEntity.getWasteTank().getAmount(), this.x + 10, this.y + 100, 0xFFFFFF);
        textRenderer.draw(matrices, "Energy: " + handler.reactorControllerBlockEntity.getEnergyStorage().getAmount(), this.x + 10, this.y + 110, 0xFFFFFF);
    }
}
