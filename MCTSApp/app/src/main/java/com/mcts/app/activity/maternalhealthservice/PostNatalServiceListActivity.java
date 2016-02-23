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
import com.mcts.app.adapter.PostNatalListAdapter;
import com.mcts.app.adapter.PostNatalServiceAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PostNatalServiceListActivity extends AppCompatActivity{

    Activity thisActivity;
    private static String TAG = "PostNatalServiceListActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView txt_prag_reg_date,txt_pregnant_woman_name,txt_village_name;
    private ListView list_postnatal_service_list;
    private String pregnantwomanregdId;
    private String strVillageId, strVillageName;
    private String postNatalData;
    private JSONObject jsonpostNatalDataObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_natal_service_list);

        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = PostNatalServiceListActivity.this;
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

        Intent intent=getIntent();
        postNatalData=intent.getStringExtra("postNatalData");

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_post_natal_service));
        list_postnatal_service_list = (ListView) findViewById(R.id.list_postnatal_service_list);
        txt_prag_reg_date = (TextView) findViewById(R.id.txt_prag_reg_date);
        txt_pregnant_woman_name = (TextView) findViewById(R.id.txt_pregnant_woman_name);
        txt_village_name = (TextView) findViewById(R.id.txt_village_name);

        if(postNatalData!=null){
            try {
                jsonpostNatalDataObject=new JSONObject(postNatalData);
                pregnantwomanregdId=jsonpostNatalDataObject.getString("pregnantwomanregdId");
                strVillageId=jsonpostNatalDataObject.getString("villageId");
                strVillageName=jsonpostNatalDataObject.getString("villageName");
                txt_prag_reg_date.setText(jsonpostNatalDataObject.getString("pregnancyoutcomeDate"));
                txt_pregnant_woman_name.setText(jsonpostNatalDataObject.getString("firstName")+" "+jsonpostNatalDataObject.getString("middleName")+" "+jsonpostNatalDataObject.getString("lastName"));

                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                ArrayList<HashMap<String, String>> childArrayList = databaseHelper.getPostNatalServiceListData(pregnantwomanregdId,strVillageId, strVillageName);

                if (childArrayList.size() != 0) {
                    PostNatalServiceAdapter postNatalListAdapter = new PostNatalServiceAdapter(thisActivity, childArrayList);
                    list_postnatal_service_list.setAdapter(postNatalListAdapter);
                } else {
                    PostNatalServiceAdapter postNatalListAdapter = new PostNatalServiceAdapter(thisActivity, childArrayList);
                    list_postnatal_service_list.setAdapter(postNatalListAdapter);
                    String str = thisActivity.getResources().getString(R.string.no_match);
                    CustomToast customToast = new CustomToast(thisActivity, str);
                    customToast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
