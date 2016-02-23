package com.mcts.app.activity.maternalhealthservice;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mcts.app.R;
import com.mcts.app.activity.ImageCroppingActivity;
import com.mcts.app.adapter.ExpandablaListAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.Member;
import com.mcts.app.model.PregnantWomen;
import com.mcts.app.model.WomenHighRisk;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.DatePickerFragment;
import com.mcts.app.utils.FormValidation;
import com.mcts.app.utils.TakePictureUtils;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class PregnantWomenRegistrationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Activity thisActivity;
    private static String TAG = "PregnantWomenRegistrationActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private EditText ed_Mobile_number, ed_anc_regd_date, ed_lmp_date, ed_edd_date, ed_high_risk_mom, ed_height, ed_weight;
    private TextView txt_village_name, txt_anc_name, txt_anc_regdnumber, txt_take_image;
    private Spinner sp_anc_gravida, sp_anc_para, sp_anc_abortion, sp_live_male, sp_live_female, sp_asha_name, sp_blood_group_value;
    private RadioButton rdb_yes, rdb_no, rdb_chiranjivi_yes, rdb_chiranjivi_no;
    private String villageId, villageName, healthId, age;
    private String isAns, isChiranjivi;
    private Button bt_prg_women_reg;
    private long elapsedDays;
    private Member member;
    private int gravida = 0, para = 0, abortion = 0, male = 0, female = 0, liveTotal = 0, isKpsy, isJsy, isEarlyRegd;
    private Dialog progressDialog;
    private ArrayList<WomenHighRisk> womenHighRiskArrayList;
    String ancserviceDate1, ancserviceDate2, ancserviceDate3, ancserviceDate4;
    StringBuilder stringBuilderId = new StringBuilder();
    String prefixId = "";
    boolean hivStatus;

    //    Image Capture
    ImageView imgUserImage;
    private String imageName = "item_picture";
    public static final int TAKE_PICTURE = 1;
    private final int CROP_PIC = 2;
    private Uri picUri;
    private String imageRealPath = null;
    private File compressFile;
    private Bitmap image_bitmap;
    private String ashaWorkerId;
    PregnantWomen pregnantWomen=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pragnent_women_registration);

        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = PregnantWomenRegistrationActivity.this;
        Utils.hideSoftKeyboard(thisActivity);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.new_pregnancy));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        pregnantWomen = new PregnantWomen();
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_pregnant_registration));
        txt_village_name = (TextView) findViewById(R.id.txt_village_name);
        txt_anc_name = (TextView) findViewById(R.id.txt_anc_name);
        txt_anc_regdnumber = (TextView) findViewById(R.id.txt_anc_regdnumber);
        txt_take_image = (TextView) findViewById(R.id.txt_take_image);
        ed_Mobile_number = (EditText) findViewById(R.id.ed_Mobile_number);
        ed_anc_regd_date = (EditText) findViewById(R.id.ed_anc_regd_date);
        ed_lmp_date = (EditText) findViewById(R.id.ed_lmp_date);
        ed_edd_date = (EditText) findViewById(R.id.ed_edd_date);
        sp_asha_name = (Spinner) findViewById(R.id.sp_asha_name);
        ed_high_risk_mom = (EditText) findViewById(R.id.ed_high_risk_mom);
        ed_weight = (EditText) findViewById(R.id.ed_weight);
        ed_height = (EditText) findViewById(R.id.ed_height);
        sp_anc_gravida = (Spinner) findViewById(R.id.sp_anc_gravida);
        sp_anc_para = (Spinner) findViewById(R.id.sp_anc_para);
        sp_anc_abortion = (Spinner) findViewById(R.id.sp_anc_abortion);
        sp_live_male = (Spinner) findViewById(R.id.sp_live_male);
        sp_live_female = (Spinner) findViewById(R.id.sp_live_female);
        sp_blood_group_value = (Spinner) findViewById(R.id.sp_blood_group_value);
        imgUserImage = (ImageView) findViewById(R.id.imgUserImage);
        rdb_yes = (RadioButton) findViewById(R.id.rdb_yes);
        rdb_no = (RadioButton) findViewById(R.id.rdb_no);
        rdb_chiranjivi_yes = (RadioButton) findViewById(R.id.rdb_chiranjivi_yes);
        rdb_chiranjivi_no = (RadioButton) findViewById(R.id.rdb_chiranjivi_no);
        bt_prg_women_reg = (Button) findViewById(R.id.bt_prg_women_reg);

        ed_weight.addTextChangedListener(new CustomTextWatcher(ed_weight));
        ed_height.addTextChangedListener(new CustomTextWatcher(ed_height));

        SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
        String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
        try {
            JSONObject jsonObject = new JSONObject(userDetail);
            String subCenterId = jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId");
            String userId = jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");
            Random emamtaRandom = new Random();
            int emamId = emamtaRandom.nextInt(900) + 100;

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);

            String strEmamtaId = "PW/" + year + "/" + userId + "" + emamId;
            txt_anc_regdnumber.setText(strEmamtaId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        villageId = intent.getStringExtra("villageId");
        villageName = intent.getStringExtra("villageName");
        healthId = intent.getStringExtra("healthId");
        age = intent.getStringExtra("age");

        txt_village_name.setText(villageName);

        DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
        member = databaseHelper.getPregnantWomenMember(healthId);

        hivStatus=databaseHelper.getMemberHivStatus(healthId);
        if(hivStatus){
            ed_high_risk_mom.setText(this.getResources().getText(R.string.hiv_st));
            ed_high_risk_mom.setTag("7");
        }

        ArrayList<MaritalStatus> ashaWorkerArrayList = databaseHelper.getAshaWorkers(villageId);
        StatusAdapter statusAdapter = new StatusAdapter(thisActivity, ashaWorkerArrayList);
        sp_asha_name.setAdapter(statusAdapter);

        if (member != null) {

            txt_anc_name.setText(member.getFirstName() + " " + member.getMiddleName() + " " + member.getLastName());
            ed_Mobile_number.setText(member.getMobileNo());
            ed_height.setText(member.getHeight());

            String[] strings = thisActivity.getResources().getStringArray(R.array.blood_group_value);
            if (member.getBloodGroup() != null) {
                for (int i = 0; i < strings.length; i++) {
                    if (member.getBloodGroup().equals(strings[i])) {
                        sp_blood_group_value.setSelection(i);
                        sp_blood_group_value.setEnabled(false);
                    }
                }
            }
            if (member.getIsBpl() != null) {
                if (member.getIsBpl().equals("1")) {
                    rdb_yes.setChecked(true);
                    rdb_no.setChecked(false);
                    isAns = member.getIsBpl();
                } else {
                    rdb_no.setChecked(true);
                    rdb_yes.setChecked(false);
                    isAns = member.getIsBpl();
                }
            }

            if (member.getPhoto() != null) {
                if (member.getPhoto().length() > 5) {
                    if (member.getPhotoValue() != null) {
                        Uri uri = Uri.parse(member.getPhotoValue());
                        Bitmap image_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));
                        imgUserImage.setImageBitmap(image_bitmap);
                        imgUserImage.setVisibility(View.VISIBLE);
                        txt_take_image.setVisibility(View.GONE);
                    }
                } else {
                    imgUserImage.setVisibility(View.INVISIBLE);
                    txt_take_image.setVisibility(View.VISIBLE);
                }
            } else {
                imgUserImage.setVisibility(View.INVISIBLE);
                txt_take_image.setVisibility(View.VISIBLE);
            }

            /*if(member.getBloodGroup()!=null){
                String[] bloodGroup=thisActivity.getResources().getStringArray(R.array.blood_group_value);
                for(int i=0;i<bloodGroup.length;i++){
                    if(member.getBloodGroup().equals(bloodGroup[i])){
                        sp_blood_group_value.setSelection(i);
                    }else{
                        sp_blood_group_value.setSelection(0);
                    }
                }
            }*/
        }

        ArrayList<WomenHighRisk> womenHighRiskArray = databaseHelper.getHighRiskCategory();
        womenHighRiskArrayList = databaseHelper.getHighRiskSymtoms(womenHighRiskArray);


        ed_anc_regd_date.setOnClickListener(this);
        ed_lmp_date.setOnClickListener(this);
        ed_edd_date.setOnClickListener(this);
        ed_high_risk_mom.setOnClickListener(this);
        rdb_yes.setOnClickListener(this);
        rdb_no.setOnClickListener(this);
        txt_take_image.setOnClickListener(this);
        imgUserImage.setOnClickListener(this);
        rdb_chiranjivi_no.setOnClickListener(this);
        rdb_chiranjivi_yes.setOnClickListener(this);
        bt_prg_women_reg.setOnClickListener(this);
        sp_anc_gravida.setOnItemSelectedListener(this);
        sp_anc_para.setOnItemSelectedListener(this);
        sp_anc_abortion.setOnItemSelectedListener(this);
        sp_live_male.setOnItemSelectedListener(this);
        sp_live_female.setOnItemSelectedListener(this);
        sp_asha_name.setOnItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_pragnent_women_registration, menu);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgUserImage:
                captureImage();
                break;
            case R.id.txt_take_image:
                captureImage();
                break;
            case R.id.ed_anc_regd_date:
                showDatePicker(1);
                break;
            case R.id.ed_lmp_date:
                showDatePicker(2);
                break;
            case R.id.ed_edd_date:
//                showDatePicker(3);
                break;
            case R.id.rdb_yes:
                rdb_yes.setChecked(true);
                rdb_no.setChecked(false);
                isAns = "1";
                break;
            case R.id.rdb_no:
                rdb_yes.setChecked(false);
                rdb_no.setChecked(true);
                isAns = "0";
                break;
            case R.id.rdb_chiranjivi_yes:
                rdb_chiranjivi_yes.setChecked(true);
                rdb_chiranjivi_no.setChecked(false);
                isChiranjivi = "1";
                break;
            case R.id.rdb_chiranjivi_no:
                rdb_yes.setChecked(false);
                rdb_chiranjivi_no.setChecked(true);
                isChiranjivi = "0";
                break;
            case R.id.ed_high_risk_mom:

                showValidationDialog(womenHighRiskArrayList, ed_high_risk_mom,hivStatus);
                break;
            case R.id.bt_prg_women_reg:
                if (elapsedDays <= 84) {
                    isEarlyRegd = 1;
                } else {
                    isEarlyRegd = 0;
                }
                if (isAns.equals("1") && isEarlyRegd == 1) {
                    isKpsy = 1;
                } else {
                    isKpsy = 0;
                }

                if (member != null) {
                    if (member.getRaciald().equals("1") || member.getRaciald().equals("2") || isAns.equals("1")) {
                        isJsy = 1;
                    } else {
                        isJsy = 0;
                    }
                }

                savePregnantWoman();

                break;
        }
    }

    private void showDatePicker(int position) {
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
        if (position == 1) {
            date.setCallBack(regOndate);
        } else if (position == 2) {
            date.setCallBack(lmpDate);
        }
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener regOndate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {


            if (ed_lmp_date.getText().toString().length() != 0 && ed_anc_regd_date.getText().toString().length() != 0) {

                SimpleDateFormat simpleDateFormat =
                        new SimpleDateFormat("dd/M/yyyy");

                try {
//                    Date regDate = simpleDateFormat.parse(ed_lmp_date.getText().toString());
//                    Date lmpDate = simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
//                            + "/" + String.valueOf(year));
//                    long different = lmpDate.getTime() - regDate.getTime();
//                    long oneDay = 1000 * 60 * 60 * 24;
//                    elapsedDays = different / oneDay;
////                    Toast.makeText(thisActivity, "elapsedDays=" + elapsedDays, Toast.LENGTH_SHORT).show();

                    ed_anc_regd_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                            + "/" + String.valueOf(year));
                    ed_lmp_date.setText("");
                    ed_edd_date.setText("");
//                    if (elapsedDays >= 30) {
//                        ed_anc_regd_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
//                                + "/" + String.valueOf(year));
//
//                    } else {
//                        String str = thisActivity.getResources().getString(R.string.valid_lmp_date);
//                        CustomToast customToast = new CustomToast(thisActivity, str);
//                        customToast.show();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                ed_anc_regd_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));
            }
        }
    };

    DatePickerDialog.OnDateSetListener lmpDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            if (ed_anc_regd_date.getText().toString().length() != 0) {

                SimpleDateFormat simpleDateFormat =
                        new SimpleDateFormat("dd/M/yyyy");
                try {
                    Date regDate = simpleDateFormat.parse(ed_anc_regd_date.getText().toString());
                    Date lmpDate = simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                            + "/" + String.valueOf(year));
                    long different = regDate.getTime() - lmpDate.getTime();
                    long oneDay = 1000 * 60 * 60 * 24;
                    elapsedDays = different / oneDay;
//                    Toast.makeText(thisActivity, "elapsedDays=" + elapsedDays, Toast.LENGTH_SHORT).show();

                    if (elapsedDays >= 30 && elapsedDays <= 266 ) {

                        ed_lmp_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                + "/" + String.valueOf(year));

                        Calendar c = Calendar.getInstance();
                        Calendar c1 = Calendar.getInstance();
                        Calendar c2 = Calendar.getInstance();
                        Calendar c3 = Calendar.getInstance();
                        Calendar c4 = Calendar.getInstance();
                        try {
                            c.setTime(simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                    + "/" + String.valueOf(year)));
                            c1.setTime(simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                    + "/" + String.valueOf(year)));
                            c2.setTime(simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                    + "/" + String.valueOf(year)));
                            c3.setTime(simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                    + "/" + String.valueOf(year)));
                            c4.setTime(simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                    + "/" + String.valueOf(year)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE, 280);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                        c1.add(Calendar.DATE, 50);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                        c2.add(Calendar.DATE, 120);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                        c3.add(Calendar.DATE, 240);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                        c4.add(Calendar.DATE, 270);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE

                        String output = simpleDateFormat.format(c.getTime());
                        ancserviceDate1 = simpleDateFormat.format(c1.getTime());
                        ancserviceDate2 = simpleDateFormat.format(c2.getTime());
                        ancserviceDate3 = simpleDateFormat.format(c3.getTime());
                        ancserviceDate4 = simpleDateFormat.format(c4.getTime());
                        ed_edd_date.setText(output);

                    } else {
                        String str = thisActivity.getResources().getString(R.string.valid_lmp_date);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.sp_anc_gravida:
                if (position != 0 && position != 1) {
                    gravida = Integer.parseInt(parent.getSelectedItem().toString());
                    sp_anc_para.setSelection(0);
                    sp_anc_abortion.setSelection(0);
                } else {
                    sp_anc_gravida.setSelection(0);
                    gravida = 0;
                }
                break;
            case R.id.sp_anc_para:
                if (position != 0) {
                    int temp = 0;
                    para = Integer.parseInt(parent.getSelectedItem().toString());
                    int Total = para + abortion;
                    temp = gravida - 1;
                    if (Total == temp) {
                        sp_anc_abortion.setBackgroundColor(Color.GREEN);
                        sp_anc_gravida.setBackgroundColor(Color.GREEN);
                        sp_anc_para.setBackgroundColor(Color.GREEN);
                    } else {
                        sp_anc_para.setBackgroundColor(Color.RED);
                        sp_anc_abortion.setBackgroundColor(Color.RED);
                        sp_anc_gravida.setBackgroundColor(Color.RED);
                    }

                }
                break;
            case R.id.sp_anc_abortion:
                if (position != 0) {
                    int temp2 = 0;
                    abortion = Integer.parseInt(parent.getSelectedItem().toString());
                    int Total = para + abortion;
                    temp2 = gravida - 1;
                    if (temp2 == Total) {
                        sp_anc_abortion.setBackgroundColor(Color.GREEN);
                        sp_anc_gravida.setBackgroundColor(Color.GREEN);
                        sp_anc_para.setBackgroundColor(Color.GREEN);
                    } else {
                        sp_anc_para.setBackgroundColor(Color.RED);
                        sp_anc_abortion.setBackgroundColor(Color.RED);
                        sp_anc_gravida.setBackgroundColor(Color.RED);
                    }
                }
                break;
            case R.id.sp_live_male:
                if (position != 0) {
                    male = Integer.parseInt(parent.getSelectedItem().toString());
                    liveTotal = male + female;
                    if (liveTotal > para && female != 0) {
                        sp_live_male.setSelection(0);
                        sp_live_male.setBackgroundColor(Color.RED);
                        sp_live_female.setBackgroundColor(Color.RED);
                        sp_anc_para.setBackgroundColor(Color.RED);
                        String str = thisActivity.getResources().getString(R.string.valid_gravida);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    } else {
                        if (female != 0) {
                            sp_live_female.setBackgroundColor(Color.GREEN);
                        }
                        sp_live_male.setBackgroundColor(Color.GREEN);
                        sp_anc_para.setBackgroundColor(Color.GREEN);
                    }
                } else {
                    male = 0;
//                    if(sp_anc_para.getSelectedItemPosition()==1) {
//                        sp_live_male.setBackgroundColor(Color.RED);
//                    }
                }
                break;
            case R.id.sp_live_female:
                if (position != 0) {
                    female = Integer.parseInt(parent.getSelectedItem().toString());
                    liveTotal = male + female;
                    if (liveTotal > para && male != 0) {
                        sp_live_female.setSelection(0);
                        sp_live_male.setBackgroundColor(Color.RED);
                        sp_live_female.setBackgroundColor(Color.RED);
                        sp_anc_para.setBackgroundColor(Color.RED);
                        String str = thisActivity.getResources().getString(R.string.valid_gravida);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    } else {
                        if (male != 0) {
                            sp_live_male.setBackgroundColor(Color.GREEN);
                        }
                        sp_live_female.setBackgroundColor(Color.GREEN);
                        sp_anc_para.setBackgroundColor(Color.GREEN);
                    }
                } else {
                    female = 0;
//                    if(sp_anc_para.getSelectedItemPosition()==1) {
//                        sp_live_female.setBackgroundColor(Color.RED);
//                    }
                }
                break;
            case R.id.sp_asha_name:
                if (position != 0) {
                    LinearLayout linearLayout;
                    TextView textView;
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    ashaWorkerId = textView.getTag().toString();
                } else {
                    ashaWorkerId = null;
                }

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void savePregnantWoman() {

        SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
        String userDetail = sharedPreferences.getString(Constants.USER_ID, null);

        if (userDetail != null) {
            try {
                JSONObject jsonObject = new JSONObject(userDetail);

                pregnantWomen.setPregnantwomanregdId(txt_anc_regdnumber.getText().toString());
                pregnantWomen.setEmamtaId(member.getEmamtafamilyId());
                pregnantWomen.setMemberId(healthId);
                pregnantWomen.setMobile(ed_Mobile_number.getText().toString());
                pregnantWomen.setIsBpl(isAns);
                pregnantWomen.setAncDate(ed_anc_regd_date.getText().toString());
                pregnantWomen.setLmpDate(ed_lmp_date.getText().toString());
                pregnantWomen.setEddDate(ed_edd_date.getText().toString());
                if (sp_anc_gravida.getSelectedItemPosition() == 0) {
                    pregnantWomen.setGravida(0);
                } else {
                    pregnantWomen.setGravida(Integer.parseInt(sp_anc_gravida.getSelectedItem().toString()));
                }
                if (sp_anc_para.getSelectedItemPosition() == 0) {
                    pregnantWomen.setPara(0);
                } else {
                    pregnantWomen.setPara(Integer.parseInt(sp_anc_para.getSelectedItem().toString()));
                }
                if (sp_anc_abortion.getSelectedItemPosition() == 0) {
                    pregnantWomen.setAbortion(0);
                } else {
                    pregnantWomen.setAbortion(Integer.parseInt(sp_anc_abortion.getSelectedItem().toString()));
                }
                if (sp_live_male.getSelectedItemPosition() == 0) {
                    pregnantWomen.setMale(0);
                } else {
                    pregnantWomen.setMale(Integer.parseInt(sp_live_male.getSelectedItem().toString()));
                }
                if (sp_live_female.getSelectedItemPosition() == 0) {
                    pregnantWomen.setFemale(0);
                } else {
                    pregnantWomen.setFemale(Integer.parseInt(sp_live_female.getSelectedItem().toString()));
                }

                pregnantWomen.setHighRiskMother(ed_high_risk_mom.getText().toString());
                pregnantWomen.setIsChiranjivi(isChiranjivi);
                pregnantWomen.setAshaName(ashaWorkerId);
                pregnantWomen.setIsJsy("" + isJsy);
                pregnantWomen.setIsKpsy("" + isKpsy);
                pregnantWomen.setIsEarlyRegd("" + isEarlyRegd);
                pregnantWomen.setAge(age);
                pregnantWomen.setEmamtahealthId(healthId);

                /*if(ed_height.getText().toString().length()!=0){
                    pregnantWomen.setHeight(ed_height.getText().toString());
                }*/



               /* if(ed_weight.getText().toString().length()!=0){
                    pregnantWomen.setWeight(ed_weight.getText().toString());
                }*/

                if (ed_weight.getText().toString().trim().length() != 0) {
                    float weight = Float.parseFloat(ed_weight.getText().toString().trim());
                    if (weight > 25 && weight < 125) {
                        if (weight < 45) {
                            pregnantWomen.setWeight(ed_weight.getText().toString());
                            ed_weight.setBackgroundColor(Color.RED);
                            
                        } else {
                            pregnantWomen.setWeight(ed_weight.getText().toString());
                            ed_weight.setBackgroundColor(Color.GREEN);
                        }
                    } else {
                        ed_weight.setText("");
                        pregnantWomen.setWeight(ed_weight.getText().toString());
                    }
                }

                if (ed_weight.getText().toString().trim().length() != 0) {
                    float weight = Float.parseFloat(ed_weight.getText().toString().trim());
                    if (weight < 45) {
                        stringBuilderId.append("13");
                        prefixId = ",";
                        stringBuilderId.append(prefixId);
                    }
                }
                if(ed_height.getText().toString().trim().length() != 0){
                    float height = Float.parseFloat(ed_height.getText().toString().trim());
                    if (height <= 140) {
                        stringBuilderId.append("22");
                        prefixId = ",";
                        stringBuilderId.append(prefixId);
                    }
                }
                
                if (stringBuilderId.toString().length() >= 2) {
                    String riskId = stringBuilderId.toString().substring(0, stringBuilderId.toString().length() - 1);
                    ed_high_risk_mom.setTag(riskId);
                    pregnantWomen.setIsHighRisk("1");
                    pregnantWomen.setSymptomsId(ed_high_risk_mom.getTag().toString().split(","));
                }

                if (sp_blood_group_value.getSelectedItemPosition() != 0) {
                    pregnantWomen.setBloodGroup(sp_blood_group_value.getSelectedItem().toString());
                }

                if (sp_anc_para.getSelectedItemPosition() == 0 || sp_anc_abortion.getSelectedItemPosition() == 0) {
                    pregnantWomen.setTempTotal(0);
                } else {
                    pregnantWomen.setTempTotal(Integer.parseInt(sp_anc_para.getSelectedItem().toString()) + Integer.parseInt(sp_anc_abortion.getSelectedItem().toString()));
                }

                if (sp_live_male.getSelectedItemPosition() == 0 || sp_live_female.getSelectedItemPosition() == 0) {
                    pregnantWomen.setLiveTotal(0);
                } else {
                    pregnantWomen.setLiveTotal(Integer.parseInt(sp_live_male.getSelectedItem().toString()) + Integer.parseInt(sp_live_female.getSelectedItem().toString()));
                }

                if (ed_high_risk_mom.getText().length() != 0) {
                        pregnantWomen.setIsHighRisk("1");
                        pregnantWomen.setSymptomsId(ed_high_risk_mom.getTag().toString().split(","));
                } else {
                    pregnantWomen.setIsHighRisk("0");
                }
                if (ashaWorkerId != null) {
                    pregnantWomen.setEmployeeId(ashaWorkerId);
                }
                pregnantWomen.setUserId(jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId"));
                String[] array = new String[]{ancserviceDate1, ancserviceDate2, ancserviceDate3, ancserviceDate4};
                pregnantWomen.setAncServices(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (!member.getIsBpl().equals(isAns)) {
            DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
            boolean isSavePregnantWomanFamily = databaseHelper.updatePregnantWomanFamily(pregnantWomen);
        }

        if (imageRealPath != null) {
            if (!imageRealPath.equals(member.getPhotoValue()) || !member.getMobileNo().equals(pregnantWomen.getMobile())) {
                if (image_bitmap != null) {
                    pregnantWomen.setPhotoValue(imageRealPath);
                    Uri uri = Uri.parse(imageRealPath);
                    String Name = new File(uri.getPath()).getName();
                    pregnantWomen.setPhoto(Name);
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    boolean isSavePregnantWomanMember = databaseHelper.updatePregnantWomanMember(pregnantWomen);
                }
            }
        } else {
            if(member.getMobileNo()!=null){
                if (!member.getMobileNo().equals(pregnantWomen.getMobile())) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    boolean isSavePregnantWomanMember = databaseHelper.updatePregnantWomanMember(pregnantWomen);
                } else {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    boolean isSavePregnantWomanMember = databaseHelper.updatePregnantWomanMember(pregnantWomen);
                }
            }else{
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                boolean isSavePregnantWomanMember = databaseHelper.updatePregnantWomanMember(pregnantWomen);
            }

        }

        String validation = FormValidation.pregnantWomenRegister(pregnantWomen, thisActivity);
        if (validation.length() != 0) {
            stringBuilderId = new StringBuilder();
            CustomLoaderDialog customLoaderDialog = new CustomLoaderDialog(thisActivity);
            customLoaderDialog.showValidationDialog(validation);
        } else {
            DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
            boolean isExist = databaseHelper.checkPregnantWoman(member.getEmamtahealthId());
            if (isExist) {
                String str = thisActivity.getResources().getString(R.string.previous_pregnancy);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
                thisActivity.finish();
            } else {
                boolean isSavePregWomen = databaseHelper.insertNewPagnentWomen(pregnantWomen);
                boolean isHighRiskPregnantWomen = databaseHelper.insertHighRiskPregnantWomen(pregnantWomen);
                boolean isAncServices = databaseHelper.insertAncServices(pregnantWomen);
                String str = thisActivity.getResources().getString(R.string.thanks_for_pregnancy_reg);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
                thisActivity.finish();
            }
        }
    }

    public void showValidationDialog(ArrayList<WomenHighRisk> womenHighRiskArrayList, EditText ed_high_risk_mom, boolean hivStatus) {

        progressDialog = new Dialog(thisActivity, R.style.DialogTheme);
        LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressView = mInflater.inflate(R.layout.custom_highrisk_women, null);
        Utils.findAllTextView(thisActivity, (ViewGroup) progressView.findViewById(R.id.ll_high_risk_women));
        ExpandableListView expandableListView = (ExpandableListView) progressView.findViewById(R.id.exp_category);
        Button bt_save_high_risk = (Button) progressView.findViewById(R.id.bt_save_high_risk);

        ExpandablaListAdapter expandablaListAdapter = new ExpandablaListAdapter(thisActivity, womenHighRiskArrayList, ed_high_risk_mom,hivStatus);
        expandableListView.setAdapter(expandablaListAdapter);

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

        bt_save_high_risk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.dismiss();
            }
        });

    }

    public void captureImage() {

        imageName = "picture_" + "" + System.currentTimeMillis();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            mImageCaptureUri = Uri.fromFile(new File(getExternalFilesDir("temp"), imageName + ".png"));
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            Log.e(this + "", "cannot take picture " + e);
        } catch (Exception ex) {
            Log.e(this + "", "cannot take picture " + ex);
        }
    }

    private class CustomTextWatcher implements TextWatcher {
        public EditText editText;

        public CustomTextWatcher(EditText meditText) {
            this.editText = meditText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (charSequence.length() != 0) {

                switch (editText.getId()) {

                    case R.id.ed_weight:

                            float weight = Float.parseFloat(ed_weight.getText().toString().trim());
                            if (weight > 25 && weight < 125) {
                                if (weight < 45) {
                                    pregnantWomen.setWeight(ed_weight.getText().toString());
                                    ed_weight.setBackgroundColor(Color.RED);

                                } else {
                                    pregnantWomen.setWeight(ed_weight.getText().toString());
                                    ed_weight.setBackgroundColor(Color.GREEN);
                                }
                            } else {
                                ed_weight.setBackgroundColor(Color.CYAN);
                                pregnantWomen.setWeight("");
                            }

                        break;
                    case R.id.ed_height:

                            float height = Float.parseFloat(ed_height.getText().toString().trim());
                            if (height >= 75 && height <= 195) {
                                if (height <= 140) {
                                    ed_height.setBackgroundColor(Color.RED);
                                    pregnantWomen.setHeight(ed_height.getText().toString());

                                } else if (height > 140) {
                                    pregnantWomen.setHeight(ed_height.getText().toString());
                                    ed_height.setBackgroundColor(Color.GREEN);
                                }
                            } else {
                                ed_height.setBackgroundColor(Color.CYAN);
                                pregnantWomen.setHeight("");
                            }

                        break;
                }
            }
        }

        public void afterTextChanged(Editable s) {

        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == TAKE_PICTURE) {
                String selectedImagePath = new File(getExternalFilesDir("temp"),
                        imageName + ".png").getPath();
                ;
                Intent intent = new Intent(this, ImageCroppingActivity.class);
                intent.putExtra("imagePath", selectedImagePath);
                startActivityForResult(intent, CROP_PIC);

            } else if (requestCode == CROP_PIC) {

                txt_take_image.setVisibility(View.GONE);
                imgUserImage.setVisibility(View.VISIBLE);
                imageRealPath = data.getStringExtra("imagePath");
                Uri uri = Uri.parse(imageRealPath);
                image_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));

                imgUserImage.setImageBitmap(image_bitmap);
            }

        }
    }
}
