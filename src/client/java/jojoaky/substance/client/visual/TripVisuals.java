package jojoaky.substance.client.visual;

import com.mojang.blaze3d.vertex.VertexConsumer;
import jojoaky.substance.Config;
import jojoaky.substance.register.ModEffects;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/** Detached render props: never inserted into the level's entity or block storage. */
public final class TripVisuals {
    private static final RandomSource RANDOM = RandomSource.create();
    private static final List<Apparition> APPARITIONS = new ArrayList<>();
    private static ClientLevel level;
    private static int hallucinationTimer, dreadTimer;
    private static LivingEntity rendering;

    private TripVisuals() {}

    public static boolean isApparition(LivingEntity entity) {
        return rendering == entity;
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(TripVisuals::tick);
        WorldRenderEvents.AFTER_ENTITIES.register(TripVisuals::render);
    }

    private static boolean enabled(Minecraft mc) {
        return mc.level != null && mc.player != null && mc.player.isAlive()
                && Config.get().enableShaderEffects && Config.get().visualEffectStrength > 0;
    }

    private static void tick(Minecraft mc) {
        if (level != mc.level || !enabled(mc)) {
            APPARITIONS.clear();
            level = mc.level;
            hallucinationTimer = 60;
            dreadTimer = 100;
        }
        if (!enabled(mc) || mc.isPaused()) return;
        Config config = Config.get();
        boolean hallucination = config.enableHallucinationVisuals && mc.player.hasEffect(ModEffects.HALLUCINATION);
        boolean dread = config.enableDreadVisuals && mc.player.hasEffect(ModEffects.DREAD);
        APPARITIONS.removeIf(a -> a.dread ? !dread : !hallucination);
        for (Apparition a : APPARITIONS) {
            a.age++;
            if (a.entity != null) {
                Vec3 direction = mc.player.getEyePosition().subtract(a.position);
                float yaw = (float) (Math.atan2(-direction.x, direction.z) * Mth.RAD_TO_DEG);
                a.entity.setYRot(yaw);
                a.entity.yRotO = yaw;
                a.entity.yHeadRot = a.entity.yHeadRotO = yaw;
                a.entity.yBodyRot = a.entity.yBodyRotO = yaw;
                if (a.entity instanceof Creeper && a.age > 15
                        && mc.player.getLookAngle().dot(direction.normalize().scale(-1)) > 0.88
                        && visible(mc, a.position.add(0, 1, 0))) {
                    // Local particles only: no explosion packet, damage, or terrain mutation.
                    level.addParticle(ParticleTypes.EXPLOSION, a.position.x, a.position.y + 0.8,
                            a.position.z, 0, 0, 0);
                    a.age = a.lifetime;
                }
            }
        }
        APPARITIONS.removeIf(a -> a.age >= a.lifetime
                || a.position.distanceToSqr(mc.player.position()) > 160 * 160
                || (a.dread && !(a.entity instanceof Creeper)
                    && a.position.distanceToSqr(mc.player.position()) < Math.pow(
                            Mth.clamp(config.dreadAnimalFadeDistance, 4.0f, 64.0f), 2)));
        if (hallucination && --hallucinationTimer <= 0) {
            hallucinationTimer = nextInterval(config.hallucinationApparitionInterval);
            if (count(false) < Mth.clamp(config.hallucinationMaxApparitions, 0, 20)) spawnHallucination(mc);
        }
        if (dread && --dreadTimer <= 0) {
            dreadTimer = nextInterval(config.dreadApparitionInterval);
            if (count(true) < Mth.clamp(config.dreadMaxApparitions, 0, 20)) spawnDread(mc);
        }
    }

    private static int nextInterval(float seconds) {
        int average = Math.max(1, Math.round(Mth.clamp(seconds, 0.5f, 60.0f) * 20));
        int variance = Math.max(1, average * 2 / 5);
        return average - variance + RANDOM.nextInt(variance * 2 + 1);
    }

    private static long count(boolean dread) {
        return APPARITIONS.stream().filter(a -> a.dread == dread).count();
    }

    private static boolean visible(Minecraft mc, Vec3 target) {
        return level.clip(new ClipContext(mc.player.getEyePosition(), target,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player)).getType() == HitResult.Type.MISS;
    }

    private static void spawnHallucination(Minecraft mc) {
        if (RANDOM.nextFloat() < Mth.clamp(Config.get().hallucinationVillagerChance, 0.0f, 1.0f)) {
            Vec3 p = mc.player.position().add(mc.player.getLookAngle().scale(22))
                    .add(RANDOM.nextInt(13) - 6, 10 + RANDOM.nextInt(8), RANDOM.nextInt(13) - 6);
            addEntity(EntityType.VILLAGER, p, false, 240);
            return;
        }
        // A displaced image of nearby terrain, leaving the real block untouched.
        for (int i = 0; i < 16; i++) {
            BlockPos pos = mc.player.blockPosition().offset(RANDOM.nextInt(15) - 7,
                    RANDOM.nextInt(7) - 3, RANDOM.nextInt(15) - 7);
            if (!level.hasChunkAt(pos)) continue;
            BlockState state = level.getBlockState(pos);
            if (state.getRenderShape() != RenderShape.MODEL || state.hasBlockEntity()
                    || !state.isSolidRender(level, pos) || !level.getBlockState(pos.above()).isAir()) continue;
            APPARITIONS.add(new Apparition(null, state, Vec3.atLowerCornerOf(pos), false, 100));
            break;
        }
    }

    private static void spawnDread(Minecraft mc) {
        Config config = Config.get();
        if (RANDOM.nextFloat() < Mth.clamp(config.dreadCreeperChance, 0.0f, 1.0f)) {
            Vec3 behind = mc.player.getLookAngle().multiply(1, 0, 1).normalize().scale(-5);
            BlockPos ground = groundAt(mc.player.position().add(behind));
            if (ground != null && Math.abs(ground.getY() - mc.player.getY()) < 4)
                addEntity(EntityType.CREEPER, Vec3.atBottomCenterOf(ground), true, 180);
            return;
        }
        double angle = RANDOM.nextDouble() * Math.PI * 2;
        float distance = Mth.clamp(config.dreadAnimalDistance, 32.0f, 128.0f);
        Vec3 center = mc.player.position().add(Math.sin(angle) * distance, 0, Math.cos(angle) * distance);
        EntityType<?>[] animals = {EntityType.COW, EntityType.PIG, EntityType.CHICKEN};
        EntityType<?> type = animals[RANDOM.nextInt(animals.length)];
        for (int i = 0, count = 1 + RANDOM.nextInt(3); i < count; i++) {
            BlockPos ground = groundAt(center.add(i * 2.5, 0, RANDOM.nextDouble() * 3));
            if (ground != null) addEntity(type, Vec3.atBottomCenterOf(ground), true, 600);
        }
    }

    private static BlockPos groundAt(Vec3 p) {
        BlockPos column = BlockPos.containing(p);
        if (!level.hasChunkAt(column)) return null;
        // NO_LEAVES is not synchronized to clients and starts empty in client chunks.
        BlockPos ground = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, column);
        if (ground.getY() <= level.getMinBuildHeight() || !level.getFluidState(ground.below()).isEmpty()
                || !level.getBlockState(ground).isAir() || !level.getBlockState(ground.above()).isAir()) return null;
        return ground;
    }

    private static void addEntity(EntityType<?> type, Vec3 p, boolean dread, int lifetime) {
        if (!level.hasChunkAt(BlockPos.containing(p))) return;
        if (!(type.create(level) instanceof LivingEntity entity)) return;
        entity.setPos(p);
        entity.xo = p.x;
        entity.yo = p.y;
        entity.zo = p.z;
        APPARITIONS.add(new Apparition(entity, null, p, dread, lifetime));
    }

    private static void render(WorldRenderContext context) {
        Minecraft mc = Minecraft.getInstance();
        if (!enabled(mc) || level != mc.level || context.consumers() == null) return;
        Vec3 camera = context.camera().getPosition();
        for (Apparition a : APPARITIONS) {
            Config config = Config.get();
            if (a.dread ? (!config.enableDreadVisuals || !mc.player.hasEffect(ModEffects.DREAD))
                    : (!config.enableHallucinationVisuals || !mc.player.hasEffect(ModEffects.HALLUCINATION))) continue;
            float age = a.age + context.tickDelta();
            float fade = Mth.clamp(Math.min(age / 20, (a.lifetime - age) / 30), 0, 1);
            if (a.dread && !(a.entity instanceof Creeper))
                fade *= Mth.clamp((float) (a.position.distanceTo(mc.player.position())
                        - Mth.clamp(config.dreadAnimalFadeDistance, 4.0f, 64.0f)) / 40, 0, 1);
            float effectStrength = a.dread ? config.dreadVisualStrength : config.hallucinationVisualStrength;
            float alpha = Mth.clamp(fade * Math.min(1, config.visualEffectStrength)
                    * Mth.clamp(effectStrength, 0.0f, 2.0f) * 0.85f, 0.0f, 1.0f);
            if (alpha <= 0) continue;
            var pose = context.matrixStack();
            pose.pushPose();
            try {
                Vec3 p = a.position.subtract(camera);
                pose.translate(p.x, p.y, p.z);
                if (a.entity != null) {
                    if (!a.dread) pose.translate(0, Math.sin(age * 0.035) * 0.8, 0);
                    rendering = a.entity;
                    MultiBufferSource faded = type -> new AlphaConsumer(context.consumers().getBuffer(type), alpha);
                    // Call the renderer directly to avoid real-entity shadows, hitboxes and fire.
                    mc.getEntityRenderDispatcher().getRenderer(a.entity).render(a.entity,
                            a.entity.getYRot(), context.tickDelta(), pose, faded,
                            mc.getEntityRenderDispatcher().getPackedLightCoords(a.entity, context.tickDelta()));
                } else {
                    float motion = mc.options.screenEffectScale().get().floatValue();
                    pose.translate((0.18 + Math.sin(age * 0.05) * 0.06) * motion,
                            (0.14 + Math.sin(age * 0.03) * 0.05) * motion, 0.07 * motion);
                    MultiBufferSource blockImage = type -> new AlphaConsumer(context.consumers().getBuffer(
                            net.minecraft.client.renderer.RenderType.entityTranslucent(
                                    net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS)), alpha * 1.0f);
                    if (motion > 0) {
                        BlockPos sourcePos = BlockPos.containing(a.position);
                        // The source itself is opaque, so its stored light is usually zero. Sample the
                        // exposed air above it just as the visible surface is lit in the world.
                        int packedLight = net.minecraft.client.renderer.LevelRenderer.getLightColor(
                                level, sourcePos.above());
                        mc.getBlockRenderer().renderSingleBlock(a.block, pose, blockImage,
                                packedLight, OverlayTexture.NO_OVERLAY);
                    }
                }
            } finally {
                rendering = null;
                pose.popPose();
            }
        }
    }

    private static final class Apparition {
        final LivingEntity entity;
        final BlockState block;
        final Vec3 position;
        final boolean dread;
        final int lifetime;
        int age;

        Apparition(LivingEntity entity, BlockState block, Vec3 position, boolean dread, int lifetime) {
            this.entity = entity;
            this.block = block;
            this.position = position;
            this.dread = dread;
            this.lifetime = lifetime;
        }
    }

    private record AlphaConsumer(VertexConsumer delegate, float alpha) implements VertexConsumer {
        public VertexConsumer vertex(double x, double y, double z) { delegate.vertex(x, y, z); return this; }
        public VertexConsumer color(int r, int g, int b, int a) { delegate.color(r, g, b, (int) (a * alpha)); return this; }
        public VertexConsumer uv(float u, float v) { delegate.uv(u, v); return this; }
        public VertexConsumer overlayCoords(int u, int v) { delegate.overlayCoords(u, v); return this; }
        public VertexConsumer uv2(int u, int v) { delegate.uv2(u, v); return this; }
        public VertexConsumer normal(float x, float y, float z) { delegate.normal(x, y, z); return this; }
        public void endVertex() { delegate.endVertex(); }
        public void defaultColor(int r, int g, int b, int a) { delegate.defaultColor(r, g, b, (int) (a * alpha)); }
        public void unsetDefaultColor() { delegate.unsetDefaultColor(); }
    }
}
