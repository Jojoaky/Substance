# Herbal rolls

[Content index](content.md) | [Effects and potions](effects.md) | [Pipes](pipes.md) | [Configuration](configuration.md)

![Herbal Roll](../src/main/resources/assets/substance/textures/item/herbal_roll.png) ![Thick Herbal Roll](../src/main/resources/assets/substance/textures/item/thick_herbal_roll.png)

Herbal Rolls and Thick Herbal Rolls are reusable consumables made from dried herb buds and paper. They burn down only while held in use, and longer draws give longer and stronger effects.

## Obtaining dried herb buds

![Mature Large Herb](../src/main/resources/assets/substance/textures/block/large_herb_bottom_stage3.png) ![Herb Bud](../src/main/resources/assets/substance/textures/item/herb_bud.png) ![Dried Herb Bud](../src/main/resources/assets/substance/textures/item/dried_herb_bud.png)

1. Obtain Herb Seeds through piglin bartering. Piglins can also barter dried herb buds directly.
2. Plant the Large Herb with enough room for its two-block height. Bone Meal can accelerate its growth.
3. Use Shears on a mature plant to collect one to three Herb Buds without destroying it. The plant returns to an earlier growth stage.
4. Dry each Herb Bud in a smoker for 7.5 seconds or a furnace for 15 seconds. Either method gives one Dried Herb Bud and 1 experience.

One Herb Bud can also be crafted into one Herb Seed.

## Herbal Roll

![Herbal Roll](../src/main/resources/assets/substance/textures/item/herbal_roll.png)

- ID: `substance:herbal_roll`
- Stack size: 16
- Default total smoking time: 23 seconds

Craft the roll in one horizontal row:

| Paper | Dried Herb Bud | Paper |
|-------|----------------|-------|

## Thick Herbal Roll

![Thick Herbal Roll](../src/main/resources/assets/substance/textures/item/thick_herbal_roll.png)

- ID: `substance:thick_herbal_roll`
- Stack size: 16
- Default total smoking time: 28.5 seconds

Without Create, use this shaped recipe:

|                | Paper          |                |
|----------------|----------------|----------------|
| Dried Herb Bud | Dried Herb Bud | Dried Herb Bud |
|                | Paper          |                |

When Create is installed, the normal recipe is replaced by Mechanical Crafting with this five-item row:

| Paper | Dried Herb Bud | Dried Herb Bud | Dried Herb Bud | Paper |
|-------|----------------|----------------|----------------|-------|

## Effects

The normal Herbal Roll provides [Relaxation](effects.md#relaxation) and [Warp](effects.md#warp). The Thick Herbal Roll builds both effects faster, reaches higher levels, and also applies [Haze](effects.md#haze).

| Item | Maximum effect levels |
|------|-----------------------|
| Herbal Roll | Relaxation III and Warp IV |
| Thick Herbal Roll | Relaxation IV, Warp V, and Haze II |

Both items keep Relaxation and Warp active while the player smokes. Releasing the use control adds duration according to the length of the draw. Repeated draws before an effect expires can raise its level. Draws longer than five seconds add four seconds of Nausea.

Dried Herb Buds can also provide Relaxation when used in a [pipe](pipes.md#ingredients).

The total smoking time is covered by the [durability settings](configuration.md#durabilities). Maximum draw duration and cooldown are covered by [smoking and sniffing](configuration.md#smoking-and-sniffing).

[Back to the content index](content.md)
