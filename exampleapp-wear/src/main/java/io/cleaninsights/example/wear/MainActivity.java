package io.cleaninsights.example.wear;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.cleaninsights.sdk.piwik.CleanInsightsApplication;
import io.cleaninsights.sdk.piwik.MeasureHelper;
import io.cleaninsights.sdk.piwik.Measurer;

public class MainActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private Button mVoteDogs, mVoteCats;
    private TextView mClockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mVoteDogs = (Button) findViewById(R.id.btnVoteDog);
        mVoteCats = (Button) findViewById(R.id.btnVoteCat);

        mClockView = (TextView) findViewById(R.id.clock);
    }

    private Measurer getTracker() {
        return ((CleanInsightsApplication) getApplication()).getMeasurer();
    }

    public void voteDogs (View view)
    {
        MeasureHelper.track()
                .screen("/vote/dog/like/1")
                .title("Vote")
                .variable(1, "dog", "1")
                .with(getTracker());
    }

    public void voteCats (View view)
    {
        MeasureHelper.track()
                .screen("/vote/cat/like/1")
                .title("Vote")
                .variable(1, "cat", "1")
                .with(getTracker());
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.VISIBLE);
            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mClockView.setVisibility(View.GONE);
        }
    }
}
