package com.mcts.app.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.model.HighRiskSymtoms;
import com.mcts.app.model.WomenHighRisk;
import com.mcts.app.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Raj on 12/18/2015.
 */
public class ExpandablaHistoryListAdapter implements ExpandableListAdapter {

    private Context _context;
    private ArrayList<WomenHighRisk> womenHighRiskArrayList;
    private EditText ed_high_risk_mom;
    private TextView textTag;
    ViewHolderItem viewHolder;

    public ExpandablaHistoryListAdapter(Context context, ArrayList<WomenHighRisk> mWomenHighRiskArrayList, EditText mEd_high_risk_mom, TextView mTextTag) {
        this._context = context;
        this.womenHighRiskArrayList = mWomenHighRiskArrayList;
        this.ed_high_risk_mom = mEd_high_risk_mom;
        this.textTag = mTextTag;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.womenHighRiskArrayList.get(groupPosition).getHighRiskSymtomsArrayList().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final HighRiskSymtoms highRiskSymtoms = (HighRiskSymtoms) getChild(groupPosition, childPosition);

        if (convertView == null) {
            viewHolder = new ViewHolderItem();
            viewHolder.customTextWatcher=new CustomTextWatcher();
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_history_layout, null);
            Utils.findAllTextView(_context, (ViewGroup) convertView.findViewById(R.id.ll_history));
            viewHolder.txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);
            viewHolder.ed_year = (EditText) convertView
                    .findViewById(R.id.ed_year);
            viewHolder.chk_symptoms = (CheckBox) convertView
                    .findViewById(R.id.chk_symptoms);
            Typeface type = Typeface.createFromAsset(_context.getAssets(), "SHRUTI.TTF");
            viewHolder.txtListChild.setTypeface(type, Typeface.BOLD);
            viewHolder.ed_year.addTextChangedListener(viewHolder.customTextWatcher);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        viewHolder.chk_symptoms.setTag(highRiskSymtoms.getSymptomId() + "," + highRiskSymtoms.getCategoryId() + "," + highRiskSymtoms.getSymptomName());
        if (highRiskSymtoms.getIsChecked() == null || highRiskSymtoms.getIsChecked().equals("1")) {
            viewHolder.chk_symptoms.setChecked(true);
            highRiskSymtoms.setIsChecked("1");
        }else{
            viewHolder.chk_symptoms.setChecked(false);
            highRiskSymtoms.setIsChecked("0");
        }

        viewHolder.txtListChild.setText(highRiskSymtoms.getSymptomName());
        viewHolder.customTextWatcher.setPosition(groupPosition, childPosition);
        if(highRiskSymtoms.getYear()!=null){
            viewHolder.ed_year.setText(highRiskSymtoms.getYear());
//            viewHolder.ed_year.setVisibility(View.VISIBLE);
        }else{
//            viewHolder.ed_year.setVisibility(View.GONE);
            viewHolder.ed_year.setText("");
        }

        viewHolder.chk_symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if (highRiskSymtoms.getIsChecked() == null || highRiskSymtoms.getIsChecked().equals("0")) {
                    checkBox.setChecked(true);
                    highRiskSymtoms.setIsChecked("1");
//                    viewHolder.ed_year.setVisibility(View.VISIBLE);
                    womenHighRiskArrayList.get(groupPosition).getHighRiskSymtomsArrayList().set(childPosition, highRiskSymtoms);
                    String tag = checkBox.getTag().toString();
                    String[] array = tag.split(",");

//                    Toast.makeText(_context, "SymptomId=" + array[0] + "CategoryId=" + array[1] + "SymptomName=" + array[2], Toast.LENGTH_LONG).show();
                    setHighRiskValue();
                } else {
                    checkBox.setChecked(false);
                    highRiskSymtoms.setIsChecked("0");
                    highRiskSymtoms.setYear(null);
//                    viewHolder.ed_year.setVisibility(View.GONE);
                    womenHighRiskArrayList.get(groupPosition).getHighRiskSymtomsArrayList().set(childPosition, highRiskSymtoms);
                    setHighRiskValue();
                }
            }
        });

//        ed_year.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() != 0) {
//                    highRiskSymtoms.setYear(s.toString());
//                    womenHighRiskArrayList.get(groupPosition).getHighRiskSymtomsArrayList().set(childPosition, highRiskSymtoms);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });



        return convertView;
    }

    static class ViewHolderItem {
        TextView txtListChild;
        EditText ed_year;
        CheckBox chk_symptoms;
        CustomTextWatcher customTextWatcher;
    }

    private class CustomTextWatcher implements TextWatcher {
        public int groupPosition;
        public int childPosition;

        public CustomTextWatcher() {

        }
        public void setPosition(int mgroupPosition,int mchildPosition) {
            this.groupPosition=mgroupPosition;
            this.childPosition=mchildPosition;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (charSequence.length() != 0) {
                HighRiskSymtoms highRiskSymtoms= womenHighRiskArrayList.get(groupPosition).getHighRiskSymtomsArrayList().get(childPosition);
                highRiskSymtoms.setYear(charSequence.toString());
                womenHighRiskArrayList.get(groupPosition).getHighRiskSymtomsArrayList().set(childPosition, highRiskSymtoms);
                setHighRiskValue();

            }else{
                HighRiskSymtoms highRiskSymtoms= womenHighRiskArrayList.get(groupPosition).getHighRiskSymtomsArrayList().get(childPosition);
                highRiskSymtoms.setYear(null);
                womenHighRiskArrayList.get(groupPosition).getHighRiskSymtomsArrayList().set(childPosition, highRiskSymtoms);
                setHighRiskValue();
            }
        }

        public void afterTextChanged(Editable s) {

        }

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.womenHighRiskArrayList.get(groupPosition).getHighRiskSymtomsArrayList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.womenHighRiskArrayList.get(groupPosition).getCategoryName();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return this.womenHighRiskArrayList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
            Utils.findAllTextView(_context, (ViewGroup) convertView.findViewById(R.id.ll_list_group));
        }



        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        Typeface type = Typeface.createFromAsset(_context.getAssets(), "SHRUTI.TTF");
        lblListHeader.setTypeface(type, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    public void setHighRiskValue(){

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilderId=new StringBuilder();
        StringBuilder stringBuilderDays=new StringBuilder();
        String prefix = "";
        String prefixId = "";
        String prefixDays = "";

        for (int i = 0; i < womenHighRiskArrayList.size(); i++) {

            if (womenHighRiskArrayList != null) {
                for (int j = 0; j < womenHighRiskArrayList.get(i).getHighRiskSymtomsArrayList().size(); j++) {
                    HighRiskSymtoms highRisk = womenHighRiskArrayList.get(i).getHighRiskSymtomsArrayList().get(j);
                    if (highRisk.getIsChecked() != null && highRisk.getIsChecked().equals("1")) {
                        stringBuilder.append(highRisk.getSymptomName());
                        prefix = ",";
                        stringBuilder.append(prefix);

                        stringBuilderId.append(highRisk.getSymptomId());
                        stringBuilderDays.append(highRisk.getYear());
                        prefixId = ",";
                        prefixDays = ",";
                        stringBuilderId.append(prefixId);
                        stringBuilderDays.append(prefixDays);
                    }
                }
            }
        }
        if(stringBuilder.toString().length()>=2){
            String risk=stringBuilder.toString().substring(0, stringBuilder.toString().length()-1);
            String riskId=stringBuilderId.toString().substring(0, stringBuilderId.toString().length()-1);
            String days=stringBuilderDays.toString().substring(0, stringBuilderDays.toString().length()-1);
            ed_high_risk_mom.setText(risk);
            ed_high_risk_mom.setTag(riskId);
            textTag.setTag(days);
        }else {
            ed_high_risk_mom.setText(stringBuilder.toString());
            ed_high_risk_mom.setTag(stringBuilderId.toString());
            textTag.setTag(stringBuilderDays.toString());
        }
    }
}
