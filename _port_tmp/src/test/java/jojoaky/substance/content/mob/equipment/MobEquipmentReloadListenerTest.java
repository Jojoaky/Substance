package jojoaky.substance.content.mob.equipment;

import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MobEquipmentReloadListenerTest {
    @Test
    void ordersByPriorityThenSpecificityThenIdentifier() {
        MobEquipment tag = definition("z_tag", 0, MobEquipment.TAG_SPECIFICITY);
        MobEquipment entityB = definition("b_entity", 0, MobEquipment.ENTITY_SPECIFICITY);
        MobEquipment highPriorityTag = definition("high_tag", 10, MobEquipment.TAG_SPECIFICITY);
        MobEquipment entityA = definition("a_entity", 0, MobEquipment.ENTITY_SPECIFICITY);
        List<MobEquipment> definitions = new ArrayList<>(List.of(tag, entityB, highPriorityTag, entityA));

        definitions.sort(MobEquipmentReloadListener.DEFINITION_ORDER);

        assertEquals(List.of(highPriorityTag, entityA, entityB, tag), definitions);
    }

    private static MobEquipment definition(String path, int priority, int specificity) {
        return new MobEquipment(
                new ResourceLocation("test", path),
                priority,
                specificity,
                false,
                entityType -> true,
                Map.of()
        );
    }
}
