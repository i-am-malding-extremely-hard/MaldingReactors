package im.malding.maldingreactors.data;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import im.malding.maldingreactors.MaldingReactors;
import im.malding.maldingreactors.content.MediumRegistry;
import im.malding.maldingreactors.util.CascadeErrorLogger;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.impl.resource.conditions.ResourceConditionsImpl;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;

import java.util.*;

public class MediumLinksLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    public MediumLinksLoader() {
        super(GSON, "medium_links");
    }

    public Map<Identifier, TagKey<Block>> TO_BE_RESOLVED_TAG_LINKS = new HashMap<>();

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        TO_BE_RESOLVED_TAG_LINKS.clear();

        prepared.forEach((id, fileElement) -> {
            try {
                if(fileElement instanceof JsonObject jsonObject){
                    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                        String key = entry.getKey();
                        JsonElement element = entry.getValue();

                        Identifier mediumID = Identifier.tryParse(key);

                        if (mediumID == null) {
                            LOGGER.error("[MediumLinksLoader]: Unable to parse a given Medium Links due to the objects key ({}) not being a valid Identifier: [File ID:{}]", key, id);

                            break;
                        }

                        CascadeErrorLogger errorHelper = new CascadeErrorLogger((message, logType, loggerFunc) -> {
                            loggerFunc.accept(LOGGER, "[MediumLinksLoader]: {} during parsing of a given Medium Link {}: [File ID: {}, Entry ID: {}]", new Object[]{logType, message, id, mediumID});

                            return null;
                        });

                        if(!MediumRegistry.MEDIUM_PROPERTIES.containsKey(mediumID)){
                            errorHelper.warn("due to the given Properties ID isn't found within the registry!");

                            continue;
                        }

                        if(!(element instanceof JsonArray array)){
                            errorHelper.error("due to the given element found not be an array of Block(s) and/or Tag(s)");

                            continue;
                        }

                        for (JsonElement jsonElement : array) {
                            deserializeEntry(jsonElement, mediumID, errorHelper);
                        }
                    }
                } else {
                    LOGGER.error("[MediumLinksLoader]: Unable to parse a given Medium Properties File as it isn't a valid JsonObject: [File ID:{}]", id);
                }
            } catch (IllegalArgumentException | JsonParseException var10) {
                LOGGER.error("[MediumLinksLoader]: Parsing error loading Medium Properties {}: [ID:{}]", id, var10);
            }
        });
    }

    public void deserializeEntry(JsonElement element, Identifier mediumID, CascadeErrorLogger errorHelper) {
        String linkEntry = element.getAsString();

        boolean isTag = linkEntry.contains("#");

        if (isTag) linkEntry = linkEntry.replace("#", "");

        Identifier linkEntryID = Identifier.tryParse(linkEntry);

        if (linkEntryID == null) {
            errorHelper.error("due to the given element [" + linkEntry + "] was invalid based on the Identifier Format.");

            return;
        }

        if (isTag) {
            var entryList = getEntryList(linkEntryID);

            if (entryList.isEmpty()) {
                //Disable this within the future?
                errorHelper.warn("due to the given Tag Entry [" + linkEntry + "] was found to be empty, such will be skipped!");

                return;
            }

            entryList.get().stream()
                    .map(RegistryEntry::value)
                    .map(Registries.BLOCK::getId)
                    .forEach(blockID -> MediumRegistry.BLOCK_ID_TO_MEDIUM.put(blockID, mediumID));

            return;
        }

        if (!Registries.BLOCK.containsId(linkEntryID)) {
            //Disable this within the future?
            errorHelper.warn("due to the given Block Entry [" + linkEntry + "] wasn't found within the Block Registry.");

            return;
        }

        MediumRegistry.BLOCK_ID_TO_MEDIUM.put(linkEntryID, mediumID);
    }

    private static Optional<Collection<RegistryEntry<Block>>> getEntryList(Identifier tagID){
        var registryToTagEntriesMap = ResourceConditionsImpl.LOADED_TAGS.get();

        Map<Identifier, Collection<RegistryEntry<?>>> tagToRegistryEntries = registryToTagEntriesMap.get(RegistryKeys.BLOCK);

        if(tagToRegistryEntries.containsKey(tagID)){
            List<RegistryEntry<Block>> collection = tagToRegistryEntries.get(tagID).stream()
                    .map(registryEntry -> (RegistryEntry<Block>) registryEntry)
                    .toList();

            return Optional.of(collection);
        }

        return Optional.empty();
    }

    @Override
    public String getName() {
        return getFabricId().toString();
    }

    @Override
    public Identifier getFabricId() {
        return MaldingReactors.id("medium_links");
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return List.of(MediumPropertiesLoader.ID);
    }
}
