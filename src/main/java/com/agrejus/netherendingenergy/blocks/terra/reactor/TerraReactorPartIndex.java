package com.agrejus.netherendingenergy.blocks.terra.reactor;

import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.energy.TerraReactorEnergyPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone.TerraReactorRedstoneInputPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.heatsink.TerraHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.casing.TerraReactorCasingBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.item.TerraReactorItemPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.liquid.TerraReactorAcidPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone.TerraReactorRedstoneOutputPortBlock;
import net.minecraft.block.AirBlock;
import net.minecraft.util.IStringSerializable;

import java.util.Arrays;
import java.util.List;

public enum TerraReactorPartIndex implements IStringSerializable {
    UNFORMED("unformed", 0, 0, 0),

    // 55 Total Blocks

    /* Middle Columns */
    P_n2_n2_0("p_n2_n2_0", -2, -2, 0, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_n2_n1_0("p_n2_n1_0", -2, -1, 0, TerraReactorCasingBlock.class),
    P_n2_0_0("p_n2_0_0", -2, 0, 0, TerraReactorCasingBlock.class),
    P_n2_1_0("p_n2_1_0", -2, 1, 0, TerraReactorCasingBlock.class),
    P_n2_2_0("p_n2_2_0", -2, 2, 0, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),

    P_n1_n2_0("p_n1_n2_0", -1, -2, 0, TerraHeatSinkBlock.class),
    P_n1_n1_0("p_n1_n1_0", -1, -1, 0, AirBlock.class),
    P_n1_0_0("p_n1_0_0", -1, 0, 0, AirBlock.class),
    P_n1_1_0("p_n1_1_0", -1, 1, 0, AirBlock.class),
    P_n1_2_0("p_n1_2_0", -1, 2, 0, TerraHeatSinkBlock.class),

    //  Center Column
    P_0_n2_0("p_0_n2_0", 0, -2, 0, TerraHeatSinkBlock.class),
    P_0_n1_0("p_0_n1_0", 0, -1, 0, AirBlock.class),
    P_0_0_0("p_0_0_0", 0, 0, 0, TerraReactorCoreBlock.class),
    P_0_1_0("p_0_1_0", 0, 1, 0, AirBlock.class),
    P_0_2_0("p_0_2_0", 0, 2, 0, TerraHeatSinkBlock.class),

    P_1_n2_0("p_1_n2_0", 1, -2, 0, TerraHeatSinkBlock.class),
    P_1_n1_0("p_1_n1_0", 1, -1, 0, AirBlock.class),
    P_1_0_0("p_1_0_0", 1, 0, 0, AirBlock.class),
    P_1_1_0("p_1_1_0", 1, 1, 0, AirBlock.class),
    P_1_2_0("p_1_2_0", 1, 2, 0, TerraHeatSinkBlock.class),

    P_2_n2_0("p_2_n2_0", 2, -2, 0, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_2_n1_0("p_2_n1_0", 2, -1, 0, TerraReactorCasingBlock.class),
    P_2_0_0("p_2_0_0", 2, 0, 0, TerraReactorCasingBlock.class),
    P_2_1_0("p_2_1_0", 2, 1, 0, TerraReactorCasingBlock.class),
    P_2_2_0("p_2_2_0", 2, 2, 0, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),

    /* Offset 1 */
    P_n2_n2_1("p_n2_n2_1", -2, -2, 1, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_n2_n1_1("p_n2_n1_1", -2, -1, 1, AirBlock.class),
    P_n2_0_1("p_n2_0_1", -2, 0, 1, AirBlock.class),
    P_n2_1_1("p_n2_1_1", -2, 1, 1, AirBlock.class),
    P_n2_2_1("p_n2_2_1", -2, 2, 1, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),

    P_n1_n2_1("p_n1_n2_1", -1, -2, 1, TerraHeatSinkBlock.class),
    P_n1_n1_1("p_n1_n1_1", -1, -1, 1, AirBlock.class),
    P_n1_0_1("p_n1_0_1", -1, 0, 1, AirBlock.class),
    P_n1_1_1("p_n1_1_1", -1, 1, 1, AirBlock.class),
    P_n1_2_1("p_n1_2_1", -1, 2, 1, TerraHeatSinkBlock.class),

    P_0_n2_1("p_0_n2_1", 0, -2, 1, TerraHeatSinkBlock.class),
    P_0_n1_1("p_0_n1_1", 0, -1, 1, AirBlock.class),
    P_0_0_1("p_0_0_1", 0, 0, 1, AirBlock.class),
    P_0_1_1("p_0_1_1", 0, 1, 1, AirBlock.class),
    P_0_2_1("p_0_2_1", 0, 2, 1, TerraHeatSinkBlock.class),

    P_1_n2_1("p_1_n2_1", 1, -2, 1, TerraHeatSinkBlock.class),
    P_1_n1_1("p_1_n1_1", 1, -1, 1, AirBlock.class),
    P_1_0_1("p_1_0_1", 1, 0, 1, AirBlock.class),
    P_1_1_1("p_1_1_1", 1, 1, 1, AirBlock.class),
    P_1_2_1("p_1_2_1", 1, 2, 1, TerraHeatSinkBlock.class),

    P_2_n2_1("p_2_n2_1", 2, -2, 1, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_2_n1_1("p_2_n1_1", 2, -1, 1, AirBlock.class),
    P_2_0_1("p_2_0_1", 2, 0, 1, AirBlock.class),
    P_2_1_1("p_2_1_1", 2, 1, 1, AirBlock.class),
    P_2_2_1("p_2_2_1", 2, 2, 1, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),

    /* Offset 2 */
    // Column of air

    P_n1_n2_2("p_n1_n2_2", -1, -2, 2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_n1_n1_2("p_n1_n1_2", -1, -1, 2, AirBlock.class),
    P_n1_0_2("p_n1_0_2", -1, 0, 2, AirBlock.class),
    P_n1_1_2("p_n1_1_2", -1, 1, 2, AirBlock.class),
    P_n1_2_2("p_n1_2_2", -1, 2, 2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),

    P_0_n2_2("p_0_n2_2", 0, -2, 2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_0_n1_2("p_0_n1_2", 0, -1, 2, TerraReactorCasingBlock.class),
    P_0_0_2("p_0_0_2", 0, 0, 2, TerraReactorCasingBlock.class),
    P_0_1_2("p_0_1_2", 0, 1, 2, TerraReactorCasingBlock.class),
    P_0_2_2("p_0_2_2", 0, 2, 2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),

    P_1_n2_2("p_1_n2_2", 1, -2, 2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_1_n1_2("p_1_n1_2", 1, -1, 2, AirBlock.class),
    P_1_0_2("p_1_0_2", 1, 0, 2, AirBlock.class),
    P_1_1_2("p_1_1_2", 1, 1, 2, AirBlock.class),
    P_1_2_2("p_1_2_2", 1, 2, 2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),

    // Column of air

    /* Offset -1 */
    P_n2_n2_n1("p_n2_n2_n1", -2, -2, -1, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_n2_n1_n1("p_n2_n1_n1", -2, -1, -1, AirBlock.class),
    P_n2_0_n1("p_n2_0_n1", -2, 0, -1, AirBlock.class),
    P_n2_1_n1("p_n2_1_n1", -2, 1, -1, AirBlock.class),
    P_n2_2_n1("p_n2_2_n1", -2, 2, -1, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),

    P_n1_n2_n1("p_n1_n2_n1", -1, -2, -1, TerraHeatSinkBlock.class),
    P_n1_n1_n1("p_n1_n1_n1", -1, -1, -1, AirBlock.class),
    P_n1_0_n1("p_n1_0_n1", -1, 0, -1, AirBlock.class),
    P_n1_1_n1("p_n1_1_n1", -1, 1, -1, AirBlock.class),
    P_n1_2_n1("p_n1_2_n1", -1, 2, -1, TerraHeatSinkBlock.class),

    P_0_n2_n1("p_0_n2_n1", 0, -2, -1, TerraHeatSinkBlock.class),
    P_0_n1_n1("p_0_n1_n1", 0, -1, -1, AirBlock.class),
    P_0_0_n1("p_0_0_n1", 0, 0, -1, AirBlock.class),
    P_0_1_n1("p_0_1_n1", 0, 1, -1, AirBlock.class),
    P_0_2_n1("p_0_2_n1", 0, 2, -1, TerraHeatSinkBlock.class),

    P_1_n2_n1("p_1_n2_n1", 1, -2, -1, TerraHeatSinkBlock.class),
    P_1_n1_n1("p_1_n1_n1", 1, -1, -1, AirBlock.class),
    P_1_0_n1("p_1_0_n1", 1, 0, -1, AirBlock.class),
    P_1_1_n1("p_1_1_n1", 1, 1, -1, AirBlock.class),
    P_1_2_n1("p_1_2_n1", 1, 2, -1, TerraHeatSinkBlock.class),

    P_2_n2_n1("p_2_n2_n1", 2, -2, -1, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_2_n1_n1("p_2_n1_n1", 2, -1, -1, AirBlock.class),
    P_2_0_n1("p_2_0_n1", 2, 0, -1, AirBlock.class),
    P_2_1_n1("p_2_1_n1", 2, 1, -1, AirBlock.class),
    P_2_2_n1("p_2_2_n1", 2, 2, -1, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),

    /* Offset -2 */
    P_n1_n2_n2("p_n1_n2_n2", -1, -2, -2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_n1_n1_n2("p_n1_n1_n2", -1, -1, -2, AirBlock.class),
    P_n1_0_n2("p_n1_0_n2", -1, 0, -2, AirBlock.class),
    P_n1_1_n2("p_n1_1_n2", -1, 1, -2, AirBlock.class),
    P_n1_2_n2("p_n1_2_n2", -1, 2, -2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),

    P_0_n2_n2("p_0_n2_n2", 0, -2, -2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_0_n1_n2("p_0_n1_n2", 0, -1, -2, TerraReactorCasingBlock.class),
    P_0_0_n2("p_0_0_n2", 0, 0, -2, TerraReactorCasingBlock.class),
    P_0_1_n2("p_0_1_n2", 0, 1, -2, TerraReactorCasingBlock.class),
    P_0_2_n2("p_0_2_n2", 0, 2, -2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),

    P_1_n2_n2("p_1_n2_n2", 1, -2, -2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class),
    P_1_n1_n2("p_1_n1_n2", 1, -1, -2, AirBlock.class),
    P_1_0_n2("p_1_0_n2", 1, 0, -2, AirBlock.class),
    P_1_1_n2("p_1_1_n2", 1, 1, -2, AirBlock.class),
    P_1_2_n2("p_1_2_n2", 1, 2, -2, TerraReactorCasingBlock.class, TerraReactorEnergyPortBlock.class, TerraReactorRedstoneInputPortBlock.class, TerraReactorAcidPortBlock.class, TerraReactorItemPortBlock.class, TerraReactorRedstoneOutputPortBlock.class);

    // Optimization
    public static final TerraReactorPartIndex[] VALUES = TerraReactorPartIndex.values();

    private final String name;
    private final int dx;
    private final int dy;
    private final int dz;
    private final Class[] blockTypes;

    TerraReactorPartIndex(String name, int dx, int dy, int dz, Class... blockTypes) {
        this.name = name;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.blockTypes = blockTypes;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<Class> getAllowedBlockTypes() {
        return Arrays.asList(this.blockTypes);
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getDz() {
        return dz;
    }
}
