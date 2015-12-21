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
import com.mcts.app.activity.familyhealthsurvey.UpdateFamilyActivity;
import com.mcts.app.activity.maternalhealthservice.PregnantWomenRegistrationActivity;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.Family;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.Member;
import com.mcts.app.utils.TakePictureUtils;
import com.mcts.app.utils.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Raj on 12/15/2015.
 */
public class PragnentWomenAdapter extends BaseAdapter implements View.OnClickListener{

    private Context context;
    private ArrayList<Member> womenMemberArrayList;
    private String villageId,villageName,searchString;
    private String isParmenant = "0";
    private String talukaID,villageID,dirstID;

    public PragnentWomenAdapter(Context mContext, ArrayList<Member> mFamilyArrayList, String strVillageId, String strVillageName,String mSearchString){
        this.context=mContext;
        this.womenMemberArrayList=mFamilyArrayList;
        this.villageId=strVillageId;
        this.villageName=strVillageName;
        this.searchString=mSearchString;
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

        if(convertView==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_search_women, parent, false);
            viewHolder=new ViewHolder();
            viewHolder.img_women_member=(ImageView)convertView.findViewById(R.id.img_women_member);
            viewHolder.txt_women_number=(TextView)convertView.findViewById(R.id.txt_women_number);
            viewHolder.txt_women_member_name=(TextView)convertView.findViewById(R.id.txt_women_member_name);
            viewHolder.txt_women_age=(TextView)convertView.findViewById(R.id.txt_women_age);
            viewHolder.txt_reg_preg=(TextView)convertView.findViewById(R.id.txt_reg_preg);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "SHRUTI.TTF");
            viewHolder.txt_reg_preg.setTypeface(type, Typeface.BOLD);

            Utils.findAllTextView(context, (ViewGroup) convertView.findViewById(R.id.ll_search_women));
            viewHolder.txt_reg_preg.setOnClickListener(this);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_women_member_name.setText(womenMemberArrayList.get(position).getFirstName()+" "+womenMemberArrayList.get(position).getMiddleName()+" "+womenMemberArrayList.get(position).getLastName());
        viewHolder.txt_women_number.setText(womenMemberArrayList.get(position).getEmamtahealthId());
        viewHolder.txt_women_age.setText(""+womenMemberArrayList.get(position).getAgeDifference());
        viewHolder.txt_reg_preg.setTag(womenMemberArrayList.get(position).getEmamtahealthId()+","+womenMemberArrayList.get(position).getAgeDifference());

        if (womenMemberArrayList.get(position).getPhoto() != null) {
            if(womenMemberArrayList.get(position).getPhoto().length() > 5) {
                if(womenMemberArrayList.get(position).getPhoto()!=null) {
                    Uri uri = Uri.parse(womenMemberArrayList.get(position).getPhotoValue());
                    Bitmap image_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));
                    viewHolder.img_women_member.setImageBitmap(image_bitmap);
                }
            }else {
                viewHolder.img_women_member.setImageResource(R.drawable.ic_launcher);
            }
        }else{
            viewHolder.img_women_member.setImageResource(R.drawable.ic_launcher);
        }
        return convertView;
    }




    private static class ViewHolder{

        ImageView img_women_member;
        TextView txt_women_number;
        TextView txt_women_member_name;
        TextView txt_women_age;
        TextView txt_reg_preg;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.txt_reg_preg:

                TextView textView=(TextView)v;
                String[] array=textView.getTag().toString().split(",");
                Intent intent=new Intent(context, PregnantWomenRegistrationActivity.class);
                intent.putExtra("villageId",villageId);
                intent.putExtra("villageName",villageName);
                intent.putExtra("healthId",array[0]);
                intent.putExtra("age",array[1]);
                context.startActivity(intent);
                break;
        }
    }
}
