package i.malding.hard.maldingreactors.content;

import i.malding.hard.maldingreactors.MaldingReactors;
import i.malding.hard.maldingreactors.content.reactor.ReactorControllerBlock;
import i.malding.hard.maldingreactors.content.reactor.ReactorFuelRodBlock;
import i.malding.hard.maldingreactors.content.reactor.ReactorFuelRodControllerBlock;
import i.malding.hard.maldingreactors.content.reactor.ReactorItemPortBlock;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class MaldingBlocks implements BlockRegistryContainer {

    @Tab(0)
    public static Block REACTOR_CASING = new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK));
    public static Block REACTOR_GLASS = new GlassBlock(FabricBlockSettings.copy(Blocks.GLASS));

    public static Block REACTOR_FUEL_ROD = new ReactorFuelRodBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK));
    public static Block REACTOR_FUEL_ROD_CONTROLLER = new ReactorFuelRodControllerBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK));

    public static Block REACTOR_CONTROLLER = new ReactorControllerBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK));
    public static Block REACTOR_ITEM_PORT = new ReactorItemPortBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK));

    @Tab(1)
    public static Block YELLORITE_ORE = new OreBlock(FabricBlockSettings.copy(Blocks.IRON_ORE));

    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        return new BlockItem(block, new Item.Settings().group(MaldingReactors.REACTORS_TAB));
    }

    //---------------------------------------------------------------------------------

    private static int cachedTab = 1;

    @Override
    public void postProcessField(String namespace, Block value, String identifier, Field field) {
        if (field.isAnnotationPresent(NoBlockItem.class)) return;

        int tab;

        if (field.isAnnotationPresent(Tab.class)) {
            tab = field.getAnnotation(Tab.class).value();
        } else {
            tab = cachedTab;
        }

        cachedTab = tab;

        Registry.register(Registry.ITEM, new Identifier(namespace, identifier),
                new BlockItem(value, new OwoItemSettings().tab(tab).group(MaldingReactors.REACTORS_TAB)));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface Tab {
        int value();
    }
}
