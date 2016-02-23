package com.mcts.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.childhealth.ChildImmunization;
import com.mcts.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Raj on 12/15/2015.
 */
public class ImmunizeDetailAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<HashMap<String,String>> immunizationArrayList;

    public ImmunizeDetailAdapter(Context mContext, ArrayList<HashMap<String, String>> mChildArrayList) {
        this.context = mContext;
        this.immunizationArrayList = mChildArrayList;
    }

    @Override
    public int getCount() {
        return immunizationArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return immunizationArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {

        TextView txt_vaccine_name;
        TextView txt_vaccine_duedate;
        EditText ed_vaccine_givendate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_immunization_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_vaccine_name = (TextView) convertView.findViewById(R.id.txt_vaccine_name);
            viewHolder.txt_vaccine_duedate = (TextView) convertView.findViewById(R.id.txt_vaccine_duedate);
            viewHolder.ed_vaccine_givendate = (EditText) convertView.findViewById(R.id.ed_vaccine_givendate);

            Utils.findAllTextView(context, (ViewGroup) convertView.findViewById(R.id.ll_vaccine_details));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_vaccine_name.setText(immunizationArrayList.get(position).get("vaccineName"));
        viewHolder.txt_vaccine_duedate.setText(immunizationArrayList.get(position).get("vaccinedueDate"));
        viewHolder.ed_vaccine_givendate.setText(immunizationArrayList.get(position).get("vaccinegivenDate"));

        return convertView;
    }

}
