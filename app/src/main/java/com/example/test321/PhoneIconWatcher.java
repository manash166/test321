package com.example.test321;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PhoneIconWatcher implements TextWatcher {

    private final TextInputLayout inputLayout;
    private final int iconResId;

    public PhoneIconWatcher(TextInputLayout inputLayout, int iconResId) {
        this.inputLayout = inputLayout;
        this.iconResId = iconResId;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Not needed
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Not needed
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 5) {
            inputLayout.setStartIconDrawable(null); // Hide icon
        } else {
            inputLayout.setStartIconDrawable(iconResId); // Show icon
        }
    }
}
