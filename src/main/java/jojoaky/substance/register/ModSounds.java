package jojoaky.substance.register;

import jojoaky.substance.Substance;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class ModSounds {
    public static final SoundEvent HALLUCINATION_AMBIENT = register("effect.hallucination.ambient");
    public static final SoundEvent DREAD_AMBIENT = register("effect.dread.ambient");

    private ModSounds() {
    }

    private static SoundEvent register(String name) {
        ResourceLocation id = Substance.resource(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void initialize() {
        // Loads the class so the static sound event registrations run.
    }
}
