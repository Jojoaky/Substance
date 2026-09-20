package jojoaky.substance.content.mob.equipment;

import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MobEquipmentRegistryTest {
    @Test
    void overrideControlsOnlyEquipmentThatPredatesDefinitions() {
        MobEquipment skipOccupied = definition(false);
        MobEquipment overrideOccupied = definition(true);

        assertTrue(skipOccupied.shouldSkipSlot(false, true));
        assertFalse(overrideOccupied.shouldSkipSlot(false, true));
        assertTrue(overrideOccupied.shouldSkipSlot(true, true));
        assertTrue(overrideOccupied.shouldSkipSlot(true, false));
    }

    private static MobEquipment definition(boolean override) {
        return new MobEquipment(
                new ResourceLocation("test", "definition"),
                0,
                MobEquipment.ENTITY_SPECIFICITY,
                override,
                entityType -> true,
                Map.of()
        );
    }
}
