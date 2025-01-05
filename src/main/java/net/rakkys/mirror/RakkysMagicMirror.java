package net.rakkys.mirror;

import net.fabricmc.api.ModInitializer;
import net.rakkys.mirror.registries.GameRulesRegistry;
import net.rakkys.mirror.registries.ItemGroupRegistry;
import net.rakkys.mirror.registries.ItemRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RakkysMagicMirror implements ModInitializer {
	public static final String MOD_ID = "rakkys-mirror";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ItemRegistry.registerItems();
		ItemGroupRegistry.registerItemGroups();
		GameRulesRegistry.registerGameRules();

		RakkysMagicMirror.LOGGER.info("[Rakkys Magic Mirror] fully initialized");
	}
}