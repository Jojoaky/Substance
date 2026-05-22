package jojoaky.substance.datagen.recipe_generator;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import jojoaky.substance.Substance;
import jojoaky.substance.content.flask.FilledFlaskItem;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

import java.util.*;
import java.util.stream.Stream;

public final class ShapedRecipeDef implements RecipeDef {
    private final String name;

    private final List<String> pattern = new ArrayList<>();
    private final Map<Character, ShapeIngredient> key = new LinkedHashMap<>();

    private final List<ItemStack> itemOutputs = new ArrayList<>();
    private final List<RandomItemOutput> chancedItemOutputs = new ArrayList<>();
    private final List<FluidOutput> fluidOutputs = new ArrayList<>();

    private boolean vanillaShaped = false;
    private boolean createMechanicalCrafting = false;
    private boolean mechanicalAllowMirrored = true;

    private boolean disableVanillaIfCreate = false;

    public sealed interface ShapeIngredient permits ShapeIngredient.OfItem, ShapeIngredient.OfTag {
        record OfItem(ItemLike item) implements ShapeIngredient {}
        record OfTag(TagKey<Item> tag) implements ShapeIngredient {}
    }

    public record FluidOutput(Fluid fluid, long amount) {}
    public record RandomItemOutput(ItemStack stack, float chance) {}

    private ShapedRecipeDef(String name) { this.name = name; }

    public static ShapedRecipeDef named(String name) {
        return new ShapedRecipeDef(name);
    }


    // Pattern

    public ShapedRecipeDef pattern(String... rows) {
        this.pattern.addAll(Arrays.asList(rows));
        return this;
    }

    public ShapedRecipeDef key(char symbol, ItemLike item) {
        this.key.put(symbol, new ShapeIngredient.OfItem(item));
        return this;
    }

    public ShapedRecipeDef key(char symbol, TagKey<Item> tag) {
        this.key.put(symbol, new ShapeIngredient.OfTag(tag));
        return this;
    }


    // Outputs

    public ShapedRecipeDef output(ItemLike item) {
        return this.output(item, 1);
    }

    public ShapedRecipeDef output(ItemLike item, int count) {
        this.itemOutputs.add(new ItemStack(item, count));
        return this;
    }

    public ShapedRecipeDef output(ItemStack stack) {
        this.itemOutputs.add(stack.copy());
        return this;
    }

    public ShapedRecipeDef output(Fluid fluid, long amount) {
        this.fluidOutputs.add(new FluidOutput(fluid, amount));
        return this;
    }

    public ShapedRecipeDef output(ItemLike item, float chance) {
        return this.output(item, 1, chance);
    }

    public ShapedRecipeDef output(ItemLike item, int count, float chance) {
        this.chancedItemOutputs.add(new RandomItemOutput(new ItemStack(item, count), chance));
        return this;
    }


    public ShapedRecipeDef vanillaShaped() {
        this.vanillaShaped = true;
        return this;
    }

    public ShapedRecipeDef createMechanicalCrafting() {
        this.createMechanicalCrafting = true;
        return this;
    }

    public ShapedRecipeDef disableVanillaIfCreate() {
        this.disableVanillaIfCreate = true;
        return this;
    }

    public ShapedRecipeDef AllowMirroredMechanical(boolean allowMirrored) {
        this.mechanicalAllowMirrored = allowMirrored;
        return this;
    }

    private final List<ConditionJsonProvider> conditions = new ArrayList<>();

    public ShapedRecipeDef condition(ConditionJsonProvider condition) {
        this.conditions.add(condition);
        return this;
    }

    public List<ConditionJsonProvider> getConditions() {
        return conditions;
    }

    public ShapedRecipeDef build() {
        validateRecipe();
        return this;
    }

    private void validateRecipe() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Recipe name cannot be null or empty.");
        }

        if (pattern.isEmpty()) {
            throw new IllegalStateException("Recipe '" + name + "' must define a pattern.");
        }

        for (String row : pattern) {
            for (char c : row.toCharArray()) {
                if (c != ' ' && !key.containsKey(c)) {
                    throw new IllegalStateException("Recipe '" + name + "' uses symbol '" + c + "' with no key defined.");
                }
            }
        }

        for (char c : key.keySet()) {
            boolean used = pattern.stream().anyMatch(row -> row.indexOf(c) >= 0);
            if (!used) {
                Substance.LOGGER.warn("Recipe '{}' defines key '{}' that is never used in the pattern.", name, c);
            }
        }

        boolean hasOutputs = !itemOutputs.isEmpty() || !chancedItemOutputs.isEmpty() || !fluidOutputs.isEmpty();
        if (!hasOutputs) {
            throw new IllegalStateException("Recipe '" + name + "' must specify at least one output.");
        }
    }

    public String getName() { return name; }
    public List<String> getPattern() { return pattern; }
    public Map<Character, ShapeIngredient> getKey() { return key; }
    public List<ItemStack> getItemOutputs() { return itemOutputs; }
    public List<RandomItemOutput> getChancedItemOutputs() { return chancedItemOutputs; }
    public List<FluidOutput> getFluidOutputs() { return fluidOutputs; }

    public boolean isMechanicalMirrorAllowed() { return mechanicalAllowMirrored; }

    public boolean isVanillaShaped() { return vanillaShaped; }
    public boolean isCreateMechanicalCrafting() { return createMechanicalCrafting; }
    public boolean isDisableVanillaIfCreate() { return disableVanillaIfCreate; }

    public static ItemStack convertFluidOutputToFlask(FluidOutput fluidOutput) {
        FilledFlaskItem item = FilledFlaskItem.getFlaskForFluid(fluidOutput.fluid());
        if (item == null) {
            throw new IllegalArgumentException("Required Flask for Fluid " + fluidOutput.fluid() + " could not be found!");
        }
        int count = (int) (fluidOutput.amount() / FilledFlaskItem.CAPACITY);
        return new ItemStack(item, count);
    }

    public Stream<ItemStack> getFluidOutputsAsFlasks() {
        return fluidOutputs.stream().map(ShapedRecipeDef::convertFluidOutputToFlask);
    }

    public Stream<ItemStack> getAllOutputsAsItems() {
        return Stream.concat(itemOutputs.stream(), getFluidOutputsAsFlasks());
    }

    public ItemStack getSingleItemOutput() {
        if (itemOutputs.size() > 1) {
            throw new IllegalStateException("Recipe '" + name + "' expects a single standard Item output, but contains multiple entries.");
        }
        if (itemOutputs.isEmpty()) {
            throw new IllegalStateException("Recipe '" + name + "' does not contain a definitive standard Item output.");
        }
        return itemOutputs.getFirst();
    }

    public ItemStack getSingleOutputAsItem() {
        long totalOutputs = itemOutputs.size() + fluidOutputs.size();
        if (totalOutputs > 1) {
            throw new IllegalStateException("Recipe '" + name + "' expects exactly one singular item or fluid outcome, but multiple exist.");
        }
        if (!itemOutputs.isEmpty()) return itemOutputs.getFirst();
        if (!fluidOutputs.isEmpty()) return convertFluidOutputToFlask(fluidOutputs.getFirst());
        throw new IllegalStateException("Recipe '" + name + "' does not yield any non-chanced physical item or flask outputs.");
    }
}