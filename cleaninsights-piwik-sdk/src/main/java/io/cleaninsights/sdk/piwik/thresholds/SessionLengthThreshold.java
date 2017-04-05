package io.cleaninsights.sdk.piwik.thresholds;

import java.util.Date;

/**
 * Created by n8fr8 on 4/5/17.
 */

public class SessionLengthThreshold extends BaseThreshold {

    private Date mStartSessionTime;
    private long mLengthMS;

    public SessionLengthThreshold(boolean isRequired, long lengthSeconds) {
        super(isRequired);

        mStartSessionTime = new Date();
        mLengthMS = lengthSeconds * 1000;
    }

    @Override
    public boolean allowMeasurement() {
        Date now = new Date();
        long sessionLength = now.getTime() - mStartSessionTime.getTime();
        return sessionLength > (mLengthMS);
    }

}
