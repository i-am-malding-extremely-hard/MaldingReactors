package i.malding.hard.maldingreactors.content;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class AllItems implements ItemRegistryContainer {
    public static final Item YELLORIUM = new Item(new OwoItemSettings().tab(1));
}
