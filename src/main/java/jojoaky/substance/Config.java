package jojoaky.substance;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;

public class Config {
    public static ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(Substance.resource("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("substance.json"))
                    .build())
            .build();

    @SerialEntry
    public boolean myCoolBoolean = true;

    @SerialEntry
    public int myCoolInteger = 5;

    @SerialEntry(comment = "This string is amazing")
    public String myCoolString = "How amazing!";
}
