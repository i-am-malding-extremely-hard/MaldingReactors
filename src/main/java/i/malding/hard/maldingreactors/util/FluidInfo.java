package i.malding.hard.maldingreactors.util;

import i.malding.hard.maldingreactors.MaldingReactors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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
        still = Registry.register(Registry.FLUID, id, still);
        flowing = Registry.register(Registry.FLUID, id + "_flowing", flowing);
        block = Registry.register(Registry.BLOCK, id + "_fluid", new FluidBlock((FlowableFluid) still, FabricBlockSettings.copy(Blocks.WATER)));
        bucket = Registry.register(Registry.ITEM, id + "_bucket", new BucketItem(still, new FabricItemSettings().group(MaldingReactors.REACTORS_TAB)));
    }
}
