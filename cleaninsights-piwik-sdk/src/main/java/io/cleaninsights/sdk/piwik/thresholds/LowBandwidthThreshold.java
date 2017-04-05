package io.cleaninsights.sdk.piwik.thresholds;

/**
 * Created by n8fr8 on 4/5/17.
 */

public class LowBandwidthThreshold extends BaseThreshold {

    public LowBandwidthThreshold(boolean isRequired) {
        super(isRequired);
    }

    @Override
    public boolean allowMeasurement() {

        //check if on a low bandwidth network, or somehow measure throughput
        return false;
    }
}
