package net.rakkys.mirror.registries;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.rakkys.mirror.RakkysMagicMirror;

public class SoundRegistry {
    public static final SoundEvent MIRROR_TELEPORT_SUCCESS = register("item.mirror.teleport.success");
    public static final SoundEvent MIRROR_TELEPORT_FAILURE = register("item.mirror.teleport.failure");

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(RakkysMagicMirror.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void registerSounds() {
        RakkysMagicMirror.LOGGER.info("[Rakkys Magic Mirror] Registering Sounds");
    }
}
