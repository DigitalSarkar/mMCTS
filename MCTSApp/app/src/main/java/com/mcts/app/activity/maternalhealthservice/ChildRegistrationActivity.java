package com.mcts.app.activity.maternalhealthservice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.annotations.Until;
import com.mcts.app.R;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.FormValidation;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class ChildRegistrationActivity extends AppCompatActivity {

    Activity thisActivity;
    private static String TAG = "ChildRegistrationActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private String childData, childregdId, isChildbirthlive, isSex, isFeeding, kangarooCare, complication1, complication2, isInjCortico, isVitaminK;
    private TextView txt_village_name, txt_anc_name, txt_child_regd_number, txt_child_birthdate, txt_child_name_name;
    private EditText ed_child_birth_weight;
    private RadioButton rdb_childbirthlive, rdb_childbirthdeath, rdb_sex_Male, rdb_sex_Female, rdb_feeding_yes, rdb_feeding_no, rdb_kangaroo_care_yes, rdb_kangaroo_care_no;
    private RadioButton rdb_vitamin_k_yes, rdb_vitamin_k_no, rdb_dont_know, rdb_inj_cortico_no, rdb_inj_cortico_yes, rdb_vitamin_k_dont_know;
    private CheckBox chk_comp_1, chk_comp_2;
    private Button bt_save_child, bt_edit_child;
    private LinearLayout ll_child_name,ll_child_weight,ll_breastfeeding,ll_kangarooCare,ll_vitaminK,ll_birthcomp;
    private JSONObject jsonChildObject;
    StringBuilder stringBuilder = new StringBuilder();
    String prefix = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_registration);

        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = ChildRegistrationActivity.this;
        Utils.hideSoftKeyboard(thisActivity);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.child_reg));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        Intent intent = getIntent();
        childData = intent.getStringExtra("childData");
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_child_reg));

        txt_village_name = (TextView) findViewById(R.id.txt_village_name);
        txt_anc_name = (TextView) findViewById(R.id.txt_anc_name);
        txt_child_regd_number = (TextView) findViewById(R.id.txt_child_regd_number);
        txt_child_birthdate = (TextView) findViewById(R.id.txt_child_birthdate);
        txt_child_name_name = (TextView) findViewById(R.id.txt_child_name_name);
        ed_child_birth_weight = (EditText) findViewById(R.id.ed_child_birth_weight);
        rdb_childbirthlive = (RadioButton) findViewById(R.id.rdb_childbirthlive);
        rdb_childbirthdeath = (RadioButton) findViewById(R.id.rdb_childbirthdeath);
        rdb_sex_Male = (RadioButton) findViewById(R.id.rdb_sex_Male);
        rdb_sex_Female = (RadioButton) findViewById(R.id.rdb_sex_Female);
        rdb_feeding_yes = (RadioButton) findViewById(R.id.rdb_feeding_yes);
        rdb_feeding_no = (RadioButton) findViewById(R.id.rdb_feeding_no);
        rdb_kangaroo_care_yes = (RadioButton) findViewById(R.id.rdb_kangaroo_care_yes);
        rdb_kangaroo_care_no = (RadioButton) findViewById(R.id.rdb_kangaroo_care_no);
        rdb_vitamin_k_yes = (RadioButton) findViewById(R.id.rdb_vitamin_k_yes);
        rdb_vitamin_k_no = (RadioButton) findViewById(R.id.rdb_vitamin_k_no);
        rdb_dont_know = (RadioButton) findViewById(R.id.rdb_dont_know);
        rdb_inj_cortico_no = (RadioButton) findViewById(R.id.rdb_inj_cortico_no);
        rdb_inj_cortico_yes = (RadioButton) findViewById(R.id.rdb_inj_cortico_yes);
        rdb_vitamin_k_dont_know = (RadioButton) findViewById(R.id.rdb_vitamin_k_dont_know);
        chk_comp_1 = (CheckBox) findViewById(R.id.chk_comp_1);
        chk_comp_2 = (CheckBox) findViewById(R.id.chk_comp_2);
        bt_save_child = (Button) findViewById(R.id.bt_save_child);
        bt_edit_child = (Button) findViewById(R.id.bt_edit_child);
        ll_child_name = (LinearLayout) findViewById(R.id.ll_child_name);
        ll_child_weight = (LinearLayout) findViewById(R.id.ll_child_weight);
        ll_breastfeeding = (LinearLayout) findViewById(R.id.ll_breastfeeding);
        ll_kangarooCare = (LinearLayout) findViewById(R.id.ll_kangarooCare);
        ll_vitaminK = (LinearLayout) findViewById(R.id.ll_vitaminK);
        ll_birthcomp = (LinearLayout) findViewById(R.id.ll_birthcomp);
        ed_child_birth_weight.addTextChangedListener(new CustomTextWatcher(ed_child_birth_weight));

        if (childData != null) {
            try {
                jsonChildObject = new JSONObject(childData);
                childregdId = jsonChildObject.getString("childregdId");
                txt_village_name.setText(jsonChildObject.getString("villageName"));
                txt_child_birthdate.setText(jsonChildObject.getString("pregancyoutcomeDate"));
                txt_child_regd_number.setText(jsonChildObject.getString("childregdId"));
                txt_anc_name.setText(jsonChildObject.getString("firstName") + " " + jsonChildObject.getString("middleName") + " " + jsonChildObject.getString("lastName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickEvent(View view) {

        switch (view.getId()) {
            case R.id.bt_save_child:
                Random rand = new Random();
                int emamtahealthId = rand.nextInt(900000) + 100000;

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("childregdId", childregdId);
                hashMap.put("birthStatus", isChildbirthlive);
                hashMap.put("birthWeight", ed_child_birth_weight.getText().toString());
                hashMap.put("breastFeeding", isFeeding);
                hashMap.put("kangarooCare", kangarooCare);
                hashMap.put("corticosteroid", isInjCortico);
                hashMap.put("injvitaminK", isVitaminK);
                hashMap.put("emamtahealthId", "" + emamtahealthId);
                hashMap.put("gender", isSex);
                if (complication1 != null) {
                    stringBuilder.append(complication1);
                    prefix = ",";
                    stringBuilder.append(prefix);
                }
                if (complication2 != null) {
                    stringBuilder.append(complication2);
                    prefix = ",";
                    stringBuilder.append(prefix);
                }
                if (stringBuilder.length() != 0) {
                    String complication = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
                    hashMap.put("birthComplication", complication);
                }

                String validation = FormValidation.validChildReg(hashMap, thisActivity);
                if (validation.length() != 0) {
                    CustomLoaderDialog customLoaderDialog = new CustomLoaderDialog(thisActivity);
                    customLoaderDialog.showValidationDialog(validation);
                } else {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    boolean isSaved = databaseHelper.saveChildData(hashMap);
                    if (isSaved) {
                        SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                        String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                        try {
                            JSONObject jsonObject = new JSONObject(userDetail);
                            hashMap.put("subcentreId", jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("subcentreId"));
                            String userId = jsonObject.getJSONArray("userdetails").getJSONObject(0).getString("userId");
                            hashMap.put("userId", userId);

                            hashMap.put("villageId", jsonChildObject.getString("villageId"));
                            hashMap.put("emamtafamilyId", jsonChildObject.getString("emamtafamilyId"));
                            hashMap.put("isHead", "0");
                            hashMap.put("firstName", txt_child_name_name.getText().toString());
                            hashMap.put("middleName", jsonChildObject.getString("middleName"));
                            hashMap.put("lastName", jsonChildObject.getString("lastName"));
                            hashMap.put("birthDate", jsonChildObject.getString("pregancyoutcomeDate"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(isChildbirthlive.equals("1")){
                            isSaved = databaseHelper.saveNewChildMember(hashMap);
                            if (isSaved) {
                                ArrayList<HashMap<String, String>> vaccinationArrayList = new ArrayList<>();
                                String[] arrayName = new String[]{"Bcg",
                                        "Hepatitis",
                                        "Polio",
                                        "Pentavalent1",
                                        "Polio1",
                                        "Pentavalent2",
                                        "Polio2",
                                        "Pentavalent3",
                                        "Polio3",
                                        "Measles1",
                                        "vitaminA1",
                                        "Measles2",
                                        "PolioB",
                                        "dptB"};

                                String[] arrayDueDate = new String[]{hashMap.get("birthDate"),
                                        hashMap.get("birthDate"),
                                        hashMap.get("birthDate"),
                                        Utils.addDays(hashMap.get("birthDate"), 41),
                                        Utils.addDays(hashMap.get("birthDate"), 41),
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        Utils.addDays(hashMap.get("birthDate"), 269),
                                        Utils.addDays(hashMap.get("birthDate"), 269),
                                        "null",
                                        "null",
                                        "null"};

                                for (int i = 0; i < arrayName.length; i++) {
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("childregdId", hashMap.get("childregdId"));
                                    map.put("emamtahealthId", hashMap.get("emamtahealthId"));
                                    map.put("userId", hashMap.get("userId"));
                                    map.put("vaccineName", arrayName[i]);
                                    map.put("vaccinedueDate", arrayDueDate[i]);
                                    vaccinationArrayList.add(map);
                                }

                                isSaved = databaseHelper.setChildVaccination(vaccinationArrayList);

                                if (isSaved) {
                                    String str = thisActivity.getResources().getString(R.string.member_new_success);
                                    CustomToast customToast = new CustomToast(thisActivity, str);
                                    customToast.show();
                                    thisActivity.finish();
                                }
                            }
                        }
                    }
                }
                break;

            case R.id.rdb_childbirthlive:
                isChildbirthlive = "1";
                rdb_childbirthlive.setChecked(true);
                rdb_childbirthdeath.setChecked(false);
                ed_child_birth_weight.setEnabled(true);

                ll_child_name.setVisibility(View.VISIBLE);
                ll_child_weight.setVisibility(View.VISIBLE);
                ll_breastfeeding.setVisibility(View.VISIBLE);
                ll_kangarooCare.setVisibility(View.VISIBLE);
                ll_vitaminK.setVisibility(View.VISIBLE);
                ll_birthcomp.setVisibility(View.VISIBLE);

                break;
            case R.id.rdb_childbirthdeath:
                isChildbirthlive = "0";
                rdb_childbirthlive.setChecked(false);
                rdb_childbirthdeath.setChecked(true);
                ed_child_birth_weight.setText("");
                ed_child_birth_weight.setEnabled(false);

                ll_child_name.setVisibility(View.GONE);
                ll_child_weight.setVisibility(View.GONE);
                ll_breastfeeding.setVisibility(View.GONE);
                ll_kangarooCare.setVisibility(View.GONE);
                ll_vitaminK.setVisibility(View.GONE);
                ll_birthcomp.setVisibility(View.GONE);

                break;
            case R.id.rdb_sex_Male:
                isSex = "M";
                rdb_sex_Male.setChecked(true);
                rdb_sex_Female.setChecked(false);
                txt_child_name_name.setText(thisActivity.getResources().getString(R.string.boy));
                break;
            case R.id.rdb_sex_Female:
                isSex = "F";
                rdb_sex_Male.setChecked(false);
                rdb_sex_Female.setChecked(true);
                txt_child_name_name.setText(thisActivity.getResources().getString(R.string.girl));
                break;
            case R.id.rdb_feeding_yes:
                isFeeding = "1";
                rdb_feeding_yes.setChecked(true);
                rdb_feeding_no.setChecked(false);
                break;
            case R.id.rdb_feeding_no:
                isFeeding = "0";
                rdb_feeding_yes.setChecked(false);
                rdb_feeding_no.setChecked(true);
                break;
            case R.id.rdb_kangaroo_care_yes:
                kangarooCare = "1";
                rdb_kangaroo_care_yes.setChecked(true);
                rdb_kangaroo_care_no.setChecked(false);
                break;
            case R.id.rdb_kangaroo_care_no:
                kangarooCare = "0";
                rdb_kangaroo_care_yes.setChecked(false);
                rdb_kangaroo_care_no.setChecked(true);
                break;

            case R.id.rdb_inj_cortico_yes:
                isInjCortico = "1";
                rdb_inj_cortico_yes.setChecked(true);
                rdb_inj_cortico_no.setChecked(false);
                rdb_dont_know.setChecked(false);
                break;

            case R.id.rdb_inj_cortico_no:
                isInjCortico = "0";
                rdb_inj_cortico_yes.setChecked(false);
                rdb_inj_cortico_no.setChecked(true);
                rdb_dont_know.setChecked(false);
                break;

            case R.id.rdb_dont_know:
                isInjCortico = "2";
                rdb_inj_cortico_yes.setChecked(false);
                rdb_inj_cortico_no.setChecked(false);
                rdb_dont_know.setChecked(true);
                break;

            case R.id.rdb_vitamin_k_yes:
                isVitaminK = "1";
                rdb_vitamin_k_yes.setChecked(true);
                rdb_vitamin_k_no.setChecked(false);
                rdb_vitamin_k_dont_know.setChecked(false);
                break;

            case R.id.rdb_vitamin_k_no:
                isVitaminK = "0";
                rdb_vitamin_k_yes.setChecked(false);
                rdb_vitamin_k_dont_know.setChecked(false);
                rdb_vitamin_k_no.setChecked(true);
                break;

            case R.id.rdb_vitamin_k_dont_know:
                isVitaminK = "2";
                rdb_vitamin_k_yes.setChecked(false);
                rdb_vitamin_k_no.setChecked(false);
                rdb_vitamin_k_dont_know.setChecked(true);
                break;

            case R.id.chk_comp_1:
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    complication1 = thisActivity.getResources().getString(R.string.complication1);
                } else {
                    complication1 = null;
                }
                break;
            case R.id.chk_comp_2:
                CheckBox checkBox2 = (CheckBox) view;
                if (checkBox2.isChecked()) {
                    complication2 = thisActivity.getResources().getString(R.string.complication2);
                } else {
                    complication2 = null;
                }
                break;
        }
    }

    private class CustomTextWatcher implements TextWatcher {
        public EditText editText;

        public CustomTextWatcher(EditText meditText) {
            this.editText = meditText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (charSequence.length() != 0) {

                switch (editText.getId()) {

                    case R.id.ed_child_birth_weight:

                        float weight = Float.parseFloat(charSequence.toString());
                        if (weight <= 5.0) {
                            if (weight <= 2.5) {
                                ed_child_birth_weight.setBackgroundColor(Color.RED);
                            } else {
                                ed_child_birth_weight.setBackgroundColor(Color.GREEN);
                            }
                        } else {
                            ed_child_birth_weight.setBackgroundColor(Color.CYAN);
                        }

                        break;

                }

            }
        }

        public void afterTextChanged(Editable s) {

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            thisActivity.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
        // Handle your other action bar items...
    }
}
