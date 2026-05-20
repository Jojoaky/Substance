package jojoaky.substance.datagen.recipe_generator;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import jojoaky.substance.content.flask.FilledFlaskItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RecipeDef {
    private final String name;

    private final List<ItemStack> itemInputs = new ArrayList<>();
    private final List<TagInput> tagInputs = new ArrayList<>();
    private final List<FluidInput> fluidInputs = new ArrayList<>();

    private final List<ItemStack> itemOutputs = new ArrayList<>();
    private final List<RandomItemOutput> chancedItemOutputs = new ArrayList<>();
    private final List<FluidOutput> fluidOutputs = new ArrayList<>();

    private boolean vanillaShapeless = false;
    private boolean vanillaBlasting = false;
    private long blastDuration = 100;
    private boolean vanillaCampfireCooking = false;
    private long campfireDuration = 600;
    private boolean vanillaSmelting = false;
    private long smeltDuration = 200;
    private boolean vanillaSmoking = false;
    private long smokeDuration = 100;

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
    private boolean createSplashing = false;

    private boolean customVanillaMixing = false;
    private boolean customVanillaCompacting = false;
    private boolean customVanillaSplashing = false;

    private boolean disableVanillaIfCreate = false;

    public record TagInput(TagKey<Item> tag, int count) {}
    public record FluidInput(Fluid fluid, long amount) {}
    public record FluidOutput(Fluid fluid, long amount) {}
    public record RandomItemOutput(ItemStack stack, float chance) {}

    private RecipeDef(String name) { this.name = name; }

    public static RecipeDef named(String name) {
        return new RecipeDef(name);
    }

    // Inputs

    public RecipeDef require(ItemLike item) {
        return this.require(item, 1);
    }

    public RecipeDef require(ItemLike item, int count) {
        this.itemInputs.add(new ItemStack(item, count));
        return this;
    }

    public RecipeDef require(TagKey<Item> tag) {
        return this.require(tag, 1);
    }

    public RecipeDef require(TagKey<Item> tag, int count) {
        this.tagInputs.add(new TagInput(tag, count));
        return this;
    }

    public RecipeDef require(Fluid fluid, long amount) {
        this.fluidInputs.add(new FluidInput(fluid, amount));
        return this;
    }


    // Outputs

    public RecipeDef output(ItemLike item) {
        return this.output(item, 1);
    }

    public RecipeDef output(ItemLike item, int count) {
        this.itemOutputs.add(new ItemStack(item, count));
        return this;
    }

    public RecipeDef output(ItemStack stack) {
        this.itemOutputs.add(stack.copy());
        return this;
    }

    public RecipeDef output(Fluid fluid, long amount) {
        this.fluidOutputs.add(new FluidOutput(fluid, amount));
        return this;
    }

    public RecipeDef output(ItemLike item, float chance) {
        return this.output(item, 1, chance);
    }

    public RecipeDef output(ItemLike item, int count, float chance) {
        this.chancedItemOutputs.add(new RandomItemOutput(new ItemStack(item, count), chance));
        return this;
    }


    // Operations

    public RecipeDef vanillaShapeless() {
        this.vanillaShapeless = true;
        return this;
    }

    public RecipeDef vanillaBlasting() {
        return this.vanillaBlasting(100);
    }

    public RecipeDef vanillaBlasting(long duration) {
        this.vanillaBlasting = true;
        this.blastDuration = duration;
        return this;
    }

    public RecipeDef vanillaCampfireCooking() {
        return this.vanillaCampfireCooking(600);
    }

    public RecipeDef vanillaCampfireCooking(long duration) {
        this.vanillaCampfireCooking = true;
        this.campfireDuration = duration;
        return this;
    }

    public RecipeDef vanillaSmelting() {
        return this.vanillaSmelting(200);
    }

    public RecipeDef vanillaSmelting(long duration) {
        this.vanillaSmelting = true;
        this.smeltDuration = duration;
        return this;
    }

    public RecipeDef vanillaSmoking() {
        return this.vanillaSmoking(100);
    }

    public RecipeDef vanillaSmoking(long duration) {
        this.vanillaSmoking = true;
        this.smokeDuration = duration;
        return this;
    }

    public RecipeDef createMixing() {
        return this.createMixing(HeatCondition.NONE);
    }

    public RecipeDef createMixing(HeatCondition condition) {
        this.createMixing = true;
        this.mixingHeat = condition;
        return this;
    }

    public RecipeDef createCrushing() {
        this.createCrushing = true;
        return this;
    }

    public RecipeDef createMilling() {
        this.createMilling = true;
        return this;
    }

    public RecipeDef createPressing() {
        this.createPressing = true;
        return this;
    }

    public RecipeDef createCompacting() {
        return this.createCompacting(HeatCondition.NONE);
    }

    public RecipeDef createCompacting(HeatCondition condition) {
        this.createCompacting = true;
        this.compactingHeat = condition;
        return this;
    }

    public RecipeDef createEmptying() {
        this.createEmptying = true;
        return this;
    }

    public RecipeDef createFilling() {
        this.createFilling = true;
        return this;
    }

    public RecipeDef createHaunting() {
        this.createHaunting = true;
        return this;
    }

    public RecipeDef createSplashing() {
        this.createSplashing = true;
        return this;
    }

    public RecipeDef generateVanillaMixing() {
        this.customVanillaMixing = true;
        return this;
    }

    public RecipeDef generateVanillaCompacting() {
        this.customVanillaCompacting = true;
        return this;
    }

    public RecipeDef generateVanillaSplashing() {
        this.customVanillaSplashing = true;
        return this;
    }


    public RecipeDef disableVanillaIfCreate() {
        this.disableVanillaIfCreate = true;
        return this;
    }


    public RecipeDef build() {
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
    public boolean isVanillaBlasting() { return vanillaBlasting; }
    public long getBlastDuration() { return blastDuration; }
    public boolean isVanillaCampfireCooking() { return vanillaCampfireCooking; }
    public long getCampfireDuration() { return campfireDuration; }
    public boolean isVanillaSmelting() { return vanillaSmelting; }
    public long getSmeltDuration() { return smeltDuration; }
    public boolean isVanillaSmoking() { return vanillaSmoking; }
    public long getSmokeDuration() { return smokeDuration; }

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
    public boolean isCreateSplashing() { return createSplashing; }

    public boolean isCustomVanillaMixing() { return customVanillaMixing; }
    public boolean isCustomVanillaCompacting() { return customVanillaCompacting; }
    public boolean isCustomVanillaSplashing() { return customVanillaSplashing; }

    public boolean isDisableVanillaIfCreate() { return disableVanillaIfCreate; }




    public static ItemStack convertFluidInputToFlask(RecipeDef.FluidInput fluidInput) {
        FilledFlaskItem item = FilledFlaskItem.getFlaskForFluid(fluidInput.fluid());
        if (item == null) {
            throw new IllegalArgumentException("Required Flask for Fluid " + fluidInput.fluid().toString() + " could not be found!");
        }
        int count = (int) (fluidInput.amount() / FilledFlaskItem.CAPACITY);
        return new ItemStack(item, count);
    }

    public static ItemStack convertFluidOutputToFlask(RecipeDef.FluidOutput fluidOutput) {
        FilledFlaskItem item = FilledFlaskItem.getFlaskForFluid(fluidOutput.fluid());
        if (item == null) {
            throw new IllegalArgumentException("Required Flask for Fluid " + fluidOutput.fluid().toString() + " could not be found!");
        }
        int count = (int) (fluidOutput.amount() / FilledFlaskItem.CAPACITY);
        return new ItemStack(item, count);
    }

    public Stream<ItemStack> getFluidInputsAsFlasks() {
        return fluidInputs.stream().map(RecipeDef::convertFluidInputToFlask);
    }

    public Stream<ItemStack> getFluidOutputsAsFlasks() {
        return fluidOutputs.stream().map(RecipeDef::convertFluidOutputToFlask);
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
}