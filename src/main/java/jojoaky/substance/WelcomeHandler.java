package jojoaky.substance;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnstableApiUsage")
public final class WelcomeHandler {

    public static final AttachmentType<Boolean> SEEN_WELCOME = AttachmentRegistry.<Boolean>builder()
            .initializer(() -> false)
            .persistent(Codec.BOOL)
            .copyOnDeath()
            .buildAndRegister(new ResourceLocation("substance", "seen_welcome"));

    public static void onPlayerJoin(ServerPlayer player, MinecraftServer server) {
        if (!player.getAttachedOrCreate(SEEN_WELCOME)) {
            player.setAttached(SEEN_WELCOME, true);

            CompletableFuture.runAsync(() -> server.execute(() -> {
                if (server.getPlayerList().getPlayer(player.getUUID()) != null) {
                    player.sendSystemMessage(
                            Component
                            .literal("[SUBSTANCE] ")
                            .append(Component.literal("Welcome to the Substance Mod! This mod is purely fictional and for entertainment purposes only. It is not meant to encourage the use of illicit substances in any way."))
                    );
                }
            }), CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS));
        }
    }
}