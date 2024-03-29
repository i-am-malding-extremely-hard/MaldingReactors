package im.malding.maldingreactors.content.handlers;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class MaldingScreenHandlers implements AutoRegistryContainer<ScreenHandlerType<?>> {

    public static final ScreenHandlerType<ReactorScreenHandler> REACTOR_CONTROLLER = new ExtendedScreenHandlerType<>(ReactorScreenHandler::new);
    public static final ScreenHandlerType<ReactorItemPortScreenHandler> REACTOR_ITEM_PORT = new ExtendedScreenHandlerType<>(ReactorItemPortScreenHandler::new);
    public static final ScreenHandlerType<ReactorFuelRodControllerScreenHandler> REACTOR_ROD_CONTROLLER = new ExtendedScreenHandlerType<>(ReactorFuelRodControllerScreenHandler::new);

    @Override
    public Registry<ScreenHandlerType<?>> getRegistry() {
        return Registries.SCREEN_HANDLER;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<ScreenHandlerType<?>> getTargetFieldType() {
        return (Class<ScreenHandlerType<?>>) (Object) ScreenHandlerType.class;
    }
}
