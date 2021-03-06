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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.adapter.ChildImmunizeListAdapter;
import com.mcts.app.adapter.ChildListAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ChildImmunizationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Activity thisActivity;
    private static String TAG="ChildImmunizationActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private ListView list_child_list;
    private Spinner sp_village;
    private String strVillageId, strVillageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bal_rasikaran);
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = ChildImmunizationActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.bal_rasikaran));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {


        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_child_immu));
        list_child_list = (ListView) findViewById(R.id.list_child_list);
        sp_village = (Spinner) findViewById(R.id.sp_village);

        sp_village.setOnItemSelectedListener(this);
        getVillageData();

    }

    private void getVillageData() {

        DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
        ArrayList<MaritalStatus> villageArray = databaseHelper.getVillageData();
        if (villageArray != null) {
            StatusAdapter masikAdapter = new StatusAdapter(thisActivity, villageArray);
            sp_village.setAdapter(masikAdapter);
        } else {
            String str = thisActivity.getResources().getString(R.string.no_data);
            CustomToast customToast = new CustomToast(thisActivity, str);
            customToast.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout linearLayout;
        TextView textView;
        if (position != 0) {
            linearLayout = (LinearLayout) view;
            textView = (TextView) linearLayout.getChildAt(0);
            strVillageId = textView.getTag().toString();
            strVillageName = textView.getText().toString();
            DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
            ArrayList<HashMap<String, String>> childArrayList = databaseHelper.getChildImmunizeData(strVillageId, strVillageName);

            if (childArrayList.size() != 0) {
                ChildImmunizeListAdapter childListAdapter = new ChildImmunizeListAdapter(thisActivity, childArrayList);
                list_child_list.setAdapter(childListAdapter);
            } else {
                ChildImmunizeListAdapter childListAdapter = new ChildImmunizeListAdapter(thisActivity, childArrayList);
                list_child_list.setAdapter(childListAdapter);
                String str = thisActivity.getResources().getString(R.string.no_match);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        } else {
            strVillageId = null;
            strVillageName = null;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_bal_rasikaran, menu);
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
}
