package io.cleaninsights.sdk;

import android.content.Context;

import io.cleaninsights.sdk.piwik.DownloadTracker;
import io.cleaninsights.sdk.piwik.PiwikApplication;
import io.cleaninsights.sdk.piwik.TrackHelper;

import timber.log.Timber;

/**
 * Created by n8fr8 on 3/8/17.
 */
public class InsightManager {

    public final static String TAG = "CleanInsights";

    private static InsightManager mInstance = null;

    private InsightManager(Context context)
    {
        init(context);
    }

    public synchronized static InsightManager getInstance (Context context)
    {
        if (mInstance == null)
        {
            mInstance = new InsightManager(context);
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
        // Changing it later will track new events as belonging to a different user.
        // String userEmail = ....preferences....getString

        // getTracker().setApplicationDomain();
        app.getTracker().setVisitCustomVariable(0, "foo","blat");
        app.getTracker().setVisitCustomVariable(1, "bar","blat");
        app.getTracker().setUserId("anonymous");


        // Track this app install, this will only trigger once per app version.
        // i.e. "http://com.piwik.demo:1/185DECB5CFE28FDB2F45887022D668B4"
        TrackHelper.track().download().identifier(DownloadTracker.Extra.APK_CHECKSUM).with(app.getTracker());
        // Alternative:
        // i.e. "http://com.piwik.demo:1/com.android.vending"
        // getTracker().download();
    }

}
