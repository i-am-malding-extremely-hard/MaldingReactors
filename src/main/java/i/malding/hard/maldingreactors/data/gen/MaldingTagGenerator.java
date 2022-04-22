package i.malding.hard.maldingreactors.data.gen;

import i.malding.hard.maldingreactors.data.MaldingTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import static i.malding.hard.maldingreactors.content.MaldingItems.*;
import static i.malding.hard.maldingreactors.content.MaldingBlocks.*;

public class MaldingTagGenerator {

    public static class MaldingItemTagProvider extends ItemTagProvider {

        public MaldingItemTagProvider(FabricDataGenerator dataGenerator, @Nullable BlockTagProvider blockTagProvider) {
            super(dataGenerator, blockTagProvider);
        }

        @Override
        protected void generateTags() {
            tag(MaldingTags.REACTOR_FUEL)
                    .add(YELLORIUM_INGOT);
        }

        // Yarn is bad
        public FabricTagBuilder<Item> tag(TagKey<Item> tag) {
            return getOrCreateTagBuilder(tag);
        }
    }

    public static class MaldingBlockTagProvider extends FabricTagProvider.BlockTagProvider {

        public MaldingBlockTagProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateTags() {
            tag(MaldingTags.BASE_REACTOR_BLOCKS)
                    .add(REACTOR_CASING,
                            REACTOR_GLASS,
                            REACTOR_CONTROLLER,
                            REACTOR_ITEM_PORT,
                            REACTOR_POWER_PORT,
                            REACTOR_FUEL_ROD_CONTROLLER);

            tag(MaldingTags.REACTOR_COMPONENT_BLOCKS)
                    .add(REACTOR_ITEM_PORT,
                            REACTOR_POWER_PORT,
                            REACTOR_FUEL_ROD_CONTROLLER);
        }

        // Yarn is bad
        public FabricTagBuilder<Block> tag(TagKey<Block> tag) {
            return getOrCreateTagBuilder(tag);
        }
    }
}
