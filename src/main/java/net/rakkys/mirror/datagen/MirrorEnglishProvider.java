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

        translationBuilder.add("rakkys-mirror.mirror.error.null_spawn_args", "Could not locate your spawn-point, :3");
    }
}
