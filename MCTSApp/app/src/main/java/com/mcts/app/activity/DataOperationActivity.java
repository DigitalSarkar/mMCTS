package com.mcts.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.mcts.app.R;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.Messages;
import com.mcts.app.utils.MyVolley;
import com.mcts.app.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataOperationActivity extends AppCompatActivity implements View.OnClickListener {

    Activity thisActivity;
    private static String TAG = "DataOperationActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private Button bt_send_data, bt_get_data, bt_go_ahead;
    private TextView txt_last_data_send, txt_last_data_get;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_operation);
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = DataOperationActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.login));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
    }

    private void init() {

        bt_send_data = (Button) findViewById(R.id.bt_send_data);
        bt_get_data = (Button) findViewById(R.id.bt_get_data);
        bt_go_ahead = (Button) findViewById(R.id.bt_go_ahead);
        txt_last_data_send = (TextView) findViewById(R.id.txt_last_data_send);
        txt_last_data_get = (TextView) findViewById(R.id.txt_last_data_get);

        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        bt_send_data.setTypeface(type, Typeface.BOLD);
        bt_get_data.setTypeface(type, Typeface.BOLD);
        bt_go_ahead.setTypeface(type, Typeface.BOLD);
        txt_last_data_send.setTypeface(type);
        txt_last_data_get.setTypeface(type);

        setProgressDialog();

        bt_go_ahead.setOnClickListener(this);
        bt_get_data.setOnClickListener(this);

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
//        getMenuInflater().inflate(R.menu.menu_data_operation, menu);
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

        switch (id) {
            case R.id.bt_go_ahead:
                Intent intent = new Intent(thisActivity, HealthServicesActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_get_data:
                if (NetworkUtil.getConnectivityStatus(thisActivity) != 0) {
                    RequestQueue queue = MyVolley.getRequestQueue();
                    StringRequest myReq = new StringRequest(Request.Method.GET,
                            Constants.BASE_URL + Constants.MEMBER_DETAIL,
                            createMyReqSuccessListener(),
                            createMyReqErrorListener());

                    queue.add(myReq);

                    progressDialog.show();
                } else {
                    String str=thisActivity.getResources().getString(R.string.no_internet);
                    CustomToast customToast = new CustomToast(thisActivity, str);
                    customToast.show();
                }
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
                        getFamilyDetails();
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
            RequestQueue queue = MyVolley.getRequestQueue();
            StringRequest myReq = new StringRequest(Request.Method.GET,
                    Constants.BASE_URL + Constants.FAMILY_DETAIL,
                    createFamilyDetailSuccessListener(),
                    createMyReqErrorListener());

            queue.add(myReq);
        } else {
            String str=thisActivity.getResources().getString(R.string.no_internet);
            CustomToast customToast = new CustomToast(thisActivity, str);
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
                progressDialog.dismiss();
                Log.v("VolleyError", error.getMessage());
            }
        };
    }
}
