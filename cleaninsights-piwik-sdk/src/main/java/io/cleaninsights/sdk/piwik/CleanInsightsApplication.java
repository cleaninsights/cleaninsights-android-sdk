/*
 * Android SDK for Piwik
 *
 * @link https://github.com/piwik/piwik-android-sdk
 * @license https://github.com/piwik/piwik-sdk-android/blob/master/LICENSE BSD-3 Clause
 */

package io.cleaninsights.sdk.piwik;

import android.app.Application;
import android.os.Build;

import java.net.MalformedURLException;

public abstract class CleanInsightsApplication extends Application {

    private Measurer mMeasurer;

    public Piwik getPiwik() {
        return Piwik.getInstance(this);
    }

    /**
     * Gives you an all purpose thread-safe persisted Tracker object.
     *
     * @return a shared Tracker
     */
    public synchronized Measurer getMeasurer() {
        if (mMeasurer == null) {
            try {
                mMeasurer = getPiwik().newMeasurer(getMeasureUrl(), getSiteId(), null, getMeasureUrlCertificatePin());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Tracker URL was malformed.");
            }
        }
        return mMeasurer;
    }

    /**
     * The URL of your remote Piwik server.
     */
    public abstract String getMeasureUrl();

    /**
     * The siteID you specified for this application in Piwik.
     */
    public abstract Integer getSiteId();

    /**
     * The certificate pin of the remote Pwiki server
     */
    public String getMeasureUrlCertificatePin()
    {
        return null; //not required
    }


    /**
     * // we don't need these anymore since we are using a disk based FIFO for queuing 
    @Override
    public void onLowMemory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH && mMeasurer != null) {
            mMeasurer.dispatch();
        }
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        if ((level == TRIM_MEMORY_UI_HIDDEN || level == TRIM_MEMORY_COMPLETE) && mMeasurer != null) {
            mMeasurer.dispatch();
        }
        super.onTrimMemory(level);
    }
    **/
}
