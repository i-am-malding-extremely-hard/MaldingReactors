package i.malding.hard.maldingreactors.content;

import i.malding.hard.maldingreactors.MaldingReactors;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.minecraft.item.Item;

public class MaldingItems implements ItemRegistryContainer {
    public static final Item COPIUM_INGOT = new Item(new OwoItemSettings().tab(1).group(MaldingReactors.GROUP));
    public static final Item MALDING_COPIUM_INGOT = new Item(new OwoItemSettings().tab(1).group(MaldingReactors.GROUP));
}
