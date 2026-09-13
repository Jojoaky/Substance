# Blue Crystals

[Content index](content.md) | [Effects and potions](effects.md) | [Pipes](pipes.md) | [White Crystals](white-crystals.md) | [Configuration](configuration.md)

![Blue Crystals](../src/main/resources/assets/substance/textures/item/blue_crystals.png)

- ID: `substance:blue_crystals`
- Stack size: 16
- Rarity: Epic

Blue Crystals are a single-use powder with a longer chemistry chain and higher effect limits than White Crystals.

## Properties

Hold use for the full 2.5 seconds. One item is consumed and the player receives about
55 seconds of [Surge II](effects.md#surge) and 27.5 seconds of [Warp I](effects.md#warp).

Repeated uses before the effects expire can raise Surge and Warp as high as level IV. 
Blue Crystals can also provide Surge when smoked in a [pipe](pipes.md#ingredients).

## Production guide

Blue Crystal Oil combines two branches. One produces Phenylacetone, while the other produces Methylamine.

### Basic equipment and gases

![Glass Flask](../src/main/resources/assets/substance/textures/item/flask.png) ![Empty Gas Bottle](../src/main/resources/assets/substance/textures/item/gas_bottle.png) ![Tray](../src/main/resources/assets/substance/textures/item/tray.png)

- Craft six Glass Flasks with five Glass in a U shape.
- Craft three Empty Gas Bottles with three Glass placed diagonally from the bottom-left to the top-right of the crafting grid.
- Use an Empty Gas Bottle on a downward magma-block bubble column to collect Hydrogen.
- Use an Empty Gas Bottle on an upward soul-sand bubble column to collect Oxygen.
- Craft an Empty Gas Bottle with a Potato to obtain Nitrogen.
- Craft one Tray from a horizontal row of three Iron Ingots. Apprentice toolsmiths also sell Trays for three Emeralds.

### Phenylacetone branch

| Step | Inputs                                             | Output                                                                                             |
|-----:|----------------------------------------------------|----------------------------------------------------------------------------------------------------|
|    1 | Apple                                              | ![Cyanide Powder](../src/main/resources/assets/substance/textures/item/cyanide.png) Cyanide Powder |
|    2 | Cyanide Powder and Water Flask                     | Phenylacetic Acid Flask                                                                            |
|    3 | Oxygen Gas Bottle and Sugar                        | Acetic Anhydride Flask                                                                             |
|    4 | Phenylacetic Acid Flask and Acetic Anhydride Flask | Two Phenylacetone Flasks                                                                           |

### Methylamine branch

First make a Sculk Catalyst Crystal. Without Create, combine a Sculk Catalyst, an Echo Shard, and a Lava Flask to produce two crystals. Vanilla crafting recipes return the crystal after using it as a catalyst.

| Step | Inputs | Output |
|-----:|--------|--------|
| 1 | Nitrogen Gas Bottle and Hydrogen Gas Bottle | Ammonia Flask |
| 2 | Hydrogen Gas Bottle, Sculk Catalyst Crystal, and any coal item | Methanol Flask |
| 3 | Ammonia Flask, Methanol Flask, and Sculk Catalyst Crystal | Two Methylamine Flasks |

All three recipes are shapeless.

### Finishing the crystals

![Blue Oil Tray](../src/main/resources/assets/substance/textures/item/blue_oil_tray.png)

1. Combine one Phenylacetone Flask with one Methylamine Flask to make one Blue Crystal Oil Flask.
2. Combine a Tray with three Blue Crystal Oil Flasks to make a Blue Oil Tray.
3. Place the filled tray and wait 1.5 seconds for it to dry.
4. Use a pickaxe on the dry tray to obtain one item of Blue Crystals. The Tray remains and the pickaxe takes one durability damage.

### Create route

Create can process the same chemistry as fluids:

- Heat and compact a Sculk Catalyst with an Echo Shard to make two Sculk Catalyst Crystals.
- Mix Cyanide Powder with 10 mB of water to make 10 mB of Phenylacetic Acid.
- Mix an Oxygen Gas Bottle with Sugar to make 10 mB of Acetic Anhydride.
- Mix equal amounts of Phenylacetic Acid and Acetic Anhydride to make twice that amount of Phenylacetone.
- Mix Hydrogen and Nitrogen Gas Bottles to make 10 mB of Ammonia.
- Mix a Hydrogen Gas Bottle, a Sculk Catalyst Crystal, and coal to make 10 mB of Methanol.
- Mix equal amounts of Ammonia and Methanol with a Sculk Catalyst Crystal to make twice that amount of Methylamine.
- Mix equal amounts of Phenylacetone and Methylamine to make Blue Crystal Oil.
- Fill a Tray with 30 mB of Blue Crystal Oil, then press it to obtain Blue Crystals.

Create also supports producing Hydrogen and Oxygen together by mixing three Empty Gas Bottles, 10 mB of water, and a Sculk Catalyst Crystal.

The use duration and cooldown are configurable under [smoking and sniffing](configuration.md#smoking-and-sniffing).

[Back to the content index](content.md)
