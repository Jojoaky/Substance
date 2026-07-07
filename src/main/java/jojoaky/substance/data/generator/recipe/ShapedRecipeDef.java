package jojoaky.substance.data.generator.recipe;

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

    public enum Operation {
        VANILLA_SHAPED(""),
        CREATE_MECHANICAL_CRAFTING("_mechanical_crafting");

        private final String suffix;

        Operation(String suffix) {
            this.suffix = suffix;
        }

        public String getSuffix() {
            return suffix;
        }

        public static Operation[] allVanilla() {
            return new Operation[] {
                    VANILLA_SHAPED
            };
        }

        public static Operation[] allReplacements() {
            return new Operation[] {
            };
        }

        public static Operation[] allCreate() {
            return new Operation[] {
                    CREATE_MECHANICAL_CRAFTING
            };
        }
    }

    private final Set<Operation> operations = EnumSet.noneOf(Operation.class);
    private final Map<Operation, String> nameOverrides = new EnumMap<>(Operation.class);

    private final List<ConditionJsonProvider> globalConditions = new ArrayList<>();
    private final Map<Operation, List<ConditionJsonProvider>> specificConditions = new EnumMap<>(Operation.class);

    private boolean mechanicalAllowMirrored = true;

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
        this.operations.add(Operation.VANILLA_SHAPED);
        return this;
    }

    public ShapedRecipeDef createMechanicalCrafting() {
        this.operations.add(Operation.CREATE_MECHANICAL_CRAFTING);
        return this;
    }

    public ShapedRecipeDef allowMirroredMechanical(boolean allowMirrored) {
        this.mechanicalAllowMirrored = allowMirrored;
        return this;
    }

    private final Set<ShapedRecipeDef.Operation> manualOnlyOperations = EnumSet.noneOf(ShapedRecipeDef.Operation.class);
    public ShapedRecipeDef manualOnly(ShapedRecipeDef.Operation... ops) {
        if (ops == null || ops.length == 0) {
            Collections.addAll(this.manualOnlyOperations, Operation.allVanilla());
            return this;
        }

        Collections.addAll(this.manualOnlyOperations, ops);
        return this;
    }
    public boolean isManualOnly(ShapedRecipeDef.Operation op) {
        return this.manualOnlyOperations.contains(op);
    }

    // Naming & Overrides

    public ShapedRecipeDef overrideName(Operation op, String customName) {
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

    public ShapedRecipeDef condition(ConditionJsonProvider condition, Operation... ops) {
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

    public List<String> getPattern() { return pattern; }
    public Map<Character, ShapeIngredient> getKey() { return key; }
    public List<ItemStack> getItemOutputs() { return itemOutputs; }
    public List<RandomItemOutput> getChancedItemOutputs() { return chancedItemOutputs; }
    public List<FluidOutput> getFluidOutputs() { return fluidOutputs; }

    public boolean isMechanicalMirrorAllowed() { return mechanicalAllowMirrored; }

    public Set<Operation> getOperations() { return Collections.unmodifiableSet(operations); }
    public boolean hasOperation(Operation op) { return operations.contains(op); }

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

    public ShapedRecipeDef requireModLoaded(String modId, ShapedRecipeDef.Operation... ops) {
        return this.condition(DefaultResourceConditions.allModsLoaded(modId), ops);
    }

    public ShapedRecipeDef requireModNotLoaded(String modId, ShapedRecipeDef.Operation... ops) {
        return this.condition(DefaultResourceConditions.not(DefaultResourceConditions.allModsLoaded(modId)), ops);
    }

    public ShapedRecipeDef requireAllModsLoaded(List<String> modIds, ShapedRecipeDef.Operation... ops) {
        return this.condition(DefaultResourceConditions.allModsLoaded(modIds.toArray(new String[0])), ops);
    }

    public ShapedRecipeDef requireAnyModLoaded(List<String> modIds, ShapedRecipeDef.Operation... ops) {
        return this.condition(DefaultResourceConditions.anyModLoaded(modIds.toArray(new String[0])), ops);
    }

    public ShapedRecipeDef requireNoModsLoaded(List<String> modIds, ShapedRecipeDef.Operation... ops) {
        return this.condition(
                DefaultResourceConditions.not(
                        DefaultResourceConditions.anyModLoaded(modIds.toArray(new String[0]))
                ),
                ops
        );
    }

    public ShapedRecipeDef requireNotAllModsLoaded(List<String> modIds, ShapedRecipeDef.Operation... ops) {
        return this.condition(
                DefaultResourceConditions.not(
                        DefaultResourceConditions.allModsLoaded(modIds.toArray(new String[0]))
                ),
                ops
        );
    }

    public ShapedRecipeDef requireTagsEmpty(List<TagKey<?>> tags, ShapedRecipeDef.Operation... ops) {
        return this.condition(
                DefaultResourceConditions.not(
                        DefaultResourceConditions.tagsPopulated(tags.toArray(new TagKey[0]))
                ),
                ops
        );
    }

    public ShapedRecipeDef requireTagsPopulated(List<TagKey<?>> tags, ShapedRecipeDef.Operation... ops) {
        return this.condition(DefaultResourceConditions.tagsPopulated(tags.toArray(new TagKey[0])), ops);
    }

    public ShapedRecipeDef useWeakReplacements() {
        return requireModNotLoaded("create", ShapedRecipeDef.Operation.allReplacements());
    }
}