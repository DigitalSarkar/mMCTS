package com.mcts.app.activity.maternalhealthservice;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.adapter.SimpleDropDownAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.adapter.TreatmentAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.DatePickerFragment;
import com.mcts.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PostNatalServiceActivity extends AppCompatActivity implements View.OnClickListener,OnItemSelectedListener {

    Activity thisActivity;
    private static String TAG = "PostNatalServiceActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView txt_village_name, txt_prag_name, txt_delv_date;
    private Spinner sp_delv_checkup, sp_family_welfare;
    private EditText ed_checkup_date, ed_mother_comp;
    private RadioButton rdb_yes, rdb_no, rdb_living, rdb_dead;
    private Button bt_prg_women_reg;
    private String isRefer, isLive;
    private Dialog progressDialog;
    ArrayList<MaritalStatus> complicationArrayList = null;
    private String postNatalData,postnatalserviceId;
    private JSONObject jsonpostNatalDataObject;
    private String familyWalfare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_natal_service);

        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = PostNatalServiceActivity.this;
        Intent intent=getIntent();
        postNatalData=intent.getStringExtra("postNatalData");

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
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_post_natal));
        txt_village_name = (TextView) findViewById(R.id.txt_village_name);
        txt_prag_name = (TextView) findViewById(R.id.txt_prag_name);
        txt_delv_date = (TextView) findViewById(R.id.txt_delv_date);
        sp_delv_checkup = (Spinner) findViewById(R.id.sp_delv_checkup);
        sp_family_welfare = (Spinner) findViewById(R.id.sp_family_welfare);
        ed_checkup_date = (EditText) findViewById(R.id.ed_checkup_date);
        ed_mother_comp = (EditText) findViewById(R.id.ed_mother_comp);
        rdb_yes = (RadioButton) findViewById(R.id.rdb_yes);
        rdb_no = (RadioButton) findViewById(R.id.rdb_no);
        rdb_living = (RadioButton) findViewById(R.id.rdb_living);
        rdb_dead = (RadioButton) findViewById(R.id.rdb_dead);
        bt_prg_women_reg = (Button) findViewById(R.id.bt_prg_women_reg);

        ed_checkup_date.setOnClickListener(this);
        ed_mother_comp.setOnClickListener(this);
        sp_family_welfare.setOnItemSelectedListener(this);

        setDropDown();

        if(postNatalData!=null){
            try {
                jsonpostNatalDataObject=new JSONObject(postNatalData);
                txt_village_name.setText(jsonpostNatalDataObject.getString("villageName"));
                postnatalserviceId=jsonpostNatalDataObject.getString("postnatalserviceId");
                txt_delv_date.setText(jsonpostNatalDataObject.getString("pregnancyoutcomeDate"));
                txt_prag_name.setText(jsonpostNatalDataObject.getString("firstName") + " " + jsonpostNatalDataObject.getString("middleName") + " " + jsonpostNatalDataObject.getString("lastName"));
                int position=Integer.parseInt(jsonpostNatalDataObject.getString("postnatalservicetypeId"));
                sp_delv_checkup.setSelection(position);
                sp_delv_checkup.setEnabled(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    private void setDropDown() {

        ArrayList<HashMap<String, String>> pragChekupArrayList = new ArrayList<>();
        String[] array = thisActivity.getResources().getStringArray(R.array.postnatal_service_type);

        for (int i = 0; i < array.length; i++) {
            HashMap map = new HashMap();
            map.put("" + i, array[i]);
            pragChekupArrayList.add(map);
        }

        SimpleDropDownAdapter simpleDropDownAdapter = new SimpleDropDownAdapter(thisActivity, pragChekupArrayList);
        sp_delv_checkup.setAdapter(simpleDropDownAdapter);

        DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
        ArrayList<MaritalStatus> familyWalfareList = databaseHelper.getFamilyPlanningData();
        StatusAdapter familyWalfareAdapter = new StatusAdapter(thisActivity, familyWalfareList);
        sp_family_welfare.setAdapter(familyWalfareAdapter);

        String[] complicationArray = thisActivity.getResources().getStringArray(R.array.mother_complication);
        complicationArrayList = new ArrayList<>();
        for (int i = 0; i < complicationArray.length; i++) {
            MaritalStatus maritalStatus = new MaritalStatus();
            maritalStatus.setId("" + i);
            maritalStatus.setStatus(complicationArray[i]);
            complicationArrayList.add(maritalStatus);
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

    public void clickEvent(View view) {

        switch (view.getId()) {
            case R.id.rdb_yes:
                isRefer = "1";
                rdb_yes.setChecked(true);
                rdb_no.setChecked(false);
                break;
            case R.id.rdb_no:
                isRefer = "0";
                rdb_yes.setChecked(false);
                rdb_no.setChecked(true);
                break;

            case R.id.rdb_living:
                isLive = "1";
                rdb_living.setChecked(true);
                rdb_dead.setChecked(false);
                break;

            case R.id.rdb_dead:
                isLive = "0";
                rdb_living.setChecked(false);
                rdb_dead.setChecked(true);
                break;

            case R.id.bt_prg_women_reg:

                DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("postnatalservicegivenDate",ed_checkup_date.getText().toString());
                hashMap.put("postnatalComplication",ed_mother_comp.getText().toString());
                hashMap.put("isReferred",isRefer);
                hashMap.put("plannedfpMethod",familyWalfare);
                hashMap.put("memberStatus",isLive);
                boolean isSaved=databaseHelper.savePostNatalData(hashMap,postnatalserviceId);
                if(isSaved){
                    String str = thisActivity.getResources().getString(R.string.insert);
                    CustomToast customToast = new CustomToast(thisActivity, str);
                    customToast.show();
                    thisActivity.finish();
                }
                break;

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ed_checkup_date:
                showDatePicker();
                break;
            case R.id.ed_mother_comp:
                showComplicationDialog(complicationArrayList, ed_mother_comp);
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
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            ed_checkup_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                    + "/" + String.valueOf(year));
        }
    };

    public void showComplicationDialog(ArrayList<MaritalStatus> treatmentArrayList, EditText ed_high_risk_mom) {

        progressDialog = new Dialog(thisActivity, R.style.DialogTheme);
        LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressView = mInflater.inflate(R.layout.custom_complication, null);
        Utils.findAllTextView(thisActivity, (ViewGroup) progressView.findViewById(R.id.ll_treatment));
        ListView list_complication = (ListView) progressView.findViewById(R.id.list_complication);
        Button bt_save_treatment = (Button) progressView.findViewById(R.id.bt_save_treatment);

        TreatmentAdapter treatmentAdapter = new TreatmentAdapter(thisActivity, treatmentArrayList, ed_high_risk_mom);
        list_complication.setAdapter(treatmentAdapter);

        Typeface type = Typeface.createFromAsset(thisActivity.getAssets(), "SHRUTI.TTF");
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(progressView);

        WindowManager wm = (WindowManager) thisActivity.getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
        Display display = wm.getDefaultDisplay(); // getting the screen size of device
        Point size = new Point();
        display.getSize(size);
        int width1 = WindowManager.LayoutParams.WRAP_CONTENT;
        int height1 = WindowManager.LayoutParams.WRAP_CONTENT;

        int tempValue = 0;
        tempValue = ((size.x) * 200) / 1440;
        int width = size.x - tempValue;  // Set your widths
        int height = height1; // set your heights

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(progressDialog.getWindow().getAttributes());

        lp.width = width;
        lp.height = height;
        progressDialog.getWindow().setAttributes(lp);
        progressDialog.setContentView(progressView);
        progressDialog.setCancelable(true);
        progressDialog.show();

        bt_save_treatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position!=0) {
            LinearLayout linearLayout;
            TextView textView;
            switch (parent.getId()) {

                case R.id.sp_family_welfare:
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    familyWalfare = textView.getTag().toString();
                    break;

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
