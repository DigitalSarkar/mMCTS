package com.mcts.app.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mcts.app.R;
import com.mcts.app.adapter.AncPlanListAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.DatePickerFragment;
import com.mcts.app.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MyWorkPlan extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnClickListener {

    Activity thisActivity;
    private static String TAG = "MyWorkPlan";
    private Toolbar mToolbar;
    private TextView mTitle;
    private ListView list_work_list;
    private EditText ed_work_date;
    private Spinner sp_village, sp_work_list;
    private String strVillageId, strVillageName, optionId, isAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_work_plan);

        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = MyWorkPlan.this;
        Utils.hideSoftKeyboard(thisActivity);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.my_schedule));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_my_work));
        list_work_list = (ListView) findViewById(R.id.list_work_list);
        sp_village = (Spinner) findViewById(R.id.sp_village);
        sp_work_list = (Spinner) findViewById(R.id.sp_work_list);
        ed_work_date = (EditText) findViewById(R.id.ed_work_date);

        sp_village.setOnItemSelectedListener(this);
        sp_work_list.setOnItemSelectedListener(this);
        ed_work_date.setOnClickListener(this);
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

        String[] array = this.getResources().getStringArray(R.array.my_work_choice);
        ArrayList<MaritalStatus> choiceArrayList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            MaritalStatus reason = new MaritalStatus();
            reason.setId("" + i);
            reason.setStatus(array[i]);
            choiceArrayList.add(reason);
        }
        StatusAdapter coppertTypeAdapter = new StatusAdapter(thisActivity, choiceArrayList);
        sp_work_list.setAdapter(coppertTypeAdapter);
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
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                } else {
                    strVillageId = null;
                    strVillageName = null;
                }
                break;
            case R.id.sp_work_list:
                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    optionId = textView.getTag().toString();
//                    Toast.makeText(thisActivity,optionId +" = "+textView.getText().toString(),Toast.LENGTH_SHORT).show();
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);

                } else {
                    optionId = null;
                }
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ed_work_date:
                showDatePicker();
                break;

        }

    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(getDate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener getDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            ed_work_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                    + "/" + String.valueOf(year));
        }
    };

    public void clickEvent(View view) {

        DatabaseHelper databaseHelper;
        switch (Integer.parseInt(optionId)) {
            case 1:
                databaseHelper = new DatabaseHelper(thisActivity);
                ArrayList<HashMap<String, String>> planArrayList = databaseHelper.getAncWorkPlan(strVillageId, ed_work_date.getText().toString());

                AncPlanListAdapter ancPlanListAdapter = new AncPlanListAdapter(thisActivity, planArrayList,1);
                list_work_list.setAdapter(ancPlanListAdapter);
                break;
            case 2:
               /* databaseHelper = new DatabaseHelper(thisActivity);
                ArrayList<HashMap<String, String>> pregnancyArrayList = databaseHelper.getEstimatePregnancy(strVillageId);

                AncPlanListAdapter pregrancyAdapter = new AncPlanListAdapter(thisActivity, pregnancyArrayList,2);
                list_work_list.setAdapter(pregrancyAdapter);*/
                break;
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
