package i.malding.hard.maldingreactors.content.handlers;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public class MaldingScreenHandlers implements AutoRegistryContainer<ScreenHandlerType<?>> {

    public static final ScreenHandlerType<ReactorScreenHandler> REACTOR_CONTROLLER = new ScreenHandlerType<>(ReactorScreenHandler::new);
    public static final ScreenHandlerType<ReactorItemPortScreenHandler> REACTOR_ITEM_PORT = new ScreenHandlerType<>(ReactorItemPortScreenHandler::new);
    public static final ScreenHandlerType<ReactorFuelRodControllerScreenHandler> REACTOR_ROD_CONTROLLER = new ScreenHandlerType<>(ReactorFuelRodControllerScreenHandler::new);

    @Override
    public Registry<ScreenHandlerType<?>> getRegistry() {
        return Registry.SCREEN_HANDLER;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<ScreenHandlerType<?>> getTargetFieldType() {
        return (Class<ScreenHandlerType<?>>) (Object) ScreenHandlerType.class;
    }
}
