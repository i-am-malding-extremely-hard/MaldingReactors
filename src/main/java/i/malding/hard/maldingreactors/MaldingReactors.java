package i.malding.hard.maldingreactors;

import i.malding.hard.maldingreactors.content.*;
import i.malding.hard.maldingreactors.content.worldgen.AllFeatures;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class MaldingReactors implements ModInitializer {

    public static OwoItemGroup REACTORS_TAB = new ReactorsItemGroup();

    public static String REACTORS_ID = "malding-reactors";

    @Override
    public void onInitialize() {
        FieldRegistrationHandler.register(AllBlocks.class, MaldingReactors.REACTORS_ID, false);
        FieldRegistrationHandler.register(AllBlockEntities.class, MaldingReactors.REACTORS_ID, false);
        FieldRegistrationHandler.register(AllItems.class, MaldingReactors.REACTORS_ID, false);
        FieldRegistrationHandler.processSimple(AllFluids.class, false);
        FieldRegistrationHandler.processSimple(AllFeatures.class, false);
        REACTORS_TAB.initialize();
    }

    public static Identifier asResource(String path) {
        return new Identifier(REACTORS_ID, path.toLowerCase().replace(' ', '_'));
    }
}
