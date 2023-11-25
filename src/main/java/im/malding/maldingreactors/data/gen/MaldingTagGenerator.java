package im.malding.maldingreactors.data.gen;

import im.malding.maldingreactors.data.MaldingTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static im.malding.maldingreactors.content.MaldingBlocks.*;
import static im.malding.maldingreactors.content.MaldingItems.COPIUM_INGOT;

public class MaldingTagGenerator {

    public static class MaldingItemTagProvider extends ItemTagProvider {

        public MaldingItemTagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable BlockTagProvider blockTagProvider) {
            super(dataOutput, completableFuture, blockTagProvider);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            getOrCreateTagBuilder(MaldingTags.REACTOR_FUEL)
                    .add(COPIUM_INGOT);
        }
    }

    public static class MaldingBlockTagProvider extends FabricTagProvider.BlockTagProvider {

        public MaldingBlockTagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataOutput, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            getOrCreateTagBuilder(MaldingTags.BASE_REACTOR_BLOCKS)
                    .add(REACTOR_CASING,
                            REACTOR_GLASS,
                            REACTOR_CONTROLLER,
                            REACTOR_ITEM_PORT,
                            REACTOR_POWER_PORT,
                            REACTOR_FUEL_ROD_CONTROLLER);

            getOrCreateTagBuilder(MaldingTags.REACTOR_COMPONENT_BLOCKS)
                    .add(REACTOR_CASING,
                            REACTOR_GLASS,
                            REACTOR_ITEM_PORT,
                            REACTOR_POWER_PORT,
                            REACTOR_FUEL_ROD_CONTROLLER);
        }
    }
}
