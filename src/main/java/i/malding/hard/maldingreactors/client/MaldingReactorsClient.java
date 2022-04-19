package i.malding.hard.maldingreactors.client;

import i.malding.hard.maldingreactors.content.AllFluids;
import me.alphamode.star.client.renderers.UpsideDownFluidRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;

@Environment(EnvType.CLIENT)
public class MaldingReactorsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FluidRenderHandlerRegistry.INSTANCE.register(AllFluids.COOLANT.still(), AllFluids.COOLANT.flowing(), new UpsideDownFluidRenderer());
    }
}
