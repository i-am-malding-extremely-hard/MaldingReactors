package i.malding.hard.maldingreactors.util;

import i.malding.hard.maldingreactors.MaldingReactors;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FluidInfo {

    private Fluid still, flowing;
    private Item bucket;
    private Block block;

    public FluidInfo(Fluid still, Fluid flowing) {
        this.still = still;
        this.flowing = flowing;
    }

    public Fluid still() {
        return still;
    }

    public Fluid flowing() {
        return flowing;
    }

    public Item bucket() {
        return bucket;
    }

    public Block block() {
        return block;
    }

    public void register(Identifier id) {
        still = Registry.register(Registries.FLUID, id, still);
        flowing = Registry.register(Registries.FLUID, id + "_flowing", flowing);
        block = Registry.register(Registries.BLOCK, id + "_fluid", new FluidBlock((FlowableFluid) still, FabricBlockSettings.copy(Blocks.WATER)));
        bucket = Registry.register(Registries.ITEM, id + "_bucket", new BucketItem(still, new OwoItemSettings().group(MaldingReactors.GROUP)));
    }
}
