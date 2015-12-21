package com.mcts.app.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mcts.app.R;
import com.mcts.app.model.HighRiskSymtoms;
import com.mcts.app.model.WomenHighRisk;
import com.mcts.app.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Raj on 12/18/2015.
 */
public class ExpandablaListAdapter implements ExpandableListAdapter {

    private Context _context;
    private ArrayList<WomenHighRisk> womenHighRiskArrayList;
    private EditText ed_high_risk_mom;

    public ExpandablaListAdapter(Context context, ArrayList<WomenHighRisk> mWomenHighRiskArrayList, EditText mEd_high_risk_mom) {
        this._context = context;
        this.womenHighRiskArrayList = mWomenHighRiskArrayList;
        this.ed_high_risk_mom = mEd_high_risk_mom;
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
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        Utils.findAllTextView(_context, (ViewGroup) convertView.findViewById(R.id.ll_list_item));
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        CheckBox chk_symptoms = (CheckBox) convertView
                .findViewById(R.id.chk_symptoms);
        chk_symptoms.setTag(highRiskSymtoms.getSymptomId() + "," + highRiskSymtoms.getCategoryId() + "," + highRiskSymtoms.getSymptomName());
        if (highRiskSymtoms.getIsChecked() == null || highRiskSymtoms.getIsChecked().equals("1")) {
            chk_symptoms.setChecked(true);
            highRiskSymtoms.setIsChecked("1");
        }else{
            chk_symptoms.setChecked(false);
            highRiskSymtoms.setIsChecked("0");
        }

        txtListChild.setText(highRiskSymtoms.getSymptomName());

        chk_symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if (highRiskSymtoms.getIsChecked() == null || highRiskSymtoms.getIsChecked().equals("0")) {
                    checkBox.setChecked(true);
                    highRiskSymtoms.setIsChecked("1");
                    womenHighRiskArrayList.get(groupPosition).getHighRiskSymtomsArrayList().set(childPosition,highRiskSymtoms);
                    String tag = checkBox.getTag().toString();
                    String[] array = tag.split(",");

//                    Toast.makeText(_context, "SymptomId=" + array[0] + "CategoryId=" + array[1] + "SymptomName=" + array[2], Toast.LENGTH_LONG).show();
                    setHighRiskValue();
                } else {
                    checkBox.setChecked(false);
                    highRiskSymtoms.setIsChecked("0");
                    womenHighRiskArrayList.get(groupPosition).getHighRiskSymtomsArrayList().set(childPosition, highRiskSymtoms);
                    setHighRiskValue();
                }
            }
        });

        return convertView;
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
        }

        Utils.findAllTextView(_context, (ViewGroup) convertView.findViewById(R.id.ll_list_group));

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
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
        String prefix = "";
        String prefixId = "";

        for (int i = 0; i < womenHighRiskArrayList.size(); i++) {

            if (womenHighRiskArrayList != null) {
                for (int j = 0; j < womenHighRiskArrayList.get(i).getHighRiskSymtomsArrayList().size(); j++) {
                    HighRiskSymtoms highRisk = womenHighRiskArrayList.get(i).getHighRiskSymtomsArrayList().get(j);
                    if (highRisk.getIsChecked() != null && highRisk.getIsChecked().equals("1")) {
                        stringBuilder.append(highRisk.getSymptomName());
                        prefix = ",";
                        stringBuilder.append(prefix);

                        stringBuilderId.append(highRisk.getSymptomId());
                        prefixId = ",";
                        stringBuilderId.append(prefixId);
                    }
                }
            }
        }
        if(stringBuilder.toString().length()>=2){
            String risk=stringBuilder.toString().substring(0, stringBuilder.toString().length()-1);
            String riskId=stringBuilderId.toString().substring(0, stringBuilderId.toString().length()-1);
            ed_high_risk_mom.setText(risk);
            ed_high_risk_mom.setTag(riskId);
        }else {
            ed_high_risk_mom.setText(stringBuilder.toString());
            ed_high_risk_mom.setTag(stringBuilderId.toString());
        }
    }
}
