package com.mcts.app.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.maternalhealthservice.AntenatalServiceDetailActivity;
import com.mcts.app.model.AntenatalService;
import com.mcts.app.model.PregnantWomen;
import com.mcts.app.utils.DatePickerFragment;
import com.mcts.app.utils.FormValidation;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Raj on 12/22/2015.
 */
public class AntenatalServiceAdapter extends BaseAdapter implements View.OnClickListener{

    private Context context;
    private ArrayList<PregnantWomen> womenMemberArrayList;
    private String stringJson;

    public AntenatalServiceAdapter(Context mContext, ArrayList<PregnantWomen> mFamilyArrayList,String mJson){
        this.context=mContext;
        this.womenMemberArrayList=mFamilyArrayList;
        this.stringJson=mJson;
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
            convertView = inflater.inflate(R.layout.custom_antenatal_service, parent, false);
            viewHolder=new ViewHolder();
            viewHolder.txt_number=(TextView)convertView.findViewById(R.id.txt_number);
            viewHolder.txt_service_date=(TextView)convertView.findViewById(R.id.txt_service_date);
            viewHolder.txt_service_due_date=(TextView)convertView.findViewById(R.id.txt_service_due_date);
            viewHolder.txt_add_services=(TextView)convertView.findViewById(R.id.txt_add_services);

            Utils.findAllTextView(context, (ViewGroup) convertView.findViewById(R.id.ll_antenatal_service));
            viewHolder.txt_add_services.setOnClickListener(this);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int index=position+1;
        viewHolder.txt_number.setText(""+index);
        viewHolder.txt_service_date.setText(womenMemberArrayList.get(position).getAncServiceDate());
        viewHolder.txt_service_due_date.setText(womenMemberArrayList.get(position).getAncServiceDueDate());
        viewHolder.txt_add_services.setTag(womenMemberArrayList.get(position).getLmpDate()+","+womenMemberArrayList.get(position).getEddDate());

        return convertView;
    }




    private static class ViewHolder{

        TextView txt_number;
        TextView txt_service_date;
        TextView txt_service_due_date;
        TextView txt_add_services;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.txt_add_services:
                TextView textView= (TextView) v;
                Intent intent=new Intent(context, AntenatalServiceDetailActivity.class);
                try {
                    JSONObject jsonObject=new JSONObject(stringJson);
                    String[] date=v.getTag().toString().split(",");
                    jsonObject.put("lmpDate",date[0]);
                    jsonObject.put("eddDate",date[1]);
                    stringJson=jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("stringJson",stringJson);
                context.startActivity(intent);
        }
    }


}
