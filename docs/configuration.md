# Configuration reference

Configure Substance in Mod Menu or in `.minecraft/config/substance.json` while Minecraft is stopped. Gameplay settings are server-authoritative and synchronized to clients; client settings apply only locally.

## Gameplay settings

### Durabilities

| Key                         | Default | Notes |
|-----------------------------|--------:|-------|
| `woodenPipeDurability`      |  `2048` |       |
| `bubblePipeDurability`      |   `512` |       |
| `herbalRollDurability`      |   `460` |       |
| `thickHerbalRollDurability` |   `570` |       |
| `cigaretteDurability`       |   `525` |       |

Existing partially consumed items retain their stored maximum durability.

### Smoking and sniffing

| Key                          |     Default | Notes                     |
|------------------------------|------------:|---------------------------|
| `maxSmokeDuration`           |     `6.0 s` |                           |
| `smokeCooldown`              |     `1.5 s` |                           |
| `maxSniffDuration`           |     `2.5 s` |                           |
| `sniffCooldown`              |     `2.0 s` |                           |
| `pipeItemConsumeProbability` |       `0.4` | Probability (`0.0`-`1.0`) |
| `mobUseAttemptInterval`      | `140 ticks` | -                         |
| `horrorTripChance`           |       `0.1` | Probability (`0.0`-`1.0`) |

### Effect behavior

| Key                           |     Default | Notes                |
|-------------------------------|------------:|----------------------|
| `surgeMovementSpeedBonus`     |       `0.2` | Relative speed bonus |
| `surgeElytraBoost`            |     `0.025` | Per tick             |
| `surgeElytraMaxSpeed`         |      `0.25` | For Surge I          |
| `surgeElytraMaxSpeedPerLevel` |       `0.1` | Per additional level |
| `keenMiningSpeedMultiplier`   |       `3.0` | Multiplier           |
| `relaxationDarknessDuration`  | `120 ticks` |                      |

## Client settings

| Key                    | Default | Notes            |
|------------------------|--------:|------------------|
| `enableShaderEffects`  |  `true` | Requires restart |
| `visualEffectStrength` |   `1.0` |                  |
| `visualEffectsInMenus` |  `true` |                  |
| `enableAudioEffects`   |  `true` | Requires restart |
| `audioEffectStrength`  |   `1.0` |                  |
| `enableAmbientSounds`  |  `true` |                  |
| `ambientSoundInterval` |  `30 s` | Average interval |

### Hallucination visuals

| Key                               | Default | Notes                     |
|-----------------------------------|--------:|---------------------------|
| `enableHallucinationVisuals`      |  `true` |                           |
| `hallucinationVisualStrength`     |   `1.0` |                           |
| `hallucinationApparitionInterval` | `4.0 s` | Average interval          |
| `hallucinationMaxApparitions`     |     `8` | `0` disables apparitions  |
| `hallucinationVillagerChance`     |  `0.25` | Probability (`0.0`-`1.0`) |

### Dread visuals

| Key                       |       Default | Notes                     |
|---------------------------|--------------:|---------------------------|
| `enableDreadVisuals`      |        `true` |                           |
| `dreadVisualStrength`     |         `1.0` |                           |
| `dreadApparitionInterval` |       `4.0 s` | Average interval          |
| `dreadMaxApparitions`     |           `8` | `0` disables apparitions  |
| `dreadCreeperChance`      |         `0.2` | Probability (`0.0`-`1.0`) |
| `dreadAnimalDistance`     | `24.0 blocks` | Spawn distance            |
| `dreadAnimalFadeDistance` |  `4.0 blocks` | Fade distance             |

## Resetting configuration

Use YACL's reset controls or delete `config/substance.json` while Minecraft is stopped to regenerate defaults.
