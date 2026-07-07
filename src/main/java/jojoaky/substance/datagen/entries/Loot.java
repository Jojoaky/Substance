package jojoaky.substance.datagen.entries;

import jojoaky.substance.data.generator.datapatch.DatapatchRegistry;
import jojoaky.substance.data.generator.datapatch.def.LootEntryDef;
import jojoaky.substance.register.ModItems;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.stream.Stream;

public class Loot {
    static final ResourceLocation outpostChest = new ResourceLocation("minecraft", "chests/pillager_outpost");
    static final ResourceLocation mansionChest = new ResourceLocation("minecraft", "chests/woodland_mansion");
    static final ResourceLocation pillagerDrop = new ResourceLocation("minecraft", "entities/pillager");
    static final ResourceLocation vindicatorDrop = new ResourceLocation("minecraft", "entities/vindicator");

    static final ResourceLocation[] ALL_VILLAGE_CHESTS = new ResourceLocation[]{
            // Desert
            new ResourceLocation("minecraft", "chests/village/village_desert_house"),
            // Plains
            new ResourceLocation("minecraft", "chests/village/village_plains_house"),
            // Savanna
            new ResourceLocation("minecraft", "chests/village/village_savanna_house"),
            // Snowy
            new ResourceLocation("minecraft", "chests/village/village_snowy_house"),
            // Taiga
            new ResourceLocation("minecraft", "chests/village/village_taiga_house"),
    };

    public static void initialize() {
        DatapatchRegistry.accept(
                LootEntryDef.named("wooden_pipe_outpost", outpostChest)
                        .drops(ModItems.WOODEN_PIPE)
                        .weight(4)
                        .count(1.0f, 1.0f),

                LootEntryDef.named("wooden_pipe_mansion", mansionChest)
                        .drops(ModItems.WOODEN_PIPE)
                        .weight(40)
                        .count(1.0f, 1.0f),

                LootEntryDef.named("wooden_pipe_pillager", pillagerDrop)
                        .drops(ModItems.WOODEN_PIPE)
                        .weight(1)
                        .count(1.0f, 1.0f),

                LootEntryDef.named("wooden_pipe_vindicator", vindicatorDrop)
                        .drops(ModItems.WOODEN_PIPE)
                        .weight(1)
                        .count(1.0f, 1.0f)
        );

        Stream<LootEntryDef> villageBubblePipeLoot = Arrays.stream(ALL_VILLAGE_CHESTS)
                .map(chestLocation -> LootEntryDef.named("bubble_pipe_" + chestLocation.getPath().replace('/', '_'), chestLocation)
                        .drops(ModItems.BUBBLE_PIPE)
                        .weight(2)
                        .count(1.0f, 1.0f)
                );

        DatapatchRegistry.accept(villageBubblePipeLoot);
    }
}