package jojoaky.substance.generator.trade;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;

public class WanderingTraderTradeDef extends MerchantTradeDef<WanderingTraderTradeDef> {
    private final int pool;

    private WanderingTraderTradeDef(int pool) {
        this.pool = pool;
    }

    public static WanderingTraderTradeDef pool(int pool) {
        return new WanderingTraderTradeDef(pool);
    }

    public int getPool() { return pool; }

    @Override
    public void register() {
        TradeOfferHelper.registerWanderingTraderOffers(getPool(), factories -> {
            factories.add(toItemListing());
        });
    }
}