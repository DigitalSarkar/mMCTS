package com.mcts.app.activity.childhealth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.adapter.ChildImmunizeListAdapter;
import com.mcts.app.adapter.ImmunizeDetailAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChildImmunization extends AppCompatActivity {

    Activity thisActivity;
    private static String TAG="ChildImmunization";
    private Toolbar mToolbar;
    private TextView mTitle;
    private ListView list_child_immunization;
    private String childData,childregdId;
    private JSONObject jsonChildObject;
    private TextView txt_village_name,txt_child_name,txt_child_birth_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_immunization);
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = ChildImmunization.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.bal_rasikaran));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        Intent intent = getIntent();
        childData = intent.getStringExtra("childData");

        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_child_immunization));
        list_child_immunization = (ListView) findViewById(R.id.list_child_immunization);
        txt_village_name = (TextView) findViewById(R.id.txt_village_name);
        txt_child_name = (TextView) findViewById(R.id.txt_child_name);
        txt_child_birth_date = (TextView) findViewById(R.id.txt_child_birth_date);

        if (childData != null) {
            try {
                jsonChildObject = new JSONObject(childData);
                childregdId = jsonChildObject.getString("childregdId");
                txt_village_name.setText(jsonChildObject.getString("villageName"));
                txt_child_birth_date.setText(jsonChildObject.getString("birthDate"));
                txt_child_name.setText(jsonChildObject.getString("firstName") + " " + jsonChildObject.getString("middleName") + " " + jsonChildObject.getString("lastName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getChildImmuData();

    }

    private void getChildImmuData() {

        DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
        ArrayList<HashMap<String, String>> childArrayList = databaseHelper.getChildImmunizeDetails(childregdId);

        if (childArrayList.size() != 0) {
            ImmunizeDetailAdapter immunizeDetailAdapter = new ImmunizeDetailAdapter(thisActivity, childArrayList);
            list_child_immunization.setAdapter(immunizeDetailAdapter);
        } else {
            ImmunizeDetailAdapter immunizeDetailAdapter = new ImmunizeDetailAdapter(thisActivity, childArrayList);
            list_child_immunization.setAdapter(immunizeDetailAdapter);
            String str = thisActivity.getResources().getString(R.string.no_match);
            CustomToast customToast = new CustomToast(thisActivity, str);
            customToast.show();
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
    }
}
