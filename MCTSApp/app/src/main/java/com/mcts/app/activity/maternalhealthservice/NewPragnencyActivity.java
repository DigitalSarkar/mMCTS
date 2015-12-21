package com.mcts.app.activity.maternalhealthservice;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.mcts.app.adapter.PragnentWomenAdapter;
import com.mcts.app.adapter.SearchMemberAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.Family;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.Member;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewPragnencyActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    Activity thisActivity;
    private static String TAG="NewPragnencyActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private EditText ed_mother_name;
    private Button bt_mother_search;
    private ListView list_mother;
    private Spinner sp_village;
    private String strVillageName, strVillageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pragnancy);

        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = NewPragnencyActivity.this;
        Utils.hideSoftKeyboard(thisActivity);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.new_pregnancy));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
    }

    private void init() {

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_new_pragnency));
        ed_mother_name= (EditText) findViewById(R.id.ed_mother_name);
        bt_mother_search= (Button) findViewById(R.id.bt_mother_search);
        list_mother= (ListView) findViewById(R.id.list_mother);
        sp_village = (Spinner) findViewById(R.id.sp_village);

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
            String str=thisActivity.getResources().getString(R.string.no_data);
            CustomToast customToast = new CustomToast(thisActivity, str);
            customToast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_new_pragnancy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_mother_search:
                Utils.hideSoftKeyboard(thisActivity);
                if(strVillageId!=null) {
                    if (ed_mother_name.getText().toString().length() > 0) {
                        String searchString = ed_mother_name.getText().toString().trim();
                        new GetFemales().execute(searchString,strVillageId);
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
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout linearLayout;
        TextView textView;
        switch (parent.getId()) {
            case R.id.sp_village:
                if(position!=0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    strVillageId = textView.getTag().toString();
                    strVillageName = textView.getText().toString();
                }else{
                    strVillageId=null;
                    strVillageName=null;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class GetFemales extends AsyncTask<String,String,String> {

        ArrayList<Member> womenMemberArrayList;
        CustomLoaderDialog cm;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cm=new CustomLoaderDialog(thisActivity);
            cm.show(true);
        }

        @Override
        protected String doInBackground(String... params) {

            DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
            womenMemberArrayList = databaseHelper.searchFemales(params[0],params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cm.hide();
            if (womenMemberArrayList.size() != 0) {

                ArrayList<Member> memberArrayList=new ArrayList<>();
                for(int i=0;i<womenMemberArrayList.size();i++){
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "dd-MMM-yyyy");
                        Date currentDate = new Date();
                        Date date = dateFormat.parse(new SimpleDateFormat("dd-MMM-yyyy").format(currentDate));
                        Date prevDate = dateFormat.parse(womenMemberArrayList.get(i).getBirthDate());
                        int year=getYear(prevDate, date);
//                        Member member=womenMemberArrayList.get(i);
//                        member.setAgeDifference(year);
//                        womenMemberArrayList.set(i,member);
                        if(year>=15 && year<=49){
                            Member member=womenMemberArrayList.get(i);
                            member.setAgeDifference(year);
                            memberArrayList.add(member);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                Log.v(TAG,"womenMemberArrayList Size= "+womenMemberArrayList.size());
                Log.v(TAG,"memberArrayList Size= "+memberArrayList.size());

                String searchString = ed_mother_name.getText().toString().trim();
                PragnentWomenAdapter pragnentWomenAdapter = new PragnentWomenAdapter(thisActivity, memberArrayList, strVillageId, strVillageName,searchString);
                list_mother.setAdapter(pragnentWomenAdapter);

            } else {
                String searchString = ed_mother_name.getText().toString().trim();
                PragnentWomenAdapter pragnentWomenAdapter = new PragnentWomenAdapter(thisActivity, womenMemberArrayList, strVillageId, strVillageName,searchString);
                list_mother.setAdapter(pragnentWomenAdapter);
                String str=thisActivity.getResources().getString(R.string.no_match);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        }

    }

    private int getYear(Date date1,Date date2){
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
        Integer.parseInt(simpleDateformat.format(date1));

        return Integer.parseInt(simpleDateformat.format(date2))- Integer.parseInt(simpleDateformat.format(date1));

    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.hideSoftKeyboard(thisActivity);
        if(strVillageId!=null) {
            if (ed_mother_name.getText().toString().length() > 0) {
                String searchString = ed_mother_name.getText().toString().trim();
                new GetFemales().execute(searchString, strVillageId);
            }
        }
    }
}
