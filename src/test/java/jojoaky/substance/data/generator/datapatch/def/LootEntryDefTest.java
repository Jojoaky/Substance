package jojoaky.substance.data.generator.datapatch.def;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LootEntryDefTest {
    private static LootEntryDef definition() {
        return LootEntryDef.named("test", null);
    }

    @Test
    void independentPoolUsesExplicitChance() {
        LootEntryDef definition = definition().chance(0.25f);

        assertEquals(LootEntryDef.Placement.INDEPENDENT_POOL, definition.getPlacement());
        assertEquals(0.25f, definition.getChance());
    }

    @Test
    void weightedEntryTargetsOnePool() {
        LootEntryDef definition = definition().weightedInPool(2, 7);

        assertEquals(LootEntryDef.Placement.EXISTING_POOL, definition.getPlacement());
        assertEquals(2, definition.getPoolIndex());
        assertEquals(7, definition.getWeight());
    }

    @Test
    void rejectsInvalidChanceWeightAndCountRange() {
        assertThrows(IllegalArgumentException.class, () -> definition().chance(1.01f));
        assertThrows(IllegalArgumentException.class, () -> definition().weightedInPool(0, 0));
        assertThrows(IllegalArgumentException.class, () -> definition().count(2.0f, 1.0f));
    }

    @Test
    void rejectsMixedPlacementStrategies() {
        LootEntryDef definition = definition().chance(0.25f);

        assertThrows(IllegalStateException.class, () -> definition.weightedInPool(0, 1));
    }
}
