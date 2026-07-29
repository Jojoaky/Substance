package jojoaky.substance;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;

public class Config {
    public static ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(Substance.resource("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("substance.json"))
                    .build())
            .build();

    public static Config get() {
        return HANDLER.instance();
    }

    // Client
    @SerialEntry
    public boolean enableShaderEffects = true;

    @SerialEntry
    public float visualEffectStrength = 1.0f;

    @SerialEntry
    public boolean visualEffectsInMenus = true;

    @SerialEntry
    public boolean enableAudioEffects = true;

    @SerialEntry
    public float audioEffectStrength  = 1.0f;

    // Gameplay
    @SerialEntry
    public int woodenPipeDurability = 2048;

    @SerialEntry
    public int bubblePipeDurability = 512;

    @SerialEntry
    public int herbalRollDurability = 460;

    @SerialEntry
    public int thickHerbalRollDurability = 570;

    @SerialEntry
    public int cigaretteDurability = 525;

    @SerialEntry
    public float maxSmokeDuration = 6.0f;
    @SerialEntry
    public float smokeCooldown = 1.5f;

    @SerialEntry
    public float maxSniffDuration = 3.0f;
    @SerialEntry
    public float sniffCooldown = 2.0f;

    @SerialEntry
    public float pipeItemConsumeProbability = 0.4f;

    @SerialEntry
    public float mobCigaretteSpawnChance = 0.03f;

    @SerialEntry
    public float mobHerbalRollSpawnChance = 0.03f;

    @SerialEntry
    public float mobThickHerbalRollSpawnChance = 0.0025f;

    @SerialEntry
    public int mobUseAttemptInterval = 140;

    @SerialEntry
    public float horrorTripChance = 0.05f;
}


/*
 * Configs to implement:
 *  General: Durabilities
 */