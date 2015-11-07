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

public class BalRasikaranActivity extends AppCompatActivity {

    Activity thisActivity;
    private static String TAG="HealthSevaActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private Button bt_polio_0,bt_hipetytis_0,bt_bcg,bt_pentavelent_1,bt_pentavelent_2,bt_pentavelent_3,bt_ori_1
            ,bt_ori_2,bt_vita_a_1,bt_dpt_1,bt_dpt_booster,bt_polio_booster,bt_dpt_five_year,bt_tt_ten_year,bt_dpt_3,bt_tt_sixtine_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bal_rasikaran);
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = BalRasikaranActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.bal_rasikaran));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
    }

    private void init() {

        bt_polio_0=(Button)findViewById(R.id.bt_polio_0);
        bt_hipetytis_0=(Button)findViewById(R.id.bt_hipetytis_0);
        bt_bcg=(Button)findViewById(R.id.bt_bcg);
        bt_pentavelent_1=(Button)findViewById(R.id.bt_pentavelent_1);
        bt_pentavelent_2=(Button)findViewById(R.id.bt_pentavelent_2);
        bt_pentavelent_3=(Button)findViewById(R.id.bt_pentavelent_3);
        bt_ori_1=(Button)findViewById(R.id.bt_ori_1);
        bt_ori_2=(Button)findViewById(R.id.bt_ori_2);
        bt_vita_a_1=(Button)findViewById(R.id.bt_vita_a_1);
        bt_dpt_1=(Button)findViewById(R.id.bt_dpt_1);
        bt_dpt_booster=(Button)findViewById(R.id.bt_dpt_booster);
        bt_polio_booster=(Button)findViewById(R.id.bt_polio_booster);
        bt_dpt_five_year=(Button)findViewById(R.id.bt_dpt_five_year);
        bt_tt_ten_year=(Button)findViewById(R.id.bt_tt_ten_year);
        bt_dpt_3=(Button)findViewById(R.id.bt_dpt_3);
        bt_tt_sixtine_year=(Button)findViewById(R.id.bt_tt_sixtine_year);

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        bt_polio_0.setTypeface(type, Typeface.BOLD);
        bt_hipetytis_0.setTypeface(type, Typeface.BOLD);
        bt_bcg.setTypeface(type, Typeface.BOLD);
        bt_pentavelent_1.setTypeface(type, Typeface.BOLD);
        bt_pentavelent_2.setTypeface(type, Typeface.BOLD);
        bt_pentavelent_3.setTypeface(type, Typeface.BOLD);
        bt_ori_1.setTypeface(type, Typeface.BOLD);
        bt_ori_2.setTypeface(type, Typeface.BOLD);
        bt_vita_a_1.setTypeface(type, Typeface.BOLD);
        bt_dpt_1.setTypeface(type, Typeface.BOLD);
        bt_dpt_booster.setTypeface(type, Typeface.BOLD);
        bt_polio_booster.setTypeface(type, Typeface.BOLD);
        bt_dpt_five_year.setTypeface(type, Typeface.BOLD);
        bt_tt_ten_year.setTypeface(type, Typeface.BOLD);
        bt_dpt_3.setTypeface(type, Typeface.BOLD);
        bt_tt_sixtine_year.setTypeface(type, Typeface.BOLD);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_bal_rasikaran, menu);
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
