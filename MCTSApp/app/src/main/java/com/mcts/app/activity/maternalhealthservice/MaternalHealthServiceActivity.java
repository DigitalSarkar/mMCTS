package com.mcts.app.activity.maternalhealthservice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.utils.Utils;

public class MaternalHealthServiceActivity extends AppCompatActivity implements View.OnClickListener{

    Activity thisActivity;
    private static String TAG="MaternalHealthServiceActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private LinearLayout ll_new_pregnancy,ll_pregnancy_services,ll_delivery_registration,ll_post_delivery_services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sagrbhavstha);
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = MaternalHealthServiceActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.maternal_health));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_maternal_health));
        ll_new_pregnancy=(LinearLayout)findViewById(R.id.ll_new_pregnancy);
        ll_pregnancy_services=(LinearLayout)findViewById(R.id.ll_pregnancy_services);
        ll_delivery_registration=(LinearLayout)findViewById(R.id.ll_delivery_registration);
        ll_post_delivery_services=(LinearLayout)findViewById(R.id.ll_post_delivery_services);

        ll_new_pregnancy.setOnClickListener(this);
        ll_pregnancy_services.setOnClickListener(this);
        ll_delivery_registration.setOnClickListener(this);
        ll_post_delivery_services.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_sagrbhavstha, menu);
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
        // Handle your other action bar items...
    }

    @Override
    public void onClick(View v) {
        Utils.ButtonClickEffect(v);
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.ll_new_pregnancy:
                intent =new Intent(thisActivity,NewPragnencyActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_pregnancy_services:
                intent =new Intent(thisActivity,PregnantWomenServicesActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_delivery_registration:
                intent =new Intent(thisActivity,DeliveryRegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_children_list:
                intent =new Intent(thisActivity,ChildListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_post_delivery_services:
                intent =new Intent(thisActivity,PostNatalListActivity.class);
                startActivity(intent);
                break;
        }
    }


}
