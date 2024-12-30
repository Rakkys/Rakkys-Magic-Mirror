package net.rakkys.mirror.registries;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class GameRulesRegistry {
    public static final GameRules.Key<GameRules.BooleanRule> INSTANT_MAGIC_MIRROR =
            GameRuleRegistry.register("instantMagicMirror",
                    GameRules.Category.PLAYER,
                    GameRuleFactory.createBooleanRule(false));

    public static final GameRules.Key<GameRules.IntRule> MAGIC_MIRROR_COOLDOWN =
            GameRuleRegistry.register("magicMirrorCooldown",
                    GameRules.Category.PLAYER,
                    GameRuleFactory.createIntRule(20, 20));

    public static final GameRules.Key<GameRules.IntRule> MIRROR_EXPERIENCE_LEVEL_USAGE =
            GameRuleRegistry.register("mirrorExperienceLevelUsage",
                    GameRules.Category.PLAYER,
                    GameRuleFactory.createIntRule(0, 0));

    public static void registerGameRules() {

    }
}
