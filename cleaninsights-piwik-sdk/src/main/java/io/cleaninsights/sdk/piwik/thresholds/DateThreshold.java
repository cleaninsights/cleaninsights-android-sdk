package io.cleaninsights.sdk.piwik.thresholds;

import java.util.Date;

/**
 * Created by n8fr8 on 4/5/17.
 */

public class DateThreshold extends BaseThreshold {

    private Date mStartTime;
    private Date mEndTime;

    public DateThreshold(boolean isRequired, Date startTime, Date endTime) {
        super(isRequired);

        mStartTime = startTime;
        mEndTime = endTime;
    }

    @Override
    public boolean allowMeasurement() {
        Date now = new Date();
        return mStartTime.before(now) && mEndTime.after(now);
    }

    @Override
    public boolean isRequired() {
        return super.isRequired();
    }
}
