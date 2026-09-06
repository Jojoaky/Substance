package jojoaky.substance.config;

/** Read-only view of the gameplay settings used by common client/server code. */
public interface GameplayOptions {
    int woodenPipeDurability();
    int bubblePipeDurability();
    int herbalRollDurability();
    int thickHerbalRollDurability();
    int cigaretteDurability();
    float maxSmokeDuration();
    float smokeCooldown();
    float maxSniffDuration();
    float sniffCooldown();
    float pipeItemConsumeProbability();
    int mobUseAttemptInterval();
    float horrorTripChance();
    float surgeMovementSpeedBonus();
    float surgeElytraBoost();
    float surgeElytraMaxSpeed();
    float surgeElytraMaxSpeedPerLevel();
    float keenMiningSpeedMultiplier();
    int relaxationDarknessDuration();
}
