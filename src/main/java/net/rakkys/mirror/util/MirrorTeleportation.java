package net.rakkys.mirror.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.rakkys.mirror.registries.GameRulesRegistry;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class MirrorTeleportation {
    public static boolean teleportPlayerToSpawn(ServerPlayerEntity player) {
        BlockPos spawnPos = player.getSpawnPointPosition();
        RegistryKey<World> spawnWorldKey = player.getSpawnPointDimension();
        ServerWorld spawnWorld = Objects.requireNonNull(player.getServer()).getWorld(spawnWorldKey);

        Set<PositionFlag> flags = Set.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z);
        player.teleport(spawnWorld,
                spawnPos.getX() + 0.5, spawnPos.getY() + 1, spawnPos.getZ() + 0.5,
                flags, player.getYaw(), player.getPitch());

        return true;
    }

    public static boolean canTeleport(BlockPos spawnPos, ServerWorld spawnWorld, ServerPlayerEntity player, boolean decreaseExperience) {
        return checkValidRespawnPos(spawnPos, spawnWorld, player) &&
                canTeleportOtherDimension(spawnWorld, player) &&
                runExperienceCheck(decreaseExperience, player);
    }

    public static boolean canTeleport(ServerPlayerEntity player, boolean decreaseExperience) {
        BlockPos spawnPos = player.getSpawnPointPosition();
        RegistryKey<World> spawnWorldKey = player.getSpawnPointDimension();
        ServerWorld spawnWorld = Objects.requireNonNull(player.getServer()).getWorld(spawnWorldKey);

        return canTeleport(spawnPos, spawnWorld, player, decreaseExperience);
    }

    public static boolean checkValidRespawnPos(BlockPos spawnPos, ServerWorld spawnWorld, ServerPlayerEntity player) {
        if (spawnPos == null || spawnWorld == null) {
            player.sendMessage(Text.translatable("text.rakkys-mirror.error.null_spawn_args"), true);
            return false;
        }

        Optional<Vec3d> userSpawn = PlayerEntity.findRespawnPosition(spawnWorld, spawnPos, 0f, false, player.isAlive());
        if (userSpawn.isEmpty()) {
            player.sendMessage(Text.translatable("text.rakkys-mirror.error.null_spawn_args"), true);
            return false;
        }

        return true;
    }

    public static boolean canTeleportOtherDimension(ServerWorld spawnWorld, ServerPlayerEntity player) {
        if (player.getWorld().getGameRules().getBoolean(GameRulesRegistry.MIRROR_HOME_DIMENSION_ONLY)) {
            if (player.getWorld() != spawnWorld && !player.isCreative()) {
                player.sendMessage(Text.translatable("text.rakkys-mirror.error.not_home_dimension"), true);
                return false;
            }
        }

        return true;
    }

    public static boolean runExperienceCheck(boolean decreaseExperience, ServerPlayerEntity player) {
        if (decreaseExperience && !player.isCreative()) {
            int experienceUsage = player.getWorld().getGameRules().getInt(GameRulesRegistry.MIRROR_EXPERIENCE_LEVEL_USAGE);
            if (player.experienceLevel >= experienceUsage) {
                player.setExperienceLevel(player.experienceLevel - experienceUsage);
                return true;
            } else {
                player.sendMessage(Text.translatable("text.rakkys-mirror.error.not_enough_experience"), true);
                return false;
            }
        }
        return true;
    }
}
