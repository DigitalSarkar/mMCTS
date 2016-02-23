package com.mcts.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.maternalhealthservice.AntenatalServiceActivity;
import com.mcts.app.activity.maternalhealthservice.DeliveryRegistarActivity;
import com.mcts.app.activity.maternalhealthservice.PregnantWomenRegistrationActivity;
import com.mcts.app.model.Member;
import com.mcts.app.model.PregnantWomen;
import com.mcts.app.utils.TakePictureUtils;
import com.mcts.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Raj on 12/15/2015.
 */
public class DeliveryWomenRegisterAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private ArrayList<PregnantWomen> womenMemberArrayList;
    private String villageId, villageName;
    private String isParmenant = "0";
    private String talukaID, villageID, dirstID;

    public DeliveryWomenRegisterAdapter(Context mContext, ArrayList<PregnantWomen> mFamilyArrayList, String strVillageId, String strVillageName) {
        this.context = mContext;
        this.womenMemberArrayList = mFamilyArrayList;
        this.villageId = strVillageId;
        this.villageName = strVillageName;
    }

    @Override
    public int getCount() {
        return womenMemberArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return womenMemberArrayList.get(position);
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
            convertView = inflater.inflate(R.layout.custom_search_women_service, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.img_women_member = (ImageView) convertView.findViewById(R.id.img_women_member);
            viewHolder.txt_preg_reg_number = (TextView) convertView.findViewById(R.id.txt_preg_reg_number);
            viewHolder.txt_preg_women_name = (TextView) convertView.findViewById(R.id.txt_preg_women_name);
            viewHolder.txt_preg_reg_date = (TextView) convertView.findViewById(R.id.txt_preg_reg_date);
            viewHolder.txt_edit = (TextView) convertView.findViewById(R.id.txt_edit);
            viewHolder.txt_services = (TextView) convertView.findViewById(R.id.txt_services);
            viewHolder.ll_preg_search_women = (LinearLayout) convertView.findViewById(R.id.ll_preg_search_women);

            Utils.findAllTextView(context, (ViewGroup) convertView.findViewById(R.id.ll_preg_search_women));
            viewHolder.txt_edit.setOnClickListener(this);
            viewHolder.txt_services.setOnClickListener(this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_preg_women_name.setText(womenMemberArrayList.get(position).getName());
        viewHolder.txt_preg_reg_number.setText(womenMemberArrayList.get(position).getPregnantwomanregdId());
        viewHolder.txt_preg_reg_date.setText("" + womenMemberArrayList.get(position).getEddDate());
        viewHolder.txt_edit.setTag(womenMemberArrayList.get(position).getPregnantwomanregdId());
        viewHolder.txt_edit.setVisibility(View.GONE);
        viewHolder.txt_services.setText(context.getResources().getString(R.string.preg_register));
        viewHolder.txt_services.setTag(womenMemberArrayList.get(position).getPregnantwomanregdId() + "," + position);

        if (womenMemberArrayList.get(position).getIsHighRisk().equals("1")) {
            viewHolder.ll_preg_search_women.setBackgroundColor(Color.RED);
            viewHolder.txt_preg_women_name.setTextColor(Color.WHITE);
            viewHolder.txt_preg_reg_date.setTextColor(Color.WHITE);
            viewHolder.txt_preg_reg_number.setTextColor(Color.WHITE);
        } else {
            viewHolder.ll_preg_search_women.setBackgroundColor(Color.WHITE);
        }

        if (womenMemberArrayList.get(position).getPhoto() != null) {
            if (womenMemberArrayList.get(position).getPhoto().length() > 5) {
                if (womenMemberArrayList.get(position).getPhoto() != null) {
                    Uri uri = Uri.parse(womenMemberArrayList.get(position).getPhotoValue());
                    Bitmap image_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));
                    viewHolder.img_women_member.setImageBitmap(image_bitmap);
                }
            } else {
                viewHolder.img_women_member.setImageResource(R.drawable.ic_launcher);
            }
        } else {
            viewHolder.img_women_member.setImageResource(R.drawable.ic_launcher);
        }
        return convertView;
    }


    private static class ViewHolder {

        ImageView img_women_member;
        TextView txt_preg_reg_number;
        TextView txt_preg_women_name;
        TextView txt_preg_reg_date;
        TextView txt_edit;
        TextView txt_services;
        LinearLayout ll_preg_search_women;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.txt_services:
                TextView textView=(TextView)v;
                String[] array=textView.getTag().toString().split(",");
                Intent intent=new Intent(context, DeliveryRegistarActivity.class);
                intent.putExtra("pregnantId",array[0]);
                PregnantWomen pregnantWomen=womenMemberArrayList.get(Integer.parseInt(array[1]));
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("villageId", villageId);
                    jsonObject.put("villageName", villageName);
                    jsonObject.put("name", pregnantWomen.getName());
                    jsonObject.put("pregnantwomanregdId", pregnantWomen.getPregnantwomanregdId());
                    jsonObject.put("pregnantwomanregdDate", pregnantWomen.getPregnantwomanregdDate());
                    jsonObject.put("eddDate", pregnantWomen.getEddDate());
                    jsonObject.put("emamtahealthId", pregnantWomen.getEmamtahealthId());
                    jsonObject.put("bloodGroup", pregnantWomen.getBloodGroup());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("data",jsonObject.toString());
                context.startActivity(intent);
                break;
        }
    }
}
