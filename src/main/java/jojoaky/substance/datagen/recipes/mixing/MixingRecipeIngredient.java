package jojoaky.substance.datagen.recipes.mixing;

import jojoaky.substance.register.ModFluids;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public sealed interface MixingRecipeIngredient permits
        MixingRecipeIngredient.Item,
        MixingRecipeIngredient.Tag,
        MixingRecipeIngredient.Fluid {

    record Item(ItemLike item, int count) implements MixingRecipeIngredient {}

    record Tag(TagKey<net.minecraft.world.item.Item> tag, int count) implements MixingRecipeIngredient {}

    record Fluid(ModFluids.ChemicalFluidSet fluid, long amount) implements MixingRecipeIngredient {}
}