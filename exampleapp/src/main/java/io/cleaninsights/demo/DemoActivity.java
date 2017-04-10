/*
 * Android SDK for Piwik
 *
 * @link https://github.com/piwik/piwik-android-sdk
 * @license https://github.com/piwik/piwik-sdk-android/blob/master/LICENSE BSD-3 Clause
 */

package io.cleaninsights.demo;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;

import io.cleaninsights.sdk.piwik.CleanInsightsApplication;
import io.cleaninsights.sdk.piwik.MeasureHelper;
import io.cleaninsights.sdk.piwik.Measurer;

import io.cleaninsights.sdk.consent.ConsentUI;


public class DemoActivity extends AppCompatActivity {

    private CardContainer mCardContainer;

    private int mLikeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_swipe);

        Resources r = getResources();

        mCardContainer = (CardContainer) findViewById(R.id.swipeview);
        mCardContainer.setOrientation(Orientations.Orientation.Disordered);

        SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(this);

        int[] imgIds = {R.drawable.lolcat1,R.drawable.loldog1,R.drawable.lolcat2,R.drawable.loldog2,R.drawable.lolcat3,R.drawable.loldog3};

        for (int i = 0; i < 20; i++) {
            final int imgIdx = imgIds[(int)(Math.random()*((float)imgIds.length))];
            CardModel card = new CardModel("Option " + (i+1), "Swipe left to like, right to not", r.getDrawable(imgIdx));
            card.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                @Override
                public void onLike() {

                    //this is the total like counter for our privacy-enhanced "randomized response" tracking later in onPause()
                    mLikeCount++;

                    //this is typical event tracked, but shared with the server in a secure, non-unique identified manner
                    MeasureHelper.track()
                            .screen("/vote/cat/like/" + imgIdx)
                            .title("Vote")
                            .variable(1, "option", imgIdx + "")
                            .with(getTracker());
                }

                @Override
                public void onDislike() {

                    //this is typical event tracked, but shared with the server in a secure, non-unique identified manner
                    MeasureHelper.track()
                            .screen("/vote/cat/dislike" + imgIdx)
                            .title("Vote")
                            .variable(1, "option", imgIdx + "")
                            .with(getTracker());
                }
            });

            adapter.add(card);
        }

        mCardContainer.setAdapter(adapter);

        mLikeCount = 0;

        new ConsentUI().showConsentDialog(this);


    }

    private Measurer getTracker() {
        return ((CleanInsightsApplication) getApplication()).getMeasurer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //when the app pauses do a private, randomized-response based tracking of the number of likes
        MeasureHelper.track().privateEvent("Vote", "Like per Session", Integer.valueOf(mLikeCount).floatValue(), getTracker())
                .with(getTracker());

        //dispatch the current set of events to the server
        ((CleanInsightsApplication)getApplication()).getMeasurer().dispatch();
    }

    /**
    @OnClick(R.id.trackMainScreenViewButton)
    void onTrackMainScreenClicked(View view) {
        MeasureHelper.track().screen("/").title("Clean Insights Demo App: Main Screen").with(getTracker());
    }

    @OnClick(R.id.trackCustomVarsButton)
    void onTrackCustomVarsClicked(View view) {
        MeasureHelper.track()
                .screen("/custom_vars")
                .title("Custom Vars")
                .variable(1, "first", "var")
                .variable(2, "second", "long value")
                .with(getTracker());
    }

    @OnClick(R.id.raiseExceptionButton)
    void onRaiseExceptionClicked(View view) {
        MeasureHelper.track().exception(new Exception("OnPurposeException")).description("Crash button").fatal(false).with(getTracker());
    }

    @OnClick(R.id.trackGoalButton)
    void onTrackGoalClicked(View view) {
        float revenue;
        try {
            revenue = Integer.valueOf(
                    ((EditText) findViewById(R.id.goalTextEditView)).getText().toString()
            );
        } catch (Exception e) {
            MeasureHelper.track().exception(e).description("wrong revenue").with(getTracker());
            revenue = 0;  items = new EcommerceItems();
        }
        MeasureHelper.track().goal(1).revenue(revenue).with(getTracker());
    }**/


}
