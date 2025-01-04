package net.rakkys.mirror.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.rakkys.mirror.registries.GameRulesRegistry;
import net.rakkys.mirror.registries.ItemRegistry;
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
        if (user instanceof ServerPlayerEntity player) {
            int useDuration = this.getMaxUseTime(stack) - remainingUseTicks;

            if (useDuration >= CHARGE_TIME) {
                playEffects(world, user);
                teleportUser(player);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);

        boolean instantMirror = world.getGameRules().getBoolean(GameRulesRegistry.INSTANT_MAGIC_MIRROR);

        if (instantMirror && user instanceof ServerPlayerEntity player) {
            playEffects(world, user);
            teleportUser(player);

            return TypedActionResult.success(itemStack);
        } else {
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return CHARGE_TIME * 1800;
    }

    public void playEffects(World world, LivingEntity user) {
        if (world.isClient()) {
            world.addParticle(ParticleTypes.FLASH, user.getX(), user.getY(), user.getZ(),
                    0, 0, 0);
        } else {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS,
                    1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + CHARGE_TIME * 0.5F);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (MinecraftClient.getInstance().options.advancedItemTooltips) {
            tooltip.add(Text.translatable("rakkys-mirror.mirror.gaze_tooltip"));
        } else {
            tooltip.add(Text.translatable("rakkys-mirror.mirror.regular_tooltip"));
        }
    }

    public void teleportUser(ServerPlayerEntity player) {
        int cooldownDuration = player.getWorld().getGameRules().getInt(GameRulesRegistry.MAGIC_MIRROR_COOLDOWN);
        player.getItemCooldownManager().set(ItemRegistry.MAGIC_MIRROR, cooldownDuration);
        player.getItemCooldownManager().set(ItemRegistry.ICE_MIRROR, cooldownDuration);

        player.incrementStat(Stats.USED.getOrCreateStat(this));
        MirrorTeleportation.teleportPlayerToSpawn(player, true);

        player.stopUsingItem();
    }
}
