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
                .setTitleText("Here's a message!")
                .setContentText("It's pretty, isn't it?")
                .show();
    }
}
