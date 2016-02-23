package com.mcts.app.activity.familywalfare;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.familywalfare.fragment.FamilyPlannigListFragment;
import com.mcts.app.utils.CommonClass;
import com.mcts.app.utils.Utils;

import java.util.List;

public class FamilyPlannigActivity extends AppCompatActivity implements FamilyPlannigListFragment.OnFragmentInteractionListener{

    Activity thisActivity;
    private static String TAG = "FamilyPlannigActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private FamilyPlannigListFragment familyPlannigListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_plannig_list);

        setToolBar();

        CommonClass.addFragment(thisActivity, familyPlannigListFragment, false, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, R.id.frame_container);
//
    }

    private void setToolBar() {

        thisActivity = FamilyPlannigActivity.this;
        Utils.hideSoftKeyboard(thisActivity);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.kutumnb_kalyan_seva));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        familyPlannigListFragment=new FamilyPlannigListFragment();

    }

    public void setToolBarTitle(String Title, boolean isShowBack) {

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(Title);*/
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(Title);
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            thisActivity.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
        // Handle your other action bar items...
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
