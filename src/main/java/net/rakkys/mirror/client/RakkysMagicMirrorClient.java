package net.rakkys.mirror.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.rakkys.mirror.RakkysMagicMirror;
import net.rakkys.mirror.particle.MirrorSparkleParticle;
import net.rakkys.mirror.registries.ParticleRegistry;

public class RakkysMagicMirrorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.MIRROR_SPARKLE_1, MirrorSparkleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.MIRROR_SPARKLE_2, MirrorSparkleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.MIRROR_SPARKLE_3, MirrorSparkleParticle.Factory::new);

        RakkysMagicMirror.LOGGER.info("[Rakkys Magic Mirror] client fully initialized");
    }
}
