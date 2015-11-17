package com.mcts.app.activity.kutumb;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mcts.app.R;
import com.mcts.app.adapter.ReligionAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.Member;
import com.mcts.app.model.Religion;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.DatePickerFragment;
import com.mcts.app.utils.Messages;
import com.mcts.app.utils.TakePictureUtils;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class UpdateKutumbActivity extends BaseActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,AdapterView.OnItemSelectedListener {

    Activity thisActivity;
    private static String TAG="UpdateKutumbActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView txt_village_name,txt_lat,txt_lng,txt_lbl_village,lbl_family_number,lbl_yes,lbl_no,lbl_bpl,lbl_family_dharm,lbl_family_cast;
    private TextView lbl_house_number,bt_family_location,txt_add_street,txt_take_image,txt_add_location;
    private Spinner sp_family_cast,sp_family_dharm,sp_street_name,sp_Marital_status,sp_aganvali;
    private RadioButton rdb_yes,rdb_no,rdb_sex_Male,rdb_sex_Female;
    private Button bt_family_identity,bt_add_family,bt_family_add_member;
    private ArrayList<Religion> castArrayList;
    private ArrayList<Religion> religionArrayList;
    private ArrayList<Religion> faliyaArrayList;
    private EditText ed_house_number,ed_husband_name,ed_Sir_Name,ed_family_number,ed_landmark,ed_family_head_name,ed_Birth_date,ed_Mobile_number;
    private String isAns="1",isGender="M";
    private Location mLastLocation;
    private ImageView imgUserImage;
    private String villageId,villageName;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private String strReligion,strCast,strFaliyaId;
    DatabaseHelper databaseHelper;
    private CustomToast customToast;
    private int randomNumber;
    private String MemberId;
    private Member member;
    private String isRisky="1";
    String bplNumber,rationCardNumber,rsbyNumber,maaCardNumber;
    //    Image Capture
//    ImageView userImage;
    private String imageName = "item_picture";
    public static final int TAKE_PICTURE = 1;
    private String imageRealPath = null;
    private File compressFile;
    private Bitmap receipt_bitmap;
    private int width, height;
    private String aaganvadiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_kutumb);

        setToolBar();
        init();
        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        getDataFromDB();
    }


    private void setToolBar() {

        thisActivity = UpdateKutumbActivity.this;
        Utils.hideSoftKeyboard(thisActivity);

        Intent intent=getIntent();
        villageId=intent.getStringExtra("villageId");
        villageName=intent.getStringExtra("villageName");
        MemberId=intent.getStringExtra("MemberId");

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.edit_family));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);

    }

    private void init() {

//        Randrom number for temp family number
        Random rand = new Random();
        randomNumber = rand.nextInt(900000) + 100000;
        Log.v("randomNumber",""+randomNumber);

        databaseHelper=new DatabaseHelper(thisActivity);
        txt_village_name=(TextView)findViewById(R.id.txt_village_name);
        txt_lbl_village=(TextView)findViewById(R.id.txt_lbl_village);
        txt_add_street=(TextView)findViewById(R.id.txt_add_street);
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_add_family));
        lbl_family_number=(TextView)findViewById(R.id.lbl_family_number);
        lbl_yes=(TextView)findViewById(R.id.lbl_yes);
        lbl_no=(TextView)findViewById(R.id.lbl_no);
        lbl_bpl=(TextView)findViewById(R.id.lbl_bpl);
        txt_take_image=(TextView)findViewById(R.id.txt_take_image);
        lbl_family_dharm=(TextView)findViewById(R.id.lbl_family_dharm);
        lbl_family_cast=(TextView)findViewById(R.id.lbl_family_cast);
        lbl_house_number=(TextView)findViewById(R.id.lbl_house_number);
        txt_add_location=(TextView)findViewById(R.id.txt_add_location);
        ed_family_number=(EditText)findViewById(R.id.ed_family_number);
        ed_family_head_name=(EditText)findViewById(R.id.ed_family_head_name);
        ed_husband_name=(EditText)findViewById(R.id.ed_husband_name);
        ed_Sir_Name=(EditText)findViewById(R.id.ed_Sir_Name);
        ed_landmark=(EditText)findViewById(R.id.ed_landmark);
        txt_lat=(TextView)findViewById(R.id.txt_lat);
        txt_lng=(TextView)findViewById(R.id.txt_lng);

        ed_house_number=(EditText)findViewById(R.id.ed_house_number);
        ed_Birth_date=(EditText)findViewById(R.id.ed_Birth_date);
        ed_Mobile_number=(EditText)findViewById(R.id.ed_Mobile_number);

        sp_family_cast=(Spinner)findViewById(R.id.sp_family_cast);
        sp_family_dharm=(Spinner)findViewById(R.id.sp_family_dharm);
        sp_street_name=(Spinner)findViewById(R.id.sp_street_name);
        sp_Marital_status=(Spinner)findViewById(R.id.sp_Marital_status);
        sp_aganvali=(Spinner)findViewById(R.id.sp_aganvali);

        rdb_yes=(RadioButton)findViewById(R.id.rdb_yes);
        rdb_no=(RadioButton)findViewById(R.id.rdb_no);
        rdb_sex_Male=(RadioButton)findViewById(R.id.rdb_sex_Male);
        rdb_sex_Female=(RadioButton)findViewById(R.id.rdb_sex_Female);

        bt_family_identity=(Button)findViewById(R.id.bt_family_identity);
        bt_family_add_member=(Button)findViewById(R.id.bt_family_add_member);
        bt_family_location=(TextView)findViewById(R.id.bt_family_location);
        bt_add_family=(Button)findViewById(R.id.bt_add_family);
        imgUserImage=(ImageView)findViewById(R.id.imgUserImage);

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        bt_add_family.setTypeface(type, Typeface.BOLD);
        txt_lbl_village.setTypeface(type, Typeface.BOLD);
        txt_village_name.setTypeface(type, Typeface.BOLD);
        txt_village_name.setText(villageName);
        lbl_family_number.setTypeface(type, Typeface.BOLD);
        lbl_yes.setTypeface(type, Typeface.BOLD);
        lbl_no.setTypeface(type, Typeface.BOLD);
        lbl_bpl.setTypeface(type, Typeface.BOLD);
        lbl_house_number.setTypeface(type, Typeface.BOLD);
        lbl_family_cast.setTypeface(type, Typeface.BOLD);
        lbl_family_dharm.setTypeface(type, Typeface.BOLD);
        bt_family_location.setTypeface(type, Typeface.BOLD);
        bt_family_identity.setTypeface(type, Typeface.BOLD);
        ed_house_number.setTypeface(type, Typeface.BOLD);
        rdb_yes.setTypeface(type, Typeface.BOLD);
        rdb_no.setTypeface(type, Typeface.BOLD);
        ed_family_number.setTypeface(type, Typeface.BOLD);


        bt_family_identity.setOnClickListener(this);
        txt_add_location.setOnClickListener(this);
        bt_add_family.setOnClickListener(this);
        bt_family_add_member.setOnClickListener(this);
        rdb_yes.setOnClickListener(this);
        rdb_no.setOnClickListener(this);
        rdb_no.setOnClickListener(this);
        rdb_sex_Male.setOnClickListener(this);
        rdb_sex_Female.setOnClickListener(this);
        ed_Birth_date.setOnClickListener(this);
        imgUserImage.setOnClickListener(this);
        txt_take_image.setOnClickListener(this);
        txt_add_street.setOnClickListener(this);
        rdb_yes.setChecked(true);

        sp_family_dharm.setOnItemSelectedListener(this);
        sp_family_cast.setOnItemSelectedListener(this);
        sp_street_name.setOnItemSelectedListener(this);
        sp_aganvali.setOnItemSelectedListener(this);

        if(MemberId!=null){
            setMemberData();
        }else{
            ed_family_number.setText("" + randomNumber);
            ed_family_number.setEnabled(false);
        }

    }

    private void setMemberData() {

        DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
        member=databaseHelper.getMemberDetail(MemberId);
        ed_family_number.setText(member.getEmamtafamilyId());
        ed_house_number.setText(member.getHouseNumber());
        ed_landmark.setText(member.getLandmark());
        ed_family_head_name.setText(member.getFirstName());
        ed_husband_name.setText(member.getMiddleName());
        ed_Sir_Name.setText(member.getLastName());
        ed_Birth_date.setText(member.getBirthDate());
        ed_Mobile_number.setText(member.getMobileNo());

        /*Temparary disable data*/
        ed_family_number.setEnabled(false);
        ed_husband_name.setEnabled(false);
        ed_family_head_name.setEnabled(false);
        ed_Sir_Name.setEnabled(false);
        ed_Birth_date.setEnabled(false);
        ed_Mobile_number.setEnabled(false);
        rdb_sex_Female.setEnabled(false);
        rdb_sex_Male.setEnabled(false);

        if (member.getIsBpl().equals("1")){
            rdb_yes.setChecked(true);
            rdb_no.setChecked(false);
            isAns="1";
            Log.v("Ans","Yes");
        }else{
            rdb_yes.setChecked(false);
            rdb_no.setChecked(true);
            isAns="0";
            Log.v("Ans", "No");
        }
        if(member.getGender().equals("M")){
            rdb_sex_Male.setChecked(true);
            rdb_sex_Female.setChecked(false);
            isGender="M";
        }else{
            rdb_sex_Male.setChecked(false);
            rdb_sex_Female.setChecked(true);
            isGender="F";
        }
        if(member.getLattitudes()!=null){
            txt_lat.setText(""+member.getLattitudes());
            txt_lng.setText(""+member.getLongitude());
        }

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void getDataFromDB() {

        religionArrayList= databaseHelper.getReligionData();
        ReligionAdapter religionAdapter=new ReligionAdapter(thisActivity,religionArrayList);
        sp_family_dharm.setAdapter(religionAdapter);

        castArrayList=new ArrayList<>();
        castArrayList=databaseHelper.getCastData();
        ReligionAdapter castAdapter=new ReligionAdapter(thisActivity,castArrayList);
        sp_family_cast.setAdapter(castAdapter);

        faliyaArrayList=databaseHelper.getFaliyaList(villageId);
        if(faliyaArrayList!=null){
            ReligionAdapter faliyaAdapter=new ReligionAdapter(thisActivity,faliyaArrayList);
            sp_street_name.setAdapter(faliyaAdapter);
        }

        SharedPreferences sharedPreferences=thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
        String userDetail=sharedPreferences.getString(Constants.USER_ID, null);
        try {
            JSONObject jsonObject = new JSONObject(userDetail);
            String subCenterId=jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcenterId");
            String userId=jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");
            ArrayList<MaritalStatus> aganvadiList=databaseHelper.getAganvadi(villageId, subCenterId);
            StatusAdapter aganvadiAdapter=new StatusAdapter(thisActivity,aganvadiList);
            sp_aganvali.setAdapter(aganvadiAdapter);
            if(member!=null) {
                if (member.getAnganwadiId() != null) {
                    for (int i = 0; i < aganvadiList.size(); i++) {
                        if (member.getAnganwadiId().equalsIgnoreCase(aganvadiList.get(i).getId())) {
                            sp_aganvali.setSelection(i);
                        }
                    }
                } else {
                    sp_aganvali.setSelection(0);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        if(member!=null){
            if(!member.getRaciald().equals("null")) {
                int racialId = Integer.parseInt(member.getRaciald());
                racialId=racialId-1;
                sp_family_cast.setSelection(racialId);
            }else{
                sp_family_cast.setSelection(0);
            }
            if (!member.getReligionId().equals("null")) {
                int religionId = Integer.parseInt(member.getReligionId());
                religionId=religionId-1;
                sp_family_dharm.setSelection(religionId);
            }else{
                sp_family_dharm.setSelection(0);
            }
            if(member.getFaliyu()!=null){
//                int faliyaId = Integer.parseInt(member.getFamilyId());
                for(int i=0;i<faliyaArrayList.size();i++){
                    if(member.getFaliyu().equalsIgnoreCase(faliyaArrayList.get(i).getId())){
//                        faliyaId=faliyaId-1;
                        sp_street_name.setSelection(i);
                    }
                }

            }else{
                sp_street_name.setSelection(0);
            }
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_add_kutumb, menu);
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
            case R.id.bt_family_identity:

                final Dialog dialog = new Dialog(thisActivity);
                LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view=mInflater.inflate(R.layout.family_identity_layout, null);
                Utils.findAllTextView(thisActivity, ((ViewGroup) view.findViewById(R.id.ll_alert)));
                final EditText ed_bpl_number=(EditText)view.findViewById(R.id.ed_bpl_number);
                final EditText ed_bpl_card_number=(EditText)view.findViewById(R.id.ed_bpl_card_number);
                final EditText ed_rsby_number=(EditText)view.findViewById(R.id.ed_rsby_number);
                final EditText ed_maa_card_number=(EditText)view.findViewById(R.id.ed_maa_card_number);
                Button bt_save=(Button)view.findViewById(R.id.bt_save);
                Button bt_identity_cancel=(Button)view.findViewById(R.id.bt_identity_cancel);

                if(member!=null) {
                    ed_bpl_number.setText(member.getBplNumber());
                    ed_bpl_card_number.setText(member.getRationcardNrumber());
                    ed_rsby_number.setText(member.getRsbycardNumber());
                    ed_maa_card_number.setText(member.getMacardNumber());
                }

                bt_identity_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        bplNumber=ed_bpl_number.getText().toString();
                        rationCardNumber=ed_bpl_card_number.getText().toString();
                        rsbyNumber=ed_rsby_number.getText().toString();
                        maaCardNumber=ed_maa_card_number.getText().toString();

                        if(!bplNumber.equalsIgnoreCase(member.getBplNumber())){
                            member.setBplNumber(bplNumber);
                        }
                        if(!rationCardNumber.equalsIgnoreCase(member.getRationcardNrumber())) {
                            member.setRationcardNrumber(rationCardNumber);
                        }
                        if(!rsbyNumber.equalsIgnoreCase(member.getRsbycardNumber())) {
                            member.setRsbycardNumber(rsbyNumber);
                        }
                        if(!maaCardNumber.equalsIgnoreCase(member.getMacardNumber())) {
                            member.setMacardNumber(maaCardNumber);
                        }
                    }
                });
                bt_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        bplNumber=ed_bpl_number.getText().toString();
                        rationCardNumber=ed_bpl_card_number.getText().toString();
                        rsbyNumber=ed_rsby_number.getText().toString();
                        maaCardNumber=ed_maa_card_number.getText().toString();
                        member.setBplNumber(bplNumber);
                        member.setRationcardNrumber(rationCardNumber);
                        member.setRsbycardNumber(rsbyNumber);
                        member.setMacardNumber(maaCardNumber);
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

            case R.id.txt_add_street:

                final Dialog streetDialog = new Dialog(thisActivity);
                LayoutInflater Inflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View streetView=Inflater.inflate(R.layout.add_faliyu_layout, null);
                final EditText ed_street_name=(EditText)streetView.findViewById(R.id.ed_street_name);
                final RadioButton rdb_risky_area_yes=(RadioButton)streetView.findViewById(R.id.rdb_risky_area_yes);
                final RadioButton rdb_risky_area_no=(RadioButton)streetView.findViewById(R.id.rdb_risky_area_no);
                Button bt_faliyu_save=(Button)streetView.findViewById(R.id.bt_faliyu_save);
                Button bt_faliyu_cancel=(Button)streetView.findViewById(R.id.bt_faliyu_cancel);

                rdb_risky_area_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rdb_risky_area_yes.setChecked(true);
                        rdb_risky_area_no.setChecked(false);
                        isRisky="1";
                    }
                });
                rdb_risky_area_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rdb_risky_area_yes.setChecked(false);
                        rdb_risky_area_no.setChecked(true);
                        isRisky="0";
                    }
                });
                bt_faliyu_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ed_street_name.getText().toString().length()!=0) {
                            String faliyaName = ed_street_name.getText().toString();
                            SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                            String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                            try {
                                JSONObject jsonObject = new JSONObject(userDetail);
                                String userId = jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");
                                boolean flag = databaseHelper.insertFaliyu(villageId, faliyaName, isRisky, userId);
                                if (flag) {
                                    streetDialog.dismiss();
                                    faliyaArrayList = databaseHelper.getFaliyaList(villageId);
                                }
                                if (faliyaArrayList != null) {
                                    ReligionAdapter religionAdapter = new ReligionAdapter(thisActivity, faliyaArrayList);
                                    sp_street_name.setAdapter(religionAdapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            CustomToast customToast=new CustomToast(thisActivity, Messages.ADD_FALIYU);
                            customToast.show();
                        }

                    }
                });
                bt_faliyu_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        streetDialog.dismiss();
                    }
                });

                Utils.findAllTextView(thisActivity, ((ViewGroup) streetView.findViewById(R.id.ll_add_faliyu)));
                streetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                streetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                streetDialog.setContentView(streetView);

                WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
                Display disp = windowManager.getDefaultDisplay(); // getting the screen size of device
                Point sizeStreet = new Point();
                disp.getSize(sizeStreet);
                int widthStreet = WindowManager.LayoutParams.WRAP_CONTENT;
                int heightStreet = WindowManager.LayoutParams.WRAP_CONTENT;

                int tempValueStreet = 0;
                tempValueStreet = ((sizeStreet.x) * 200) / 1440;
                int widsizeStreet = sizeStreet.x - tempValueStreet;  // Set your widths
                int heigsizeStreet = heightStreet; // set your heights

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(streetDialog.getWindow().getAttributes());

                layoutParams.width = widsizeStreet;
                layoutParams.height = heigsizeStreet;
                streetDialog.getWindow().setAttributes(layoutParams);
                streetDialog.show();

                break;
            case R.id.rdb_yes:
                rdb_yes.setChecked(true);
                rdb_no.setChecked(false);
                isAns="1";
                Log.v("Ans","Yes");
                break;
            case R.id.rdb_no:
                rdb_yes.setChecked(false);
                rdb_no.setChecked(true);
                isAns="0";
                Log.v("Ans","No");
                break;
            case R.id.rdb_sex_Male:
                rdb_sex_Male.setChecked(true);
                rdb_sex_Female.setChecked(false);
                isGender="M";
                Log.v("m","m");
                break;
            case R.id.rdb_sex_Female:
                rdb_sex_Male.setChecked(false);
                rdb_sex_Female.setChecked(true);
                isGender="F";
                break;
            case R.id.txt_add_location:

                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);

                if (mLastLocation != null) {
                    double latitude = mLastLocation.getLatitude();
                    double longitude = mLastLocation.getLongitude();

                    txt_lat.setText(""+latitude);
                    txt_lng.setText(""+longitude);
//                    Toast.makeText(thisActivity,latitude + ", " + longitude,Toast.LENGTH_SHORT).show();
                } else {
                    checkLocationService();
                }
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
            case R.id.bt_add_family:

                Member familyMember=new Member();
                familyMember.setEmamtafamilyId(member.getEmamtafamilyId());
                familyMember.setHouseNumber(ed_house_number.getText().toString());
                String landmark=ed_landmark.getText().toString();
                familyMember.setLandmark(landmark);
                String racial=strCast;
                familyMember.setRaciald(racial);
                String religion=strReligion;
                familyMember.setReligionId(religion);
                String isBpl=isAns;
                familyMember.setIsBpl(isBpl);
                familyMember.setBplNumber(bplNumber);
                familyMember.setRationcardNrumber(rationCardNumber);
                familyMember.setRsbycardNumber(rsbyNumber);
                familyMember.setMacardNumber(maaCardNumber);
                familyMember.setAnganwadiId(aaganvadiId);
                if (mLastLocation != null) {
                    double latitude = mLastLocation.getLatitude();
                    double longitude = mLastLocation.getLongitude();

                    familyMember.setLattitudes("" + latitude);
                    familyMember.setLongitude("" + longitude);
//                    Toast.makeText(thisActivity,latitude + ", " + longitude,Toast.LENGTH_SHORT).show();
                }
                if(strFaliyaId!=null){
                    familyMember.setFaliyu(strFaliyaId);
                }

                boolean isSave=databaseHelper.updateFamilyDetails(familyMember);
                if(isSave){
                    CustomToast customToast=new CustomToast(thisActivity, Messages.FAMILY_UPDATE);
                    customToast.show();
                    ed_house_number.setEnabled(false);
                    ed_landmark.setEnabled(false);
                    sp_family_cast.setEnabled(false);
                    sp_family_dharm.setEnabled(false);
                    rdb_no.setEnabled(false);
                    rdb_yes.setEnabled(false);
                    thisActivity.finish();
                }
                break;
            case R.id.bt_family_add_member:
                Intent intent=new Intent(thisActivity,AddFamilyMemberActivity.class);
                intent.putExtra("emamtafamilyId",ed_family_number.getText().toString());
                intent.putExtra("villageId",villageId);
                startActivity(intent);
                break;

        }
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
            Log.e(this  + "", "cannot take picture " + e);
        } catch (Exception ex) {
            Log.e(this + "", "cannot take picture " + ex);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        mLastLocation = LocationServices.FusedLocationApi
//                .getLastLocation(mGoogleApiClient);
//
//        if (mLastLocation != null) {
//            double latitude = mLastLocation.getLatitude();
//            double longitude = mLastLocation.getLongitude();
//
//            txt_lat.setText(""+latitude);
//            txt_lng.setText(""+longitude);
////                    Toast.makeText(thisActivity,latitude + ", " + longitude,Toast.LENGTH_SHORT).show();
////
//        } else {
//            checkLocationService();
//        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void checkLocationService(){

        LocationManager lm = (LocationManager)thisActivity.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(thisActivity);
            dialog.setMessage("Location service not enable");
            dialog.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    thisActivity.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()==R.id.sp_family_cast){
            Religion religion=castArrayList.get(position);
            strCast=religion.getId();
//            Toast.makeText(thisActivity,religion.getName(),Toast.LENGTH_SHORT).show();
        }else if(parent.getId()==R.id.sp_family_dharm){
            Religion religion=religionArrayList.get(position);
            strReligion=religion.getId();
//            Toast.makeText(thisActivity,religion.getName(),Toast.LENGTH_SHORT).show();
        }else if(parent.getId()==R.id.sp_street_name){
            Religion religion=faliyaArrayList.get(position);
            strFaliyaId=religion.getId();
        }else if(parent.getId()==R.id.sp_aganvali){
            LinearLayout linearLayout = (LinearLayout) view;
            TextView textView = (TextView) linearLayout.getChildAt(0);
            aaganvadiId = textView.getTag().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {

                imageRealPath = new File(getExternalFilesDir("temp"),
                        imageName + ".png").getPath();
                compressFile = new File(imageRealPath);
                txt_take_image.setVisibility(View.GONE);
                imgUserImage.setVisibility(View.VISIBLE);
                receipt_bitmap = TakePictureUtils.decodeFile(compressFile);
                receipt_bitmap = Bitmap.createScaledBitmap(receipt_bitmap, width, height, true);
                imgUserImage.setImageBitmap(receipt_bitmap);
            }
        } else {
            Toast.makeText(getApplicationContext(), "No any image selected", Toast.LENGTH_SHORT).show();
        }
    }
}
