package net.rakkys.mirror.item;

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
import net.minecraft.world.World;
import net.rakkys.mirror.registries.GameRulesRegistry;

import java.util.Set;

import static com.ibm.icu.text.PluralRules.Operand.f;

public class MagicMirrorItem extends Item {
    public MagicMirrorItem(Settings settings) {
        super(settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int useDuration = this.getMaxUseTime(stack) - remainingUseTicks;

        if (useDuration == 20) {
            world.addParticle(ParticleTypes.FLASH, user.getX(), user.getY(), user.getZ(),
                    0, 0, 0);

            world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS,
                    1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + useDuration * 0.5F);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof ServerPlayerEntity player)) {
            return;
        }

        int useDuration = this.getMaxUseTime(stack) - remainingUseTicks;

        if (useDuration >= 20) {
            teleportUser(player);
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

        if (user.getWorld().getGameRules().getBoolean(GameRulesRegistry.INSTANT_MAGIC_MIRROR)) {
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
            Set<PositionFlag> flags = Set.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z);
            player.teleport(SpawnWorld,
                    SpawnPos.getX(), SpawnPos.getY(), SpawnPos.getZ(),
                    flags, player.getYaw(), player.getPitch());
        } else {
            player.sendMessage(Text.translatable("rakkys-mirror.mirror.error.null_spawn_args"));
        }
    }
}
