package jojoaky.substance.compat.recipeviewer;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Viewer-neutral description of an interaction performed directly in the world.
 * Recipe-viewer integrations should adapt this data instead of defining their own copies.
 */
@SuppressWarnings("UnstableApiUsage")
public record WorldInteractionDisplay(
        ResourceLocation id,
        List<InteractionInput> leftInputs,
        List<InteractionInput> rightInputs,
        List<ItemStack> outputs,
        String instructionKey,
        List<Object> instructionArguments,
        int durationTicks
) {
    public WorldInteractionDisplay {
        Objects.requireNonNull(id, "Interaction id cannot be null");

        leftInputs = List.copyOf(leftInputs);
        rightInputs = List.copyOf(rightInputs);
        outputs = copyStacks(outputs);
        instructionArguments = List.copyOf(instructionArguments);

        if (leftInputs.isEmpty() && rightInputs.isEmpty()) {
            throw new IllegalArgumentException(id + " must have at least one input");
        }

        if (outputs.isEmpty()) {
            throw new IllegalArgumentException(id + " must have at least one output");
        }

        if (instructionKey == null || instructionKey.isBlank()) {
            throw new IllegalArgumentException(id + " must have an instruction translation key");
        }

        if (durationTicks < 0) {
            throw new IllegalArgumentException(id + " has a negative duration");
        }
    }

    public Component instruction() {
        return Component.translatable(instructionKey, instructionArguments.toArray());
    }

    @Override
    public @NotNull List<ItemStack> outputs() {
        return copyStacks(outputs);
    }

    public static Builder builder(ResourceLocation id) {
        return new Builder(id);
    }

    private static List<ItemStack> copyStacks(List<ItemStack> stacks) {
        return stacks.stream()
                .map(stack -> Objects.requireNonNull(stack, "Output stack cannot be null").copy())
                .toList();
    }

    public static final class Builder {
        private final ResourceLocation id;
        private final List<InteractionInput> leftInputs = new ArrayList<>();
        private final List<InteractionInput> rightInputs = new ArrayList<>();
        private final List<ItemStack> outputs = new ArrayList<>();

        private String instructionKey;
        private final List<Object> instructionArguments = new ArrayList<>();
        private int durationTicks;

        private Builder(ResourceLocation id) {
            this.id = Objects.requireNonNull(id);
        }

        // EMI does not support catalysts on the left side
        public Builder leftInput(InteractionInput input) {
            leftInputs.add(Objects.requireNonNull(input));
            return this;
        }

        public Builder leftItem(Ingredient ingredient) {
            return leftInput(ItemInput.of(ingredient));
        }

        public Builder leftItem(Ingredient ingredient, int count) {
            return leftInput(ItemInput.of(ingredient, count));
        }

        @ApiStatus.Experimental
        public Builder leftItemCatalyst(Ingredient ingredient) {
            return leftInput(ItemInput.catalyst(ingredient));
        }

        @ApiStatus.Experimental
        public Builder leftItemCatalyst(Ingredient ingredient, int count) {
            return leftInput(ItemInput.catalyst(ingredient, count));
        }

        public Builder leftFluid(Fluid fluid, long amount) {
            return leftInput(FluidInput.of(fluid, amount));
        }

        public Builder leftFluid(List<Fluid> fluids, long amount) {
            return leftInput(FluidInput.of(fluids, amount));
        }

        @ApiStatus.Experimental
        public Builder leftFluidCatalyst(Fluid fluid, long amount) {
            return leftInput(FluidInput.catalyst(fluid, amount));
        }

        @ApiStatus.Experimental
        public Builder leftFluidCatalyst(List<Fluid> fluids, long amount) {
            return leftInput(FluidInput.catalyst(fluids, amount));
        }

        public Builder rightInput(InteractionInput input) {
            rightInputs.add(Objects.requireNonNull(input));
            return this;
        }

        public Builder rightItem(Ingredient ingredient) {
            return rightInput(ItemInput.of(ingredient));
        }

        public Builder rightItem(Ingredient ingredient, int count) {
            return rightInput(ItemInput.of(ingredient, count));
        }

        public Builder rightItemCatalyst(Ingredient ingredient) {
            return rightInput(ItemInput.catalyst(ingredient));
        }

        public Builder rightItemCatalyst(Ingredient ingredient, int count) {
            return rightInput(ItemInput.catalyst(ingredient, count));
        }

        public Builder rightFluid(Fluid fluid, long amount) {
            return rightInput(FluidInput.of(fluid, amount));
        }

        public Builder rightFluid(List<Fluid> fluids, long amount) {
            return rightInput(FluidInput.of(fluids, amount));
        }

        public Builder rightFluidCatalyst(Fluid fluid, long amount) {
            return rightInput(FluidInput.catalyst(fluid, amount));
        }

        public Builder rightFluidCatalyst(List<Fluid> fluids, long amount) {
            return rightInput(FluidInput.catalyst(fluids, amount));
        }

        public Builder output(ItemStack output) {
            outputs.add(Objects.requireNonNull(output));
            return this;
        }

        public Builder outputs(List<ItemStack> outputs) {
            this.outputs.addAll(outputs);
            return this;
        }

        public Builder instruction(String key, Object... args) {
            instructionKey = key;
            instructionArguments.clear();
            instructionArguments.addAll(Arrays.asList(args));
            return this;
        }

        public Builder duration(int ticks) {
            durationTicks = ticks;
            return this;
        }

        public WorldInteractionDisplay build() {
            return new WorldInteractionDisplay(
                    id,
                    leftInputs,
                    rightInputs,
                    outputs,
                    instructionKey,
                    instructionArguments,
                    durationTicks
            );
        }
    }

    public sealed interface InteractionInput
            permits ItemInput, FluidInput {

        boolean isCatalyst();
    }

    public record ItemInput(
            Ingredient ingredient,
            int count,
            boolean isCatalyst
    ) implements InteractionInput {

        public ItemInput {
            Objects.requireNonNull(ingredient, "Item ingredient cannot be null");

            if (ingredient.isEmpty()) {
                throw new IllegalArgumentException("Item ingredient cannot be empty");
            }

            if (count < 1) {
                throw new IllegalArgumentException("Item count must be positive");
            }
        }

        public static ItemInput of(Ingredient ingredient) {
            return new ItemInput(ingredient, 1, false);
        }

        public static ItemInput of(Ingredient ingredient, int count) {
            return new ItemInput(ingredient, count, false);
        }

        public static ItemInput catalyst(Ingredient ingredient) {
            return new ItemInput(ingredient, 1, true);
        }

        public static ItemInput catalyst(Ingredient ingredient, int count) {
            return new ItemInput(ingredient, count, true);
        }
    }

    /**
     * A fluid input.
     *
     * <p>{@code amount} is expressed in Fabric fluid droplets.
     * One bucket is {@link FluidConstants#BUCKET} droplets.</p>
     *
     * <p>The fluid list represents alternatives, similarly to how an
     * {@link Ingredient} may match multiple items.</p>
     */
    public record FluidInput(
            List<Fluid> fluids,
            long amount,
            boolean isCatalyst
    ) implements InteractionInput {

        public FluidInput {
            fluids = List.copyOf(fluids);

            if (fluids.isEmpty()) {
                throw new IllegalArgumentException("Fluid ingredient cannot be empty");
            }

            if (fluids.stream().anyMatch(fluid -> fluid == null || fluid == Fluids.EMPTY)) {
                throw new IllegalArgumentException("Fluid ingredient contains an empty fluid");
            }

            if (amount < FluidConstants.DROPLET) {
                throw new IllegalArgumentException("Fluid amount must be positive");
            }
        }

        public static FluidInput of(Fluid fluid, long amount) {
            return new FluidInput(List.of(fluid), amount, false);
        }

        public static FluidInput of(List<Fluid> fluids, long amount) {
            return new FluidInput(fluids, amount, false);
        }

        public static FluidInput catalyst(Fluid fluid, long amount) {
            return new FluidInput(List.of(fluid), amount, true);
        }

        public static FluidInput catalyst(List<Fluid> fluids, long amount) {
            return new FluidInput(fluids, amount, true);
        }
    }
}