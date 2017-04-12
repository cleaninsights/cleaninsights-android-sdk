package io.cleaninsights.example.wear;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
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
    private TextView mClockView;

    private SensorManager mSensorManager;

    private int mFeelHealthy = 0;
    private int mFeelSick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);

        mClockView = (TextView) findViewById(R.id.clock);

        if (askForPermission(Manifest.permission.BODY_SENSORS,1))
            initSensors();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //permission granted I assume?
        initSensors ();

    }


    private boolean askForPermission(String permission, Integer requestCode) {

        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                requestPermissions(new String[]{permission}, requestCode);

            } else {
                requestPermissions(new String[]{permission}, requestCode);
            }

            return false;
        }

        return true;
    }


    private void initSensors ()
    {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor heartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        if (heartRateSensor != null) {
            mSensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {

                    //sensorEvent.sensor.getType(), sensorEvent.accuracy, sensorEvent.timestamp, sensorEvent.values;

                    float[] heartRate = sensorEvent.values;

                    MeasureHelper.track().privateEvent("Feels", "heartRate", heartRate[0], getTracker())
                            .with(getTracker());

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            }, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        Sensor stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor != null) {
            mSensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {

                    //sensorEvent.sensor.getType(), sensorEvent.accuracy, sensorEvent.timestamp, sensorEvent.values;

                    float[] steps = sensorEvent.values;

                    MeasureHelper.track().privateEvent("Feels", "steps", steps[0], getTracker())
                            .with(getTracker());
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            }, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    private Measurer getTracker() {
        return ((CleanInsightsApplication) getApplication()).getMeasurer();
    }

    public void tappedHealthy (View view)
    {
        mFeelHealthy++;
    }

    public void tappedSick (View view)
    {
        mFeelSick++;
    }

    @Override
    protected void onPause() {
        super.onPause();

        //when the app pauses do a private, randomized-response based tracking of the number of likes
        MeasureHelper.track().privateEvent("Feels", "Healthy per Session", Integer.valueOf(mFeelHealthy).floatValue(), getTracker())
                .with(getTracker());

        MeasureHelper.track().privateEvent("Feels", "Sick per Session", Integer.valueOf(mFeelSick).floatValue(), getTracker())
                .with(getTracker());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //dispatch the current set of events to the server
        ((CleanInsightsApplication)getApplication()).getMeasurer().dispatch();
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
