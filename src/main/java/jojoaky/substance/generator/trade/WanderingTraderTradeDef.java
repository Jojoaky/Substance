package jojoaky.substance.generator.trade;

public class WanderingTraderTradeDef extends MerchantTradeDef<WanderingTraderTradeDef> {
    private final int pool;

    private WanderingTraderTradeDef(int pool) {
        this.pool = pool;
    }

    public static WanderingTraderTradeDef named(String name, int pool) {
        return new WanderingTraderTradeDef(pool).named(name);
    }

    public static WanderingTraderTradeDef pool(int pool) {
        return new WanderingTraderTradeDef(pool);
    }

    public int getPool() { return pool; }
}