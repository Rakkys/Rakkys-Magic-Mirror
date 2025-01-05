package net.rakkys.mirror.registries;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.rakkys.mirror.RakkysMagicMirror;

public class ItemGroupRegistry {
    public static final ItemGroup MAGIC_MIRRORS = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ItemRegistry.MAGIC_MIRROR))
            .displayName(Text.translatable("itemGroup.rakkys-mirror.magic_mirrors"))
            .entries(((displayContext, entries) -> {
                entries.add(ItemRegistry.MAGIC_MIRROR);
                entries.add(ItemRegistry.ICE_MIRROR);
            }))
            .build();

    private static void registerItemGroup(String name, ItemGroup itemGroup) {
        Registry.register(Registries.ITEM_GROUP, new Identifier(RakkysMagicMirror.MOD_ID, name), itemGroup);
    }

    public static void registerItemGroups() {
        registerItemGroup("magic_mirrors", MAGIC_MIRRORS);

        RakkysMagicMirror.LOGGER.info("[Rakkys Magic Mirror] Registering Item Groups");
    }
}
