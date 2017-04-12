package io.cleaninsights.example.television;

import io.cleaninsights.sdk.CleanInsights;
import io.cleaninsights.sdk.piwik.CleanInsightsApplication;

/**
 * Created by n8fr8 on 4/12/17.
 */

public class TelevisionApp extends CleanInsightsApplication {

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
