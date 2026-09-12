# White Crystals

[Content index](content.md) | [Effects and potions](effects.md) | [Pipes](pipes.md) | [Blue Crystals](blue-crystals.md) | [Configuration](configuration.md)

![White Crystals](../src/main/resources/assets/substance/textures/item/white_crystals.png) ![Spiced White Crystals](../src/main/resources/assets/substance/textures/item/white_crystals_chili.png)

White Crystals are single-use powders produced through a chemistry chain. Spiced White Crystals trade some duration for a stronger immediate effect.

## White Crystals

![White Crystals](../src/main/resources/assets/substance/textures/item/white_crystals.png)

- ID: `substance:white_crystals`
- Stack size: 16
- Rarity: Uncommon

Hold use for the full 2.5 seconds. One item is consumed and the player receives about 50 seconds of [Surge I](effects.md#surge) and 25 seconds of [Warp I](effects.md#warp) with the default settings.

White Crystals can also be smoked in a [pipe](pipes.md#ingredients) or used to brew a Potion of [Surge](effects.md#surge).

## Spiced White Crystals

![Spiced White Crystals](../src/main/resources/assets/substance/textures/item/white_crystals_chili.png)

- ID: `substance:white_crystals_chili`
- Stack size: 16
- Rarity: Rare

Combine one item of White Crystals with one Chili Pepper in any crafting arrangement.

A completed use deals one point of magic damage, equal to half a heart. It gives about 45 seconds of [Surge II](effects.md#surge) and 22.5 seconds of [Warp I](effects.md#warp). Repeated uses can raise Warp to level II.

## Production guide

### Basic equipment

![Glass Flask](../src/main/resources/assets/substance/textures/item/flask.png) ![Tray](../src/main/resources/assets/substance/textures/item/tray.png)

- Craft six Glass Flasks with five Glass in a U shape.
- Fill a flask directly from a water source to make a Water Flask. The bulk crafting recipe combines eight Glass Flasks with one Water Bucket to make eight Water Flasks.
- Craft one Tray from a horizontal row of three Iron Ingots. Apprentice toolsmiths also sell Trays for three Emeralds.

### Vanilla-compatible route

| Step | Method and inputs | Output |
|-----:|-------------------|--------|
| 1 | Smelt or blast Bone Meal | ![White Phosphorus](../src/main/resources/assets/substance/textures/item/white_phosphorus.png) White Phosphorus |
| 2 | Smelt or blast White Phosphorus | ![Red Phosphorus](../src/main/resources/assets/substance/textures/item/red_phosphorus.png) Red Phosphorus |
| 3 | Craft Dried Kelp with a Water Flask | ![Iodine Crystals](../src/main/resources/assets/substance/textures/item/iodine.png) Iodine Crystals |
| 4 | Craft four Ephedra Bundles together | Four ![Pseudoephedrine](../src/main/resources/assets/substance/textures/item/pseudoephedrine.png) Pseudoephedrine |
| 5 | Combine Pseudoephedrine, Red Phosphorus, Iodine, a Water Flask, and a Lava Flask | One White Crystal Oil Flask |
| 6 | Combine a Tray with three White Crystal Oil Flasks | ![White Oil Tray](../src/main/resources/assets/substance/textures/item/white_oil_tray.png) White Oil Tray |
| 7 | Place the filled tray, wait 1.5 seconds, then use a pickaxe on it | One item of White Crystals and the empty Tray |

Wandering traders sell Ephedra Seeds, and apprentice farmers sell Ephedra Bundles. Mature Ephedra drops bundles and more seeds.

### Create route

Create provides machine alternatives for several stages:

- Mill or crush an Ephedra Bundle to obtain Pseudoephedrine, with chances for an extra unit and two Ephedra Seeds.
- Wash Dried Kelp to obtain Iodine, with a 25% chance for two extra units.
- Mix Pseudoephedrine, Red Phosphorus, Iodine, and 10 mB of water with heat to produce 10 mB of White Crystal Oil.
- Fill a Tray with 30 mB of White Crystal Oil.
- Press the filled tray to obtain White Crystals.

## Properties

Crystal effects apply only after the complete use action. Releasing early does not consume the item or grant its effects. Repeated uses before an effect expires can extend its duration and raise its level where the item permits it.

The use duration and cooldown are configurable under [smoking and sniffing](configuration.md#smoking-and-sniffing).

[Back to the content index](content.md)
