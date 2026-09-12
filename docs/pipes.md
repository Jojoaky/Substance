# Pipes

[Content index](content.md) | [Effects and potions](effects.md) | [Herbal rolls](herbal-rolls.md) | [Cigarette](cigarette.md) | [White Crystals](white-crystals.md) | [Blue Crystals](blue-crystals.md)

Pipes are reusable smoking items with five internal ingredient slots. The Wooden Pipe and Bubble Pipe accept the same ingredients and produce the same effects. Their durability and acquisition methods differ.

## Obtaining pipes

### Wooden Pipe

![Wooden Pipe](../src/main/resources/assets/substance/textures/item/wooden_pipe.png)

- ID: `substance:wooden_pipe`
- Durability: 2,048 ticks, equal to 102.4 seconds of accumulated use

Wooden Pipes can appear in pillager outpost and woodland mansion chests. Pillagers and vindicators can also drop them. They cannot be crafted.

### Bubble Pipe

![Bubble Pipe](../src/main/resources/assets/substance/textures/item/bubble_pipe.png)

- ID: `substance:bubble_pipe`
- Durability: 512 ticks, equal to 25.6 seconds of accumulated use

Novice clerics sell a Bubble Pipe for 12 Emeralds. Bubble Pipes can also appear rarely in village house chests. They cannot be crafted.

## Loading and using a pipe

1. Crouch and use the pipe to open its five-slot inventory. Using an empty pipe also opens the inventory without crouching.
2. Insert one or more accepted ingredients. Each slot can hold a normal stack.
3. Stop crouching and hold use to smoke the loaded pipe.

The pipe processes each different ingredient type once per draw. Extra copies of the same ingredient act as a reserve and do not multiply that draw's effect.

By default, a complete six-second draw has a 40% chance to consume one item of each ingredient type. Shorter draws reduce that chance in proportion to their duration. Pipe durability decreases continuously while smoking. If a pipe breaks, it drops its remaining contents.

## Base pipe effects

Every loaded pipe provides [Warp](effects.md#warp) while it is in use. Releasing the use control applies [Haze](effects.md#haze), with duration based on the length of the draw. A draw longer than five seconds also gives four seconds of Nausea.

Ingredient effects are added to these base effects.

## Ingredients

| Ingredient | Effect from smoking | Related guide |
|------------|---------------------|---------------|
| ![Dried Herb Bud](../src/main/resources/assets/substance/textures/item/dried_herb_bud.png) Dried Herb Bud | [Relaxation](effects.md#relaxation), stacking up to level V | [Herbal rolls](herbal-rolls.md#obtaining-dried-herb-buds) |
| ![Dried Tobacco Leaf](../src/main/resources/assets/substance/textures/item/dried_tobacco_leaf.png) Dried Tobacco Leaf | [Keen](effects.md#keen), stacking up to level IV | [Cigarette](cigarette.md#obtaining-dried-tobacco-leaves) |
| ![White Crystals](../src/main/resources/assets/substance/textures/item/white_crystals.png) White Crystals | [Surge](effects.md#surge), stacking up to level II | [White Crystals](white-crystals.md) |
| ![Blue Crystals](../src/main/resources/assets/substance/textures/item/blue_crystals.png) Blue Crystals | [Surge](effects.md#surge), stacking up to level IV | [Blue Crystals](blue-crystals.md) |
| Red Mushroom | [Hallucination](effects.md#hallucination), or sometimes [Dread](effects.md#dread) | [Red mushroom](#red-mushroom) |

Effect duration grows with the length of the draw. Repeated draws before an effect expires can extend its duration and raise its level up to the listed limit.

### Red mushroom

Red mushrooms normally cause [Hallucination](effects.md#hallucination), which can stack up to level II. Each draw has a 10% default chance to cause [Dread](effects.md#dread) instead. A Dread result removes an existing Hallucination effect and plays a thunder sound.

Server owners can change the ingredient consumption chance, maximum draw duration, cooldown, and Dread chance. See [smoking and sniffing in the configuration reference](configuration.md#smoking-and-sniffing).

[Back to the content index](content.md)
