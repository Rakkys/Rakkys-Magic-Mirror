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

import java.util.Optional;
import java.util.Set;

public class MirrorTeleportation {
    public static void teleportPlayerToSpawn(ServerPlayerEntity player) {
        teleportPlayerToSpawn(player, false);
    }

    public static void teleportPlayerToSpawn(ServerPlayerEntity player, boolean decreaseExperience) {
        BlockPos spawnPos = player.getSpawnPointPosition();
        RegistryKey<World> spawnWorldKey = player.getSpawnPointDimension();
        ServerWorld spawnWorld = player.getServer().getWorld(spawnWorldKey);


        if (spawnPos == null || spawnWorld == null) {
            player.sendMessage(Text.translatable("text.rakkys-mirror.error.error.null_spawn_args"), true);
            return;
        }

        Optional<Vec3d> userSpawn = PlayerEntity.findRespawnPosition(spawnWorld, spawnPos, 0f, false, player.isAlive());
        if (userSpawn.isEmpty()) {
            player.sendMessage(Text.translatable("text.rakkys-mirror.error.null_spawn_args"), true);
            return;
        }

        if (player.getWorld().getGameRules().getBoolean(GameRulesRegistry.MIRROR_HOME_DIMENSION_ONLY)) {
            if (player.getWorld() != spawnWorld && !player.isCreative()) {
                player.sendMessage(Text.translatable("text.rakkys-mirror.error.not_home_dimension"), true);
                return;
            }
        }

        if (decreaseExperience && !player.isCreative()) {
            int experienceUsage = player.getWorld().getGameRules().getInt(GameRulesRegistry.MIRROR_EXPERIENCE_LEVEL_USAGE);
            if (player.experienceLevel >= experienceUsage) {
                player.setExperienceLevel(player.experienceLevel - experienceUsage);
            } else {
                player.sendMessage(Text.translatable("text.rakkys-mirror.error.not_enough_experience"), true);
                return;
            }
        }

        Set<PositionFlag> flags = Set.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z);
        player.teleport(spawnWorld,
                spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(),
                flags, player.getYaw(), player.getPitch());
    }
}
