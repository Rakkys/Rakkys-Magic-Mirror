package net.rakkys.mirror.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rakkys.mirror.registries.*;
import net.rakkys.mirror.util.MirrorTeleportation;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class MagicMirrorItem extends Item {
    private final int CHARGE_TIME = 20;

    public MagicMirrorItem(Settings settings) {
        super(settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int useDuration = this.getMaxUseTime(stack) - remainingUseTicks;

        if (useDuration == CHARGE_TIME) {
            if (user instanceof ServerPlayerEntity player) {
                teleportUser(player);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);

        boolean instantMirror = world.getGameRules().getBoolean(GameRulesRegistry.INSTANT_MAGIC_MIRROR);
        if (user.isCreative()) {
            instantMirror = true;
        }

        if (instantMirror && user instanceof ServerPlayerEntity player) {
            teleportUser(player);

            return TypedActionResult.success(itemStack);
        } else {
            return TypedActionResult.consume(itemStack);
        }
    }

    private static int getMirrorUseCount(ServerPlayerEntity serverPlayer) {
        ServerStatHandler statHandler = serverPlayer.getStatHandler();
        return statHandler.getStat(Stats.USED.getOrCreateStat(ItemRegistry.MAGIC_MIRROR))
                + statHandler.getStat(Stats.USED.getOrCreateStat(ItemRegistry.ICE_MIRROR));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return CHARGE_TIME * 1800;
    }

    public void playEffects(ServerWorld world, ServerPlayerEntity user, boolean success) {
        int particleAmount = (world.getRandom().nextInt(CHARGE_TIME * 3) + 5) * 2;

        for (int i = 0; i <= particleAmount; i++) {
            int randomParticle = (int) (Math.random() * 3);

            world.spawnParticles(ParticleRegistry.MIRROR_SPARKLES.get(randomParticle),
                    user.getX(), user.getY(), user.getZ(),
                    2, 0, 0, 0, 1);
        }

        SoundEvent teleportSuccess = SoundRegistry.MIRROR_TELEPORT_SUCCESS;
        SoundEvent teleportFailure = SoundRegistry.MIRROR_TELEPORT_FAILURE;

        SoundEvent soundEvent = success ? teleportSuccess: teleportFailure;

        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                soundEvent, SoundCategory.PLAYERS,
                1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + CHARGE_TIME * 0.5F);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (MinecraftClient.getInstance().options.advancedItemTooltips) {
            tooltip.add(Text.translatable("item.rakkys-mirror.mirrors.gaze_tooltip"));
        } else {
            tooltip.add(Text.translatable("item.rakkys-mirror.mirrors.regular_tooltip"));
        }
    }

    public void teleportUser(ServerPlayerEntity player) {
        int cooldownDuration = player.getWorld().getGameRules().getInt(GameRulesRegistry.MAGIC_MIRROR_COOLDOWN);
        player.getItemCooldownManager().set(ItemRegistry.MAGIC_MIRROR, cooldownDuration);
        player.getItemCooldownManager().set(ItemRegistry.ICE_MIRROR, cooldownDuration);

        player.incrementStat(Stats.USED.getOrCreateStat(this));

        ServerWorld world = player.getServerWorld();

        BlockPos startPos = player.getBlockPos();
        BlockPos endPos;

        if (MirrorTeleportation.canTeleport(player, true) || player.isCreative()) {
            playEffects(world, player, true); // Spawn before tp
            MirrorTeleportation.teleportPlayerToSpawn(player);
            playEffects(world, player, true); // Spawn after tp
        } else {
            playEffects(world, player, false);
        }

        player.stopUsingItem();

        // Advancement Handling

        int useCount = getMirrorUseCount(player);
        CriteriaRegistry.MIRROR_USE.trigger(player, useCount);

        endPos = player.getBlockPos();
        CriteriaRegistry.MIRROR_TELEPORT_DISTANCE.trigger(player, startPos, endPos);
    }
}
