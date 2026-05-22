package jojoaky.substance.datagen.recipe_generator;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import jojoaky.substance.content.flask.FilledFlaskItem;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class ShapelessRecipeDef implements RecipeDef {
    private final String name;

    private final List<ItemStack> itemInputs = new ArrayList<>();
    private final List<TagInput> tagInputs = new ArrayList<>();
    private final List<FluidInput> fluidInputs = new ArrayList<>();

    private final List<ItemStack> itemOutputs = new ArrayList<>();
    private final List<RandomItemOutput> chancedItemOutputs = new ArrayList<>();
    private final List<FluidOutput> fluidOutputs = new ArrayList<>();

    private boolean vanillaShapeless = false;
    private boolean vanillaBlasting = false;
    private int blastDuration = 100;
    private float blastXP = 0;
    private boolean vanillaCampfireCooking = false;
    private int campfireDuration = 600;
    private float campfireXP = 600;
    private boolean vanillaSmelting = false;
    private int smeltDuration = 200;
    private float smeltXP = 200;
    private boolean vanillaSmoking = false;
    private int smokeDuration = 100;
    private float smokeXP = 100;

    private boolean createMixing = false;
    private HeatCondition mixingHeat = HeatCondition.NONE;
    private boolean createCrushing = false;
    private boolean createMilling = false;
    private boolean createPressing = false;
    private boolean createCompacting = false;
    private HeatCondition compactingHeat = HeatCondition.NONE;
    private boolean createEmptying = false;
    private boolean createFilling = false;
    private boolean createHaunting = false;
    private boolean createWashing = false;

    private boolean customVanillaMixing = false;
    private boolean customVanillaCompacting = false;
    private boolean customVanillaWashing = false;

    public record TagInput(TagKey<Item> tag, int count) {}
    public record FluidInput(Fluid fluid, long amount) {}
    public record FluidOutput(Fluid fluid, long amount) {}
    public record RandomItemOutput(ItemStack stack, float chance) {}

    private ShapelessRecipeDef(String name) { this.name = name; }

    public static ShapelessRecipeDef named(String name) {
        return new ShapelessRecipeDef(name);
    }

    // Inputs

    public ShapelessRecipeDef require(ItemLike item) {
        return this.require(item, 1);
    }

    public ShapelessRecipeDef require(ItemLike item, int count) {
        this.itemInputs.add(new ItemStack(item, count));
        return this;
    }

    public ShapelessRecipeDef require(TagKey<Item> tag) {
        return this.require(tag, 1);
    }

    public ShapelessRecipeDef require(TagKey<Item> tag, int count) {
        this.tagInputs.add(new TagInput(tag, count));
        return this;
    }

    public ShapelessRecipeDef require(Fluid fluid, long amount) {
        this.fluidInputs.add(new FluidInput(fluid, amount));
        return this;
    }


    // Outputs

    public ShapelessRecipeDef output(ItemLike item) {
        return this.output(item, 1);
    }

    public ShapelessRecipeDef output(ItemLike item, int count) {
        this.itemOutputs.add(new ItemStack(item, count));
        return this;
    }

    public ShapelessRecipeDef output(ItemStack stack) {
        this.itemOutputs.add(stack.copy());
        return this;
    }

    public ShapelessRecipeDef output(Fluid fluid, long amount) {
        this.fluidOutputs.add(new FluidOutput(fluid, amount));
        return this;
    }

    public ShapelessRecipeDef output(ItemLike item, float chance) {
        return this.output(item, 1, chance);
    }

    public ShapelessRecipeDef output(ItemLike item, int count, float chance) {
        this.chancedItemOutputs.add(new RandomItemOutput(new ItemStack(item, count), chance));
        return this;
    }


    // Operations

    public ShapelessRecipeDef vanillaShapeless() {
        this.vanillaShapeless = true;
        return this;
    }

    public ShapelessRecipeDef blasting() {
        return this.blasting(100, 0);
    }

    public ShapelessRecipeDef blasting(int duration, float xp) {
        this.vanillaBlasting = true;
        this.blastDuration = duration;
        this.blastXP = xp;
        return this;
    }

    public ShapelessRecipeDef vanillaCampfireCooking() {
        return this.vanillaCampfireCooking(600, 0);
    }

    public ShapelessRecipeDef vanillaCampfireCooking(int duration, float xp) {
        this.vanillaCampfireCooking = true;
        this.campfireDuration = duration;
        this.campfireXP = xp;
        return this;
    }

    public ShapelessRecipeDef smelting() {
        return this.smelting(200, 0);
    }

    public ShapelessRecipeDef smelting(int duration, float xp) {
        this.vanillaSmelting = true;
        this.smeltDuration = duration;
        this.smeltXP = xp;
        return this;
    }

    public ShapelessRecipeDef smoking() {
        return this.smoking(100, 0);
    }

    public ShapelessRecipeDef smoking(int duration, float xp) {
        this.vanillaSmoking = true;
        this.smokeDuration = duration;
        this.smokeXP = xp;
        return this;
    }

    public ShapelessRecipeDef createMixing() {
        return this.createMixing(HeatCondition.NONE);
    }

    public ShapelessRecipeDef createMixing(HeatCondition condition) {
        this.createMixing = true;
        this.mixingHeat = condition;
        return this;
    }

    public ShapelessRecipeDef createCrushing() {
        this.createCrushing = true;
        return this;
    }

    public ShapelessRecipeDef createMilling() {
        this.createMilling = true;
        return this;
    }

    public ShapelessRecipeDef createPressing() {
        this.createPressing = true;
        return this;
    }

    public ShapelessRecipeDef createCompacting() {
        return this.createCompacting(HeatCondition.NONE);
    }

    public ShapelessRecipeDef createCompacting(HeatCondition condition) {
        this.createCompacting = true;
        this.compactingHeat = condition;
        return this;
    }

    public ShapelessRecipeDef createEmptying() {
        this.createEmptying = true;
        return this;
    }

    public ShapelessRecipeDef createFilling() {
        this.createFilling = true;
        return this;
    }

    public ShapelessRecipeDef createHaunting() {
        this.createHaunting = true;
        return this;
    }

    public ShapelessRecipeDef createWashing() {
        this.createWashing = true;
        return this;
    }

    public ShapelessRecipeDef generateVanillaMixing() {
        this.customVanillaMixing = true;
        return this;
    }

    public ShapelessRecipeDef generateVanillaCompacting() {
        this.customVanillaCompacting = true;
        return this;
    }

    public ShapelessRecipeDef generateVanillaWashing() {
        this.customVanillaWashing = true;
        return this;
    }


    private final List<ConditionJsonProvider> conditions = new ArrayList<>();

    public ShapelessRecipeDef condition(ConditionJsonProvider condition) {
        this.conditions.add(condition);
        return this;
    }

    public List<ConditionJsonProvider> getConditions() {
        return conditions;
    }

    private boolean disableVanillaIfCreate = false;
    public ShapelessRecipeDef disableVanillaIfCreate() {
        this.disableVanillaIfCreate = true;
        return this;
    }
    public boolean isDisableVanillaIfCreate() { return disableVanillaIfCreate; }


    public ShapelessRecipeDef build() {
        validateRecipe();
        return this;
    }

    private void validateRecipe() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Recipe name cannot be null or empty.");
        }

        boolean hasInputs = !itemInputs.isEmpty() || !tagInputs.isEmpty() || !fluidInputs.isEmpty();
        boolean hasOutputs = !itemOutputs.isEmpty() || !chancedItemOutputs.isEmpty() || !fluidOutputs.isEmpty();

        if (!hasInputs) {
            throw new IllegalStateException("Recipe '" + name + "' must specify at least one input ingredient.");
        }
        if (!hasOutputs) {
            throw new IllegalStateException("Recipe '" + name + "' must specify at least one output.");
        }
    }

    public String getName() { return name; }
    public List<ItemStack> getItemInputs() { return itemInputs; }
    public List<TagInput> getTagInputs() { return tagInputs; }
    public List<FluidInput> getFluidInputs() { return fluidInputs; }
    public List<ItemStack> getItemOutputs() { return itemOutputs; }
    public List<RandomItemOutput> getChancedItemOutputs() { return chancedItemOutputs; }
    public List<FluidOutput> getFluidOutputs() { return fluidOutputs; }

    public boolean isVanillaShapeless() { return vanillaShapeless; }
    public boolean isBlasting() { return vanillaBlasting; }
    public boolean isSmelting() { return vanillaSmelting; }
    public boolean isSmoking() { return vanillaSmoking; }
    public boolean isVanillaCampfireCooking() { return vanillaCampfireCooking; }

    public int getBlastDuration() { return blastDuration; }
    public int getSmeltDuration() { return smeltDuration; }
    public int getSmokeDuration() { return smokeDuration; }
    public int getCampfireDuration() { return campfireDuration; }

    public float getBlastXP() { return blastXP; }
    public float getSmeltXP() { return smeltXP; }
    public float getSmokeXP() { return smokeXP; }
    public float getCampfireXP() { return campfireXP; }

    public boolean isCreateMixing() { return createMixing; }
    public HeatCondition getMixingHeat() { return mixingHeat; }
    public boolean isCreateCrushing() { return createCrushing; }
    public boolean isCreateMilling() { return createMilling; }
    public boolean isCreatePressing() { return createPressing; }
    public boolean isCreateCompacting() { return createCompacting; }
    public HeatCondition getCompactingHeat() { return compactingHeat; }
    public boolean isCreateEmptying() { return createEmptying; }
    public boolean isCreateFilling() { return createFilling; }
    public boolean isCreateHaunting() { return createHaunting; }
    public boolean isCreateWashing() { return createWashing; }

    public boolean isCustomVanillaMixing() { return customVanillaMixing; }
    public boolean isCustomVanillaCompacting() { return customVanillaCompacting; }
    public boolean isCustomVanillaWashing() { return customVanillaWashing; }

    public static ItemStack convertFluidInputToFlask(ShapelessRecipeDef.FluidInput fluidInput) {
        FilledFlaskItem item = FilledFlaskItem.getFlaskForFluid(fluidInput.fluid());
        if (item == null) {
            throw new IllegalArgumentException("Required Flask for Fluid " + fluidInput.fluid().toString() + " could not be found!");
        }
        int count = (int) (fluidInput.amount() / FilledFlaskItem.CAPACITY);
        return new ItemStack(item, count);
    }

    public static ItemStack convertFluidOutputToFlask(ShapelessRecipeDef.FluidOutput fluidOutput) {
        FilledFlaskItem item = FilledFlaskItem.getFlaskForFluid(fluidOutput.fluid());
        if (item == null) {
            throw new IllegalArgumentException("Required Flask for Fluid " + fluidOutput.fluid().toString() + " could not be found!");
        }
        int count = (int) (fluidOutput.amount() / FilledFlaskItem.CAPACITY);
        return new ItemStack(item, count);
    }

    public Stream<ItemStack> getFluidInputsAsFlasks() {
        return fluidInputs.stream().map(ShapelessRecipeDef::convertFluidInputToFlask);
    }

    public Stream<ItemStack> getFluidOutputsAsFlasks() {
        return fluidOutputs.stream().map(ShapelessRecipeDef::convertFluidOutputToFlask);
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

    public FluidOutput getSingleFluidOutput() {
        if (fluidOutputs.size() > 1) {
            throw new IllegalStateException("Recipe '" + name + "' expects a single Fluid output, but contains multiple entries.");
        }
        if (fluidOutputs.isEmpty()) {
            throw new IllegalStateException("Recipe '" + name + "' does not contain a definitive Fluid output.");
        }
        return fluidOutputs.getFirst();
    }

    public ItemStack getSingleOutputAsItem() {
        long totalOutputs = itemOutputs.size() + fluidOutputs.size();

        if (totalOutputs > 1) {
            throw new IllegalStateException("Recipe '" + name + "' expects exactly one singular item or fluid outcome, but multiple exist.");
        }
        if (!itemOutputs.isEmpty()) {
            return itemOutputs.getFirst();
        }
        if (!fluidOutputs.isEmpty()) {
            return convertFluidOutputToFlask(fluidOutputs.getFirst());
        }

        throw new IllegalStateException("Recipe '" + name + "' does not yield any non-chanced physical item or flask outputs.");
    }

    public Ingredient getSingleInputAsIngredient() {
        int totalInputs = itemInputs.size() + tagInputs.size() + fluidInputs.size();

        if (totalInputs > 1) {
            throw new IllegalStateException("Recipe '" + name + "' expects exactly one singular input ingredient, but multiple exist.");
        }

        if (!itemInputs.isEmpty()) {
            return Ingredient.of(itemInputs.getFirst());
        }
        if (!tagInputs.isEmpty()) {
            return Ingredient.of(tagInputs.getFirst().tag());
        }
        if (!fluidInputs.isEmpty()) {
            ItemStack flask = convertFluidInputToFlask(fluidInputs.getFirst());
            return Ingredient.of(flask);
        }

        throw new IllegalStateException("Recipe '" + name + "' does not contain any input ingredients.");
    }
}