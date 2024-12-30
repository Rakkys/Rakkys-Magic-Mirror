package net.rakkys.mirror.registries;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.rakkys.mirror.RakkysMagicMirror;
import net.rakkys.mirror.item.MagicMirrorItem;

public class ItemRegistry {
    public static Item MAGIC_MIRROR = registerItem("magic_mirror",
            new MagicMirrorItem(new FabricItemSettings().maxCount(1)));

    public static Item ICE_MIRROR = registerItem("ice_mirror",
            new MagicMirrorItem(new FabricItemSettings().maxCount(1)));

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(RakkysMagicMirror.MOD_ID, name), item);
    }

    public static void registerItems() {

    }
}
