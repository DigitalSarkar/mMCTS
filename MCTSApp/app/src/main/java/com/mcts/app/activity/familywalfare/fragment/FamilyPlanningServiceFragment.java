package com.mcts.app.activity.familywalfare.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.familywalfare.FamilyPlannigActivity;
import com.mcts.app.activity.maternalhealthservice.ChildListActivity;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.CommonClass;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.DatePickerFragment;
import com.mcts.app.utils.FormValidation;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Raj on 2/1/2016.
 */
public class FamilyPlanningServiceFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    private View rootView;
    private Activity thisActivity;
    private FamilyPlannigActivity familyPlannigActivity;
    private TextView txt_village_name,txt_fm_walfare_name;
    private Spinner sp_fm_method,sp_coppert_type;
    private EditText ed_fm_walfare_date,ed_service_place,ed_service_place_name,ed_service_given_by;
    private Button bt_save_fm,bt_edit_fm;
    private String data;
    private JSONObject jsonData;
    private Dialog progressDialog;
    private String dirstID, dirstName, optionId, stateId, stateName, optionName;
    private String talukaID, talukaName, selectionId, selectionName, employeeId, designName, employeeName;
    private String familyWalfare,villageId,memberId,emamtahealthId,emamtafamilyId,coppert_type;
    private LinearLayout ll_coppert_type;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.family_planning_service, container, false);
            thisActivity=getActivity();

            Bundle args = getArguments();
            if (args != null && args.containsKey("data")) {
                data = args.getString("data");
            }

            familyPlannigActivity = (FamilyPlannigActivity) getActivity();
            familyPlannigActivity.setToolBarTitle(thisActivity.getResources().getString(R.string.new_kutumnb_kalyan_seva), false);
            CommonClass.hideSoftKeyboard(thisActivity);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;

    }

    private void init() {

        Typeface type = Typeface.createFromAsset(thisActivity.getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) rootView.findViewById(R.id.ll_family_plan));
        txt_village_name= (TextView) rootView.findViewById(R.id.txt_village_name);
        txt_fm_walfare_name= (TextView) rootView.findViewById(R.id.txt_fm_walfare_name);
        sp_fm_method= (Spinner) rootView.findViewById(R.id.sp_fm_method);
        sp_coppert_type= (Spinner) rootView.findViewById(R.id.sp_coppert_type);
        ed_fm_walfare_date= (EditText) rootView.findViewById(R.id.ed_fm_walfare_date);
        ed_service_place= (EditText) rootView.findViewById(R.id.ed_service_place);
        ed_service_place_name= (EditText) rootView.findViewById(R.id.ed_service_place_name);
        ed_service_given_by= (EditText) rootView.findViewById(R.id.ed_service_given_by);
        bt_save_fm= (Button) rootView.findViewById(R.id.bt_save_fm);
        bt_edit_fm= (Button) rootView.findViewById(R.id.bt_edit_fm);
        ll_coppert_type= (LinearLayout) rootView.findViewById(R.id.ll_coppert_type);
        bt_edit_fm.setVisibility(View.GONE);

        DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
        ArrayList<MaritalStatus> familyWalfareList = databaseHelper.getFamilyPlanningData();
        familyWalfareList.remove(familyWalfareList.size()-1);
        familyWalfareList.remove(familyWalfareList.size() - 1);
        StatusAdapter familyWalfareAdapter = new StatusAdapter(thisActivity, familyWalfareList);
        sp_fm_method.setAdapter(familyWalfareAdapter);

        String[] array = thisActivity.getResources().getStringArray(R.array.copper_type);
        ArrayList<MaritalStatus> coppertTypeArrayList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            MaritalStatus maritalStatus = new MaritalStatus();
            maritalStatus.setId("" + i);
            maritalStatus.setStatus(array[i]);
            coppertTypeArrayList.add(maritalStatus);
        }

        StatusAdapter coppertTypeAdapter = new StatusAdapter(thisActivity, coppertTypeArrayList);
        sp_coppert_type.setAdapter(coppertTypeAdapter);

        sp_fm_method.setOnItemSelectedListener(this);
        sp_coppert_type.setOnItemSelectedListener(this);


        try {
            jsonData=new JSONObject(data);
            txt_village_name.setText(jsonData.getString("villageName"));
            txt_fm_walfare_name.setText(jsonData.getString("name"));
            memberId=jsonData.getString("memberId");
            emamtahealthId=jsonData.getString("emamtahealthId");
            emamtafamilyId=jsonData.getString("emamtafamilyId");
            villageId=jsonData.getString("villageId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ed_fm_walfare_date.setOnClickListener(this);
        ed_service_place.setOnClickListener(this);
        bt_save_fm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ed_fm_walfare_date:
                showDatePicker();
                break;
            case R.id.ed_service_place:
                showDeliveryPlaceDialog();
                break;
            case R.id.bt_save_fm:
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("memberId",memberId);
                hashMap.put("emamtahealthId",emamtahealthId);
                hashMap.put("emamtafamilyId",emamtafamilyId);
                hashMap.put("villageId",villageId);
                hashMap.put("facilityId",optionId);
                hashMap.put("employeeId",employeeId);
                hashMap.put("adoptedfpMethod",familyWalfare);
                hashMap.put("copperttypeId",coppert_type);
                hashMap.put("adoptedDate",ed_fm_walfare_date.getText().toString());
                SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, thisActivity.MODE_PRIVATE);
                String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                try {
                    JSONObject jsonObject = new JSONObject(userDetail);
                    hashMap.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                    String userId = jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");
                    hashMap.put("createdbyuserId", userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String validation = FormValidation.validFPDetails(hashMap, thisActivity);
                if (validation.length() != 0) {
                    CustomLoaderDialog customLoaderDialog = new CustomLoaderDialog(thisActivity);
                    customLoaderDialog.showValidationDialog(validation);
                } else {
                    DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
                    boolean isSaved=databaseHelper.saveFPMethod(hashMap);
                    if (isSaved) {
                        String str = thisActivity.getResources().getString(R.string.insert);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }

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
        date.setCallBack(walFareDate);

        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener walFareDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd/M/yyyy");
            try {
                Date tt1Date = simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));
                ed_fm_walfare_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    public void showDeliveryPlaceDialog() {

        progressDialog = new Dialog(thisActivity, R.style.DialogTheme);
        LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressView = mInflater.inflate(R.layout.custom_delivery_place, null);
        Utils.findAllTextView(thisActivity, (ViewGroup) progressView.findViewById(R.id.ll_delivery_places));
        final Spinner sp_delivery_place = (Spinner) progressView.findViewById(R.id.sp_delivery_place);
        final Spinner sp_del_state = (Spinner) progressView.findViewById(R.id.sp_del_state);
        final Spinner sp_del_dirst = (Spinner) progressView.findViewById(R.id.sp_del_dirst);
        final Spinner sp_del_tal = (Spinner) progressView.findViewById(R.id.sp_del_tal);
        final Spinner sp_del_village = (Spinner) progressView.findViewById(R.id.sp_del_village);
        final Spinner sp_del_person_name = (Spinner) progressView.findViewById(R.id.sp_del_person_name);
        Button bt_save_place = (Button) progressView.findViewById(R.id.bt_save_place);


        DatabaseHelper databaseStateHelper = new DatabaseHelper(thisActivity);

        ArrayList<MaritalStatus> facilityArray = databaseStateHelper.getFacilityData();
        StatusAdapter facilityAdapter = new StatusAdapter(thisActivity, facilityArray);
        sp_delivery_place.setAdapter(facilityAdapter);

        final ArrayList<MaritalStatus> stateArray = databaseStateHelper.getStateData();
        StatusAdapter stateAdapter = new StatusAdapter(thisActivity, stateArray);
        sp_del_state.setAdapter(stateAdapter);

        DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
        ArrayList<MaritalStatus> districtArray = databaseHelper.getTaluka("null");
        StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
        sp_del_tal.setAdapter(districtAdapter);

        sp_delivery_place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                LinearLayout linearLayout = (LinearLayout) view;
                TextView textView = (TextView) linearLayout.getChildAt(0);
                optionId = textView.getTag().toString();
                optionName = textView.getText().toString();

               /* if (position != 0) {
                    Toast.makeText(thisActivity, optionId, Toast.LENGTH_SHORT).show();
                }*/

                sp_del_dirst.setSelection(0);
                sp_del_tal.setSelection(0);
                sp_del_state.setSelection(0);

                if (position == 1) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getSubCenterData("null");
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_village.setAdapter(districtAdapter);
                } else if (position == 2) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getPhcData("null");
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_village.setAdapter(districtAdapter);
                } else if (position == 9) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getVillages("null");
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_village.setAdapter(districtAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_del_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            LinearLayout linearLayout;
            TextView textView;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                linearLayout = (LinearLayout) view;
                textView = (TextView) linearLayout.getChildAt(0);
                stateId = textView.getTag().toString();
                stateName = textView.getText().toString();
//                if(position!=0) {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                ArrayList<MaritalStatus> districtArray = databaseHelper.getDistrictData();
                StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                sp_del_dirst.setAdapter(districtAdapter);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_del_dirst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            LinearLayout linearLayout;
            TextView textView;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    dirstID = textView.getTag().toString();
                    dirstName = textView.getText().toString();

                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getTaluka(dirstID);
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_tal.setAdapter(districtAdapter);
                    sp_del_tal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                linearLayout = (LinearLayout) view;
                                textView = (TextView) linearLayout.getChildAt(0);
                                talukaID = textView.getTag().toString();
                                talukaName = textView.getText().toString();

                                if (sp_delivery_place.getSelectedItemPosition() == 1) {
                                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                                    ArrayList<MaritalStatus> districtArray = databaseHelper.getSubCenterData(talukaID);
                                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                                    sp_del_village.setAdapter(districtAdapter);
                                } else if (sp_delivery_place.getSelectedItemPosition() == 2) {
                                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                                    ArrayList<MaritalStatus> districtArray = databaseHelper.getPhcData(talukaID);
                                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                                    sp_del_village.setAdapter(districtAdapter);
                                } else if (sp_delivery_place.getSelectedItemPosition() == 9) {
                                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                                    ArrayList<MaritalStatus> districtArray = databaseHelper.getVillages(talukaID);
                                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                                    sp_del_village.setAdapter(districtAdapter);
                                }
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_del_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            LinearLayout linearLayout;
            TextView textView;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                linearLayout = (LinearLayout) view;
                textView = (TextView) linearLayout.getChildAt(0);
                selectionId = textView.getTag().toString();
                selectionName = textView.getText().toString();
                if (sp_delivery_place.getSelectedItemPosition() == 1) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getDesignationBySubcenter(talukaID, selectionId);
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_person_name.setAdapter(districtAdapter);
                } else if (sp_delivery_place.getSelectedItemPosition() == 2) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getDesignation(optionId, selectionId);
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_person_name.setAdapter(districtAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_del_person_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            LinearLayout linearLayout;
            TextView textView;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    String[] array = textView.getTag().toString().split(",");
                    employeeId = array[0];
                    designName = array[1];
                    employeeName = textView.getText().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        bt_save_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ed_service_place.setText(stateName + "," + dirstName + "," + talukaName + "," + selectionName);
                ed_service_place_name.setText(optionName);
                ed_service_given_by.setText(employeeName);
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            LinearLayout linearLayout;
            TextView textView;
            switch (parent.getId()) {

                case R.id.sp_fm_method:
                    if(position!=0) {
                        if(position==5){
                            ll_coppert_type.setVisibility(View.VISIBLE);
                        }else{
                            ll_coppert_type.setVisibility(View.GONE);
                        }
                        linearLayout = (LinearLayout) view;
                        textView = (TextView) linearLayout.getChildAt(0);
                        familyWalfare = textView.getTag().toString();
                    }else{
                        familyWalfare=null;
                    }
                    break;
                case R.id.sp_coppert_type:
                    if(position!=0) {

                        linearLayout = (LinearLayout) view;
                        textView = (TextView) linearLayout.getChildAt(0);
                        coppert_type = textView.getTag().toString();
                    }else{
                        coppert_type=null;
                    }
                    break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
