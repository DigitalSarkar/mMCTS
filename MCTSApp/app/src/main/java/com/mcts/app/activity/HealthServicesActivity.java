package com.mcts.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.Member;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.BaseActivity;
import com.mcts.app.volley.Constant;
import com.mcts.app.volley.CustomLoaderDialog;
import com.mcts.app.volley.IVolleyRespose;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HealthServicesActivity extends BaseActivity implements View.OnClickListener, IVolleyRespose {

    Activity thisActivity;
    private static String TAG = "HealthServicesActivity";
    HealthServicesActivity mContext;
    private Toolbar mToolbar;
    private TextView mTitle;
    private LinearLayout ll_health_servey, ll_sagarbha_seva, ll_bal_seva, ll_kutumnb_kalyan_seva;
    private Button bt_send_data, bt_get_data;
    private TextView txt_last_data_send, txt_last_data_get;
    private JSONObject jsonObject;
    private int webIndex = 0;
    private ImageView img_pic;
    CustomLoaderDialog customLoaderDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_seva);
        mContext = this;
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = HealthServicesActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.health_seva));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
    }

    private void init() {

        bt_send_data = (Button) findViewById(R.id.bt_send_data);
        bt_get_data = (Button) findViewById(R.id.bt_get_data);
        txt_last_data_send = (TextView) findViewById(R.id.txt_last_data_send);
        txt_last_data_get = (TextView) findViewById(R.id.txt_last_data_get);
        ll_health_servey = (LinearLayout) findViewById(R.id.ll_health_servey);
        ll_sagarbha_seva = (LinearLayout) findViewById(R.id.ll_sagarbha_seva);
        ll_bal_seva = (LinearLayout) findViewById(R.id.ll_bal_seva);
        ll_kutumnb_kalyan_seva = (LinearLayout) findViewById(R.id.ll_kutumnb_kalyan_seva);
        img_pic = (ImageView) findViewById(R.id.img_pic);

        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_health_seva));

        bt_get_data.setOnClickListener(this);
        bt_send_data.setOnClickListener(this);
        ll_health_servey.setOnClickListener(this);
        ll_sagarbha_seva.setOnClickListener(this);
        ll_kutumnb_kalyan_seva.setOnClickListener(this);
        ll_bal_seva.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_health_seva, menu);
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
        Utils.ButtonClickEffect(v);
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.ll_health_servey:
                intent = new Intent(thisActivity, FamilyHeathSurveyActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_sagarbha_seva:
                intent = new Intent(thisActivity, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_get_data:
                Map<String, String> params = new HashMap<>();
                try {
                    SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                    String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                    if (userDetail != null) {
                        jsonObject = new JSONObject(userDetail);
                        params.put("id", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcenterId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                        customLoaderDialog = new CustomLoaderDialog(thisActivity);
                        customLoaderDialog.show(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.bt_send_data:
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                JSONArray jsonArray = databaseHelper.familyMamberData();
                Log.v("Post Data", jsonArray.toString());
                Map<String, String> imageParams = new HashMap<>();
                try {
                    imageParams.put("details", jsonArray.toString());
                    getWebData(Constants.MEMBER_POST, imageParams);
                    customLoaderDialog = new CustomLoaderDialog(thisActivity);
                    customLoaderDialog.show(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.ll_bal_seva:
                intent = new Intent(thisActivity, MainActivity.class);
                startActivity(intent);
//                Map<String, String> params1 = new HashMap<>();
//                getWebData("postimg", params1);
                break;
            case R.id.ll_kutumnb_kalyan_seva:
                intent = new Intent(thisActivity, MainActivity.class);
                startActivity(intent);
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
            Log.v("MEMBER_DETAIL ", mRes);
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
                            params.put("subcenterid", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcenterId"));
                            getWebData(Constants.webApiArrayList[webIndex], params);
                        }
                    } else {
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[1])) {
            Log.v("FAMILY_DETAIL ", mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteFamilyDetail();
                JSONObject jsonFamily = new JSONObject(mRes);
                if (jsonFamily.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonFamily.getJSONArray("familydetails");
                    if (databaseHelper.insertFamilyDetail(jsonArray)) {
                        params.put("subcenterid", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcenterId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
                    } else {
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[2])) {
            Log.v("VILLAGE", mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteVillageDetail();
                JSONObject jsonFamily = new JSONObject(mRes);
                if (jsonFamily.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonFamily.getJSONArray("villagedetails");
                    if (databaseHelper.insertVillageDetail(jsonArray)) {
                        params.put("subcenterid", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcenterId"));
                        getWebData(Constants.webApiArrayList[webIndex], params);
//                        webIndex = 0;
                    } else {
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[3])) {
            Log.v("AGANWADI", mRes);
            webIndex++;
            Map<String, String> params = new HashMap<>();
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteAganwadieDetail();
                JSONObject jsonFamily = new JSONObject(mRes);
                if (jsonFamily.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonFamily.getJSONArray("anganwadidetails");
                    if (databaseHelper.insertAganwadi(jsonArray)) {
                        params.put("subcenterid", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcenterId"));
                        webIndex = 0;
                        customLoaderDialog.hide();
                        String str = thisActivity.getResources().getString(R.string.data_download_success);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                        SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.DATA_DOWNLOAD, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.DATA_DOWNLOAD, "1");
                        editor.commit();
                    } else {
                        String str = getResources().getString(R.string.try_again);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (ResponseTag.equalsIgnoreCase(Constants.MEMBER_POST)) {
            customLoaderDialog.hide();
            Log.v("membermodify Res", mRes);
        } else if (ResponseTag.equalsIgnoreCase("postimg")) {
            Log.v("Image Res", mRes);
            try {
                JSONObject jsonImage = new JSONObject(mRes);
                if (jsonImage.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonImage.getJSONArray("familydetails");
                    JSONObject image = jsonArray.getJSONObject(0);

                    byte[] decodedString = Base64.decode(image.getString("photo"), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    img_pic.setImageBitmap(decodedByte);
                    String filePath = Utils.saveIamge(decodedByte, "MCTS", "Profile Pic");
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    boolean isStored = databaseHelper.storeImage(filePath);
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
//            DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
//            databaseHelper.deleteMemberData();
//            databaseHelper.deleteFamilyDetail();
//            databaseHelper.deleteVillageDetail();
//            databaseHelper.deleteAganwadieDetail();
            customLoaderDialog.hide();
        }
    }
}
