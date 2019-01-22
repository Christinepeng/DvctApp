package com.divercity.android.features.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by lucas on 26/02/2018.
 */

public class DatePickerDialogFragment extends DialogFragment {

    private DatePickerDialogListener mListener;

    public interface DatePickerDialogListener {
        void onDateSetListener(String year, String month, String day);
    }

    public static DatePickerDialogFragment newInstance() {
        return new DatePickerDialogFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // To know if the dialog is being called from an activity or fragment
            mListener = (DatePickerDialogListener) getParentFragment();
            if (mListener == null)
                mListener = (DatePickerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling context must implement onDateSetListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                String dayString = String.valueOf(d);
                if (dayString.length() == 1)
                    dayString = "0" + dayString;

                int month = m + 1;
                String monthString = String.valueOf(month);
                if (monthString.length() == 1)
                    monthString = "0" + monthString;

                mListener.onDateSetListener(Integer.toString(y), monthString, dayString);
            }
        }, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return datePickerDialog;
    }

}
