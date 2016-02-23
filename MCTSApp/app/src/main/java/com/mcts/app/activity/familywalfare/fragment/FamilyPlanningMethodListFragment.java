package com.mcts.app.activity.familywalfare.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.familywalfare.FamilyPlannigActivity;
import com.mcts.app.adapter.FamilyPlanningMethodAdapter;
import com.mcts.app.adapter.SearchFamilyMemberForFPAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.utils.CommonClass;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Raj on 2/2/2016.
 */
public class FamilyPlanningMethodListFragment extends Fragment {

    private View rootView;
    private Activity thisActivity;
    private FamilyPlannigActivity familyPlannigActivity;
    private String data;
    private String familyWalfare,villageId,villageName,memberId,emamtahealthId,emamtafamilyId,coppert_type;
    private JSONObject jsonData;
    private ListView list_member_fp;
    private TextView txt_village_name,txt_fm_walfare_name,txt_health_number;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.family_planning_method_list, container, false);
            thisActivity=getActivity();

            Bundle args = getArguments();
            if (args != null && args.containsKey("data")) {
                data = args.getString("data");
            }

            familyPlannigActivity = (FamilyPlannigActivity) getActivity();
            familyPlannigActivity.setToolBarTitle(thisActivity.getResources().getString(R.string.fmp_list), false);
            CommonClass.hideSoftKeyboard(thisActivity);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;

    }

    private void init() {

        Typeface type = Typeface.createFromAsset(thisActivity.getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) rootView.findViewById(R.id.ll_fmp_list));
        list_member_fp = (ListView) rootView.findViewById(R.id.list_member_fp);
        txt_village_name = (TextView) rootView.findViewById(R.id.txt_village_name);
        txt_fm_walfare_name = (TextView) rootView.findViewById(R.id.txt_fm_walfare_name);
        txt_health_number = (TextView) rootView.findViewById(R.id.txt_health_number);

        try {
            jsonData=new JSONObject(data);
            txt_village_name.setText(jsonData.getString("villageName"));
            txt_fm_walfare_name.setText(jsonData.getString("name"));
            txt_health_number.setText(jsonData.getString("emamtahealthId"));
            memberId=jsonData.getString("memberId");
            emamtahealthId=jsonData.getString("emamtahealthId");
            emamtafamilyId=jsonData.getString("emamtafamilyId");
            villageId=jsonData.getString("villageId");
            villageName=jsonData.getString("villageName");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new FamilyPlanningMethod().execute(emamtahealthId);
    }

    private class FamilyPlanningMethod extends AsyncTask<String,String,String> {

        ArrayList<HashMap<String,String>> familyPlanningMethodArrayList;
        CustomLoaderDialog cm;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cm=new CustomLoaderDialog(thisActivity);
            cm.show(true);
        }

        @Override
        protected String doInBackground(String... params) {

            DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
            familyPlanningMethodArrayList = databaseHelper.searchFamilyPlanningMethod(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cm.hide();
            if (familyPlanningMethodArrayList.size() != 0) {
                FamilyPlanningMethodAdapter familyPlanningMethodAdapter = new FamilyPlanningMethodAdapter(thisActivity, familyPlanningMethodArrayList, villageId, villageName);
                list_member_fp.setAdapter(familyPlanningMethodAdapter);
            } else {
                FamilyPlanningMethodAdapter familyPlanningMethodAdapter = new FamilyPlanningMethodAdapter(thisActivity, familyPlanningMethodArrayList, villageId, villageName);
                list_member_fp.setAdapter(familyPlanningMethodAdapter);
                String str=thisActivity.getResources().getString(R.string.no_match);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        }

    }
}
