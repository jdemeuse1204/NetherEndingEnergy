package com.agrejus.netherendingenergy;

public class RegistryNames {
    public static final String CAUSTIC_ROOTS = "caustic_roots";
    public static final String CAUSTIC_VINES = "caustic_vines";
    public static final String CAUSTIC_BELL = "caustic_bell";
    public static final String CAUSTIC_DIRT = "caustic_dirt";
    public static final String CAUSTIC_MASH = "caustic_mash";

    /* Terra */
    public static final String TERRA_REACTOR_REDSTONE_INPUT_PORT = "terra_reactor_redstone_input_port";
    public static final String TERRA_REACTOR_REDSTONE_OUTPUT_PORT = "terra_reactor_redstone_output_port";
    public static final String TERRA_REACTOR_ACID_PORT = "terra_reactor_acid_port";
    public static final String TERRA_REACTOR_ITEM_PORT = "terra_reactor_item_port";
    public static final String TERRA_REACTOR_CASING = "terra_reactor_casing";
    public static final String TERRA_HEAT_SINK = "terra_heat_sink";
    public static final String TERRA_REACTOR_CORE = "terra_reactor_core";
    public static final String TERRA_COLLECTING_STATION = "terra_collecting_station";
    public static final String TERRA_MIXER = "terra_mixer";
    public static final String TERRA_REACTOR_INJECTOR = "terra_reactor_injector";
    public static final String TERRA_REACTOR_ITEM_STABILIZER = "terra_reactor_item_stabilizer";

    /* Abyssal */
    public static final String ABYSS_HEAT_SINK = "abyssal_heat_sink";

    /* Chaotic */
    public static final String CHAOTIC_HEAT_SINK = "chaotic_heat_sink";

    /* General */
    public static final String TERRA_REACTOR_ENERGY_PORT = "terra_reactor_energy_port";

    public static final String BOTANISTS_CODEX = "botanists_codex";


    public static final String IMBUING_MACHINE = "imbuing_machine";
    public static final String FURNACE_GENERATOR = "furnace_generator";

    public static final String RAW_ACID = "raw_acid";
    public static final String RAW_ACID_FLOWING = "raw_acid_flowing";

    public static String CreateModRegistryName(String registryName) {
        return String.format("{0}:{1}", NetherEndingEnergy.MODID, registryName);
    }

}
