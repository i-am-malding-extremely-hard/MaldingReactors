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

    //-------------------------------------------

    private static TagKey<Block> createMaldingBlockTag(String nameSpace){
        return createBlockTag(MaldingReactors.asResource(nameSpace));
    }

    private static TagKey<Block> createBlockTag(Identifier identifier){
        return TagKey.of(Registry.BLOCK_KEY, identifier);
    }

    //-------------------------------------------

    public static final TagKey<Item> REACTOR_FUEL = createMaldingItemTag("Reactor Fuel");

    //-------------------------------------------

    private static TagKey<Item> createMaldingItemTag(String nameSpace){
        return createItemTag(MaldingReactors.asResource(nameSpace));
    }

    private static TagKey<Item> createItemTag(Identifier identifier){
        return TagKey.of(Registry.ITEM_KEY, identifier);
    }
}
