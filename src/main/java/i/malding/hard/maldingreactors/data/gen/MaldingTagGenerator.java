package i.malding.hard.maldingreactors.data.gen;

import i.malding.hard.maldingreactors.content.AllItems;
import i.malding.hard.maldingreactors.data.MaldingTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;

public class MaldingTagGenerator extends ItemTagProvider {
    public MaldingTagGenerator(FabricDataGenerator dataGenerator) {
        super(dataGenerator, null);
    }

    @Override
    protected void generateTags() {
        tag(MaldingTags.REACTOR_FUEL)
                .add(AllItems.YELLORIUM);
    }

    // Yarn is bad
    public FabricTagBuilder<Item> tag(TagKey<Item> tag) {
        return getOrCreateTagBuilder(tag);
    }
}
