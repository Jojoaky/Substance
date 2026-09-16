package jojoaky.substance.content.mob.equipment;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RawMobEquipmentTest {
    private static RawMobEquipment parse(String json) {
        return RawMobEquipment.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(json)).getOrThrow(false, message -> {
            throw new AssertionError(message);
        });
    }

    @Test
    void optionalFieldsUseBackwardsCompatibleDefaults() {
        RawMobEquipment definition = parse("""
                {
                  "entities": [],
                  "equipment": {}
                }
                """);

        assertEquals(0, definition.priority());
        assertFalse(definition.override());
    }

    @Test
    void parsesPriorityAndOverride() {
        RawMobEquipment definition = parse("""
                {
                  "entities": [],
                  "equipment": {},
                  "priority": 25,
                  "override": true
                }
                """);

        assertEquals(25, definition.priority());
        assertTrue(definition.override());
    }

    @Test
    void emptyDefinitionDisablesAReplacedResource() {
        RawMobEquipment definition = parse("""
                {
                  "entities": [],
                  "equipment": {}
                }
                """);

        assertTrue(definition.toMobEquipment(new ResourceLocation("substance", "illagers")).isEmpty());
    }
}
