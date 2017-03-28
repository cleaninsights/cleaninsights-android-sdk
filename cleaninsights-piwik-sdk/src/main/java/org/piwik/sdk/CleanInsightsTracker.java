package org.piwik.sdk;

import android.support.annotation.NonNull;

import java.net.MalformedURLException;
import java.util.UUID;

import io.cleaninsights.sdk.tools.DeviceHelper;

/**
 * Created by n8fr8 on 3/27/17.
 */

public class CleanInsightsTracker extends Tracker {

    private static final String DEFAULT_UNKNOWN_VALUE = "unknown";
    private static final String DEFAULT_TRUE_VALUE = "1";

    protected CleanInsightsTracker(@NonNull final String url, int siteId, String authToken, String certPin, @NonNull Piwik piwik) throws MalformedURLException {

        super (url, siteId, authToken, certPin, piwik);

        String userId = getSharedPreferences().getString(PREF_KEY_TRACKER_USERID, null);
        getSharedPreferences().edit().putString(PREF_KEY_TRACKER_USERID, userId).apply();
        
        getDefaultTrackMe().set(QueryParams.USER_ID, userId);

        getDefaultTrackMe().set(QueryParams.SESSION_START, DEFAULT_TRUE_VALUE);

        String resolution = DEFAULT_UNKNOWN_VALUE;

        int[] res = DeviceHelper.getResolution(piwik.getContext());
        if (res != null)
            resolution = String.format("%sx%s", res[0], res[1]);
        getDefaultTrackMe().set(QueryParams.SCREEN_RESOLUTION, resolution);

        getDefaultTrackMe().set(QueryParams.USER_AGENT, DeviceHelper.getUserAgent());
        getDefaultTrackMe().set(QueryParams.LANGUAGE, DeviceHelper.getUserLanguage());
        getDefaultTrackMe().set(QueryParams.COUNTRY, DeviceHelper.getUserCountry());
        getDefaultTrackMe().set(QueryParams.VISITOR_ID, makeRandomVisitorId());
    }

}
