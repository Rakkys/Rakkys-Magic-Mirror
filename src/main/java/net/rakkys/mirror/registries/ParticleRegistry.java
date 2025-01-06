package net.rakkys.mirror.registries;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.rakkys.mirror.RakkysMagicMirror;

import java.util.ArrayList;
import java.util.List;

public class ParticleRegistry {
    public static final DefaultParticleType MIRROR_SPARKLE_1 = FabricParticleTypes.simple();
    public static final DefaultParticleType MIRROR_SPARKLE_2 = FabricParticleTypes.simple();
    public static final DefaultParticleType MIRROR_SPARKLE_3 = FabricParticleTypes.simple();

    public static final List<DefaultParticleType> MIRROR_SPARKLES = new ArrayList<>(List.of(MIRROR_SPARKLE_1, MIRROR_SPARKLE_2, MIRROR_SPARKLE_3));

    public static void registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RakkysMagicMirror.MOD_ID,
                "mirror_sparkle1"), MIRROR_SPARKLE_1);

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RakkysMagicMirror.MOD_ID,
                "mirror_sparkle2"), MIRROR_SPARKLE_2);

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(RakkysMagicMirror.MOD_ID,
                "mirror_sparkle3"), MIRROR_SPARKLE_3);

        RakkysMagicMirror.LOGGER.info("[Rakkys Magic Mirror] Registering Particles");
    }
}
