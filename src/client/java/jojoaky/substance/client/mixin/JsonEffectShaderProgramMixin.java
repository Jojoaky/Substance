package jojoaky.substance.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import jojoaky.substance.Config;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

// Inspired by: https://github.com/Ladysnake/Satin/blob/1.20/src/main/java/ladysnake/satin/mixin/client/gl/JsonEffectGlShaderMixin.java

@Mixin(EffectInstance.class)
public class JsonEffectShaderProgramMixin {
    @Unique
    private static final String SHADER_DIR = "shaders/program/";

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/resources/ResourceLocation",
                    ordinal = 0
            )
    )
    private ResourceLocation fixNamespacedProgramId(
            String id,
            Operation<ResourceLocation> original
    ) {
        if (!Config.get().enableShaderEffects) {
            return original.call(id);
        }

        int colon = id.indexOf(':');

        if (colon < 0) return original.call(id);

        String namespace = id.substring(0, colon);
        String path = id.substring(colon + 1);

        namespace = namespace.substring(namespace.lastIndexOf('/') + 1);
        if (!path.endsWith(".json")) path += ".json";

        return new ResourceLocation(namespace, SHADER_DIR + path);
    }

    @WrapOperation(
            method = "getOrCreate",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/resources/ResourceLocation",
                    ordinal = 0
            )
    )
    private static ResourceLocation fixNamespacedStageId(
            String id,
            Operation<ResourceLocation> original
    ) {
        if (!Config.get().enableShaderEffects) {
            return original.call(id);
        }

        int colon = id.indexOf(':');

        if (colon < 0) return original.call(id);

        String namespace = id.substring(0, colon);
        String path = id.substring(colon + 1);

        namespace = namespace.substring(namespace.lastIndexOf('/') + 1);

        return new ResourceLocation(namespace, SHADER_DIR + path);
    }
}