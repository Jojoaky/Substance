package jojoaky.substance.generator.trade;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerProfession;

public class VillagerTradeDef extends MerchantTradeDef<VillagerTradeDef> {
    private final VillagerProfession profession;
    private final int level;

    private VillagerTradeDef(VillagerProfession profession, int level) {
        this.profession = profession;
        this.level = level;
    }

    public static VillagerTradeDef profession(VillagerProfession profession, int level) {
        return new VillagerTradeDef(profession, level);
    }

    public VillagerProfession getProfession() { return profession; }
    public int getLevel() { return level; }

    @Override
    public void register() {
        TradeOfferHelper.registerVillagerOffers(getProfession(), getLevel(), factories -> {
            factories.add(toItemListing());
        });
    }
}