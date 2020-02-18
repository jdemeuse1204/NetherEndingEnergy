package com.agrejus.netherendingenergy.setup.config;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nonnull;

public class CausticBellConfig implements IFeatureConfig {

    @Nonnull
    @Override
    public <T> Dynamic<T> serialize(@Nonnull DynamicOps<T> ops) {
        return new Dynamic<>(ops);
    }

    public static CausticBellConfig deserialize(Dynamic<?> dynamic) {
        return new CausticBellConfig();
    }
}
