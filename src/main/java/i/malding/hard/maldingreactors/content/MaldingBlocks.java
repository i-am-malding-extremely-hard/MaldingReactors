package i.malding.hard.maldingreactors.content;

import i.malding.hard.maldingreactors.MaldingReactors;
import i.malding.hard.maldingreactors.content.reactor.*;
import i.malding.hard.maldingreactors.util.Tab;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

public class MaldingBlocks implements BlockRegistryContainer {

    @Tab(0)
    public static final Block REACTOR_CASING = new ReactorBaseBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK), BlockEntityFactory.typed(() -> MaldingBlockEntities.REACTOR_CASING, ReactorBaseBlockEntity::new));
    public static final Block REACTOR_GLASS = new ReactorGlassBlock(FabricBlockSettings.copy(Blocks.GLASS));

    public static final Block REACTOR_FUEL_ROD = new ReactorBaseBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK), BlockEntityFactory.typed(() -> MaldingBlockEntities.REACTOR_FUEL_ROD, ReactorBaseBlockEntity::new), BlockRenderType.ENTITYBLOCK_ANIMATED);

    public static final Block REACTOR_FUEL_ROD_CONTROLLER = new ReactorFuelRodControllerBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK));

    public static final Block REACTOR_CONTROLLER = new ReactorControllerBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK));
    public static final Block REACTOR_ITEM_PORT = new ReactorItemPortBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK));
    public static final Block REACTOR_POWER_PORT = new ReactorPowerPortBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK));

    @Tab(1)
    public static final Block COPIUM_ORE = new ExperienceDroppingBlock(FabricBlockSettings.copy(Blocks.IRON_ORE));

    //---------------------------------------------------------------------------------

    private static int cachedTab = 1;

    @Override
    public void postProcessField(String namespace, Block value, String identifier, Field field) {
        if (field.isAnnotationPresent(NoBlockItem.class)) return;

        if (field.isAnnotationPresent(Tab.class)) cachedTab = field.getAnnotation(Tab.class).value();

        Registry.register(Registries.ITEM, new Identifier(namespace, identifier),
                new BlockItem(value, new OwoItemSettings().tab(cachedTab).group(MaldingReactors.GROUP)));
    }

}
