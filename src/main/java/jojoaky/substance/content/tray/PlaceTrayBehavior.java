package jojoaky.substance.content.tray;

import jojoaky.substance.util.DispenserHelper;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlaceTrayBehavior extends OptionalDispenseItemBehavior {
    @Override
    protected @NotNull ItemStack execute(
            @NotNull BlockSource source,
            @NotNull ItemStack stack
    ) {
        InteractionResult result = DispenserHelper.placeBlock(source, stack);
        setSuccess(result == InteractionResult.SUCCESS);
        return stack;
    }
}
