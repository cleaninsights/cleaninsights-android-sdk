package io.cleaninsights.sdk;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

import info.guardianproject.netcipher.client.StrongBuilder;
import info.guardianproject.netcipher.client.StrongConnectionBuilder;
import info.guardianproject.netcipher.proxy.OrbotHelper;

/**
 * Created by n8fr8 on 3/8/17.
 */
public class CIManager {

    public final static String TAG = "CleanInsights";

    public void init (Context context)
    {
        initNetCipher (context);
    }

    private void initNetCipher (Context context)
    {
        OrbotHelper.get(context).init();


    }

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
    }

}
