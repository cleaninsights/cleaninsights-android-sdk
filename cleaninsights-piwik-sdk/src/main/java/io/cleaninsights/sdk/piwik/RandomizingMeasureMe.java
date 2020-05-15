package io.cleaninsights.sdk.piwik;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.common.io.BaseEncoding;
import io.cleaninsights.sdk.rappor.Encoder;

public class RandomizingMeasureMe extends MeasureMe {
    private static final String ENCODER_ID = "RandomizingMeasureMe";
    private static final String EVENT_ACTION_PREFIX = "randomized/";

    private final Measurer mMeasurer;

    public RandomizingMeasureMe(final MeasureMe trackMe, final Measurer measurer) {
        super(trackMe);
        mMeasurer = measurer;
    }

    public synchronized MeasureMe set(@NonNull QueryParams key, String value) {
        if (key == QueryParams.EVENT_ACTION) {
            value = EVENT_ACTION_PREFIX + value;
        }
        set(key.toString(), value);
        return this;
    }

    public synchronized MeasureMe set(@NonNull QueryParams key, int value) {
        final String stringValue;
        if (key == QueryParams.EVENT_VALUE) {
            key = QueryParams.EVENT_NAME;
            stringValue = BaseEncoding.base64().encode(createRandomizingEncoder().encodeOrdinal(value));
        } else {
            stringValue = Integer.toString(value);
        }
        set(key, stringValue);
        return this;
    }

    public synchronized MeasureMe set(@NonNull QueryParams key, float value) {
        if (key == QueryParams.EVENT_VALUE) {
            set(key, Math.round(value));
        } else {
            set(key, Float.toString(value));
        }
        return this;
    }

    public synchronized MeasureMe set(@NonNull QueryParams key, long value) {
        if (key == QueryParams.EVENT_VALUE) {
            set(key, Long.valueOf(value).intValue());
        } else {
            set(key, Long.toString(value));
        }
        return this;
    }

    private Encoder createRandomizingEncoder() {
        // TODO: Choose appropriate parameters
        return new Encoder(mMeasurer.getUserSecret(),
                ENCODER_ID,
                4096,
                13.0 / 128.0,
                0.25,
                0.75,
                1,
                2);
    }
}
