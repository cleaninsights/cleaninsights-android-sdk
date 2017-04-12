/*
 * Android SDK for Piwik
 *
 * @link https://github.com/piwik/piwik-android-sdk
 * @license https://github.com/piwik/piwik-sdk-android/blob/master/LICENSE BSD-3 Clause
 */

package io.cleaninsights.demo;

import info.guardianproject.netcipher.proxy.OrbotHelper;
import io.cleaninsights.sdk.piwik.CleanInsightsApplication;

import io.cleaninsights.sdk.CleanInsights;

public class DemoApp extends CleanInsightsApplication {

    @Override
    public String getMeasureUrl() {
        return "https://demo.cleaninsights.io";
    }

    @Override
    public String getMeasureUrlCertificatePin()
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
        CleanInsights cim = CleanInsights.getInstance(this);
        cim.initPwiki(this);

        getMeasurer().setApplicationDomain("demo.cleaninsights.io");


    }
}
