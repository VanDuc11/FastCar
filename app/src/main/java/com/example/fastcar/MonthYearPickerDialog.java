package com.example.fastcar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.fastcar.Dialog.CustomDialogNotify;

import java.util.Calendar;

public class MonthYearPickerDialog extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private int month;
    private int year;

    public void setListener(DatePickerDialog.OnDateSetListener listener, int month, int year) {
        this.listener = listener;
        this.month = month;
        this.year = year;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
        builder.setTitle(null);
        builder.setView(dialog);

        Calendar calendar = Calendar.getInstance();
        final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);
        TextView btnCancel = dialog.findViewById(R.id.btnCancel_DatePicker);
        TextView btnConfirm = dialog.findViewById(R.id.btnConfirm_DatePicker);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(month + 1);

        yearPicker.setMinValue(calendar.get(Calendar.YEAR) - 2);
        yearPicker.setMaxValue(calendar.get(Calendar.YEAR));
        yearPicker.setValue(year);

        btnCancel.setOnClickListener(v -> MonthYearPickerDialog.this.getDialog().cancel());

        btnConfirm.setOnClickListener(v -> {
            listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
            MonthYearPickerDialog.this.getDialog().cancel();
        });

        return builder.create();
    }
}