package i.malding.hard.maldingreactors.content;

import i.malding.hard.maldingreactors.MaldingReactors;
import i.malding.hard.maldingreactors.content.fluids.Coolant;
import i.malding.hard.maldingreactors.content.fluids.CyaniteFluid;
import i.malding.hard.maldingreactors.content.fluids.SteamFluid;
import i.malding.hard.maldingreactors.content.fluids.YelloriumFluid;
import i.malding.hard.maldingreactors.util.FluidInfo;
import io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject;

import java.lang.reflect.Field;

public class AllFluids implements SimpleFieldProcessingSubject<FluidInfo> {

    public static final FluidInfo YELLORIUM = new FluidInfo(new YelloriumFluid.Still(), new YelloriumFluid.Flowing());
    public static final FluidInfo CYANITE = new FluidInfo(new CyaniteFluid.Still(), new CyaniteFluid.Flowing());

    public static final FluidInfo STEAM = new FluidInfo(new SteamFluid.Still(), new SteamFluid.Flowing());
    public static final FluidInfo COOLANT = new FluidInfo(new Coolant.Still(), new Coolant.Flowing());

    @Override
    public Class<FluidInfo> getTargetFieldType() {
        return FluidInfo.class;
    }

    @Override
    public void processField(FluidInfo value, String identifier, Field field) {
        value.register(MaldingReactors.asResource(identifier));
    }
}
