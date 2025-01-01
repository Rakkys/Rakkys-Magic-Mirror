package net.rakkys.mirror.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.rakkys.mirror.registries.ItemRegistry;

public class MirrorEnglishProvider extends FabricLanguageProvider {
    public MirrorEnglishProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ItemRegistry.MAGIC_MIRROR, "Magic Mirror");
        translationBuilder.add(ItemRegistry.ICE_MIRROR, "Ice Mirror");

        translationBuilder.add("rakkys-mirror.error.null_spawn_args", "Could not locate your spawn-point, :3");
        translationBuilder.add("rakkys-mirror.error.not_home_dimension", "You cannot teleport to other dimensions");
        translationBuilder.add("rakkys-mirror.error.not_enough_experience", "You do not have enough experience to teleport");

        translationBuilder.add("rakkys-mirror.mirror.gaze_tooltip", "Gaze in the mirror to return home");
    }
}
