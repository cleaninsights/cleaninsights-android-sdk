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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.cleaninsights.sdk.piwik.CleanInsightsApplication;
import io.cleaninsights.sdk.piwik.MeasureHelper;
import io.cleaninsights.sdk.piwik.Measurer;
import io.cleaninsights.sdk.piwik.ecommerce.EcommerceItems;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.cleaninsights.sdk.consent.ConsentUI;


public class DemoActivity extends ActionBarActivity {


    private CardContainer mCardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_swipe);

        Resources r = getResources();

        mCardContainer = (CardContainer) findViewById(R.id.swipeview);
        mCardContainer.setOrientation(Orientations.Orientation.Disordered);

        SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(this);

        for (int i = 0; i < 10; i++) {
            CardModel card = new CardModel("Cat " + i, "what a funny little kitty", r.getDrawable(R.drawable.lolcat1));
            final int currentId = i;
            card.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                @Override
                public void onLike() {
                    MeasureHelper.track()
                            .screen("/vote/cat/like/" + currentId)
                            .title("Vote")
                            .variable(1, "cat", currentId + "")
                            .with(getTracker());
                }

                @Override
                public void onDislike() {
                    MeasureHelper.track()
                            .screen("/vote/cat/dislike" + currentId)
                            .title("Vote")
                            .variable(1, "cat", currentId + "")
                            .with(getTracker());
                }
            });

            adapter.add(card);
        }

        mCardContainer.setAdapter(adapter);

        //new ConsentUI().showConsentDialog(this);

        new SweetAlertDialog(this)
                .setTitleText("Clean Insights Demo")
                .setContentText("Swipe Left to like, Swipe Right if you don't!")
                .show();

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
