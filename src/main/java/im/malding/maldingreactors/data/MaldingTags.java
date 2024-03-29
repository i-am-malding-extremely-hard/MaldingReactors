package im.malding.maldingreactors.data;

import im.malding.maldingreactors.MaldingReactors;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class MaldingTags {

    //-------------------------------------------

    public static final TagKey<Block> BASE_REACTOR_BLOCKS = createMaldingBlockTag("Base Reactor Blocks");

    public static final TagKey<Block> REACTOR_COMPONENT_BLOCKS = createMaldingBlockTag("Base Component Blocks");

    public static final TagKey<Block> REACTOR_CASING_BLOCKS = createMaldingBlockTag("Reactor Casing Blocks");


    //-------------------------------------------

    private static TagKey<Block> createMaldingBlockTag(String path) {
        return createBlockTag(MaldingReactors.id(path));
    }

    private static TagKey<Block> createBlockTag(Identifier identifier) {
        return TagKey.of(RegistryKeys.BLOCK, identifier);
    }

    //-------------------------------------------

    public static final TagKey<Item> REACTOR_FUEL = createMaldingItemTag("Reactor Fuel");

    //-------------------------------------------

    private static TagKey<Item> createMaldingItemTag(String path) {
        return createItemTag(MaldingReactors.id(path));
    }

    private static TagKey<Item> createItemTag(Identifier identifier) {
        return TagKey.of(RegistryKeys.ITEM, identifier);
    }
}
