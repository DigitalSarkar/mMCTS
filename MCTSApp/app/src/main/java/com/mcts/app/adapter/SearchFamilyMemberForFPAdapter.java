package com.mcts.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.familyhealthsurvey.UpdateFamilyMemberActivity;
import com.mcts.app.activity.familywalfare.fragment.FamilyPlanningMethodListFragment;
import com.mcts.app.activity.familywalfare.fragment.FamilyPlanningServiceFragment;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.Family;
import com.mcts.app.model.MaritalStatus;
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
public class SearchFamilyMemberForFPAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private ArrayList<HashMap<String,String>> familyArrayList;
    private String villageId, villageName, searchString;
    private String isParmenant = "0";
    private String talukaID,villageID,dirstID,stateId,subcentreId;

    public SearchFamilyMemberForFPAdapter(Context mContext, ArrayList<HashMap<String,String>> mFamilyArrayList, String strVillageId, String strVillageName, String mSearchString) {
        this.context = mContext;
        this.familyArrayList = mFamilyArrayList;
        this.villageId = strVillageId;
        this.villageName = strVillageName;
        this.searchString = mSearchString;
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
            convertView = inflater.inflate(R.layout.custom_member_fp_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.img_member = (ImageView) convertView.findViewById(R.id.img_member);
            viewHolder.txt_fm_member = (TextView) convertView.findViewById(R.id.txt_fm_member);
            viewHolder.txt_fm_number = (TextView) convertView.findViewById(R.id.txt_fm_number);
            viewHolder.txt_fmhealth_number = (TextView) convertView.findViewById(R.id.txt_fmhealth_number);
            viewHolder.txt_new_fm_walfare = (TextView) convertView.findViewById(R.id.txt_new_fm_walfare);
            viewHolder.txt_edit_fm_walfare = (TextView) convertView.findViewById(R.id.txt_edit_fm_walfare);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "SHRUTI.TTF");
            viewHolder.txt_fm_number.setTypeface(type, Typeface.BOLD);
            viewHolder.txt_fm_member.setTypeface(type, Typeface.BOLD);

            Utils.findAllTextView(context, (ViewGroup) convertView.findViewById(R.id.ll_search_member));
            viewHolder.txt_new_fm_walfare.setOnClickListener(this);
            viewHolder.txt_edit_fm_walfare.setOnClickListener(this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_fm_member.setText(familyArrayList.get(position).get("name"));
        viewHolder.txt_fmhealth_number.setText(familyArrayList.get(position).get("memberId"));
        viewHolder.txt_fm_number.setText(familyArrayList.get(position).get("emamtafamilyId"));
        viewHolder.txt_edit_fm_walfare.setTag(position);
        viewHolder.txt_new_fm_walfare.setTag(position);

        if (familyArrayList.get(position).get("photo") != null) {
            if(familyArrayList.get(position).get("photo").length()>5) {
                Uri uri = Uri.parse(familyArrayList.get(position).get("photovalue"));
                Bitmap image_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));
                viewHolder.img_member.setImageBitmap(image_bitmap);
            }else {
                viewHolder.img_member.setImageResource(R.drawable.ic_launcher);
            }
        } else {
            viewHolder.img_member.setImageResource(R.drawable.ic_launcher);
        }

        return convertView;
    }


    private static class ViewHolder {

        ImageView img_member;
        TextView txt_fm_number;
        TextView txt_fmhealth_number;
        TextView txt_fm_member;
        TextView txt_new_fm_walfare;
        TextView txt_edit_fm_walfare;
    }

    @Override
    public void onClick(View view) {

        Utils.ButtonClickEffect(view);
        TextView textView;
        int pos;
        Bundle bundle;
        JSONObject jsonObject;
        switch (view.getId()) {

            case R.id.txt_new_fm_walfare:
                textView= (TextView) view;
                pos=Integer.parseInt(textView.getTag().toString());
                FamilyPlanningServiceFragment familyPlanningServiceFragment=new FamilyPlanningServiceFragment();
                bundle=new Bundle();
                jsonObject=new JSONObject();
                try {
                    jsonObject.put("name",familyArrayList.get(pos).get("name"));
                    jsonObject.put("emamtafamilyId",familyArrayList.get(pos).get("emamtafamilyId"));
                    jsonObject.put("memberId",familyArrayList.get(pos).get("memberId"));
                    jsonObject.put("emamtahealthId",familyArrayList.get(pos).get("emamtahealthId"));
                    jsonObject.put("villageId",villageId);
                    jsonObject.put("villageName",villageName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bundle.putString("data",jsonObject.toString());
                familyPlanningServiceFragment.setArguments(bundle);
                replaceFragment(familyPlanningServiceFragment);

                break;
            case R.id.txt_edit_fm_walfare:
                textView= (TextView) view;
                pos=Integer.parseInt(textView.getTag().toString());
                FamilyPlanningMethodListFragment familyPlanningMethodListFragment=new FamilyPlanningMethodListFragment();
                bundle=new Bundle();
                jsonObject=new JSONObject();
                try {
                    jsonObject.put("name",familyArrayList.get(pos).get("name"));
                    jsonObject.put("emamtafamilyId",familyArrayList.get(pos).get("emamtafamilyId"));
                    jsonObject.put("memberId",familyArrayList.get(pos).get("memberId"));
                    jsonObject.put("emamtahealthId",familyArrayList.get(pos).get("emamtahealthId"));
                    jsonObject.put("villageId",villageId);
                    jsonObject.put("villageName",villageName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bundle.putString("data",jsonObject.toString());
                familyPlanningMethodListFragment.setArguments(bundle);
                replaceFragment(familyPlanningMethodListFragment);

                break;
        }
    }

    public void replaceFragment(Fragment fragment) {

        if (!fragment.isVisible()) {
            String backStateName = fragment.getClass().getName();

            android.support.v4.app.FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

            if (!fragmentPopped) { //fragment not in back stack, create it.

                FragmentTransaction ft = manager.beginTransaction();
//                ft.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left,R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();

            }
        }
    }

}
