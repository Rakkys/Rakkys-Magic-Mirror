package net.rakkys.mirror.registries;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class GameRulesRegistry {
    public static final GameRules.Key<GameRules.BooleanRule> INSTANT_MAGIC_MIRROR =
            GameRuleRegistry.register("instantMagicMirror", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));

    public static void registerGameRules() {

    }
}
