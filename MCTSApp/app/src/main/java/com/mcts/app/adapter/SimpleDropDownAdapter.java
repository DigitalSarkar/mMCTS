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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Raj on 10/14/2015.
 */
public class SimpleDropDownAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String,String>> hashMapArrayList;
    public SimpleDropDownAdapter(Context mContext, ArrayList<HashMap<String,String>> mMaritalStatusArrayList){
        this.context=mContext;
        this.hashMapArrayList=mMaritalStatusArrayList;
    }
    @Override
    public int getCount() {
        return hashMapArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return hashMapArrayList.get(position);
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

        viewHolder.txt_value.setText(hashMapArrayList.get(position).get(""+position));
        return convertView;
    }

    static class ViewHolder{
        TextView txt_value;
    }
}
