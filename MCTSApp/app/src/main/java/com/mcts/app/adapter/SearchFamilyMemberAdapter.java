package com.mcts.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.kutumb.UpdateFamilyMemberActivity;
import com.mcts.app.activity.kutumb.UpdateKutumbActivity;
import com.mcts.app.model.Family;
import com.mcts.app.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Raj on 11/7/2015.
 */
public class SearchFamilyMemberAdapter extends BaseAdapter implements View.OnClickListener{

    private Context context;
    private ArrayList<Family> familyArrayList;
    private String villageId,villageName;
    public SearchFamilyMemberAdapter(Context mContext, ArrayList<Family> mFamilyArrayList, String strVillageId, String strVillageName){
        this.context=mContext;
        this.familyArrayList=mFamilyArrayList;
        this.villageId=strVillageId;
        this.villageName=strVillageName;
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
        if(convertView==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_member_list_layout, parent, false);
            viewHolder=new ViewHolder();
            viewHolder.img_member=(ImageView)convertView.findViewById(R.id.img_member);
            viewHolder.txt_fm_member=(TextView)convertView.findViewById(R.id.txt_fm_member);
            viewHolder.txt_fm_number=(TextView)convertView.findViewById(R.id.txt_fm_number);
            viewHolder.txt_fmhealth_number=(TextView)convertView.findViewById(R.id.txt_fmhealth_number);
            viewHolder.txt_edit=(TextView)convertView.findViewById(R.id.txt_edit);
            viewHolder.txt_delete=(TextView)convertView.findViewById(R.id.txt_delete);
            viewHolder.txt_migrate=(TextView)convertView.findViewById(R.id.txt_migrate);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "SHRUTI.TTF");
            viewHolder.txt_fm_number.setTypeface(type,Typeface.BOLD);
            viewHolder.txt_fm_member.setTypeface(type,Typeface.BOLD);

            Utils.findAllTextView(context, (ViewGroup) convertView.findViewById(R.id.ll_search_member));
            viewHolder.txt_edit.setOnClickListener(this);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_fm_member.setText(familyArrayList.get(position).getMemberName());
        viewHolder.txt_fmhealth_number.setText(familyArrayList.get(position).getMemberId());
        viewHolder.txt_fm_number.setText(familyArrayList.get(position).getEmamtaFamilyId());
        viewHolder.txt_edit.setTag(familyArrayList.get(position).getMemberId());

        if(familyArrayList.get(position).getUserImageArray()!=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(familyArrayList.get(position).getUserImageArray(), 0, familyArrayList.get(position).getUserImageArray().length);
            viewHolder.img_member.setImageBitmap(bitmap);
        }

        return convertView;
    }



    static class ViewHolder{

        ImageView img_member;
        TextView txt_fm_number;
        TextView txt_fmhealth_number;
        TextView txt_fm_member;
        TextView txt_edit;
        TextView txt_delete;
        TextView txt_migrate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_edit:
                TextView textView=(TextView)v;
                Intent intent=new Intent(context, UpdateFamilyMemberActivity.class);
                intent.putExtra("MemberId",textView.getTag().toString());
                intent.putExtra("villageId",villageId);
                intent.putExtra("villageName",villageName);
                context.startActivity(intent);
                break;
        }
    }

}
