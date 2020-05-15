package io.cleaninsights.demo;

import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.cleaninsights.sdk.piwik.CleanInsightsApplication;
import io.cleaninsights.sdk.piwik.Measurer;
import io.cleaninsights.sdk.piwik.thresholds.BaseThreshold;
import io.cleaninsights.sdk.piwik.thresholds.DateThreshold;
import io.cleaninsights.sdk.piwik.thresholds.SessionLengthThreshold;

public class ThresholdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();

        //only measure when the user is in the app for longer than 60 seconds
        getMeasurer().addThreshold(new SessionLengthThreshold(true, 60));

        try {
            //only measure between the specified dates and/or times
            Date startDate = SimpleDateFormat.getDateInstance().parse("4/20/2017");
            Date endDate = SimpleDateFormat.getDateInstance().parse("4/21/2017");

            //measure when between these dates, but DON'T require if another threshold matches
            getMeasurer().addThreshold(new DateThreshold(false, startDate, endDate));
        }
        catch (ParseException pe){}

        //use a simple geodistance filter to limit measurement only within a specific location
        double latitude = 40.758896; //Times Square, NYC!
        double longitude = -73.985130;
        Location locationNear = new Location("dummyprovider");
        locationNear.setLatitude(latitude);
        locationNear.setLongitude(longitude);
        float distanceLimit = 1000; //meters

        getMeasurer().addThreshold(new GeoFenceThreshold(false, this, locationNear, distanceLimit));

        //implement custom threshold that only allows when random number is greater than 0.5
        getMeasurer().addThreshold(new BaseThreshold(true) {
            @Override
            public boolean allowMeasurement() {

                return Math.random()>0.5f;
            }
        });




    }

    private void initUI ()
    {
        setContentView(R.layout.activity_threshold);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Measurements dispatched!", Snackbar.LENGTH_LONG)
                        .setAction("Ok", null).show();

                //dispatch the current set of events to the server
                ((CleanInsightsApplication)getApplication()).getMeasurer().dispatch();
            }
        });
    }



    private Measurer getMeasurer() {
        return ((CleanInsightsApplication) getApplication()).getMeasurer();
    }


}
