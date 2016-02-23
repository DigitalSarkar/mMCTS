package com.mcts.app.activity.familywalfare.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.familywalfare.FamilyPlannigActivity;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.CommonClass;
import com.mcts.app.utils.DatePickerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Raj on 2/2/2016.
 */
public class EditFpMethodFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private View rootView;
    private Activity thisActivity;
    private FamilyPlannigActivity familyPlannigActivity;
    private String data,complicationResult,fpComplicationResult,dischargeReason,adoptedfpmethodId;
    private JSONObject jsonData;
    private EditText ed_discontinue_date, ed_complication_date;
    private Spinner sp_discontinue_reason, sp_post_fp_complication, sp_complication_result;
    private Button bt_save_fmp, bt_edit_fmp;
    private LinearLayout ll_comp;

    public static EditFpMethodFragment newInstance(String title) {
        EditFpMethodFragment frag = new EditFpMethodFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.edit_family_plannig_method, container, false);
            thisActivity = getActivity();

            Bundle args = getArguments();
            if (args != null && args.containsKey("data")) {
                data = args.getString("data");
            }

            familyPlannigActivity = (FamilyPlannigActivity) getActivity();
            familyPlannigActivity.setToolBarTitle(thisActivity.getResources().getString(R.string.edit_fm_walfare), false);
            CommonClass.hideSoftKeyboard(thisActivity);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);


        try {
            jsonData=new JSONObject(data);
            adoptedfpmethodId=jsonData.getString("adoptedfpmethodId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init(View view) {

        ed_discontinue_date = (EditText) view.findViewById(R.id.ed_discontinue_date);
        ed_complication_date = (EditText) view.findViewById(R.id.ed_complication_date);
        sp_discontinue_reason = (Spinner) view.findViewById(R.id.sp_discontinue_reason);
        sp_post_fp_complication = (Spinner) view.findViewById(R.id.sp_post_fp_complication);
        sp_complication_result = (Spinner) view.findViewById(R.id.sp_complication_result);
        ll_comp = (LinearLayout) view.findViewById(R.id.ll_comp);
        bt_save_fmp = (Button) view.findViewById(R.id.bt_save_fmp);
        bt_edit_fmp = (Button) view.findViewById(R.id.bt_edit_fmp);

        String[] fpResultArray = this.getResources().getStringArray(R.array.complications_results);
        ArrayList<MaritalStatus> complicationResultArrayList = new ArrayList<>();
        for (int i = 0; i < fpResultArray.length; i++) {
            MaritalStatus reason = new MaritalStatus();
            reason.setId("" + i);
            reason.setStatus(fpResultArray[i]);
            complicationResultArrayList.add(reason);
        }
        StatusAdapter complicationResultAdapter = new StatusAdapter(getContext(), complicationResultArrayList);
        sp_complication_result.setAdapter(complicationResultAdapter);


        String[] fpArray = this.getResources().getStringArray(R.array.post_fp_complications);
        ArrayList<MaritalStatus> complicationArrayList = new ArrayList<>();
        for (int i = 0; i < fpArray.length; i++) {
            MaritalStatus reason = new MaritalStatus();
            reason.setId("" + i);
            reason.setStatus(fpArray[i]);
            complicationArrayList.add(reason);
        }
        StatusAdapter complicationAdapter = new StatusAdapter(getContext(), complicationArrayList);
        sp_post_fp_complication.setAdapter(complicationAdapter);

        String[] array = this.getResources().getStringArray(R.array.discontinue_reasons);
        ArrayList<MaritalStatus> discountinueReasonArrayList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            MaritalStatus reason = new MaritalStatus();
            reason.setId("" + i);
            reason.setStatus(array[i]);
            discountinueReasonArrayList.add(reason);
        }
        StatusAdapter coppertTypeAdapter = new StatusAdapter(getContext(), discountinueReasonArrayList);
        sp_discontinue_reason.setAdapter(coppertTypeAdapter);


        ed_discontinue_date.setOnClickListener(this);
        ed_complication_date.setOnClickListener(this);
        bt_save_fmp.setOnClickListener(this);
        bt_edit_fmp.setOnClickListener(this);
        sp_complication_result.setOnItemSelectedListener(this);
        sp_post_fp_complication.setOnItemSelectedListener(this);
        sp_discontinue_reason.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ed_discontinue_date:
                showDatePicker(1);
                break;
            case R.id.ed_complication_date:
                showDatePicker(2);
                break;
            case R.id.bt_save_fmp:
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("discontinueDate",ed_discontinue_date.getText().toString());
                hashMap.put("reasonforremovalId",dischargeReason);
                hashMap.put("fpComplication",fpComplicationResult);
                hashMap.put("fpcomplicationDate",ed_complication_date.getText().toString());
                hashMap.put("statusafterComplication",complicationResult);
                DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
                boolean isSaved=databaseHelper.saveUpdatedFpMethod(adoptedfpmethodId,hashMap);
                if (isSaved) {
                    String str = thisActivity.getResources().getString(R.string.update);
                    CustomToast customToast = new CustomToast(thisActivity, str);
                    customToast.show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }

    private void showDatePicker(int position) {

        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        if (position == 1) {
            date.setCallBack(discontinueDate);
        } else if (position == 2) {
            date.setCallBack(complicationDate);
        }
        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener discontinueDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd/M/yyyy");
            try {
                Date tt1Date = simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));
                ed_discontinue_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener complicationDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd/M/yyyy");
            try {
                Date tt1Date = simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));
                ed_complication_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            LinearLayout linearLayout;
            TextView textView;
            switch (parent.getId()) {

                case R.id.sp_complication_result:
                    if(position!=0) {
                        linearLayout = (LinearLayout) view;
                        textView = (TextView) linearLayout.getChildAt(0);
                        complicationResult = textView.getTag().toString();
                    }else{
                        complicationResult=null;
                    }

                    break;
                case R.id.sp_post_fp_complication:
                    if(position!=0) {
                        linearLayout = (LinearLayout) view;
                        textView = (TextView) linearLayout.getChildAt(0);
                        fpComplicationResult = textView.getTag().toString();
                    }else{
                        fpComplicationResult=null;
                    }
                    break;
                case R.id.sp_discontinue_reason:
                    if(position!=0) {
                        linearLayout = (LinearLayout) view;
                        textView = (TextView) linearLayout.getChildAt(0);
                        dischargeReason = textView.getTag().toString();

                        if (position == 1) {
                            ll_comp.setVisibility(View.VISIBLE);
                        } else {
                            ll_comp.setVisibility(View.GONE);
                        }
                    }else{
                        dischargeReason=null;
                    }

                    break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
