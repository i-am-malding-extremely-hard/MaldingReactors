package i.malding.hard.maldingreactors.content;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.minecraft.item.Item;

public class MaldingItems implements ItemRegistryContainer {
    public static final Item YELLORIUM_INGOT = new Item(new OwoItemSettings().tab(1));
    public static final Item CYANITE_INGOT = new Item(new OwoItemSettings().tab(1));
}
