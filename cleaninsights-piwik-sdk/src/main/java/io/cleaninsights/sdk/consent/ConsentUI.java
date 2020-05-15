package io.cleaninsights.sdk.consent;

import android.app.Activity;
import android.content.Context;

import io.cleaninsights.sdk.piwik.CleanInsightsApplication;

/**
 * Created by n8fr8 on 3/8/17.
 */
public class ConsentUI {


    public void showConsentDialog (Activity context)
    {

        String url = ((CleanInsightsApplication) context.getApplication()).getMeasureUrl();


    }
}
