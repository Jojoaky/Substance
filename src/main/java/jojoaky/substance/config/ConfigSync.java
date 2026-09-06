package jojoaky.substance.config;

import jojoaky.substance.Config;
import jojoaky.substance.Substance;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class ConfigSync {
    public static final ResourceLocation CHANNEL = Substance.resource("gameplay_config");
    public static final int PROTOCOL_VERSION = 1;

    private ConfigSync() {
    }

    public static void initializeServer() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                send(handler.getPlayer()));
    }

    public static void broadcast(MinecraftServer server) {
        server.getPlayerList().getPlayers().forEach(player -> {
            refreshActiveConfigDependentEffects(player);
            send(player);
        });
    }

    private static void send(ServerPlayer player) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeVarInt(PROTOCOL_VERSION);
        GameplayConfig.from(Config.get()).write(buffer);
        ServerPlayNetworking.send(player, CHANNEL, buffer);
    }

    private static void refreshActiveConfigDependentEffects(ServerPlayer player) {
        player.getActiveEffects().stream()
                .filter(effect -> effect.getEffect() instanceof ConfigDependentEffect)
                .forEach(effect -> {
                    ConfigDependentEffect configDependentEffect =
                            (ConfigDependentEffect) effect.getEffect();
                    configDependentEffect.refreshAfterConfigChange(player, effect.getAmplifier());
                });
    }
}
