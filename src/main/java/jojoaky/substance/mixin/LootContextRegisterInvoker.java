package jojoaky.substance.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.function.Consumer;

@Mixin(LootContextParamSets.class)
public interface LootContextRegisterInvoker {

    @Invoker("register")
    static LootContextParamSet invokeRegister(
            String registryName,
            Consumer<LootContextParamSet.Builder> builderConsumer
    ) {
        throw new AssertionError();
    }
}