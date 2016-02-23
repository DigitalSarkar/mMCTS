package com.mcts.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.childhealth.ChildHealthActivity;
import com.mcts.app.activity.familywalfare.FamilyPlannigActivity;
import com.mcts.app.activity.maternalhealthservice.MaternalHealthServiceActivity;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.NetworkUtil;
import com.mcts.app.utils.TakePictureUtils;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.BaseActivity;
import com.mcts.app.volley.Constant;
import com.mcts.app.volley.CustomLoaderDialog;
import com.mcts.app.volley.IVolleyRespose;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HealthServicesActivity extends BaseActivity implements View.OnClickListener, IVolleyRespose {

    Activity thisActivity;
    private static String TAG = "HealthServicesActivity";
    HealthServicesActivity mContext;
    private Toolbar mToolbar;
    private TextView mTitle;
    private LinearLayout ll_health_servey, ll_sagarbha_seva, ll_bal_seva, ll_kutumnb_kalyan_seva;
    private LinearLayout ll_upload_data, ll_download_data;
    private TextView txt_last_data_send, txt_last_data_get,txt_user_name,txt_user_subcentre,txt_user_mob;
    private JSONObject jsonObject;
    private int webIndex = 0;
    private static final String WEBCALL_TAG = "WEB_CALL";
    private ImageView img_user, img_profile;
    CustomLoaderDialog customLoaderDialog;
    private Dialog progressDialog;

    //	Image Related Variable
    private String imageName = "item_picture";
    public static final int TAKE_PICTURE = 1;
    int REQUEST_CODE_LOAD_IMAGE = 3;
    int REQUEST_CODE_CAPTURE = 4;
    static String IMAGE_DIRECTORY_NAME = "BarberShop";
    int REQUEST_CODE_CROP_IMAGE = 10;
    Uri fileUri;
    private final int CROP_PIC = 2;
    protected boolean circleCrop = false;
    private String imageRealPath = null;
    private File compressFile;
    private Bitmap receipt_bitmap;
    private  String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_seva);
        mContext = this;
        setToolBar();
        init();
        loadUserImage();
        setDataOperationDate();

    }

    private void setToolBar() {

        thisActivity = HealthServicesActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.health_seva));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences=thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
        String userDetail=sharedPreferences.getString(Constants.USER_ID, null);
        try {
            JSONObject jsonObject = new JSONObject(userDetail);
            String subCenterId=jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId");
            userId=jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init() {

        ll_upload_data = (LinearLayout) findViewById(R.id.ll_upload_data);
        ll_download_data = (LinearLayout) findViewById(R.id.ll_download_data);
        txt_last_data_send = (TextView) findViewById(R.id.txt_last_data_send);
        txt_last_data_get = (TextView) findViewById(R.id.txt_last_data_get);
        txt_user_name = (TextView) findViewById(R.id.txt_user_name);
        txt_user_subcentre = (TextView) findViewById(R.id.txt_user_subcentre);
        txt_user_mob = (TextView) findViewById(R.id.txt_user_mob);
        ll_health_servey = (LinearLayout) findViewById(R.id.ll_health_servey);
        ll_sagarbha_seva = (LinearLayout) findViewById(R.id.ll_sagarbha_seva);
        ll_bal_seva = (LinearLayout) findViewById(R.id.ll_bal_seva);
        ll_kutumnb_kalyan_seva = (LinearLayout) findViewById(R.id.ll_kutumnb_kalyan_seva);
        img_profile = (ImageView) findViewById(R.id.img_user);

        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_health_seva));


        ll_upload_data.setOnClickListener(this);
        ll_download_data.setOnClickListener(this);
        ll_health_servey.setOnClickListener(this);
        ll_sagarbha_seva.setOnClickListener(this);
        ll_kutumnb_kalyan_seva.setOnClickListener(this);
        ll_bal_seva.setOnClickListener(this);
        img_profile.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_health_seva, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        Utils.ButtonClickEffect(v);
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.ll_health_servey:
                intent = new Intent(thisActivity, FamilyHeathSurveyActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_sagarbha_seva:
                intent = new Intent(thisActivity, MaternalHealthServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_download_data:
                if (NetworkUtil.checkInternetConnection(thisActivity)) {
                    Map<String, String> params = new HashMap<>();
                    try {
                        SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                        String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                        if (userDetail != null) {
                            jsonObject = new JSONObject(userDetail);
                            params.put("id", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                            getWebData(Constants.webApiArrayList[webIndex], params);
                            customLoaderDialog = new CustomLoaderDialog(thisActivity);
                            customLoaderDialog.show(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    String str = getResources().getString(R.string.no_internet);
                    CustomToast customToast = new CustomToast(thisActivity, str);
                    customToast.show();
                }
                break;

            case R.id.ll_upload_data:
                if (NetworkUtil.checkInternetConnection(thisActivity)) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    JSONArray jsonArray = databaseHelper.familyMamberData();
                    Log.v("MEMBER_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.MEMBER_POST, imageParams);
                        customLoaderDialog = new CustomLoaderDialog(thisActivity);
                        customLoaderDialog.show(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String str = getResources().getString(R.string.no_internet);
                    CustomToast customToast = new CustomToast(thisActivity, str);
                    customToast.show();
                }

                break;
            case R.id.ll_bal_seva:
                intent = new Intent(thisActivity, ChildHealthActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_kutumnb_kalyan_seva:
                intent = new Intent(thisActivity, FamilyPlannigActivity.class);
                startActivity(intent);
                break;
            case R.id.img_user:
                showProfileDialog();
                break;
        }
    }

    public void getWebData(String method, Map<String, String> params) {
        callVolley(thisActivity, Constant.POST_REQUEST, method, Constants.BASE_URL + method, params, Constant.CALL_TIME_OUT, Constant.SHOULD_CACHE, Constant.VOLLEY_RETRY_COUNT, false, Constant.IS_PROGRESSDAILOG_CANCELABLE, mContext);
    }

    @Override
    public void onVolleyResponse(int responseCode, String mRes, String ResponseTag) {

        Log.v("ResponseTag", ResponseTag);
        if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[0])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[0] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteMemberData();
                JSONObject jsonMember = new JSONObject(mRes);
                if (jsonMember.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonMember.getJSONArray("memberdetails");
                    if (databaseHelper.insertMemberData(jsonArray)) {
                        SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                        String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                        if (userDetail != null) {
                            jsonObject = new JSONObject(userDetail);
                            params.put("subcenterid", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                            getWebData(Constants.webApiArrayList[webIndex], params);
                        }
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[1])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[1] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteFamilyDetail();
                JSONObject jsonFamily = new JSONObject(mRes);
                if (jsonFamily.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonFamily.getJSONArray("familydetails");
                    if (databaseHelper.insertFamilyDetail(jsonArray)) {
                        params.put("subcenterid", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[2])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[2] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteVillageDetail();
                JSONObject jsonFamily = new JSONObject(mRes);
                if (jsonFamily.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonFamily.getJSONArray("villagedetails");
                    if (databaseHelper.insertVillageDetail(jsonArray)) {
                        params.put("subcenterid", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
//                        webIndex = 0;
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[3])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[3] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteAganwadieDetail();
                JSONObject jsonFamily = new JSONObject(mRes);
                if (jsonFamily.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonFamily.getJSONArray("anganwadidetails");
                    if (databaseHelper.insertAganwadi(jsonArray)) {
                        params.put("subcenterid", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        params.put("employeeId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("employeeId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[4])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[4] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteEmployeeDetails();
                JSONObject jsonFmployee = new JSONObject(mRes);
                if (jsonFmployee.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonFmployee.getJSONArray(Constants.EMPLOYEE_DETAILS);
                    if (databaseHelper.insertEmployeeDetails(jsonArray)) {
                        params.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[5])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[5] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteChildRegDetails();
                JSONObject jsonChild = new JSONObject(mRes);
                if (jsonChild.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonChild.getJSONArray(Constants.CHILD_REGISTRATION_DETAILS);
                    if (databaseHelper.insertChildRegDetails(jsonArray)) {
                        params.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[6])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[6] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteChildImmunizationDetails();
                JSONObject jsonChild = new JSONObject(mRes);
                if (jsonChild.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonChild.getJSONArray(Constants.CHILD_IMMUNIZATION_DETAILS);
                    if (databaseHelper.insertChildImmunizationDetails(jsonArray)) {
                        params.put("subcenterid", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[7])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[7] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteAdoptedMethodsDetails();
                JSONObject jsonChild = new JSONObject(mRes);
                if (jsonChild.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonChild.getJSONArray(Constants.ADOPTED_METHOD_DETAILS);
                    if (databaseHelper.insertAdoptedMethodDetails(jsonArray)) {
                        params.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[8])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[8] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteAncServiceDetails();
                JSONObject jsonChild = new JSONObject(mRes);
                if (jsonChild.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonChild.getJSONArray(Constants.ANCSERVICE_DETAILS);
                    if (databaseHelper.insertAncServicedDetails(jsonArray)) {
                        params.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[9])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[9] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteAncToxDetails();
                JSONObject jsonChild = new JSONObject(mRes);
                if (jsonChild.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonChild.getJSONArray(Constants.ANC_TOXOID_DETAILS);
                    if (databaseHelper.insertAncToxoidDetails(jsonArray)) {
                        params.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[10])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[10] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteHighRiskDetails();
                JSONObject jsonChild = new JSONObject(mRes);
                if (jsonChild.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonChild.getJSONArray(Constants.HIGH_RISK_DETAILS);
                    if (databaseHelper.insertHighRiskMotherDetails(jsonArray)) {
                        params.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[11])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[11] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteFaliyaDetails();
                JSONObject jsonChild = new JSONObject(mRes);
                if (jsonChild.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonChild.getJSONArray(Constants.FALIYA_DETAILS);
                    if (databaseHelper.insertFaliyaDetails(jsonArray)) {
                        params.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[12])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[12] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deletePostnatalDetails();
                JSONObject jsonChild = new JSONObject(mRes);
                if (jsonChild.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonChild.getJSONArray(Constants.POSTNATAL_SERVICE_DETAILS);
                    if (databaseHelper.insertPostnatalServiceDetails(jsonArray)) {
                        params.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[13])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[13] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deletePregnantWomenDetails();
                JSONObject jsonChild = new JSONObject(mRes);
                if (jsonChild.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonChild.getJSONArray(Constants.PREGNANT_WOMAN_DETAILS);
                    if (databaseHelper.insertPregnantWomenDetails(jsonArray)) {
                        params.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[14])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[14] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deletePregnancyOutComeDetails();
                JSONObject jsonChild = new JSONObject(mRes);
                if (jsonChild.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonChild.getJSONArray(Constants.PREGNANCY_OUTCOME_DETAILS);
                    if (databaseHelper.insertPregnancyOutComeDetails(jsonArray)) {
                        params.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[15])) {
            Log.v(WEBCALL_TAG, Constants.webApiArrayList[15] + " = " + mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteMemberHistoryDetails();
                JSONObject jsonChild = new JSONObject(mRes);
                if (jsonChild.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonChild.getJSONArray(Constants.MEMBER_HISTORY_DETAILS);
                    if (databaseHelper.insertMemberHistoryDetails(jsonArray)) {
                        params.put("subcenterid", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                        webIndex = 0;
                        loadUserImage();
                        customLoaderDialog.hide();
                        String str = thisActivity.getResources().getString(R.string.data_download_success);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date date = new Date();

                        SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.DATA_OPERATION, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.DOWNLOAD, "1");
                        editor.putString(Constants.DATA_DOWNLOAD_DATE_TIME, dateFormat.format(date));
                        editor.commit();

                        setDataOperationDate();
                    } else {
                        customLoaderDialog.hide();
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.MEMBER_POST)) {

            Log.v("membermodify Res", mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updateMemberTable();
                    JSONArray jsonArray = databaseHelper.getAllFamilyMember();
                    Log.v("FAMILY_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.FAMILY_POST, imageParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.FAMILY_POST)) {
            Log.v(Constants.FAMILY_POST + " Res", mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updateFamilyTable();
                    JSONArray jsonArray = databaseHelper.getMemberHistoryDetails();
                    Log.v("MEMBER_HISTORY_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.MEMBER_HISTORY_POST, imageParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (ResponseTag.equalsIgnoreCase(Constants.MEMBER_HISTORY_POST)) {
            Log.v(Constants.MEMBER_HISTORY_POST, mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updateHistoryTable();
                    JSONArray jsonArray = databaseHelper.getPregnantWomanDetails();
                    Log.v("PREGNANT_WOMAN_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.PREGNANT_WOMAN_POST, imageParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (ResponseTag.equalsIgnoreCase(Constants.PREGNANT_WOMAN_POST)) {

            Log.v(Constants.PREGNANT_WOMAN_POST, mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updatePregnantWomanTable();

                    JSONArray jsonArray = databaseHelper.getAncSerivceDetails();
                    Log.v("ANC_SERVICE_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.ANC_SERVICE_POST, imageParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (ResponseTag.equalsIgnoreCase(Constants.ANC_SERVICE_POST)) {
            Log.v(Constants.ANC_SERVICE_POST, mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updateAncServiceTable();

                    JSONArray jsonArray = databaseHelper.getHighRiskDetails();
                    Log.v("HIGH_RISK_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.HIGH_RISK_POST, imageParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (ResponseTag.equalsIgnoreCase(Constants.HIGH_RISK_POST)) {
            Log.v(Constants.HIGH_RISK_POST, mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updateHighRiskTable();

                    JSONArray jsonArray = databaseHelper.getAncToxoidDetails();
                    Log.v("ANC_TOXOID_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.ANC_TOXOID_POST, imageParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (ResponseTag.equalsIgnoreCase(Constants.ANC_TOXOID_POST)) {

            Log.v(Constants.ANC_TOXOID_POST, mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updateAncToxoidTable();

                    JSONArray jsonArray = databaseHelper.getFaliyaDetails();
                    Log.v("FALIYA_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.FALIYA_POST, imageParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (ResponseTag.equalsIgnoreCase(Constants.FALIYA_POST)) {

            Log.v(Constants.FALIYA_POST, mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updateFaliyaTable();

                    JSONArray jsonArray = databaseHelper.getPregOutcomeRegDetails();
                    Log.v("PREG_OUTCOME_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.PREG_OUTCOME_POST, imageParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (ResponseTag.equalsIgnoreCase(Constants.PREG_OUTCOME_POST)) {

            Log.v("PREG_OUTCOME_POST", Constants.PREG_OUTCOME_POST + " " + mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updatePregOutComeTable();

                    JSONArray jsonArray = databaseHelper.getChildRegDetails();
                    Log.v("CHILD_REG_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.CHILD_REG_POST, imageParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (ResponseTag.equalsIgnoreCase(Constants.CHILD_REG_POST)) {

            Log.v("CHILD_REG_POST", Constants.CHILD_REG_POST + " " + mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updateChildRegTable();

                    JSONArray jsonArray = databaseHelper.getPostnataServiceDetails();
                    Log.v("POSTNATAL_SERVICES_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.POSTNATAL_SERVICES_POST, imageParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (ResponseTag.equalsIgnoreCase(Constants.POSTNATAL_SERVICES_POST)) {

            Log.v("POSTNATAL_SERVICES_POST", Constants.POSTNATAL_SERVICES_POST + " " + mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updatePostnatalTable();

                    JSONArray jsonArray = databaseHelper.getChildImmunizationDetails();
                    Log.v("CHILD_IMMUNIZATION_POST", jsonArray.toString());
                    Map<String, String> imageParams = new HashMap<>();
                    try {
                        imageParams.put("details", jsonArray.toString());
                        getWebData(Constants.CHILD_IMMUNIZATION_POST, imageParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (ResponseTag.equalsIgnoreCase(Constants.CHILD_IMMUNIZATION_POST)) {
            customLoaderDialog.hide();
            Log.v("CHILD_IMMUNIZATION_POST", Constants.CHILD_IMMUNIZATION_POST + " " + mRes);
            try {
                JSONObject jsonObject = new JSONObject(mRes);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.updateChildImmunizationTable();

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();

                    SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.DATA_OPERATION, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.DATA_UPLOAD, "1");
                    editor.putString(Constants.DATA_UPLOAD_DATE_TIME, dateFormat.format(date));
                    editor.commit();

                    setDataOperationDate();

                    String str = thisActivity.getResources().getString(R.string.data_uploaded);
                    CustomToast customToast = new CustomToast(thisActivity, str);
                    customToast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onVolleyError(int Code, String mError, String ResponseTag) {
        Log.v("mError", mError);
        if (customLoaderDialog != null) {
            customLoaderDialog.hide();
            String str = thisActivity.getResources().getString(R.string.try_again);
            CustomToast customToast = new CustomToast(thisActivity, str);
            customToast.show();
        }
    }

    public void setDataOperationDate() {

        SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.DATA_OPERATION, MODE_PRIVATE);
        String downloadAt = sharedPreferences.getString(Constants.DATA_DOWNLOAD_DATE_TIME, null);
        String uploadAt = sharedPreferences.getString(Constants.DATA_UPLOAD_DATE_TIME, null);

        if (uploadAt != null) {
            txt_last_data_send.setText(thisActivity.getString(R.string.last_data_sent) + "\n" + uploadAt);
        }

        if (downloadAt != null) {
            txt_last_data_get.setText(thisActivity.getString(R.string.last_data_get) + "\n" + downloadAt);
        }
    }

    public void showProfileDialog() {

        progressDialog = new Dialog(thisActivity, R.style.DialogTheme);
        LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressView = mInflater.inflate(R.layout.custom_user_profile, null);
        Utils.findAllTextView(thisActivity, (ViewGroup) progressView.findViewById(R.id.ll_userprofile));
        img_user = (ImageView) progressView.findViewById(R.id.img_user);
        final EditText ed_user_name = (EditText) progressView.findViewById(R.id.ed_user_name);
        final EditText ed_user_middle_name = (EditText) progressView.findViewById(R.id.ed_user_middle_name);
        final EditText ed_user_sir_name = (EditText) progressView.findViewById(R.id.ed_user_sir_name);
        Button bt_user_save = (Button) progressView.findViewById(R.id.bt_user_save);
        Button bt_user_cancel = (Button) progressView.findViewById(R.id.bt_user_cancel);
        Typeface type = Typeface.createFromAsset(thisActivity.getAssets(), "SHRUTI.TTF");
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(progressView);
        Utils.hideSoftKeyboard(thisActivity);

        DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
        HashMap<String,String> hashMap=databaseHelper.getUserProfile(userId);
        ed_user_name.setText(hashMap.get("firstName"));
        ed_user_middle_name.setText(hashMap.get("middleName"));
        ed_user_sir_name.setText(hashMap.get("lastName"));

        if (hashMap.get("photo")!=null && hashMap.get("photo").length() > 5) {
            Uri uri = Uri.parse(hashMap.get("photovalue"));
            Bitmap image_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));
            img_user.setImageBitmap(image_bitmap);
        }

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

        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Camera", "Gallery",
                        "Remove", "Close"};

                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                builder.setTitle("Add Photo");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Camera")) {
                            captureImage();
                        } else if (items[item].equals("Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, REQUEST_CODE_CAPTURE);
                        } else if (items[item].equals("Close")) {
                            dialog.dismiss();
                        } else if (items[item].equals("Remove")) {
                            img_user.setImageResource(R.drawable.ic_launcher);
                        }
                    }
                });
                builder.show();
            }
        });

        bt_user_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> hashMap=new HashMap<String, String>();
                if (receipt_bitmap != null) {
                    Uri uri = Uri.parse(imageRealPath);
                    String Name = new File(uri.getPath()).getName();
                    hashMap.put("photovalue",imageRealPath);
                    hashMap.put("photo",Name);
                }

                hashMap.put("firstName",ed_user_name.getText().toString());
                hashMap.put("middleName",ed_user_middle_name.getText().toString());
                hashMap.put("lastName",ed_user_sir_name.getText().toString());

                SharedPreferences sharedPreferences=thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                String userDetail=sharedPreferences.getString(Constants.USER_ID, null);
                try {
                    JSONObject jsonObject = new JSONObject(userDetail);
                    String subCenterId=jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId");
                    String userId=jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");
                    hashMap.put("userId",userId);
                    DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
                    boolean isSaved=databaseHelper.updateUserProfile(hashMap);
                    if(isSaved){
                        loadUserImage();
                    }

                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

        bt_user_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.dismiss();

            }
        });

    }

    public void loadUserImage(){
        DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
        HashMap<String,String> hashMap=databaseHelper.getUserProfile(userId);
        txt_user_name.setText(hashMap.get("firstName") +" "+hashMap.get("middleName")+" "+hashMap.get("lastName"));
        txt_user_subcentre.setText(hashMap.get("subcentre"));
        txt_user_mob.setText(hashMap.get("mobileNumber"));

        if (hashMap.get("photo")!=null && hashMap.get("photo").length() > 5) {
            Uri uri = Uri.parse(hashMap.get("photovalue"));
            Bitmap image_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));
            img_profile.setImageBitmap(image_bitmap);
        }
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
                String selectedImagePath = fileUri.getPath();
                Intent intent = new Intent(this, ImageCroppingActivity.class);
                intent.putExtra("imagePath", selectedImagePath);
                startActivityForResult(intent, CROP_PIC);

            } else if (requestCode == CROP_PIC) {

                if (fileUri != null) {
                    File file = new File(fileUri.getPath());
                    boolean deleted = file.delete();
                }

                imageRealPath = data.getStringExtra("imagePath");
                if(!imageRealPath.equals("")) {
                    Uri uri = Uri.parse(imageRealPath);
                    receipt_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));

                    img_user.setImageBitmap(receipt_bitmap);
                }
//                img_profile.setImageBitmap(receipt_bitmap);
            } else if (requestCode == REQUEST_CODE_CAPTURE && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String selectedImagePath = cursor.getString(columnIndex);
                cursor.close();

                Intent intent = new Intent(this, ImageCroppingActivity.class);
                intent.putExtra("imagePath", selectedImagePath);
                startActivityForResult(intent, CROP_PIC);
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
