package im.malding.maldingreactors.content;

import im.malding.maldingreactors.content.FundamentalProperties;
import im.malding.maldingreactors.content.MediumRegistry;
import net.minecraft.util.Identifier;

public class MediumProperties {

    public static final MediumProperties BASE = new MediumProperties(FundamentalProperties.BASE_GAMMA_RAY_PROPS, FundamentalProperties.BASE_NEUTRON_PROPS,0.15f);

    public final FundamentalProperties gammaRayProps;
    public final FundamentalProperties neutronProps;

    private final float conductivity;

    public MediumProperties(FundamentalProperties gammaRayProps, FundamentalProperties neutronProps, float conductivity){
        this.neutronProps = neutronProps;
        this.gammaRayProps = gammaRayProps;

        this.conductivity = conductivity;
    }

    public boolean isOf(MediumProperties properties) {
        return this == properties;
    }

    public Identifier getID(){
        return MediumRegistry.MEDIUM_PROPERTIES.inverse().get(this);
    }

    //--

    public FundamentalProperties getNeutronProps(){
        return this.neutronProps;
    }
    public FundamentalProperties getGammaRayProps(){
        return this.gammaRayProps;
    }

    public float conductivity(){
        return this.conductivity;
    }

}
