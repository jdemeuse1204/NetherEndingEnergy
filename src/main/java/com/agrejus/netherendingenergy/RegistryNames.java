package com.agrejus.netherendingenergy;

public class RegistryNames {
    public static final String CAUSTIC_BELL = "caustic_bell";
    public static final String CAUSTIC_IMBUED_SOIL = "caustic_imbued_soil";

    /* Terra */
    public static final String TERRA_MACHINE_CASING = "terra_machine_casing";
    public static final String TERRA_HEAT_SINK = "terra_heat_sink";
    public static final String TERRA_REACTOR_CORE = "terra_reactor_core";
    public static final String TERRA_COLLECTING_STATION = "terra_collecting_station";
    public static final String TERRA_MIXER = "terra_mixer";

    /* Abyssal */
    public static final String ABYSS_HEAT_SINK = "abyssal_heat_sink";

    /* Chaotic */
    public static final String CHAOTIC_HEAT_SINK = "chaotic_heat_sink";

    /* General */
    public static final String REACTOR_REDSTONE_PORT = "reactor_redstone_port";
    public static final String IMBUING_MACHINE = "imbuing_machine";
    public static final String FURNACE_GENERATOR = "furnace_generator";

    public static final String RAW_ACID = "raw_acid";
    public static final String RAW_ACID_FLOWING = "raw_acid_flowing";

    public static String CreateModRegistryName(String registryName) {
        return String.format("{0}:{1}", NetherEndingEnergy.MODID, registryName);
    }

}
