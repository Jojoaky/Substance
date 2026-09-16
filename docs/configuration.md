# Configuration reference

[Content guide](content.md) | [Effects and potions](effects.md) | [Pipes](pipes.md)

Configure Substance in Mod Menu or in `.minecraft/config/substance.json` while Minecraft is stopped. Gameplay settings are server-authoritative and synchronized to clients; client settings apply only locally.

## Gameplay settings

### Durabilities

| Key                         | Default | Notes |
|-----------------------------|--------:|-------|
| `herbalRollDurability`      |   `460` |       |
| `thickHerbalRollDurability` |   `570` |       |
| `cigaretteDurability`       |   `525` |       |

Pipe durability is fixed: wooden pipes have 2048 durability and bubble pipes have 512 durability.

Existing partially consumed herbal rolls and cigarettes retain their stored maximum durability.

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
| `dreadDistantEntityTypes` | Six vanilla entity IDs | Entity IDs for distant apparitions |
| `dreadAnimalDistance`     | `24.0 blocks` | Spawn distance            |
| `dreadAnimalFadeDistance` |  `4.0 blocks` | Fade distance             |

## Resetting configuration

Use YACL's reset controls or delete `config/substance.json` while Minecraft is stopped to regenerate defaults.


# Data packs and resource packs

## Data packs

### Mob equipment definitions (experimental)

This mod adds a custom data format for defining equipment that mobs spawn with using loot tables.
The custom data format for mob equipment is **experimental**, and it might not work as expected.
The definitions live under `data/<namespace>/mob_equipment/<name>.json` and have these fields:

| Field       | Type    | Default | Description                                                                                               |
|-------------|---------|---------|-----------------------------------------------------------------------------------------------------------|
| `entities`  | array   | required | Entity IDs such as `minecraft:zombie`, or entity-tag IDs prefixed with `#`, such as `#substance:zombies`. |
| `equipment` | object  | required | Maps an equipment slot to a loot-table ID using the custom `"type": "substance:equipment"`.              |
| `priority`  | integer | `0`     | Higher-priority definitions run before lower-priority definitions.                                        |
| `override`  | boolean | `false` | When `true`, generated equipment may replace an item assigned by vanilla.                                 |

Definitions are ordered by descending `priority`, then exact entity entries before tag entries, then alphabetically by identifier.
The first definition that successfully assigns a slot wins.
`override` only controls equipment already present before these definitions run. It does not let a lower-priority definition replace an item assigned by a higher-priority definition.
Valid slot names include `mainhand`, `offhand`, `head`, `chest`, `legs`, and `feet`. 

`"type": "substance:equipment"` is a custom loot table format that includes information about the entity and the spawn location.


For example, this is used to spawn zombies with cigarettes and herbal rolls.
#### data/substance/mob_equipment/zombies.json:
```json
{
  "entities": ["#substance:zombies"],
  "equipment": {
    "mainhand": "substance:equipment/smoking_zombie"
  }
}
```

- `"substance:equipment/smoking_zombie"` is a `"type": "substance:equipment"` loot table.  
- `"#substance:zombies"` is an entity_type tag.

#### Replacing or disabling a definition

Mob equipment definitions follow normal data-pack resource priority. A higher-priority pack can replace a definition by using the same namespace and path. For example, a pack can disable the bundled `data/substance/mob_equipment/illagers.json` definition with:

```json
{
  "entities": [],
  "equipment": {}
}
```

The loader reads only the highest-priority version of `substance:illagers`. It does not merge that file with the version from Substance.

### Tags

Substance exposes the following tags.

| Tag                   | Registry     | Purpose                                                                        |
|-----------------------|--------------|--------------------------------------------------------------------------------|
| `substance:smokables` | Items        | Items recognized as smokables by the smoking visuals.                          |
| `substance:drugs`     | Items        | Bundled grouping of Substance's drug items, available for pack authors to use. |
| `substance:pipes`     | Items        | Bundled grouping of the mod's pipe items, available for pack authors to use.   |
| `substance:can_smoke` | Entity types | Mobs eligible for Substance's smoking behaviour.                               |
| `substance:zombies`   | Entity types | Zombie-family group used by the bundled equipment definition.                  |
| `substance:skeletons` | Entity types | Skeleton-family group used by the bundled equipment definition.                |
| `substance:piglins`   | Entity types | Piglin-family group used by the bundled equipment definition.                  |
| `substance:illagers`  | Entity types | Illager group used by the bundled equipment definition.                        |

### Loot tables and trades

Loot and trade patches are hard-coded in the mod and are currently not configurable.

### Post-processing shaders

Substance uses post-processing shaders to implement the visual effects.

Minecraft (1.20.1) does not support namespaces in post-processing chains and shader programs by default.
Substance uses a mixin to override this behavior.

All effect shaders are supplied with an Intensity uniform, additional uniforms may be set for specific effects.
