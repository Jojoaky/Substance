# Effects and potions

[Content index](content.md) | [Pipes](pipes.md) | [Configuration](configuration.md)

Substance effects can change gameplay, rendering, or sound. Visual and audio processing can be reduced or disabled in the client configuration without removing the gameplay effects.

## Potions

Every effect on this page has three potion variants:

| Variant  | Duration            | Level | Brewing step                              |
|----------|---------------------|-------|-------------------------------------------|
| Normal   | 30 seconds          | I     | Awkward Potion plus the effect ingredient |
| Extended | 1 minute 30 seconds | I     | Normal potion plus Redstone Dust          |
| Strong   | 15 seconds          | II    | Normal potion plus Glowstone Dust         |

Splash and lingering versions can be made with the normal Minecraft brewing steps.

| Effect                          | Ingredient                                                                                                            |
|---------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| [Haze](#haze)                   | Fermented Spider Eye                                                                                                  |
| [Warp](#warp)                   | ![Cyanide Powder](../src/main/resources/assets/substance/textures/item/cyanide.png) Cyanide Powder                    |
| [Keen](#keen)                   | ![Dried Tobacco Leaf](../src/main/resources/assets/substance/textures/item/dried_tobacco_leaf.png) Dried Tobacco Leaf |
| [Relaxation](#relaxation)       | ![Herb Bud](../src/main/resources/assets/substance/textures/item/herb_bud.png) Herb Bud                               |
| [Surge](#surge)                 | ![White Crystals](../src/main/resources/assets/substance/textures/item/white_crystals.png) White Crystals             |
| [Hallucination](#hallucination) | Red Mushroom                                                                                                          |
| [Dread](#dread)                 | Sculk                                                                                                                 |

## Haze

![Haze](../src/main/resources/assets/substance/textures/mob_effect/haze.png)

- ID: `substance:haze`
- Category: Neutral
- Potion ingredient: Fermented Spider Eye

Haze makes the view drift and leaves delayed trails, especially near the edges of the screen. It also muffles high-frequency audio and adds reverb. Haze has no direct attribute or damage effect.

Sources include [pipes](pipes.md#base-pipe-effects), [Thick Herbal Rolls](herbal-rolls.md#thick-herbal-roll), toxic chemical fluids, and Potions of Haze.

## Warp

![Warp](../src/main/resources/assets/substance/textures/mob_effect/warp.png)

- ID: `substance:warp`
- Category: Neutral
- Potion ingredient: ![Cyanide Powder](../src/main/resources/assets/substance/textures/item/cyanide.png) Cyanide Powder

Warp slowly bends the view and separates the red and blue color channels. Like Haze, it muffles high-frequency audio and adds reverb. Warp has no direct attribute or damage effect.

Sources include [herbal rolls](herbal-rolls.md#effects), [crystals](white-crystals.md#properties), [pipes](pipes.md#base-pipe-effects), and Potions of Warp.

## Keen

![Keen](../src/main/resources/assets/substance/textures/mob_effect/keen.png)

- ID: `substance:keen`
- Category: Beneficial
- Potion ingredient: ![Dried Tobacco Leaf](../src/main/resources/assets/substance/textures/item/dried_tobacco_leaf.png) Dried Tobacco Leaf

Keen multiplies mining speed by 3 at the default configuration. Its visual effect slightly desaturates the world and darkens the edges of the view.

Sources include [cigarettes](cigarette.md#effects), dried tobacco leaves smoked in a [pipe](pipes.md#ingredients), and Potions of Keen.

## Relaxation

![Relaxation](../src/main/resources/assets/substance/textures/mob_effect/relaxation.png)

- ID: `substance:relaxation`
- Category: Beneficial
- Potion ingredient: ![Herb Bud](../src/main/resources/assets/substance/textures/item/herb_bud.png) Herb Bud

Relaxation warms the screen colors and adds blur. Damaging a mob or another player, including with a ranged attack, immediately removes Relaxation, strikes the attacker with lightning, and applies six seconds of Darkness by default.

Sources include [herbal rolls](herbal-rolls.md#effects), dried herb buds smoked in a [pipe](pipes.md#ingredients), and Potions of Relaxation.

## Surge

![Surge](../src/main/resources/assets/substance/textures/mob_effect/surge.png)

- ID: `substance:surge`
- Category: Beneficial
- Potion ingredient: ![White Crystals](../src/main/resources/assets/substance/textures/item/white_crystals.png) White Crystals

Each level of Surge adds 20% movement speed at the default configuration. It also lets players start fall-flying without an Elytra by using the normal jump control while airborne. Surge accelerates the player in the direction they look and limits that boost according to the effect level.

The visual effect adds radial motion trails and speed lines while leaving the center of the view clear.

Sources include [White Crystals](white-crystals.md#properties), [Blue Crystals](blue-crystals.md#properties), crystals smoked in a [pipe](pipes.md#ingredients), and Potions of Surge.

## Hallucination

![Hallucination](../src/main/resources/assets/substance/textures/mob_effect/hallucination.png)

- ID: `substance:hallucination`
- Category: Neutral
- Potion ingredient: Red Mushroom

Hallucination adds light screen distortion, color separation, and drifting marks. It can show displaced copies of nearby blocks or floating villagers without changing the world. Occasional ambient sounds play while the effect is active.

Smoke a red mushroom in a [pipe](pipes.md#red-mushroom) or brew a Potion of Hallucination to receive the effect.

## Dread

![Dread](../src/main/resources/assets/substance/textures/mob_effect/dread.png)

- ID: `substance:dread`
- Category: Harmful
- Potion ingredient: Sculk

Dread darkens and desaturates the view. It can create a false Creeper behind the player or groups of distant creatures that disappear when approached. It also plays occasional ambient sounds. These apparitions cannot damage the player and do not exist on the server.

By default, smoking a red mushroom in a [pipe](pipes.md#red-mushroom) has a 10% chance to cause Dread instead of Hallucination. Potions of Dread provide a predictable source.

[Back to the content index](content.md)
