package com.mcts.app.activity.maternalhealthservice;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mcts.app.R;
import com.mcts.app.adapter.StatusAdapter;
import com.mcts.app.adapter.TreatmentAdapter;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.DeliveryDetails;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.DatePickerFragment;
import com.mcts.app.utils.FormValidation;
import com.mcts.app.utils.TimePickerDialogFragment;
import com.mcts.app.utils.Utils;
import com.mcts.app.volley.CustomLoaderDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DeliveryRegistarActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemSelectedListener {

    Activity thisActivity;
    private static String TAG = "DeliveryRegistarActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView txt_village_name, txt_anc_name, txt_new_pregnancy_number;
    private EditText ed_anc_regdnumber, ed_delivery_discharge_date, ed_delivery_place, ed_delivery_place_name, ed_delivery_designation,
            ed_delivery_name, ed_delivery_treatment, ed_delivery_regd_date, ed_delivery_date;
    private Spinner sp_delivery_type, sp_children_delivered;
    private RadioButton rdb_transport_service_yes, rdb_transport_service_no, rdb_108_service_yes, rdb_108_service_no, rdb_mamta_kit_yes, rdb_mamta_kit_no;
    private Button bt_save, bt_edit;
    private JSONObject jsonObject;
    private String date;
    private Dialog progressDialog;
    private String dirstID, dirstName, optionId, stateId, stateName, optionName, isTransportService, is108Service, isMamtaKit, pregnantwomanregdDate, pregnantwomanregdId;
    private String talukaID, talukaName, selectionId, selectionName, employeeId, designName, employeeName;
    ArrayList<MaritalStatus> treatmentList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_registar);

        setToolBar();
        init();
    }


    private void setToolBar() {

        thisActivity = DeliveryRegistarActivity.this;
        Utils.hideSoftKeyboard(thisActivity);
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_delivery_regs));
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.pre_pregnancy_seva));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {


        txt_village_name = (TextView) findViewById(R.id.txt_village_name);
        txt_anc_name = (TextView) findViewById(R.id.txt_anc_name);
        txt_new_pregnancy_number = (TextView) findViewById(R.id.txt_new_pregnancy_number);
        ed_anc_regdnumber = (EditText) findViewById(R.id.ed_anc_regdnumber);
        ed_delivery_discharge_date = (EditText) findViewById(R.id.ed_delivery_discharge_date);
        ed_delivery_place = (EditText) findViewById(R.id.ed_delivery_place);
        ed_delivery_place_name = (EditText) findViewById(R.id.ed_delivery_place_name);
        ed_delivery_name = (EditText) findViewById(R.id.ed_delivery_name);
        ed_delivery_treatment = (EditText) findViewById(R.id.ed_delivery_treatment);
        ed_delivery_regd_date = (EditText) findViewById(R.id.ed_delivery_regd_date);
        ed_delivery_designation = (EditText) findViewById(R.id.ed_delivery_designation);
        ed_delivery_date = (EditText) findViewById(R.id.ed_delivery_date);
        sp_delivery_type = (Spinner) findViewById(R.id.sp_delivery_type);
        sp_children_delivered = (Spinner) findViewById(R.id.sp_children_delivered);
        rdb_transport_service_yes = (RadioButton) findViewById(R.id.rdb_transport_service_yes);
        rdb_transport_service_no = (RadioButton) findViewById(R.id.rdb_transport_service_no);
        rdb_108_service_yes = (RadioButton) findViewById(R.id.rdb_108_service_yes);
        rdb_108_service_no = (RadioButton) findViewById(R.id.rdb_108_service_no);
        rdb_mamta_kit_yes = (RadioButton) findViewById(R.id.rdb_mamta_kit_yes);
        rdb_mamta_kit_no = (RadioButton) findViewById(R.id.rdb_mamta_kit_no);
        bt_save = (Button) findViewById(R.id.bt_save);
        bt_edit = (Button) findViewById(R.id.bt_edit);

        Intent intent = getIntent();
        String stringJson = intent.getStringExtra("data");
        if (stringJson != null) {
            try {
                jsonObject = new JSONObject(stringJson);
                txt_village_name.setText(jsonObject.getString("villageName"));
                txt_anc_name.setText(jsonObject.getString("name"));
                txt_new_pregnancy_number.setText(jsonObject.getString("pregnantwomanregdId"));
                pregnantwomanregdDate = (jsonObject.getString("pregnantwomanregdDate"));
                pregnantwomanregdId = (jsonObject.getString("pregnantwomanregdId"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        String[] array = thisActivity.getResources().getStringArray(R.array.delivery_treatment_list);
        treatmentList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            MaritalStatus maritalStatus = new MaritalStatus();
            maritalStatus.setId("" + i);
            maritalStatus.setStatus(array[i]);
            treatmentList.add(maritalStatus);
        }

        ed_delivery_regd_date.setOnClickListener(this);
        ed_delivery_date.setOnClickListener(this);
        ed_delivery_discharge_date.setOnClickListener(this);
        ed_delivery_place.setOnClickListener(this);
        ed_delivery_treatment.setOnClickListener(this);
        sp_delivery_type.setOnItemSelectedListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ed_delivery_regd_date:
                showDatePicker(false, 1);
                break;
            case R.id.ed_delivery_date:
                showDatePicker(true, 2);
                break;
            case R.id.ed_delivery_discharge_date:
                showDatePicker(true, 3);
                break;
            case R.id.ed_delivery_place:
                showDeliveryPlaceDialog();
                break;
            case R.id.ed_delivery_treatment:

                showComplicationDialog(treatmentList, ed_delivery_treatment);
                break;

        }
    }

    private void showDatePicker(boolean isTime, int position) {

        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        if (!isTime) {
            date.setCallBack(onMTPDate);
        } else {
            if (position == 2) {
                date.setCallBack(onDate);
            } else if (position == 3) {
                date.setCallBack(onDischargeDate);
            }
        }
        date.show(getSupportFragmentManager(), "Date Picker");
    }


    DatePickerDialog.OnDateSetListener onMTPDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            if (view.isShown()) {
                SimpleDateFormat simpleDateFormat =
                        new SimpleDateFormat("dd/M/yyyy");
                Date pregDate = null;
                Date delDate = null;
                try {
                    pregDate = simpleDateFormat.parse(pregnantwomanregdDate);
                    delDate = simpleDateFormat.parse(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                            + "/" + String.valueOf(year));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                boolean isAfter = Utils.checkAfterDate(delDate, pregDate);
                if (isAfter || pregDate.equals(delDate)) {
                    ed_delivery_regd_date.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                            + "/" + String.valueOf(year));
                }


            }
        }
    };

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            if (view.isShown()) {
                date = (String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));
                showTimePicker(1);
            }
        }
    };

    DatePickerDialog.OnDateSetListener onDischargeDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            if (view.isShown()) {
                date = (String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year));
                showTimePicker(2);
            }
        }
    };

    private void showTimePicker(int i) {

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        /** Creating a bundle object to pass currently set time to the fragment */
        Bundle b = new Bundle();
        /** Adding currently set hour to bundle object */
        b.putInt("set_hour", hours);
        /** Adding currently set minute to bundle object */
        b.putInt("set_minute", minutes);
        /** Instantiating TimePickerDialogFragment */
        TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
        /** Setting the bundle object on timepicker fragment */
        timePicker.setArguments(b);
        if (i == 1) {
            timePicker.setCallBack(onTime1);
        } else {
            timePicker.setCallBack(onTime2);
        }
        /** Adding the fragment object to the fragment transaction */
        timePicker.show(getSupportFragmentManager(), "time_picker");
    }

    TimePickerDialog.OnTimeSetListener onTime1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            String date=ed_delivery_date.getTag().toString();
            updateTime(hourOfDay, minute, 1);
        }
    };

    TimePickerDialog.OnTimeSetListener onTime2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            String date=ed_delivery_date.getTag().toString();
            updateTime(hourOfDay, minute, 2);
        }
    };

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins, int i) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(date).append(",").append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        if (i == 1) {
            ed_delivery_date.setText(aTime);
        } else {
            ed_delivery_discharge_date.setText(aTime);
        }
    }

    public void showDeliveryPlaceDialog() {

        progressDialog = new Dialog(thisActivity, R.style.DialogTheme);
        LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressView = mInflater.inflate(R.layout.custom_delivery_place, null);
        Utils.findAllTextView(thisActivity, (ViewGroup) progressView.findViewById(R.id.ll_delivery_places));
        final Spinner sp_delivery_place = (Spinner) progressView.findViewById(R.id.sp_delivery_place);
        final Spinner sp_del_state = (Spinner) progressView.findViewById(R.id.sp_del_state);
        final Spinner sp_del_dirst = (Spinner) progressView.findViewById(R.id.sp_del_dirst);
        final Spinner sp_del_tal = (Spinner) progressView.findViewById(R.id.sp_del_tal);
        final Spinner sp_del_village = (Spinner) progressView.findViewById(R.id.sp_del_village);
        final Spinner sp_del_person_name = (Spinner) progressView.findViewById(R.id.sp_del_person_name);
        Button bt_save_place = (Button) progressView.findViewById(R.id.bt_save_place);


        DatabaseHelper databaseStateHelper = new DatabaseHelper(thisActivity);

        ArrayList<MaritalStatus> facilityArray = databaseStateHelper.getFacilityData();
        StatusAdapter facilityAdapter = new StatusAdapter(thisActivity, facilityArray);
        sp_delivery_place.setAdapter(facilityAdapter);

        final ArrayList<MaritalStatus> stateArray = databaseStateHelper.getStateData();
        StatusAdapter stateAdapter = new StatusAdapter(thisActivity, stateArray);
        sp_del_state.setAdapter(stateAdapter);

        DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
        ArrayList<MaritalStatus> districtArray = databaseHelper.getTaluka("null");
        StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
        sp_del_tal.setAdapter(districtAdapter);

        sp_delivery_place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                LinearLayout linearLayout = (LinearLayout) view;
                TextView textView = (TextView) linearLayout.getChildAt(0);
                optionId = textView.getTag().toString();
                optionName = textView.getText().toString();

               /* if (position != 0) {
                    Toast.makeText(thisActivity, optionId, Toast.LENGTH_SHORT).show();
                }*/

                sp_del_dirst.setSelection(0);
                sp_del_tal.setSelection(0);
                sp_del_state.setSelection(0);

                if (position == 1) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getSubCenterData("null");
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_village.setAdapter(districtAdapter);
                } else if (position == 2) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getPhcData("null");
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_village.setAdapter(districtAdapter);
                } else if (position == 9) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getVillages("null");
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_village.setAdapter(districtAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_del_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            LinearLayout linearLayout;
            TextView textView;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                linearLayout = (LinearLayout) view;
                textView = (TextView) linearLayout.getChildAt(0);
                stateId = textView.getTag().toString();
                stateName = textView.getText().toString();
//                if(position!=0) {
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                ArrayList<MaritalStatus> districtArray = databaseHelper.getDistrictData();
                StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                sp_del_dirst.setAdapter(districtAdapter);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_del_dirst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            LinearLayout linearLayout;
            TextView textView;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    dirstID = textView.getTag().toString();
                    dirstName = textView.getText().toString();

                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getTaluka(dirstID);
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_tal.setAdapter(districtAdapter);
                    sp_del_tal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                linearLayout = (LinearLayout) view;
                                textView = (TextView) linearLayout.getChildAt(0);
                                talukaID = textView.getTag().toString();
                                talukaName = textView.getText().toString();

                                if (sp_delivery_place.getSelectedItemPosition() == 1) {
                                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                                    ArrayList<MaritalStatus> districtArray = databaseHelper.getSubCenterData(talukaID);
                                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                                    sp_del_village.setAdapter(districtAdapter);
                                } else if (sp_delivery_place.getSelectedItemPosition() == 2) {
                                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                                    ArrayList<MaritalStatus> districtArray = databaseHelper.getPhcData(talukaID);
                                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                                    sp_del_village.setAdapter(districtAdapter);
                                } else if (sp_delivery_place.getSelectedItemPosition() == 9) {
                                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                                    ArrayList<MaritalStatus> districtArray = databaseHelper.getVillages(talukaID);
                                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                                    sp_del_village.setAdapter(districtAdapter);
                                }
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_del_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            LinearLayout linearLayout;
            TextView textView;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                linearLayout = (LinearLayout) view;
                textView = (TextView) linearLayout.getChildAt(0);
                selectionId = textView.getTag().toString();
                selectionName = textView.getText().toString();
                if (sp_delivery_place.getSelectedItemPosition() == 1) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getDesignationBySubcenter(talukaID, selectionId);
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_person_name.setAdapter(districtAdapter);
                } else if (sp_delivery_place.getSelectedItemPosition() == 2) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                    ArrayList<MaritalStatus> districtArray = databaseHelper.getDesignation(optionId, selectionId);
                    StatusAdapter districtAdapter = new StatusAdapter(thisActivity, districtArray);
                    sp_del_person_name.setAdapter(districtAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_del_person_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            LinearLayout linearLayout;
            TextView textView;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    linearLayout = (LinearLayout) view;
                    textView = (TextView) linearLayout.getChildAt(0);
                    String[] array = textView.getTag().toString().split(",");
                    employeeId = array[0];
                    designName = array[1];
                    employeeName = textView.getText().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Typeface type = Typeface.createFromAsset(thisActivity.getAssets(), "SHRUTI.TTF");
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(progressView);

        WindowManager wm = (WindowManager) thisActivity.getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
        Display display = wm.getDefaultDisplay(); // getting the screen size of device
        Point size = new Point();
        display.getSize(size);
        int width1 = WindowManager.LayoutParams.WRAP_CONTENT;
        int height1 = WindowManager.LayoutParams.WRAP_CONTENT;

        int tempValue = 0;
        tempValue = ((size.x) * 200) / 1440;
        int width = size.x - tempValue;  // Set your widths
        int height = height1; // set your heights

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(progressDialog.getWindow().getAttributes());

        lp.width = width;
        lp.height = height;
        progressDialog.getWindow().setAttributes(lp);
        progressDialog.setContentView(progressView);
        progressDialog.setCancelable(true);
        progressDialog.show();

        bt_save_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ed_delivery_place.setText(stateName + "," + dirstName + "," + talukaName + "," + selectionName);
                ed_delivery_place_name.setText(optionName);
                ed_delivery_designation.setText(designName);
                ed_delivery_name.setText(employeeName);
                progressDialog.dismiss();
            }
        });

    }

    public void showComplicationDialog(ArrayList<MaritalStatus> treatmentArrayList, EditText ed_high_risk_mom) {

        progressDialog = new Dialog(thisActivity, R.style.DialogTheme);
        LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressView = mInflater.inflate(R.layout.custom_complication, null);
        Utils.findAllTextView(thisActivity, (ViewGroup) progressView.findViewById(R.id.ll_treatment));
        ListView list_complication = (ListView) progressView.findViewById(R.id.list_complication);
        Button bt_save_treatment = (Button) progressView.findViewById(R.id.bt_save_treatment);

        TreatmentAdapter treatmentAdapter = new TreatmentAdapter(thisActivity, treatmentArrayList, ed_high_risk_mom);
        list_complication.setAdapter(treatmentAdapter);

        Typeface type = Typeface.createFromAsset(thisActivity.getAssets(), "SHRUTI.TTF");
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(progressView);

        WindowManager wm = (WindowManager) thisActivity.getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
        Display display = wm.getDefaultDisplay(); // getting the screen size of device
        Point size = new Point();
        display.getSize(size);
        int width1 = WindowManager.LayoutParams.WRAP_CONTENT;
        int height1 = WindowManager.LayoutParams.WRAP_CONTENT;

        int tempValue = 0;
        tempValue = ((size.x) * 200) / 1440;
        int width = size.x - tempValue;  // Set your widths
        int height = height1; // set your heights

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(progressDialog.getWindow().getAttributes());

        lp.width = width;
        lp.height = height;
        progressDialog.getWindow().setAttributes(lp);
        progressDialog.setContentView(progressView);
        progressDialog.setCancelable(true);
        progressDialog.show();

        bt_save_treatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_delivery_type:
                if (position == 4 || position == 5) {
                    sp_children_delivered.setSelection(1);
                    sp_children_delivered.setEnabled(false);
                } else {
                    sp_children_delivered.setEnabled(true);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void clickEvent(View view) {

        switch (view.getId()) {

            case R.id.bt_save:
                DatabaseHelper databaseHelper = new DatabaseHelper(thisActivity);
                DeliveryDetails deliveryDetails = new DeliveryDetails();
                boolean isSaved = false;
                if (ed_delivery_date.getText().toString().length() != 0) {
                    String[] array = ed_delivery_date.getText().toString().split(",");
                    deliveryDetails.setPregancyoutcomeDate(array[0]);
                    deliveryDetails.setPregnancyoutcomeTime(array[1]);
                    isSaved = databaseHelper.setMTPDate(pregnantwomanregdId, array[0]);
                } else {
                    deliveryDetails.setPregancyoutcomeDate(null);
                    deliveryDetails.setPregnancyoutcomeTime(null);
                }

                if (isSaved) {
                    deliveryDetails.setPregnantwomanregdId(pregnantwomanregdId);
                    deliveryDetails.setPregnancyoutcomeregdDate(ed_delivery_regd_date.getText().toString());
                    if (sp_delivery_type.getSelectedItemPosition() != 0) {
                        deliveryDetails.setPregancyoutcometypeId("" + sp_delivery_type.getSelectedItemPosition());
                    } else {
                        deliveryDetails.setPregancyoutcometypeId(null);
                    }
                    deliveryDetails.setEmployeeId(employeeId);
                    deliveryDetails.setFacilityId(optionId);
                    deliveryDetails.setFacilitynameId(selectionId);

                    if (ed_delivery_discharge_date.getText().toString().length() != 0) {
                        String[] arrayDischarge = ed_delivery_discharge_date.getText().toString().split(",");
                        deliveryDetails.setDischargeDate(arrayDischarge[0]);
                        deliveryDetails.setDischargeTime(arrayDischarge[1]);
                    } else {
                        deliveryDetails.setDischargeDate(null);
                        deliveryDetails.setDischargeTime(null);
                    }

                    if (sp_children_delivered.getSelectedItemPosition() != 0) {
                        deliveryDetails.setNumberofChild("" + sp_children_delivered.getSelectedItem());
                    } else {
                        deliveryDetails.setNumberofChild(null);
                    }
                    deliveryDetails.setDeliverymamtaKit(isMamtaKit);
                    deliveryDetails.setDelivery108Service(is108Service);
                    deliveryDetails.setDeliveryIncentive(isTransportService);
                    if (ed_delivery_treatment.getText().toString().length() != 0) {
                        String treatments = getTreatment();
                        deliveryDetails.setPregnancyoutcomeTreatment(treatments.split(","));
                    }

                    SharedPreferences sharedPreferences = thisActivity.getSharedPreferences(Constants.USER_LOGIN_PREF, MODE_PRIVATE);
                    String userDetail = sharedPreferences.getString(Constants.USER_ID, null);
                    try {
                        JSONObject jsonUserObject = new JSONObject(userDetail);
                        deliveryDetails.setUserId(jsonUserObject.getJSONArray("userdetails").getJSONObject(0).getString("userId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String validation = FormValidation.validDeliveryDetails(deliveryDetails, thisActivity);
                    if (validation.length() != 0) {
                        CustomLoaderDialog customLoaderDialog = new CustomLoaderDialog(thisActivity);
                        customLoaderDialog.showValidationDialog(validation);
                    } else {
                        isSaved = databaseHelper.insertPregnancyOutCome(deliveryDetails);
                        if (isSaved) {
                            isSaved = databaseHelper.insertTreatment(deliveryDetails);
                            if (isSaved) {

                                SimpleDateFormat simpleDateFormat =
                                        new SimpleDateFormat("dd/M/yyyy");

                                Calendar c1 = Calendar.getInstance();
                                Calendar c2 = Calendar.getInstance();
                                Calendar c3 = Calendar.getInstance();
                                Calendar c4 = Calendar.getInstance();
                                Calendar c5 = Calendar.getInstance();
                                Calendar c6 = Calendar.getInstance();
                                try {
                                    c1.setTime(simpleDateFormat.parse(deliveryDetails.getPregancyoutcomeDate()));
                                    c2.setTime(simpleDateFormat.parse(deliveryDetails.getPregancyoutcomeDate()));
                                    c3.setTime(simpleDateFormat.parse(deliveryDetails.getPregancyoutcomeDate()));
                                    c4.setTime(simpleDateFormat.parse(deliveryDetails.getPregancyoutcomeDate()));
                                    c5.setTime(simpleDateFormat.parse(deliveryDetails.getPregancyoutcomeDate()));
                                    c6.setTime(simpleDateFormat.parse(deliveryDetails.getPregancyoutcomeDate()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                c1.add(Calendar.DATE, 2);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                                c2.add(Calendar.DATE, 6);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                                c3.add(Calendar.DATE, 13);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                                c4.add(Calendar.DATE, 20);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                                c5.add(Calendar.DATE, 27);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                                c6.add(Calendar.DATE, 41);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE

                                String zeroDays = deliveryDetails.getPregancyoutcomeDate();
                                String threeDays = simpleDateFormat.format(c1.getTime());
                                String sevenDays = simpleDateFormat.format(c2.getTime());
                                String fortyDays = simpleDateFormat.format(c3.getTime());
                                String twentyOnewDays = simpleDateFormat.format(c4.getTime());
                                String twentyEightDays = simpleDateFormat.format(c5.getTime());
                                String fortyEightDays = simpleDateFormat.format(c6.getTime());
                                String[] dateArray = new String[]{zeroDays, threeDays, sevenDays, fortyDays, twentyOnewDays, twentyEightDays, fortyEightDays};
                                deliveryDetails.setPostnatalservicedueDate(dateArray);

                                isSaved = databaseHelper.insertPostnatalService(deliveryDetails);
                                if (isSaved) {

                                    isSaved = databaseHelper.insertNewChild(deliveryDetails);
                                    if (isSaved) {
                                        String str = thisActivity.getResources().getString(R.string.insert);
                                        CustomToast customToast = new CustomToast(thisActivity, str);
                                        customToast.show();
//                                        thisActivity.finish();
                                        Intent intent=new Intent(thisActivity,ChildListActivity.class);
                                        intent.putExtra("pregnantwomanregdId",deliveryDetails.getPregnantwomanregdId());
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                    }


                }
                break;
            case R.id.rdb_transport_service_yes:
                isTransportService = "1";
                rdb_transport_service_yes.setChecked(true);
                rdb_transport_service_no.setChecked(false);
                break;
            case R.id.rdb_transport_service_no:
                isTransportService = "0";
                rdb_transport_service_yes.setChecked(false);
                rdb_transport_service_no.setChecked(true);
                break;
            case R.id.rdb_108_service_yes:
                is108Service = "1";
                rdb_108_service_yes.setChecked(true);
                rdb_108_service_no.setChecked(false);
                break;
            case R.id.rdb_108_service_no:
                is108Service = "0";
                rdb_108_service_yes.setChecked(false);
                rdb_108_service_no.setChecked(true);
                break;
            case R.id.rdb_mamta_kit_yes:
                isMamtaKit = "1";
                rdb_mamta_kit_yes.setChecked(true);
                rdb_mamta_kit_no.setChecked(false);
                break;
            case R.id.rdb_mamta_kit_no:
                isMamtaKit = "0";
                rdb_mamta_kit_yes.setChecked(false);
                rdb_mamta_kit_no.setChecked(true);
                break;
        }
    }

    private String getTreatment() {

        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "";
        for (int i = 0; i < treatmentList.size(); i++) {
            MaritalStatus maritalStatus = treatmentList.get(i);
            if (maritalStatus.getIsChecked() != null && maritalStatus.getIsChecked().equals("1")) {
                stringBuilder.append(maritalStatus.getId());
                prefix = ",";
                stringBuilder.append(prefix);
            }
        }

        if (stringBuilder.toString().length() >= 2) {
            return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
        } else {
            return (stringBuilder.toString());
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
