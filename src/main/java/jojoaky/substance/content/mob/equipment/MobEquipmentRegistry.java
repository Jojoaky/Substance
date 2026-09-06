package jojoaky.substance.content.mob.equipment;

import jojoaky.substance.Substance;
import jojoaky.substance.mixin.LootContextRegisterInvoker;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MobEquipmentRegistry {
    public static final String DIRECTORY = "mob_equipment";

    public static final LootContextParamSet MOB_EQUIPMENT_LOOT_PARAMS = LootContextRegisterInvoker.invokeRegister(
            Substance.resource("equipment").toString(),
            builder -> builder
                    .required(LootContextParams.THIS_ENTITY)
                    .required(LootContextParams.ORIGIN)
    );

    private static volatile List<MobEquipment> EQUIPMENT_DEFINITIONS = List.of();

    private MobEquipmentRegistry() {
    }

    public static void initialize() {
        ResourceManagerHelper.get(PackType.SERVER_DATA)
                .registerReloadListener(new MobEquipmentReloadListener(MobEquipmentRegistry::replaceDefinitions));
    }

    public static void applyRandomEquipment(Mob mob) {
        if (!(mob.level() instanceof ServerLevel serverLevel)) return;
        Set<EquipmentSlot> assigned = EnumSet.noneOf(EquipmentSlot.class);

        for (MobEquipment definition : EQUIPMENT_DEFINITIONS) {
            if (!definition.matches(mob.getType())) {
                continue;
            }

            for (Map.Entry<EquipmentSlot, ResourceLocation> entry : definition.equipment().entrySet()) {
                EquipmentSlot slot = entry.getKey();
                if (assigned.contains(slot) || !mob.getItemBySlot(slot).isEmpty()) {
                    continue;
                }

                List<ItemStack> generatedItems = generateItems(serverLevel, mob, entry.getValue());

                if (generatedItems.size() == 1 && !generatedItems.get(0).isEmpty()) {
                    mob.setItemSlot(slot, generatedItems.get(0));
                    assigned.add(slot);
                } else if (generatedItems.size() > 1) {
                    Substance.LOGGER.error(
                            "Mob equipment loot table {} for definition {} generated {} items; expected at most one.",
                            entry.getValue(),
                            definition.id(),
                            generatedItems.size()
                    );
                }
            }
        }
    }

    private static List<ItemStack> generateItems(ServerLevel level, Mob mob, ResourceLocation lootTableId) {
        LootTable lootTable = level.getServer().getLootData().getLootTable(lootTableId);
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, mob)
                .withParameter(LootContextParams.ORIGIN, mob.position())
                .create(MOB_EQUIPMENT_LOOT_PARAMS);
        return lootTable.getRandomItems(params);
    }

    private static void replaceDefinitions(List<MobEquipment> definitions) {
        EQUIPMENT_DEFINITIONS = List.copyOf(definitions);
    }
}
