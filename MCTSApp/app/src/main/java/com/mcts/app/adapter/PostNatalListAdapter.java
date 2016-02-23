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
import com.mcts.app.activity.maternalhealthservice.ChildRegistrationActivity;
import com.mcts.app.activity.maternalhealthservice.PostNatalServiceActivity;
import com.mcts.app.activity.maternalhealthservice.PostNatalServiceListActivity;
import com.mcts.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Raj on 12/15/2015.
 */
public class PostNatalListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private ArrayList<HashMap<String,String>> childArrayList;

    public PostNatalListAdapter(Context mContext, ArrayList<HashMap<String, String>> mChildArrayList) {
        this.context = mContext;
        this.childArrayList = mChildArrayList;
    }

    @Override
    public int getCount() {
        return childArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childArrayList.get(position);
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
            convertView = inflater.inflate(R.layout.custom_child_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_child_id = (TextView) convertView.findViewById(R.id.txt_child_id);
            viewHolder.txt_mother_name = (TextView) convertView.findViewById(R.id.txt_mother_name);
            viewHolder.txt_child_birth_date = (TextView) convertView.findViewById(R.id.txt_child_birth_date);
            viewHolder.txt_edit = (TextView) convertView.findViewById(R.id.txt_edit);

            Utils.findAllTextView(context, (ViewGroup) convertView.findViewById(R.id.ll_custom_child));
            viewHolder.txt_edit.setOnClickListener(this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_child_id.setText(childArrayList.get(position).get("pregnantwomanregdId"));
        viewHolder.txt_mother_name.setText(childArrayList.get(position).get("firstName")+" "+childArrayList.get(position).get("middleName")+ "" +childArrayList.get(position).get("lastName"));
        viewHolder.txt_child_birth_date.setText(childArrayList.get(position).get("pregnancyoutcomeDate"));
        viewHolder.txt_edit.setTag(position);

        return convertView;
    }


    private static class ViewHolder {

        TextView txt_child_id;
        TextView txt_mother_name;
        TextView txt_child_birth_date;
        TextView txt_edit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.txt_edit:
                TextView textView=(TextView)v;
                Intent intent=new Intent(context, PostNatalServiceListActivity.class);
                HashMap<String,String> hashMap=childArrayList.get(Integer.parseInt(textView.getTag().toString()));
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
                context.startActivity(intent);
                break;
        }
    }
}
