package com.mcts.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.mcts.app.R;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.Messages;
import com.mcts.app.utils.MyVolley;
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

public class HealthSevaActivity extends BaseActivity implements View.OnClickListener,IVolleyRespose{

    Activity thisActivity;
    private static String TAG="HealthSevaActivity";
    HealthSevaActivity mContext;
    private Toolbar mToolbar;
    private TextView mTitle;
//    private Button bt_health_servey,bt_sagarbha_seva,bt_prasuti_seva,bt_bal_seva,bt_bal_rasikaran_seva,bt_kutumnb_kalyan_seva,bt_death_register,bt_rog_sarvey,bt_aasha,bt_staff_detail;
    private LinearLayout ll_health_servey,ll_sagarbha_seva,ll_bal_seva,ll_kutumnb_kalyan_seva;
    private Button bt_send_data, bt_get_data;
    private TextView txt_last_data_send, txt_last_data_get;
    private Dialog progressDialog;
    private JSONObject jsonObject;
    private int webIndex=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_seva);
        mContext=this;
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = HealthSevaActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.health_seva));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
    }

    private void init() {

        /*bt_health_servey=(Button)findViewById(R.id.bt_health_servey);
        bt_sagarbha_seva=(Button)findViewById(R.id.bt_sagarbha_seva);
        bt_prasuti_seva=(Button)findViewById(R.id.bt_prasuti_seva);
        bt_bal_seva=(Button)findViewById(R.id.bt_bal_seva);
        bt_bal_rasikaran_seva=(Button)findViewById(R.id.bt_bal_rasikaran_seva);
        bt_kutumnb_kalyan_seva=(Button)findViewById(R.id.bt_kutumnb_kalyan_seva);
        bt_death_register=(Button)findViewById(R.id.bt_death_register);
        bt_rog_sarvey=(Button)findViewById(R.id.bt_rog_sarvey);
        bt_aasha=(Button)findViewById(R.id.bt_aasha);
        bt_staff_detail=(Button)findViewById(R.id.bt_staff_detail);

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        bt_health_servey.setTypeface(type, Typeface.BOLD);
        bt_sagarbha_seva.setTypeface(type, Typeface.BOLD);
        bt_prasuti_seva.setTypeface(type, Typeface.BOLD);
        bt_bal_seva.setTypeface(type, Typeface.BOLD);
        bt_bal_rasikaran_seva.setTypeface(type, Typeface.BOLD);
        bt_kutumnb_kalyan_seva.setTypeface(type, Typeface.BOLD);
        bt_death_register.setTypeface(type, Typeface.BOLD);
        bt_rog_sarvey.setTypeface(type, Typeface.BOLD);
        bt_aasha.setTypeface(type, Typeface.BOLD);
        bt_staff_detail.setTypeface(type, Typeface.BOLD);

        bt_health_servey.setOnClickListener(this);
        bt_sagarbha_seva.setOnClickListener(this);
        bt_bal_rasikaran_seva.setOnClickListener(this);
        bt_bal_seva.setOnClickListener(this);*/

        bt_send_data = (Button) findViewById(R.id.bt_send_data);
        bt_get_data = (Button) findViewById(R.id.bt_get_data);
        txt_last_data_send = (TextView) findViewById(R.id.txt_last_data_send);
        txt_last_data_get = (TextView) findViewById(R.id.txt_last_data_get);
        ll_health_servey=(LinearLayout)findViewById(R.id.ll_health_servey);
        ll_sagarbha_seva=(LinearLayout)findViewById(R.id.ll_sagarbha_seva);
        ll_bal_seva=(LinearLayout)findViewById(R.id.ll_bal_seva);
        ll_kutumnb_kalyan_seva=(LinearLayout)findViewById(R.id.ll_kutumnb_kalyan_seva);

        setProgressDialog();
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_health_seva));

        bt_get_data.setOnClickListener(this);

        ll_health_servey.setOnClickListener(this);
        ll_sagarbha_seva.setOnClickListener(this);
        ll_kutumnb_kalyan_seva.setOnClickListener(this);
        ll_bal_seva.setOnClickListener(this);
    }

    private void setProgressDialog() {
        progressDialog = new Dialog(thisActivity);
        LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressView = mInflater.inflate(R.layout.custom_progress_dialog, null);
        TextView txt_protext = (TextView) progressView.findViewById(R.id.txt_protext);
        txt_protext.setText(Messages.DATA_DOWNLOAD);
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

        int id=v.getId();
        Intent intent;
        switch (id){
            case R.id.ll_health_servey:
                intent=new Intent(thisActivity,FamilyHeathSurveyActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_sagarbha_seva:
                intent=new Intent(thisActivity,SagrbhavsthaActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_get_data:
//                Map<String,String> param=new HashMap<>();
//                getWebData(Constants.webApiArrayList[webIndex],param);

//                RequestQueue queue = MyVolley.getRequestQueue();
//                StringRequest myReq = new StringRequest(Request.Method.GET,
//                        Constants.BASE_URL + Constants.MEMBER_DETAIL,
//                        createMyReqSuccessListener(),
//                        createMyReqErrorListener());
//                queue.add(myReq);
                break;
            case R.id.ll_bal_seva:
                intent=new Intent(thisActivity,BalHealthActivity.class);
                startActivity(intent);
                break;
        }
    }

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("MEMBER_DETAIL", response);
                try {
                    JSONObject jsonMemberObject = new JSONObject(response);
                    JSONArray jsonArray = jsonMemberObject.getJSONArray("memberdetails");
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.deleteMemberData();
                    if (databaseHelper.insertMemberData(jsonArray)) {
//                        getFamilyDetails();
                    } else {
                        progressDialog.dismiss();
                        CustomToast customToast = new CustomToast(thisActivity, "Error");
                        customToast.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void getFamilyDetails(){
        if (NetworkUtil.getConnectivityStatus(thisActivity) != 0) {

            SharedPreferences sharedPreferences=thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
            String userDetail=sharedPreferences.getString(Constants.USER_ID, null);
            Log.v("userDetail",userDetail);
            if(userDetail!=null) {
                try {
                    jsonObject = new JSONObject(userDetail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = MyVolley.getRequestQueue();
                StringRequest myReq = new StringRequest(Request.Method.POST,
                        Constants.BASE_URL + Constants.FAMILY_DETAIL,
                        createFamilyDetailSuccessListener(),
                        createMyReqErrorListener()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();

                        try {
                            params.put("subcenterid", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcenterId"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return params;
                    }
                };
                queue.add(myReq);
            }
        } else {
            CustomToast customToast = new CustomToast(thisActivity, Messages.NO_INTERNET);
            customToast.show();
        }
    }

    private Response.Listener<String> createFamilyDetailSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("Family DETAIL", response);
                try {
                    JSONObject jsonMemberObject = new JSONObject(response);
                    JSONArray jsonArray = jsonMemberObject.getJSONArray("familydetails");
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    databaseHelper.deleteFamilyDetail();
                    if (databaseHelper.insertFamilyDetail(jsonArray)) {
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        CustomToast customToast = new CustomToast(thisActivity, "Error");
                        customToast.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 500:
                            Log.v("VolleyError", "500");
                            break;
                    }
                    //Additional cases
                }

            }
        };
    }

    public void getWebData(String method, Map<String, String> params){
//        if(params==null) {
//            callVolleyWithoutParam(thisActivity, Constant.POST_REQUEST, method, Constants.BASE_URL + method, params, Constant.CALL_TIME_OUT, Constant.SHOULD_CACHE, Constant.VOLLEY_RETRY_COUNT, false, Constant.IS_PROGRESSDAILOG_CANCELABLE, mContext);
//        }else{
            callVolley(thisActivity, Constant.POST_REQUEST, method, Constants.BASE_URL + method, params, Constant.CALL_TIME_OUT, Constant.SHOULD_CACHE, Constant.VOLLEY_RETRY_COUNT, false, Constant.IS_PROGRESSDAILOG_CANCELABLE, mContext);
//        }
    }

    @Override
    public void onVolleyResponse(int responseCode, String mRes, String ResponseTag) {

        Log.v("mRes", mRes);
        if(ResponseTag.equalsIgnoreCase(Constants.webApiArrayList[webIndex])) {
//            saveDataInTable()
//            if()
            webIndex++;
            Map<String, String> params = new HashMap<>();
            getWebData(Constants.webApiArrayList[webIndex],params);
        }

    }

    @Override
    public void onVolleyError(int Code, String mError, String ResponseTag) {
        Log.v("mError",mError);
    }
}
