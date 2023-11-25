package im.malding.maldingreactors.content;

public record FundamentalProperties(float reflectivity, float absorption, float transparency, float emission, float conversion) {
    public static final FundamentalProperties BASE_NEUTRON_PROPS = new FundamentalProperties(0.0f, 0.1f, 0f, 0.2f);
    public static final FundamentalProperties BASE_GAMMA_RAY_PROPS = new FundamentalProperties(0.0f, 0.01f, 0f, 0.0f);

    public FundamentalProperties(float reflectivity, float absorption, float emission, float conversion) {
        this(reflectivity, absorption, 1.0f - (reflectivity + absorption), emission, conversion);
    }
}
