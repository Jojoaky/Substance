package jojoaky.substance.config;

import io.netty.buffer.Unpooled;
import jojoaky.substance.Config;
import net.minecraft.network.FriendlyByteBuf;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameplayConfigTest {
    private static final GameplayConfig DISTINCT_VALUES = new GameplayConfig(
            101, 102, 103, 104, 105,
            1.25f, 2.5f, 3.75f, 4.125f, 0.625f,
            106, 0.875f, 1.125f, 0.03125f, 2.25f, 0.375f, 7.5f, 107
    );

    @Test
    void networkCodecRoundTripsEveryGameplayOption() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        DISTINCT_VALUES.write(buffer);

        assertEquals(DISTINCT_VALUES, GameplayConfig.read(buffer));
        assertEquals(0, buffer.readableBytes(), "codec should consume the complete payload");
    }

    @Test
    void snapshotCopiesEveryGameplayOption() {
        assertEquals(DISTINCT_VALUES, GameplayConfig.from(DISTINCT_VALUES));
    }

    @Test
    void payloadCoversEveryMarkedGameplayField() {
        Set<String> configFields = Arrays.stream(Config.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(GameplayOption.class))
                .map(Field::getName)
                .collect(Collectors.toSet());
        Set<String> payloadFields = Arrays.stream(GameplayConfig.class.getRecordComponents())
                .map(RecordComponent::getName)
                .collect(Collectors.toSet());

        assertEquals(configFields, payloadFields);
    }
}
