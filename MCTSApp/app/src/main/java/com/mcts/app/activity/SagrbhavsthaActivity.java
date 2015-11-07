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

public class SagrbhavsthaActivity extends AppCompatActivity {

    Activity thisActivity;
    private static String TAG="HealthSevaActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private Button bt_masik_sevry,bt_new_pregnancy,bt_pregnancy_first_test,bt_pregnancy_second_test,bt_pregnancy_third_test,bt_pregnancy_forth_test,bt_tt_1
            ,bt_tt_2,bt_tt_booster,bt_risky_mom,bt_Kortikostiroidasa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sagrbhavstha);
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = SagrbhavsthaActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.sagarbha_seva_title));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
    }

    private void init() {

        bt_masik_sevry=(Button)findViewById(R.id.bt_masik_sevry);
        bt_new_pregnancy=(Button)findViewById(R.id.bt_new_pregnancy);
        bt_pregnancy_first_test=(Button)findViewById(R.id.bt_pregnancy_first_test);
        bt_pregnancy_second_test=(Button)findViewById(R.id.bt_pregnancy_second_test);
        bt_pregnancy_third_test=(Button)findViewById(R.id.bt_pregnancy_third_test);
        bt_pregnancy_forth_test=(Button)findViewById(R.id.bt_pregnancy_forth_test);
        bt_tt_1=(Button)findViewById(R.id.bt_tt_1);
        bt_tt_2=(Button)findViewById(R.id.bt_tt_2);
        bt_tt_booster=(Button)findViewById(R.id.bt_tt_booster);
        bt_risky_mom=(Button)findViewById(R.id.bt_risky_mom);
        bt_Kortikostiroidasa=(Button)findViewById(R.id.bt_Kortikostiroidasa);

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        bt_masik_sevry.setTypeface(type, Typeface.BOLD);
        bt_new_pregnancy.setTypeface(type, Typeface.BOLD);
        bt_pregnancy_first_test.setTypeface(type, Typeface.BOLD);
        bt_pregnancy_second_test.setTypeface(type, Typeface.BOLD);
        bt_pregnancy_third_test.setTypeface(type, Typeface.BOLD);
        bt_pregnancy_forth_test.setTypeface(type, Typeface.BOLD);
        bt_tt_1.setTypeface(type, Typeface.BOLD);
        bt_tt_2.setTypeface(type, Typeface.BOLD);
        bt_tt_booster.setTypeface(type, Typeface.BOLD);
        bt_risky_mom.setTypeface(type, Typeface.BOLD);
        bt_Kortikostiroidasa.setTypeface(type, Typeface.BOLD);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_sagrbhavstha, menu);
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
