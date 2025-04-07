package com.example.test321;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class ScheduleBottomSheet {

    public static void show(Context context, TextView textLater) {
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_schedule_picker, null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);

        DatePicker datePicker = view.findViewById(R.id.date_picker);
        TimePicker timePicker = view.findViewById(R.id.time_picker);
        MaterialButton continueBtn = view.findViewById(R.id.btn_continue);
        ImageButton closeBtn = view.findViewById(R.id.btn_close);

        continueBtn.setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            String result = String.format(Locale.getDefault(), "%02d/%02d at %02d:%02d", day, month, hour, minute);

            // Update TextView in current activity
            if (textLater != null) {
                textLater.setText(result);
                dialog.dismiss();
            }



        });

        closeBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}