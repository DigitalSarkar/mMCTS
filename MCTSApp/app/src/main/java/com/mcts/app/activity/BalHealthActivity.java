package com.mcts.app.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.mcts.app.R;

public class BalHealthActivity extends AppCompatActivity {

    Activity thisActivity;
    private static String TAG="BalHealthActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private Button bt_imnci,bt_hbnc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bal_health);
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = BalHealthActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.bal_seva_title));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
    }

    private void init() {

        bt_imnci=(Button)findViewById(R.id.bt_imnci);
        bt_hbnc=(Button)findViewById(R.id.bt_hbnc);

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        bt_imnci.setTypeface(type, Typeface.BOLD);
        bt_hbnc.setTypeface(type, Typeface.BOLD);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_bal_health, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
