package com.mcts.app.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;

    public DatePickerFragment() {
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    private int year, month, day;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        pickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        return pickerDialog;
    }
}  