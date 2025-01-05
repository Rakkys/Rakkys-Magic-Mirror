package net.rakkys.mirror.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class MirrorSparkleParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteSet;

    protected MirrorSparkleParticle(ClientWorld clientWorld, double x, double y, double z,
                                    SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(clientWorld, x, y + 0.5, z, xd, yd, zd);

        this.velocityMultiplier = 0.9f;
        this.x = xd;
        this.y = yd;
        this.z = zd;
        this.scale = 0.1f;
        this.maxAge = 28 + (int)(Math.random() * 32);

        this.spriteSet = spriteSet;

        setSprite(spriteSet.getSprite(age, maxAge));
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteForAge(spriteSet);
        fadeOut();
    }

    private void fadeOut() {
        this.alpha = (-(1/(float) maxAge) * age + 1);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new MirrorSparkleParticle(world, x, y, z, this.sprites, velocityX, velocityY, velocityZ);
        }
    }
}
