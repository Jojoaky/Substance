package jojoaky.substance.content.chemical_fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class ChemicalFluidBlock extends LiquidBlock {

    private final float toxicity;

    public ChemicalFluidBlock(FlowingFluid fluid, Properties properties, float toxicity) {
        super(fluid, properties);
        this.toxicity = toxicity;
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (toxicity <= 0) return;

        if (!(entity instanceof LivingEntity livingEntity)) return;

        if (world.getGameTime() % 20 == 0) {
            livingEntity.hurt(world.damageSources().magic(), toxicity/2);
            if (toxicity > 2) livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON , 180, 0));
            if (toxicity > 4) livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 50, 0));
        }
    }
}
