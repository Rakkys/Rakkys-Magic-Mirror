package net.rakkys.mirror.item;

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
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.rakkys.mirror.registries.GameRulesRegistry;
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
            playParticles(world, user, useDuration);

            if (user instanceof ServerPlayerEntity player) {
                MirrorTeleportation.teleportPlayerToSpawn(player, true);

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
                MirrorTeleportation.teleportPlayerToSpawn(player, true);
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
            MirrorTeleportation.teleportPlayerToSpawn(player, true);

            return TypedActionResult.success(itemStack);
        } else {
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 36000;
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
