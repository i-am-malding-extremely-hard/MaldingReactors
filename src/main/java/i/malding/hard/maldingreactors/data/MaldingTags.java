package i.malding.hard.maldingreactors.data;

import i.malding.hard.maldingreactors.MaldingReactors;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class MaldingTags {
    public static final TagKey<Item> REACTOR_FUEL = TagKey.of(Registry.ITEM_KEY, MaldingReactors.asResource("Reactor Fuel"));
}
