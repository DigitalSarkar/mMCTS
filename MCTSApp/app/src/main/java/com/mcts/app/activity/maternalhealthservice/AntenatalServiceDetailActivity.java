package com.mcts.app.activity.maternalhealthservice;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mcts.app.R;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.AntenatalService;
import com.mcts.app.model.HighRiskSymtoms;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.DatePickerFragment;
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

public class AntenatalServiceDetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Activity thisActivity;
    private static String TAG = "AntenatalServiceDetailActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private EditText ed_hb, ed_anc_service_date, ed_weight, ed_systolic_bp, ed_diastolic_bp, ed_rbs, ed_muac, ed_ifa, ed_calcium_tab, ed_tt1, ed_tt2, ed_tt_booster, ed_pholik;
    private LinearLayout ll_ifa, ll_pholik, ll_albedize, ll_inj_cortico, ll_albedazole_tab;
    private Spinner sp_urine_sugar, sp_urine_albumin, sp_presentation, sp_hiv, sp_anc_service_type, sp_blood_group_value, sp_vdrl_test;
    private RadioButton rdb_albedazole_yes, rdb_albedazole_no, rdb_yes, rdb_no;
    private Button bt_save, bt_edit;
    private String isAlbedazole, isCortico, bloodGroup;
    private JSONObject jsonObject;
    private long elapsedDays;
    private boolean hivStatus;
    private CustomTextWatcher customTextWatcher;
    AntenatalService antenatalService;
    ArrayList<String> date;
    ArrayList<String> typeId;
    ArrayList<String> highRiskArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antenatal_service_detail);

        setToolBar();
        init();

    }

    private void init() {

        Intent intent = getIntent();
        String stringJson = intent.getStringExtra("stringJson");
        try {
            jsonObject = new JSONObject(stringJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        antenatalService = new AntenatalService();
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_antenatal_service_dialog));
        ed_hb = (EditText) findViewById(R.id.ed_hb);
        ed_weight = (EditText) findViewById(R.id.ed_weight);
        ed_systolic_bp = (EditText) findViewById(R.id.ed_systolic_bp);
        ed_tt1 = (EditText) findViewById(R.id.ed_tt1);
        ed_tt2 = (EditText) findViewById(R.id.ed_tt2);
        ed_tt_booster = (EditText) findViewById(R.id.ed_tt_booster);
        ed_diastolic_bp = (EditText) findViewById(R.id.ed_diastolic_bp);
        sp_urine_sugar = (Spinner) findViewById(R.id.sp_urine_sugar);
        sp_urine_albumin = (Spinner) findViewById(R.id.sp_urine_albumin);
        ed_rbs = (EditText) findViewById(R.id.ed_rbs);
        ed_muac = (EditText) findViewById(R.id.ed_muac);
        sp_presentation = (Spinner) findViewById(R.id.sp_presentation);
        sp_hiv = (Spinner) findViewById(R.id.sp_hiv);
        sp_anc_service_type = (Spinner) findViewById(R.id.sp_anc_service_type);
        sp_blood_group_value = (Spinner) findViewById(R.id.sp_blood_group_value);
        sp_vdrl_test = (Spinner) findViewById(R.id.sp_vdrl_test);
        ed_ifa = (EditText) findViewById(R.id.ed_ifa);
        ed_pholik = (EditText) findViewById(R.id.ed_pholik);
        ed_anc_service_date = (EditText) findViewById(R.id.ed_anc_service_date);
        ed_calcium_tab = (EditText) findViewById(R.id.ed_calcium_tab);
        rdb_albedazole_yes = (RadioButton) findViewById(R.id.rdb_albedazole_yes);
        rdb_albedazole_no = (RadioButton) findViewById(R.id.rdb_albedazole_no);
        rdb_yes = (RadioButton) findViewById(R.id.rdb_yes);
        rdb_no = (RadioButton) findViewById(R.id.rdb_no);
        bt_save = (Button) findViewById(R.id.bt_save);
        bt_edit = (Button) findViewById(R.id.bt_edit);
        ll_ifa = (LinearLayout) findViewById(R.id.ll_ifa);
        ll_pholik = (LinearLayout) findViewById(R.id.ll_pholik);
        ll_albedize = (LinearLayout) findViewById(R.id.ll_albedize);
        ll_inj_cortico = (LinearLayout) findViewById(R.id.ll_inj_cortico);
        ll_albedazole_tab = (LinearLayout) findViewById(R.id.ll_albedazole_tab);

        ed_anc_service_date.addTextChangedListener(new CustomTextWatcher(ed_anc_service_date));
        ed_hb.addTextChangedListener(new CustomTextWatcher(ed_hb));
        ed_weight.addTextChangedListener(new CustomTextWatcher(ed_weight));
        ed_systolic_bp.addTextChangedListener(new CustomTextWatcher(ed_systolic_bp));
        ed_diastolic_bp.addTextChangedListener(new CustomTextWatcher(ed_diastolic_bp));
        ed_rbs.addTextChangedListener(new CustomTextWatcher(ed_rbs));
        ed_muac.addTextChangedListener(new CustomTextWatcher(ed_muac));
        ed_ifa.addTextChangedListener(new CustomTextWatcher(ed_ifa));
        ed_calcium_tab.addTextChangedListener(new CustomTextWatcher(ed_calcium_tab));

        bt_save.setOnClickListener(this);
        bt_edit.setOnClickListener(this);
        rdb_no.setOnClickListener(this);
        rdb_yes.setOnClickListener(this);
        ed_tt1.setOnClickListener(this);
        ed_tt2.setOnClickListener(this);
        ed_tt_booster.setOnClickListener(this);
        rdb_albedazole_no.setOnClickListener(this);
        rdb_albedazole_yes.setOnClickListener(this);
        ed_anc_service_date.setOnClickListener(this);
        sp_anc_service_type.setOnItemSelectedListener(this);

        DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
        try {
            hivStatus = databaseHelper.getMemberHivStatus(jsonObject.getString("emamtahealthId"));
            if (hivStatus) {
                sp_hiv.setSelection(2);
                sp_hiv.setEnabled(false);
            } else {
                sp_hiv.setSelection(1);
            }
            bloodGroup = jsonObject.getString("bloodGroup");
            if (bloodGroup != null) {
                String[] bloodGroupArray = thisActivity.getResources().getStringArray(R.array.blood_group_value);
                for (int i = 0; i < bloodGroupArray.length; i++) {
                    if (bloodGroup.equals(bloodGroupArray[i])) {
                        sp_blood_group_value.setSelection(i);
                        sp_blood_group_value.setEnabled(false);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setToolBar() {

        thisActivity = AntenatalServiceDetailActivity.this;
        Utils.hideSoftKeyboard(thisActivity);
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.pre_pregnancy_seva));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ed_anc_service_date:
                showDatePicker(1);
                break;
            case R.id.ed_tt1:
                showDatePicker(2);
                break;
            case R.id.ed_tt2:
                showDatePicker(3);
                break;
            case R.id.ed_tt_booster:
                showDatePicker(4);
                break;
            case R.id.rdb_yes:
                rdb_yes.setChecked(true);
                rdb_no.setChecked(false);
                isCortico = "1";
                break;
            case R.id.rdb_no:
                rdb_yes.setChecked(false);
                rdb_no.setChecked(true);
                isCortico = "0";
                break;
            case R.id.rdb_albedazole_yes:
                rdb_albedazole_yes.setChecked(true);
                rdb_albedazole_no.setChecked(false);
                isAlbedazole = "1";
                break;
            case R.id.rdb_albedazole_no:
                rdb_albedazole_yes.setChecked(false);
                rdb_albedazole_no.setChecked(true);
                isAlbedazole = "0";
                break;
            case R.id.bt_save:

                if (ed_hb.getText().toString().trim().length() != 0) {
                    float hb = Float.parseFloat(ed_hb.getText().toString().trim());
                    if (hb > 0 && hb < 15) {
                        if (hb < 7) {
                            highRiskArrayList.add(new String("14"));
                            ed_hb.setBackgroundColor(Color.RED);
                            antenatalService.setHb(ed_hb.getText().toString());
                        } else if (hb >= 7 && hb < 11) {
                            antenatalService.setHb(ed_hb.getText().toString());
                            ed_hb.setBackgroundColor(Color.YELLOW);
                        } else if (hb >= 11) {
                            antenatalService.setHb(ed_hb.getText().toString());
                            ed_hb.setBackgroundColor(Color.GREEN);
                        }
                    } else {
                        ed_hb.setText("");
                        antenatalService.setHb(null);
                    }
                }

                if (ed_weight.getText().toString().trim().length() != 0) {
                    float weight = Float.parseFloat(ed_weight.getText().toString().trim());
                    if (weight > 25 && weight < 125) {
                        if (weight < 45) {
                            highRiskArrayList.add(new String("13"));
                            antenatalService.setWeight(ed_weight.getText().toString());
                            ed_weight.setBackgroundColor(Color.RED);
                        } else {
                            antenatalService.setWeight(ed_weight.getText().toString());
                            ed_weight.setBackgroundColor(Color.GREEN);
                        }
                    } else {
                        ed_weight.setText("");
                        antenatalService.setWeight(null);
                    }
                }

                if (ed_rbs.getText().toString().trim().length() != 0) {
                    float rbs = Float.parseFloat(ed_rbs.getText().toString().trim());
                    if (rbs > 50 && rbs < 500) {
                        if (rbs >= 200) {
                            highRiskArrayList.add(new String("17"));
                            antenatalService.setRbs(ed_rbs.getText().toString());
                            ed_rbs.setBackgroundColor(Color.RED);
                        } else {
                            antenatalService.setRbs(ed_rbs.getText().toString());
                            ed_rbs.setBackgroundColor(Color.GREEN);
                        }
                    } else {
                        ed_rbs.setText("");
                        antenatalService.setRbs(null);
                    }
                }

                if (ed_systolic_bp.getText().toString().trim().length() != 0) {
                    float systolic_bp = Float.parseFloat(ed_systolic_bp.getText().toString().trim());
                    if (systolic_bp > 50 && systolic_bp < 220) {
                        if (systolic_bp >= 140) {
                            highRiskArrayList.add(new String("15"));
                            antenatalService.setSystolicbp(ed_systolic_bp.getText().toString());
                            ed_systolic_bp.setBackgroundColor(Color.RED);
                        } else {
                            antenatalService.setSystolicbp(ed_systolic_bp.getText().toString());
                            ed_systolic_bp.setBackgroundColor(Color.GREEN);
                        }
                    } else {
                        ed_systolic_bp.setText("");
                        antenatalService.setSystolicbp(null);
                    }
                }

                if (ed_diastolic_bp.getText().toString().trim().length() != 0) {
                    float diastolic_bp = Float.parseFloat(ed_diastolic_bp.getText().toString().trim());
                    if (diastolic_bp > 50 && diastolic_bp < 200) {
                        if (diastolic_bp >= 90) {
                            highRiskArrayList.add(new String("15"));
                            antenatalService.setDiastolicbp(ed_diastolic_bp.getText().toString());
                            ed_diastolic_bp.setBackgroundColor(Color.RED);
                        } else {
                            antenatalService.setDiastolicbp(ed_diastolic_bp.getText().toString());
                            ed_diastolic_bp.setBackgroundColor(Color.GREEN);
                        }
                    } else {
                        ed_diastolic_bp.setText("");
                        antenatalService.setDiastolicbp(null);
                    }
                }

                if (sp_urine_sugar.getSelectedItemPosition() == 6) {
                    sp_urine_sugar.setBackgroundColor(Color.YELLOW);
                    antenatalService.setUrinesusugar(sp_urine_sugar.getSelectedItem().toString());
                } else if (sp_urine_sugar.getSelectedItemPosition() == 1) {
                    sp_urine_sugar.setBackgroundColor(Color.GREEN);
                    antenatalService.setUrinesusugar(sp_urine_sugar.getSelectedItem().toString());
                } else if (sp_urine_sugar.getSelectedItemPosition() == 2 || sp_urine_sugar.getSelectedItemPosition() == 3 || sp_urine_sugar.getSelectedItemPosition() == 4 || sp_urine_sugar.getSelectedItemPosition() == 5) {

                    sp_urine_sugar.setBackgroundColor(Color.RED);
                    antenatalService.setUrinesusugar(sp_urine_sugar.getSelectedItem().toString());
                } else {
                    antenatalService.setUrinesusugar(null);
                }

                if (sp_urine_albumin.getSelectedItemPosition() == 6) {
                    sp_urine_albumin.setBackgroundColor(Color.YELLOW);
                    antenatalService.setUrinealbumin(sp_urine_albumin.getSelectedItem().toString());
                } else if (sp_urine_albumin.getSelectedItemPosition() == 1) {
                    sp_urine_albumin.setBackgroundColor(Color.GREEN);
                    antenatalService.setUrinealbumin(sp_urine_albumin.getSelectedItem().toString());
                } else if (sp_urine_albumin.getSelectedItemPosition() == 2 || sp_urine_albumin.getSelectedItemPosition() == 3 || sp_urine_albumin.getSelectedItemPosition() == 4 || sp_urine_albumin.getSelectedItemPosition() == 5) {
                    sp_urine_albumin.setBackgroundColor(Color.RED);
                    antenatalService.setUrinealbumin(sp_urine_albumin.getSelectedItem().toString());
                } else {
                    antenatalService.setUrinealbumin(null);
                }

                if (ed_muac.getText().toString().trim().length() != 0) {
                    float mauc = Float.parseFloat(ed_muac.getText().toString().trim());
                    if (mauc > 100 && mauc < 300) {
                        if (mauc <= 210) {

                            antenatalService.setMuac(ed_muac.getText().toString());
                            ed_muac.setBackgroundColor(Color.RED);
                        } else {
                            antenatalService.setMuac(ed_muac.getText().toString());
                            ed_muac.setBackgroundColor(Color.GREEN);
                        }
                    } else {
                        ed_muac.setText("");
                        antenatalService.setMuac(null);
                    }
                }

                if (sp_presentation.getSelectedItemPosition() == 1) {
                    sp_presentation.setBackgroundColor(Color.GREEN);
                    antenatalService.setPresentation(sp_presentation.getSelectedItem().toString());
                } else if (sp_presentation.getSelectedItemPosition() == 2 || sp_presentation.getSelectedItemPosition() == 3) {
                    highRiskArrayList.add(new String("19"));
                    sp_presentation.setBackgroundColor(Color.RED);
                    antenatalService.setPresentation(sp_presentation.getSelectedItem().toString());
                } else {
                    antenatalService.setPresentation(null);
                }

                if (sp_blood_group_value.getSelectedItemPosition() != 0) {
                    antenatalService.setBloodGroup(sp_blood_group_value.getSelectedItem().toString());
                } else {
                    antenatalService.setBloodGroup(null);
                }

                if (sp_hiv.getSelectedItemPosition() == 1) {
                    sp_hiv.setBackgroundColor(Color.GREEN);
                    antenatalService.setHiv(sp_hiv.getSelectedItem().toString());
                } else if (sp_hiv.getSelectedItemPosition() == 2) {
                    highRiskArrayList.add(new String("10"));
                    sp_hiv.setBackgroundColor(Color.RED);
                    antenatalService.setHiv(sp_hiv.getSelectedItem().toString());
                } else if (sp_hiv.getSelectedItemPosition() == 3) {
                    sp_hiv.setBackgroundColor(Color.YELLOW);
                    antenatalService.setHiv(sp_hiv.getSelectedItem().toString());
                } else {
                    antenatalService.setHiv(null);
                }

                if (sp_vdrl_test.getSelectedItemPosition() == 1) {
                    sp_vdrl_test.setBackgroundColor(Color.GREEN);
                    antenatalService.setVdrl(sp_vdrl_test.getSelectedItem().toString());
                } else if (sp_vdrl_test.getSelectedItemPosition() == 2) {
                    sp_vdrl_test.setBackgroundColor(Color.RED);
                    antenatalService.setVdrl(sp_vdrl_test.getSelectedItem().toString());
                } else if (sp_vdrl_test.getSelectedItemPosition() == 3) {
                    sp_vdrl_test.setBackgroundColor(Color.YELLOW);
                    antenatalService.setVdrl(sp_vdrl_test.getSelectedItem().toString());
                } else {
                    antenatalService.setVdrl(null);
                }

                if (ll_pholik.getVisibility() == View.VISIBLE) {
                    antenatalService.setIfaTablet(ed_pholik.getText().toString());
                } else {
                    antenatalService.setIfaTablet(ed_ifa.getText().toString());
                }

                antenatalService.setCalcium(ed_calcium_tab.getText().toString());
                antenatalService.setAncServicedate(ed_anc_service_date.getText().toString());
                antenatalService.setTt1(ed_tt1.getText().toString());
                antenatalService.setTt2(ed_tt2.getText().toString());
                antenatalService.setTt_booster(ed_tt_booster.getText().toString());

                if (antenatalService.getTt1().length() != 0) {
                    SimpleDateFormat simpleDateFormat =
                            new SimpleDateFormat("dd/M/yyyy");
                    try {
                        Calendar c = Calendar.getInstance();
                        c.setTime(simpleDateFormat.parse(ed_tt1.getText().toString()));
                        c.add(Calendar.DATE, 28);
                        antenatalService.setTt2dueDate(simpleDateFormat.format(c.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                /*if (typeId != null) {
                    if (!typeId.contains(antenatalService.getAncservicetrimesterId())) {

                    }
                }*/
                SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                try {
                    JSONObject jsonUserObject = new JSONObject(userDetail);
                    antenatalService.setPregnantwomanregdId(jsonObject.getString("pregnantwomanregdId"));
                    antenatalService.setUserId(jsonUserObject.getJSONArray("userdetails").getJSONObject(0).getString("userId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (sp_vdrl_test.getSelectedItemPosition() != 0) {
                    antenatalService.setVdrl(sp_vdrl_test.getSelectedItem().toString());
                } else {
                    antenatalService.setVdrl(null);
                }


                String validation = FormValidation.validAntenatalDetails(antenatalService, thisActivity);
                if (validation.length() != 0) {
                    CustomLoaderDialog customLoaderDialog = new CustomLoaderDialog(thisActivity);
                    customLoaderDialog.showValidationDialog(validation);
                } else {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    boolean flag = databaseHelper.setAncServiceData(antenatalService);
                    flag = databaseHelper.insertTTDate(antenatalService);
                    if (flag) {
                        flag = databaseHelper.setHighRiskData(highRiskArrayList,antenatalService);
                        if (flag) {
                            flag = databaseHelper.setHighRisk(highRiskArrayList, antenatalService);
                            if (flag) {
                                String str = thisActivity.getResources().getString(R.string.insert);
                                CustomToast customToast = new CustomToast(thisActivity, str);
                                customToast.show();
                                thisActivity.finish();
                            }

                        }

                    }
                }

                break;

            case R.id.bt_edit:
                thisActivity.finish();
                break;
        }

    }

    private void showDatePicker(int position) {

        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        if (position == 1) {
            date.setCallBack(ancServiceDate);
        } else if (position == 2) {
            date.setCallBack(dateForTT1);
        } else if (position == 3) {
            if (ed_tt1.getText().toString().length() != 0) {
                date.setCallBack(dateForTT2);
            } else {
                String str = thisActivity.getResources().getString(R.string.tt_2);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        } else if (position == 4) {
            date.setCallBack(dateForTTBooster);
        }
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ancServiceDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {


            if (sp_anc_service_type.getSelectedItemPosition() != 0) {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                date = databaseHelper.checkServiceDate(sp_anc_service_type.getSelectedItemPosition());
                typeId = databaseHelper.checkTypeId();
                SimpleDateFormat simpleDateFormat =
                        new SimpleDateFormat("dd/M/yyyy");
                if (date != null) {
                    try {
                        for (int i = 0; i < date.size(); i++) {
                            Date regDate = simpleDateFormat.parse(date.get(i));
                            Date ancServiceDate = simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                    + "/" + String.valueOf(year));
                            boolean isAfter = Utils.checkAfterDate(ancServiceDate, regDate);
                            if (isAfter) {
                                ed_anc_service_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                        + "/" + String.valueOf(year));
                            } else {
                                String str = thisActivity.getResources().getString(R.string.after_date);
                                CustomToast customToast = new CustomToast(thisActivity, str);
                                customToast.show();
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    ed_anc_service_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                            + "/" + String.valueOf(year));
                }
            } else {
                String str = thisActivity.getResources().getString(R.string.anc_service_type_select);
                CustomToast customToast = new CustomToast(thisActivity, str);
                customToast.show();
            }
        }
    };

    DatePickerDialog.OnDateSetListener dateForTT1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd/M/yyyy");
            try {
                Date tt1Date = simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));
                Date regDate = simpleDateFormat.parse(jsonObject.getString("pregnantwomanregdDate"));
                Date eddDate = simpleDateFormat.parse(jsonObject.getString("eddDate"));
                boolean isValid = between(tt1Date, regDate, eddDate);
                if (isValid) {
                    ed_tt1.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                            + "/" + String.valueOf(year));
                    ed_tt_booster.setEnabled(false);
                    ed_tt2.setEnabled(false);
                } else {
                    String str = thisActivity.getResources().getString(R.string.tt_1);
                    CustomToast customToast = new CustomToast(thisActivity, str);
                    customToast.show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener dateForTT2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd/M/yyyy");
            try {
                Date tt1Date = simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));
                Date regDate = simpleDateFormat.parse(jsonObject.getString("pregnantwomanregdDate"));
                Date eddDate = simpleDateFormat.parse(jsonObject.getString("eddDate"));
                boolean isValid = between(tt1Date, regDate, eddDate);
                if (isValid) {

                    Date tt1 = simpleDateFormat.parse(ed_tt1.getText().toString());
                    Date tt2 = simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                            + "/" + String.valueOf(year));
                    long different = tt1.getTime() - tt2.getTime();
                    long oneDay = 1000 * 60 * 60 * 24;
                    elapsedDays = different / oneDay;

                    if (elapsedDays > 28) {
                        ed_tt2.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                + "/" + String.valueOf(year));
                    } else {
                        ed_tt2.setText("");
                    }

                } else {
                    String str = thisActivity.getResources().getString(R.string.tt_1);
                    CustomToast customToast = new CustomToast(thisActivity, str);
                    customToast.show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener dateForTTBooster = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd/M/yyyy");
            try {
                Date tt1Date = simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));
                Date regDate = simpleDateFormat.parse(jsonObject.getString("pregnantwomanregdDate"));
                Date eddDate = simpleDateFormat.parse(jsonObject.getString("eddDate"));
                if(eddDate.equals(tt1Date) && regDate.equals(tt1Date)){
                    ed_tt_booster.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                            + "/" + String.valueOf(year));
                    ed_tt1.setEnabled(false);
                    ed_tt2.setEnabled(false);
                }else{
                    boolean isValid = between(tt1Date, regDate, eddDate);
                    if (isValid) {
                        ed_tt_booster.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                + "/" + String.valueOf(year));
                        ed_tt1.setEnabled(false);
                        ed_tt2.setEnabled(false);
                    } else {
                        String str = thisActivity.getResources().getString(R.string.tt_1);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public boolean between(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            if (date.after(dateStart) && date.before(dateEnd)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_anc_service_type:
                if (position != 0) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    boolean isExist = databaseHelper.isServiceExist(position);
                    if (isExist) {
                        String str = sp_anc_service_type.getSelectedItem().toString() + " " + thisActivity.getResources().getString(R.string.already_checked);
                        CustomToast customToast = new CustomToast(thisActivity, str);
                        customToast.show();
                        antenatalService.setAncServicetype(null);
                    } else {
                        antenatalService.setAncServicetype("" + sp_anc_service_type.getSelectedItemPosition());
                    }
                } else {
                    antenatalService.setAncServicetype(null);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                    case R.id.ed_anc_service_date:

                        SimpleDateFormat simpleDateFormat =
                                new SimpleDateFormat("dd/M/yyyy");
                        try {
                            Date ancDate = simpleDateFormat.parse(ed_anc_service_date.getText().toString());
                            Date lmpDate = simpleDateFormat.parse(jsonObject.getString("lmpDate"));
                            long different = ancDate.getTime() - lmpDate.getTime();
                            long oneDay = 1000 * 60 * 60 * 24;
                            elapsedDays = different / oneDay;

                            if (elapsedDays <= 90) {
                                if (typeId != null) {
                                    if (!typeId.contains("1")) {
                                        antenatalService.setAncservicetrimesterId("1");
//                                    Toast.makeText(thisActivity, "elapsedDays=" + elapsedDays, Toast.LENGTH_SHORT).show();
                                        ll_pholik.setVisibility(View.VISIBLE);
                                        antenatalService.setIfaTablet(ed_pholik.getText().toString());
                                        ll_ifa.setVisibility(View.GONE);
                                        ll_albedazole_tab.setVisibility(View.GONE);
                                        ll_inj_cortico.setVisibility(View.GONE);
                                    } else {
                                        ed_anc_service_date.setText("");
                                        String str = thisActivity.getResources().getString(R.string.after_date);
                                        CustomToast customToast = new CustomToast(thisActivity, str);
                                        customToast.show();
                                    }
                                } else {
                                    antenatalService.setAncservicetrimesterId("1");
//                                    Toast.makeText(thisActivity, "elapsedDays=" + elapsedDays, Toast.LENGTH_SHORT).show();
                                    ll_pholik.setVisibility(View.VISIBLE);
                                    antenatalService.setIfaTablet(ed_pholik.getText().toString());
                                    ll_ifa.setVisibility(View.GONE);
                                    ll_albedazole_tab.setVisibility(View.GONE);
                                    ll_inj_cortico.setVisibility(View.GONE);
                                }
                            } else if (elapsedDays > 90 && elapsedDays <= 180) {
                                if (typeId != null) {
                                    if (!typeId.contains("2")) {
                                        antenatalService.setAncservicetrimesterId("2");
                                        antenatalService.setIfaTablet(ed_ifa.getText().toString());
                                        ll_inj_cortico.setVisibility(View.GONE);
                                        ll_pholik.setVisibility(View.GONE);
                                        ll_ifa.setVisibility(View.VISIBLE);
                                        ll_albedazole_tab.setVisibility(View.VISIBLE);

                                    } else {
                                        ed_anc_service_date.setText("");
                                        String str = thisActivity.getResources().getString(R.string.after_date);
                                        CustomToast customToast = new CustomToast(thisActivity, str);
                                        customToast.show();
                                    }
                                } else {
                                    antenatalService.setAncservicetrimesterId("2");
                                    antenatalService.setIfaTablet(ed_ifa.getText().toString());
                                    ll_inj_cortico.setVisibility(View.GONE);
                                    ll_pholik.setVisibility(View.GONE);
                                    ll_ifa.setVisibility(View.VISIBLE);
                                    ll_albedazole_tab.setVisibility(View.VISIBLE);
                                }

                            } else if (elapsedDays > 180 && elapsedDays <= 280) {

                                if (typeId != null) {
                                    if (!typeId.contains("3")) {
                                        antenatalService.setAncservicetrimesterId("3");
                                        ll_inj_cortico.setVisibility(View.VISIBLE);
                                        ll_pholik.setVisibility(View.GONE);
                                        ll_ifa.setVisibility(View.VISIBLE);
                                        ll_albedazole_tab.setVisibility(View.VISIBLE);
                                        antenatalService.setIfaTablet(ed_ifa.getText().toString());
                                    } else if (!typeId.contains("4")) {
                                        antenatalService.setAncservicetrimesterId("4");
                                        ll_inj_cortico.setVisibility(View.VISIBLE);
                                        ll_pholik.setVisibility(View.GONE);
                                        ll_ifa.setVisibility(View.VISIBLE);
                                        ll_albedazole_tab.setVisibility(View.VISIBLE);
                                        antenatalService.setIfaTablet(ed_ifa.getText().toString());
                                    } else {
                                        ed_anc_service_date.setText("");
                                        String str = thisActivity.getResources().getString(R.string.after_date);
                                        CustomToast customToast = new CustomToast(thisActivity, str);
                                        customToast.show();
                                    }
                                } else {
                                    antenatalService.setAncservicetrimesterId("3");
                                    ll_inj_cortico.setVisibility(View.VISIBLE);
                                    ll_pholik.setVisibility(View.GONE);
                                    ll_ifa.setVisibility(View.VISIBLE);
                                    ll_albedazole_tab.setVisibility(View.VISIBLE);
                                    antenatalService.setIfaTablet(ed_ifa.getText().toString());
                                }
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.ed_hb:

//                        if (ed_hb.getText().toString().trim().length() != 0) {
                        float hb = Float.parseFloat(charSequence.toString());
                        if (hb > 0 && hb < 15) {
                            if (hb < 7) {

                                ed_hb.setBackgroundColor(Color.RED);
                            } else if (hb >= 7 && hb < 11) {
                                ed_hb.setBackgroundColor(Color.YELLOW);
                            } else if (hb >= 11) {
                                ed_hb.setBackgroundColor(Color.GREEN);
                            }
                        } else {
                            ed_hb.setBackgroundColor(Color.CYAN);
                        }
//                        }

                        break;
                    case R.id.ed_weight:

//                        if (ed_weight.getText().toString().trim().length() != 0) {
                        float weight = Float.parseFloat(charSequence.toString());
                        if (weight > 25 && weight < 125) {
                            if (weight < 45) {

                                ed_weight.setBackgroundColor(Color.RED);
                            } else {
                                ed_weight.setBackgroundColor(Color.GREEN);
                            }
                        } else {
                            ed_weight.setBackgroundColor(Color.CYAN);
                        }
//                        }

                        break;
                    case R.id.ed_systolic_bp:
//                        if (ed_systolic_bp.getText().toString().trim().length() != 0) {
                        float systolic_bp = Float.parseFloat(charSequence.toString());
                        if (systolic_bp > 50 && systolic_bp < 220) {
                            if (systolic_bp >= 160) {
//                                    antenatalService.setSystolicbp(ed_systolic_bp.getText().toString());
                                ed_systolic_bp.setBackgroundColor(Color.RED);
                            } else {
//                                    antenatalService.setSystolicbp(ed_systolic_bp.getText().toString());
                                ed_systolic_bp.setBackgroundColor(Color.GREEN);
                            }
                        } else {
                            ed_systolic_bp.setBackgroundColor(Color.CYAN);
//                                antenatalService.setSystolicbp(ed_systolic_bp.getText().toString());
                        }
//                        }
                        break;
                    case R.id.ed_diastolic_bp:
//                        if (ed_diastolic_bp.getText().toString().trim().length() != 0) {
                        float diastolic_bp = Float.parseFloat(charSequence.toString());
                        if (diastolic_bp > 50 && diastolic_bp < 200) {
                            if (diastolic_bp >= 90) {
//                                    antenatalService.setDiastolicbp(ed_diastolic_bp.getText().toString());
                                ed_diastolic_bp.setBackgroundColor(Color.RED);
                            } else {
//                                    antenatalService.setDiastolicbp(ed_diastolic_bp.getText().toString());
                                ed_diastolic_bp.setBackgroundColor(Color.GREEN);
                            }
                        } else {
                            ed_diastolic_bp.setBackgroundColor(Color.CYAN);
//                                antenatalService.setDiastolicbp(ed_diastolic_bp.getText().toString());
                        }
//                        }
                        break;
                    case R.id.ed_rbs:
//                        if (ed_rbs.getText().toString().trim().length() != 0) {
                        float rbs = Float.parseFloat(charSequence.toString());
                        if (rbs > 50 && rbs < 500) {
                            if (rbs >= 200) {
//                                    antenatalService.setRbs(ed_rbs.getText().toString());
                                ed_rbs.setBackgroundColor(Color.RED);
                            } else {
//                                    antenatalService.setRbs(ed_rbs.getText().toString());
                                ed_rbs.setBackgroundColor(Color.GREEN);
                            }
                        } else {
                            ed_rbs.setBackgroundColor(Color.CYAN);
//                                antenatalService.setRbs(ed_rbs.getText().toString());
                        }
//                        }
                        break;
                    case R.id.ed_muac:

//                        if (ed_muac.getText().toString().trim().length() != 0) {
                        float mauc = Float.parseFloat(charSequence.toString());
                        if (mauc > 100 && mauc < 300) {
                            if (mauc <= 210) {
//                                    antenatalService.setMuac(ed_muac.getText().toString());
                                ed_muac.setBackgroundColor(Color.RED);
                            } else {
//                                    antenatalService.setMuac(ed_muac.getText().toString());
                                ed_muac.setBackgroundColor(Color.GREEN);
                            }
                        } else {
                            ed_muac.setBackgroundColor(Color.CYAN);
//                                antenatalService.setMuac(ed_muac.getText().toString());
                        }
//                        }


                        break;
                    case R.id.ed_ifa:
                        break;
                    case R.id.ed_calcium_tab:
                        break;
                }

            } else {

            }
        }

        public void afterTextChanged(Editable s) {
            /*if (s.toString().length() != 0) {
                switch (editText.getId()) {
                    case R.id.ed_weight:

                        float weight = Float.parseFloat(s.toString());
                        if (weight > 25 && weight < 125) {
                            if (weight < 45) {

                                ed_weight.setBackgroundColor(Color.RED);
                            } else {
                                ed_weight.setBackgroundColor(Color.GREEN);
                            }
                        } else {
                            ed_weight.setBackgroundColor(Color.CYAN);
                            ed_weight.setText("");
                        }
                        break;
                }
            }*/


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_antenatal_service_detail, menu);
        return true;
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
