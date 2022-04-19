package i.malding.hard.maldingreactors.content.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.function.Function;

public record OreEntry(ConfiguredFeature<?, ?> configuredFeature, Function<ConfiguredFeature<?, ?>, PlacedFeature> feature) {
    public void register(Identifier id) {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, id, feature.apply(configuredFeature));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
    }
}
