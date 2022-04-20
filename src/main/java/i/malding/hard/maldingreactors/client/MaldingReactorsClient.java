package i.malding.hard.maldingreactors.client;

import i.malding.hard.maldingreactors.content.handlers.AllScreenHandlerTypes;
import i.malding.hard.maldingreactors.client.screen.ReactorItemPortScreen;
import i.malding.hard.maldingreactors.client.screen.ReactorScreen;
import i.malding.hard.maldingreactors.content.AllFluids;
import me.alphamode.star.client.renderers.UpsideDownFluidRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class MaldingReactorsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerScreens();

        FluidRenderHandlerRegistry.INSTANCE.register(AllFluids.COOLANT.still(), AllFluids.COOLANT.flowing(), new UpsideDownFluidRenderer());
    }

    public void registerScreens(){
        HandledScreens.register(AllScreenHandlerTypes.REACTOR_CONTROLLER, ReactorScreen::new);
        HandledScreens.register(AllScreenHandlerTypes.REACTOR_ITEM_PORT, ReactorItemPortScreen::new);
    }
}
