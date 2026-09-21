package jojoaky.substance;

import jojoaky.substance.config.GameplayOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Objects;

/**
 * Substance's native NeoForge configuration.
 *
 * <p>Gameplay values use a server config so NeoForge synchronizes them to
 * connected clients. Visual and audio values remain local to each client.</p>
 */
public final class Config implements GameplayOptions {
    public static final boolean DEFAULT_ENABLE_SHADER_EFFECTS = true;
    public static final double DEFAULT_VISUAL_EFFECT_STRENGTH = 1.0;
    public static final boolean DEFAULT_VISUAL_EFFECTS_IN_MENUS = true;
    public static final boolean DEFAULT_ENABLE_HALLUCINATION_VISUALS = true;
    public static final double DEFAULT_HALLUCINATION_VISUAL_STRENGTH = 1.0;
    public static final double DEFAULT_HALLUCINATION_APPARITION_INTERVAL = 4.0;
    public static final int DEFAULT_HALLUCINATION_MAX_APPARITIONS = 8;
    public static final double DEFAULT_HALLUCINATION_VILLAGER_CHANCE = 0.25;
    public static final boolean DEFAULT_ENABLE_DREAD_VISUALS = true;
    public static final double DEFAULT_DREAD_VISUAL_STRENGTH = 1.0;
    public static final double DEFAULT_DREAD_APPARITION_INTERVAL = 4.0;
    public static final int DEFAULT_DREAD_MAX_APPARITIONS = 8;
    public static final double DEFAULT_DREAD_CREEPER_CHANCE = 0.2;
    public static final List<String> DEFAULT_DREAD_DISTANT_ENTITY_TYPES = List.of(
            "minecraft:cow", "minecraft:pig", "minecraft:chicken", "minecraft:horse",
            "minecraft:zombie", "minecraft:wandering_trader"
    );
    public static final double DEFAULT_DREAD_ANIMAL_DISTANCE = 24.0;
    public static final double DEFAULT_DREAD_ANIMAL_FADE_DISTANCE = 4.0;
    public static final boolean DEFAULT_ENABLE_AUDIO_EFFECTS = true;
    public static final double DEFAULT_AUDIO_EFFECT_STRENGTH = 1.0;
    public static final boolean DEFAULT_ENABLE_AMBIENT_SOUNDS = true;
    public static final double DEFAULT_AMBIENT_SOUND_INTERVAL = 30.0;

    public static final int DEFAULT_HERBAL_ROLL_DURABILITY = 460;
    public static final int DEFAULT_THICK_HERBAL_ROLL_DURABILITY = 570;
    public static final int DEFAULT_CIGARETTE_DURABILITY = 525;
    public static final double DEFAULT_MAX_SMOKE_DURATION = 6.0;
    public static final double DEFAULT_SMOKE_COOLDOWN = 1.5;
    public static final double DEFAULT_MAX_SNIFF_DURATION = 2.5;
    public static final double DEFAULT_SNIFF_COOLDOWN = 2.0;
    public static final double DEFAULT_PIPE_ITEM_CONSUME_PROBABILITY = 0.4;
    public static final int DEFAULT_MOB_USE_ATTEMPT_INTERVAL = 140;
    public static final double DEFAULT_HORROR_TRIP_CHANCE = 0.1;
    public static final double DEFAULT_SURGE_MOVEMENT_SPEED_BONUS = 0.2;
    public static final double DEFAULT_SURGE_ELYTRA_BOOST = 0.025;
    public static final double DEFAULT_SURGE_ELYTRA_MAX_SPEED = 0.25;
    public static final double DEFAULT_SURGE_ELYTRA_MAX_SPEED_PER_LEVEL = 0.1;
    public static final double DEFAULT_KEEN_MINING_SPEED_MULTIPLIER = 3.0;
    public static final int DEFAULT_RELAXATION_DARKNESS_DURATION = 120;

    public static final ClientValues CLIENT;
    public static final ServerValues SERVER;
    public static final ModConfigSpec CLIENT_SPEC;
    public static final ModConfigSpec SERVER_SPEC;

    private static final Config INSTANCE = new Config();

    // Kept source-compatible with the config API used throughout the existing mod.
    // NeoForge owns the actual values; these fields are refreshed after config events.
    public volatile boolean enableShaderEffects = DEFAULT_ENABLE_SHADER_EFFECTS;
    public volatile float visualEffectStrength = (float) DEFAULT_VISUAL_EFFECT_STRENGTH;
    public volatile boolean visualEffectsInMenus = DEFAULT_VISUAL_EFFECTS_IN_MENUS;
    public volatile boolean enableHallucinationVisuals = DEFAULT_ENABLE_HALLUCINATION_VISUALS;
    public volatile float hallucinationVisualStrength = (float) DEFAULT_HALLUCINATION_VISUAL_STRENGTH;
    public volatile float hallucinationApparitionInterval = (float) DEFAULT_HALLUCINATION_APPARITION_INTERVAL;
    public volatile int hallucinationMaxApparitions = DEFAULT_HALLUCINATION_MAX_APPARITIONS;
    public volatile float hallucinationVillagerChance = (float) DEFAULT_HALLUCINATION_VILLAGER_CHANCE;
    public volatile boolean enableDreadVisuals = DEFAULT_ENABLE_DREAD_VISUALS;
    public volatile float dreadVisualStrength = (float) DEFAULT_DREAD_VISUAL_STRENGTH;
    public volatile float dreadApparitionInterval = (float) DEFAULT_DREAD_APPARITION_INTERVAL;
    public volatile int dreadMaxApparitions = DEFAULT_DREAD_MAX_APPARITIONS;
    public volatile float dreadCreeperChance = (float) DEFAULT_DREAD_CREEPER_CHANCE;
    public volatile List<String> dreadDistantEntityTypes = DEFAULT_DREAD_DISTANT_ENTITY_TYPES;
    public volatile float dreadAnimalDistance = (float) DEFAULT_DREAD_ANIMAL_DISTANCE;
    public volatile float dreadAnimalFadeDistance = (float) DEFAULT_DREAD_ANIMAL_FADE_DISTANCE;
    public volatile boolean enableAudioEffects = DEFAULT_ENABLE_AUDIO_EFFECTS;
    public volatile float audioEffectStrength = (float) DEFAULT_AUDIO_EFFECT_STRENGTH;
    public volatile boolean enableAmbientSounds = DEFAULT_ENABLE_AMBIENT_SOUNDS;
    public volatile float ambientSoundInterval = (float) DEFAULT_AMBIENT_SOUND_INTERVAL;

    public volatile int herbalRollDurability = DEFAULT_HERBAL_ROLL_DURABILITY;
    public volatile int thickHerbalRollDurability = DEFAULT_THICK_HERBAL_ROLL_DURABILITY;
    public volatile int cigaretteDurability = DEFAULT_CIGARETTE_DURABILITY;
    public volatile float maxSmokeDuration = (float) DEFAULT_MAX_SMOKE_DURATION;
    public volatile float smokeCooldown = (float) DEFAULT_SMOKE_COOLDOWN;
    public volatile float maxSniffDuration = (float) DEFAULT_MAX_SNIFF_DURATION;
    public volatile float sniffCooldown = (float) DEFAULT_SNIFF_COOLDOWN;
    public volatile float pipeItemConsumeProbability = (float) DEFAULT_PIPE_ITEM_CONSUME_PROBABILITY;
    public volatile int mobUseAttemptInterval = DEFAULT_MOB_USE_ATTEMPT_INTERVAL;
    public volatile float horrorTripChance = (float) DEFAULT_HORROR_TRIP_CHANCE;
    public volatile float surgeMovementSpeedBonus = (float) DEFAULT_SURGE_MOVEMENT_SPEED_BONUS;
    public volatile float surgeElytraBoost = (float) DEFAULT_SURGE_ELYTRA_BOOST;
    public volatile float surgeElytraMaxSpeed = (float) DEFAULT_SURGE_ELYTRA_MAX_SPEED;
    public volatile float surgeElytraMaxSpeedPerLevel = (float) DEFAULT_SURGE_ELYTRA_MAX_SPEED_PER_LEVEL;
    public volatile float keenMiningSpeedMultiplier = (float) DEFAULT_KEEN_MINING_SPEED_MULTIPLIER;
    public volatile int relaxationDarknessDuration = DEFAULT_RELAXATION_DARKNESS_DURATION;

    private volatile EntityType<?>[] dreadDistantEntityTypeCache = new EntityType<?>[0];

    static {
        ModConfigSpec.Builder clientBuilder = new ModConfigSpec.Builder();
        CLIENT = new ClientValues(clientBuilder);
        CLIENT_SPEC = clientBuilder.build();

        ModConfigSpec.Builder serverBuilder = new ModConfigSpec.Builder();
        SERVER = new ServerValues(serverBuilder);
        SERVER_SPEC = serverBuilder.build();
    }

    private Config() {
    }

    public static void register(ModContainer container, IEventBus modEventBus) {
        container.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        container.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
        modEventBus.addListener(Config::onConfigEvent);
    }

    /** Returns the compatibility view used by the existing client code. */
    public static Config get() {
        return INSTANCE;
    }

    /** Returns gameplay values from the local server or the connected server's synchronized config. */
    public static GameplayOptions gameplay() {
        return INSTANCE;
    }

    public static void refreshDreadDistantEntityTypes() {
        CLIENT.refreshDreadDistantEntityTypes();
        INSTANCE.dreadDistantEntityTypeCache = CLIENT.dreadDistantEntityTypeCache();
    }

    private static void onConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == CLIENT_SPEC) {
            INSTANCE.refreshClientValues();
        } else if (event.getConfig().getSpec() == SERVER_SPEC) {
            INSTANCE.refreshServerValues();
        }
    }

    private void refreshClientValues() {
        enableShaderEffects = CLIENT.enableShaderEffects();
        visualEffectStrength = CLIENT.visualEffectStrength();
        visualEffectsInMenus = CLIENT.visualEffectsInMenus();
        enableHallucinationVisuals = CLIENT.enableHallucinationVisuals();
        hallucinationVisualStrength = CLIENT.hallucinationVisualStrength();
        hallucinationApparitionInterval = CLIENT.hallucinationApparitionInterval();
        hallucinationMaxApparitions = CLIENT.hallucinationMaxApparitions();
        hallucinationVillagerChance = CLIENT.hallucinationVillagerChance();
        enableDreadVisuals = CLIENT.enableDreadVisuals();
        dreadVisualStrength = CLIENT.dreadVisualStrength();
        dreadApparitionInterval = CLIENT.dreadApparitionInterval();
        dreadMaxApparitions = CLIENT.dreadMaxApparitions();
        dreadCreeperChance = CLIENT.dreadCreeperChance();
        dreadDistantEntityTypes = List.copyOf(CLIENT.dreadDistantEntityTypes());
        dreadAnimalDistance = CLIENT.dreadAnimalDistance();
        dreadAnimalFadeDistance = CLIENT.dreadAnimalFadeDistance();
        enableAudioEffects = CLIENT.enableAudioEffects();
        audioEffectStrength = CLIENT.audioEffectStrength();
        enableAmbientSounds = CLIENT.enableAmbientSounds();
        ambientSoundInterval = CLIENT.ambientSoundInterval();
        refreshDreadDistantEntityTypes();
    }

    private void refreshServerValues() {
        herbalRollDurability = SERVER.herbalRollDurability();
        thickHerbalRollDurability = SERVER.thickHerbalRollDurability();
        cigaretteDurability = SERVER.cigaretteDurability();
        maxSmokeDuration = SERVER.maxSmokeDuration();
        smokeCooldown = SERVER.smokeCooldown();
        maxSniffDuration = SERVER.maxSniffDuration();
        sniffCooldown = SERVER.sniffCooldown();
        pipeItemConsumeProbability = SERVER.pipeItemConsumeProbability();
        mobUseAttemptInterval = SERVER.mobUseAttemptInterval();
        horrorTripChance = SERVER.horrorTripChance();
        surgeMovementSpeedBonus = SERVER.surgeMovementSpeedBonus();
        surgeElytraBoost = SERVER.surgeElytraBoost();
        surgeElytraMaxSpeed = SERVER.surgeElytraMaxSpeed();
        surgeElytraMaxSpeedPerLevel = SERVER.surgeElytraMaxSpeedPerLevel();
        keenMiningSpeedMultiplier = SERVER.keenMiningSpeedMultiplier();
        relaxationDarknessDuration = SERVER.relaxationDarknessDuration();
    }

    public EntityType<?>[] dreadDistantEntityTypeCache() {
        return dreadDistantEntityTypeCache.clone();
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

    private static ModConfigSpec.ConfigValue<List<? extends String>> entityTypeList(
            ModConfigSpec.Builder builder,
            String key,
            List<String> defaults
    ) {
        return builder
                .comment("Entity IDs used for distant dread apparitions. Unknown IDs are ignored.")
                .translation("substance.configuration." + key)
                .defineListAllowEmpty(key, defaults, () -> "", Config::isResourceLocation);
    }

    private static boolean isResourceLocation(Object value) {
        return value instanceof String id && ResourceLocation.tryParse(id) != null;
    }

    public static final class ClientValues {
        private final ModConfigSpec.BooleanValue enableShaderEffects;
        private final ModConfigSpec.DoubleValue visualEffectStrength;
        private final ModConfigSpec.BooleanValue visualEffectsInMenus;
        private final ModConfigSpec.BooleanValue enableHallucinationVisuals;
        private final ModConfigSpec.DoubleValue hallucinationVisualStrength;
        private final ModConfigSpec.DoubleValue hallucinationApparitionInterval;
        private final ModConfigSpec.IntValue hallucinationMaxApparitions;
        private final ModConfigSpec.DoubleValue hallucinationVillagerChance;
        private final ModConfigSpec.BooleanValue enableDreadVisuals;
        private final ModConfigSpec.DoubleValue dreadVisualStrength;
        private final ModConfigSpec.DoubleValue dreadApparitionInterval;
        private final ModConfigSpec.IntValue dreadMaxApparitions;
        private final ModConfigSpec.DoubleValue dreadCreeperChance;
        private final ModConfigSpec.ConfigValue<List<? extends String>> dreadDistantEntityTypes;
        private final ModConfigSpec.DoubleValue dreadAnimalDistance;
        private final ModConfigSpec.DoubleValue dreadAnimalFadeDistance;
        private final ModConfigSpec.BooleanValue enableAudioEffects;
        private final ModConfigSpec.DoubleValue audioEffectStrength;
        private final ModConfigSpec.BooleanValue enableAmbientSounds;
        private final ModConfigSpec.DoubleValue ambientSoundInterval;

        private volatile EntityType<?>[] dreadDistantEntityTypeCache = new EntityType<?>[0];

        private ClientValues(ModConfigSpec.Builder builder) {
            builder.comment("Post-processing and menu rendering.")
                    .translation("substance.configuration.shaders")
                    .push("shaders");
            enableShaderEffects = builder
                    .comment("Enable post-processing shaders. Requires a game restart.")
                    .translation("substance.configuration.enableShaderEffects")
                    .gameRestart()
                    .define("enableShaderEffects", DEFAULT_ENABLE_SHADER_EFFECTS);
            visualEffectStrength = builder
                    .comment("Overall shader intensity.")
                    .translation("substance.configuration.visualEffectStrength")
                    .defineInRange("visualEffectStrength", DEFAULT_VISUAL_EFFECT_STRENGTH, 0.0, 2.0);
            visualEffectsInMenus = builder
                    .comment("Render shader effects while menus are open.")
                    .translation("substance.configuration.visualEffectsInMenus")
                    .define("visualEffectsInMenus", DEFAULT_VISUAL_EFFECTS_IN_MENUS);
            builder.pop();

            builder.comment("Hallucination shader and apparitions.")
                    .translation("substance.configuration.hallucination")
                    .push("hallucination");
            enableHallucinationVisuals = builder
                    .comment("Enable hallucination visuals.")
                    .translation("substance.configuration.enableHallucinationVisuals")
                    .define("enableHallucinationVisuals", DEFAULT_ENABLE_HALLUCINATION_VISUALS);
            hallucinationVisualStrength = builder
                    .comment("Hallucination distortion and apparition opacity.")
                    .translation("substance.configuration.hallucinationVisualStrength")
                    .defineInRange("hallucinationVisualStrength", DEFAULT_HALLUCINATION_VISUAL_STRENGTH, 0.0, 2.0);
            hallucinationApparitionInterval = builder
                    .comment("Average seconds between hallucination apparitions.")
                    .translation("substance.configuration.hallucinationApparitionInterval")
                    .defineInRange("hallucinationApparitionInterval", DEFAULT_HALLUCINATION_APPARITION_INTERVAL, 0.5, 60.0);
            hallucinationMaxApparitions = builder
                    .comment("Maximum visible hallucination apparitions. Zero disables them.")
                    .translation("substance.configuration.hallucinationMaxApparitions")
                    .defineInRange("hallucinationMaxApparitions", DEFAULT_HALLUCINATION_MAX_APPARITIONS, 0, 20);
            hallucinationVillagerChance = builder
                    .comment("Chance that an apparition is a floating villager.")
                    .translation("substance.configuration.hallucinationVillagerChance")
                    .defineInRange("hallucinationVillagerChance", DEFAULT_HALLUCINATION_VILLAGER_CHANCE, 0.0, 1.0);
            builder.pop();

            builder.comment("Dread shader and apparitions.")
                    .translation("substance.configuration.dread")
                    .push("dread");
            enableDreadVisuals = builder
                    .comment("Enable dread visuals.")
                    .translation("substance.configuration.enableDreadVisuals")
                    .define("enableDreadVisuals", DEFAULT_ENABLE_DREAD_VISUALS);
            dreadVisualStrength = builder
                    .comment("Dread darkness, distortion, and apparition opacity.")
                    .translation("substance.configuration.dreadVisualStrength")
                    .defineInRange("dreadVisualStrength", DEFAULT_DREAD_VISUAL_STRENGTH, 0.0, 2.0);
            dreadApparitionInterval = builder
                    .comment("Average seconds between dread apparitions.")
                    .translation("substance.configuration.dreadApparitionInterval")
                    .defineInRange("dreadApparitionInterval", DEFAULT_DREAD_APPARITION_INTERVAL, 0.5, 60.0);
            dreadMaxApparitions = builder
                    .comment("Maximum visible dread apparitions. Zero disables them.")
                    .translation("substance.configuration.dreadMaxApparitions")
                    .defineInRange("dreadMaxApparitions", DEFAULT_DREAD_MAX_APPARITIONS, 0, 20);
            dreadCreeperChance = builder
                    .comment("Chance that a dread apparition is a fake creeper.")
                    .translation("substance.configuration.dreadCreeperChance")
                    .defineInRange("dreadCreeperChance", DEFAULT_DREAD_CREEPER_CHANCE, 0.0, 1.0);
            dreadDistantEntityTypes = entityTypeList(builder, "dreadDistantEntityTypes", DEFAULT_DREAD_DISTANT_ENTITY_TYPES);
            dreadAnimalDistance = builder
                    .comment("Distance in blocks at which distant apparitions appear.")
                    .translation("substance.configuration.dreadAnimalDistance")
                    .defineInRange("dreadAnimalDistance", DEFAULT_DREAD_ANIMAL_DISTANCE, 4.0, 128.0);
            dreadAnimalFadeDistance = builder
                    .comment("Distance in blocks at which distant apparitions disappear.")
                    .translation("substance.configuration.dreadAnimalFadeDistance")
                    .defineInRange("dreadAnimalFadeDistance", DEFAULT_DREAD_ANIMAL_FADE_DISTANCE, 1.0, 64.0);
            builder.pop();

            builder.comment("Audio filters and ambient trip sounds.")
                    .translation("substance.configuration.audio")
                    .push("audio");
            enableAudioEffects = builder
                    .comment("Enable low-pass and reverb filters. Requires a game restart.")
                    .translation("substance.configuration.enableAudioEffects")
                    .gameRestart()
                    .define("enableAudioEffects", DEFAULT_ENABLE_AUDIO_EFFECTS);
            audioEffectStrength = builder
                    .comment("Audio-filter intensity.")
                    .translation("substance.configuration.audioEffectStrength")
                    .defineInRange("audioEffectStrength", DEFAULT_AUDIO_EFFECT_STRENGTH, 0.0, 2.0);
            enableAmbientSounds = builder
                    .comment("Enable ambient sounds during trips.")
                    .translation("substance.configuration.enableAmbientSounds")
                    .define("enableAmbientSounds", DEFAULT_ENABLE_AMBIENT_SOUNDS);
            ambientSoundInterval = builder
                    .comment("Average seconds between ambient trip sounds.")
                    .translation("substance.configuration.ambientSoundInterval")
                    .defineInRange("ambientSoundInterval", DEFAULT_AMBIENT_SOUND_INTERVAL, 1.0, 120.0);
        }

        public boolean enableShaderEffects() {
            return enableShaderEffects.getAsBoolean();
        }

        public float visualEffectStrength() {
            return visualEffectStrength.get().floatValue();
        }

        public boolean visualEffectsInMenus() {
            return visualEffectsInMenus.getAsBoolean();
        }

        public boolean enableHallucinationVisuals() {
            return enableHallucinationVisuals.getAsBoolean();
        }

        public float hallucinationVisualStrength() {
            return hallucinationVisualStrength.get().floatValue();
        }

        public float hallucinationApparitionInterval() {
            return hallucinationApparitionInterval.get().floatValue();
        }

        public int hallucinationMaxApparitions() {
            return hallucinationMaxApparitions.getAsInt();
        }

        public float hallucinationVillagerChance() {
            return hallucinationVillagerChance.get().floatValue();
        }

        public boolean enableDreadVisuals() {
            return enableDreadVisuals.getAsBoolean();
        }

        public float dreadVisualStrength() {
            return dreadVisualStrength.get().floatValue();
        }

        public float dreadApparitionInterval() {
            return dreadApparitionInterval.get().floatValue();
        }

        public int dreadMaxApparitions() {
            return dreadMaxApparitions.getAsInt();
        }

        public float dreadCreeperChance() {
            return dreadCreeperChance.get().floatValue();
        }

        public List<? extends String> dreadDistantEntityTypes() {
            return List.copyOf(dreadDistantEntityTypes.get());
        }

        public EntityType<?>[] dreadDistantEntityTypeCache() {
            return dreadDistantEntityTypeCache.clone();
        }

        public float dreadAnimalDistance() {
            return dreadAnimalDistance.get().floatValue();
        }

        public float dreadAnimalFadeDistance() {
            return dreadAnimalFadeDistance.get().floatValue();
        }

        public boolean enableAudioEffects() {
            return enableAudioEffects.getAsBoolean();
        }

        public float audioEffectStrength() {
            return audioEffectStrength.get().floatValue();
        }

        public boolean enableAmbientSounds() {
            return enableAmbientSounds.getAsBoolean();
        }

        public float ambientSoundInterval() {
            return ambientSoundInterval.get().floatValue();
        }

        private void refreshDreadDistantEntityTypes() {
            dreadDistantEntityTypeCache = dreadDistantEntityTypes.get().stream()
                    .map(ResourceLocation::tryParse)
                    .filter(Objects::nonNull)
                    .filter(BuiltInRegistries.ENTITY_TYPE::containsKey)
                    .map(BuiltInRegistries.ENTITY_TYPE::get)
                    .toArray(EntityType<?>[]::new);
        }
    }

    public static final class ServerValues implements GameplayOptions {
        private final ModConfigSpec.IntValue herbalRollDurability;
        private final ModConfigSpec.IntValue thickHerbalRollDurability;
        private final ModConfigSpec.IntValue cigaretteDurability;
        private final ModConfigSpec.DoubleValue maxSmokeDuration;
        private final ModConfigSpec.DoubleValue smokeCooldown;
        private final ModConfigSpec.DoubleValue maxSniffDuration;
        private final ModConfigSpec.DoubleValue sniffCooldown;
        private final ModConfigSpec.DoubleValue pipeItemConsumeProbability;
        private final ModConfigSpec.IntValue mobUseAttemptInterval;
        private final ModConfigSpec.DoubleValue horrorTripChance;
        private final ModConfigSpec.DoubleValue surgeMovementSpeedBonus;
        private final ModConfigSpec.DoubleValue surgeElytraBoost;
        private final ModConfigSpec.DoubleValue surgeElytraMaxSpeed;
        private final ModConfigSpec.DoubleValue surgeElytraMaxSpeedPerLevel;
        private final ModConfigSpec.DoubleValue keenMiningSpeedMultiplier;
        private final ModConfigSpec.IntValue relaxationDarknessDuration;

        private ServerValues(ModConfigSpec.Builder builder) {
            builder.comment("Consumable durability.")
                    .translation("substance.configuration.durability")
                    .push("durability");
            herbalRollDurability = builder
                    .comment("Maximum herbal roll durability.")
                    .translation("substance.configuration.herbalRollDurability")
                    .defineInRange("herbalRollDurability", DEFAULT_HERBAL_ROLL_DURABILITY, 1, Integer.MAX_VALUE);
            thickHerbalRollDurability = builder
                    .comment("Maximum thick herbal roll durability.")
                    .translation("substance.configuration.thickHerbalRollDurability")
                    .defineInRange("thickHerbalRollDurability", DEFAULT_THICK_HERBAL_ROLL_DURABILITY, 1, Integer.MAX_VALUE);
            cigaretteDurability = builder
                    .comment("Maximum cigarette durability.")
                    .translation("substance.configuration.cigaretteDurability")
                    .defineInRange("cigaretteDurability", DEFAULT_CIGARETTE_DURABILITY, 1, Integer.MAX_VALUE);
            builder.pop();

            builder.comment("Smoking, sniffing, and mob-use timing.")
                    .translation("substance.configuration.consumption")
                    .push("consumption");
            maxSmokeDuration = builder
                    .comment("Maximum smoking interaction duration in seconds.")
                    .translation("substance.configuration.maxSmokeDuration")
                    .defineInRange("maxSmokeDuration", DEFAULT_MAX_SMOKE_DURATION, 0.5, 120.0);
            smokeCooldown = builder
                    .comment("Cooldown after smoking in seconds.")
                    .translation("substance.configuration.smokeCooldown")
                    .defineInRange("smokeCooldown", DEFAULT_SMOKE_COOLDOWN, 0.0, 30.0);
            maxSniffDuration = builder
                    .comment("Maximum sniffing interaction duration in seconds.")
                    .translation("substance.configuration.maxSniffDuration")
                    .defineInRange("maxSniffDuration", DEFAULT_MAX_SNIFF_DURATION, 0.5, 30.0);
            sniffCooldown = builder
                    .comment("Cooldown after sniffing in seconds.")
                    .translation("substance.configuration.sniffCooldown")
                    .defineInRange("sniffCooldown", DEFAULT_SNIFF_COOLDOWN, 0.0, 30.0);
            pipeItemConsumeProbability = builder
                    .comment("Chance that using a pipe consumes the item inside.")
                    .translation("substance.configuration.pipeItemConsumeProbability")
                    .defineInRange("pipeItemConsumeProbability", DEFAULT_PIPE_ITEM_CONSUME_PROBABILITY, 0.0, 1.0);
            mobUseAttemptInterval = builder
                    .comment("Maximum ticks between attempts by an idle mob to use its held consumable.")
                    .translation("substance.configuration.mobUseAttemptInterval")
                    .defineInRange("mobUseAttemptInterval", DEFAULT_MOB_USE_ATTEMPT_INTERVAL, 1, Integer.MAX_VALUE);
            builder.pop();

            builder.comment("Effect behavior.")
                    .translation("substance.configuration.effects")
                    .push("effects");
            horrorTripChance = builder
                    .comment("Chance that red mushrooms cause a horror trip.")
                    .translation("substance.configuration.horrorTripChance")
                    .defineInRange("horrorTripChance", DEFAULT_HORROR_TRIP_CHANCE, 0.0, 1.0);
            surgeMovementSpeedBonus = builder
                    .comment("Relative movement-speed bonus per Surge level.")
                    .translation("substance.configuration.surgeMovementSpeedBonus")
                    .defineInRange("surgeMovementSpeedBonus", DEFAULT_SURGE_MOVEMENT_SPEED_BONUS, 0.0, 2.0);
            surgeElytraBoost = builder
                    .comment("Directional acceleration applied each tick while fall-flying with Surge.")
                    .translation("substance.configuration.surgeElytraBoost")
                    .defineInRange("surgeElytraBoost", DEFAULT_SURGE_ELYTRA_BOOST, 0.0, 0.2);
            surgeElytraMaxSpeed = builder
                    .comment("Maximum fall-flying speed at Surge I, in blocks per tick.")
                    .translation("substance.configuration.surgeElytraMaxSpeed")
                    .defineInRange("surgeElytraMaxSpeed", DEFAULT_SURGE_ELYTRA_MAX_SPEED, 0.25, 5.0);
            surgeElytraMaxSpeedPerLevel = builder
                    .comment("Additional maximum fall-flying speed per Surge level above I.")
                    .translation("substance.configuration.surgeElytraMaxSpeedPerLevel")
                    .defineInRange("surgeElytraMaxSpeedPerLevel", DEFAULT_SURGE_ELYTRA_MAX_SPEED_PER_LEVEL, 0.0, 2.0);
            keenMiningSpeedMultiplier = builder
                    .comment("Mining-speed multiplier applied by Keen.")
                    .translation("substance.configuration.keenMiningSpeedMultiplier")
                    .defineInRange("keenMiningSpeedMultiplier", DEFAULT_KEEN_MINING_SPEED_MULTIPLIER, 1.0, 10.0);
            relaxationDarknessDuration = builder
                    .comment("Darkness duration in ticks after attacking while relaxed.")
                    .translation("substance.configuration.relaxationDarknessDuration")
                    .defineInRange("relaxationDarknessDuration", DEFAULT_RELAXATION_DARKNESS_DURATION, 0, 1200);
        }

        @Override
        public int herbalRollDurability() {
            return herbalRollDurability.getAsInt();
        }

        @Override
        public int thickHerbalRollDurability() {
            return thickHerbalRollDurability.getAsInt();
        }

        @Override
        public int cigaretteDurability() {
            return cigaretteDurability.getAsInt();
        }

        @Override
        public float maxSmokeDuration() {
            return maxSmokeDuration.get().floatValue();
        }

        @Override
        public float smokeCooldown() {
            return smokeCooldown.get().floatValue();
        }

        @Override
        public float maxSniffDuration() {
            return maxSniffDuration.get().floatValue();
        }

        @Override
        public float sniffCooldown() {
            return sniffCooldown.get().floatValue();
        }

        @Override
        public float pipeItemConsumeProbability() {
            return pipeItemConsumeProbability.get().floatValue();
        }

        @Override
        public int mobUseAttemptInterval() {
            return mobUseAttemptInterval.getAsInt();
        }

        @Override
        public float horrorTripChance() {
            return horrorTripChance.get().floatValue();
        }

        @Override
        public float surgeMovementSpeedBonus() {
            return surgeMovementSpeedBonus.get().floatValue();
        }

        @Override
        public float surgeElytraBoost() {
            return surgeElytraBoost.get().floatValue();
        }

        @Override
        public float surgeElytraMaxSpeed() {
            return surgeElytraMaxSpeed.get().floatValue();
        }

        @Override
        public float surgeElytraMaxSpeedPerLevel() {
            return surgeElytraMaxSpeedPerLevel.get().floatValue();
        }

        @Override
        public float keenMiningSpeedMultiplier() {
            return keenMiningSpeedMultiplier.get().floatValue();
        }

        @Override
        public int relaxationDarknessDuration() {
            return relaxationDarknessDuration.getAsInt();
        }
    }
}
