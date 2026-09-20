package jojoaky.substance.content.mob;

import jojoaky.substance.Config;
import jojoaky.substance.content.consumable.framework.ConsumableItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MobUseConsumableGoal extends Goal {
    private final Mob mob;
    private InteractionHand activeHand;
    private int checkCooldown = 0;

    public MobUseConsumableGoal(Mob mob) {
        this.mob = mob;
        // Allows the mob to keep walking/looking around while using the item
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));    }

    @Override
    public boolean canUse() {
        if (mob.isUsingItem()) {
            return false;
        }

        // Space out ticks to avoid checking every single tick
        if (--this.checkCooldown > 0) {
            return false;
        }

        int interval = Math.max(1, Config.gameplay().mobUseAttemptInterval());
        this.checkCooldown = mob.getRandom().nextInt(interval);

        if (mob.getMainHandItem().getItem() instanceof ConsumableItem) {
            this.activeHand = InteractionHand.MAIN_HAND;
            return true;
        } else if (mob.getOffhandItem().getItem() instanceof ConsumableItem) {
            this.activeHand = InteractionHand.OFF_HAND;
            return true;
        }

        return false;
    }

    @Override
    public void start() {
        if (activeHand != null) {
            mob.startUsingItem(activeHand);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return mob.isUsingItem();
    }
}
