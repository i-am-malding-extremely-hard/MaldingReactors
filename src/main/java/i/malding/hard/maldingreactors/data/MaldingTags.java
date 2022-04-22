package i.malding.hard.maldingreactors.data;

import i.malding.hard.maldingreactors.MaldingReactors;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaldingTags {

    //-------------------------------------------

    public static final TagKey<Block> BASE_REACTOR_BLOCKS = createMaldingBlockTag("Base Reactor Blocks");

    public static final TagKey<Block> REACTOR_COMPONENT_BLOCKS = createMaldingBlockTag("Base Component Blocks");

    public static final TagKey<Block> REACTOR_CASING_BLOCKS = createMaldingBlockTag("Reactor Casing Blocks");


    //-------------------------------------------

    private static TagKey<Block> createMaldingBlockTag(String path) {
        return createBlockTag(MaldingReactors.asResource(path));
    }

    private static TagKey<Block> createBlockTag(Identifier identifier) {
        return TagKey.of(Registry.BLOCK_KEY, identifier);
    }

    //-------------------------------------------

    public static final TagKey<Item> REACTOR_FUEL = createMaldingItemTag("Reactor Fuel");

    //-------------------------------------------

    private static TagKey<Item> createMaldingItemTag(String path) {
        return createItemTag(MaldingReactors.asResource(path));
    }

    private static TagKey<Item> createItemTag(Identifier identifier) {
        return TagKey.of(Registry.ITEM_KEY, identifier);
    }
}
