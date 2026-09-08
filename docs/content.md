# Substance content guide

The creative tab (`Substance`) is useful for discovering content,
but normal survival progression is built around the recipes, loot injections, villager trades, and
optional Create processing described below.

## Consumables and effects

The consumable and smoking items are:

| Item                                                                                                                          | ID                               | Effects                                                                       |
|-------------------------------------------------------------------------------------------------------------------------------|----------------------------------|-------------------------------------------------------------------------------|
| ![White Crystals](../src/main/resources/assets/substance/textures/item/white_crystals.png) White Crystals                     | `substance:white_crystals`       | Surge and Warp.                                                               |
| ![Spiced White Crystals](../src/main/resources/assets/substance/textures/item/white_crystals_chili.png) Spiced White Crystals | `substance:white_crystals_chili` | Surge and Warp.                                                               |
| ![Blue Crystals](../src/main/resources/assets/substance/textures/item/blue_crystals.png) Blue Crystals                        | `substance:blue_crystals`        | Surge and Warp.                                                               |
| ![Herbal Roll](../src/main/resources/assets/substance/textures/item/herbal_roll.png) Herbal Roll                              | `substance:herbal_roll`          | Relaxation and Warp. A long use also causes Confusion.                        |
| ![Thick Herbal Roll](../src/main/resources/assets/substance/textures/item/thick_herbal_roll.png) Thick Herbal Roll            | `substance:thick_herbal_roll`    | Stronger Relaxation and Warp, plus Haze. A long use also causes Confusion.    |
| ![Cigarette](../src/main/resources/assets/substance/textures/item/cigarette.png) Cigarette                                    | `substance:cigarette`            | Keen and Haste. A long use also causes Confusion.                             |
| ![Wooden Pipe](../src/main/resources/assets/substance/textures/item/wooden_pipe.png) Wooden Pipe                              | `substance:wooden_pipe`          | Reusable pipe; smoking gives Haze and Warp, plus the effects of its contents. |
| ![Bubble Pipe](../src/main/resources/assets/substance/textures/item/bubble_pipe.png) Bubble Pipe                              | `substance:bubble_pipe`          | Reusable pipe; smoking gives Haze and Warp, plus the effects of its contents. |

Using a consumable can apply one or more Substance effects. The effect names and their broad gameplay themes are:

- **Haze**: visual disorientation and delayed visual effects.
- **Warp**: visual distortion and altered perception.
- **Keen**: a beneficial mining/haste-style effect; its mining multiplier is configurable.
- **Relaxation**: a beneficial calming effect with defensive behavior and configurable darkness duration.
- **Surge**: a movement and Elytra boost; speed and flight parameters are configurable.
- **Hallucination**: client-side apparitions, displaced block images, and ambient sounds.
- **Dread**: a harmful horror effect with darkness, false Creepers, animal apparitions, and ambient sounds.

Potions are registered for every active effect. For each effect there is a base, long, and strong potion. The brewing pattern is:

1. Brew an awkward potion with the effect ingredient.
2. Add redstone to make the long version.
3. Add glowstone dust to make the strong version.

The effect ingredients are fermented spider eye (Haze), cyanide (Warp), sculk (Dread), dried tobacco leaf (Keen), herb bud (Relaxation), white crystals (Surge), and red mushroom (Hallucination).

## Plants and farming

Substance adds four plant lines:

- **Herb**: plant `substance:herb_seeds` to grow the large herb crop, harvest `herb_bud`, and dry the bud before using it in herbal rolls.
- **Ephedra**: plant `substance:ephedra_seeds` to grow ephedra and obtain ephedra bundles for the chemistry chain.
- **Chili**: buy `substance:chili_seeds` from a wandering trader, grow chili peppers, and combine a pepper with white crystals to craft spiced white crystals.
- **Tobacco**: plant `substance:tobacco_seeds`, harvest ripe tobacco leaves, and dry them to obtain dried tobacco leaves for cigarettes and Keen brewing.

Crop drops and growth are data-defined in the `substance` loot tables. The crop blocks are intentionally lightweight and can be harvested like other vanilla crops.

## Chemistry and fluids

The chemistry system adds the following fluids, each with a world fluid, bucket, and flask representation:

- Phenylacetic acid
- Acetic anhydride
- Methanol
- Methylamine
- Phenylacetone
- Ammonia
- White crystal oil
- Blue crystal oil

The corresponding IDs use the lower-case names, for example `substance:methanol`, `substance:methanol_bucket`, and `substance:methanol_flask`. Empty flasks can be filled and returned by fluid interactions; filled gas bottles similarly return an empty bottle when consumed by a recipe.

Gas bottles are available as empty, oxygen, hydrogen, and nitrogen variants. The generated recipes describe how to obtain the gases from Create-compatible processing chains and vanilla inputs.

## Trays and crystal processing

The basic `substance:tray` is used to process crystal oils. Filled trays are available for white and blue crystal oil (`white_oil_tray` and `blue_oil_tray`). Filled trays can break during processing; the shatter loot tables control the resulting drops. The tray and filled trays are also included in the Substance creative tab.

## Mob equipment

Some hostile and neutral mobs can spawn holding a smoking item. The built-in definitions cover the Illagers, Piglins, Skeletons, and Zombies entity tags. The definitions are loaded from `data/substance/mob_equipment/*.json`, so a datapack can add or replace definitions without changing Java code. See [custom-data.md](custom-data.md) for the schema.

## Loot, trades, and compatibility

The mod adds selected items to vanilla loot tables and villager/wandering-trader pools through its datapatch registry. Create integration supplies alternate processing recipes and replacement recipes when Create is loaded. Without Create, the vanilla recipe path remains available where one exists.

## Advancements

The advancement tree is grouped by pipes, herbs, tobacco, crystals, and miscellaneous chemistry. It is intended to provide discovery goals rather than gate the entire progression. If an advancement appears missing, verify that the resource pack and the `data/substance/advancements` files are present in the installed JAR.
