package com.mcts.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.familywalfare.fragment.EditFpMethodFragment;
import com.mcts.app.activity.familywalfare.fragment.FamilyPlanningMethodListFragment;
import com.mcts.app.activity.familywalfare.fragment.FamilyPlanningServiceFragment;
import com.mcts.app.utils.TakePictureUtils;
import com.mcts.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Raj on 11/7/2015.
 */
public class FamilyPlanningMethodAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private ArrayList<HashMap<String, String>> familyArrayList;
    private String villageId, villageName, searchString;
    private String isParmenant = "0";
    private String talukaID, villageID, dirstID, stateId, subcentreId;

    public FamilyPlanningMethodAdapter(Context mContext, ArrayList<HashMap<String, String>> mFamilyArrayList, String strVillageId, String strVillageName) {
        this.context = mContext;
        this.familyArrayList = mFamilyArrayList;
        this.villageId = strVillageId;
        this.villageName = strVillageName;
    }

    @Override
    public int getCount() {
        return familyArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return familyArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_family_planning_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_number = (TextView) convertView.findViewById(R.id.txt_number);
            viewHolder.txt_adopted_fp = (TextView) convertView.findViewById(R.id.txt_adopted_fp);
            viewHolder.txt_fm_walfare_date = (TextView) convertView.findViewById(R.id.txt_fm_walfare_date);
            viewHolder.txt_discontinue_date = (TextView) convertView.findViewById(R.id.txt_discontinue_date);
            viewHolder.txt_fp_edit = (TextView) convertView.findViewById(R.id.txt_fp_edit);
            viewHolder.ll_search_member = (LinearLayout) convertView.findViewById(R.id.ll_search_member);

            Utils.findAllTextView(context, (ViewGroup) convertView.findViewById(R.id.ll_search_member));
            viewHolder.txt_fp_edit.setOnClickListener(this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int index = position + 1;
        viewHolder.txt_number.setText("" + index);
        viewHolder.txt_adopted_fp.setText(familyArrayList.get(position).get("familyplanningMethod"));
        viewHolder.txt_fm_walfare_date.setText(familyArrayList.get(position).get("adoptedDate"));
        viewHolder.txt_discontinue_date.setText(familyArrayList.get(position).get("discontinueDate"));
        viewHolder.txt_fp_edit.setTag(position);

        if (familyArrayList.get(position).get("adoptedfpMethod").equals("1") || familyArrayList.get(position).get("adoptedfpMethod").equals("2")
                || familyArrayList.get(position).get("adoptedfpMethod").equals("3") || familyArrayList.get(position).get("adoptedfpMethod").equals("4")) {
            viewHolder.ll_search_member.setBackgroundColor(Color.GREEN);
        } else if (familyArrayList.get(position).get("adoptedfpMethod").equals("5") || familyArrayList.get(position).get("adoptedfpMethod").equals("6")
                || familyArrayList.get(position).get("adoptedfpMethod").equals("7")) {

            if(familyArrayList.get(position).get("discontinueDate") != null) {
                viewHolder.ll_search_member.setBackgroundColor(Color.RED);
            }else{
                viewHolder.ll_search_member.setBackgroundColor(Color.YELLOW);
            }
        }

        return convertView;
    }


    private static class ViewHolder {

        TextView txt_number;
        TextView txt_adopted_fp;
        TextView txt_fm_walfare_date;
        TextView txt_discontinue_date;
        TextView txt_fp_edit;
        LinearLayout ll_search_member;
    }

    @Override
    public void onClick(View view) {

        Utils.ButtonClickEffect(view);
        TextView textView;
        int pos;
        Bundle bundle;
        JSONObject jsonObject;
        switch (view.getId()) {

            case R.id.txt_fp_edit:
                textView = (TextView) view;
                pos = Integer.parseInt(textView.getTag().toString());
                EditFpMethodFragment editFpMethodFragment = new EditFpMethodFragment();
                bundle = new Bundle();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", familyArrayList.get(pos).get("name"));
                    jsonObject.put("emamtahealthId", familyArrayList.get(pos).get("emamtahealthId"));
                    jsonObject.put("adoptedfpmethodId", familyArrayList.get(pos).get("adoptedfpmethodId"));
                    jsonObject.put("villageId", villageId);
                    jsonObject.put("villageName", villageName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bundle.putString("data", jsonObject.toString());
                editFpMethodFragment.setArguments(bundle);
                replaceFragment(editFpMethodFragment);
//                showEditDialog();

                break;

        }
    }


    public void replaceFragment(Fragment fragment) {

        if (!fragment.isVisible()) {
            String backStateName = fragment.getClass().getName();

            android.support.v4.app.FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

            if (!fragmentPopped) { //fragment not in back stack, create it.

                FragmentTransaction ft = manager.beginTransaction();
                ft.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left, R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();

            }
        }
    }

}
