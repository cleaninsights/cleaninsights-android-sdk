package io.cleaninsights.sdk.consent;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.tapadoo.alerter.Alerter;

import io.cleaninsights.sdk.piwik.CleanInsightsApplication;

/**
 * Created by n8fr8 on 3/8/17.
 */
public class ConsentUI {


    public void showConsentDialog (Activity context, View.OnClickListener listener)
    {

        String url = ((CleanInsightsApplication) context.getApplication()).getMeasureUrl();

        Alerter.create(context)
            .setText("Help us, help you! Tap to enable us to improve your user experience!")
                .setOnClickListener(listener)
            .show();

    }
}
