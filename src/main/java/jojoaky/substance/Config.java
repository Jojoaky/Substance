package jojoaky.substance;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import jojoaky.substance.config.GameplayConfig;
import jojoaky.substance.config.GameplayOption;
import jojoaky.substance.config.GameplayOptions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

public class Config implements GameplayOptions {
    private static volatile GameplayConfig synchronizedGameplay;
    public static final boolean DEFAULT_ENABLE_SHADER_EFFECTS = true;
    public static final float DEFAULT_VISUAL_EFFECT_STRENGTH = 1.0f;
    public static final boolean DEFAULT_VISUAL_EFFECTS_IN_MENUS = true;
    public static final boolean DEFAULT_ENABLE_HALLUCINATION_VISUALS = true;
    public static final float DEFAULT_HALLUCINATION_VISUAL_STRENGTH = 1.0f;
    public static final float DEFAULT_HALLUCINATION_APPARITION_INTERVAL = 4.0f;
    public static final int DEFAULT_HALLUCINATION_MAX_APPARITIONS = 8;
    public static final float DEFAULT_HALLUCINATION_VILLAGER_CHANCE = 0.25f;
    public static final boolean DEFAULT_ENABLE_DREAD_VISUALS = true;
    public static final float DEFAULT_DREAD_VISUAL_STRENGTH = 1.0f;
    public static final float DEFAULT_DREAD_APPARITION_INTERVAL = 4.0f;
    public static final int DEFAULT_DREAD_MAX_APPARITIONS = 8;
    public static final float DEFAULT_DREAD_CREEPER_CHANCE = 0.2f;
    public static final float DEFAULT_DREAD_ANIMAL_DISTANCE = 24.0f;
    public static final float DEFAULT_DREAD_ANIMAL_FADE_DISTANCE = 4.0f;
    public static final boolean DEFAULT_ENABLE_AUDIO_EFFECTS = true;
    public static final float DEFAULT_AUDIO_EFFECT_STRENGTH = 1.0f;
    public static final boolean DEFAULT_ENABLE_AMBIENT_SOUNDS = true;
    public static final float DEFAULT_AMBIENT_SOUND_INTERVAL = 30.0f;
    public static final int DEFAULT_WOODEN_PIPE_DURABILITY = 2048;
    public static final int DEFAULT_BUBBLE_PIPE_DURABILITY = 512;
    public static final int DEFAULT_HERBAL_ROLL_DURABILITY = 460;
    public static final int DEFAULT_THICK_HERBAL_ROLL_DURABILITY = 570;
    public static final int DEFAULT_CIGARETTE_DURABILITY = 525;
    public static final float DEFAULT_MAX_SMOKE_DURATION = 6.0f;
    public static final float DEFAULT_SMOKE_COOLDOWN = 1.5f;
    public static final float DEFAULT_MAX_SNIFF_DURATION = 2.5f;
    public static final float DEFAULT_SNIFF_COOLDOWN = 2.0f;
    public static final float DEFAULT_PIPE_ITEM_CONSUME_PROBABILITY = 0.4f;
    public static final int DEFAULT_MOB_USE_ATTEMPT_INTERVAL = 140;
    public static final float DEFAULT_HORROR_TRIP_CHANCE = 0.1f;
    public static final float DEFAULT_SURGE_MOVEMENT_SPEED_BONUS = 0.2f;
    public static final float DEFAULT_SURGE_ELYTRA_BOOST = 0.025f;
    public static final float DEFAULT_SURGE_ELYTRA_MAX_SPEED = 0.25f;
    public static final float DEFAULT_SURGE_ELYTRA_MAX_SPEED_PER_LEVEL = 0.1f;
    public static final float DEFAULT_KEEN_MINING_SPEED_MULTIPLIER = 3.0f;
    public static final int DEFAULT_RELAXATION_DARKNESS_DURATION = 200;

    public static ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(new ResourceLocation(Substance.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("substance.json"))
                    .build())
            .build();

    public static Config get() {
        return HANDLER.instance();
    }

    public static GameplayOptions gameplay() {
        GameplayConfig synchronizedConfig = synchronizedGameplay;
        return synchronizedConfig != null ? synchronizedConfig : get();
    }

    public static void setSynchronizedGameplay(GameplayConfig gameplayConfig) {
        synchronizedGameplay = gameplayConfig;
    }

    public static void clearSynchronizedGameplay() {
        synchronizedGameplay = null;
    }

    // Client
    @SerialEntry
    public boolean enableShaderEffects = DEFAULT_ENABLE_SHADER_EFFECTS;

    @SerialEntry
    public float visualEffectStrength = DEFAULT_VISUAL_EFFECT_STRENGTH;

    @SerialEntry
    public boolean visualEffectsInMenus = DEFAULT_VISUAL_EFFECTS_IN_MENUS;

    @SerialEntry
    public boolean enableHallucinationVisuals = DEFAULT_ENABLE_HALLUCINATION_VISUALS;

    @SerialEntry
    public float hallucinationVisualStrength = DEFAULT_HALLUCINATION_VISUAL_STRENGTH;

    @SerialEntry
    public float hallucinationApparitionInterval = DEFAULT_HALLUCINATION_APPARITION_INTERVAL;

    @SerialEntry
    public int hallucinationMaxApparitions = DEFAULT_HALLUCINATION_MAX_APPARITIONS;

    @SerialEntry
    public float hallucinationVillagerChance = DEFAULT_HALLUCINATION_VILLAGER_CHANCE;

    @SerialEntry
    public boolean enableDreadVisuals = DEFAULT_ENABLE_DREAD_VISUALS;

    @SerialEntry
    public float dreadVisualStrength = DEFAULT_DREAD_VISUAL_STRENGTH;

    @SerialEntry
    public float dreadApparitionInterval = DEFAULT_DREAD_APPARITION_INTERVAL;

    @SerialEntry
    public int dreadMaxApparitions = DEFAULT_DREAD_MAX_APPARITIONS;

    @SerialEntry
    public float dreadCreeperChance = DEFAULT_DREAD_CREEPER_CHANCE;

    @SerialEntry
    public float dreadAnimalDistance = DEFAULT_DREAD_ANIMAL_DISTANCE;

    @SerialEntry
    public float dreadAnimalFadeDistance = DEFAULT_DREAD_ANIMAL_FADE_DISTANCE;

    @SerialEntry
    public boolean enableAudioEffects = DEFAULT_ENABLE_AUDIO_EFFECTS;

    @SerialEntry
    public float audioEffectStrength = DEFAULT_AUDIO_EFFECT_STRENGTH;

    @SerialEntry
    public boolean enableAmbientSounds = DEFAULT_ENABLE_AMBIENT_SOUNDS;

    @SerialEntry
    public float ambientSoundInterval = DEFAULT_AMBIENT_SOUND_INTERVAL;

    // Gameplay
    @SerialEntry
    @GameplayOption
    public int woodenPipeDurability = DEFAULT_WOODEN_PIPE_DURABILITY;

    @SerialEntry
    @GameplayOption
    public int bubblePipeDurability = DEFAULT_BUBBLE_PIPE_DURABILITY;

    @SerialEntry
    @GameplayOption
    public int herbalRollDurability = DEFAULT_HERBAL_ROLL_DURABILITY;

    @SerialEntry
    @GameplayOption
    public int thickHerbalRollDurability = DEFAULT_THICK_HERBAL_ROLL_DURABILITY;

    @SerialEntry
    @GameplayOption
    public int cigaretteDurability = DEFAULT_CIGARETTE_DURABILITY;

    @SerialEntry
    @GameplayOption
    public float maxSmokeDuration = DEFAULT_MAX_SMOKE_DURATION;
    @SerialEntry
    @GameplayOption
    public float smokeCooldown = DEFAULT_SMOKE_COOLDOWN;

    @SerialEntry
    @GameplayOption
    public float maxSniffDuration = DEFAULT_MAX_SNIFF_DURATION;
    @SerialEntry
    @GameplayOption
    public float sniffCooldown = DEFAULT_SNIFF_COOLDOWN;

    @SerialEntry
    @GameplayOption
    public float pipeItemConsumeProbability = DEFAULT_PIPE_ITEM_CONSUME_PROBABILITY;

    @SerialEntry
    @GameplayOption
    public int mobUseAttemptInterval = DEFAULT_MOB_USE_ATTEMPT_INTERVAL;

    @SerialEntry
    @GameplayOption
    public float horrorTripChance = DEFAULT_HORROR_TRIP_CHANCE;

    @SerialEntry
    @GameplayOption
    public float surgeMovementSpeedBonus = DEFAULT_SURGE_MOVEMENT_SPEED_BONUS;

    @SerialEntry
    @GameplayOption
    public float surgeElytraBoost = DEFAULT_SURGE_ELYTRA_BOOST;

    @SerialEntry
    @GameplayOption
    public float surgeElytraMaxSpeed = DEFAULT_SURGE_ELYTRA_MAX_SPEED;

    @SerialEntry
    @GameplayOption
    public float surgeElytraMaxSpeedPerLevel = DEFAULT_SURGE_ELYTRA_MAX_SPEED_PER_LEVEL;

    @SerialEntry
    @GameplayOption
    public float keenMiningSpeedMultiplier = DEFAULT_KEEN_MINING_SPEED_MULTIPLIER;

    @SerialEntry
    @GameplayOption
    public int relaxationDarknessDuration = DEFAULT_RELAXATION_DARKNESS_DURATION;

    @Override
    public int woodenPipeDurability() {
        return woodenPipeDurability;
    }

    @Override
    public int bubblePipeDurability() {
        return bubblePipeDurability;
    }

    @Override
    public int herbalRollDurability() {
        return herbalRollDurability;
    }

    @Override
    public int thickHerbalRollDurability() {
        return thickHerbalRollDurability;
    }

    @Override
    public int cigaretteDurability() {
        return cigaretteDurability;
    }

    @Override
    public float maxSmokeDuration() {
        return maxSmokeDuration;
    }

    @Override
    public float smokeCooldown() {
        return smokeCooldown;
    }

    @Override
    public float maxSniffDuration() {
        return maxSniffDuration;
    }

    @Override
    public float sniffCooldown() {
        return sniffCooldown;
    }

    @Override
    public float pipeItemConsumeProbability() {
        return pipeItemConsumeProbability;
    }

    @Override
    public int mobUseAttemptInterval() {
        return mobUseAttemptInterval;
    }

    @Override
    public float horrorTripChance() {
        return horrorTripChance;
    }

    @Override
    public float surgeMovementSpeedBonus() {
        return surgeMovementSpeedBonus;
    }

    @Override
    public float surgeElytraBoost() {
        return surgeElytraBoost;
    }

    @Override
    public float surgeElytraMaxSpeed() {
        return surgeElytraMaxSpeed;
    }

    @Override
    public float surgeElytraMaxSpeedPerLevel() {
        return surgeElytraMaxSpeedPerLevel;
    }

    @Override
    public float keenMiningSpeedMultiplier() {
        return keenMiningSpeedMultiplier;
    }

    @Override
    public int relaxationDarknessDuration() {
        return relaxationDarknessDuration;
    }
}
