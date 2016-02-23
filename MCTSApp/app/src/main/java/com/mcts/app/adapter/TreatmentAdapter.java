package com.mcts.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.model.MaritalStatus;

import java.util.ArrayList;

/**
 * Created by Raj on 10/14/2015.
 */
public class TreatmentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MaritalStatus> maritalStatusArrayList;
    private EditText ed_high_risk_mom;

    public TreatmentAdapter(Context mContext, ArrayList<MaritalStatus> mMaritalStatusArrayList, EditText med_high_risk_mom){
        this.context=mContext;
        this.maritalStatusArrayList=mMaritalStatusArrayList;
        this.ed_high_risk_mom=med_high_risk_mom;
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
            convertView=inflater.inflate(R.layout.custom_treatment_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.txt_treat = (TextView) convertView.findViewById(R.id.txt_treat);
            viewHolder.chk_treat = (CheckBox) convertView.findViewById(R.id.chk_treat);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "SHRUTI.TTF");
            viewHolder.txt_treat.setTypeface(type, Typeface.BOLD);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.txt_treat.setText(maritalStatusArrayList.get(position).getStatus());
        viewHolder.chk_treat.setTag("" + position);

        MaritalStatus maritalStatus=maritalStatusArrayList.get(position);
        if(maritalStatus.getIsChecked() != null) {
            if (maritalStatus.getIsChecked().equals("1")) {
                viewHolder.chk_treat.setChecked(true);
                maritalStatus.setIsChecked("1");
            } else {
                viewHolder.chk_treat.setChecked(false);
                maritalStatus.setIsChecked("0");
            }
        }else{
            viewHolder.chk_treat.setChecked(false);
            maritalStatus.setIsChecked("0");
        }

        maritalStatusArrayList.set(position,maritalStatus);

        viewHolder.chk_treat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                int pos=Integer.parseInt(checkBox.getTag().toString());
                MaritalStatus maritalStatus=maritalStatusArrayList.get(pos);
                if(maritalStatus.getIsChecked().equals("1")){
                    maritalStatus.setIsChecked("0");
                    checkBox.setChecked(false);
                    maritalStatusArrayList.set(pos, maritalStatus);
                    setValue();
                }else{
                    maritalStatus.setIsChecked("1");
                    checkBox.setChecked(true);
                    maritalStatusArrayList.set(pos, maritalStatus);
                    setValue();
                }

            }
        });

        return convertView;
    }

    private void setValue() {

        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "";
        for (int i = 0; i < maritalStatusArrayList.size(); i++) {
            MaritalStatus maritalStatus=maritalStatusArrayList.get(i);
            if (maritalStatus.getIsChecked() != null && maritalStatus.getIsChecked().equals("1")) {
                stringBuilder.append(maritalStatus.getStatus());
                prefix = ",";
                stringBuilder.append(prefix);
            }
        }

        if(stringBuilder.toString().length()>=2){
            String risk=stringBuilder.toString().substring(0, stringBuilder.toString().length()-1);
            ed_high_risk_mom.setText(risk);
        }else {
            ed_high_risk_mom.setText(stringBuilder.toString());
        }
    }

    static class ViewHolder{
        TextView txt_treat;
        CheckBox chk_treat;
    }
}
