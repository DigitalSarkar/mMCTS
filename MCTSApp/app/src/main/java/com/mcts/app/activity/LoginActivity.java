package com.mcts.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mcts.app.R;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.Messages;
import com.mcts.app.utils.NetworkUtil;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.BaseActivity;
import com.mcts.app.volley.Constant;
import com.mcts.app.volley.IVolleyRespose;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher, IVolleyRespose {

    Activity thisActivity;
    LoginActivity mContext;
    private static String TAG = "LoginActivity";
    private Toolbar mToolbar;
    private TextView mTitle, txt_password;
    private Button bt_login_submit;
    private Button bt_one, bt_two, bt_three, bt_four, bt_five, bt_six, bt_seven, bt_eight, bt_nine, bt_change_pin, bt_zero, bt_back;
    private EditText ed_pin;
    StringBuilder strPinText = new StringBuilder();
    private boolean isStart = false;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_login);


        setToolBar();
        init();
        setProgressDialog();
        getScreenResolutuon();
    }

    private void getScreenResolutuon() {

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Log.v("Device height x width =", "" + height + " x " + width);

        float dens = getResources().getDisplayMetrics().density;
        Log.e("Resolution density", "" + dens);

        int density = getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
//                Toast.makeText(this, "LDPI", Toast.LENGTH_SHORT).show();
                Log.e("Resolution", "LDPI");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
//                Toast.makeText(this, "MDPI", Toast.LENGTH_SHORT).show();
                Log.e("Resolution", "MDPI");
                break;
            case DisplayMetrics.DENSITY_HIGH:
//                Toast.makeText(this, "HDPI", Toast.LENGTH_SHORT).show();
                Log.e("Resolution", "HDPI");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
//                Toast.makeText(this, "XHDPI", Toast.LENGTH_SHORT).show();
                Log.e("Resolution", "XHDPI");
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                Log.e("Resolution", "XXHDPI");
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                Log.e("Resolution", "XXXHDPI");
                break;
        }
    }

    private void setToolBar() {

        thisActivity = LoginActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.login));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
    }

    private void init() {

        bt_change_pin = (Button) findViewById(R.id.bt_change_pin);
        bt_back = (Button) findViewById(R.id.bt_back);
        bt_login_submit = (Button) findViewById(R.id.bt_login_submit);
        bt_one = (Button) findViewById(R.id.bt_one);
        bt_two = (Button) findViewById(R.id.bt_two);
        bt_three = (Button) findViewById(R.id.bt_three);
        bt_four = (Button) findViewById(R.id.bt_four);
        bt_five = (Button) findViewById(R.id.bt_five);
        bt_six = (Button) findViewById(R.id.bt_six);
        bt_seven = (Button) findViewById(R.id.bt_seven);
        bt_eight = (Button) findViewById(R.id.bt_eight);
        bt_nine = (Button) findViewById(R.id.bt_nine);
        bt_zero = (Button) findViewById(R.id.bt_zero);
        ed_pin = (EditText) findViewById(R.id.ed_pin);
        txt_password = (TextView) findViewById(R.id.txt_password);
        ed_pin.addTextChangedListener(this);

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        bt_change_pin.setTypeface(type, Typeface.BOLD);
        bt_back.setTypeface(type, Typeface.BOLD);
        bt_login_submit.setTypeface(type, Typeface.BOLD);
        txt_password.setTypeface(type, Typeface.BOLD);

        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_login));

        bt_login_submit.setOnClickListener(this);
        bt_one.setOnClickListener(this);
        bt_two.setOnClickListener(this);
        bt_three.setOnClickListener(this);
        bt_four.setOnClickListener(this);
        bt_five.setOnClickListener(this);
        bt_six.setOnClickListener(this);
        bt_seven.setOnClickListener(this);
        bt_eight.setOnClickListener(this);
        bt_nine.setOnClickListener(this);
        bt_zero.setOnClickListener(this);
        bt_back.setOnClickListener(this);
        bt_change_pin.setOnClickListener(this);

    }

    private void setProgressDialog() {
        progressDialog = new Dialog(thisActivity);
        LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressView = mInflater.inflate(R.layout.custom_progress_dialog, null);
        TextView txt_protext = (TextView) progressView.findViewById(R.id.txt_protext);
        txt_protext.setText(R.string.please_wait);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        txt_protext.setTypeface(type);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(progressView);

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
        lp.copyFrom(progressDialog.getWindow().getAttributes());

        lp.width = width;
        lp.height = height;
        progressDialog.getWindow().setAttributes(lp);

        progressDialog.setContentView(progressView);
        progressDialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        int id = v.getId();
        Editable strPin = ed_pin.getText();
        switch (id) {
            case R.id.bt_login_submit:
//                Intent intent = new Intent(thisActivity, DataOperationActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(thisActivity, HealthServicesActivity.class);
                startActivity(intent);
                thisActivity.finish();
                break;
            case R.id.bt_one:
                strPin = strPin.append(bt_one.getText());
                strPinText.append("*");
                txt_password.setText(strPinText);
                ed_pin.setText(strPin);

                break;
            case R.id.bt_two:
                strPin = strPin.append(bt_two.getText());
                strPinText.append("*");
                txt_password.setText(strPinText);
                ed_pin.setText(strPin);

                break;
            case R.id.bt_three:
                strPin = strPin.append(bt_three.getText());
                strPinText.append("*");
                txt_password.setText(strPinText);
                ed_pin.setText(strPin);

                break;
            case R.id.bt_four:
                strPin = strPin.append(bt_four.getText());
                strPinText.append("*");
                txt_password.setText(strPinText);
                ed_pin.setText(strPin);

                break;
            case R.id.bt_five:
                strPin = strPin.append(bt_five.getText());
                strPinText.append("*");
                txt_password.setText(strPinText);
                ed_pin.setText(strPin);

                break;
            case R.id.bt_six:
                strPin = strPin.append(bt_six.getText());
                strPinText.append("*");
                txt_password.setText(strPinText);
                ed_pin.setText(strPin);

                break;
            case R.id.bt_seven:
                strPin = strPin.append(bt_seven.getText());
                strPinText.append("*");
                txt_password.setText(strPinText);
                ed_pin.setText(strPin);

                break;
            case R.id.bt_eight:
                strPin = strPin.append(bt_eight.getText());
                strPinText.append("*");
                txt_password.setText(strPinText);
                ed_pin.setText(strPin);

                break;
            case R.id.bt_nine:
                strPin = strPin.append(bt_nine.getText());
                strPinText.append("*");
                txt_password.setText(strPinText);
                ed_pin.setText(strPin);

                break;
            case R.id.bt_zero:
                strPin = strPin.append(bt_zero.getText());
                strPinText.append("*");
                txt_password.setText(strPinText);
                ed_pin.setText(strPin);

                break;
            case R.id.bt_back:
                int length = ed_pin.getText().length();
                if (length > 0) {
                    ed_pin.getText().delete(length - 1, length);
                    String str = ed_pin.getText().toString();
                    if (str.length() != 0) {
                        str.substring(0, str.length() - 1);
                        strPinText = new StringBuilder();
                        if (str.length() == 1) {
                            strPinText.append("*");
                        } else if (str.length() == 2) {
                            strPinText.append("**");
                        } else if (str.length() == 3) {
                            strPinText.append("***");
                        } else if (str.length() == 4) {
                            strPinText.append("****");
                        } else if (str.length() == 5) {
                            strPinText.append("*****");
                        } else if (str.length() == 6) {
                            strPinText.append("******");
                        } else {
                            strPinText.append("");
                        }
                    } else {
                        strPinText = new StringBuilder();
                    }
                    txt_password.setText(strPinText);
                    strPin = ed_pin.getText();
                    ed_pin.setText(strPin);
                } else {
                    ed_pin.setText("");
                    strPinText = new StringBuilder();
                    txt_password.setText(strPinText);
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(final CharSequence s, int start, int before, int count) {
        if (s.toString().length() == 6 && txt_password.getText().length() == 6) {
            if (isStart == false) {
                isStart = true;
                SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                if (userDetail != null) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    boolean isValid = databaseHelper.getUserDetails(s.toString());
                    if (isValid) {
                        Intent intent = new Intent(thisActivity, HealthServicesActivity.class);
                        startActivity(intent);
                        thisActivity.finish();
                    } else {
                        isStart = false;
                        ed_pin.setText("");
                        strPinText = new StringBuilder();
                        txt_password.setText(strPinText);
                        String str = thisActivity.getResources().getString(R.string.wrong_pass);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                } else {
                    if (NetworkUtil.getConnectivityStatus(thisActivity) != 0) {
                        Log.i("call", "service");
                        Map<String, String> params = new HashMap<>();
                        params.put("password", s.toString());
                        params.put("mobileNumber", "null");
                        String strMAC = Utils.getMacAddress(thisActivity);
                        Log.e("MAC", strMAC);
                        params.put("macid", "34:31:11:70:DF:58");
//                        params.put("macid", "e4:90:7e:f0:40:5b");
                        String strIMEI = Utils.getIMEINumber(thisActivity);
                        Log.e("IMEI", strIMEI);
                        params.put("imeiid", "352116062009715");
//                        params.put("imeiid", "353327063688253");
                        callVolley(thisActivity, Constant.POST_REQUEST, Constants.USER_LOGIN, Constants.BASE_URL + Constants.USER_LOGIN, params, Constant.CALL_TIME_OUT, Constant.SHOULD_CACHE, Constant.VOLLEY_RETRY_COUNT, false, Constant.IS_PROGRESSDAILOG_CANCELABLE, mContext);

                    } else {
                        String str = thisActivity.getResources().getString(R.string.no_internet);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                        isStart = false;
                        ed_pin.setText("");
                        strPinText = new StringBuilder();
                        txt_password.setText(strPinText);
                    }
                }
            } else {
                ed_pin.setText("");
                strPinText = new StringBuilder();
                txt_password.setText(strPinText);
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();


            }
        };
    }

    @Override
    public void onVolleyResponse(int responseCode, String response, String ResponseTag) {
        super.onVolleyResponse(responseCode, response, ResponseTag);

        Log.v("User Response", response);
//              {"status":"ok","error":0,"userdetails":[{"userId":6,"subcenterId":1}]}
        try {
            JSONObject jsonUserDetail = new JSONObject(response);
            if (jsonUserDetail.getString("status").equals("ok")) {

                SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.USER_ID, jsonUserDetail.toString());
                editor.commit();

                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                databaseHelper.deleteUserDetails();
                JSONObject jsonMember = new JSONObject(response);
                if (jsonMember.getString("status").equals("ok")) {
                    JSONArray jsonArray = jsonMember.getJSONArray("userdetails");
                    if (databaseHelper.insertUser(jsonArray)) {
                        Intent intent = new Intent(thisActivity, HealthServicesActivity.class);
                        startActivity(intent);
                        thisActivity.finish();
                    } else {
                        CustomToast customToast = new CustomToast(thisActivity, "Error");
                        customToast.show();
                    }
                }
            } else {
                isStart = false;
                ed_pin.setText("");
                strPinText = new StringBuilder();
                txt_password.setText(strPinText);
                String str = thisActivity.getResources().getString(R.string.wrong_pass);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onVolleyError(int Code, String mError, String ResponseTag) {
        super.onVolleyError(Code, mError, ResponseTag);
        Log.e("VolleyError", mError);
        isStart = false;
        ed_pin.setText("");
        strPinText = new StringBuilder();
        txt_password.setText(strPinText);
    }
}
