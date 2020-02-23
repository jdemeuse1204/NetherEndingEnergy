package com.agrejus.netherendingenergy.blocks.terra.reactor;

import com.agrejus.netherendingenergy.blocks.base.reactor.energyport.ReactorEnergyPortBlock;
import com.agrejus.netherendingenergy.blocks.base.reactor.redstoneport.ReactorRedstonePortBlock;
import com.agrejus.netherendingenergy.blocks.base.reactor.redstoneport.ReactorRedstonePortTile;
import com.agrejus.netherendingenergy.blocks.terra.heatsink.TerraHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.terra.machinecasing.TerraMachineCasingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.IStringSerializable;

import java.util.Arrays;
import java.util.List;

public enum TerraReactorPartIndex implements IStringSerializable {
    UNFORMED("unformed", 0, 0, 0),

    // 55 Total Blocks

    /* BASE */
    //  Row 1
    P001("p001", 0, 0, 1, TerraMachineCasingBlock.class),
    P002("p002", 0, 0, 2, TerraMachineCasingBlock.class),
    P003("p003", 0, 0, 3, TerraMachineCasingBlock.class),

    // Row 2
    P100("p100", 1, 0, 0, ReactorRedstonePortBlock.class, ReactorEnergyPortBlock.class),
    P101("p101", 1, 0, 1, TerraHeatSinkBlock.class),
    P102("p102", 1, 0, 2, TerraHeatSinkBlock.class),
    P103("p103", 1, 0, 3, TerraHeatSinkBlock.class),
    P104("p104", 1, 0, 4, TerraMachineCasingBlock.class),

    // Row 3
    P200("p200", 2, 0, 0, TerraReactorCoreBlock.class),
    P201("p201", 2, 0, 1, TerraHeatSinkBlock.class),
    P202("p202", 2, 0, 2, TerraHeatSinkBlock.class),
    P203("p203", 2, 0, 3, TerraHeatSinkBlock.class),
    P204("p204", 2, 0, 4, TerraMachineCasingBlock.class),

    // Row 4
    P300("p300", 3, 0, 0, ReactorRedstonePortBlock.class, ReactorEnergyPortBlock.class),
    P301("p301", 3, 0, 1, TerraHeatSinkBlock.class),
    P302("p302", 3, 0, 2, TerraHeatSinkBlock.class),
    P303("p303", 3, 0, 3, TerraHeatSinkBlock.class),
    P304("p304", 3, 0, 4, TerraMachineCasingBlock.class),

    // Row 5
    P401("p401", 4, 0, 1, TerraMachineCasingBlock.class),
    P402("p402", 4, 0, 2, TerraMachineCasingBlock.class),
    P403("p403", 4, 0, 3, TerraMachineCasingBlock.class),

    /* Second Step */
    // Row 1
    P012("p012", 0, 1, 2, TerraMachineCasingBlock.class),

    // Row 3
    P210("p210", 2, 1, 0, TerraMachineCasingBlock.class),
    P214("p214", 2, 1, 4, TerraMachineCasingBlock.class),

    // Row 5
    P412("p412", 4, 1, 2, TerraMachineCasingBlock.class),

    /* Third Step */
    // Row 1
    P022("p022", 0, 2, 2, TerraMachineCasingBlock.class),

    // Row 3
    P222("p222", 2, 2, 2, TerraMachineCasingBlock.class),

    // Row 5
    P422("p422", 4, 2, 2, TerraMachineCasingBlock.class),

    /* Fourth Step */
    // Row 1
    P032("p032", 0, 3, 2, TerraMachineCasingBlock.class),

    // Row 3
    P230("p230", 2, 3, 0, TerraMachineCasingBlock.class),
    P234("p234", 2, 3, 4, TerraMachineCasingBlock.class),

    // Row 5
    P432("p432", 4, 3, 2, TerraMachineCasingBlock.class),

    /* Fifth Step */
    // Row 1
    P041("p041", 0, 4, 1, TerraMachineCasingBlock.class),
    P042("p042", 0, 4, 2, TerraMachineCasingBlock.class),
    P043("p043", 0, 4, 3, TerraMachineCasingBlock.class),

    // Row 2
    P140("p140", 1, 4, 0, TerraMachineCasingBlock.class),
    P141("p141", 1, 4, 1, TerraHeatSinkBlock.class),
    P142("p142", 1, 4, 2, TerraHeatSinkBlock.class),
    P143("p143", 1, 4, 3, TerraHeatSinkBlock.class),
    P144("p144", 1, 4, 4, TerraMachineCasingBlock.class),

    // Row 3
    P240("p240", 2, 4, 0, TerraMachineCasingBlock.class),
    P241("p241", 2, 4, 1, TerraHeatSinkBlock.class),
    P242("p242", 2, 4, 2, TerraHeatSinkBlock.class),
    P243("p243", 2, 4, 3, TerraHeatSinkBlock.class),
    P244("p244", 2, 4, 4, TerraMachineCasingBlock.class),

    // Row 4
    P340("p340", 3, 4, 0, TerraMachineCasingBlock.class),
    P341("p341", 3, 4, 1, TerraHeatSinkBlock.class),
    P342("p342", 3, 4, 2, TerraHeatSinkBlock.class),
    P343("p343", 3, 4, 3, TerraHeatSinkBlock.class),
    P344("p344", 3, 4, 4, TerraMachineCasingBlock.class),

    // Row 5
    P441("p441", 4, 4, 1, TerraMachineCasingBlock.class),
    P442("p442", 4, 4, 2, TerraMachineCasingBlock.class),
    P443("p443", 4, 4, 3, TerraMachineCasingBlock.class);

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

    public static TerraReactorPartIndex getIndex(int dx, int dy, int dz) {
        return VALUES[dx * 4 + dy * 2 + dz + 1];
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
