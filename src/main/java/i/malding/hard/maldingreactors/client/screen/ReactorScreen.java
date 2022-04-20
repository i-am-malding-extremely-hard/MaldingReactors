package i.malding.hard.maldingreactors.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import i.malding.hard.maldingreactors.client.AllTextures;
import i.malding.hard.maldingreactors.content.handlers.ReactorScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ReactorScreen extends HandledScreen<ReactorScreenHandler> {
    public ReactorScreen(ReactorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, new TranslatableText(""));
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, AllTextures.REACTOR_BACKGROUND);
        drawTexture(matrices, this.x, this.y, 0, 0, backgroundWidth, backgroundHeight);
    }
}
