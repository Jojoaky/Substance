package jojoaky.substance.content.consumable.framework;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntSupplier;

public class ConsumableItem extends Item {

    private final DurabilityStrategy durabilityStrategy;
    private final IntSupplier useDurationProvider;
    private final IntSupplier cooldownProvider;
    private final UseAnim useAnimation;
    private final List<ConsumableComponent> components;

    public ConsumableItem(
            Properties properties,
            DurabilityStrategy durabilityStrategy,
            int useDuration,
            int cooldown,
            UseAnim useAnimation,
            ConsumableComponent... components
    ) {
        this(
                properties,
                durabilityStrategy,
                () -> useDuration,
                () -> cooldown,
                useAnimation,
                components
        );
    }

    public ConsumableItem(
            Properties properties,
            DurabilityStrategy durabilityStrategy,
            IntSupplier useDurationProvider,
            IntSupplier cooldownProvider,
            UseAnim useAnimation,
            ConsumableComponent... components
    ) {
        super(durabilityStrategy.configureProperties(properties));
        this.durabilityStrategy = durabilityStrategy;
        this.useDurationProvider = useDurationProvider;
        this.cooldownProvider = cooldownProvider;
        this.useAnimation = useAnimation;
        this.components = new ArrayList<>(Arrays.asList(components));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int i) {
        int useDuration = getUseDuration(stack) - i;

        if (useDuration == 0) {
            for (ConsumableComponent component : components) {
                component.onStartConsuming(stack, level, entity);
            }
            onStartConsuming(stack, level, entity);
        }

        consumeTick(stack, level, entity, useDuration);
        durabilityStrategy.onConsumeTick(this, stack, level, entity, useDuration);

        for (ConsumableComponent component : components) {
            component.onConsumeTick(stack, level, entity, useDuration);
        }

        onConsumeTick(stack, level, entity, useDuration);
    }

    @Override
    @NotNull
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        int useDuration = getUseDuration(stack);
        finishConsuming(stack, level, entity, useDuration);
        stopConsuming(stack, level, entity, useDuration);
        return stack;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int i) {
        int useDuration = getUseDuration(stack) - i;
        stopConsuming(stack, level, entity, useDuration);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return useDurationProvider.getAsInt();
    }

    public int getCooldown(ItemStack stack) {
        return cooldownProvider.getAsInt();
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return useAnimation;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        Boolean strategyValue = durabilityStrategy.isBarVisible(stack);
        return strategyValue != null ? strategyValue : super.isBarVisible(stack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        Integer strategyValue = durabilityStrategy.getBarWidth(stack);
        return strategyValue != null ? strategyValue : super.getBarWidth(stack);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        Integer strategyValue = durabilityStrategy.getBarColor(stack);
        return strategyValue != null ? strategyValue : super.getBarColor(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        durabilityStrategy.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public boolean allowNbtUpdateAnimation(
            Player player,
            InteractionHand hand,
            ItemStack oldStack,
            ItemStack newStack
    ) {
        return durabilityStrategy.allowNbtUpdateAnimation(player, hand, oldStack, newStack);
    }

    public boolean hasCustomRenderModel() {
        for (ConsumableComponent component : components) {
            if (component.hasCustomRenderModel()) {
                return true;
            }
        }
        return false;
    }

    protected void stopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        durabilityStrategy.onStopConsuming(this, stack, level, entity, useDuration);

        for (ConsumableComponent component : components) {
            component.onStopConsuming(stack, level, entity, useDuration);
        }

        onStopConsuming(stack, level, entity, useDuration);
    }

    protected void finishConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        if (!level.isClientSide) {
            int cooldown = getCooldown(stack);
            if (cooldown > 0 && entity instanceof Player player) {
                player.getCooldowns().addCooldown(this, cooldown);
            }
        }

        durabilityStrategy.onFinishConsuming(this, stack, level, entity, useDuration);

        for (ConsumableComponent component : components) {
            component.onFinishConsuming(stack, level, entity, useDuration);
        }

        onFinishConsuming(stack, level, entity, useDuration);
    }

    protected void onStartConsuming(ItemStack stack, Level level, LivingEntity entity) {
    }

    protected void consumeTick(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
    }

    protected void onConsumeTick(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
    }

    protected void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
    }

    protected void onFinishConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
    }
}
