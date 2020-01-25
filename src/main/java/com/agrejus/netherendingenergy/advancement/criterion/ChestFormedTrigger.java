package com.agrejus.netherendingenergy.advancement.criterion;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.block.ChestMaterial;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChestFormedTrigger extends BaseCriterionTrigger<Pair<ChestMaterial, Integer>, ChestFormedTrigger.Instance> {
    public ChestFormedTrigger() {
        super(new ResourceLocation(NetherEndingEnergy.MODID, "chest_formed"));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        ChestMaterial material = null;
        JsonElement element = json.get("material");
        if (element != null && !element.isJsonNull()) {
            String materialString = element.getAsString();
            try {
                material = Objects.requireNonNull(ChestMaterial.valueOf(materialString), "Could not find a chest material by name " + materialString);
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Could not find a colossal chest material by name " + materialString
                        + ". Allowed values: "
                        + ChestMaterial.VALUES.stream().map(ChestMaterial::getName).collect(Collectors.toList()));
            }
        }

        Integer minimumSize = null;
        JsonElement elementSize = json.get("minimumSize");
        if (elementSize != null && !elementSize.isJsonNull()) {
            minimumSize = JSONUtils.getInt(elementSize, "minimumSize");
        }
        return new Instance(getId(), material, minimumSize);
    }

    public static class Instance extends CriterionInstance implements ICriterionInstanceTestable<Pair<ChestMaterial, Integer>> {
        private final ChestMaterial material;
        private final Integer minimumSize;

        public Instance(ResourceLocation criterionIn, @Nullable ChestMaterial material, @Nullable Integer minimumSize) {
            super(criterionIn);
            this.material = material;
            this.minimumSize = minimumSize;
        }

        public boolean test(ServerPlayerEntity player, Pair<ChestMaterial, Integer> data) {
            return (this.material == null || this.material == data.getLeft())
                    && (this.minimumSize == null || this.minimumSize <= data.getRight());
        }
    }

}
