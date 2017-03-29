package io.cleaninsights.sdk;

import android.content.Context;

import io.cleaninsights.sdk.piwik.DownloadInsight;
import io.cleaninsights.sdk.piwik.PiwikApplication;
import io.cleaninsights.sdk.piwik.MeasureHelper;

import timber.log.Timber;

/**
 * Created by n8fr8 on 3/8/17.
 */
public class CleanInsights {

    public final static String TAG = "CleanInsights";

    private static CleanInsights mInstance = null;

    private CleanInsights(Context context)
    {
        init(context);
    }

    public synchronized static CleanInsights getInstance (Context context)
    {
        if (mInstance == null)
        {
            mInstance = new CleanInsights(context);
        }

        return mInstance;
    }

    public void init (Context context)
    {
        initNetCipher (context);
    }

    private void initNetCipher (Context context)
    {
       // OrbotHelper.get(context).init();

    }

    /**
    public static void getStrongBuilder (Context context, URL url, StrongBuilder.Callback callback)
    {
        try {
            StrongConnectionBuilder
                    .forMaxSecurity(context)
                    .connectTo(url)
                    .build(callback);
        }
        catch (Exception e) {
            Log.e(TAG,
                    "Exception loading SO questions", e);

        }
    }**/

    public void initPwiki (PiwikApplication app)
    {
        //Then init Piwik

        // Print debug output when working on an app.
        Timber.plant(new Timber.DebugTree());

        // When working on an app we don't want to skew tracking results.
        app.getPiwik().setDryRun(BuildConfig.DEBUG);

        // If you want to set a specific userID other than the random UUID token, do it NOW to ensure all future actions use that token.
        // Changing it later will measure new events as belonging to a different user.
        // String userEmail = ....preferences....getString

        // getMeasurer().setApplicationDomain();
        app.getMeasurer().setVisitCustomVariable(0, "foo","blat");
        app.getMeasurer().setVisitCustomVariable(1, "bar","blat");
        app.getMeasurer().setUserId("anonymous");


        // Track this app install, this will only trigger once per app version.
        // i.e. "http://com.piwik.demo:1/185DECB5CFE28FDB2F45887022D668B4"
        MeasureHelper.track().download().identifier(DownloadInsight.Extra.APK_CHECKSUM).with(app.getMeasurer());
        // Alternative:
        // i.e. "http://com.piwik.demo:1/com.android.vending"
        // getMeasurer().download();
    }

}
