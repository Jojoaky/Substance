# Configuration reference

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
| `dreadAnimalDistance`     | `24.0 blocks` | Spawn distance            |
| `dreadAnimalFadeDistance` |  `4.0 blocks` | Fade distance             |

## Resetting configuration

Use YACL's reset controls or delete `config/substance.json` while Minecraft is stopped to regenerate defaults.


# Data packs and resource packs

## Data packs

### Mob equipment definitions (experimental)

The custom data format below is **experimental**: field names
and behavior may change between mod versions, and it might not work as expected.

Server data packs may add or replace files under
`data/<namespace>/mob_equipment/<name>.json`. Substance reads these files whenever server data is
reloaded and gives matching mobs equipment generated from a loot table. Exact entity entries are
applied before tag entries (priorities 200 and 100 respectively), then by identifier; the first
definition that successfully assigns a slot wins.

Each file has these fields:

| Field       | Type             | Description                                                                                               |
|-------------|------------------|-----------------------------------------------------------------------------------------------------------|
| `entities`  | array of strings | Entity IDs such as `minecraft:zombie`, or entity-tag IDs prefixed with `#`, such as `#substance:zombies`. |
| `equipment` | object           | Maps an equipment slot to a loot-table ID. A loot table must generate zero or one item.                   |

Valid slot names include `mainhand`, `offhand`, `head`, `chest`, `legs`, and `feet`. For example,
this makes entities in a custom tag receive the smoking-zombie loot table in their main hand:

```json
{
  "entities": ["#example:smoking_mobs"],
  "equipment": {
    "mainhand": "substance:equipment/smoking_zombie"
  }
}
```

The mod's bundled definitions live in `data/substance/mob_equipment/`. A file with the same
namespace and path in a higher-priority data pack replaces the bundled file. To provide the tag in
the example, create `data/example/tags/entity_types/smoking_mobs.json` using the normal tag format.

### Tags

Substance exposes the following tags. Add values to them from a data pack with the standard
Minecraft tag JSON format; use `"replace": false` to extend the built-in values.

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

For example, to allow a custom item to be used as a smokable, add
`data/substance/tags/items/smokables.json` to a data pack:

```json
{
  "replace": false,
  "values": [
    "example:herbal_blend"
  ]
}
```

Tags are resolved at reload time. They can refer to other tags with `#namespace:tag_name`, just
like vanilla tags.

`smokables` and `can_smoke` are checked directly by the mod. The remaining bundled tags are useful
groups for equipment definitions and compatible packs, but are not additional configurable gameplay
rules on their own.

### Generated data and integrations

The mod ships recipes, loot tables, advancements, and optional Create recipes as regular data-pack
resources. They may be overridden in the usual way. Recipes requiring Create are conditional and
are available only when Create is installed.

Some generated `data/substance/datapatch` files describe loot and merchant-trade additions, but
they are currently reference/generated data only. Runtime loot and trade patches are registered by
the mod itself, so supplying a `datapatch` JSON file does not currently change gameplay.

## Resource packs

Textures, language files, sounds, item models, blockstates, and other standard Minecraft assets can
be overridden at their normal `assets/substance/...` paths. In particular, Substance's visual effects
are ordinary Minecraft post-processing shader resources, so a resource pack can replace their JSON
or GLSL files without code changes.

### Post-processing shaders

The built-in post chains are:

| Effect | Post-chain resource |
|--------|---------------------|
| Dread | `assets/substance/shaders/post/dread.json` |
| Hallucination | `assets/substance/shaders/post/hallucination.json` |
| Haze | `assets/substance/shaders/post/haze.json` |
| Keen | `assets/substance/shaders/post/keen.json` |
| Relaxation | `assets/substance/shaders/post/relaxation.json` |
| Stagger | `assets/substance/shaders/post/stagger.json` |
| Surge | `assets/substance/shaders/post/surge.json` |
| Warp | `assets/substance/shaders/post/warp.json` |

To modify an effect, copy the desired file to the same path in a resource pack and edit it. Post
chains declare passes and intermediate targets; a pass's `name` points to a program definition in
`assets/substance/shaders/program/<name>.json`. That program JSON selects the vertex and fragment
shader and declares its uniforms, while the fragment shader is `<name>.fsh` in the same directory.

For example, the hallucination post chain contains a pass named
`substance:hallucination`; its program is
`assets/substance/shaders/program/hallucination.json`, which uses
`assets/substance/shaders/program/hallucination.fsh`. Copying and editing the `.fsh` file changes
the effect while retaining the mod's existing chain and uniforms.

Keep the uniforms expected by the effect when replacing a program. Substance updates effect-specific
uniforms such as `Intensity` and may set shared uniforms across all passes. Missing or malformed
post-chain/program JSON, or missing shader files, disables that shader and logs an error rather than
preventing the game from starting. Reload resource packs after changes; if shader effects were
disabled through `enableShaderEffects`, restart Minecraft after re-enabling them.
