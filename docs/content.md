# Substance content guide

Substance adds growable ingredients, consumables, pipes, status effects, potions, and multistep chemistry. This page summarizes the available content and links to the detailed guides.

All durations and chances in these guides use the default settings. Server owners can change many of them. See the [configuration reference](configuration.md).

## Consumables

| Item | Main effects | Guide |
|------|--------------|-------|
| ![White Crystals](../src/main/resources/assets/substance/textures/item/white_crystals.png) White Crystals | [Surge](effects.md#surge) and [Warp](effects.md#warp) | [White Crystals](white-crystals.md#white-crystals) |
| ![Spiced White Crystals](../src/main/resources/assets/substance/textures/item/white_crystals_chili.png) Spiced White Crystals | Stronger [Surge](effects.md#surge), [Warp](effects.md#warp), and minor damage | [Spiced White Crystals](white-crystals.md#spiced-white-crystals) |
| ![Blue Crystals](../src/main/resources/assets/substance/textures/item/blue_crystals.png) Blue Crystals | Stronger [Surge](effects.md#surge) and [Warp](effects.md#warp) | [Blue Crystals](blue-crystals.md#blue-crystals) |
| ![Herbal Roll](../src/main/resources/assets/substance/textures/item/herbal_roll.png) Herbal Roll | [Relaxation](effects.md#relaxation) and [Warp](effects.md#warp) | [Herbal Roll](herbal-rolls.md#herbal-roll) |
| ![Thick Herbal Roll](../src/main/resources/assets/substance/textures/item/thick_herbal_roll.png) Thick Herbal Roll | Stronger [Relaxation](effects.md#relaxation) and [Warp](effects.md#warp), plus [Haze](effects.md#haze) | [Thick Herbal Roll](herbal-rolls.md#thick-herbal-roll) |
| ![Cigarette](../src/main/resources/assets/substance/textures/item/cigarette.png) Cigarette | [Keen](effects.md#keen) and Haste II | [Cigarette](cigarette.md#cigarette) |

Crystals are single-use powders. Finish the use action to consume one and receive its effects. Rolls and cigarettes burn down while held in use, so shorter draws consume less of the item and give shorter effects. A draw longer than five seconds also gives four seconds of Nausea.

## Pipes

| Item | Durability | How to obtain |
|------|-----------:|---------------|
| ![Wooden Pipe](../src/main/resources/assets/substance/textures/item/wooden_pipe.png) [Wooden Pipe](pipes.md#wooden-pipe) | 2,048 ticks | Pillagers, vindicators, pillager outposts, and woodland mansions |
| ![Bubble Pipe](../src/main/resources/assets/substance/textures/item/bubble_pipe.png) [Bubble Pipe](pipes.md#bubble-pipe) | 512 ticks | Cleric villagers and village chests |

Both pipes have five ingredient slots and accept dried herb buds, dried tobacco leaves, White Crystals, Blue Crystals, and red mushrooms. See the [pipe guide](pipes.md) for controls and ingredient effects.

## Effects and potions

Substance has seven active effects:

- [Haze](effects.md#haze) adds visual trails, distortion, and muffled audio.
- [Warp](effects.md#warp) bends the view, separates colors, and alters audio.
- [Keen](effects.md#keen) increases mining speed and narrows the visible area.
- [Relaxation](effects.md#relaxation) warms and blurs the view, but punishes attacks.
- [Surge](effects.md#surge) increases movement speed and grants boosted flight.
- [Hallucination](effects.md#hallucination) creates false blocks, floating villagers, and ambient sounds.
- [Dread](effects.md#dread) darkens the view and creates threatening apparitions.

Each effect has a normal, extended, and strong potion. The [effects and potions guide](effects.md#potions) lists every brewing ingredient and duration.

## Plants and farming

| Plant | Main product | Used for |
|-------|--------------|----------|
| ![Mature Large Herb](../src/main/resources/assets/substance/textures/block/large_herb_bottom_stage3.png) Large Herb | ![Herb Bud](../src/main/resources/assets/substance/textures/item/herb_bud.png) Herb Bud | [Herbal rolls](herbal-rolls.md) and Potions of [Relaxation](effects.md#relaxation) |
| ![Mature Tobacco](../src/main/resources/assets/substance/textures/block/tobacco_bottom_stage3.png) Tobacco | ![Ripe Tobacco Leaf](../src/main/resources/assets/substance/textures/item/ripe_tobacco_leaf.png) Ripe Tobacco Leaf | [Cigarettes](cigarette.md) and Potions of [Keen](effects.md#keen) |
| ![Mature Ephedra](../src/main/resources/assets/substance/textures/block/ephedra_stage3.png) Ephedra | ![Ephedra Bundle](../src/main/resources/assets/substance/textures/item/ephedra_bundle.png) Ephedra Bundle | [White Crystal chemistry](white-crystals.md#production-guide) |
| ![Mature Chili Plant](../src/main/resources/assets/substance/textures/block/chili_stage3.png) Chili Plant | ![Chili Pepper](../src/main/resources/assets/substance/textures/item/chili_pepper.png) Chili Pepper | [Spiced White Crystals](white-crystals.md#spiced-white-crystals) |

Herb seeds enter progression through piglin bartering. Farmers sell tobacco seeds and ephedra bundles, while wandering traders sell ephedra and chili seeds. Chili peppers can also appear in desert pyramids and desert or savanna village chests.

## Chemistry and processing

The chemistry system uses reusable glass flasks, gas bottles, liquid chemicals, and trays. White and Blue Crystals each have a complete vanilla-compatible production route. Installing Create adds machine recipes for many of the same stages.

- [White Crystal production](white-crystals.md#production-guide) uses pseudoephedrine, red phosphorus, iodine, water, and heat.
- [Blue Crystal production](blue-crystals.md#production-guide) uses two chemical branches that produce phenylacetone and methylamine.
- [Pipe ingredients](pipes.md#ingredients) provide another way to use the finished materials.

Chemical fluids can harm entities that stand in them. The more toxic fluids also apply Poison, Haze, or Blindness.

## World content

Substance adds items to vanilla loot, villager trades, wandering-trader trades, and piglin bartering. Some illagers, piglins, skeletons, and zombies can spawn with smoking items and use them while idle.

The advancement tree covers pipes, herb farming, tobacco, crystal production, chemistry, and collecting effects. It provides progression hints without locking the recipes behind advancements.

## Further reference

- [Configuration, data packs, and resource packs](configuration.md)
- [Project README](../README.md)
