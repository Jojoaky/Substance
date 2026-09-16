package jojoaky.substance.mixin;

import jojoaky.substance.Substance;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public class VillagerMixin {
    private static final TagKey<Item> FARMER_PLANTABLE_SEEDS = TagKey.create(
            Registries.ITEM,
            new ResourceLocation(Substance.MOD_ID, "farmer_plantable_seeds")
    );

    @Inject(method = "wantsToPickUp", at = @At("HEAD"), cancellable = true)
    private void substance$wantsToPickUpFarmerPlantableSeeds(
            ItemStack stack,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Villager villager = (Villager) (Object) this;

        if (villager.getVillagerData().getProfession() == VillagerProfession.FARMER
                && stack.is(FARMER_PLANTABLE_SEEDS)) {
            cir.setReturnValue(villager.getInventory().canAddItem(stack));
        }
    }
}
