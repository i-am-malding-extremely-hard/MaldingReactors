package im.malding.maldingreactors.content;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class MediumRegistry {
    public static final BiMap<Identifier, MediumProperties> MEDIUM_PROPERTIES = HashBiMap.create();

    public static final Map<Identifier, Identifier> BLOCK_ID_TO_MEDIUM = new HashMap<>();

    public static void registerMedium(Identifier id, MediumProperties mediumProperties){
       MEDIUM_PROPERTIES.put(id, mediumProperties);
    }

    public static MediumProperties getProperty(Block block){
        return getProperty(Registries.BLOCK.getId(block));
    }

    public static MediumProperties getProperty(Identifier blockID){
        return BLOCK_ID_TO_MEDIUM.containsKey(blockID) ? MEDIUM_PROPERTIES.get(BLOCK_ID_TO_MEDIUM.get(blockID)) : MediumProperties.BASE;
    }
}
