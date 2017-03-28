/*
 * Android SDK for Piwik
 *
 * @link https://github.com/piwik/piwik-android-sdk
 * @license https://github.com/piwik/piwik-sdk-android/blob/master/LICENSE BSD-3 Clause
 */

package io.cleaninsights.demo;

import org.piwik.sdk.DownloadTracker;
import org.piwik.sdk.PiwikApplication;
import org.piwik.sdk.TrackHelper;

import io.cleaninsights.sdk.CIManager;
import timber.log.Timber;

public class DemoApp extends PiwikApplication {

    @Override
    public String getTrackerUrl() {
        return "https://demo.cleaninsights.io";
    }

    @Override
    public String getTrackerUrlCertificatePin ()
    {
        //generate your own using this tool: https://github.com/scottyab/ssl-pin-generator
        return "sha256/ZG+5y3w2mxstotmn15d9tnJtwss591+L6EH/yJMF41I=";
    }

    @Override
    public Integer getSiteId() {
        return 1;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initCleanInsights();
    }

    private void initCleanInsights() {

        //First init CI
        CIManager cim = CIManager.getInstance(this);
        cim.initPwiki(this);

        getTracker().setApplicationDomain("demo.cleaninsights.io");
    }
}
