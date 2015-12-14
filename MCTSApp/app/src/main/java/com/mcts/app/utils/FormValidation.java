package com.mcts.app.utils;

import android.content.Context;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

import com.mcts.app.R;
import com.mcts.app.activity.familyhealthsurvey.AddFamilyMemberActivity;
import com.mcts.app.model.Member;

/**
 * Created by Raj on 12/1/2015.
 */
public class FormValidation {

    private static String validString;

    static {
        validString = "";
    }



    public static String validateFamilyRegistrationForm(Member familyMember, Context context) {
        validString = "";
        if (familyMember.getHouseNumber() == null || (familyMember.getHouseNumber() != null && familyMember.getHouseNumber().length() <= 0)) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.house_number);
        }
        if (familyMember.getFaliyu() == null || ((familyMember.getFaliyu() != null && familyMember.getFaliyu().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.street_name);
        }
        if (familyMember.getRaciald() == null || ((familyMember.getRaciald() != null && familyMember.getRaciald().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.family_cast);
        }
        if (familyMember.getReligionId() == null || ((familyMember.getReligionId() != null && familyMember.getReligionId().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.family_dharm);
        }if (familyMember.getIsBpl() == null || ((familyMember.getIsBpl() != null && familyMember.getIsBpl().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.family_bpl);
        }if (familyMember.getFirstName() == null || ((familyMember.getFirstName() != null && familyMember.getFirstName().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.family_head_name);
        }if (familyMember.getMiddleName() == null || ((familyMember.getMiddleName() != null && familyMember.getMiddleName().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.husband_name);
        }if (familyMember.getLastName() == null || ((familyMember.getLastName() != null && familyMember.getLastName().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.Sir_Name);
        }if (familyMember.getGender() == null || ((familyMember.getGender() != null && familyMember.getGender().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.Sex);
        }if (familyMember.getMaritalStatus() == null || ((familyMember.getMaritalStatus() != null && familyMember.getMaritalStatus().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.Marital_status);
        }if (familyMember.getBirthDate() == null || ((familyMember.getBirthDate() != null && familyMember.getBirthDate().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.Birth_date);
        }if (familyMember.getMobileNo() != null && familyMember.getMobileNo().length() != 0) {
            if(familyMember.getMobileNo().length() != 10) {
                validString += context.getString(R.string.line_separator) + context.getString(R.string.valid_mobile);
            }
        }

        return validString;
    }

    public static String validateFamilyMemberRegistrationForm(Member familyMember, Context context) {
        validString = "";
        if (familyMember.getFirstName() == null || ((familyMember.getFirstName() != null && familyMember.getFirstName().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.family_head_name);
        }if (familyMember.getMiddleName() == null || ((familyMember.getMiddleName() != null && familyMember.getMiddleName().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.husband_name);
        }if (familyMember.getLastName() == null || ((familyMember.getLastName() != null && familyMember.getLastName().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.Sir_Name);
        }if (familyMember.getGender() == null || ((familyMember.getGender() != null && familyMember.getGender().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.Sex);
        }else{
            if(familyMember.getGender().equals("F")){
                if (familyMember.getIsPregnant() == null || ((familyMember.getIsPregnant() != null && familyMember.getIsPregnant().length() <= 0))) {
                    validString += context.getString(R.string.line_separator) + context.getString(R.string.isPregnent);
                }
                if (familyMember.getAdoptedfpMethod() == null || ((familyMember.getAdoptedfpMethod() != null && familyMember.getAdoptedfpMethod().length() <= 0) )) {
                    validString += context.getString(R.string.line_separator) + context.getString(R.string.Family_welfare);
                }
            }
        }

        if (familyMember.getMaritalStatus() == null || ((familyMember.getMaritalStatus() != null && familyMember.getMaritalStatus().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.Marital_status);
        }if (familyMember.getBirthDate() == null || ((familyMember.getBirthDate() != null && familyMember.getBirthDate().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.Birth_date);
        }if (familyMember.getMemberStatus() == null || ((familyMember.getMemberStatus() != null && familyMember.getMemberStatus().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.Current_Status);
        }if (familyMember.getMobileNo() != null && familyMember.getMobileNo().length() != 0) {
            if(familyMember.getMobileNo().length() != 10) {
                validString += context.getString(R.string.line_separator) + context.getString(R.string.valid_mobile);
            }
        }

        return validString;
    }
}
