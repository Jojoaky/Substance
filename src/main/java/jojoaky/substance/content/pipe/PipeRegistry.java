package jojoaky.substance.content.pipe;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public class PipeRegistry {

    private static final Map<Item, PipeSmokableItem> ITEMS = new IdentityHashMap<>();

    public static PipeSmokableItem register(PipeSmokableItem entry) {
        ITEMS.put(entry.item(), entry);
        return entry;
    }

    public static boolean isPipeSmokableItem(Item item) {
        return getItem(item) != null;
    }

    public static @Nullable PipeSmokableItem getItem(Item item) {
        return ITEMS.get(item);
    }

    public static void initialize() {
    }
}
