package com.agrejus.netherendingenergy;

public class RegistryNames {
    public static final String CAUSTIC_BELL = "caustic_bell";

    /* Terra */
    public static final String TERRA_MACHINE_CASING = "terra_machine_casing";
    public static final String TERRA_HEAT_SINK = "terra_heat_sink";
    public static final String TERRA_REACTOR_CORE = "terra_reactor_core";
    public static final String TERRA_FURNACE_GENERATOR = "terra_furnace_generator";
    public static final String TERRA_VAPOR_COLLECTOR = "terra_vapor_collector";

    public static String CreateModRegistryName(String registryName) {
        return String.format("{0}:{1}", NetherEndingEnergy.MODID, registryName);
    }

}
