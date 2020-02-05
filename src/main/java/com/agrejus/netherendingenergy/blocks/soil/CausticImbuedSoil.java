package com.agrejus.netherendingenergy.blocks.soil;

import com.agrejus.netherendingenergy.RegistryNames;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;

public class CausticImbuedSoil extends Block {

    public static final EnumProperty<ImbueMaterial> IMBUED_MATERIAL = EnumProperty.create("imbued_material", ImbueMaterial.class);

    public CausticImbuedSoil() {
        super(Block.Properties.create(Material.GLASS)
                .sound(SoundType.GROUND)
                .hardnessAndResistance(1.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.CAUSTIC_IMBUED_SOIL);
        setDefaultState(getStateContainer().getBaseState().with(IMBUED_MATERIAL, ImbueMaterial.LAPIS));
    }

    public ImbueMaterial getImbueMaterial() {

        IProperty<?> p = stateContainer.getProperty("imbued_material");

        return (ImbueMaterial) getDefaultState().get(p);
    }

    public void setImbuedMaterial(ImbueMaterial material) {
        setDefaultState(getStateContainer().getBaseState().with(IMBUED_MATERIAL, material));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(IMBUED_MATERIAL);
    }
}
