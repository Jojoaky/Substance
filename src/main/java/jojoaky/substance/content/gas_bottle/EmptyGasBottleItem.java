package jojoaky.substance.content.gas_bottle;

import jojoaky.substance.register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class EmptyGasBottleItem extends Item {

    public EmptyGasBottleItem(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);

            if (state.is(Blocks.BUBBLE_COLUMN)) {
                boolean dragDown = state.getValue(BubbleColumnBlock.DRAG_DOWN);
                ItemStack filledStack = dragDown ? new ItemStack(ModItems.GAS_BOTTLE_HYDROGEN) : new ItemStack(ModItems.GAS_BOTTLE_OXYGEN);

                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                return InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(heldStack, player, filledStack), level.isClientSide());
            }
        }

        player.awardStat(Stats.ITEM_USED.get(ModItems.GAS_BOTTLE));
        return InteractionResultHolder.pass(heldStack);
    }
}