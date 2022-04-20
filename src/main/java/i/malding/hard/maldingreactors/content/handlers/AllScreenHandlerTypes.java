package i.malding.hard.maldingreactors.content.handlers;

import i.malding.hard.maldingreactors.content.handlers.ReactorItemPortScreenHandler;
import i.malding.hard.maldingreactors.content.handlers.ReactorScreenHandler;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public class AllScreenHandlerTypes implements AutoRegistryContainer<ScreenHandlerType<?>> {

    public static final ScreenHandlerType<ReactorScreenHandler> REACTOR_CONTROLLER = new ScreenHandlerType<>(ReactorScreenHandler::new);
    public static final ScreenHandlerType<ReactorItemPortScreenHandler> REACTOR_ITEM_PORT = new ScreenHandlerType<>(ReactorItemPortScreenHandler::new);

    @Override
    public Registry<ScreenHandlerType<?>> getRegistry() {
        return Registry.SCREEN_HANDLER;
    }

    @Override
    public Class<ScreenHandlerType<?>> getTargetFieldType() {
        return (Class<ScreenHandlerType<?>>) (Object) ScreenHandlerType.class;
    }
}
