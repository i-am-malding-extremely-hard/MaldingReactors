package i.malding.hard.maldingreactors.data.gen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MaldingDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var pack = generator.createPack();

        MaldingTagGenerator.MaldingBlockTagProvider blockTagProvider = pack.addProvider(MaldingTagGenerator.MaldingBlockTagProvider::new);

        pack.addProvider((output, registriesFuture) -> new MaldingTagGenerator.MaldingItemTagProvider(output, registriesFuture, blockTagProvider));

    }
}
