package jojoaky.substance.content.gas_bottle;

import jojoaky.substance.register.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FilledGasBottleItem extends Item {

    public FilledGasBottleItem(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
                                                            @NotNull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH,
                SoundSource.PLAYERS, 0.5F, 1.2F);
        player.awardStat(Stats.ITEM_USED.get(this));

        ItemStack emptyBottle = ItemUtils.createFilledResult(heldStack, player, new ItemStack(ModItems.GAS_BOTTLE), false);
        return InteractionResultHolder.sidedSuccess(emptyBottle, level.isClientSide());
    }
}
