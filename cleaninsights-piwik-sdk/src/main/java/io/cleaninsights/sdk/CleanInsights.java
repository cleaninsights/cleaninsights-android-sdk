package io.cleaninsights.sdk;

import android.content.Context;
import android.content.Intent;

import info.guardianproject.netcipher.proxy.OrbotHelper;
import info.guardianproject.netcipher.proxy.StatusCallback;
import io.cleaninsights.sdk.piwik.DownloadInsight;
import io.cleaninsights.sdk.piwik.CleanInsightsApplication;
import io.cleaninsights.sdk.piwik.MeasureHelper;

import timber.log.Timber;

/**
 * Created by n8fr8 on 3/8/17.
 */
public class CleanInsights {

    public final static String TAG = "CleanInsights";

    private static CleanInsights mInstance = null;

    private boolean mTorEnabled = false;
    private int mTorSocksPort = -1;
    private int mTorHttpPort = -1;

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
        //check if Orbot/Tor is installed, and if so initialize settings
        OrbotHelper orbotHelper = OrbotHelper.get(context);

        orbotHelper.addStatusCallback(new StatusCallback() {
            @Override
            public void onEnabled(Intent intent) {

                mTorEnabled = true;

                String status = intent.getStringExtra(OrbotHelper.EXTRA_STATUS);
                mTorSocksPort = intent.getIntExtra(OrbotHelper.EXTRA_PROXY_PORT_SOCKS,-1);
                mTorHttpPort = intent.getIntExtra(OrbotHelper.EXTRA_PROXY_PORT_HTTP,-1);
            }

            @Override
            public void onStarting() {

            }

            @Override
            public void onStopping() {

            }

            @Override
            public void onDisabled() {
                mTorEnabled = false;
            }

            @Override
            public void onStatusTimeout() {

            }

            @Override
            public void onNotYetInstalled() {

            }
        });

        orbotHelper.init();


    }

    public boolean isTorEnabled ()
    {
        return mTorEnabled;
    }

    public int getTorSocksPort ()
    {
        return mTorSocksPort;
    }

    public int getTorHttpPort ()
    {
        return mTorHttpPort;
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

    public void initPwiki (CleanInsightsApplication app)
    {
        //Then init Piwik

        // Print debug output when working on an app.
        Timber.plant(new Timber.DebugTree());

        // When working on an app we don't want to skew tracking results.
        app.getPiwik().setDryRun(BuildConfig.DEBUG);

        // Track this app install, this will only trigger once per app version.
        // i.e. "http://com.piwik.demo:1/185DECB5CFE28FDB2F45887022D668B4"
        MeasureHelper.track().download().identifier(DownloadInsight.Extra.APK_CHECKSUM).with(app.getMeasurer());
        // Alternative:
        // i.e. "http://com.piwik.demo:1/com.android.vending"
        // getMeasurer().download();
    }

}
