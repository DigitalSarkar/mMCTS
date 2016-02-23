package com.mcts.app.utils;

import android.app.Activity;
import android.content.Context;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

import com.mcts.app.R;
import com.mcts.app.activity.familyhealthsurvey.AddFamilyMemberActivity;
import com.mcts.app.model.AntenatalService;
import com.mcts.app.model.DeliveryDetails;
import com.mcts.app.model.Member;
import com.mcts.app.model.PregnantWomen;

import java.util.HashMap;

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

    public static String pregnantWomenRegister(PregnantWomen pregnantWomen, Context context) {
        validString = "";
        if (pregnantWomen.getIsBpl() == null || ((pregnantWomen.getIsBpl() != null && pregnantWomen.getIsBpl().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.family_bpl);
        }if (pregnantWomen.getAncDate() == null || ((pregnantWomen.getAncDate() != null && pregnantWomen.getAncDate().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.anc_regd_date);
        }if (pregnantWomen.getLmpDate() == null || ((pregnantWomen.getLmpDate() != null && pregnantWomen.getLmpDate().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.lmp_date);
        }if (pregnantWomen.getGravida() == 0 ) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.anc_gravida);
        }
//        int temp = pregnantWomen.getGravida() - 1;
        if (pregnantWomen.getTempTotal()<0) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.anc_para);
        }
        if (pregnantWomen.getTempTotal()<0) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.anc_abortion);
        }
        if (pregnantWomen.getMale() <0){
            validString += context.getString(R.string.line_separator) + context.getString(R.string.live_male);

        }if (pregnantWomen.getFemale() <0){
            validString += context.getString(R.string.line_separator) + context.getString(R.string.live_female);
        }
        if (pregnantWomen.getHeight()== null || ((pregnantWomen.getHeight() != null && pregnantWomen.getHeight().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.height);
        }if (pregnantWomen.getWeight()== null || ((pregnantWomen.getWeight() != null && pregnantWomen.getWeight().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.weight);
        }if (pregnantWomen.getIsChiranjivi()== null || ((pregnantWomen.getIsChiranjivi() != null && pregnantWomen.getIsChiranjivi().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.chiranjivi);
        }/*if (pregnantWomen.getAshaName()== null || ((pregnantWomen.getAshaName() != null && pregnantWomen.getAshaName().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.asha_name);
        }*/

        return validString;
    }

    public static String validAntenatalDetails(AntenatalService antenatalService, Context context) {
        validString = "";
        if (antenatalService.getAncServicedate() == null || ((antenatalService.getAncServicedate() != null && antenatalService.getAncServicedate().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.anc_service_date);
        } if (antenatalService.getHb() == null || ((antenatalService.getHb() != null && antenatalService.getHb().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.hb);
        }if (antenatalService.getWeight() == null || ((antenatalService.getWeight() != null && antenatalService.getWeight().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.weight);
        }if (antenatalService.getSystolicbp() == null || ((antenatalService.getSystolicbp() != null && antenatalService.getSystolicbp().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.systolic_bp);
        }if (antenatalService.getDiastolicbp() == null || ((antenatalService.getDiastolicbp() != null && antenatalService.getDiastolicbp().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.diastolic_bp);
        }if (antenatalService.getRbs() == null || ((antenatalService.getRbs() != null && antenatalService.getRbs().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.rbs);
        }if (antenatalService.getPresentation() == null || ((antenatalService.getPresentation() != null && antenatalService.getPresentation().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.presentation);
        }if (antenatalService.getHiv() == null || ((antenatalService.getHiv() != null && antenatalService.getHiv().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.hiv);
        }if (antenatalService.getIfaTablet() == null || ((antenatalService.getIfaTablet() != null && antenatalService.getIfaTablet().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.ifa);
        }if (antenatalService.getCalcium() == null || ((antenatalService.getCalcium() != null && antenatalService.getCalcium().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.calcium_tab);
        }if (antenatalService.getVdrl() == null || ((antenatalService.getVdrl() != null && antenatalService.getVdrl().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.vdrl_test);
        }if (antenatalService.getAncServicetype() == null || ((antenatalService.getAncServicetype() != null && antenatalService.getAncServicetype().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.anc_service_type);
        }
        return validString;
    }

    public static String validDeliveryDetails(DeliveryDetails deliveryDetails, Context context) {
        validString = "";
        if (deliveryDetails.getPregnancyoutcomeregdDate() == null || ((deliveryDetails.getPregnancyoutcomeregdDate() != null && deliveryDetails.getPregnancyoutcomeregdDate().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.delivery_regd_date);
        } if (deliveryDetails.getPregancyoutcometypeId() == null || ((deliveryDetails.getPregancyoutcometypeId() != null && deliveryDetails.getPregancyoutcometypeId().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.delivery_type);
        }if (deliveryDetails.getEmployeeId() == null || ((deliveryDetails.getEmployeeId() != null && deliveryDetails.getEmployeeId().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.delivery_name);
        }if (deliveryDetails.getPregancyoutcomeDate() == null || ((deliveryDetails.getPregancyoutcomeDate() != null && deliveryDetails.getPregancyoutcomeDate().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.delivery_date);
        }if (deliveryDetails.getDischargeDate() == null || ((deliveryDetails.getDischargeDate() != null && deliveryDetails.getDischargeDate().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.delivery_dischage_date);
        }if (deliveryDetails.getNumberofChild() == null || ((deliveryDetails.getNumberofChild() != null && deliveryDetails.getNumberofChild().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.children_delivered);
        }if (deliveryDetails.getDeliverymamtaKit() == null || ((deliveryDetails.getDeliverymamtaKit() != null && deliveryDetails.getDeliverymamtaKit().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.delivery_mamta_kit);
        }if (deliveryDetails.getDelivery108Service() == null || ((deliveryDetails.getDelivery108Service() != null && deliveryDetails.getDelivery108Service().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.delivery_108_service);
        }if (deliveryDetails.getDeliveryIncentive() == null || ((deliveryDetails.getDeliveryIncentive() != null && deliveryDetails.getDeliveryIncentive().length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.delivery_transport_incentive);
        }
        return validString;
    }


    public static String validChildReg(HashMap<String, String> childDetails, Context context) {
        validString = "";
        if (childDetails.get("birthStatus") == null || ((childDetails.get("birthStatus") != null && childDetails.get("birthStatus").length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.child_birth_status);
        }if (childDetails.get("gender") == null || ((childDetails.get("gender") != null && childDetails.get("gender").length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.child_gender);
        }

        if(childDetails.get("birthStatus") != null){
            if(childDetails.get("birthStatus").equals("1")){
                if (childDetails.get("birthWeight") == null || ((childDetails.get("birthWeight") != null && childDetails.get("birthWeight").length() <= 0) )) {
                    if(childDetails.get("birthStatus") != null && childDetails.get("birthStatus").equals("1")) {
                        validString += context.getString(R.string.line_separator) + context.getString(R.string.child_birth_weight);
                    }
                } if (childDetails.get("breastFeeding") == null || ((childDetails.get("breastFeeding") != null && childDetails.get("breastFeeding").length() <= 0) )) {
                    validString += context.getString(R.string.line_separator) + context.getString(R.string.breast_feeding);
                }if (childDetails.get("kangarooCare") == null || ((childDetails.get("kangarooCare") != null && childDetails.get("kangarooCare").length() <= 0) )) {
                    validString += context.getString(R.string.line_separator) + context.getString(R.string.kangaroo_care);
                }if (childDetails.get("corticosteroid") == null || ((childDetails.get("corticosteroid") != null && childDetails.get("corticosteroid").length() <= 0) )) {
                    validString += context.getString(R.string.line_separator) + context.getString(R.string.inj_cortico);
                }if (childDetails.get("injvitaminK") == null || ((childDetails.get("injvitaminK") != null && childDetails.get("injvitaminK").length() <= 0) )) {
                    validString += context.getString(R.string.line_separator) + context.getString(R.string.vitamin_k);
                }
            }
        }

        return validString;
    }

    public static String validFPDetails(HashMap<String, String> fpDetails, Activity context) {
        validString = "";
        if (fpDetails.get("facilityId") == null || ((fpDetails.get("facilityId") != null && fpDetails.get("facilityId").length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.service_place);
        } if (fpDetails.get("adoptedfpMethod") == null || ((fpDetails.get("adoptedfpMethod") != null && fpDetails.get("adoptedfpMethod").length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.Family_welfare);
            if(fpDetails.get("adoptedfpMethod") != null && fpDetails.get("adoptedfpMethod").equals("5")) {
                validString += context.getString(R.string.line_separator) + context.getString(R.string.coppert_type);
            }
        } if (fpDetails.get("adoptedDate") == null || ((fpDetails.get("adoptedDate") != null && fpDetails.get("adoptedDate").length() <= 0) )) {
            validString += context.getString(R.string.line_separator) + context.getString(R.string.fm_walfare_date);
        }
        return validString;
    }
}
