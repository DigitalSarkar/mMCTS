package com.mcts.app.activity.kutumb;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mcts.app.R;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.Member;
import com.mcts.app.utils.DatePickerFragment;
import com.mcts.app.utils.TakePictureUtils;
import com.mcts.app.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class UpdateFamilyMemberActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    Activity thisActivity;
    private static String TAG = "UpdateFamilyMemberActivity";
    private Toolbar mToolbar;
    private TextView mTitle, txt_family_number, txt_health_number, txt_take_image;
    private EditText ed_Name, ed_husband_name, ed_Sir_Name, ed_Birth_date, ed_Mobile_number;
    private RadioButton rdb_yes, rdb_no, rdb_sex_Male, rdb_sex_Female, rdb_second_child_yes, rdb_second_child_no, rdb_Current_Status_yes, rdb_Current_Status_no;
    private Spinner sp_family_head_relation, sp_Marital_status,sp_Family_welfare,sp_Family_welfare_user,sp_periods_status,sp_Whos_sun_daughter,sp_Whos_wife;
    private Button bt_family_identity, bt_bank_detail, bt_Please_Modern, bt_Cancel, bt_Please_migration;
    DatabaseHelper databaseHelper;
    int FamilyNumber;
    private int familyHealthNumber;
    private String isAns="1", familyHeadRelation;
    private String gender="M", secondChild, isLiving, maritalStatus;
    private LinearLayout ll_masik,ll_isPregnant,ll_Family_welfare_user,ll_Want_Family_welfare,ll_Family_welfare,ll_whose_wife,ll_secong_child;

    //    Image Capture
    ImageView userImage;
    private String imageName = "item_picture";
    public static final int TAKE_PICTURE = 1;
    private String imageRealPath = null;
    private File compressFile;
    private Bitmap receipt_bitmap;
    private int width, height;
    private byte[] userImagebyteArray;
    private String villageId,villageName,MemberId;
    private String emamtaFamilyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_family_member);

        setToolBar();
        init();
//        getMemberData();
        setDropDownValue();

        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_add_memmber));
    }



    private void setToolBar() {

        thisActivity = UpdateFamilyMemberActivity.this;

        Intent intent=getIntent();
        villageId=intent.getStringExtra("villageId");
        villageName=intent.getStringExtra("villageName");
        MemberId=intent.getStringExtra("MemberId");
        emamtaFamilyId=intent.getStringExtra("emamtaFamilyId");

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.edit_family_member));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);

    }

    private void init() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        Intent intent = getIntent();
        FamilyNumber = intent.getIntExtra("FamilyNumber", 0);
        databaseHelper = new DatabaseHelper(thisActivity);
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_add_memmber));
        txt_family_number = (TextView) findViewById(R.id.txt_family_number);

        ll_masik=(LinearLayout)findViewById(R.id.ll_masik);
        ll_isPregnant=(LinearLayout)findViewById(R.id.ll_isPregnant);
        ll_Family_welfare_user=(LinearLayout)findViewById(R.id.ll_Family_welfare_user);
        ll_Want_Family_welfare=(LinearLayout)findViewById(R.id.ll_Want_Family_welfare);
        ll_Family_welfare=(LinearLayout)findViewById(R.id.ll_Family_welfare);
        ll_whose_wife=(LinearLayout)findViewById(R.id.ll_whose_wife);
        ll_secong_child=(LinearLayout)findViewById(R.id.ll_secong_child);

        txt_take_image = (TextView) findViewById(R.id.txt_take_image);
        userImage = (ImageView) findViewById(R.id.imgUserImage);
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


        bt_Please_Modern.setOnClickListener(this);
        rdb_yes.setOnClickListener(this);
        rdb_no.setOnClickListener(this);
        rdb_sex_Male.setOnClickListener(this);
        rdb_sex_Female.setOnClickListener(this);
        rdb_second_child_yes.setOnClickListener(this);
        rdb_second_child_no.setOnClickListener(this);
        rdb_Current_Status_yes.setOnClickListener(this);
        rdb_Current_Status_no.setOnClickListener(this);
        bt_family_identity.setOnClickListener(this);
        bt_bank_detail.setOnClickListener(this);
        ed_Birth_date.setOnClickListener(this);
        userImage.setOnClickListener(this);
        txt_take_image.setOnClickListener(this);
    }

    private void getMemberData() {

        DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
        Member member=databaseHelper.getFamilyMember(MemberId);
    }

    private void setDropDownValue() {

        ArrayList<MaritalStatus> maritalStatusArrayList = databaseHelper.getMaritalStatus();
        if(maritalStatusArrayList!=null) {
            StatusAdapter statusAdapter = new StatusAdapter(thisActivity, maritalStatusArrayList);
            sp_Marital_status.setAdapter(statusAdapter);
            sp_Marital_status.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> familyWalfareList=databaseHelper.getFamilyPlanningData();
        if(familyWalfareList!=null) {
            StatusAdapter familyWalfareAdapter = new StatusAdapter(thisActivity, familyWalfareList);
            sp_Family_welfare.setAdapter(familyWalfareAdapter);
            sp_Family_welfare_user.setAdapter(familyWalfareAdapter);
            sp_Family_welfare.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> periodStatusArray=databaseHelper.getPeriodeData();
        if(periodStatusArray!=null){
            StatusAdapter masikAdapter=new StatusAdapter(thisActivity,periodStatusArray);
            sp_periods_status.setAdapter(masikAdapter);
            sp_periods_status.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> relationArrayList = databaseHelper.getRelation();
        if(relationArrayList!=null) {
            StatusAdapter relationAdapter = new StatusAdapter(thisActivity, relationArrayList);
            sp_family_head_relation.setAdapter(relationAdapter);
            sp_family_head_relation.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> sunOfArrayList=databaseHelper.getParentList(emamtaFamilyId);
        if(sunOfArrayList!=null){
            StatusAdapter sunDaughterAdapter = new StatusAdapter(thisActivity, sunOfArrayList);
            sp_Whos_sun_daughter.setAdapter(sunDaughterAdapter);
            sp_Whos_sun_daughter.setOnItemSelectedListener(this);
        }

        ArrayList<MaritalStatus> wifeOfArrayList=databaseHelper.getWifeList(emamtaFamilyId);
        if(sunOfArrayList!=null){
            StatusAdapter sunDaughterAdapter = new StatusAdapter(thisActivity, wifeOfArrayList);
            sp_Whos_wife.setAdapter(sunDaughterAdapter);
            sp_Whos_wife.setOnItemSelectedListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Please_Modern:
                String name = ed_Name.getText().toString();
                String husband_name = ed_husband_name.getText().toString();
                String Sir_Name = ed_Sir_Name.getText().toString();
                String isHead = isAns;
                String headRelation = familyHeadRelation;
                String sex = gender;
                String marStatus = maritalStatus;
                String bDate = ed_Birth_date.getText().toString();
                String moNumber = ed_Mobile_number.getText().toString();
//                String Family_welfare = ed_Family_welfare.getText().toString();
//                String Family_welfare_use = ed_Family_welfare_user.getText().toString();
                String secChild = secondChild;
                String live = isLiving;
//                String periods_status = ed_periods_status.getText().toString();
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
                isLiving = "y";
                break;
            case R.id.rdb_Current_Status_no:
                rdb_Current_Status_yes.setChecked(false);
                rdb_Current_Status_no.setChecked(true);
                isLiving = "n";
                break;
            case R.id.bt_family_identity:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(thisActivity);
                LayoutInflater layoutInflater
                        = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.family_identity_layout, null);
                Utils.findAllTextView(thisActivity, ((ViewGroup) view.findViewById(R.id.ll_alert)));
                alertDialogBuilder.setView(view);
                alertDialogBuilder.show();
                break;
            case R.id.bt_bank_detail:
                AlertDialog.Builder bankAlertDialog = new AlertDialog.Builder(thisActivity);
                LayoutInflater layoutBankInflater
                        = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View bankView = layoutBankInflater.inflate(R.layout.bank_detail_layout, null);
                Utils.findAllTextView(thisActivity, ((ViewGroup) bankView.findViewById(R.id.ll_alert)));
                bankAlertDialog.setView(bankView);
                bankAlertDialog.show();
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
                linearLayout = (LinearLayout) view;
                textView = (TextView) linearLayout.getChildAt(0);
                familyHeadRelation = textView.getTag().toString();
                break;
            case R.id.sp_Marital_status:
                linearLayout = (LinearLayout) view;
                textView = (TextView) linearLayout.getChildAt(0);
                maritalStatus = textView.getTag().toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {

                imageRealPath = new File(getExternalFilesDir("temp"),
                        imageName + ".png").getPath();
                compressFile = new File(imageRealPath);
                txt_take_image.setVisibility(View.GONE);
                userImage.setVisibility(View.VISIBLE);
                receipt_bitmap = TakePictureUtils.decodeFile(compressFile);
                receipt_bitmap = Bitmap.createScaledBitmap(receipt_bitmap, width, height, true);
                userImage.setImageBitmap(receipt_bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                receipt_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                userImagebyteArray= stream.toByteArray();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No any image selected", Toast.LENGTH_SHORT).show();
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
}
