package jojoaky.substance.config;

import net.minecraft.network.FriendlyByteBuf;

/**
 * An immutable snapshot of every server-authoritative config option.
 *
 * <p>The explicit codec is intentionally kept here so the persisted config and
 * the network representation do not become coupled to Gson or YACL.</p>
 */
public record GameplayConfig(
        int woodenPipeDurability,
        int bubblePipeDurability,
        int herbalRollDurability,
        int thickHerbalRollDurability,
        int cigaretteDurability,
        float maxSmokeDuration,
        float smokeCooldown,
        float maxSniffDuration,
        float sniffCooldown,
        float pipeItemConsumeProbability,
        int mobUseAttemptInterval,
        float horrorTripChance,
        float surgeMovementSpeedBonus,
        float surgeElytraBoost,
        float surgeElytraMaxSpeed,
        float surgeElytraMaxSpeedPerLevel,
        float keenMiningSpeedMultiplier,
        int relaxationDarknessDuration
) implements GameplayOptions {
    public static GameplayConfig from(GameplayOptions config) {
        return new GameplayConfig(
                config.woodenPipeDurability(),
                config.bubblePipeDurability(),
                config.herbalRollDurability(),
                config.thickHerbalRollDurability(),
                config.cigaretteDurability(),
                config.maxSmokeDuration(),
                config.smokeCooldown(),
                config.maxSniffDuration(),
                config.sniffCooldown(),
                config.pipeItemConsumeProbability(),
                config.mobUseAttemptInterval(),
                config.horrorTripChance(),
                config.surgeMovementSpeedBonus(),
                config.surgeElytraBoost(),
                config.surgeElytraMaxSpeed(),
                config.surgeElytraMaxSpeedPerLevel(),
                config.keenMiningSpeedMultiplier(),
                config.relaxationDarknessDuration()
        );
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(woodenPipeDurability);
        buffer.writeVarInt(bubblePipeDurability);
        buffer.writeVarInt(herbalRollDurability);
        buffer.writeVarInt(thickHerbalRollDurability);
        buffer.writeVarInt(cigaretteDurability);
        buffer.writeFloat(maxSmokeDuration);
        buffer.writeFloat(smokeCooldown);
        buffer.writeFloat(maxSniffDuration);
        buffer.writeFloat(sniffCooldown);
        buffer.writeFloat(pipeItemConsumeProbability);
        buffer.writeVarInt(mobUseAttemptInterval);
        buffer.writeFloat(horrorTripChance);
        buffer.writeFloat(surgeMovementSpeedBonus);
        buffer.writeFloat(surgeElytraBoost);
        buffer.writeFloat(surgeElytraMaxSpeed);
        buffer.writeFloat(surgeElytraMaxSpeedPerLevel);
        buffer.writeFloat(keenMiningSpeedMultiplier);
        buffer.writeVarInt(relaxationDarknessDuration);
    }

    public static GameplayConfig read(FriendlyByteBuf buffer) {
        return new GameplayConfig(
                buffer.readVarInt(),
                buffer.readVarInt(),
                buffer.readVarInt(),
                buffer.readVarInt(),
                buffer.readVarInt(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readVarInt(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readVarInt()
        );
    }
}
