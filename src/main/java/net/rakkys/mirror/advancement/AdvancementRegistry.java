package net.rakkys.mirror.advancement;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.rakkys.mirror.RakkysMagicMirror;
import net.rakkys.mirror.item.MagicMirrorItem;
import net.rakkys.mirror.registries.ItemRegistry;

import java.util.List;
import java.util.function.Consumer;

public class AdvancementRegistry extends FabricAdvancementProvider {
    public AdvancementRegistry(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {

        // TODO: Figure out how to make it take either the normal or ice mirror instead of needing both

        Advancement rootAdvancement = Advancement.Builder.create()
                .display(
                        ItemRegistry.MAGIC_MIRROR,
                        Text.translatable("advancements.rakkys-mirror.obtain_mirror.title"),
                        Text.translatable("advancements.rakkys-mirror.obtain_mirror.description"),
                        new Identifier("textures/block/stone_bricks.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("obtain_normal_mirror", InventoryChangedCriterion.Conditions.items(ItemRegistry.MAGIC_MIRROR))
                .build(consumer, RakkysMagicMirror.MOD_ID + "/obtain_mirror");

        // MIRROR USE ADVANCEMENTS

        Advancement mirrorUseEasy = Advancement.Builder.create()
                .parent(rootAdvancement)
                .display(
                        ItemRegistry.MAGIC_MIRROR,
                        Text.translatable("advancements.rakkys-mirror.mirror-use-easy.title"),
                        Text.translatable("advancements.rakkys-mirror.mirror-use-easy.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("mirror_use", MirrorUseCriterion.Conditions.create(5))
                .build(consumer, RakkysMagicMirror.MOD_ID + "/mirror_use_easy");

        Advancement mirrorUseMedium = Advancement.Builder.create()
                .parent(mirrorUseEasy)
                .display(
                        ItemRegistry.MAGIC_MIRROR,
                        Text.translatable("advancements.rakkys-mirror.mirror-use-medium.title"),
                        Text.translatable("advancements.rakkys-mirror.mirror-use-medium.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("mirror_use", MirrorUseCriterion.Conditions.create(25))
                .build(consumer, RakkysMagicMirror.MOD_ID + "/mirror_use_medium");

        // MIRROR TELEPORT ADVANCEMENTS

        Advancement mirrorTeleportEasy = Advancement.Builder.create()
                .parent(rootAdvancement)
                .display(
                        ItemRegistry.MAGIC_MIRROR,
                        Text.translatable("advancements.rakkys-mirror.mirror-teleport-easy.title"),
                        Text.translatable("advancements.rakkys-mirror.mirror-teleport-easy.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("mirror_teleport", MirrorTeleportCriterion.Conditions.create(100))
                .build(consumer, RakkysMagicMirror.MOD_ID + "/mirror_teleport_easy");

        Advancement mirrorTeleportMedium = Advancement.Builder.create()
                .parent(mirrorTeleportEasy)
                .display(
                        ItemRegistry.MAGIC_MIRROR,
                        Text.translatable("advancements.rakkys-mirror.mirror-teleport-medium.title"),
                        Text.translatable("advancements.rakkys-mirror.mirror-teleport-medium.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("mirror_teleport", MirrorTeleportCriterion.Conditions.create(1000))
                .build(consumer, RakkysMagicMirror.MOD_ID + "/mirror_teleport_medium");

        // MIRROR MISC ADVANCEMENTS

        // TODO: Finish these advancements

        Advancement quickEscape = null; /*Advancement.Builder.create()
                .parent(rootAdvancement)
                .display(
                        ItemRegistry.MAGIC_MIRROR,
                        Text.translatable("advancements.rakkys-mirror.quick-escape.title"),
                        Text.translatable("advancements.rakkys-mirror.quick-escape.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("quick_escape", MirrorHealthUseCriterion.Conditions.create(6F))
                .build(consumer, RakkysMagicMirror.MOD_ID + "/quick_escape");*/

        /*List<RegistryKey<Biome>> snowyBiomeList = List.of(BiomeKeys.SNOWY_TAIGA, BiomeKeys.SNOWY_PLAINS,
                BiomeKeys.SNOWY_SLOPES, BiomeKeys.SNOWY_BEACH);*/

        Advancement snowyRetreat = null; /*Advancement.Builder.create()
                .parent(rootAdvancement)
                .display(
                        ItemRegistry.MAGIC_MIRROR,
                        Text.translatable("advancements.rakkys-mirror.mirror-teleport-medium.title"),
                        Text.translatable("advancements.rakkys-mirror.mirror-teleport-medium.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("snowy_retreat", BiomeTeleportCriterion.Conditions.create(snowyBiomeList))
                .build(consumer, RakkysMagicMirror.MOD_ID + "/snowy_retreat");*/
    }
}
