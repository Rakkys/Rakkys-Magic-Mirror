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
        BlockPos SpawnPos = player.getSpawnPointPosition();
        RegistryKey<World> SpawnWorldKey = player.getSpawnPointDimension();
        ServerWorld SpawnWorld = player.getServer().getWorld(SpawnWorldKey);


        if (SpawnPos != null && SpawnWorld != null) {
            player.sendMessage(Text.translatable("rakkys-mirror.mirror.error.null_spawn_args"), true);
            return;
        }

        Optional<Vec3d> userSpawn = PlayerEntity.findRespawnPosition(SpawnWorld, SpawnPos, 0f, false, player.isAlive());
        if (userSpawn.isEmpty()) {
            player.sendMessage(Text.translatable("rakkys-mirror.mirror.error.null_spawn_args"), true);
            return;
        }

        if (decreaseExperience) {
            int experienceUsage = player.getWorld().getGameRules().getInt(GameRulesRegistry.MIRROR_EXPERIENCE_LEVEL_USAGE);
            player.addExperienceLevels(-experienceUsage);
        }

        Set<PositionFlag> flags = Set.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z);
        player.teleport(SpawnWorld,
                SpawnPos.getX(), SpawnPos.getY(), SpawnPos.getZ(),
                flags, player.getYaw(), player.getPitch());
    }
}
