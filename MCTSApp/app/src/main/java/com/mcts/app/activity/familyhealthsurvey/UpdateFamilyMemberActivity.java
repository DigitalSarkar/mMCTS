package com.mcts.app.activity.familyhealthsurvey;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class UpdateFamilyMemberActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Activity thisActivity;
    private static String TAG = "UpdateFamilyMemberActivity";
    private Toolbar mToolbar;
    private TextView mTitle, txt_family_number, txt_health_number, txt_take_image;
    private EditText ed_Name, ed_husband_name, ed_Sir_Name, ed_Birth_date, ed_Mobile_number, ed_health_problems;
    private RadioButton rdb_yes, rdb_no, rdb_sex_Male, rdb_sex_Female, rdb_second_child_yes, rdb_second_child_no, rdb_Current_Status_yes, rdb_Current_Status_no;
    private RadioButton rdb_adopt_planning_yes, rdb_adopt_planning_no, rdb_isPregnent_yes, rdb_isPregnent_no;
    private Spinner sp_family_head_relation, sp_Marital_status, sp_Family_welfare, sp_Family_welfare_user, sp_periods_status, sp_Whos_sun_daughter, sp_Whos_wife;
    private Button bt_family_identity, bt_bank_detail, bt_Please_Modern, bt_Cancel, bt_Please_migration;
    DatabaseHelper databaseHelper;
    private TextView textTag;
    int FamilyNumber;
    private int familyHealthNumber;
    private String isAns = "0", familyHeadRelation;
    private String gender, secondChild = "1", isLiving, maritalStatus, isAdoptPlanning = "0", isPregnent;
    String electionNo, panNo, drivingNo, passportNo;
    String bankName, branchName, acNumber, IFSCCode, aadharNumber;
    private LinearLayout ll_masik, ll_isPregnant, ll_Family_welfare_user, ll_Want_Family_welfare, ll_Family_welfare, ll_whose_wife, ll_secong_child;
    private Member member, familyMember;

    //    Image Capture
    ImageView imgUserImage;
    private String imageName = "item_picture";
    public static final int TAKE_PICTURE = 1;
    private final int CROP_PIC = 2;
    private Uri picUri;
    private String imageRealPath = null;
    private File compressFile;
    private Bitmap image_bitmap;
    private int width, height;
    private byte[] userImagebyteArray;
    private String villageId, villageName, MemberId;
    private String emamtaFamilyId;
    private String wifeOf, sunOf, familyWalfare, familyWalfareUser, periodeStatus;
    private Uri fileUri;
    private Dialog progressDialog;
    private ArrayList<WomenHighRisk> memberHistoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_family_member);

        setToolBar();
        init();
        getMemberData();
        setDropDownValue();

        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_add_memmber));
    }


    private void setToolBar() {

        thisActivity = UpdateFamilyMemberActivity.this;
        textTag = new TextView(thisActivity);
        Utils.hideSoftKeyboard(thisActivity);
        Intent intent = getIntent();
        villageId = intent.getStringExtra("villageId");
        villageName = intent.getStringExtra("villageName");
        MemberId = intent.getStringExtra("MemberId");
        emamtaFamilyId = intent.getStringExtra("emamtaFamilyId");

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.edit_family_member));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void init() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        familyMember = new Member();
        Intent intent = getIntent();
        FamilyNumber = intent.getIntExtra("FamilyNumber", 0);
        databaseHelper = new DatabaseHelper(thisActivity);
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

        txt_take_image.setVisibility(View.VISIBLE);
        imgUserImage.setVisibility(View.GONE);

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


    }

    private void getMemberData() {

        DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
        member = databaseHelper.getFamilyMember(MemberId);

        if (member != null) {

            txt_family_number.setText(member.getEmamtafamilyId());
            txt_health_number.setText(member.getEmamtahealthId());
            ed_Name.setText(member.getFirstName());
            ed_husband_name.setText(member.getMiddleName());
            ed_Sir_Name.setText(member.getLastName());
            ed_Birth_date.setText(member.getBirthDate());
            ed_Mobile_number.setText(member.getMobileNo());

            if (member.getPhoto() != null) {
                if (member.getPhoto().length() > 5) {
                    txt_take_image.setVisibility(View.GONE);
                    imgUserImage.setVisibility(View.VISIBLE);
                    Uri uri = Uri.parse(member.getPhotoValue());
                    Bitmap image_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));
                    imgUserImage.setImageBitmap(image_bitmap);
                }
            }

            if (member.getIsHead() != null) {
                if (member.getIsHead().equals("1")) {
                    rdb_yes.setChecked(true);
                    rdb_no.setChecked(false);
                    isAns = "1";
                } else {
                    rdb_yes.setChecked(false);
                    rdb_no.setChecked(true);
                    isAns = "0";
                }
            }

            if (member.getWantadoptedfpMethod() != null) {
                if (member.getWantadoptedfpMethod().equals("1")) {
                    isAdoptPlanning = "1";
                    rdb_adopt_planning_yes.setChecked(true);
                    rdb_adopt_planning_no.setChecked(false);
                    ll_Family_welfare_user.setVisibility(View.VISIBLE);
                } else {
                    isAdoptPlanning = "0";
                    rdb_adopt_planning_yes.setChecked(false);
                    rdb_adopt_planning_no.setChecked(true);
                    ll_Family_welfare_user.setVisibility(View.GONE);
                }
            }
            if (member.getIsPregnant() != null) {
                if (member.getIsPregnant().equals("1")) {
                    isPregnent = "1";
                    rdb_isPregnent_yes.setChecked(true);
                    rdb_isPregnent_no.setChecked(false);
                    ll_secong_child.setVisibility(View.GONE);
                } else {
                    isPregnent = "0";
                    rdb_isPregnent_yes.setChecked(false);
                    rdb_isPregnent_no.setChecked(true);
                    ll_secong_child.setVisibility(View.VISIBLE);
                }
            }

            if (member.getWantChild() != null) {
                if (member.getWantChild().equals("1")) {
                    secondChild = "1";
                    rdb_second_child_yes.setChecked(true);
                    rdb_second_child_no.setChecked(false);
                } else {
                    secondChild = "0";
                    rdb_second_child_yes.setChecked(false);
                    rdb_second_child_no.setChecked(true);
                }
            }

            if (member.getMemberStatus() != null) {
                if (member.getMemberStatus().equals("1")) {
                    rdb_Current_Status_yes.setChecked(true);
                    rdb_Current_Status_no.setChecked(false);
                    isLiving = "1";
                } else {
                    rdb_Current_Status_yes.setChecked(false);
                    rdb_Current_Status_no.setChecked(true);
                    isLiving = "0";
                }
            }

            if (member.getGender().equals("M")) {
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
            } else {
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
            }
        }
    }

    private void setDropDownValue() {

        ArrayList<WomenHighRisk> womenHighRiskArray = databaseHelper.getHistoryType();
        ArrayList<HighRiskSymtoms> highRiskSymtomsArrayList = databaseHelper.getMemberExistHistory(member.getMemberId());
        memberHistoryArrayList = databaseHelper.getMemberHistory(womenHighRiskArray);
        if (highRiskSymtomsArrayList != null) {
            ArrayList<String> ids=new ArrayList<>();
            for (int p = 0; p < highRiskSymtomsArrayList.size(); p++) {
                ids.add(highRiskSymtomsArrayList.get(p).getSymptomId());
            }
            setHighRiskDetails(ids,highRiskSymtomsArrayList);

        }

        ArrayList<MaritalStatus> maritalStatusArrayList = databaseHelper.getMaritalStatus();
        if (maritalStatusArrayList != null) {
            StatusAdapter statusAdapter = new StatusAdapter(thisActivity, maritalStatusArrayList);
            sp_Marital_status.setAdapter(statusAdapter);

            if (member.getMaritalStatus() != null) {
//                int faliyaId = Integer.parseInt(member.getFamilyId());
                for (int i = 0; i < maritalStatusArrayList.size(); i++) {
                    if (member.getMaritalStatus().equalsIgnoreCase(maritalStatusArrayList.get(i).getId())) {
                        sp_Marital_status.setSelection(i);
//                        if(maritalStatusArrayList.get(i).getId().equals("1")){
//                            ll_masik.setVisibility(View.VISIBLE);
//                            ll_isPregnant.setVisibility(View.VISIBLE);
//                            ll_Family_welfare.setVisibility(View.VISIBLE);
//                            ll_Family_welfare_user.setVisibility(View.VISIBLE);
//                            ll_Want_Family_welfare.setVisibility(View.VISIBLE);
//                            ll_whose_wife.setVisibility(View.VISIBLE);
//                            ll_secong_child.setVisibility(View.VISIBLE);
//                        }
                    }
                }
            } else {
                sp_Marital_status.setSelection(0);
            }

            sp_Marital_status.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> familyWalfareList = databaseHelper.getFamilyPlanningData();
        if (familyWalfareList != null) {
            StatusAdapter familyWalfareAdapter = new StatusAdapter(thisActivity, familyWalfareList);
            sp_Family_welfare.setAdapter(familyWalfareAdapter);
            sp_Family_welfare_user.setAdapter(familyWalfareAdapter);

            if (member.getPlannedfpMethod() != null) {
//                int faliyaId = Integer.parseInt(member.getFamilyId());
                for (int i = 0; i < familyWalfareList.size(); i++) {
                    if (member.getPlannedfpMethod().equalsIgnoreCase(familyWalfareList.get(i).getId())) {
                        sp_Family_welfare_user.setSelection(i);
                    }
                }
            } else {
                sp_Family_welfare_user.setSelection(0);
            }

            if (member.getAdoptedfpMethod() != null) {
//                int faliyaId = Integer.parseInt(member.getFamilyId());
                for (int i = 0; i < familyWalfareList.size(); i++) {
                    if (member.getAdoptedfpMethod().equalsIgnoreCase(familyWalfareList.get(i).getId())) {
                        sp_Family_welfare.setSelection(i);
                    }
                }
            } else {
                sp_Family_welfare.setSelection(0);
            }

            sp_Family_welfare.setOnItemSelectedListener(this);
            sp_Family_welfare_user.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> periodStatusArray = databaseHelper.getPeriodeData();
        if (periodStatusArray != null) {
            StatusAdapter masikAdapter = new StatusAdapter(thisActivity, periodStatusArray);
            sp_periods_status.setAdapter(masikAdapter);

            if (member.getMenstruationStatus() != null) {
//                int faliyaId = Integer.parseInt(member.getFamilyId());
                for (int i = 0; i < periodStatusArray.size(); i++) {
                    if (member.getMenstruationStatus().equalsIgnoreCase(periodStatusArray.get(i).getId())) {
                        sp_periods_status.setSelection(i);
                    }
                }
            } else {
                sp_periods_status.setSelection(0);
            }

            sp_periods_status.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> relationArrayList = databaseHelper.getRelation();
        if (relationArrayList != null) {
            StatusAdapter relationAdapter = new StatusAdapter(thisActivity, relationArrayList);
            sp_family_head_relation.setAdapter(relationAdapter);

            if (member.getRelationwithheadId() != null) {
//                int faliyaId = Integer.parseInt(member.getFamilyId());
                for (int i = 0; i < relationArrayList.size(); i++) {
                    if (member.getRelationwithheadId().equalsIgnoreCase(relationArrayList.get(i).getId())) {
                        sp_family_head_relation.setSelection(i);
                    }
                }
            } else {
                sp_family_head_relation.setSelection(0);
            }

            sp_family_head_relation.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> sunOfArrayList = databaseHelper.getParentList(emamtaFamilyId);
        if (sunOfArrayList != null) {
            StatusAdapter sunDaughterAdapter = new StatusAdapter(thisActivity, sunOfArrayList);
            sp_Whos_sun_daughter.setAdapter(sunDaughterAdapter);

            if (member.getChildof() != null) {
//                int faliyaId = Integer.parseInt(member.getFamilyId());
                for (int i = 0; i < sunOfArrayList.size(); i++) {
                    if (member.getChildof().equalsIgnoreCase(sunOfArrayList.get(i).getId())) {
                        sp_Whos_sun_daughter.setSelection(i);
                    }
                }
            } else {
                sp_Whos_sun_daughter.setSelection(0);
            }

            sp_Whos_sun_daughter.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> wifeOfArrayList = databaseHelper.getWifeList(emamtaFamilyId);
        if (sunOfArrayList != null) {
            StatusAdapter sunDaughterAdapter = new StatusAdapter(thisActivity, wifeOfArrayList);
            sp_Whos_wife.setAdapter(sunDaughterAdapter);

            if (member.getWifeof() != null) {
//                int faliyaId = Integer.parseInt(member.getFamilyId());
                for (int i = 0; i < wifeOfArrayList.size(); i++) {
                    if (member.getWifeof().equalsIgnoreCase(wifeOfArrayList.get(i).getId())) {
                        sp_Whos_wife.setSelection(i);
                    }
                }
            } else {
                sp_Whos_wife.setSelection(0);
            }

            sp_Whos_wife.setOnItemSelectedListener(this);
        }
    }

    private void setHighRiskDetails(ArrayList<String> ids, ArrayList<HighRiskSymtoms> highRiskSymtomsArrayList) {

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilderId=new StringBuilder();
        StringBuilder stringBuilderDays=new StringBuilder();
        String prefix = "";
        String prefixId = "";
        String prefixDays = "";

        for (int j = 0; j < memberHistoryArrayList.size(); j++) {
            WomenHighRisk womenHighRisk = memberHistoryArrayList.get(j);
//                    do {
            for (int i = 0; i < womenHighRisk.getHighRiskSymtomsArrayList().size(); i++) {

                for (int p = 0; p < highRiskSymtomsArrayList.size(); p++) {
                    if (highRiskSymtomsArrayList.get(p).getSymptomId().equals(womenHighRisk.getHighRiskSymtomsArrayList().get(i).getSymptomId())) {
                        HighRiskSymtoms highRiskSymtoms = memberHistoryArrayList.get(j).getHighRiskSymtomsArrayList().get(i);
                        highRiskSymtoms.setIsChecked("1");

                        Calendar currentDate = Calendar.getInstance();
                        int fromYear = currentDate.get(Calendar.YEAR);
                        int toYear = Integer.parseInt(highRiskSymtomsArrayList.get(p).getYear());
                        int year = fromYear - toYear;

                        highRiskSymtoms.setYear("" + year);
                        memberHistoryArrayList.get(j).getHighRiskSymtomsArrayList().set(i, highRiskSymtoms);
                        womenHighRisk.setHighRiskSymtomsArrayList(memberHistoryArrayList.get(j).getHighRiskSymtomsArrayList());

                        stringBuilder.append(highRiskSymtoms.getSymptomName());
                        prefix = ",";
                        stringBuilder.append(prefix);


                        stringBuilderId.append(highRiskSymtoms.getSymptomId());
                        stringBuilderDays.append(highRiskSymtoms.getYear());
                        prefixId = ",";
                        prefixDays = ",";
                        stringBuilderId.append(prefixId);
                        stringBuilderDays.append(prefixDays);

                    } else {
                        if(!ids.contains(womenHighRisk.getHighRiskSymtomsArrayList().get(i).getSymptomId())){
                            HighRiskSymtoms highRiskSymtoms = memberHistoryArrayList.get(j).getHighRiskSymtomsArrayList().get(i);
                            highRiskSymtoms.setIsChecked("0");
                            highRiskSymtoms.setYear(null);
                            memberHistoryArrayList.get(j).getHighRiskSymtomsArrayList().set(i, highRiskSymtoms);
                            womenHighRisk.setHighRiskSymtomsArrayList(memberHistoryArrayList.get(j).getHighRiskSymtomsArrayList());
                        }

                    }
                }

            }
            memberHistoryArrayList.set(j, womenHighRisk);

//                    } while (cursor.moveToNext());
        }

        if (stringBuilder.toString().length() >= 2) {
            String risk = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
            String riskId=stringBuilderId.toString().substring(0, stringBuilderId.toString().length()-1);
            String days=stringBuilderDays.toString().substring(0, stringBuilderDays.toString().length()-1);
            ed_health_problems.setText(risk);
            ed_health_problems.setTag(riskId);
            textTag.setTag(days);
        }else {
            ed_health_problems.setText(stringBuilder.toString());
            ed_health_problems.setTag(stringBuilderId.toString());
            textTag.setTag(stringBuilderDays.toString());
        }
    }


    @Override
    public void onClick(View v) {
        Utils.ButtonClickEffect(v);
        switch (v.getId()) {
            case R.id.bt_Please_Modern:
                String name = ed_Name.getText().toString();
                familyMember.setMemberId(MemberId);
                familyMember.setFirstName(name);
                String husband_name = ed_husband_name.getText().toString();
                familyMember.setMiddleName(husband_name);
                String Sir_Name = ed_Sir_Name.getText().toString();
                familyMember.setLastName(Sir_Name);
                String isHead = isAns;
                familyMember.setIsHead(isHead);
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
                familyMember.setMenstruationStatus(prdStatus);
                if (image_bitmap != null) {
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    image_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    userImagebyteArray = stream.toByteArray();
//                    Log.v(TAG, "Image length" + userImagebyteArray.length);
                    familyMember.setPhotoValue(imageRealPath);
                    Uri uri = Uri.parse(imageRealPath);
                    String Name = new File(uri.getPath()).getName();
                    familyMember.setPhoto(Name);
                }

                familyMember.setBankName(member.getBankName());
                familyMember.setBranchName(member.getBranchName());
                familyMember.setAccountNo(member.getAccountNo());
                familyMember.setIfscCode(member.getIfscCode());
                familyMember.setAadharNo(member.getAadharNo());

                familyMember.setElectioncardNumber(member.getElectioncardNumber());
                familyMember.setPancardNumber(member.getPancardNumber());
                familyMember.setDrivingcardNumer(member.getDrivingcardNumer());
                familyMember.setPassportcardNumber(member.getPassportcardNumber());
                familyMember.setAadharNo(member.getAadharNo());

                String validateAddFamilyDetailForm = FormValidation.validateFamilyMemberRegistrationForm(familyMember, thisActivity);
                if (validateAddFamilyDetailForm.length() != 0) {
                    CustomLoaderDialog customLoaderDialog = new CustomLoaderDialog(thisActivity);
                    customLoaderDialog.showValidationDialog(validateAddFamilyDetailForm);
                } else {
                    SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                    String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                    try {
                        JSONObject jsonObject = new JSONObject(userDetail);
                        familyMember.setSubCenterId(jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        String userId = jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");
                        familyMember.setUserId(userId);
                        familyMember.setVillageId(villageId);
                        familyMember.setEmamtahealthId(member.getEmamtahealthId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    boolean flag = databaseHelper.updateFamilyMemberDetails(familyMember);
                    if (flag) {

                        if (ed_health_problems.getText().length() != 0) {
                            String[] historyArray = ed_health_problems.getTag().toString().split(",");
                            String[] yeasArray = textTag.getTag().toString().split(",");
                            DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                            databaseHelper.deleteHighRisk(familyMember.getMemberId());
                            boolean flagHistory = databaseHelper.insertMemberHistory(familyMember, historyArray, yeasArray);

                            if (flagHistory) {
                                String str = thisActivity.getResources().getString(R.string.member_update_success);
                                CustomToast customToast = new CustomToast(thisActivity, str);
                                customToast.show();
                                thisActivity.finish();
                            }
                        } else {
                            databaseHelper.deleteHighRisk(familyMember.getMemberId());
                            String str = thisActivity.getResources().getString(R.string.member_update_success);
                            CustomToast customToast = new CustomToast(thisActivity, str);
                            customToast.show();
                            thisActivity.finish();
                        }
                    }


                }


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
            case R.id.rdb_sex_Male:
                rdb_sex_Male.setChecked(true);
                rdb_sex_Female.setChecked(false);
                gender = "M";
                ll_masik.setVisibility(View.GONE);
                ll_isPregnant.setVisibility(View.GONE);
                ll_Family_welfare.setVisibility(View.GONE);
                ll_Family_welfare_user.setVisibility(View.GONE);
                ll_Want_Family_welfare.setVisibility(View.GONE);
                ll_whose_wife.setVisibility(View.GONE);
                ll_secong_child.setVisibility(View.GONE);
                break;
            case R.id.rdb_sex_Female:
                rdb_sex_Male.setChecked(false);
                rdb_sex_Female.setChecked(true);
                ll_masik.setVisibility(View.VISIBLE);
                ll_isPregnant.setVisibility(View.VISIBLE);
                ll_Family_welfare.setVisibility(View.VISIBLE);
                ll_Family_welfare_user.setVisibility(View.VISIBLE);
                ll_Want_Family_welfare.setVisibility(View.VISIBLE);
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
                final EditText ed_aadhar_number = (EditText) view.findViewById(R.id.ed_aadhar_no);
                Button bt_save = (Button) view.findViewById(R.id.bt_save);
                Button bt_identity_cancel = (Button) view.findViewById(R.id.bt_identity_cancel);

                if (member != null) {
                    ed_election_no.setText(member.getElectioncardNumber());
                    ed_pan_no.setText(member.getPancardNumber());
                    ed_driving_no.setText(member.getDrivingcardNumer());
                    ed_passport_no.setText(member.getPassportcardNumber());
                    ed_aadhar_number.setText(member.getAadharNo());
                }

                bt_identity_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.ButtonClickEffect(v);
                        dialog.dismiss();

                        electionNo = ed_election_no.getText().toString();
                        panNo = ed_pan_no.getText().toString();
                        drivingNo = ed_driving_no.getText().toString();
                        passportNo = ed_passport_no.getText().toString();
                        aadharNumber = ed_aadhar_number.getText().toString();

                        if (!electionNo.equalsIgnoreCase(member.getElectioncardNumber())) {
                            member.setElectioncardNumber(electionNo);
                        }
                        if (!panNo.equalsIgnoreCase(member.getPancardNumber())) {
                            member.setPancardNumber(panNo);
                        }
                        if (!drivingNo.equalsIgnoreCase(member.getDrivingcardNumer())) {
                            member.setDrivingcardNumer(drivingNo);
                        }
                        if (!passportNo.equalsIgnoreCase(member.getPassportcardNumber())) {
                            member.setPassportcardNumber(passportNo);
                        }
                        if (!aadharNumber.equalsIgnoreCase(familyMember.getAadharNo())) {
                            member.setAadharNo(aadharNumber);
                        }
                    }
                });
                bt_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.ButtonClickEffect(v);
                        dialog.dismiss();
                        electionNo = ed_election_no.getText().toString();
                        panNo = ed_pan_no.getText().toString();
                        drivingNo = ed_driving_no.getText().toString();
                        passportNo = ed_passport_no.getText().toString();
                        aadharNumber = ed_aadhar_number.getText().toString();
                        member.setElectioncardNumber(electionNo);
                        member.setPancardNumber(panNo);
                        member.setDrivingcardNumer(drivingNo);
                        member.setPassportcardNumber(passportNo);
                        member.setAadharNo(aadharNumber);
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
                View bankView = bankInflater.inflate(R.layout.bank_detail_layout, null);
                Utils.findAllTextView(thisActivity, ((ViewGroup) bankView.findViewById(R.id.ll_alert)));
                final EditText ed_bank_name = (EditText) bankView.findViewById(R.id.ed_bank_name);
                final EditText ed_branch_name = (EditText) bankView.findViewById(R.id.ed_branch_name);
                final EditText ed_ac_number = (EditText) bankView.findViewById(R.id.ed_ac_number);
                final EditText ed_IFSC_Code = (EditText) bankView.findViewById(R.id.ed_IFSC_Code);
                final EditText ed_aadhar_no = (EditText) bankView.findViewById(R.id.ed_aadhar_no);
                Button bt_save_bank = (Button) bankView.findViewById(R.id.bt_save_bank);
                Button bt_cancel_bank = (Button) bankView.findViewById(R.id.bt_cancel_bank);

                if (member != null) {
                    ed_bank_name.setText(member.getBankName());
                    ed_branch_name.setText(member.getBranchName());
                    ed_ac_number.setText(member.getAccountNo());
                    ed_IFSC_Code.setText(member.getIfscCode());
                    ed_aadhar_no.setText(member.getAadharNo());
                }

                bt_save_bank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.ButtonClickEffect(v);
                        bankDialog.dismiss();
                        bankName = ed_bank_name.getText().toString();
                        branchName = ed_branch_name.getText().toString();
                        acNumber = ed_ac_number.getText().toString();
                        IFSCCode = ed_IFSC_Code.getText().toString();
                        aadharNumber = ed_aadhar_no.getText().toString();
                        member.setBankName(bankName);
                        member.setBranchName(branchName);
                        member.setAccountNo(acNumber);
                        member.setIfscCode(IFSCCode);
                        member.setAadharNo(aadharNumber);
                    }
                });

                bt_cancel_bank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.ButtonClickEffect(v);
                        bankDialog.dismiss();

                        bankName = ed_bank_name.getText().toString();
                        branchName = ed_branch_name.getText().toString();
                        acNumber = ed_ac_number.getText().toString();
                        IFSCCode = ed_IFSC_Code.getText().toString();
                        aadharNumber = ed_aadhar_no.getText().toString();

                        if (!bankName.equalsIgnoreCase(member.getBankName())) {
                            member.setBankName(bankName);
                        }
                        if (!branchName.equalsIgnoreCase(member.getBranchName())) {
                            member.setBranchName(branchName);
                        }
                        if (!acNumber.equalsIgnoreCase(member.getAccountNo())) {
                            member.setAccountNo(acNumber);
                        }
                        if (!IFSCCode.equalsIgnoreCase(member.getIfscCode())) {
                            member.setIfscCode(IFSCCode);
                        }
                        if (!aadharNumber.equalsIgnoreCase(member.getAadharNo())) {
                            member.setAadharNo(aadharNumber);
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


        LinearLayout linearLayout;
        TextView textView;
        switch (parent.getId()) {
            case R.id.sp_family_head_relation:
                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    familyHeadRelation = textView.getTag().toString();
                } else {
                    familyHeadRelation = null;
                }

                break;
            case R.id.sp_Marital_status:
                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    maritalStatus = textView.getTag().toString();
                } else {
                    maritalStatus = null;
                }
                break;
            case R.id.sp_Whos_wife:
                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    wifeOf = textView.getTag().toString();
                } else {
                    wifeOf = null;
                }

                break;
            case R.id.sp_Whos_sun_daughter:
                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    sunOf = textView.getTag().toString();
                } else {
                    sunOf = null;
                }

                break;
            case R.id.sp_Family_welfare:
                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    familyWalfare = textView.getTag().toString();
                } else {
                    familyWalfare = null;
                }

                break;
            case R.id.sp_Family_welfare_user:
                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    familyWalfareUser = textView.getTag().toString();
                } else {
                    familyWalfareUser = null;
                }
                break;
            case R.id.sp_periods_status:
                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    periodeStatus = textView.getTag().toString();
                } else {
                    periodeStatus = null;
                }

                break;

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

                int check = 0,unCheck = 0,byDefault=0;
                for (int i = 0; i < memberHistoryArrayList.size(); i++) {
                    for (int j = 0; j < memberHistoryArrayList.get(i).getHighRiskSymtomsArrayList().size(); j++) {
                        HighRiskSymtoms highRiskSymtoms = memberHistoryArrayList.get(i).getHighRiskSymtomsArrayList().get(j);
                        if (highRiskSymtoms.getIsChecked().equals("1")) {
                            check++;
                            if (highRiskSymtoms.getYear() != null) {
                                unCheck++;
                            }
                        }else{
                            byDefault=1;
                        }
                    }
                }
                if(byDefault!=0) {
                    if (check != unCheck) {
                        String str = thisActivity.getResources().getString(R.string.insert_year);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    } else {
                        progressDialog.dismiss();
                    }
                }else{
                    progressDialog.dismiss();
                }

            }
        });

    }

    public void captureImage() {

        /*try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            Log.e(this + "", "cannot take picture " + e);
        } catch (Exception ex) {
            Log.e(this + "", "cannot take picture " + ex);
        }*/

        /*imageName = "picture_" + "" + System.currentTimeMillis();
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
        }*/

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
                String selectedImagePath = fileUri.getPath();
                Intent intent = new Intent(this, ImageCroppingActivity.class);
                intent.putExtra("imagePath", selectedImagePath);
                startActivityForResult(intent, CROP_PIC);

            } else if (requestCode == CROP_PIC) {

                File file = new File(fileUri.getPath());
                boolean deleted = file.delete();

                txt_take_image.setVisibility(View.GONE);
                imgUserImage.setVisibility(View.VISIBLE);
                imageRealPath = data.getStringExtra("imagePath");
                Uri uri = Uri.parse(imageRealPath);
                image_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));

                imgUserImage.setImageBitmap(image_bitmap);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_update_family_member, menu);
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
