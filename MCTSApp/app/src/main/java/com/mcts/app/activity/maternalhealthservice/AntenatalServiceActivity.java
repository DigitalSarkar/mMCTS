package com.mcts.app.activity.maternalhealthservice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.adapter.AntenatalServiceAdapter;
import com.mcts.app.adapter.PragnentWomenServiceAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.PregnantWomen;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AntenatalServiceActivity extends AppCompatActivity {

    Activity thisActivity;
    private static String TAG = "AntenatalServiceActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private ListView list_antenal_service;
    private TextView txt_village_name,txt_pregnant_woman_name,txt_new_pregnancy_number,txt_pregnant_woman_regd_date;
    private String pregnantId,villageId,villageName;
    private String pregnantwomanregdId;
    private JSONObject jsonDataObject;
    private String bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antenatal_service);
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = AntenatalServiceActivity.this;
        Utils.hideSoftKeyboard(thisActivity);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.pre_pregnancy_seva));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_antenatal_services));
        list_antenal_service = (ListView) findViewById(R.id.list_antenal_service);
        txt_village_name = (TextView) findViewById(R.id.txt_village_name);
        txt_pregnant_woman_name = (TextView) findViewById(R.id.txt_pregnant_woman_name);
        txt_new_pregnancy_number = (TextView) findViewById(R.id.txt_new_pregnancy_number);
        txt_pregnant_woman_regd_date = (TextView) findViewById(R.id.txt_pregnant_woman_regd_date);

               
        Intent intent=getIntent();
        pregnantId=intent.getStringExtra("pregnantId");
        String data=intent.getStringExtra("data");
        try {
            jsonDataObject=new JSONObject(data);
            villageId=jsonDataObject.getString("villageId");
            villageName=jsonDataObject.getString("villageName");
            String name=jsonDataObject.getString("name");
            String pregnantwomanregdDate=jsonDataObject.getString("pregnantwomanregdDate");
            pregnantwomanregdId=jsonDataObject.getString("pregnantwomanregdId");
            if(jsonDataObject.has("bloodGroup")) {
                if (jsonDataObject.getString("bloodGroup") != null) {
                    bloodGroup = jsonDataObject.getString("bloodGroup");
                }
            }
            txt_village_name.setText(villageName);
            txt_new_pregnancy_number.setText(pregnantwomanregdId);
            txt_pregnant_woman_name.setText(name);
            txt_pregnant_woman_regd_date.setText(pregnantwomanregdDate);


        } catch (JSONException e) {
            e.printStackTrace();
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
            womenMemberArrayList = databaseHelper.getPregnantWoman(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cm.hide();
            if (womenMemberArrayList.size() != 0) {
                AntenatalServiceAdapter antenatalServiceAdapter = new AntenatalServiceAdapter(thisActivity, womenMemberArrayList,jsonDataObject.toString());
                list_antenal_service.setAdapter(antenatalServiceAdapter);
            } else {
                AntenatalServiceAdapter antenatalServiceAdapter = new AntenatalServiceAdapter(thisActivity, womenMemberArrayList,jsonDataObject.toString());
                list_antenal_service.setAdapter(antenatalServiceAdapter);
                String str = thisActivity.getResources().getString(R.string.no_match);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        new  PregnantWomenAsync().execute(pregnantwomanregdId);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_antenatal, menu);
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
}
