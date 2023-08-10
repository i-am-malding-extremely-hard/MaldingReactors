package i.malding.hard.maldingreactors.content.worldgen;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.function.Function;

public record OreEntry(ConfiguredFeature<?, ?> configuredFeature,
                       Function<ConfiguredFeature<?, ?>, PlacedFeature> feature) {
    public void register(Identifier id) {
//        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
//        Registry.register(BuiltinRegistries.PLACED_FEATURE, id, feature.apply(configuredFeature));
//        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
    }
}
