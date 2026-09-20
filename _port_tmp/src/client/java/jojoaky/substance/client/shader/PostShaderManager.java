package jojoaky.substance.client.shader;

import jojoaky.substance.client.mixin.GameRendererAccessor;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class PostShaderManager {
    public static boolean lazyLoadShaders = false;

    private static final List<PostShader> shaders = new ArrayList<>();

    public static void init() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation("substance", "shaders");
            }

            @Override
            public void onResourceManagerReload(ResourceManager manager) {
                for (var shader : shaders) {
                    shader.init();
                }
            }
        });
    }

    public static void add(PostShader shader) {
        shaders.add(shader);
    }

    public static void renderShaders(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {
        for (var shader : shaders) {
            shader.render(accessor, partialTicks, time, tick);
        }
    }

    public static void resizeShaders(GameRendererAccessor accessor, int width, int height) {
        for (var shader : shaders) {
            shader.resize(accessor, width, height);
        }
    }
}
