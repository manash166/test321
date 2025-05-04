package com.example.test321;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class signup_activity extends AppCompatActivity {

    private TextInputEditText editTextName, editTextPhone;
    private Button buttonSignup,getOtpButton;
    private TextView textViewGoToLogin;
    private  String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); // Make sure the name matches your XML file

        LinearLayout otpLayout = findViewById(R.id.otpLayout);

        // Bind views
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonSignup = findViewById(R.id.buttonSignup);
        getOtpButton =findViewById(R.id.getOtpButton);
        textViewGoToLogin = findViewById(R.id.textViewGoToLogin);



        TextInputLayout phoneInputLayout = findViewById(R.id.edit_phone);
        TextInputEditText editTextPhone = findViewById(R.id.editTextPhone);
        Button getOtpButton = findViewById(R.id.getOtpButton);
        getOtpButton.setVisibility(GONE);

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && s.length()<=9||s.equals(null)){
                    phoneInputLayout.setStartIconDrawable(R.drawable.call_20px);
                    phoneInputLayout.setStartIconVisible(true);
                }

                if (!TextUtils.isEmpty(s) && s.length() == 10) {
                    // Valid phone number
                    phoneInputLayout.setStartIconDrawable(null);
                    phoneInputLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                    getOtpButton.setVisibility(View.VISIBLE); // Show Get OTP button
                } else {
                    // Invalid phone number
                    phoneInputLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT); // Show cancel icon
                    getOtpButton.setVisibility(View.GONE); // Hide Get OTP button
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

  getOtpButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          // Show OTP input fields
          otpLayout.setVisibility(View.VISIBLE);
          String phone=editTextPhone.getText().toString().trim();
          if (phone.isEmpty() || phone.length() != 10){
              editTextPhone.setError("Enter Valid Phone Number");
              return;
          }
      }
  });


        // Sign Up button logic
        buttonSignup.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();


            if (TextUtils.isEmpty(name) || name.length() < 4) {
                editTextName.setError("Name must be at least 4 characters");
                return;
            }


            if (TextUtils.isEmpty(phone) || phone.length() != 10) {
                editTextPhone.setError("Enter a valid 10-digit phone number");
                return;
            }






            // All validations passed - proceed with signup logic (e.g., Firebase or API call)


            Toast.makeText(signup_activity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();


            // You can navigate to another screen after signup (e.g., login or home)
            // startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            // finish();
        });

        // Navigate to Login screen
        textViewGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(signup_activity.this, LoginActivity.class);
            startActivity(intent);
        });

        EditText[] otpFields = {
                findViewById(R.id.otpDigit1),
                findViewById(R.id.otpDigit2),
                findViewById(R.id.otpDigit3),
                findViewById(R.id.otpDigit4),
                findViewById(R.id.otpDigit5),
                findViewById(R.id.otpDigit6)
        };

        OtpInputHelper.setupOtpFields(otpFields);


    }
}
