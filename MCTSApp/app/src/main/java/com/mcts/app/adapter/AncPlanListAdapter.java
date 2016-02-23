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
import com.mcts.app.activity.maternalhealthservice.PostNatalServiceListActivity;
import com.mcts.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Raj on 12/15/2015.
 */
public class AncPlanListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private ArrayList<HashMap<String,String>> ancPlanArrayList;
    private int option;

    public AncPlanListAdapter(Context mContext, ArrayList<HashMap<String, String>> mChildArrayList,int option) {
        this.context = mContext;
        this.ancPlanArrayList = mChildArrayList;
        this.option=option;
    }

    @Override
    public int getCount() {
        return ancPlanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return ancPlanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {

        TextView txt_anc_id;
        TextView txt_mother_name;
        TextView txt_anc_due_date;
        TextView txt_anc_type_date;
        String[] serviceArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.anc_plan_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_anc_id = (TextView) convertView.findViewById(R.id.txt_anc_id);
            viewHolder.txt_mother_name = (TextView) convertView.findViewById(R.id.txt_mother_name);
            viewHolder.txt_anc_due_date = (TextView) convertView.findViewById(R.id.txt_anc_due_date);
            viewHolder.txt_anc_type_date = (TextView) convertView.findViewById(R.id.txt_anc_type_date);

            Utils.findAllTextView(context, (ViewGroup) convertView.findViewById(R.id.ll_anc_adapter));
            viewHolder.serviceArray=context.getResources().getStringArray(R.array.anc_service_type);
//            viewHolder.txt_edit.setOnClickListener(this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_anc_id.setText(ancPlanArrayList.get(position).get("pregnantwomanregdId"));
        viewHolder.txt_mother_name.setText(ancPlanArrayList.get(position).get("firstName") + " " + ancPlanArrayList.get(position).get("middleName") + "" + ancPlanArrayList.get(position).get("lastName"));
        viewHolder.txt_anc_due_date.setText(ancPlanArrayList.get(position).get("ancservicedueDate"));
        viewHolder.txt_anc_type_date.setText(viewHolder.serviceArray[Integer.parseInt(ancPlanArrayList.get(position).get("ancservicetrimesterId"))]);

        return convertView;
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.txt_edit:
                /*TextView textView=(TextView)v;
                Intent intent=new Intent(context, PostNatalServiceListActivity.class);
                HashMap<String,String> hashMap= ancPlanArrayList.get(Integer.parseInt(textView.getTag().toString()));
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("villageId", hashMap.get("villageId"));
                    jsonObject.put("villageName", hashMap.get("villageName"));
                    jsonObject.put("firstName", hashMap.get("firstName"));
                    jsonObject.put("middleName", hashMap.get("middleName"));
                    jsonObject.put("lastName", hashMap.get("lastName"));
                    jsonObject.put("emamtahealthId", hashMap.get("emamtahealthId"));
                    jsonObject.put("pregnancyoutcomeDate", hashMap.get("pregnancyoutcomeDate"));
                    jsonObject.put("pregnantwomanregdId", hashMap.get("pregnantwomanregdId"));
                    jsonObject.put("emamtafamilyId", hashMap.get("emamtafamilyId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("postNatalData",jsonObject.toString());
                context.startActivity(intent);*/
                break;
        }
    }
}
