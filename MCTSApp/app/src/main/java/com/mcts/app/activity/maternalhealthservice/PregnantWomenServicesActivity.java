package com.mcts.app.activity.maternalhealthservice;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.adapter.PragnentWomenServiceAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.PregnantWomen;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import java.util.ArrayList;

public class PregnantWomenServicesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Activity thisActivity;
    private static String TAG = "PregnantWomenServicesActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private ListView list_mother_service;
    private Spinner sp_village;
    private EditText ed_mother_name;
    private String strVillageName, strVillageId;
    private Button bt_mother_search;
    private boolean isEdd=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnant_women_services);

        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = PregnantWomenServicesActivity.this;
        Utils.hideSoftKeyboard(thisActivity);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.pregnancy_services));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_pragnency_services));
        list_mother_service = (ListView) findViewById(R.id.list_mother_service);
        sp_village = (Spinner) findViewById(R.id.sp_village);
        ed_mother_name = (EditText) findViewById(R.id.ed_mother_name);
        bt_mother_search = (Button) findViewById(R.id.bt_mother_search);

        sp_village.setOnItemSelectedListener(this);
        bt_mother_search.setOnClickListener(this);
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
        switch (parent.getId()) {
            case R.id.sp_village:
                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    strVillageId = textView.getTag().toString();
                    strVillageName = textView.getText().toString();
                    new PregnantWomenDefaultAsync().execute("", strVillageId);
                } else {
                    strVillageId = null;
                    strVillageName = null;
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        Utils.hideSoftKeyboard(thisActivity);
        if(strVillageId!=null) {
            if (ed_mother_name.getText().toString().length() > 0) {

                String searchString = ed_mother_name.getText().toString().trim();
                new PregnantWomenAsync().execute(searchString, strVillageId);

            } else {
                String str = thisActivity.getResources().getString(R.string.three_char);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        }else{
            String str = thisActivity.getResources().getString(R.string.select_village);
            CustomToast customToast = new CustomToast(thisActivity, str);
            customToast.show();
        }

    }

    /*private void getWomanList() {

        DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
        ArrayList<PregnantWomen> womenMemberArrayList = databaseHelper.searchWomanForHealthService();
        if (womenMemberArrayList.size() != 0) {
            PragnentWomenServiceAdapter pragnentWomenAdapter = new PragnentWomenServiceAdapter(thisActivity, womenMemberArrayList);
            list_mother_service.setAdapter(pragnentWomenAdapter);
        }else {
            PragnentWomenServiceAdapter pragnentWomenAdapter = new PragnentWomenServiceAdapter(thisActivity, womenMemberArrayList);
            list_mother_service.setAdapter(pragnentWomenAdapter);
            String str=thisActivity.getResources().getString(R.string.no_match);
            CustomToast customToast = new CustomToast(thisActivity, str);
            customToast.show();
        }
    }*/

    private class PregnantWomenDefaultAsync extends AsyncTask<String, String, String> {

        ArrayList<PregnantWomen> womenMemberArrayList;
        CustomLoaderDialog cm;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cm = new CustomLoaderDialog(thisActivity);
            cm.show(true);
        }

        @Override
        protected String doInBackground(String... params) {
            DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
            womenMemberArrayList = databaseHelper.searchWomanForHealthService(params[0],params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cm.hide();
            String searchString = ed_mother_name.getText().toString().trim();
            if (womenMemberArrayList.size() != 0) {
                PragnentWomenServiceAdapter searchFamilyMemberAdapter = new PragnentWomenServiceAdapter(thisActivity, womenMemberArrayList,strVillageId,strVillageName, isEdd);
                list_mother_service.setAdapter(searchFamilyMemberAdapter);
            } else {
                PragnentWomenServiceAdapter searchFamilyMemberAdapter = new PragnentWomenServiceAdapter(thisActivity, womenMemberArrayList,strVillageId,strVillageName, isEdd);
                list_mother_service.setAdapter(searchFamilyMemberAdapter);
                String str = thisActivity.getResources().getString(R.string.no_match);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        }

    }

    private class PregnantWomenAsync extends AsyncTask<String, String, String> {

        ArrayList<PregnantWomen> womenMemberArrayList;
        CustomLoaderDialog cm;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cm = new CustomLoaderDialog(thisActivity);
            cm.show(true);
        }

        @Override
        protected String doInBackground(String... params) {
            DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
            womenMemberArrayList = databaseHelper.searchWomanForHealthService(params[0],params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cm.hide();
            String searchString = ed_mother_name.getText().toString().trim();
            if (womenMemberArrayList.size() != 0) {
                PragnentWomenServiceAdapter searchFamilyMemberAdapter = new PragnentWomenServiceAdapter(thisActivity, womenMemberArrayList,strVillageId,strVillageName, isEdd);
                list_mother_service.setAdapter(searchFamilyMemberAdapter);
            } else {
                PragnentWomenServiceAdapter searchFamilyMemberAdapter = new PragnentWomenServiceAdapter(thisActivity, womenMemberArrayList,strVillageId,strVillageName, isEdd);
                list_mother_service.setAdapter(searchFamilyMemberAdapter);
                String str = thisActivity.getResources().getString(R.string.no_match);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_pregnant_women_services, menu);
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
