package com.mcts.app.activity.childhealth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.maternalhealthservice.ChildListActivity;
import com.mcts.app.utils.Utils;

public class ChildHealthActivity extends AppCompatActivity implements OnClickListener{

    Activity thisActivity;
    private static String TAG="ChildHealthActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private LinearLayout ll_children_list,ll_bal_vaccine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bal_health);
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = ChildHealthActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.bal_seva_title));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_child_health));
        ll_children_list=(LinearLayout)findViewById(R.id.ll_children_list);
        ll_bal_vaccine=(LinearLayout)findViewById(R.id.ll_bal_vaccine);
        ll_children_list.setOnClickListener(this);
        ll_bal_vaccine.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_bal_health, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            thisActivity.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        Utils.ButtonClickEffect(v);
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.ll_children_list:
                intent =new Intent(thisActivity,ChildListActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_bal_vaccine:
                intent =new Intent(thisActivity,ChildImmunizationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
