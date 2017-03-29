/*
 * Android SDK for Piwik
 *
 * @link https://github.com/piwik/piwik-android-sdk
 * @license https://github.com/piwik/piwik-sdk-android/blob/master/LICENSE BSD-3 Clause
 */

package io.cleaninsights.demo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import io.cleaninsights.sdk.piwik.PiwikApplication;
import io.cleaninsights.sdk.piwik.MeasureHelper;

import timber.log.Timber;

public class SettingsActivity extends Activity {

    private void refreshUI(final Activity settingsActivity) {
        // auto measure button
        Button button = (Button) findViewById(R.id.bindtoapp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeasureHelper.track().screens(getApplication()).with(((PiwikApplication) getApplication()).getMeasurer());
            }
        });

        // Dry run
        CheckBox dryRun = (CheckBox) findViewById(R.id.dryRunCheckbox);
        dryRun.setChecked(((PiwikApplication) getApplication()).getPiwik().isDryRun());
        dryRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PiwikApplication) getApplication()).getPiwik().setDryRun(((CheckBox) v).isChecked());
            }
        });

        // out out
        CheckBox optOut = (CheckBox) findViewById(R.id.optOutCheckbox);
        optOut.setChecked(((PiwikApplication) getApplication()).getPiwik().isOptOut());
        optOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PiwikApplication) getApplication()).getPiwik().setOptOut(((CheckBox) v).isChecked());
            }
        });

        // dispatch interval
        EditText input = (EditText) findViewById(R.id.dispatchIntervallInput);
        input.setText(Long.toString(
                ((PiwikApplication) getApplication()).getMeasurer().getDispatchInterval()
        ));
        input.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        try {
                            int interval = Integer.valueOf(charSequence.toString().trim());
                            ((PiwikApplication) getApplication()).getMeasurer()
                                    .setDispatchInterval(interval);
                        } catch (NumberFormatException e) {
                            Timber.d("not a number: %s", charSequence.toString());
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                }

        );

        //session Timeout Input
        input = (EditText) findViewById(R.id.sessionTimeoutInput);
        input.setText(Long.toString(
                (((PiwikApplication) getApplication()).getMeasurer().getSessionTimeout() / 60000)
        ));
        input.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        try {
                            int timeoutMin = Integer.valueOf(charSequence.toString().trim());
                            timeoutMin = Math.abs(timeoutMin);
                            ((PiwikApplication) getApplication()).getMeasurer()
                                    .setSessionTimeout(timeoutMin * 60);
                        } catch (NumberFormatException e) {
                            ((EditText) settingsActivity.findViewById(R.id.sessionTimeoutInput)).setText("30");
                            Timber.d("not a number: %s", charSequence.toString());
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                }

        );

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        refreshUI(this);
    }

}
