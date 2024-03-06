package im.malding.maldingreactors.content;

public record FundamentalProperties(float reflectivity, float absorption, float transparency, float conversion) {
    public static final FundamentalProperties BASE_NEUTRON_PROPS = new FundamentalProperties(0.0f, 0.1f, 0.01f);
    public static final FundamentalProperties BASE_GAMMA_RAY_PROPS = new FundamentalProperties(0.0f, 0.01f, 0.001f);

    public FundamentalProperties(float reflectivity, float absorption, float conversion) {
        this(reflectivity, absorption, 1.0f - (reflectivity + absorption), conversion);
    }
}
