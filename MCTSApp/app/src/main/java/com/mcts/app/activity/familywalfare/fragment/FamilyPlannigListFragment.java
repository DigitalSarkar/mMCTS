package com.mcts.app.activity.familywalfare.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.familyhealthsurvey.NewFamilyActivity;
import com.mcts.app.activity.familywalfare.FamilyPlannigActivity;
import com.mcts.app.adapter.SearchFamilyMemberAdapter;
import com.mcts.app.adapter.SearchFamilyMemberForFPAdapter;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.Family;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.CommonClass;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class FamilyPlannigListFragment extends Fragment implements AdapterView.OnItemSelectedListener,View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View rootView;
    private Activity thisActivity;
    private ListView list_family_plan_list;
    private Spinner sp_village;
    private EditText ed_family_number;
    private Button bt_member_search;
    private String strVillageId, strVillageName;
    private FamilyPlannigActivity familyPlannigActivity;

    public FamilyPlannigListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FamilyPlannigListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FamilyPlannigListFragment newInstance(String param1, String param2) {
        FamilyPlannigListFragment fragment = new FamilyPlannigListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
            rootView = inflater.inflate(R.layout.fragment_family_plannig_list, container, false);
            thisActivity=getActivity();
            init();
            familyPlannigActivity = (FamilyPlannigActivity) getActivity();
            familyPlannigActivity.setToolBarTitle(thisActivity.getResources().getString(R.string.kutumnb_kalyan_seva), false);
            CommonClass.hideSoftKeyboard(thisActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        if(sp_village.getSelectedItemPosition()!=0) {
            if (ed_family_number.getText().toString().length() > 1) {

                String searchString = ed_family_number.getText().toString().trim();
                new GetFamilyMember().execute(searchString, strVillageId);

            }
        }
    }

    private void init() {

        Typeface type = Typeface.createFromAsset(thisActivity.getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) rootView.findViewById(R.id.ll_planing_list));
        list_family_plan_list = (ListView) rootView.findViewById(R.id.list_family_plan_list);
        sp_village = (Spinner) rootView.findViewById(R.id.sp_village);
        ed_family_number = (EditText) rootView.findViewById(R.id.ed_family_number);
        bt_member_search = (Button) rootView.findViewById(R.id.bt_member_search);

        sp_village.setOnItemSelectedListener(this);
        bt_member_search.setOnClickListener(this);
        getVillageData();

    }

    private void getVillageData() {

        DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
        ArrayList<MaritalStatus> villageArray = databaseHelper.getVillageData();
        if (villageArray != null) {
            StatusAdapter masikAdapter = new StatusAdapter(thisActivity, villageArray);
            sp_village.setAdapter(masikAdapter);
        } else {
            String str = thisActivity.getResources().getString(R.string.no_data);
            CustomToast customToast = new CustomToast(thisActivity, str);
            customToast.show();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout linearLayout;
        TextView textView;
        if (position != 0) {
            linearLayout = (LinearLayout) view;
            textView = (TextView) linearLayout.getChildAt(0);
            strVillageId = textView.getTag().toString();
            strVillageName = textView.getText().toString();
        } else {
            strVillageId = null;
            strVillageName = null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    @Override
    public void onClick(View view) {
        Utils.ButtonClickEffect(view);
        switch (view.getId()){
            case R.id.bt_member_search:
                Utils.hideSoftKeyboard(thisActivity);
                if(strVillageId!=null) {
                    if (ed_family_number.getText().toString().length() > 1) {

                        String searchString = ed_family_number.getText().toString().trim();
                        new GetFamilyMember().execute(searchString, strVillageId);

                    } else {
                        String str = thisActivity.getResources().getString(R.string.three_char);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }else{
                    String str = thisActivity.getResources().getString(R.string.select_village);
                    CustomToast customToast = new CustomToast(thisActivity, str);
                    customToast.show();
                }

                break;

        }
    }

    private class GetFamilyMember extends AsyncTask<String,String,String> {

        ArrayList<HashMap<String,String>> familyArrayList;
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
            familyArrayList = databaseHelper.searchMemberForFP(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cm.hide();
            if (familyArrayList.size() != 0) {
                String searchString = ed_family_number.getText().toString().trim();
                SearchFamilyMemberForFPAdapter searchFamilyMemberAdapter = new SearchFamilyMemberForFPAdapter(thisActivity, familyArrayList, strVillageId, strVillageName,searchString);
                list_family_plan_list.setAdapter(searchFamilyMemberAdapter);
            } else {
                String searchString = ed_family_number.getText().toString().trim();
                SearchFamilyMemberForFPAdapter searchFamilyMemberAdapter = new SearchFamilyMemberForFPAdapter(thisActivity, familyArrayList, strVillageId, strVillageName,searchString);
                list_family_plan_list.setAdapter(searchFamilyMemberAdapter);
                String str=thisActivity.getResources().getString(R.string.no_match);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
