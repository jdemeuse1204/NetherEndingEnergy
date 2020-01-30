package com.agrejus.netherendingenergy.blocks.test;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BlockTank extends Block {

    public BlockTank() {
        super(Properties.create(Material.GLASS)
                .hardnessAndResistance(1.0f)
                .sound(SoundType.GLASS));
        setRegistryName("tank");
        // @todo 1.13
//        setHarvestLevel("pickaxe", 0);
    }

    private static final Pattern COMPILE = Pattern.compile("@", Pattern.LITERAL);

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flags) {
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound != null) {
            CompoundNBT nbt = tagCompound.getCompound("tank");
            FluidStack fluidStack = null;
            if (!nbt.contains("Empty")) {
                fluidStack = FluidStack.loadFluidStackFromNBT(nbt);
            }
            if (fluidStack == null) {
                addInformationLocalized(tooltip, "message.netherendingenergy.tank", "empty");
            } else {
                String name = fluidStack.getDisplayName().getString();
                addInformationLocalized(tooltip, "message.netherendingenergy.tank", name + " (" + fluidStack.getAmount() + ")");
            }
        }
    }

    protected void addInformationLocalized(List<ITextComponent> tooltip, String key, Object... parameters) {
        String translated = I18n.format(key, parameters);
        translated = COMPILE.matcher(translated).replaceAll("\u00a7");

        Collections.addAll(tooltip.stream().map(ITextComponent::getFormattedText).collect(Collectors.toList()), StringUtils.split(translated, "\n"));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileTank();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote) {
            //FluidUtil.tryFillContainer(null, )
            FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getFace());
        }
        return true;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }
/*
    @Override
    public boolean isNormalCube(BlockState p_149721_1_) {
        return false;
    }*/


    public boolean isFullCube(BlockState state) {
        return false;
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
