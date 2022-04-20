package i.malding.hard.maldingreactors.data.gen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MaldingDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {

        MaldingTagGenerator.MaldingBlockTagProvider blockTagProvider = new MaldingTagGenerator.MaldingBlockTagProvider(generator);

        generator.addProvider(blockTagProvider);
        generator.addProvider(new MaldingTagGenerator.MaldingItemTagProvider(generator, blockTagProvider));

    }
}
