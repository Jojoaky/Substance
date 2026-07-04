package jojoaky.substance.register;

import jojoaky.substance.Substance;
import jojoaky.substance.content.effects.VisualMobEffect;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.List;

public class ModEffects {
    public record PotionSet(
            MobEffect effect,
            Potion basePotion,
            Potion longPotion,
            Potion strongPotion,
            int       color
    ) {}

    // Generic - visual: delayed visuals, fainting
    public static final MobEffect HAZE         = registerEffect("haze",         MobEffectCategory.NEUTRAL,    0xffccddff);
    // Generic - visual: slight hallucinations, effects
    public static final MobEffect WARP         = registerEffect("warp",         MobEffectCategory.NEUTRAL,    0xffaa88ff);

    // Alcohol -> visual: - / effect: smoothed / delayed inputs
    public static final MobEffect STAGGER      = registerEffect("stagger",      MobEffectCategory.HARMFUL,    0xffddaa55);
    // Tobacco -> visual: tunnel vision, gray / effect: haste
    public static final MobEffect KEEN         = registerEffect("keen",         MobEffectCategory.BENEFICIAL, 0xffeecc88);
    // Herbs -> visual: warmth, blur / effect: peace
    public static final MobEffect RELAXATION   = registerEffect("relaxation",   MobEffectCategory.BENEFICIAL, 0xffd9c27a);
    // Crystals -> visual: rush / effect: speed?
    public static final MobEffect SURGE        = registerEffect("surge",        MobEffectCategory.BENEFICIAL, 0xffffee44);
    // Shrooms -> visual: fake entities / effect: peace?
    public static final MobEffect HALLUCINATION= registerEffect("hallucination",MobEffectCategory.NEUTRAL,    0xffcc44cc);
    // Shrooms -> visual: dark, horror / effect: ?
    public static final MobEffect DREAD        = registerEffect("dread",        MobEffectCategory.HARMFUL,    0xff223344);

    public static final List<PotionSet> ALL_POTIONS = new ArrayList<>();

    public static final PotionSet HAZE_POTIONS          = registerPotionSet("haze",          HAZE,          600, 1800, 300, 1, Items.FERMENTED_SPIDER_EYE);
    public static final PotionSet WARP_POTIONS          = registerPotionSet("warp",          WARP,          600, 1800, 300, 1, ModItems.CYANIDE);
    public static final PotionSet DREAD_POTIONS         = registerPotionSet("dread",         DREAD,         600, 1800, 300, 1, Items.SCULK);
    public static final PotionSet STAGGER_POTIONS       = registerPotionSet("stagger",       STAGGER,       600, 1800, 300, 1, Items.HONEYCOMB);
    public static final PotionSet KEEN_POTIONS          = registerPotionSet("keen",          KEEN,          600, 1800, 300, 1, ModItems.DRIED_TOBACCO_LEAF);
    public static final PotionSet RELAXATION_POTIONS    = registerPotionSet("relaxation",    RELAXATION,    600, 1800, 300, 1, ModItems.HERB_BUD);
    public static final PotionSet SURGE_POTIONS         = registerPotionSet("surge",         SURGE,         600, 1800, 300, 1, ModItems.WHITE_CRYSTALS);
    public static final PotionSet HALLUCINATION_POTIONS = registerPotionSet("hallucination", HALLUCINATION, 600, 1800, 300, 1, Items.RED_MUSHROOM);

    private static MobEffect registerEffect(String name, MobEffectCategory category, int color) {
        return Registry.registerForHolder(
                BuiltInRegistries.MOB_EFFECT,
                Substance.resource(name),
                new VisualMobEffect(category, color)
        ).value();
    }

    private static PotionSet registerPotionSet(
            String   name,
            MobEffect effect,
            int      baseDuration,
            int      longDuration,
            int      strongDuration,
            int      strongAmplifier,
            Item     baseIngredient
    ) {
        Potion basePotion   = registerPotion(name, name,           effect, baseDuration,   0);
        Potion longPotion   = registerPotion(name, name + "_long", effect, longDuration,   0);
        Potion strongPotion = registerPotion(name, name + "_strong", effect, strongDuration, strongAmplifier);

        PotionBrewing.addMix(Potions.AWKWARD, baseIngredient,   basePotion);
        PotionBrewing.addMix(basePotion, Items.REDSTONE, longPotion);
        PotionBrewing.addMix(basePotion, Items.GLOWSTONE_DUST, strongPotion);

        PotionSet set = new PotionSet(effect, basePotion, longPotion, strongPotion, effect.getColor());
        ALL_POTIONS.add(set);
        return set;
    }

    private static Potion registerPotion(String effectName, String registryName, MobEffect effect, int duration, int amplifier) {
        return Registry.register(
                BuiltInRegistries.POTION,
                Substance.resource(registryName + "_potion"),
                new Potion(effectName, new MobEffectInstance(effect, duration, amplifier))
        );
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.SUSPICIOUS_ITEM_GROUP_KEY)
                .register(entries -> {
                    for (PotionSet set : ALL_POTIONS) {
                        entries.accept(PotionUtils.setPotion(new ItemStack(Items.POTION), set.basePotion));
                    }
                });
    }
}