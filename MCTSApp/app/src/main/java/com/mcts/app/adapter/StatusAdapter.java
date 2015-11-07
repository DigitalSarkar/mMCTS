package com.mcts.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.Religion;

import java.util.ArrayList;

/**
 * Created by Raj on 10/14/2015.
 */
public class StatusAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MaritalStatus> maritalStatusArrayList;
    public StatusAdapter(Context mContext,ArrayList<MaritalStatus> mMaritalStatusArrayList){
        this.context=mContext;
        this.maritalStatusArrayList=mMaritalStatusArrayList;
    }
    @Override
    public int getCount() {
        return maritalStatusArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return maritalStatusArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.custom_dropdown_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.txt_value = (TextView) convertView.findViewById(R.id.txt_value);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "SHRUTI.TTF");
            viewHolder.txt_value.setTypeface(type, Typeface.BOLD);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.txt_value.setText(maritalStatusArrayList.get(position).getStatus());
        viewHolder.txt_value.setTag(maritalStatusArrayList.get(position).getId());
        return convertView;
    }

    static class ViewHolder{
        TextView txt_value;
    }
}
