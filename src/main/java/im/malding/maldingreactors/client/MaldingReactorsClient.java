package im.malding.maldingreactors.client;

import im.malding.maldingreactors.client.ui.screen.ReactorFuelRodControllerScreen;
import im.malding.maldingreactors.client.ui.screen.ReactorItemPortScreen;
import im.malding.maldingreactors.client.ui.screen.ReactorScreen;
import im.malding.maldingreactors.content.MaldingBlocks;
import im.malding.maldingreactors.content.MaldingFluids;
import im.malding.maldingreactors.content.handlers.MaldingScreenHandlers;
//import me.alphamode.star.client.renderers.UpsideDownFluidRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class MaldingReactorsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerScreens();

        FluidRenderHandlerRegistry.INSTANCE.register(MaldingFluids.COOLANT.still(), MaldingFluids.COOLANT.flowing(), SimpleFluidRenderHandler.coloredWater(0x00FFFF) /*new UpsideDownFluidRenderer(0x00FFFF)*/);
        FluidRenderHandlerRegistry.INSTANCE.register(MaldingFluids.STEAM.still(), MaldingFluids.STEAM.flowing(), SimpleFluidRenderHandler.coloredWater(0xFFFFFF) /*new UpsideDownFluidRenderer(0xFFFFFF)*/);

        FluidRenderHandlerRegistry.INSTANCE.register(MaldingFluids.COPIUM.still(), MaldingFluids.COPIUM.flowing(), SimpleFluidRenderHandler.coloredWater(0x921c36));
        FluidRenderHandlerRegistry.INSTANCE.register(MaldingFluids.MALDING_COPIUM.still(), MaldingFluids.MALDING_COPIUM.flowing(), SimpleFluidRenderHandler.coloredWater(0x49396d));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                MaldingFluids.STEAM.still(), MaldingFluids.STEAM.flowing());

        BlockRenderLayerMap.INSTANCE.putBlock(MaldingBlocks.REACTOR_GLASS, RenderLayer.getTranslucent());
    }

    public void registerScreens() {
        HandledScreens.register(MaldingScreenHandlers.REACTOR_CONTROLLER, ReactorScreen::new);
        HandledScreens.register(MaldingScreenHandlers.REACTOR_ITEM_PORT, ReactorItemPortScreen::new);
        HandledScreens.register(MaldingScreenHandlers.REACTOR_ROD_CONTROLLER, ReactorFuelRodControllerScreen::new);
    }
}
