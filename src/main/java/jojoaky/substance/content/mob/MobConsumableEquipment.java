package jojoaky.substance.content.mob;

import jojoaky.substance.Config;
import jojoaky.substance.register.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class MobConsumableEquipment {
    private MobConsumableEquipment() {}

    public enum Type {
        CIGARETTE(ModItems.CIGARETTE),
        HERBAL_ROLL(ModItems.HERBAL_ROLL),
        THICK_HERBAL_ROLL(ModItems.THICK_HERBAL_ROLL);

        private final Item item;

        Type(Item item) {
            this.item = item;
        }

        public Item getItem() {
            return item;
        }
    }

    public static void tryEquip(Mob mob) {
        select(
                mob.getRandom().nextFloat(),
                Config.get().mobCigaretteSpawnChance,
                Config.get().mobHerbalRollSpawnChance,
                Config.get().mobThickHerbalRollSpawnChance
        ).ifPresent(type -> {
            EquipmentSlot targetSlot = determineSlot(mob);
            if (targetSlot != null) {
                mob.setItemSlot(targetSlot, new ItemStack(type.getItem()));
            }
        });
    }

    private static EquipmentSlot determineSlot(Mob mob) {
        if (mob.getMainHandItem().isEmpty()) {
            return EquipmentSlot.MAINHAND;
        } else if (mob.getOffhandItem().isEmpty()) {
            return EquipmentSlot.OFFHAND;
        }
        return null;
    }

    public static Optional<Type> select(
            float roll,
            float cigaretteChance,
            float herbalRollChance,
            float thickHerbalRollChance
    ) {
        if (!Float.isFinite(roll) || roll < 0.0f || roll >= 1.0f) {
            return Optional.empty();
        }

        float cumulativeChance = chance(cigaretteChance);
        if (roll < cumulativeChance) return Optional.of(Type.CIGARETTE);

        cumulativeChance = Math.min(1.0f, cumulativeChance + chance(herbalRollChance));
        if (roll < cumulativeChance) return Optional.of(Type.HERBAL_ROLL);

        cumulativeChance = Math.min(1.0f, cumulativeChance + chance(thickHerbalRollChance));
        if (roll < cumulativeChance) return Optional.of(Type.THICK_HERBAL_ROLL);

        return Optional.empty();
    }

    private static float chance(float chance) {
        return Float.isFinite(chance) ? Math.min(1.0f, Math.max(0.0f, chance)) : 0.0f;
    }
}