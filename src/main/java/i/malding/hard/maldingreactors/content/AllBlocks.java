package i.malding.hard.maldingreactors.content;

import i.malding.hard.maldingreactors.MaldingReactors;
import i.malding.hard.maldingreactors.content.reactor.ReactorController;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class AllBlocks implements BlockRegistryContainer {
    public static Block REACTOR_CASING = new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK));
    public static Block REACTOR_GLASS_CASING = new GlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
    public static Block REACTOR_CONTROLLER = new ReactorController(FabricBlockSettings.copy(Blocks.IRON_BLOCK));

    public static Block YELLORITE_ORE = new OreBlock(FabricBlockSettings.copy(Blocks.IRON_ORE));

    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        return new BlockItem(block, new Item.Settings().group(MaldingReactors.REACTORS_TAB));
    }
}
