package i.malding.hard.maldingreactors.content.worldgen;

import i.malding.hard.maldingreactors.MaldingReactors;
import io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject;

import java.lang.reflect.Field;

public class MaldingFeatures implements SimpleFieldProcessingSubject<OreEntry> {
//    public static OreEntry COPIUM_ORE = new OreEntry(
//            new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, MaldingBlocks.COPIUM_ORE.getDefaultState(), 3)),
//            configuredFeature ->
//                    new PlacedFeature(RegistryEntry.of(configuredFeature),
//                            List.of(CountPlacementModifier.of(3), // number of veins per chunk
//                                    SquarePlacementModifier.of(), // spreading horizontally
//                                    HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(32))))
//    );

    @Override
    public void processField(OreEntry entry, String identifier, Field field) {
        entry.register(MaldingReactors.id(identifier));
    }

    @Override
    public Class<OreEntry> getTargetFieldType() {
        return OreEntry.class;
    }
}
