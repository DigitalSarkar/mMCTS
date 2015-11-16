package com.mcts.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.kutumb.AddFamilyMemberActivity;
import com.mcts.app.activity.kutumb.UpdateKutumbActivity;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.Family;
import com.mcts.app.utils.Messages;
import com.mcts.app.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Raj on 10/11/2015.
 */
public class SearchMemberAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private ArrayList<Family> familyArrayList;
    private String villageId,villageName,searchString;
    public SearchMemberAdapter(Context mContext, ArrayList<Family> mFamilyArrayList, String strVillageId, String strVillageName,String mSearchString){
        this.context=mContext;
        this.familyArrayList=mFamilyArrayList;
        this.villageId=strVillageId;
        this.villageName=strVillageName;
        this.searchString=mSearchString;
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
            convertView = inflater.inflate(R.layout.custom_search_member_list_layout, parent, false);
            viewHolder=new ViewHolder();
            viewHolder.img_member=(ImageView)convertView.findViewById(R.id.img_member);
            viewHolder.txt_fm_member=(TextView)convertView.findViewById(R.id.txt_fm_member);
            viewHolder.txt_fm_number=(TextView)convertView.findViewById(R.id.txt_fm_number);
            viewHolder.txt_edit=(TextView)convertView.findViewById(R.id.txt_edit);
            viewHolder.txt_delete=(TextView)convertView.findViewById(R.id.txt_delete);
            viewHolder.txt_migrate=(TextView)convertView.findViewById(R.id.txt_migrate);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "SHRUTI.TTF");
            viewHolder.txt_fm_number.setTypeface(type,Typeface.BOLD);
            viewHolder.txt_fm_member.setTypeface(type,Typeface.BOLD);

            Utils.findAllTextView(context, (ViewGroup) convertView.findViewById(R.id.ll_search_member));
            viewHolder.txt_edit.setOnClickListener(this);
            viewHolder.txt_delete.setOnClickListener(this);
            viewHolder.txt_migrate.setOnClickListener(this);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_fm_member.setText(familyArrayList.get(position).getMemberName());
        viewHolder.txt_fm_number.setText(familyArrayList.get(position).getEmamtaFamilyId());
        viewHolder.txt_edit.setTag(familyArrayList.get(position).getMemberId());
        viewHolder.txt_delete.setTag(familyArrayList.get(position).getEmamtaFamilyId());
        viewHolder.txt_migrate.setTag(familyArrayList.get(position).getEmamtaFamilyId());

        if(familyArrayList.get(position).getUserImageArray()!=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(familyArrayList.get(position).getUserImageArray(), 0, familyArrayList.get(position).getUserImageArray().length);
            viewHolder.img_member.setImageBitmap(bitmap);
        }

        return convertView;
    }



    static class ViewHolder{

        ImageView img_member;
        TextView txt_fm_number;
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
                Intent intent=new Intent(context, UpdateKutumbActivity.class);
                intent.putExtra("MemberId",textView.getTag().toString());
                intent.putExtra("villageId",villageId);
                intent.putExtra("villageName",villageName);
                context.startActivity(intent);
                break;
            case R.id.txt_delete:


                final Dialog dialog = new Dialog(context);
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view=mInflater.inflate(R.layout.custom_delete_dialog, null);
                Utils.findAllTextView(context, ((ViewGroup) view.findViewById(R.id.ll_delete)));
                final TextView txt_delete_title=(TextView)view.findViewById(R.id.txt_delete_title);
                txt_delete_title.setText(Messages.DELETE_FAMILY);
                Button bt_delete_yes=(Button)view.findViewById(R.id.bt_delete_yes);
                Button bt_delete_no=(Button)view.findViewById(R.id.bt_delete_no);

                bt_delete_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                bt_delete_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        TextView deleteFam=(TextView)v;
                        DatabaseHelper databaseHelper=new DatabaseHelper(context);
                        String eMamtaId=deleteFam.getTag().toString();
                        boolean flag=databaseHelper.deleteFamily(eMamtaId);
                        if(!flag){
                            CustomToast customToast=new CustomToast((Activity) context,"Invalid operation");
                            customToast.show();
                        }else{
                            boolean result=databaseHelper.deleteFamilyMember(eMamtaId);
                            if(!result){
                                CustomToast customToast=new CustomToast((Activity) context,"Invalid operation");
                                customToast.show();
                            }else{
                                familyArrayList = databaseHelper.searchFamily(searchString, villageId);
                                notifyDataSetChanged();
                            }
                        }
                    }
                });


                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(view);

                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
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
                lp.copyFrom(dialog.getWindow().getAttributes());

                lp.width = width;
                lp.height = height;
                dialog.getWindow().setAttributes(lp);
                dialog.setCancelable(false);
                dialog.show();

                break;
            case R.id.txt_migrate:

                TextView eMamta=(TextView)v;
                intent=new Intent(context,AddFamilyMemberActivity.class);
                String eMamtaId=eMamta.getTag().toString();
                intent.putExtra("emamtafamilyId", eMamtaId);
                intent.putExtra("villageId", villageId);
                context.startActivity(intent);

                break;
        }
    }
}
