package jojoaky.substance.datagen.recipes.mixing;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import jojoaky.substance.content.flask.FlaskItem;
import jojoaky.substance.register.ModFluids;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public record MixingRecipeDef(
        String name,
        List<MixingRecipeIngredient> ingredients,
        MixingRecipeOutput output,
        HeatCondition requiredHeat,
        boolean replaceCraftingWhenCreate
) {
    public static Builder named(String name) {
        return new Builder(name);
    }

    public static class Builder {
        private final String name;
        private final List<MixingRecipeIngredient> ingredients = new ArrayList<>();
        private MixingRecipeOutput output;
        private HeatCondition requiredHeat = HeatCondition.NONE;
        private boolean disableCraftingWithCreate = false;

        private Builder(String name) { this.name = name; }

        public Builder require(ItemLike item) {
            return this.require(item, 1);
        }

        public Builder require(ItemLike item, int count) {
            this.ingredients.add(new MixingRecipeIngredient.Item(item, count));
            return this;
        }

        public Builder require(TagKey<Item> tag) {
            return this.require(tag, 1);
        }

        public Builder require(TagKey<Item> tag, int count) {
            this.ingredients.add(new MixingRecipeIngredient.Tag(tag, count));
            return this;
        }

        public Builder require(ModFluids.ChemicalFluidSet fluid) {
            return require(fluid, 1);
        }

        public Builder require(ModFluids.ChemicalFluidSet fluid, int multiplier) {
            return requireFluidExact(fluid, multiplier * FlaskItem.CAPACITY);
        }

        public Builder requireFluidExact(ModFluids.ChemicalFluidSet fluid, long amount) {
            this.ingredients.add(new MixingRecipeIngredient.Fluid(fluid, amount));
            return this;
        }

        public Builder output(ModFluids.ChemicalFluidSet fluid) {
            return this.output(fluid, 1);
        }

        public Builder output(ModFluids.ChemicalFluidSet fluid, int multiplier) {
            return outputFluidExact(fluid, multiplier * FlaskItem.CAPACITY);
        }

        public Builder outputFluidExact(ModFluids.ChemicalFluidSet fluid, long amount) {
            this.output = new MixingRecipeOutput.Fluid(fluid, amount);
            return this;
        }

        public Builder output(ItemStack item) {
            this.output = new MixingRecipeOutput.Item(item);
            return this;
        }

        public Builder requiresHeat(HeatCondition condition) {
            this.requiredHeat = condition;
            return this;
        }

        public Builder disableWithCreate() {
            this.disableCraftingWithCreate = true;
            return this;
        }

        public MixingRecipeDef build() {
            return new MixingRecipeDef(name, List.copyOf(ingredients), output, requiredHeat, disableCraftingWithCreate);
        }
    }
}