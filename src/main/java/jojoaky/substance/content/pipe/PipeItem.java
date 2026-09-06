package jojoaky.substance.content.pipe;

import jojoaky.substance.Config;
import jojoaky.substance.Substance;
import  jojoaky.substance.content.ItemContainer;
import jojoaky.substance.content.consumable.framework.*;
import jojoaky.substance.register.ModEffects;
import jojoaky.substance.util.SubstanceEffectHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.ToIntFunction;

public class PipeItem extends ConsumableItem {
    public PipeItem(Properties properties, ToIntFunction<ItemStack> durabilityProvider) {
        super(
                properties,
                new VanillaSuppliedDurabilityStrategy(durabilityProvider),
                () -> Math.round(Config.get().maxSmokeDuration * 20.0f),
                () -> Math.round(Config.get().smokeCooldown * 20.0f),
                UseAnim.SPYGLASS,
                new SmokeComponent()
        );
    }

    public static final int PIPE_INVENTORY_SIZE = 5;

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        var container = new ItemContainer(stack, PIPE_INVENTORY_SIZE);
        container.readFromNbt();

        if (!container.isEmpty() && !player.isCrouching()) super.use(level, player, hand);
        else {
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new ExtendedScreenHandlerFactory() {
                    @Override
                    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                        // Send hand to the client
                        buf.writeEnum(hand);
                    }

                    @Override
                    public @NotNull Component getDisplayName() {
                        return stack.getHoverName();
                    }

                    @Override
                    public @NotNull AbstractContainerMenu createMenu(int syncId, @NotNull Inventory playerInventory, @NotNull Player player) {
                        return new PipeMenu(syncId, playerInventory, new ItemContainer(stack, PIPE_INVENTORY_SIZE), hand);
                    }
                });
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    protected void onConsumeTick(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.onConsumeTick(stack, level, entity, useDuration);

        SubstanceEffectHelper.applyEffectBase(entity, ModEffects.WARP, 160, 0);
    }

    private void reduceStackAfterConsume(RandomSource random, ItemStack stack, int useDuration) {
        float probability = Config.get().pipeItemConsumeProbability;

        if (probability <= 0) return;

        if (probability >= 1) {
            stack.shrink(1);
            return;
        }

        int maxRoll = (int) (getUseDuration(stack) * (1 / probability));
        if (random.nextInt(maxRoll) < useDuration) {
            stack.shrink(1);
        }
    }

    private void handleConsumeIngredient(ItemStack ingredientStack, Set<Item> processedItems, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        if (ingredientStack == null || ingredientStack.isEmpty()) return;

        if (level.isClientSide) return;

        Item item = ingredientStack.getItem();

        if (!processedItems.add(item)) return;

        PipeSmokableItem smokable = PipeRegistry.getItem(item);

        if (smokable == null) {
            Substance.LOGGER.error("Item {} in pipe is not registered as pipe smokable item!", item);
            return;
        }

        var context = new PipeSmokableConsumeContext(stack, ingredientStack, level, entity, useDuration);
        smokable.onConsume().accept(context);

        reduceStackAfterConsume(level.random, ingredientStack, useDuration);
    }

    @Override
    protected void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.onStopConsuming(stack, level, entity, useDuration);

        var container = new ItemContainer(stack, PIPE_INVENTORY_SIZE);
        container.readFromNbt();

        if (container.isEmpty()) return;

        if (useDuration > 100) entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 4 * 20));
        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.HAZE, useDuration * 5, 600, 1);

        var processedItems = new HashSet<Item>();

        container.items.forEach((item) -> handleConsumeIngredient(item, processedItems, stack, level, entity, useDuration));

        container.writeToNbt();
    }

    public static void dropContents(ItemStack pipeStack, Level level, Vec3 pos) {
        if (level.isClientSide) return;

        ItemContainer container = new ItemContainer(pipeStack, PIPE_INVENTORY_SIZE);
        container.readFromNbt();

        for (ItemStack content : container.items) {
            if (!content.isEmpty()) {
                Containers.dropItemStack(level, pos.x(), pos.y(), pos.z(), content.copy());
            }
        }

        container.items.clear();
        container.writeToNbt();
    }

    @Override
    public void onDestroyed(ItemEntity entity) {
        if (!entity.level().isClientSide) {
            dropContents(entity.getItem(), entity.level(), entity.position());
        }
        super.onDestroyed(entity);
    }
}
