package com.agrejus.netherendingenergy.blocks.terra.reactor;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.reactor.IReactorConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerraReactorConfig implements IReactorConfig {

    public static TerraReactorConfig INSTANCE = new TerraReactorConfig();

    private HashMap<TerraReactorPartIndex, List<Block>> map;

    @Override
    public Map<Block, Integer> getBlockCounts() {
        Map<Block, Integer> result = new HashMap<Block, Integer>();

        result.put(ModBlocks.TERRA_REACTOR_ENERGY_PORT_BLOCK, 1);
        result.put(ModBlocks.TERRA_REACTOR_ITEM_PORT_BLOCK, 1);
        result.put(ModBlocks.TERRA_REACTOR_ACID_PORT_BLOCK, 1);

        return result;
    }

    public HashMap<TerraReactorPartIndex, List<Block>> getParts() {
        if (this.map == null) {

            List<Block> airOnly = new ArrayList<Block>() {
                {
                    add(Blocks.AIR);
                }
            };

            List<Block> core  = new ArrayList<Block>() {
                {
                    add(ModBlocks.TERRA_REACTOR_CORE_BLOCK);
                }
            };

            List<Block> casingOnly = new ArrayList<Block>() {
                {
                    add(ModBlocks.TERRA_REACTOR_CASING_BLOCK);
                }
            };

            List<Block> heatSinkOnly = new ArrayList<Block>() {
                {
                    add(ModBlocks.TERRA_HEAT_SINK_BLOCK);
                }
            };

            List<Block> edges = new ArrayList<Block>() {
                {
                    add(ModBlocks.TERRA_REACTOR_CASING_BLOCK);
                    add(ModBlocks.TERRA_REACTOR_ENERGY_PORT_BLOCK);
                    add(ModBlocks.REACTOR_REDSTONE_PORT_BLOCK);
                    add(ModBlocks.TERRA_REACTOR_ACID_PORT_BLOCK);
                    add(ModBlocks.TERRA_REACTOR_ITEM_PORT_BLOCK);
                    add(ModBlocks.TERRA_REACTOR_REDSTONE_OUTPUT_PORT_BLOCK);
                }
            };


            this.map = new HashMap<TerraReactorPartIndex, List<Block>>() {
                {
                    put(TerraReactorPartIndex.P_n2_n2_0, edges);
                    put(TerraReactorPartIndex.P_n2_n1_0, casingOnly);
                    put(TerraReactorPartIndex.P_n2_0_0, casingOnly);
                    put(TerraReactorPartIndex.P_n2_1_0, casingOnly);
                    put(TerraReactorPartIndex.P_n2_2_0, edges);

                    put(TerraReactorPartIndex.P_n1_n2_0, heatSinkOnly);
                    put(TerraReactorPartIndex.P_n1_n1_0, airOnly);
                    put(TerraReactorPartIndex.P_n1_0_0, airOnly);
                    put(TerraReactorPartIndex.P_n1_1_0, airOnly);
                    put(TerraReactorPartIndex.P_n1_2_0, heatSinkOnly);

                    put(TerraReactorPartIndex.P_0_n2_0, heatSinkOnly);
                    put(TerraReactorPartIndex.P_0_n1_0, airOnly);
                    put(TerraReactorPartIndex.P_0_0_0, core);
                    put(TerraReactorPartIndex.P_0_1_0, airOnly);
                    put(TerraReactorPartIndex.P_0_2_0, heatSinkOnly);

                    put(TerraReactorPartIndex.P_1_n2_0, heatSinkOnly);
                    put(TerraReactorPartIndex.P_1_n1_0, airOnly);
                    put(TerraReactorPartIndex.P_1_0_0, airOnly);
                    put(TerraReactorPartIndex.P_1_1_0, airOnly);
                    put(TerraReactorPartIndex.P_1_2_0, heatSinkOnly);

                    put(TerraReactorPartIndex.P_2_n2_0, edges);
                    put(TerraReactorPartIndex.P_2_n1_0, casingOnly);
                    put(TerraReactorPartIndex.P_2_0_0, casingOnly);
                    put(TerraReactorPartIndex.P_2_1_0, casingOnly);
                    put(TerraReactorPartIndex.P_2_2_0, edges);

                    put(TerraReactorPartIndex.P_n2_n2_1, edges);
                    put(TerraReactorPartIndex.P_n2_n1_1, airOnly);
                    put(TerraReactorPartIndex.P_n2_0_1, airOnly);
                    put(TerraReactorPartIndex.P_n2_1_1, airOnly);
                    put(TerraReactorPartIndex.P_n2_2_1, edges);

                    put(TerraReactorPartIndex.P_n1_n2_1, heatSinkOnly);
                    put(TerraReactorPartIndex.P_n1_n1_1, airOnly);
                    put(TerraReactorPartIndex.P_n1_0_1, airOnly);
                    put(TerraReactorPartIndex.P_n1_1_1, airOnly);
                    put(TerraReactorPartIndex.P_n1_2_1, heatSinkOnly);

                    put(TerraReactorPartIndex.P_0_n2_1, heatSinkOnly);
                    put(TerraReactorPartIndex.P_0_n1_1, airOnly);
                    put(TerraReactorPartIndex.P_0_0_1, airOnly);
                    put(TerraReactorPartIndex.P_0_1_1, airOnly);
                    put(TerraReactorPartIndex.P_0_2_1, heatSinkOnly);

                    put(TerraReactorPartIndex.P_1_n2_1, heatSinkOnly);
                    put(TerraReactorPartIndex.P_1_n1_1, airOnly);
                    put(TerraReactorPartIndex.P_1_0_1, airOnly);
                    put(TerraReactorPartIndex.P_1_1_1, airOnly);
                    put(TerraReactorPartIndex.P_1_2_1, heatSinkOnly);

                    put(TerraReactorPartIndex.P_2_n2_1, edges);
                    put(TerraReactorPartIndex.P_2_n1_1, airOnly);
                    put(TerraReactorPartIndex.P_2_0_1, airOnly);
                    put(TerraReactorPartIndex.P_2_1_1, airOnly);
                    put(TerraReactorPartIndex.P_2_2_1, edges);

                    put(TerraReactorPartIndex.P_n1_n2_2, edges);
                    put(TerraReactorPartIndex.P_n1_n1_2, airOnly);
                    put(TerraReactorPartIndex.P_n1_0_2, airOnly);
                    put(TerraReactorPartIndex.P_n1_1_2, airOnly);
                    put(TerraReactorPartIndex.P_n1_2_2, edges);

                    put(TerraReactorPartIndex.P_0_n2_2, edges);
                    put(TerraReactorPartIndex.P_0_n1_2, casingOnly);
                    put(TerraReactorPartIndex.P_0_0_2, casingOnly);
                    put(TerraReactorPartIndex.P_0_1_2, casingOnly);
                    put(TerraReactorPartIndex.P_0_2_2, edges);

                    put(TerraReactorPartIndex.P_1_n2_2, edges);
                    put(TerraReactorPartIndex.P_1_n1_2, airOnly);
                    put(TerraReactorPartIndex.P_1_0_2, airOnly);
                    put(TerraReactorPartIndex.P_1_1_2, airOnly);
                    put(TerraReactorPartIndex.P_1_2_2, edges);

                    put(TerraReactorPartIndex.P_n2_n2_n1, edges);
                    put(TerraReactorPartIndex.P_n2_n1_n1, airOnly);
                    put(TerraReactorPartIndex.P_n2_0_n1, airOnly);
                    put(TerraReactorPartIndex.P_n2_1_n1, airOnly);
                    put(TerraReactorPartIndex.P_n2_2_n1, edges);

                    put(TerraReactorPartIndex.P_n1_n2_n1, heatSinkOnly);
                    put(TerraReactorPartIndex.P_n1_n1_n1, airOnly);
                    put(TerraReactorPartIndex.P_n1_0_n1, airOnly);
                    put(TerraReactorPartIndex.P_n1_1_n1, airOnly);
                    put(TerraReactorPartIndex.P_n1_2_n1, heatSinkOnly);

                    put(TerraReactorPartIndex.P_0_n2_n1, heatSinkOnly);
                    put(TerraReactorPartIndex.P_0_n1_n1, airOnly);
                    put(TerraReactorPartIndex.P_0_0_n1, airOnly);
                    put(TerraReactorPartIndex.P_0_1_n1, airOnly);
                    put(TerraReactorPartIndex.P_0_2_n1, heatSinkOnly);

                    put(TerraReactorPartIndex.P_1_n2_n1, heatSinkOnly);
                    put(TerraReactorPartIndex.P_1_n1_n1, airOnly);
                    put(TerraReactorPartIndex.P_1_0_n1, airOnly);
                    put(TerraReactorPartIndex.P_1_1_n1, airOnly);
                    put(TerraReactorPartIndex.P_1_2_n1, heatSinkOnly);

                    put(TerraReactorPartIndex.P_2_n2_n1, edges);
                    put(TerraReactorPartIndex.P_2_n1_n1, airOnly);
                    put(TerraReactorPartIndex.P_2_0_n1, airOnly);
                    put(TerraReactorPartIndex.P_2_1_n1, airOnly);
                    put(TerraReactorPartIndex.P_2_2_n1, edges);

                    put(TerraReactorPartIndex.P_n1_n2_n2, edges);
                    put(TerraReactorPartIndex.P_n1_n1_n2, airOnly);
                    put(TerraReactorPartIndex.P_n1_0_n2, airOnly);
                    put(TerraReactorPartIndex.P_n1_1_n2, airOnly);
                    put(TerraReactorPartIndex.P_n1_2_n2, edges);

                    put(TerraReactorPartIndex.P_0_n2_n2, edges);
                    put(TerraReactorPartIndex.P_0_n1_n2, casingOnly);
                    put(TerraReactorPartIndex.P_0_0_n2, casingOnly);
                    put(TerraReactorPartIndex.P_0_1_n2, casingOnly);
                    put(TerraReactorPartIndex.P_0_2_n2, edges);

                    put(TerraReactorPartIndex.P_1_n2_n2, edges);
                    put(TerraReactorPartIndex.P_1_n1_n2, airOnly);
                    put(TerraReactorPartIndex.P_1_0_n2, airOnly);
                    put(TerraReactorPartIndex.P_1_1_n2, airOnly);
                    put(TerraReactorPartIndex.P_1_2_n2, edges);
                }
            };
        }
        return this.map;
    }
}
