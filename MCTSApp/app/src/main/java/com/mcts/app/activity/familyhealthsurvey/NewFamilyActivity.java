package com.mcts.app.activity.familyhealthsurvey;

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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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
import com.mcts.app.activity.ImageCroppingActivity;
import com.mcts.app.adapter.ReligionAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.Member;
import com.mcts.app.model.Religion;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.DatePickerFragment;
import com.mcts.app.utils.FormValidation;
import com.mcts.app.utils.Messages;
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
import java.util.Date;
import java.util.Random;

public class NewFamilyActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

    Activity thisActivity;
    private static String TAG = "NewFamilyActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView txt_village_name, txt_lat, txt_lng, txt_lbl_village, lbl_family_number, lbl_yes, lbl_no, lbl_bpl, lbl_family_dharm, lbl_family_cast;
    private TextView lbl_house_number, bt_family_location, txt_add_street, txt_take_image, txt_add_location;
    private Spinner sp_family_cast, sp_family_dharm, sp_street_name, sp_Marital_status, sp_aganvali;
    private RadioButton rdb_yes, rdb_no, rdb_sex_Male, rdb_sex_Female;
    private Button bt_family_identity, bt_add_family;
    private ArrayList<Religion> castArrayList;
    private ArrayList<Religion> religionArrayList;
    private ArrayList<Religion> faliyaArrayList;
    private ArrayList<Religion> streetArrayList;
    ReligionAdapter streetAdapter;
    ReligionAdapter faliyaAdapter;
    private EditText ed_house_number, ed_husband_name, ed_Sir_Name, ed_family_number, ed_landmark, ed_family_head_name, ed_Birth_date, ed_Mobile_number;
    private String isAns, isGender;
    private Location mLastLocation;
    private ImageView imgUserImage;
    private String villageId, villageName;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private String strReligion, strCast, strFaliyaId;
    DatabaseHelper databaseHelper;
    private String isRisky = "1";
    String bplNumber, rationCardNumber, rsbyNumber, maaCardNumber;

    //    Image Capture
    private String imageName = "item_picture";
    private String imageRealPath = null;
    public static final int TAKE_PICTURE = 1;
    private final int CROP_PIC = 2;
    private Uri picUri;
    private Bitmap receipt_bitmap;
    Member familyMember = new Member();
    private String maritalStatus;
    private byte[] userImagebyteArray;
    private String aaganvadiId, MemberId;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_family);

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

        thisActivity = NewFamilyActivity.this;
        Utils.hideSoftKeyboard(thisActivity);

        Intent intent = getIntent();
        villageId = intent.getStringExtra("villageId");
        villageName = intent.getStringExtra("villageName");
        MemberId = intent.getStringExtra("MemberId");

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.add_family));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void init() {

        databaseHelper = new DatabaseHelper(thisActivity);
        txt_village_name = (TextView) findViewById(R.id.txt_village_name);
        txt_lbl_village = (TextView) findViewById(R.id.txt_lbl_village);
        txt_add_street = (TextView) findViewById(R.id.txt_add_street);
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_add_family));
        lbl_family_number = (TextView) findViewById(R.id.lbl_family_number);
        lbl_yes = (TextView) findViewById(R.id.lbl_yes);
        lbl_no = (TextView) findViewById(R.id.lbl_no);
        lbl_bpl = (TextView) findViewById(R.id.lbl_bpl);
        txt_take_image = (TextView) findViewById(R.id.txt_take_image);
        lbl_family_dharm = (TextView) findViewById(R.id.lbl_family_dharm);
        lbl_family_cast = (TextView) findViewById(R.id.lbl_family_cast);
        lbl_house_number = (TextView) findViewById(R.id.lbl_house_number);
        txt_add_location = (TextView) findViewById(R.id.txt_add_location);
        ed_family_number = (EditText) findViewById(R.id.ed_family_number);
        ed_family_head_name = (EditText) findViewById(R.id.ed_family_head_name);
        ed_husband_name = (EditText) findViewById(R.id.ed_husband_name);
        ed_Sir_Name = (EditText) findViewById(R.id.ed_Sir_Name);
        ed_landmark = (EditText) findViewById(R.id.ed_landmark);
        txt_lat = (TextView) findViewById(R.id.txt_lat);
        txt_lng = (TextView) findViewById(R.id.txt_lng);

        ed_house_number = (EditText) findViewById(R.id.ed_house_number);
        ed_Birth_date = (EditText) findViewById(R.id.ed_Birth_date);
        ed_Mobile_number = (EditText) findViewById(R.id.ed_Mobile_number);

        sp_family_cast = (Spinner) findViewById(R.id.sp_family_cast);
        sp_family_dharm = (Spinner) findViewById(R.id.sp_family_dharm);
        sp_street_name = (Spinner) findViewById(R.id.sp_street_name);
        sp_Marital_status = (Spinner) findViewById(R.id.sp_Marital_status);
        sp_aganvali = (Spinner) findViewById(R.id.sp_aganvali);

        rdb_yes = (RadioButton) findViewById(R.id.rdb_yes);
        rdb_no = (RadioButton) findViewById(R.id.rdb_no);
        rdb_sex_Male = (RadioButton) findViewById(R.id.rdb_sex_Male);
        rdb_sex_Female = (RadioButton) findViewById(R.id.rdb_sex_Female);

        bt_family_identity = (Button) findViewById(R.id.bt_family_identity);
        bt_family_location = (TextView) findViewById(R.id.bt_family_location);
        bt_add_family = (Button) findViewById(R.id.bt_add_family);
        imgUserImage = (ImageView) findViewById(R.id.imgUserImage);

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

        txt_village_name.setText(villageName);

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

            String strEmamtaId = "FM/" + year + "/" + userId + "" + emamId;
            ed_family_number.setText(strEmamtaId);
            ed_family_number.setEnabled(false);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        bt_family_identity.setOnClickListener(this);
        txt_add_location.setOnClickListener(this);
        bt_add_family.setOnClickListener(this);
        rdb_yes.setOnClickListener(this);
        rdb_no.setOnClickListener(this);
        rdb_no.setOnClickListener(this);
        rdb_sex_Male.setOnClickListener(this);
        rdb_sex_Female.setOnClickListener(this);
        ed_Birth_date.setOnClickListener(this);
        imgUserImage.setOnClickListener(this);
        txt_take_image.setOnClickListener(this);
        txt_add_street.setOnClickListener(this);

        sp_family_dharm.setOnItemSelectedListener(this);
        sp_family_cast.setOnItemSelectedListener(this);
        sp_street_name.setOnItemSelectedListener(this);
        sp_Marital_status.setOnItemSelectedListener(this);
        sp_aganvali.setOnItemSelectedListener(this);
    }

    private void getDataFromDB() {

        religionArrayList = databaseHelper.getReligionData();
        if (religionArrayList != null) {
            ReligionAdapter religionAdapter = new ReligionAdapter(thisActivity, religionArrayList);
            sp_family_dharm.setAdapter(religionAdapter);
        }

        castArrayList = databaseHelper.getCastData();
        if (castArrayList != null) {
            ReligionAdapter castAdapter = new ReligionAdapter(thisActivity, castArrayList);
            sp_family_cast.setAdapter(castAdapter);
        }


        faliyaArrayList = databaseHelper.getFaliyaList(villageId);
        if (faliyaArrayList != null) {
            faliyaAdapter = new ReligionAdapter(thisActivity, faliyaArrayList);
            sp_street_name.setAdapter(faliyaAdapter);
        }

        ArrayList<MaritalStatus> maritalStatusArrayList = databaseHelper.getMaritalStatus();
        if (maritalStatusArrayList != null) {
            StatusAdapter statusAdapter = new StatusAdapter(thisActivity, maritalStatusArrayList);
            sp_Marital_status.setAdapter(statusAdapter);
        }


        SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
        String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
        try {
            JSONObject jsonObject = new JSONObject(userDetail);
            String subCenterId = jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId");
            String userId = jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");
            ArrayList<MaritalStatus> aganvadiList = databaseHelper.getAganvadi(villageId, subCenterId);
            StatusAdapter aganvadiAdapter = new StatusAdapter(thisActivity, aganvadiList);
            sp_aganvali.setAdapter(aganvadiAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        Utils.ButtonClickEffect(v);
        switch (v.getId()) {
            case R.id.bt_family_identity:

                final Dialog dialog = new Dialog(thisActivity);
                LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = mInflater.inflate(R.layout.family_identity_layout, null);
                Utils.findAllTextView(thisActivity, ((ViewGroup) view.findViewById(R.id.ll_alert)));
                final EditText ed_bpl_number = (EditText) view.findViewById(R.id.ed_bpl_number);
                final EditText ed_bpl_card_number = (EditText) view.findViewById(R.id.ed_bpl_card_number);
                final EditText ed_rsby_number = (EditText) view.findViewById(R.id.ed_rsby_number);
                final EditText ed_maa_card_number = (EditText) view.findViewById(R.id.ed_maa_card_number);
                Button bt_save = (Button) view.findViewById(R.id.bt_save);
                Button bt_identity_cancel = (Button) view.findViewById(R.id.bt_identity_cancel);

                if (familyMember.getBplNumber() != null) {
                    ed_bpl_number.setText(familyMember.getBplNumber());
                }
                if (familyMember.getRationcardNrumber() != null) {
                    ed_bpl_card_number.setText(familyMember.getRationcardNrumber());
                }
                if (familyMember.getRsbycardNumber() != null) {
                    ed_rsby_number.setText(familyMember.getRsbycardNumber());
                }
                if (familyMember.getMacardNumber() != null) {
                    ed_maa_card_number.setText(familyMember.getMacardNumber());
                }

                bt_identity_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.ButtonClickEffect(v);
                        dialog.dismiss();

                        bplNumber = ed_bpl_number.getText().toString();
                        rationCardNumber = ed_bpl_card_number.getText().toString();
                        rsbyNumber = ed_rsby_number.getText().toString();
                        maaCardNumber = ed_maa_card_number.getText().toString();
                        familyMember.setBplNumber(bplNumber);
                        familyMember.setRationcardNrumber(rationCardNumber);
                        familyMember.setRsbycardNumber(rsbyNumber);
                        familyMember.setMacardNumber(maaCardNumber);
                    }
                });
                bt_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.ButtonClickEffect(v);
                        dialog.dismiss();
                        bplNumber = ed_bpl_number.getText().toString();
                        rationCardNumber = ed_bpl_card_number.getText().toString();
                        rsbyNumber = ed_rsby_number.getText().toString();
                        maaCardNumber = ed_maa_card_number.getText().toString();
                        familyMember.setBplNumber(bplNumber);
                        familyMember.setRationcardNrumber(rationCardNumber);
                        familyMember.setRsbycardNumber(rsbyNumber);
                        familyMember.setMacardNumber(maaCardNumber);
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
                View streetView = Inflater.inflate(R.layout.add_faliyu_layout, null);
                final EditText ed_street_name = (EditText) streetView.findViewById(R.id.ed_street_name);
                final RadioButton rdb_risky_area_yes = (RadioButton) streetView.findViewById(R.id.rdb_risky_area_yes);
                final RadioButton rdb_risky_area_no = (RadioButton) streetView.findViewById(R.id.rdb_risky_area_no);
                final Spinner sp_street_name_list = (Spinner) streetView.findViewById(R.id.sp_street_name_list);
                Button bt_faliyu_save = (Button) streetView.findViewById(R.id.bt_faliyu_save);
                Button bt_faliyu_cancel = (Button) streetView.findViewById(R.id.bt_faliyu_cancel);
                Button bt_faliyu_delete = (Button) streetView.findViewById(R.id.bt_faliyu_delete);

                String faliyaName = ed_street_name.getText().toString();
                SharedPreferences streetPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                String streetUserDetail = streetPreferences.getString(Constants.USER_ID, null);

                try {
                    streetArrayList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(streetUserDetail);
                    String userId = jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");
                    streetArrayList = databaseHelper.getFaliyaList(villageId);
                    if (streetArrayList != null) {
                        streetAdapter = new ReligionAdapter(thisActivity, streetArrayList);
                        sp_street_name_list.setAdapter(streetAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sp_street_name_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        LinearLayout linearLayout = (LinearLayout) view;
//                        TextView textView = (TextView) linearLayout.getChildAt(0);
                        if (position != 0) {
                            Religion street = streetArrayList.get(position);
                            ed_street_name.setText(street.getName());
                            if (street.getIsRisky().equals("1")) {
                                rdb_risky_area_yes.setChecked(true);
                                rdb_risky_area_no.setChecked(false);
                            } else {
                                rdb_risky_area_yes.setChecked(false);
                                rdb_risky_area_no.setChecked(true);
                            }
                        } else {
                            ed_street_name.setText("");
                            rdb_risky_area_yes.setChecked(false);
                            rdb_risky_area_yes.setChecked(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                rdb_risky_area_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.ButtonClickEffect(v);
                        rdb_risky_area_yes.setChecked(true);
                        rdb_risky_area_no.setChecked(false);
                        isRisky = "1";
                    }
                });
                rdb_risky_area_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.ButtonClickEffect(v);
                        rdb_risky_area_yes.setChecked(false);
                        rdb_risky_area_no.setChecked(true);
                        isRisky = "0";
                    }
                });
                bt_faliyu_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sp_street_name_list.getSelectedItemPosition() == 0) {
//                            Toast.makeText(thisActivity,"Insert",Toast.LENGTH_SHORT).show();
                            if (ed_street_name.getText().toString().length() != 0) {

                                String faliyaName = ed_street_name.getText().toString();
                                SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                                String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                                try {
                                    JSONObject jsonObject = new JSONObject(userDetail);
                                    String userId = jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");
                                    boolean flag = databaseHelper.insertFaliyu(villageId, faliyaName, isRisky, userId);
                                    if (flag) {
                                        streetDialog.dismiss();
                                        String str = thisActivity.getResources().getString(R.string.update);
                                        CustomToast customToast = new CustomToast(thisActivity, str);
                                        customToast.show();
                                        faliyaArrayList=new ArrayList<Religion>();
                                        faliyaArrayList = databaseHelper.getFaliyaList(villageId);
                                        faliyaAdapter = new ReligionAdapter(thisActivity, faliyaArrayList);
                                        sp_street_name.setAdapter(faliyaAdapter);
                                    }
                                    if (faliyaArrayList != null) {
                                        ReligionAdapter religionAdapter = new ReligionAdapter(thisActivity, faliyaArrayList);
                                        sp_street_name.setAdapter(religionAdapter);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                String str = thisActivity.getResources().getString(R.string.add_street);
                                CustomToast customToast = new CustomToast(thisActivity, str);
                                customToast.show();
                            }
                        } else {
//                            Toast.makeText(thisActivity,"Update",Toast.LENGTH_SHORT).show();
                            if (ed_street_name.getText().toString().length() != 0) {
                                Religion street = streetArrayList.get(sp_street_name_list.getSelectedItemPosition());
                                String faliyaName = ed_street_name.getText().toString();
                                street.setName(faliyaName);
                                street.setIsRisky(isRisky);
                                boolean flag = databaseHelper.updateFaliya(street);
                                streetArrayList.set(sp_street_name_list.getSelectedItemPosition(), street);
                                streetAdapter.notifyDataSetChanged();
                                if (flag) {
                                    streetDialog.dismiss();
                                    faliyaArrayList=new ArrayList<Religion>();
                                    faliyaArrayList = databaseHelper.getFaliyaList(villageId);
                                    faliyaAdapter = new ReligionAdapter(thisActivity, faliyaArrayList);
                                    sp_street_name.setAdapter(faliyaAdapter);
                                    String str = thisActivity.getResources().getString(R.string.update);
                                    CustomToast customToast = new CustomToast(thisActivity, str);
                                    customToast.show();
                                }
                            } else {
                                String str = thisActivity.getResources().getString(R.string.add_street);
                                CustomToast customToast = new CustomToast(thisActivity, str);
                                customToast.show();
                            }
                        }
                    }
                });

                bt_faliyu_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sp_street_name_list.getSelectedItemPosition() != 0) {
                            if (ed_street_name.getText().toString().length() != 0) {
                                Religion street = streetArrayList.get(sp_street_name_list.getSelectedItemPosition());
                                boolean flag = databaseHelper.deleteFaliya(street);
                                streetArrayList.remove(sp_street_name_list.getSelectedItemPosition());
                                streetAdapter.notifyDataSetChanged();
                                if(flag) {
                                    faliyaArrayList=new ArrayList<Religion>();
                                    faliyaArrayList = databaseHelper.getFaliyaList(villageId);
                                    faliyaAdapter = new ReligionAdapter(thisActivity, faliyaArrayList);
                                    sp_street_name.setAdapter(faliyaAdapter);
                                    String str = thisActivity.getResources().getString(R.string.delete_street);
                                    CustomToast customToast = new CustomToast(thisActivity, str);
                                    customToast.show();
                                }
                            }
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
                isAns = "1";
                Log.v("Ans", "Yes");
                break;
            case R.id.rdb_no:
                rdb_yes.setChecked(false);
                rdb_no.setChecked(true);
                isAns = "0";
                Log.v("Ans", "No");
                break;
            case R.id.rdb_sex_Male:
                rdb_sex_Male.setChecked(true);
                rdb_sex_Female.setChecked(false);
                isGender = "M";
                Log.v("m", "m");
                break;
            case R.id.rdb_sex_Female:
                rdb_sex_Male.setChecked(false);
                rdb_sex_Female.setChecked(true);
                isGender = "F";
                break;
            case R.id.txt_add_location:

                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);

                if (mLastLocation != null) {
                    double latitude = mLastLocation.getLatitude();
                    double longitude = mLastLocation.getLongitude();

                    txt_lat.setText("" + latitude);
                    txt_lng.setText("" + longitude);
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
                Random rand = new Random();
                int familyHealthNumber = rand.nextInt(900000) + 100000;
                familyMember.setHouseNumber(ed_house_number.getText().toString());
                String landmark = ed_landmark.getText().toString();
                familyMember.setLandmark(landmark);
                familyMember.setVillageId(villageId);
                String racial = strCast;
                familyMember.setRaciald(racial);
                String religion = strReligion;
                familyMember.setReligionId(religion);
                String isBpl = isAns;
                familyMember.setIsBpl(isBpl);
                familyMember.setBplNumber(bplNumber);
                familyMember.setRationcardNrumber(rationCardNumber);
                familyMember.setRsbycardNumber(rsbyNumber);
                familyMember.setMacardNumber(maaCardNumber);
                familyMember.setFirstName(ed_family_head_name.getText().toString());
                familyMember.setMiddleName(ed_husband_name.getText().toString());
                familyMember.setLastName(ed_Sir_Name.getText().toString());
                familyMember.setGender(isGender);
                familyMember.setMaritalStatus(maritalStatus);
                familyMember.setBirthDate(ed_Birth_date.getText().toString());
                familyMember.setMobileNo(ed_Mobile_number.getText().toString());
                familyMember.setEmamtafamilyId(ed_family_number.getText().toString());
                familyMember.setAnganwadiId(aaganvadiId);
                familyMember.setEmamtahealthId("" + familyHealthNumber);
                if (receipt_bitmap != null) {
                    familyMember.setPhotoValue(imageRealPath);
                    Uri uri = Uri.parse(imageRealPath);
                    String Name = new File(uri.getPath()).getName();
                    familyMember.setPhoto(Name);
                }
                if (mLastLocation != null) {
                    double latitude = mLastLocation.getLatitude();
                    double longitude = mLastLocation.getLongitude();

                    familyMember.setLattitudes("" + latitude);
                    familyMember.setLongitude("" + longitude);
                }
                if (strFaliyaId != null) {
                    familyMember.setFaliyu(strFaliyaId);
                }
                String validateAddFamilyDetailForm = FormValidation.validateFamilyRegistrationForm(familyMember, this);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    boolean isSave = databaseHelper.createNewFamily(familyMember);
                    if (isSave) {
                        boolean isMemberSave = databaseHelper.createMember(familyMember);
                        if (isMemberSave) {
                            String str = thisActivity.getResources().getString(R.string.family_new_success);
                            CustomToast customToast = new CustomToast(thisActivity, str);
                            customToast.show();
                            ed_house_number.setEnabled(false);
                            ed_family_head_name.setEnabled(false);
                            ed_husband_name.setEnabled(false);
                            ed_Mobile_number.setEnabled(false);
                            ed_Birth_date.setEnabled(false);
                            ed_landmark.setEnabled(false);
                            ed_Sir_Name.setEnabled(false);
                            sp_family_cast.setEnabled(false);
                            sp_family_dharm.setEnabled(false);
                            sp_Marital_status.setEnabled(false);
                            rdb_no.setEnabled(false);
                            rdb_yes.setEnabled(false);
                            rdb_sex_Female.setEnabled(false);
                            rdb_sex_Male.setEnabled(false);
                            thisActivity.finish();
                        }
                    }
                }
//                Member member=databaseHelper.getLastInsertedFamlily();
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
     * Creating google api client object
     */
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
//        getMenuInflater().inflate(R.menu.menu_new_family, menu);
        return true;
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

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void checkLocationService() {

        LocationManager lm = (LocationManager) thisActivity.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(thisActivity);
            dialog.setMessage("Location service not enable");
            dialog.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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

        LinearLayout linearLayout;
        TextView textView;
        if (parent.getId() == R.id.sp_family_cast) {
            if (position != 0) {
                Religion religion = castArrayList.get(position);
                strCast = religion.getId();
            }
//            Toast.makeText(thisActivity,religion.getName(),Toast.LENGTH_SHORT).show();
        } else if (parent.getId() == R.id.sp_family_dharm) {
            if (position != 0) {
                Religion religion = religionArrayList.get(position);
                strReligion = religion.getId();
            }
        } else if (parent.getId() == R.id.sp_street_name) {
            if (position != 0) {
                Religion religion = faliyaArrayList.get(position);
                strFaliyaId = religion.getId();
            }

        } else if (parent.getId() == R.id.sp_Marital_status) {
            if (position != 0) {
                linearLayout = (LinearLayout) view;
                textView = (TextView) linearLayout.getChildAt(0);
                maritalStatus = textView.getTag().toString();
            }

        } else if (parent.getId() == R.id.sp_aganvali) {
            if (position != 0) {
                linearLayout = (LinearLayout) view;
                textView = (TextView) linearLayout.getChildAt(0);
                aaganvadiId = textView.getTag().toString();
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                receipt_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));

                imgUserImage.setImageBitmap(receipt_bitmap);
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
