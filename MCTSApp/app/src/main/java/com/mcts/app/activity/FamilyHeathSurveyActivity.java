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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.mcts.app.R;
import com.mcts.app.activity.kutumb.FamilyListActivity;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.Messages;
import com.mcts.app.utils.MyVolley;
import com.mcts.app.utils.NetworkUtil;
import com.mcts.app.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FamilyHeathSurveyActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    Activity thisActivity;
    private static String TAG="HealthSevaActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private ImageView img_icon_home;
    private Spinner sp_village;
    private JSONObject jsonObject;
//    private Button bt_add_family,bt_edit_family,bt_delete_family,bt_transfer_family,bt_identity_family,bt_location_family,bt_add_family_member
//            ,bt_edit_family_member,bt_delete_family_member,bt_locate_family_member,bt_identity_family_member,bt_bank_detail;
    private LinearLayout ll_family_detail,ll_family_member_detail,ll_identity_family,ll_identity_family_member,ll_location_family,ll_bank_detail;
    private Dialog progressDialog;
    private String strVillageName,strVillageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_heath_survey);

        setToolBar();
        setProgressDialog();
        init();

    }

    private void setToolBar() {

        thisActivity = FamilyHeathSurveyActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.toolbar_home);
        mTitle = (TextView) mToolbar.findViewById(R.id.txt_title);
        img_icon_home = (ImageView) mToolbar.findViewById(R.id.img_icon_home);
        mTitle.setText(thisActivity.getResources().getString(R.string.health_servey));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float screenDensity = metrics.densityDpi;
        Log.i("screenDensity", "" + screenDensity);
    }

    private void init() {

//        bt_add_family=(Button)findViewById(R.id.bt_add_family);
//        bt_edit_family=(Button)findViewById(R.id.bt_edit_family);
//        bt_delete_family=(Button)findViewById(R.id.bt_delete_family);
//        bt_transfer_family=(Button)findViewById(R.id.bt_transfer_family);
//        bt_identity_family=(Button)findViewById(R.id.bt_identity_family);
//        bt_location_family=(Button)findViewById(R.id.bt_location_family);
//        bt_add_family_member=(Button)findViewById(R.id.bt_add_family_member);
//        bt_edit_family_member=(Button)findViewById(R.id.bt_edit_family_member);
//        bt_delete_family_member=(Button)findViewById(R.id.bt_delete_family_member);
//        bt_locate_family_member=(Button)findViewById(R.id.bt_locate_family_member);
//        bt_identity_family_member=(Button)findViewById(R.id.bt_identity_family_member);
//        bt_bank_detail=(Button)findViewById(R.id.bt_bank_detail);
//
//        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
//        bt_add_family.setTypeface(type, Typeface.BOLD);
//        bt_edit_family.setTypeface(type, Typeface.BOLD);
//        bt_delete_family.setTypeface(type, Typeface.BOLD);
//        bt_transfer_family.setTypeface(type, Typeface.BOLD);
//        bt_identity_family.setTypeface(type, Typeface.BOLD);
//        bt_location_family.setTypeface(type, Typeface.BOLD);
//        bt_add_family_member.setTypeface(type, Typeface.BOLD);
//        bt_edit_family_member.setTypeface(type, Typeface.BOLD);
//        bt_delete_family_member.setTypeface(type, Typeface.BOLD);
//        bt_locate_family_member.setTypeface(type, Typeface.BOLD);
//        bt_identity_family_member.setTypeface(type, Typeface.BOLD);
//        bt_bank_detail.setTypeface(type, Typeface.BOLD);
//
//        bt_add_family.setOnClickListener(this);
//        bt_edit_family.setOnClickListener(this);
//        bt_add_family_member.setOnClickListener(this);

        ll_family_detail=(LinearLayout)findViewById(R.id.ll_family_detail);
        ll_family_member_detail=(LinearLayout)findViewById(R.id.ll_family_member_detail);
        ll_identity_family=(LinearLayout)findViewById(R.id.ll_identity_family);
        ll_identity_family_member=(LinearLayout)findViewById(R.id.ll_identity_family_member);
        ll_location_family=(LinearLayout)findViewById(R.id.ll_location_family);
        ll_bank_detail=(LinearLayout)findViewById(R.id.ll_bank_detail);
        sp_village=(Spinner)findViewById(R.id.sp_village);


        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_family_sarvy));

        img_icon_home.setOnClickListener(this);
        ll_family_detail.setOnClickListener(this);
        ll_family_member_detail.setOnClickListener(this);

        sp_village.setOnItemSelectedListener(this);


        getVillageData();
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

    private void getVillageData(){

        if (NetworkUtil.getConnectivityStatus(thisActivity)!=0) {

            SharedPreferences sharedPreferences=thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
            String userDetail=sharedPreferences.getString(Constants.USER_ID, null);
            Log.v("userDetail",userDetail);
            if(userDetail!=null){
                try {
                    jsonObject=new JSONObject(userDetail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = MyVolley.getRequestQueue();
                StringRequest myReq = new StringRequest(Request.Method.POST,
                        Constants.BASE_URL + Constants.VILLAGE,
                        createMyReqSuccessListener(),
                        createMyReqErrorListener()){
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
                progressDialog.show();
            }
        } else {
            CustomToast customToast = new CustomToast(thisActivity, Messages.NO_INTERNET);
            customToast.show();
        }
    }

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("Village Data", response);
//                {"status":"ok","error":0,"villagedetails":[{"village":"VIJAPUR"}]}
                try {
                    JSONObject jsonVillage = new JSONObject(response);
                    if (jsonVillage.getString("status").equals("ok")) {
                        progressDialog.dismiss();
                        JSONArray jsonVillageArray=jsonVillage.getJSONArray("villagedetails");
                        ArrayList<MaritalStatus> villageArray=new ArrayList<>();
                        for(int i=0;i<jsonVillageArray.length();i++){
                            JSONObject jsonObject=jsonVillageArray.getJSONObject(i);
                            MaritalStatus maritalStatus=new MaritalStatus();
                            maritalStatus.setId(jsonObject.getString("villageId"));
                            maritalStatus.setStatus(jsonObject.getString("village"));
                            villageArray.add(maritalStatus);
                        }

                        StatusAdapter masikAdapter=new StatusAdapter(thisActivity,villageArray);
                        sp_village.setAdapter(masikAdapter);

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

    @Override
    public void onClick(View v) {

        Intent intent=null;
        switch (v.getId()){
//            case R.id.bt_add_family:
//                intent=new Intent(thisActivity, UpdateKutumbActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.bt_identity_family:
//                intent=new Intent(thisActivity,FamilyIdentityActivity.class);
//                startActivity(intent);
//                break;
            case R.id.ll_family_detail:
                intent=new Intent(thisActivity,FamilyListActivity.class);
                int isFamily=0;
                intent.putExtra("strVillageId",strVillageId);
                intent.putExtra("strVillageName",strVillageName);
                intent.putExtra("isFamily",isFamily);
                startActivity(intent);
                break;
            case R.id.ll_family_member_detail:
                intent=new Intent(thisActivity,FamilyListActivity.class);
                int family=1;
                intent.putExtra("strVillageId",strVillageId);
                intent.putExtra("strVillageName",strVillageName);
                intent.putExtra("isFamily",family);
                startActivity(intent);
                break;
//            case R.id.bt_add_family_member:
//                intent=new Intent(thisActivity,AddFamilyMemberActivity.class);
//                startActivity(intent);
//                break;
            case R.id.img_icon_home:
                Toast.makeText(thisActivity,"Home",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        LinearLayout linearLayout;
        TextView textView;
        switch (parent.getId()) {
            case R.id.sp_village:
                linearLayout = (LinearLayout) view;
                textView = (TextView) linearLayout.getChildAt(0);
                strVillageId=textView.getTag().toString();
                strVillageName=textView.getText().toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 500:
                            Log.e("VolleyError", "Unexpected response");
                            break;
                    }
                }
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("VolleyError", "TimeoutError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("VolleyError", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("VolleyError", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("VolleyError", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("VolleyError", "ParseError");
                }

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_family_heath_survey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            thisActivity.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
        // Handle your other action bar items...
    }
}
