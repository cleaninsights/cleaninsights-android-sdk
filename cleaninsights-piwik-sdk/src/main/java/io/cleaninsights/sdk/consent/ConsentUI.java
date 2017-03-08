package io.cleaninsights.sdk.consent;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by n8fr8 on 3/8/17.
 */
public class ConsentUI {


    public void showConsentDialog (Context context)
    {
        new SweetAlertDialog(context)
                .setTitleText("Clean Insights")
                .setContentText("Are you ready to get measured up?")
                .show();
    }
}
