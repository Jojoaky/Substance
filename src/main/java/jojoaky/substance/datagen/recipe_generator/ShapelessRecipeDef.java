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

import java.util.*;
import java.util.stream.Stream;

public final class ShapelessRecipeDef implements RecipeDef {
    private final String name;

    private final List<ItemStack> itemInputs = new ArrayList<>();
    private final List<TagInput> tagInputs = new ArrayList<>();
    private final List<FluidInput> fluidInputs = new ArrayList<>();

    private final List<ItemStack> itemOutputs = new ArrayList<>();
    private final List<RandomItemOutput> chancedItemOutputs = new ArrayList<>();
    private final List<FluidOutput> fluidOutputs = new ArrayList<>();

    public enum Operation {
        VANILLA_SHAPELESS(""),
        VANILLA_BLASTING("_blasting"),
        VANILLA_CAMPFIRE_COOKING("_campfire"),
        VANILLA_SMELTING("_smelting"),
        VANILLA_SMOKING("_smoking"),

        CREATE_MIXING("_mixing"),
        CREATE_CRUSHING("_crushing"),
        CREATE_MILLING("_milling"),
        CREATE_PRESSING("_pressing"),
        CREATE_COMPACTING("_compacting"),
        CREATE_EMPTYING("_emptying"),
        CREATE_FILLING("_filling"),
        CREATE_HAUNTING("_haunting"),
        CREATE_WASHING("_washing"),

        CUSTOM_VANILLA_MIXING("_v_mixing"),
        CUSTOM_VANILLA_COMPACTING("_v_compacting"),
        CUSTOM_VANILLA_WASHING("_v_washing");

        private final String suffix;

        Operation(String suffix) {
            this.suffix = suffix;
        }

        public String getSuffix() {
            return suffix;
        }

        public static Operation[] allReplacements() {
            return new Operation[] {
                    CUSTOM_VANILLA_MIXING,
                    CUSTOM_VANILLA_COMPACTING,
                    CUSTOM_VANILLA_WASHING
            };
        }

        public static Operation[] allVanilla() {
            return new Operation[] {
                    VANILLA_SHAPELESS,
                    VANILLA_BLASTING,
                    VANILLA_CAMPFIRE_COOKING,
                    VANILLA_SMELTING,
                    VANILLA_SMOKING
            };
        }

        public static Operation[] allCreate() {
            return new Operation[] {
                    CREATE_MIXING,
                    CREATE_CRUSHING,
                    CREATE_MILLING,
                    CREATE_PRESSING,
                    CREATE_COMPACTING,
                    CREATE_EMPTYING,
                    CREATE_FILLING,
                    CREATE_HAUNTING,
                    CREATE_WASHING
            };
        }
    }

    private final Set<Operation> operations = EnumSet.noneOf(Operation.class);
    private final Map<Operation, String> nameOverrides = new EnumMap<>(Operation.class);

    private final List<ConditionJsonProvider> globalConditions = new ArrayList<>();
    private final Map<Operation, List<ConditionJsonProvider>> specificConditions = new EnumMap<>(Operation.class);

    private int blastDuration = 100;
    private float blastXP = 0;
    private int campfireDuration = 600;
    private float campfireXP = 600;
    private int smeltDuration = 200;
    private float smeltXP = 200;
    private int smokeDuration = 100;
    private float smokeXP = 100;

    private HeatCondition mixingHeat = HeatCondition.NONE;
    private HeatCondition compactingHeat = HeatCondition.NONE;

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
        this.operations.add(Operation.VANILLA_SHAPELESS);
        return this;
    }

    public ShapelessRecipeDef blasting() {
        return this.blasting(100, 0);
    }

    public ShapelessRecipeDef blasting(int duration, float xp) {
        this.operations.add(Operation.VANILLA_BLASTING);
        this.blastDuration = duration;
        this.blastXP = xp;
        return this;
    }

    public ShapelessRecipeDef vanillaCampfireCooking() {
        return this.vanillaCampfireCooking(600, 0);
    }

    public ShapelessRecipeDef vanillaCampfireCooking(int duration, float xp) {
        this.operations.add(Operation.VANILLA_CAMPFIRE_COOKING);
        this.campfireDuration = duration;
        this.campfireXP = xp;
        return this;
    }

    public ShapelessRecipeDef smelting() {
        return this.smelting(200, 0);
    }

    public ShapelessRecipeDef smelting(int duration, float xp) {
        this.operations.add(Operation.VANILLA_SMELTING);
        this.smeltDuration = duration;
        this.smeltXP = xp;
        return this;
    }

    public ShapelessRecipeDef smoking() {
        return this.smoking(100, 0);
    }

    public ShapelessRecipeDef smoking(int duration, float xp) {
        this.operations.add(Operation.VANILLA_SMOKING);
        this.smokeDuration = duration;
        this.smokeXP = xp;
        return this;
    }

    public ShapelessRecipeDef createMixing() {
        return this.createMixing(HeatCondition.NONE);
    }

    public ShapelessRecipeDef createMixing(HeatCondition condition) {
        this.operations.add(Operation.CREATE_MIXING);
        this.mixingHeat = condition;
        return this;
    }

    public ShapelessRecipeDef createCrushing() {
        this.operations.add(Operation.CREATE_CRUSHING);
        return this;
    }

    public ShapelessRecipeDef createMilling() {
        this.operations.add(Operation.CREATE_MILLING);
        return this;
    }

    public ShapelessRecipeDef createPressing() {
        this.operations.add(Operation.CREATE_PRESSING);
        return this;
    }

    public ShapelessRecipeDef createCompacting() {
        return this.createCompacting(HeatCondition.NONE);
    }

    public ShapelessRecipeDef createCompacting(HeatCondition condition) {
        this.operations.add(Operation.CREATE_COMPACTING);
        this.compactingHeat = condition;
        return this;
    }

    public ShapelessRecipeDef createEmptying() {
        this.operations.add(Operation.CREATE_EMPTYING);
        return this;
    }

    public ShapelessRecipeDef createFilling() {
        this.operations.add(Operation.CREATE_FILLING);
        return this;
    }

    public ShapelessRecipeDef createHaunting() {
        this.operations.add(Operation.CREATE_HAUNTING);
        return this;
    }

    public ShapelessRecipeDef createWashing() {
        this.operations.add(Operation.CREATE_WASHING);
        return this;
    }

    public ShapelessRecipeDef generateVanillaMixing() {
        this.operations.add(Operation.CUSTOM_VANILLA_MIXING);
        return this;
    }

    public ShapelessRecipeDef generateVanillaCompacting() {
        this.operations.add(Operation.CUSTOM_VANILLA_COMPACTING);
        return this;
    }

    public ShapelessRecipeDef generateVanillaWashing() {
        this.operations.add(Operation.CUSTOM_VANILLA_WASHING);
        return this;
    }


    // Naming & Overrides

    public ShapelessRecipeDef overrideName(Operation op, String customName) {
        this.nameOverrides.put(op, customName);
        return this;
    }

    public String getRecipeName(Operation op) {
        String defaultName = this.name + op.getSuffix();
        String resolvedName = this.nameOverrides.getOrDefault(op, defaultName);

        if (this.isManualOnly(op)) {
            resolvedName += "_manual_only";
        }

        return resolvedName;
    }

    public String getBaseName() { return name; }


    // Conditions

    public ShapelessRecipeDef condition(ConditionJsonProvider condition, Operation... ops) {
        if (ops == null || ops.length == 0) {
            this.globalConditions.add(condition);
        } else {
            for (Operation op : ops) {
                this.specificConditions.computeIfAbsent(op, k -> new ArrayList<>()).add(condition);
            }
        }
        return this;
    }

    public List<ConditionJsonProvider> getConditionsFor(Operation op) {
        List<ConditionJsonProvider> allConditions = new ArrayList<>(globalConditions);
        allConditions.addAll(specificConditions.getOrDefault(op, Collections.emptyList()));
        return allConditions;
    }

    private final Set<Operation> manualOnlyOperations = EnumSet.noneOf(Operation.class);
    public ShapelessRecipeDef manualOnly(Operation... ops) {
        if (ops == null || ops.length == 0) {
            Collections.addAll(this.manualOnlyOperations, Operation.allVanilla());
            return this;
        }

        Collections.addAll(this.manualOnlyOperations, ops);
        return this;
    }
    public boolean isManualOnly(Operation op) {
        return this.manualOnlyOperations.contains(op);
    }

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

    public List<ItemStack> getItemInputs() { return itemInputs; }
    public List<TagInput> getTagInputs() { return tagInputs; }
    public List<FluidInput> getFluidInputs() { return fluidInputs; }
    public List<ItemStack> getItemOutputs() { return itemOutputs; }
    public List<RandomItemOutput> getChancedItemOutputs() { return chancedItemOutputs; }
    public List<FluidOutput> getFluidOutputs() { return fluidOutputs; }

    public Set<Operation> getOperations() { return Collections.unmodifiableSet(operations); }
    public boolean hasOperation(Operation op) { return operations.contains(op); }

    public int getBlastDuration() { return blastDuration; }
    public int getSmeltDuration() { return smeltDuration; }
    public int getSmokeDuration() { return smokeDuration; }
    public int getCampfireDuration() { return campfireDuration; }

    public float getBlastXP() { return blastXP; }
    public float getSmeltXP() { return smeltXP; }
    public float getSmokeXP() { return smokeXP; }
    public float getCampfireXP() { return campfireXP; }

    public HeatCondition getMixingHeat() { return mixingHeat; }
    public HeatCondition getCompactingHeat() { return compactingHeat; }

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


    public ShapelessRecipeDef requireModLoaded(String modId, Operation... ops) {
        return this.condition(DefaultResourceConditions.allModsLoaded(modId), ops);
    }

    public ShapelessRecipeDef requireModNotLoaded(String modId, Operation... ops) {
        return this.condition(DefaultResourceConditions.not(DefaultResourceConditions.allModsLoaded(modId)), ops);
    }

    public ShapelessRecipeDef requireAllModsLoaded(List<String> modIds, Operation... ops) {
        return this.condition(DefaultResourceConditions.allModsLoaded(modIds.toArray(new String[0])), ops);
    }

    public ShapelessRecipeDef requireAnyModLoaded(List<String> modIds, Operation... ops) {
        return this.condition(DefaultResourceConditions.anyModLoaded(modIds.toArray(new String[0])), ops);
    }

    public ShapelessRecipeDef requireNoModsLoaded(List<String> modIds, Operation... ops) {
        return this.condition(
                DefaultResourceConditions.not(
                        DefaultResourceConditions.anyModLoaded(modIds.toArray(new String[0]))
                ),
                ops
        );
    }

    public ShapelessRecipeDef requireNotAllModsLoaded(List<String> modIds, Operation... ops) {
        return this.condition(
                DefaultResourceConditions.not(
                        DefaultResourceConditions.allModsLoaded(modIds.toArray(new String[0]))
                ),
                ops
        );
    }

    public ShapelessRecipeDef requireTagsEmpty(List<TagKey<?>> tags, Operation... ops) {
        return this.condition(
                DefaultResourceConditions.not(
                        DefaultResourceConditions.tagsPopulated(tags.toArray(new TagKey[0]))
                ),
                ops
        );
    }

    public ShapelessRecipeDef requireTagsPopulated(List<TagKey<?>> tags, Operation... ops) {
        return this.condition(DefaultResourceConditions.tagsPopulated(tags.toArray(new TagKey[0])), ops);
    }

    public ShapelessRecipeDef useWeakReplacements() {
        return requireModNotLoaded("create", Operation.allReplacements());
    }
}