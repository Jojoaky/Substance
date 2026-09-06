package jojoaky.substance;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public final class WelcomeHandler {
    private static final int WELCOME_DELAY_TICKS = 60;
    private static final Map<UUID, Integer> PENDING_MESSAGES = new HashMap<>();

    public static final AttachmentType<Boolean> SEEN_WELCOME = AttachmentRegistry.<Boolean>builder()
            .initializer(() -> false)
            .persistent(Codec.BOOL)
            .copyOnDeath()
            .buildAndRegister(new ResourceLocation("substance", "seen_welcome"));

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(WelcomeHandler::onEndServerTick);
    }

    public static void onPlayerJoin(ServerPlayer player, MinecraftServer server) {
        if (!player.getAttachedOrCreate(SEEN_WELCOME)) {
            player.setAttached(SEEN_WELCOME, true);

            PENDING_MESSAGES.put(player.getUUID(), WELCOME_DELAY_TICKS);
        }
    }

    private static void onEndServerTick(MinecraftServer server) {
        Iterator<Map.Entry<UUID, Integer>> iterator = PENDING_MESSAGES.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            int ticksRemaining = entry.getValue() - 1;
            if (ticksRemaining > 0) {
                entry.setValue(ticksRemaining);
                continue;
            }

            ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
            if (player != null) {
                player.sendSystemMessage(
                        Component.literal("[SUBSTANCE] ")
                                .append(Component.literal("Welcome to the Substance Mod! This mod is purely fictional and for entertainment purposes only. It is not meant to encourage the use of illicit substances in any way."))
                );
            }
            iterator.remove();
        }
    }
}
