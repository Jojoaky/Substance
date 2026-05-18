package jojoaky.substance.flask;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Set;

public class EmptyFlaskItem extends Item {
    public EmptyFlaskItem(Properties settings) {
        super(settings);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.MISS || blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        BlockPos blockPos = blockHitResult.getBlockPos();


        if (level.getFluidState(blockPos).is(FluidTags.WATER)) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);

            player.awardStat(Stats.ITEM_USED.get(this));

            if (!player.isCreative()) {
                stack.shrink(1);
                if (stack.getCount() == 0) player.setItemInHand(hand, new ItemStack(ModFlasks.WATER_FLASK));
                else player.addItem(new ItemStack(ModFlasks.WATER_FLASK));
            } else {
                if (!player.getInventory().hasAnyOf(Set.of(ModFlasks.WATER_FLASK))) {
                    player.addItem(new ItemStack(ModFlasks.WATER_FLASK));
                }
            }
        }

        return InteractionResultHolder.pass(stack);
    }
}