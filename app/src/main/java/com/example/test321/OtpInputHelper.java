package com.example.test321;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class OtpInputHelper {

    public static void setupOtpFields(EditText[] otpFields) {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;

            otpFields[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus();
                    }
                }
            });

            otpFields[index].setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_DEL &&
                        otpFields[index].getText().toString().isEmpty() &&
                        index > 0) {
                    otpFields[index - 1].setText("");
                    otpFields[index - 1].requestFocus();
                    return true;
                }
                return false;
            });
        }
    }
}

