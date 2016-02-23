package com.mcts.app.activity.maternalhealthservice;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.mcts.app.adapter.DeliveryWomenRegisterAdapter;
import com.mcts.app.adapter.PragnentWomenServiceAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.PregnantWomen;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import java.util.ArrayList;

public class DeliveryRegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    Activity thisActivity;
    private static String TAG = "DeliveryRegistrationActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private ListView list_preg_woman;
    private Spinner sp_village;
    private EditText ed_mother_name;
    private String strVillageName, strVillageId;
    private Button bt_member_search;
    private boolean isEdd=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivey_registration);

        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = DeliveryRegistrationActivity.this;
        Utils.hideSoftKeyboard(thisActivity);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.delivery_registration));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_delivery_reg));
        list_preg_woman = (ListView) findViewById(R.id.list_preg_woman);
        sp_village = (Spinner) findViewById(R.id.sp_village);
        ed_mother_name = (EditText) findViewById(R.id.ed_mother_name);
        bt_member_search = (Button) findViewById(R.id.bt_member_search);
        isEdd=true;

        sp_village.setOnItemSelectedListener(this);
        bt_member_search.setOnClickListener(this);
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
    public void onClick(View v) {

        Utils.hideSoftKeyboard(thisActivity);
        if(strVillageId!=null) {
            if (ed_mother_name.getText().toString().length() > 0) {

                String searchString = ed_mother_name.getText().toString().trim();
                new DeliveryWomenAsync().execute(searchString, strVillageId);

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
                    new DeliveryWomenDefaultAsync().execute("", strVillageId);
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

    private class DeliveryWomenDefaultAsync extends AsyncTask<String, String, String> {

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
            womenMemberArrayList = databaseHelper.searchWomanForPregRegister(params[0],params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cm.hide();
            String searchString = ed_mother_name.getText().toString().trim();
            if (womenMemberArrayList.size() != 0) {
                DeliveryWomenRegisterAdapter searchFamilyMemberAdapter = new DeliveryWomenRegisterAdapter(thisActivity, womenMemberArrayList,strVillageId,strVillageName);
                list_preg_woman.setAdapter(searchFamilyMemberAdapter);
            } else {
                DeliveryWomenRegisterAdapter searchFamilyMemberAdapter = new DeliveryWomenRegisterAdapter(thisActivity, womenMemberArrayList,strVillageId,strVillageName);
                list_preg_woman.setAdapter(searchFamilyMemberAdapter);
                String str = thisActivity.getResources().getString(R.string.no_match);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        }

    }

    private class DeliveryWomenAsync extends AsyncTask<String, String, String> {

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
            womenMemberArrayList = databaseHelper.searchWomanForHealthService(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cm.hide();
            String searchString = ed_mother_name.getText().toString().trim();
            if (womenMemberArrayList.size() != 0) {
                DeliveryWomenRegisterAdapter searchFamilyMemberAdapter = new DeliveryWomenRegisterAdapter(thisActivity, womenMemberArrayList,strVillageId,strVillageName);
                list_preg_woman.setAdapter(searchFamilyMemberAdapter);
            } else {
                DeliveryWomenRegisterAdapter searchFamilyMemberAdapter = new DeliveryWomenRegisterAdapter(thisActivity, womenMemberArrayList,strVillageId,strVillageName);
                list_preg_woman.setAdapter(searchFamilyMemberAdapter);
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
