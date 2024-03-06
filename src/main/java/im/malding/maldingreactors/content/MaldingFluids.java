package im.malding.maldingreactors.content;

import im.malding.maldingreactors.MaldingReactors;
import im.malding.maldingreactors.content.fluids.CoolantFluid;
import im.malding.maldingreactors.content.fluids.CopiumFluid;
import im.malding.maldingreactors.content.fluids.MaldingCopiumFluid;
import im.malding.maldingreactors.content.fluids.SteamFluid;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject;
import me.alphamode.star.registry.FluidInfo;

import java.lang.reflect.Field;

public class MaldingFluids implements SimpleFieldProcessingSubject<FluidInfo> {

    public static final FluidInfo COPIUM = new FluidInfo(new CopiumFluid.Still(), new CopiumFluid.Flowing());
    public static final FluidInfo MALDING_COPIUM = new FluidInfo(new MaldingCopiumFluid.Still(), new MaldingCopiumFluid.Flowing());

    public static final FluidInfo STEAM = new FluidInfo(new SteamFluid.Still(), new SteamFluid.Flowing());
    public static final FluidInfo COOLANT = new FluidInfo(new CoolantFluid.Still(), new CoolantFluid.Flowing());

    @Override
    public Class<FluidInfo> getTargetFieldType() {
        return FluidInfo.class;
    }

    @Override
    public void processField(FluidInfo value, String identifier, Field field) {
        value.register(MaldingReactors.id(identifier), new OwoItemSettings().group(MaldingReactors.GROUP));
    }
}
