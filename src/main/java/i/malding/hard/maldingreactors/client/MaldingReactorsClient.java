package i.malding.hard.maldingreactors.client;

import i.malding.hard.maldingreactors.client.screen.ReactorFuelRodControllerScreen;
import i.malding.hard.maldingreactors.client.screen.ReactorItemPortScreen;
import i.malding.hard.maldingreactors.client.screen.ReactorScreen;
import i.malding.hard.maldingreactors.content.AllFluids;
import i.malding.hard.maldingreactors.content.handlers.AllScreenHandlerTypes;
import me.alphamode.star.client.renderers.UpsideDownFluidRenderer;
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

        FluidRenderHandlerRegistry.INSTANCE.register(AllFluids.COOLANT.still(), AllFluids.COOLANT.flowing(), new UpsideDownFluidRenderer(0x00FFFF));
        FluidRenderHandlerRegistry.INSTANCE.register(AllFluids.STEAM.still(), AllFluids.STEAM.flowing(), new UpsideDownFluidRenderer(0xFFFFFF));

        FluidRenderHandlerRegistry.INSTANCE.register(AllFluids.YELLORIUM.still(), AllFluids.YELLORIUM.flowing(), SimpleFluidRenderHandler.coloredWater(0xFFFF00));
        FluidRenderHandlerRegistry.INSTANCE.register(AllFluids.CYANITE.still(), AllFluids.CYANITE.flowing(), SimpleFluidRenderHandler.coloredWater(0x273ff5));


        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                AllFluids.STEAM.still(), AllFluids.STEAM.flowing());
    }

    public void registerScreens() {
        HandledScreens.register(AllScreenHandlerTypes.REACTOR_CONTROLLER, ReactorScreen::new);
        HandledScreens.register(AllScreenHandlerTypes.REACTOR_ITEM_PORT, ReactorItemPortScreen::new);
        HandledScreens.register(AllScreenHandlerTypes.REACTOR_ROD_CONTROLLER, ReactorFuelRodControllerScreen::new);
    }
}
