package i.malding.hard.maldingreactors.content;

import i.malding.hard.maldingreactors.MaldingReactors;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.minecraft.item.ItemStack;

public class ReactorsItemGroup extends OwoItemGroup {
    public ReactorsItemGroup() {
        super(MaldingReactors.asResource("Reactors Item Group"));
    }

    @Override
    protected void setup() {
        addTab(Icon.of(MaldingBlocks.COPIUM_ORE), "reactors", null);
        addTab(Icon.of(MaldingItems.COPIUM_INGOT), "resources", null);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(MaldingBlocks.REACTOR_CASING);
    }
}
