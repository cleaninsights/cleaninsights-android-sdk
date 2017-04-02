package io.cleaninsights.sdk.piwik;

import android.support.annotation.NonNull;
import io.cleaninsights.sdk.rappor.Encoder;

import java.nio.charset.StandardCharsets;

public class RandomizedMeasureMe extends MeasureMe {
    private static final QueryParams[] RANDOMIZED_PARAMS = new QueryParams[] {
            QueryParams.EVENT_VALUE,
    };
    private static final String ENCODER_ID = "RandomizedMeasureMe";

    public synchronized MeasureMe set(@NonNull QueryParams key, String value) {
        set(key.toString(), value);
        return this;
    }

    public synchronized MeasureMe set(@NonNull QueryParams key, int value) {
        final String stringValue;
        if (requireRandomization(key)) {
            stringValue = new String(createRandomizingEncoder().encodeOrdinal(value), StandardCharsets.ISO_8859_1);
        } else {
            stringValue = Integer.toString(value);
        }
        set(key, stringValue);
        return this;
    }

    public synchronized MeasureMe set(@NonNull QueryParams key, float value) {
        if (requireRandomization(key)) {
            set(key, Math.round(value));
        } else {
            set(key, Float.toString(value));
        }
        return this;
    }

    public synchronized MeasureMe set(@NonNull QueryParams key, long value) {
        if (requireRandomization(key)) {
            set(key, Long.valueOf(value).intValue());
        } else {
            set(key, Long.toString(value));
        }
        return this;
    }

    private Encoder createRandomizingEncoder() {
        // TODO: Choose appropriate parameters
        return new Encoder(getUserSecret(),
                ENCODER_ID,
                4096,
                13.0 / 128.0,
                0.25,
                0.75,
                1,
                2);
    }

    private boolean requireRandomization(@NonNull QueryParams key) {
        for (QueryParams randomizedParam : RANDOMIZED_PARAMS) {
            if (randomizedParam == key) {
                return true;
            }
        }
        return false;
    }

    // TODO: Remember the user secret
    private byte[] getUserSecret() {
        return new byte[32];
    }
}
