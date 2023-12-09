package com.example.fastcar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.fastcar.Dialog.CustomDialogNotify;

import java.util.Calendar;

public class CustomTimePickerDialog extends DialogFragment {
    private DatePickerDialog.OnDateSetListener listener;
    String[] time_values = {"00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30",
            "05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
            "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30"};
    NumberPicker timepicker1, timepicker2;
    private int time1, time2, index;

    public void setListener(DatePickerDialog.OnDateSetListener listener, int time1, int time2, int index) {
        this.listener = listener;
        this.time1 = time1;
        this.time2 = time2;
        this.index = index;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.time_picker_dialog, null);
        builder.setTitle(null);
        builder.setView(dialog);

        timepicker1 = (NumberPicker) dialog.findViewById(R.id.time_picker1);
        timepicker2 = (NumberPicker) dialog.findViewById(R.id.time_picker2);
        TextView tv1 = dialog.findViewById(R.id.tv_timepicker1);
        TextView tv2 = dialog.findViewById(R.id.tv_timepicker2);
        TextView btnCancel = dialog.findViewById(R.id.btnCancel_TimePicker);
        TextView btnConfirm = dialog.findViewById(R.id.btnConfirm_TimePicker);

        if (index == 0) {
            tv1.setText("Nhận xe");
            tv2.setText("Trả xe");
        } else {
            tv1.setText("Bắt đầu");
            tv2.setText("Kết thúc");
        }

        timepicker1.setMinValue(0);
        timepicker1.setMaxValue(time_values.length - 1);
        timepicker1.setValue(time1);
        timepicker1.setDisplayedValues(time_values);

        timepicker2.setMinValue(0);
        timepicker2.setMaxValue(time_values.length - 1);
        timepicker2.setValue(time2);
        timepicker2.setDisplayedValues(time_values);

        btnCancel.setOnClickListener(v -> CustomTimePickerDialog.this.dismissAllowingStateLoss());

        btnConfirm.setOnClickListener(v -> {
            listener.onDateSet(null, timepicker1.getValue(), timepicker2.getValue(), 0);
            CustomTimePickerDialog.this.dismissAllowingStateLoss();
        });

        return builder.create();
    }

    public String getSelectedTime() {
        if (timepicker1 != null && timepicker2 != null) {
            int index1 = timepicker1.getValue();
            int index2 = timepicker2.getValue();

            // Kiểm tra xem index có nằm trong khoảng hợp lệ không
            if (index1 >= 0 && index1 < time_values.length && index2 >= 0 && index2 < time_values.length) {
                String selectedTime1 = time_values[index1];
                String selectedTime2 = time_values[index2];
                return selectedTime1 + " - " + selectedTime2;
            } else {
                return "Invalid selection";
            }
        } else {
            return "NumberPicker is not initialized";
        }
    }
}
