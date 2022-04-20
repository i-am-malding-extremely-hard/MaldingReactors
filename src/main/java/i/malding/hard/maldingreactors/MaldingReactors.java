package i.malding.hard.maldingreactors;

import i.malding.hard.maldingreactors.content.*;
import i.malding.hard.maldingreactors.content.worldgen.MaldingFeatures;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.network.OwoNetChannel;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class MaldingReactors implements ModInitializer {

    public static OwoItemGroup REACTORS_TAB = new ReactorsItemGroup();

    public static String REACTORS_ID = "malding-reactors";

    public static final OwoNetChannel MAIN = OwoNetChannel.create(asResource("main"));

    @Override
    public void onInitialize() {
        FieldRegistrationHandler.register(MaldingBlocks.class, MaldingReactors.REACTORS_ID, false);
        FieldRegistrationHandler.register(MaldingBlockEntities.class, MaldingReactors.REACTORS_ID, false);
        FieldRegistrationHandler.register(MaldingItems.class, MaldingReactors.REACTORS_ID, false);
        FieldRegistrationHandler.processSimple(MaldingFluids.class, false);
        FieldRegistrationHandler.processSimple(MaldingFeatures.class, false);
        REACTORS_TAB.initialize();

        registerNetworkPackets();
    }

    public static Identifier asResource(String path) {
        return new Identifier(REACTORS_ID, path.toLowerCase().replace(' ', '_'));
    }

    public static void registerNetworkPackets() {
        //MAIN.registerClientbound(MultiBlockUpdatePacket.class, MultiBlockUpdatePacket::setContollersMultipartState);
    }
}
