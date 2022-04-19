package i.malding.hard.maldingreactors.content;

import i.malding.hard.maldingreactors.content.reactor.ReactorScreenHandler;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public class AllScreenTypes implements AutoRegistryContainer<ScreenHandlerType<?>> {
    public static final ScreenHandlerType<ReactorScreenHandler> REACTOR_CONTROLLER = new ScreenHandlerType<>(ReactorScreenHandler::new);

    @Override
    public Registry<ScreenHandlerType<?>> getRegistry() {
        return Registry.SCREEN_HANDLER;
    }

    @Override
    public Class<ScreenHandlerType<?>> getTargetFieldType() {
        return (Class<ScreenHandlerType<?>>) (Object) ScreenHandlerType.class;
    }
}
