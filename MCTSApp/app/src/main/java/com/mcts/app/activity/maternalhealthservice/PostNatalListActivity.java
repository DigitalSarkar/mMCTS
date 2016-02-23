package com.mcts.app.activity.maternalhealthservice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.adapter.ChildListAdapter;
import com.mcts.app.adapter.PostNatalListAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class PostNatalListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Activity thisActivity;
    private static String TAG = "PostNatalListActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private ListView list_postnatal_list;
    private Spinner sp_village;
    private String pregnantwomanregdId;
    private String strVillageId, strVillageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_natal_list);

        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = PostNatalListActivity.this;
        Utils.hideSoftKeyboard(thisActivity);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.post_delivery_services));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_child_list));
        list_postnatal_list = (ListView) findViewById(R.id.list_postnatal_list);
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
            ArrayList<HashMap<String, String>> childArrayList = databaseHelper.getPostNatalServiceData(strVillageId, strVillageName);

            if (childArrayList.size() != 0) {
                PostNatalListAdapter postNatalListAdapter = new PostNatalListAdapter(thisActivity, childArrayList);
                list_postnatal_list.setAdapter(postNatalListAdapter);
            } else {
                PostNatalListAdapter postNatalListAdapter = new PostNatalListAdapter(thisActivity, childArrayList);
                list_postnatal_list.setAdapter(postNatalListAdapter);
                String str = thisActivity.getResources().getString(R.string.no_match);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }

            /*Intent intent=new Intent(thisActivity,PostNatalServiceActivity.class);
            startActivity(intent);*/
        } else {
            strVillageId = null;
            strVillageName = null;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sp_village.getSelectedItemPosition()!=0) {

            DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
            ArrayList<HashMap<String, String>> childArrayList = databaseHelper.getPostNatalServiceData(strVillageId, strVillageName);

            if (childArrayList.size() != 0) {
                PostNatalListAdapter postNatalListAdapter = new PostNatalListAdapter(thisActivity, childArrayList);
                list_postnatal_list.setAdapter(postNatalListAdapter);
            } else {
                PostNatalListAdapter postNatalListAdapter = new PostNatalListAdapter(thisActivity, childArrayList);
                list_postnatal_list.setAdapter(postNatalListAdapter);
                String str = thisActivity.getResources().getString(R.string.no_match);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        }
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
}
