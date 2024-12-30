package net.rakkys.mirror.item;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.rakkys.mirror.registries.GameRulesRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MagicMirrorItem extends Item {
    private final int CHARGE_TIME = 20;

    public MagicMirrorItem(Settings settings) {
        super(settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int useDuration = this.getMaxUseTime(stack) - remainingUseTicks;

        if (useDuration == CHARGE_TIME) {
            playParticles(world, user, useDuration);

            if (user instanceof ServerPlayerEntity player) {
                teleportUser(player);

                int cooldownDuration = world.getGameRules().getInt(GameRulesRegistry.MAGIC_MIRROR_COOLDOWN);
                player.getItemCooldownManager().set(this, cooldownDuration);
            }

            user.stopUsingItem();
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof ServerPlayerEntity player) {
            int useDuration = this.getMaxUseTime(stack) - remainingUseTicks;

            if (useDuration >= CHARGE_TIME) {
                teleportUser(player);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);

        if (user instanceof ClientPlayerEntity) {
            return TypedActionResult.consume(itemStack);
        }

        ServerPlayerEntity player = (ServerPlayerEntity) user;

        if (world.getGameRules().getBoolean(GameRulesRegistry.INSTANT_MAGIC_MIRROR)) {
            playParticles(world, user, 1);
            teleportUser(player);

            return TypedActionResult.success(itemStack);
        } else {
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 36000;
    }

    public void teleportUser(ServerPlayerEntity player) {
        BlockPos SpawnPos = player.getSpawnPointPosition();
        RegistryKey<World> SpawnWorldKey = player.getSpawnPointDimension();
        ServerWorld SpawnWorld = player.getServer().getWorld(SpawnWorldKey);


        if (SpawnPos != null && SpawnWorld != null) {
            Optional<Vec3d> userSpawn = PlayerEntity.findRespawnPosition(SpawnWorld, SpawnPos, 0f, false, player.isAlive());
            if (userSpawn.isPresent()) {
                int experienceUsage = player.getWorld().getGameRules().getInt(GameRulesRegistry.MIRROR_EXPERIENCE_LEVEL_USAGE);
                player.addExperienceLevels(-experienceUsage);

                Set<PositionFlag> flags = Set.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z);
                player.teleport(SpawnWorld,
                        SpawnPos.getX(), SpawnPos.getY(), SpawnPos.getZ(),
                        flags, player.getYaw(), player.getPitch());

                return;
            }
        }
        player.sendMessage(Text.translatable("rakkys-mirror.mirror.error.null_spawn_args"), true);
    }

    public void playParticles(World world, LivingEntity user, int useDuration) {
        world.addParticle(ParticleTypes.FLASH, user.getX(), user.getY(), user.getZ(),
                0, 0, 0);

        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS,
                1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + useDuration * 0.5F);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("rakkys-mirror.mirror.gaze_tooltip"));
    }
}
