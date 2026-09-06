package jojoaky.substance.client.config;

import jojoaky.substance.Config;
import jojoaky.substance.Substance;
import jojoaky.substance.config.ConfigSync;
import jojoaky.substance.config.GameplayConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class ClientConfigSync {
    private ClientConfigSync() {
    }

    public static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigSync.CHANNEL, (client, handler, buffer, responseSender) -> {
            int protocolVersion = buffer.readVarInt();
            if (protocolVersion != ConfigSync.PROTOCOL_VERSION) {
                Substance.LOGGER.warn(
                        "Ignoring gameplay config using protocol version {} (expected {})",
                        protocolVersion,
                        ConfigSync.PROTOCOL_VERSION
                );
                return;
            }

            GameplayConfig gameplayConfig = GameplayConfig.read(buffer);
            client.execute(() -> {
                // The integrated server shares this Config class with its client and is
                // already authoritative; retaining a snapshot would make in-world edits stale.
                if (client.hasSingleplayerServer()) {
                    Config.clearSynchronizedGameplay();
                } else {
                    Config.setSynchronizedGameplay(gameplayConfig);
                }
            });
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                Config.clearSynchronizedGameplay());
    }
}
