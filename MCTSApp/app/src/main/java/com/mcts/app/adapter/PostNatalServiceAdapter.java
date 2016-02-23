package com.mcts.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.maternalhealthservice.AntenatalServiceDetailActivity;
import com.mcts.app.activity.maternalhealthservice.PostNatalServiceActivity;
import com.mcts.app.activity.maternalhealthservice.PostNatalServiceListActivity;
import com.mcts.app.model.PregnantWomen;
import com.mcts.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Raj on 12/22/2015.
 */
public class PostNatalServiceAdapter extends BaseAdapter implements View.OnClickListener{

    private Context context;
    private ArrayList<HashMap<String,String>> womenMemberArrayList;
    private String stringJson;

    public PostNatalServiceAdapter(Context mContext,  ArrayList<HashMap<String,String>> mFamilyArrayList){
        this.context=mContext;
        this.womenMemberArrayList=mFamilyArrayList;
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
        viewHolder.txt_service_date.setText(womenMemberArrayList.get(position).get("postnatalservicegivenDate"));
        viewHolder.txt_service_due_date.setText(womenMemberArrayList.get(position).get("postnatalservicedueDate"));
        viewHolder.txt_add_services.setTag(position);

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
                Intent intent=new Intent(context, PostNatalServiceActivity.class);
                HashMap<String,String> hashMap=womenMemberArrayList.get(Integer.parseInt(textView.getTag().toString()));
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("villageId", hashMap.get("villageId"));
                    jsonObject.put("villageName", hashMap.get("villageName"));
                    jsonObject.put("firstName", hashMap.get("firstName"));
                    jsonObject.put("middleName", hashMap.get("middleName"));
                    jsonObject.put("lastName", hashMap.get("lastName"));
                    jsonObject.put("emamtahealthId", hashMap.get("emamtahealthId"));
                    jsonObject.put("pregnantwomanregdId", hashMap.get("pregnantwomanregdId"));
                    jsonObject.put("pregnancyoutcomeDate", hashMap.get("pregnancyoutcomeDate"));
                    jsonObject.put("postnatalserviceId", hashMap.get("postnatalserviceId"));
                    jsonObject.put("postnatalservicetypeId", hashMap.get("postnatalservicetypeId"));
                    jsonObject.put("emamtafamilyId", hashMap.get("emamtafamilyId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("postNatalData",jsonObject.toString());
                context.startActivity(intent);
        }
    }


}
