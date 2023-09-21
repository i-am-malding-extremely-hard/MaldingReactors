package i.malding.hard.maldingreactors.data;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import i.malding.hard.maldingreactors.MaldingReactors;
import i.malding.hard.maldingreactors.content.FundamentalProperties;
import i.malding.hard.maldingreactors.content.MediumProperties;
import i.malding.hard.maldingreactors.content.MediumRegistry;
import i.malding.hard.maldingreactors.util.CascadeErrorLogger;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class MediumPropertiesLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Identifier ID = MaldingReactors.id("medium_properties");

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    public MediumPropertiesLoader() {
        super(GSON, "medium_properties");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        Map<Identifier, MediumProperties> propertiesMap = new HashMap<>();

        prepared.forEach((id, fileElement) -> {
            try {
                if (fileElement instanceof JsonObject object) {
                    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                        String key = entry.getKey();
                        JsonElement element = entry.getValue();

                        Identifier parsedID = Identifier.tryParse(key);

                        if (parsedID == null) {
                            LOGGER.error("[MediumPropertiesLoader]: Unable to parse a given Medium Properties due to the objects key ({}) not being a valid Identifier: [File ID:{}]", key, id);

                            break;
                        }

                        CascadeErrorLogger errorHelper = new CascadeErrorLogger((message, logType, loggerFunc) -> {
                            loggerFunc.accept(LOGGER,"[MediumPropertiesLoader]: {} during parsing of a given Medium Properties {}: [File ID: {}, Entry ID: {}]", new Object[]{logType, message, id, parsedID});

                            return null;
                        });

                        MediumProperties properties = deserializeEntry(element, errorHelper);

                        if(properties == null) continue;

                        propertiesMap.put(parsedID, properties);
                    }
                } else {
                    LOGGER.error("[MediumPropertiesLoader]: Unable to parse a given Medium Properties File as it isn't a valid JsonObject: [File ID:{}]", id);
                }
            } catch (IllegalArgumentException | JsonParseException var10) {
                LOGGER.error("[MediumPropertiesLoader]: Parsing error loading Medium Properties {}: [ID:{}]", id, var10);
            }
        });

        MediumRegistry.MEDIUM_PROPERTIES.clear();
        MediumRegistry.MEDIUM_PROPERTIES.putAll(propertiesMap);
    }

    public MediumProperties deserializeEntry(JsonElement element, CascadeErrorLogger errorHelper){
        if(!(element instanceof JsonObject mainPropJson)){
            return errorHelper.error("due to not being a valid JsonObject", null);
        }

        FundamentalProperties neutronProp = deserializeFundamental(mainPropJson, "neutron",  errorHelper);
        FundamentalProperties gammaRayProp = deserializeFundamental( mainPropJson, "gamma_ray", errorHelper);

        if(!mainPropJson.has("conductivity")){
            return errorHelper.error("due to not containing \"conductivity\" field", null);
        }

        float conductivity = deserializeFloatField(mainPropJson, "conductivity", errorHelper);

        return new MediumProperties(gammaRayProp, neutronProp, conductivity);
    }

    public FundamentalProperties deserializeFundamental(JsonObject mainPropJson, String propName, CascadeErrorLogger errorHelper){
        if(!mainPropJson.has(propName)){
            return errorHelper.error("due to not containing \"" + propName + "\" field", null);
        }

        errorHelper = errorHelper.modifyMSG("due to \"" + propName + "\" value ");

        if(!(mainPropJson.get(propName) instanceof JsonObject fundmentalProp)){
            return errorHelper.error("not being a valid JsonObject", null);
        }

        float reflectivity = deserializeFloatField(fundmentalProp,"reflectivity", errorHelper);
        float absorption = deserializeFloatField(fundmentalProp,"absorption", errorHelper);
        float conversion = deserializeFloatField(fundmentalProp,"conversion", errorHelper);
        float emission = deserializeFloatField(fundmentalProp,"emission", errorHelper);

        return new FundamentalProperties(reflectivity, absorption, emission, conversion);
    }

    private float deserializeFloatField(JsonObject jsonObject, String fieldName, CascadeErrorLogger errorHelper){
        float value = 0.0f;

        if(!jsonObject.has(fieldName)){
            return errorHelper.warn(" not containing a \"" + fieldName + "\" value", value);
        }

        JsonElement element = jsonObject.get(fieldName);

        try {
            if (!element.isJsonPrimitive()) throw new IllegalArgumentException();

            value = element.getAsFloat();
        } catch (IllegalArgumentException e){
            errorHelper.warn(" containing a invalid \"" + fieldName + "\" field");
        }

        return value;
    }

    @Override
    public String getName() {
        return getFabricId().toString();
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }
}
