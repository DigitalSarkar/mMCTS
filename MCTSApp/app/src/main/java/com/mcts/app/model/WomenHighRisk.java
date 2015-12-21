package com.mcts.app.model;

import java.util.ArrayList;

/**
 * Created by Raj on 12/17/2015.
 */
public class WomenHighRisk {

    String categoryId;
    String categoryName;
    ArrayList<HighRiskSymtoms> highRiskSymtomsArrayList;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<HighRiskSymtoms> getHighRiskSymtomsArrayList() {
        return highRiskSymtomsArrayList;
    }

    public void setHighRiskSymtomsArrayList(ArrayList<HighRiskSymtoms> highRiskSymtomsArrayList) {
        this.highRiskSymtomsArrayList = highRiskSymtomsArrayList;
    }
}
