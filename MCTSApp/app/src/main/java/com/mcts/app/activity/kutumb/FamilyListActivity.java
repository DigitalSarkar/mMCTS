package com.mcts.app.activity.kutumb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.adapter.SearchFamilyMemberAdapter;
import com.mcts.app.adapter.SearchMemberAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.Family;
import com.mcts.app.utils.Messages;
import com.mcts.app.utils.Utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class FamilyListActivity extends AppCompatActivity implements View.OnClickListener {

    Activity thisActivity;
    private static String TAG="FamilyListActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private ImageView img_icon_home;
    private ListView list_members;
    private EditText ed_family_number;
    private Button bt_family_search,bt_add_family;
    private CustomToast customToast;
    private Spinner sp_year;
    DatabaseHelper databaseHelper;
    private String strVillageId,strVillageName;
    private TextView txt_village;
    private FloatingActionButton fab;
    private int isFamily=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_list);
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = FamilyListActivity.this;
        Utils.hideSoftKeyboard(thisActivity);

        Intent intent=getIntent();
        strVillageId=intent.getStringExtra("strVillageId");
        strVillageName=intent.getStringExtra("strVillageName");
        isFamily=intent.getIntExtra("isFamily", 0);

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.toolbar_home);
        mTitle = (TextView) mToolbar.findViewById(R.id.txt_title);
        img_icon_home = (ImageView) mToolbar.findViewById(R.id.img_icon_home);
        mTitle.setText(thisActivity.getResources().getString(R.string.family_list));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void init() {

        databaseHelper=new DatabaseHelper(thisActivity);
        bt_family_search=(Button)findViewById(R.id.bt_family_search);
        bt_add_family=(Button)findViewById(R.id.bt_add_family);
        ed_family_number=(EditText)findViewById(R.id.ed_family_number);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        txt_village=(TextView)findViewById(R.id.txt_village);
        sp_year=(Spinner)findViewById(R.id.sp_year);
        list_members=(ListView)findViewById(R.id.list_members);
        txt_village.setText(strVillageName);
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_family_list));
        bt_family_search.setOnClickListener(this);
        bt_add_family.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_family_list, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_family_search:

                if(ed_family_number.getText().toString().length()>1){
                    Utils.hideSoftKeyboard(thisActivity);
                    if(isFamily==0) {
                        String searchString = sp_year.getSelectedItem().toString() + ed_family_number.getText().toString().trim();
                        ArrayList<Family> familyArrayList = databaseHelper.searchFamily(searchString, strVillageId);
                        if (familyArrayList.size() != 0) {
                            SearchMemberAdapter searchMemberAdapter = new SearchMemberAdapter(thisActivity, familyArrayList, strVillageId, strVillageName);
                            list_members.setAdapter(searchMemberAdapter);
                        } else {
                            SearchMemberAdapter searchMemberAdapter = new SearchMemberAdapter(thisActivity, familyArrayList, strVillageId, strVillageName);
                            list_members.setAdapter(searchMemberAdapter);
                            CustomToast customToast = new CustomToast(thisActivity, Messages.NO_MATCH_DATA);
                            customToast.show();
                        }
                    }else{
                        String searchString = sp_year.getSelectedItem().toString() + ed_family_number.getText().toString().trim();
                        ArrayList<Family> familyArrayList = databaseHelper.searchFamilyMember(searchString, strVillageId);
                        if (familyArrayList.size() != 0) {
                            SearchFamilyMemberAdapter searchFamilyMemberAdapter = new SearchFamilyMemberAdapter(thisActivity, familyArrayList, strVillageId, strVillageName);
                            list_members.setAdapter(searchFamilyMemberAdapter);
                        } else {
                            SearchFamilyMemberAdapter searchFamilyMemberAdapter = new SearchFamilyMemberAdapter(thisActivity, familyArrayList, strVillageId, strVillageName);
                            list_members.setAdapter(searchFamilyMemberAdapter);
                            CustomToast customToast = new CustomToast(thisActivity, Messages.NO_MATCH_DATA);
                            customToast.show();
                        }
                    }
                }else{
                    CustomToast customToast=new CustomToast(thisActivity,Messages.MINIMUM_CHARACTER);
                    customToast.show();
                }

                break;
            case R.id.bt_add_family:
                Intent intent=new Intent(thisActivity,NewFamilyActivity.class);
                intent.putExtra("villageId",strVillageId);
                intent.putExtra("villageName",strVillageName);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            thisActivity.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
        // Handle your other action bar items...
    }
}
