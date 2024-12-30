package net.rakkys.mirror.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.rakkys.mirror.registries.ItemRegistry;

import java.util.function.Consumer;

public class MirrorRecipeProvider extends FabricRecipeProvider {
    public MirrorRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.MAGIC_MIRROR)
                .pattern(" id")
                .pattern("igi")
                .pattern("ni ")
                .input('i', Items.IRON_INGOT)
                .input('d', Items.DIAMOND)
                .input('g', ConventionalItemTags.GLASS_BLOCKS)
                .input('n', Items.IRON_NUGGET)
                .criterion(FabricRecipeProvider.hasItem(Items.DIAMOND),
                        FabricRecipeProvider.conditionsFromItem(Items.DIAMOND))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.ICE_MIRROR)
                .pattern(" id")
                .pattern("igi")
                .pattern("ni ")
                .input('i', Items.ICE)
                .input('d', Items.DIAMOND)
                .input('g', ConventionalItemTags.GLASS_BLOCKS)
                .input('n', Items.IRON_NUGGET)
                .criterion(FabricRecipeProvider.hasItem(Items.DIAMOND),
                        FabricRecipeProvider.conditionsFromItem(Items.DIAMOND))
                .offerTo(consumer);
    }
}
