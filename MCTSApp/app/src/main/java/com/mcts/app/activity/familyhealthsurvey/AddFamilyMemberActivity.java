package com.mcts.app.activity.familyhealthsurvey;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.mcts.app.adapter.ExpandablaHistoryListAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.HighRiskSymtoms;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.Member;
import com.mcts.app.model.WomenHighRisk;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.DatePickerFragment;
import com.mcts.app.utils.FormValidation;
import com.mcts.app.utils.TakePictureUtils;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AddFamilyMemberActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Activity thisActivity;
    private static String TAG = "AddFamilyMemberActivity";
    private Toolbar mToolbar;
    private TextView mTitle, txt_family_number, txt_health_number, txt_take_image;
    private EditText ed_Name, ed_husband_name, ed_Sir_Name, ed_Birth_date, ed_Mobile_number,ed_health_problems;
    private RadioButton rdb_yes, rdb_no, rdb_sex_Male, rdb_sex_Female, rdb_second_child_yes, rdb_second_child_no, rdb_Current_Status_yes, rdb_Current_Status_no;
    private RadioButton rdb_adopt_planning_yes, rdb_adopt_planning_no, rdb_isPregnent_yes, rdb_isPregnent_no;
    private Spinner sp_family_head_relation, sp_Marital_status, sp_Family_welfare, sp_Family_welfare_user, sp_periods_status, sp_Whos_sun_daughter, sp_Whos_wife;
    private Button bt_family_identity, bt_bank_detail, bt_Please_Modern, bt_Cancel, bt_Please_migration;
    private TextView textTag;
    DatabaseHelper databaseHelper;
    int FamilyNumber;
    private int familyHealthNumber;
    private String isAns = "0", familyHeadRelation;
    private String gender, secondChild = "1", isLiving, maritalStatus, isAdoptPlanning = "0", isPregnent;
    String electionNo, panNo, drivingNo, passportNo;
    String bankName, branchName, acNumber, IFSCCode, aadharNumber;
    private LinearLayout ll_masik, ll_isPregnant, ll_Family_welfare_user, ll_Want_Family_welfare, ll_Family_welfare, ll_whose_wife, ll_secong_child;
    private Member familyMember;
    private ArrayList<WomenHighRisk> memberHistoryArrayList;

    //    Image Capture
    ImageView imgUserImage;
    private String imageName = "item_picture";
    public static final int TAKE_PICTURE = 1;
    private Uri fileUri;
    private String imageRealPath = null;
    private File compressFile;
    private Bitmap receipt_bitmap;
    private int width, height;
    private byte[] userImagebyteArray;
    private String villageId, villageName, MemberId;
    private String emamtaFamilyId;
    private String wifeOf, sunOf, familyWalfare, familyWalfareUser, periodeStatus;

    //croping img................................
    private final int CROP_PIC = 2;
    protected boolean circleCrop = false;
    private Dialog progressDialog;
    //...................................................


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family_member);
        setToolBar();
        init();
        setDropDownValue();
    }


    private void setToolBar() {

        thisActivity = AddFamilyMemberActivity.this;
        textTag = new TextView(thisActivity);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.add_family_member));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void init() {

        Random rand = new Random();
        familyHealthNumber = rand.nextInt(900000) + 100000;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        Intent intent = getIntent();
        emamtaFamilyId = intent.getStringExtra("emamtafamilyId");
        villageId = intent.getStringExtra("villageId");
        databaseHelper = new DatabaseHelper(thisActivity);
        familyMember = new Member();

        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_add_memmber));
        txt_family_number = (TextView) findViewById(R.id.txt_family_number);

        ll_masik = (LinearLayout) findViewById(R.id.ll_masik);
        ll_isPregnant = (LinearLayout) findViewById(R.id.ll_isPregnant);
        ll_Family_welfare_user = (LinearLayout) findViewById(R.id.ll_Family_welfare_user);
        ll_Want_Family_welfare = (LinearLayout) findViewById(R.id.ll_Want_Family_welfare);
        ll_Family_welfare = (LinearLayout) findViewById(R.id.ll_Family_welfare);
        ll_whose_wife = (LinearLayout) findViewById(R.id.ll_whose_wife);
        ll_secong_child = (LinearLayout) findViewById(R.id.ll_secong_child);

        txt_take_image = (TextView) findViewById(R.id.txt_take_image);
        imgUserImage = (ImageView) findViewById(R.id.imgUserImage);
        txt_health_number = (TextView) findViewById(R.id.txt_health_number);
        ed_Name = (EditText) findViewById(R.id.ed_Name);
        ed_husband_name = (EditText) findViewById(R.id.ed_husband_name);
        ed_Sir_Name = (EditText) findViewById(R.id.ed_Sir_Name);
        ed_Birth_date = (EditText) findViewById(R.id.ed_Birth_date);
        ed_Mobile_number = (EditText) findViewById(R.id.ed_Mobile_number);
        sp_Family_welfare = (Spinner) findViewById(R.id.sp_Family_welfare);
        rdb_yes = (RadioButton) findViewById(R.id.rdb_yes);
        rdb_no = (RadioButton) findViewById(R.id.rdb_no);
        rdb_sex_Male = (RadioButton) findViewById(R.id.rdb_sex_Male);
        rdb_sex_Female = (RadioButton) findViewById(R.id.rdb_sex_Female);
        rdb_second_child_yes = (RadioButton) findViewById(R.id.rdb_second_child_yes);
        rdb_second_child_no = (RadioButton) findViewById(R.id.rdb_second_child_no);
        rdb_Current_Status_yes = (RadioButton) findViewById(R.id.rdb_Current_Status_yes);
        rdb_Current_Status_no = (RadioButton) findViewById(R.id.rdb_Current_Status_no);
        rdb_adopt_planning_yes = (RadioButton) findViewById(R.id.rdb_adopt_planning_yes);
        rdb_adopt_planning_no = (RadioButton) findViewById(R.id.rdb_adopt_planning_no);
        rdb_isPregnent_yes = (RadioButton) findViewById(R.id.rdb_isPregnent_yes);
        rdb_isPregnent_no = (RadioButton) findViewById(R.id.rdb_isPregnent_no);
        sp_family_head_relation = (Spinner) findViewById(R.id.sp_family_head_relation);
        sp_periods_status = (Spinner) findViewById(R.id.sp_periods_status);
        sp_Marital_status = (Spinner) findViewById(R.id.sp_Marital_status);
        sp_Family_welfare_user = (Spinner) findViewById(R.id.sp_Family_welfare_user);
        sp_Whos_sun_daughter = (Spinner) findViewById(R.id.sp_Whos_sun_daughter);
        sp_Whos_wife = (Spinner) findViewById(R.id.sp_Whos_wife);
        bt_family_identity = (Button) findViewById(R.id.bt_family_identity);
        bt_bank_detail = (Button) findViewById(R.id.bt_bank_detail);
        bt_Please_Modern = (Button) findViewById(R.id.bt_Please_Modern);
        bt_Cancel = (Button) findViewById(R.id.bt_Cancel);
        bt_Please_migration = (Button) findViewById(R.id.bt_Please_migration);
        ed_health_problems = (EditText) findViewById(R.id.ed_health_problems);


        bt_Please_Modern.setOnClickListener(this);
        rdb_yes.setOnClickListener(this);
        rdb_no.setOnClickListener(this);
        rdb_sex_Male.setOnClickListener(this);
        rdb_sex_Female.setOnClickListener(this);
        rdb_second_child_yes.setOnClickListener(this);
        rdb_second_child_no.setOnClickListener(this);
        rdb_Current_Status_yes.setOnClickListener(this);
        rdb_Current_Status_no.setOnClickListener(this);
        rdb_adopt_planning_yes.setOnClickListener(this);
        rdb_adopt_planning_no.setOnClickListener(this);
        rdb_isPregnent_yes.setOnClickListener(this);
        rdb_isPregnent_no.setOnClickListener(this);
        bt_family_identity.setOnClickListener(this);
        bt_bank_detail.setOnClickListener(this);
        ed_Birth_date.setOnClickListener(this);
        imgUserImage.setOnClickListener(this);
        txt_take_image.setOnClickListener(this);
        ed_health_problems.setOnClickListener(this);


        txt_health_number.setText("" + familyHealthNumber);
        txt_family_number.setText("" + emamtaFamilyId);
    }

    private void setDropDownValue() {

        ArrayList<MaritalStatus> maritalStatusArrayList = databaseHelper.getMaritalStatus();
        if (maritalStatusArrayList != null) {
            StatusAdapter statusAdapter = new StatusAdapter(thisActivity, maritalStatusArrayList);
            sp_Marital_status.setAdapter(statusAdapter);
            sp_Marital_status.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> familyWalfareList = databaseHelper.getFamilyPlanningData();
        ArrayList<MaritalStatus> familyUserWalfareList = databaseHelper.getFamilyUserPlanningData();
        if (familyWalfareList != null) {
            StatusAdapter familyWalfareAdapter = new StatusAdapter(thisActivity, familyWalfareList);
            StatusAdapter familyUserWalfareAdapter = new StatusAdapter(thisActivity, familyUserWalfareList);
            sp_Family_welfare.setAdapter(familyWalfareAdapter);
            sp_Family_welfare_user.setAdapter(familyUserWalfareAdapter);


            sp_Family_welfare.setOnItemSelectedListener(this);
            sp_Family_welfare_user.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> periodStatusArray = databaseHelper.getPeriodeData();
        if (periodStatusArray != null) {
            StatusAdapter masikAdapter = new StatusAdapter(thisActivity, periodStatusArray);
            sp_periods_status.setAdapter(masikAdapter);
            sp_periods_status.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> relationArrayList = databaseHelper.getRelation();
        if (relationArrayList != null) {
            StatusAdapter relationAdapter = new StatusAdapter(thisActivity, relationArrayList);
            sp_family_head_relation.setAdapter(relationAdapter);
            sp_family_head_relation.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> sunOfArrayList = databaseHelper.getParentList(emamtaFamilyId);
        if (sunOfArrayList != null) {
            StatusAdapter sunDaughterAdapter = new StatusAdapter(thisActivity, sunOfArrayList);
            sp_Whos_sun_daughter.setAdapter(sunDaughterAdapter);
            sp_Whos_sun_daughter.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> wifeOfArrayList = databaseHelper.getWifeList(emamtaFamilyId);
        if (sunOfArrayList != null) {
            StatusAdapter sunDaughterAdapter = new StatusAdapter(thisActivity, wifeOfArrayList);
            sp_Whos_wife.setAdapter(sunDaughterAdapter);
            sp_Whos_wife.setOnItemSelectedListener(this);
        }

        ArrayList<WomenHighRisk> womenHighRiskArray = databaseHelper.getHistoryType();
        memberHistoryArrayList = databaseHelper.getMemberHistory(womenHighRiskArray);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_add_family_member, menu);
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
        Utils.ButtonClickEffect(v);
        switch (v.getId()) {
            case R.id.bt_Please_Modern:
                String name = ed_Name.getText().toString();
                familyMember.setMemberId(MemberId);
                familyMember.setEmamtafamilyId(emamtaFamilyId);
                familyMember.setEmamtahealthId(txt_health_number.getText().toString());
                familyMember.setFirstName(name);
                String husband_name = ed_husband_name.getText().toString();
                familyMember.setMiddleName(husband_name);
                String Sir_Name = ed_Sir_Name.getText().toString();
                familyMember.setLastName(Sir_Name);
                String isHead = isAns;
                familyMember.setIsHead("0");
                String headRelation = familyHeadRelation;
                familyMember.setRelationwithheadId(headRelation);
                String sex = gender;
                familyMember.setGender(sex);
                String marStatus = maritalStatus;
                familyMember.setMaritalStatus(marStatus);
                String bDate = ed_Birth_date.getText().toString();
                familyMember.setBirthDate(bDate);
                String moNumber = ed_Mobile_number.getText().toString();
                familyMember.setMobileNo(moNumber);
                String wife = wifeOf;
                familyMember.setWifeof(wife);
                String sun = sunOf;
                familyMember.setChildof(sun);
                String Walfare = familyWalfare;
                familyMember.setAdoptedfpMethod(Walfare);
                String wantFamilyWalfare = isAdoptPlanning;
                familyMember.setWantadoptedfpMethod(wantFamilyWalfare);
                String WalfareUser = familyWalfareUser;
                familyMember.setPlannedfpMethod(WalfareUser);
                String pragnent = isPregnent;
                familyMember.setIsPregnant(pragnent);
                String child = secondChild;
                familyMember.setWantChild(child);
                String memStatus = isLiving;
                familyMember.setMemberStatus(memStatus);
                String prdStatus = periodeStatus;
                if(receipt_bitmap!=null) {
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    image_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    userImagebyteArray = stream.toByteArray();
//                    Log.v(TAG, "Image length" + userImagebyteArray.length);
                    familyMember.setPhotoValue(imageRealPath);
                    Uri uri=Uri.parse(imageRealPath);
                    String Name=new File(uri.getPath()).getName();
                    familyMember.setPhoto(Name);
                }
//                if (userImagebyteArray != null) {
//                    familyMember.setUserImageArray(userImagebyteArray);
//                }

                String validateAddFamilyDetailForm = FormValidation.validateFamilyMemberRegistrationForm(familyMember, this);
                if(validateAddFamilyDetailForm.length()!=0) {
                    CustomLoaderDialog customLoaderDialog = new CustomLoaderDialog(thisActivity);
                    customLoaderDialog.showValidationDialog(validateAddFamilyDetailForm);
                }else {
                    SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                    String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                    try {
                        JSONObject jsonObject = new JSONObject(userDetail);
                        familyMember.setSubCenterId(jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        String userId = jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");
                        familyMember.setUserId(userId);
                        familyMember.setVillageId(villageId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    boolean flag = databaseHelper.insertNewMember(familyMember);
                    if (flag) {

                        if (ed_health_problems.getText().length() != 0) {
                            String[] historyArray = ed_health_problems.getTag().toString().split(",");
                            String[] yeasArray = textTag.getTag().toString().split(",");
                            DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                            boolean flagHistory = databaseHelper.insertMemberHistory(familyMember, historyArray, yeasArray);

                            if(flagHistory){
                                String str=thisActivity.getResources().getString(R.string.member_new_success);
                                CustomToast customToast=new CustomToast(thisActivity,str);
                                customToast.show();
                                thisActivity.finish();
                            }

                        }

                    }
                }

                break;
            case R.id.rdb_yes:
                rdb_yes.setChecked(true);
                rdb_no.setChecked(false);
                isAns = "0";
                break;
            case R.id.rdb_no:
                rdb_yes.setChecked(false);
                rdb_no.setChecked(true);
                isAns = "0";
                break;
            case R.id.rdb_sex_Male:
                rdb_sex_Male.setChecked(true);
                rdb_sex_Female.setChecked(false);
                gender = "M";
                ll_masik.setVisibility(View.GONE);
                ll_isPregnant.setVisibility(View.GONE);
//                ll_Family_welfare.setVisibility(View.GONE);
//                ll_Family_welfare_user.setVisibility(View.GONE);
//                ll_Want_Family_welfare.setVisibility(View.GONE);
                ll_whose_wife.setVisibility(View.GONE);
                ll_secong_child.setVisibility(View.GONE);
                break;
            case R.id.rdb_sex_Female:
                rdb_sex_Male.setChecked(false);
                rdb_sex_Female.setChecked(true);
                ll_masik.setVisibility(View.VISIBLE);
                ll_isPregnant.setVisibility(View.VISIBLE);
//                ll_Family_welfare.setVisibility(View.VISIBLE);
//                ll_Family_welfare_user.setVisibility(View.VISIBLE);
//                ll_Want_Family_welfare.setVisibility(View.VISIBLE);
                ll_whose_wife.setVisibility(View.VISIBLE);
                ll_secong_child.setVisibility(View.VISIBLE);
                gender = "F";
                break;
            case R.id.rdb_second_child_yes:
                rdb_second_child_yes.setChecked(true);
                rdb_second_child_no.setChecked(false);
                secondChild = "1";
                break;
            case R.id.rdb_second_child_no:
                rdb_second_child_yes.setChecked(false);
                rdb_second_child_no.setChecked(true);
                secondChild = "0";
                break;
            case R.id.rdb_Current_Status_yes:
                rdb_Current_Status_yes.setChecked(true);
                rdb_Current_Status_no.setChecked(false);
                isLiving = "1";
                break;
            case R.id.rdb_Current_Status_no:
                rdb_Current_Status_yes.setChecked(false);
                rdb_Current_Status_no.setChecked(true);
                isLiving = "0";
                break;
            case R.id.rdb_adopt_planning_yes:
                isAdoptPlanning = "1";
                rdb_adopt_planning_yes.setChecked(true);
                rdb_adopt_planning_no.setChecked(false);
                ll_Family_welfare_user.setVisibility(View.VISIBLE);
                break;
            case R.id.rdb_adopt_planning_no:
                isAdoptPlanning = "0";
                rdb_adopt_planning_yes.setChecked(false);
                rdb_adopt_planning_no.setChecked(true);
                ll_Family_welfare_user.setVisibility(View.GONE);
                break;
            case R.id.rdb_isPregnent_yes:
                isPregnent = "1";
                rdb_isPregnent_yes.setChecked(true);
                rdb_isPregnent_no.setChecked(false);
                ll_secong_child.setVisibility(View.GONE);
                break;
            case R.id.rdb_isPregnent_no:
                isPregnent = "0";
                rdb_isPregnent_yes.setChecked(false);
                rdb_isPregnent_no.setChecked(true);
                ll_secong_child.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_family_identity:
                final Dialog dialog = new Dialog(thisActivity);
                LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = mInflater.inflate(R.layout.family_identity_member_layout, null);
                Utils.findAllTextView(thisActivity, ((ViewGroup) view.findViewById(R.id.ll_alert)));
                final EditText ed_election_no = (EditText) view.findViewById(R.id.ed_election_no);
                final EditText ed_pan_no = (EditText) view.findViewById(R.id.ed_pan_no);
                final EditText ed_driving_no = (EditText) view.findViewById(R.id.ed_driving_no);
                final EditText ed_passport_no = (EditText) view.findViewById(R.id.ed_passport_no);
                final EditText ed_aadhar_no = (EditText) view.findViewById(R.id.ed_aadhar_no);
                Button bt_save = (Button) view.findViewById(R.id.bt_save);
                Button bt_identity_cancel = (Button) view.findViewById(R.id.bt_identity_cancel);

                if(familyMember.getElectioncardNumber()!=null){
                    ed_election_no.setText(familyMember.getElectioncardNumber());
                }
                if(familyMember.getPancardNumber()!=null) {
                    ed_pan_no.setText(familyMember.getPancardNumber());
                }
                if(familyMember.getDrivingcardNumer()!=null) {
                    ed_driving_no.setText(familyMember.getDrivingcardNumer());
                }
                if(familyMember.getDrivingcardNumer()!=null) {
                    ed_passport_no.setText(familyMember.getDrivingcardNumer());
                }
                if(familyMember.getAadharNo()!=null) {
                    ed_aadhar_no.setText(familyMember.getAadharNo());
                }

                bt_identity_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        electionNo = ed_election_no.getText().toString();
                        panNo = ed_pan_no.getText().toString();
                        drivingNo = ed_driving_no.getText().toString();
                        passportNo = ed_passport_no.getText().toString();
                        aadharNumber = ed_aadhar_no.getText().toString();

                        if(!electionNo.equalsIgnoreCase(familyMember.getElectioncardNumber())){
                            familyMember.setElectioncardNumber(electionNo);
                        }
                        if(!panNo.equalsIgnoreCase(familyMember.getPancardNumber())) {
                            familyMember.setPancardNumber(panNo);
                        }
                        if(!drivingNo.equalsIgnoreCase(familyMember.getDrivingcardNumer())) {
                            familyMember.setDrivingcardNumer(drivingNo);
                        }
                        if(!passportNo.equalsIgnoreCase(familyMember.getPassportcardNumber())) {
                            familyMember.setPassportcardNumber(passportNo);
                        }
                        if(!aadharNumber.equalsIgnoreCase(familyMember.getAadharNo())) {
                            familyMember.setAadharNo(aadharNumber);
                        }

                    }
                });
                bt_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        electionNo = ed_election_no.getText().toString();
                        panNo = ed_pan_no.getText().toString();
                        drivingNo = ed_driving_no.getText().toString();
                        passportNo = ed_passport_no.getText().toString();
                        aadharNumber = ed_aadhar_no.getText().toString();
                        familyMember.setElectioncardNumber(electionNo);
                        familyMember.setPancardNumber(panNo);
                        familyMember.setDrivingcardNumer(drivingNo);
                        familyMember.setPassportcardNumber(passportNo);
                        familyMember.setAadharNo(aadharNumber);
                    }
                });


                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(view);

                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
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
                lp.copyFrom(dialog.getWindow().getAttributes());

                lp.width = width;
                lp.height = height;
                dialog.getWindow().setAttributes(lp);
                dialog.setCancelable(false);
                dialog.show();
                break;
            case R.id.bt_bank_detail:

                final Dialog bankDialog = new Dialog(thisActivity);
                LayoutInflater bankInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View bankView=bankInflater.inflate(R.layout.bank_detail_layout, null);
                Utils.findAllTextView(thisActivity, ((ViewGroup) bankView.findViewById(R.id.ll_alert)));
                final EditText ed_bank_name=(EditText)bankView.findViewById(R.id.ed_bank_name);
                final EditText ed_branch_name=(EditText)bankView.findViewById(R.id.ed_branch_name);
                final EditText ed_ac_number=(EditText)bankView.findViewById(R.id.ed_ac_number);
                final EditText ed_IFSC_Code=(EditText)bankView.findViewById(R.id.ed_IFSC_Code);
                final EditText bed_aadhar_no=(EditText)bankView.findViewById(R.id.ed_aadhar_no);
                Button bt_save_bank=(Button)bankView.findViewById(R.id.bt_save_bank);
                Button bt_cancel_bank=(Button)bankView.findViewById(R.id.bt_cancel_bank);

                if(familyMember.getBankName()!=null){
                    ed_bank_name.setText(familyMember.getBankName());
                }
                if(familyMember.getBranchName()!=null) {
                    ed_branch_name.setText(familyMember.getBranchName());
                }
                if(familyMember.getAccountNo()!=null) {
                    ed_ac_number.setText(familyMember.getAccountNo());
                }
                if(familyMember.getIfscCode()!=null) {
                    ed_IFSC_Code.setText(familyMember.getIfscCode());
                }
                if(familyMember.getAadharNo()!=null) {
                    bed_aadhar_no.setText(familyMember.getAadharNo());
                }

                bt_save_bank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.ButtonClickEffect(v);
                        bankDialog.dismiss();
                        bankName =ed_bank_name.getText().toString();
                        branchName =ed_branch_name.getText().toString();
                        acNumber =ed_ac_number.getText().toString();
                        IFSCCode=ed_IFSC_Code.getText().toString();
                        aadharNumber=bed_aadhar_no.getText().toString();
                        familyMember.setBankName(bankName);
                        familyMember.setBranchName(branchName);
                        familyMember.setAccountNo(acNumber);
                        familyMember.setIfscCode(IFSCCode);
                        familyMember.setAadharNo(aadharNumber);
                    }
                });

                bt_cancel_bank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.ButtonClickEffect(v);
                        bankDialog.dismiss();

                        bankName =ed_bank_name.getText().toString();
                        branchName =ed_branch_name.getText().toString();
                        acNumber =ed_ac_number.getText().toString();
                        IFSCCode=ed_IFSC_Code.getText().toString();
                        aadharNumber=bed_aadhar_no.getText().toString();

                        if(!bankName.equalsIgnoreCase(familyMember.getBankName())){
                            familyMember.setBankName(bankName);
                        }
                        if(!branchName.equalsIgnoreCase(familyMember.getBranchName())) {
                            familyMember.setBranchName(branchName);
                        }
                        if(!acNumber.equalsIgnoreCase(familyMember.getAccountNo())) {
                            familyMember.setAccountNo(acNumber);
                        }
                        if(!IFSCCode.equalsIgnoreCase(familyMember.getIfscCode())) {
                            familyMember.setIfscCode(IFSCCode);
                        }
                        if(!aadharNumber.equalsIgnoreCase(familyMember.getAadharNo())) {
                            familyMember.setAadharNo(aadharNumber);
                        }
                    }
                });

                bankDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                bankDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                bankDialog.setContentView(bankView);

                WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
                Display bankDisplay = windowManager.getDefaultDisplay(); // getting the screen size of device
                Point bankSize = new Point();
                bankDisplay.getSize(bankSize);
                int bankwidth1 = WindowManager.LayoutParams.WRAP_CONTENT;
                int bankheight1 = WindowManager.LayoutParams.WRAP_CONTENT;

                int bankTempValue = 0;
                bankTempValue = ((bankSize.x) * 200) / 1440;
                int bankwidth = bankSize.x - bankTempValue;  // Set your widths
                int bankheight = bankheight1; // set your heights

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(bankDialog.getWindow().getAttributes());

                layoutParams.width = bankwidth;
                layoutParams.height = bankheight;
                bankDialog.getWindow().setAttributes(layoutParams);
                bankDialog.setCancelable(false);
                bankDialog.show();
                break;

            case R.id.ed_Birth_date:
                showDatePicker();
                break;
            case R.id.imgUserImage:
                captureImage();
                break;
            case R.id.txt_take_image:
                captureImage();
                break;
            case R.id.ed_health_problems:
                memberHistory(memberHistoryArrayList, ed_health_problems, textTag);
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

            ed_Birth_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                    + "/" + String.valueOf(year));
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position!=0) {
            LinearLayout linearLayout;
            TextView textView;
            switch (parent.getId()) {
                case R.id.sp_family_head_relation:
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    familyHeadRelation = textView.getTag().toString();
                    break;
                case R.id.sp_Marital_status:
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    maritalStatus = textView.getTag().toString();
                    break;
                case R.id.sp_Whos_wife:
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    wifeOf = textView.getTag().toString();
                    break;
                case R.id.sp_Whos_sun_daughter:
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    sunOf = textView.getTag().toString();
                    break;
                case R.id.sp_Family_welfare:
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    familyWalfare = textView.getTag().toString();
                    break;
                case R.id.sp_Family_welfare_user:
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    familyWalfareUser = textView.getTag().toString();
                    break;
                case R.id.sp_periods_status:
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    periodeStatus = textView.getTag().toString();
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void memberHistory(ArrayList<WomenHighRisk> womenHighRiskArrayList, EditText ed_high_risk_mom, TextView textTag) {

        progressDialog = new Dialog(thisActivity, R.style.DialogTheme);
        LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressView = mInflater.inflate(R.layout.custom_highrisk_women, null);
        Utils.findAllTextView(thisActivity, (ViewGroup) progressView.findViewById(R.id.ll_high_risk_women));
        ExpandableListView expandableListView = (ExpandableListView) progressView.findViewById(R.id.exp_category);
        TextView txt_validation = (TextView) progressView.findViewById(R.id.txt_validation);
        Button bt_save_high_risk = (Button) progressView.findViewById(R.id.bt_save_high_risk);
        txt_validation.setText(thisActivity.getResources().getText(R.string.health_problems));

        ExpandablaHistoryListAdapter expandablaListAdapter = new ExpandablaHistoryListAdapter(thisActivity, womenHighRiskArrayList, ed_high_risk_mom, textTag);
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


                boolean flag = false;
                for (int i = 0; i < memberHistoryArrayList.size(); i++) {
                    for (int j = 0; j < memberHistoryArrayList.get(i).getHighRiskSymtomsArrayList().size(); j++) {
                        HighRiskSymtoms highRiskSymtoms = memberHistoryArrayList.get(i).getHighRiskSymtomsArrayList().get(j);
                        if (highRiskSymtoms.getIsChecked().equals("1")) {
                            if(highRiskSymtoms.getYear() != null){
                                flag = true;
                            }else{
                                flag = false;
                            }
                        }
                    }
                }
                if (!flag) {
                    String str = thisActivity.getResources().getString(R.string.insert_year);
                    CustomToast customToast = new CustomToast(thisActivity, str);
                    customToast.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });

    }

    public void captureImage() {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(Constants.MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, TAKE_PICTURE);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(Utils.getOutputMediaFile(type));
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PICTURE) {

//                String selectedImagePath =  new File(getExternalFilesDir("temp"),
//                        imageName + ".png").getPath();;
                String selectedImagePath =  fileUri.getPath();
                Intent intent = new Intent(this, ImageCroppingActivity.class);
                intent.putExtra("imagePath", selectedImagePath);
                startActivityForResult(intent, CROP_PIC);

            } else if (requestCode == CROP_PIC) {

                File file = new File(fileUri.getPath());
                boolean deleted = file.delete();

                txt_take_image.setVisibility(View.GONE);
                imgUserImage.setVisibility(View.VISIBLE);
                imageRealPath=data.getStringExtra("imagePath");
                Uri uri=Uri.parse(imageRealPath);
                receipt_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));

                imgUserImage.setImageBitmap(receipt_bitmap);
            }

        }
    }

}
