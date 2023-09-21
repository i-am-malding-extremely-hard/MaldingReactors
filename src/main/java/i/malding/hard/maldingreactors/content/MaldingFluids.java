package i.malding.hard.maldingreactors.content;

import i.malding.hard.maldingreactors.MaldingReactors;
import i.malding.hard.maldingreactors.content.fluids.CoolantFluid;
import i.malding.hard.maldingreactors.content.fluids.CopiumFluid;
import i.malding.hard.maldingreactors.content.fluids.MaldingCopiumFluid;
import i.malding.hard.maldingreactors.content.fluids.SteamFluid;
import i.malding.hard.maldingreactors.util.FluidInfo;
import io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject;

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
        value.register(MaldingReactors.id(identifier));
    }
}
