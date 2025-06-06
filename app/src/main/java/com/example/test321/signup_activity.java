package com.example.test321;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class signup_activity extends AppCompatActivity {

    private TextInputEditText editTextName, editTextPhone;
    private Button getOtpButton, buttonSignup;
    private LinearLayout otpLayout;
    private EditText otpDigit1, otpDigit2, otpDigit3, otpDigit4, otpDigit5, otpDigit6;
    TextView loginbutton_signup;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        getOtpButton = findViewById(R.id.getOtpButton);
        buttonSignup = findViewById(R.id.buttonSignup);
        otpLayout = findViewById(R.id.otpLayout);
        otpDigit1 = findViewById(R.id.otpDigit1);
        otpDigit2 = findViewById(R.id.otpDigit2);
        otpDigit3 = findViewById(R.id.otpDigit3);
        otpDigit4 = findViewById(R.id.otpDigit4);
        otpDigit5 = findViewById(R.id.otpDigit5);
        otpDigit6 = findViewById(R.id.otpDigit6);
        loginbutton_signup=findViewById(R.id.loginbutton_signup);
        TextInputLayout phoneInputLayout = findViewById(R.id.edit_phone);

       loginbutton_signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent =new Intent(signup_activity.this, LoginActivity.class);
               startActivity(intent);
            }
       });
        editTextPhone.addTextChangedListener(new PhoneIconWatcher(phoneInputLayout, R.drawable.call_20px));

        getOtpButton.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                editTextName.setError("Name required");
                return;
            }

            if (TextUtils.isEmpty(phone) || phone.length() != 10) {
                editTextPhone.setError("Valid 10-digit phone required");
                return;
            }

            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(phone);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        // User already exists
                        Toast.makeText(this, "User already has an account. Go to Login.", Toast.LENGTH_LONG).show();
                    } else {
                        // User doesn't exist, proceed with OTP
                        String fullPhoneNumber = "+91" + phone;
                        sendOtp(fullPhoneNumber);
                    }
                } else {
                    Toast.makeText(this, "Failed to check user. Try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        buttonSignup.setOnClickListener(v -> {
            String code = getOtpFromInputs();
            if (code.length() != 6) {
                Toast.makeText(this, "Enter 6-digit OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            if (verificationId != null) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                signInWithPhoneAuthCredential(credential);
            } else {
                Toast.makeText(this, "Verification ID is null. Try again.", Toast.LENGTH_SHORT).show();
            }
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

    private void sendOtp(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                // Auto verification (optional)
                                String code = credential.getSmsCode();
                                if (code != null) {
                                    fillOtpFields(code);
                                    signInWithPhoneAuthCredential(credential);
                                }
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(signup_activity.this, "OTP Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                signup_activity.this.verificationId = verificationId;
                                otpLayout.setVisibility(View.VISIBLE);
                                Toast.makeText(signup_activity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Verification Successful!", Toast.LENGTH_SHORT).show();
                        String name = editTextName.getText().toString().trim();
                        String phoneNumber = editTextPhone.getText().toString().trim();
                        SaveUserDataToFirebase.saveUserDataToFirebase(name, phoneNumber);
                        // Navigate to next activity or save user data here
                        if (!name.isEmpty() && phoneNumber.length() == 10) {
                                                       // Save locally if needed using SharedPreferences (optional)
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", name);
                            editor.putString("phone", phoneNumber);
                            editor.apply();

                            // Go to MainActivity
                            Intent intent = new Intent(signup_activity.this, MainActivity.class);
                            intent.putExtra("username", name);
                            intent.putExtra("phone", phoneNumber);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        Toast.makeText(this, "Verification Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getOtpFromInputs() {
        return otpDigit1.getText().toString().trim()
                + otpDigit2.getText().toString().trim()
                + otpDigit3.getText().toString().trim()
                + otpDigit4.getText().toString().trim()
                + otpDigit5.getText().toString().trim()
                + otpDigit6.getText().toString().trim();
    }

    private void fillOtpFields(String code) {
        if (code.length() == 6) {
            otpDigit1.setText(String.valueOf(code.charAt(0)));
            otpDigit2.setText(String.valueOf(code.charAt(1)));
            otpDigit3.setText(String.valueOf(code.charAt(2)));
            otpDigit4.setText(String.valueOf(code.charAt(3)));
            otpDigit5.setText(String.valueOf(code.charAt(4)));
            otpDigit6.setText(String.valueOf(code.charAt(5)));
        }
    }



}
