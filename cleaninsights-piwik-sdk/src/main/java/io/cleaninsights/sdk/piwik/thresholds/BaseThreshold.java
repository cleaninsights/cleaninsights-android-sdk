package io.cleaninsights.sdk.piwik.thresholds;

/**
 * Created by n8fr8 on 4/1/17.
 */

public abstract class BaseThreshold {

    private boolean mIsRequired = false;

    public BaseThreshold (boolean isRequired)
    {
        mIsRequired = isRequired;
    }

    public abstract boolean allowMeasurement ();

    public boolean isRequired ()
    {
        return mIsRequired;
    }
}
