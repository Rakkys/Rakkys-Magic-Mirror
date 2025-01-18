package net.rakkys.mirror;

import net.fabricmc.api.ModInitializer;
import net.minecraft.advancement.criterion.Criteria;
import net.rakkys.mirror.registries.*;
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
		ParticleRegistry.registerParticles();
		SoundRegistry.registerSounds();
		CriteriaRegistry.register();

		RakkysMagicMirror.LOGGER.info("[Rakkys Magic Mirror] fully initialized");
	}
}