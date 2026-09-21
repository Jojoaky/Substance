package jojoaky.substance.config;

/** Read-only gameplay settings shared by logical client and server code. */
public interface GameplayOptions {
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
