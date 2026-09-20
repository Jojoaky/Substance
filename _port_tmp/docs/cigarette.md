# Cigarette

[Content index](content.md) | [Effects and potions](effects.md) | [Pipes](pipes.md) | [Configuration](configuration.md)

![Cigarette](../src/main/resources/assets/substance/textures/item/cigarette.png)

- ID: `substance:cigarette`
- Stack size: 16
- Default total smoking time: 26.25 seconds

Cigarettes are reusable consumables made from dried tobacco leaves and paper. They burn down only while held in use.

## Obtaining dried tobacco leaves

![Mature Tobacco](../src/main/resources/assets/substance/textures/block/tobacco_bottom_stage3.png) ![Ripe Tobacco Leaf](../src/main/resources/assets/substance/textures/item/ripe_tobacco_leaf.png) ![Dried Tobacco Leaf](../src/main/resources/assets/substance/textures/item/dried_tobacco_leaf.png)

1. Buy three Tobacco Seeds from an apprentice farmer for four Emeralds.
2. Plant the seeds on farmland with enough room for the plant's two-block height. Bone Meal can accelerate its growth.
3. Use Shears on a mature plant to collect one to four Ripe Tobacco Leaves without destroying it. The plant returns to its first growth stage. A dispenser facing the mature plant can do the same job when it contains Shears and receives redstone power. Each harvest costs one Shears durability.
4. Dry each leaf in a smoker for 7.5 seconds or a furnace for 15 seconds. Either method gives one Dried Tobacco Leaf and 1 experience.

Breaking a mature plant produces leaves and more seeds, but removes the plant.

## Crafting

Craft the Cigarette in one horizontal row:

| Dried Tobacco Leaf | Paper | Dried Tobacco Leaf |
|--------------------|-------|--------------------|

## Effects

Smoking maintains [Keen](effects.md#keen) while the item is in use. Releasing the use control extends Keen according to the draw length.  
Repeated draws can raise Keen to level III.

A draw longer than five seconds also gives four seconds of Nausea.
Dried Tobacco Leaves can provide Keen without the Haste effect when used in a [pipe](pipes.md#ingredients).

The total smoking time is covered by the [durability settings](configuration.md#durabilities).
Maximum draw duration and cooldown are covered by [smoking and sniffing](configuration.md#smoking-and-sniffing), and Keen has a configurable [mining-speed multiplier](configuration.md#effect-behavior).

[Back to the content index](content.md)
