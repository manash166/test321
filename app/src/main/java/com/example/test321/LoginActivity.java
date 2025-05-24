package com.example.test321;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText phone_input;
    Button btnLogin, login_otp_btn;
    DatabaseReference databaseReference;
    TextView admin,login_signup_button;
    private LinearLayout otpLayout_login;
    private EditText otpDigit1_login, otpDigit2_login, otpDigit3_login, otpDigit4_login, otpDigit5_login, otpDigit6_login;
    private FirebaseAuth mAuth;
    private String verificationId;
    String phoneNumber_final,username;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Reading from SharedPreferences
            String phone = sharedPreferences.getString("phoneNumber", "Not Found");
            Log.d("LoginActivity", "check_through_sharepreferences_phone is"+phone);
            String username = sharedPreferences.getString("username", "Not Found");
            Log.d("LoginActivity", "check_through_sharepreferences_username is"+username);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("phoneNumber", phoneNumber_final);
            intent.putExtra("username",username);

            startActivity(intent);
            finish();
            return;
        }


        setContentView(R.layout.login_activity);

        btnLogin = findViewById(R.id.buttonLogin);
        admin = findViewById(R.id.adminscreenbtn);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        phone_input = findViewById(R.id.phone_input);
        mAuth = FirebaseAuth.getInstance();
        otpLayout_login = findViewById(R.id.otpLayout_login);
        login_otp_btn = findViewById(R.id.get_login_OtpButton);
        otpDigit1_login = findViewById(R.id.otpDigit1_login);
        otpDigit2_login = findViewById(R.id.otpDigit2_login);
        otpDigit3_login = findViewById(R.id.otpDigit3_login);
        otpDigit4_login = findViewById(R.id.otpDigit4_login);
        otpDigit5_login = findViewById(R.id.otpDigit5_login);
        otpDigit6_login = findViewById(R.id.otpDigit6_login);
        login_signup_button=findViewById(R.id.login_signup_button);



        login_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phone_input.getText().toString().trim();

                if (TextUtils.isEmpty(phone) || phone.length() != 10) {
                    phone_input.setError("Valid 10-digit phone required");
                    return;
                }

                DatabaseReference userRef_login = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(phone);

                userRef_login.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            // ✅ Phone number exists → Send OTP
                            String fullPhoneNumber = "+91" + phone;
                            sendOtp(fullPhoneNumber);
                        } else {
                            // ❌ Phone number not registered
                            Toast.makeText(LoginActivity.this, "User not registered.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to check user. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = getOtpFromInputs();

                if (code.length() == 6 && verificationId != null) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                    signInWithPhoneAuthCredential(credential);
                    // 🔐 Verify OTP
                } else {
                    Toast.makeText(LoginActivity.this, "Enter valid 6-digit OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setup OTP input fields
        EditText[] otpFields_login = {
                findViewById(R.id.otpDigit1_login),
                findViewById(R.id.otpDigit2_login),
                findViewById(R.id.otpDigit3_login),
                findViewById(R.id.otpDigit4_login),
                findViewById(R.id.otpDigit5_login),
                findViewById(R.id.otpDigit6_login)
        };

        OtpInputHelper.setupOtpFields(otpFields_login);

        // Initially hide the end drawable (cancel icon)
        phone_input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.call_20px, 0, 0, 0);

        // TextWatcher for phone_input
        phone_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    phone_input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.call_20px, 0, R.drawable.cancel_24, 0);
                } else {
                    phone_input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.call_20px, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        login_signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,signup_activity.class);
                startActivity(intent);

            }
        });



        // Apply fade-in animation to input fields
        fadeInAnimation(phone_input);

        // Handle admin screen navigation
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Admin.class);
                startActivity(intent);
            }
        });
    }

    private String getOtpFromInputs() {
        return otpDigit1_login.getText().toString().trim()
                + otpDigit2_login.getText().toString().trim()
                + otpDigit3_login.getText().toString().trim()
                + otpDigit4_login.getText().toString().trim()
                + otpDigit5_login.getText().toString().trim()
                + otpDigit6_login.getText().toString().trim();
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
                                Toast.makeText(LoginActivity.this, "OTP Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                LoginActivity.this.verificationId = verificationId;
                                otpLayout_login.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
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
                        String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                        String phoneNumber_final = phoneNumber.substring(3); // Extract phone number after +91
                        if (phoneNumber_final.length() == 10) {
                            //  login info to Firebase

                            LoginUserDataToFirebase.loginUserDataToFirebase(this, phoneNumber_final);

                        }

                    } else {
                        Toast.makeText(this, "Verification Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fillOtpFields(String code) {
        if (code.length() == 6) {
            otpDigit1_login.setText(String.valueOf(code.charAt(0)));
            otpDigit2_login.setText(String.valueOf(code.charAt(1)));
            otpDigit3_login.setText(String.valueOf(code.charAt(2)));
            otpDigit4_login.setText(String.valueOf(code.charAt(3)));
            otpDigit5_login.setText(String.valueOf(code.charAt(4)));
            otpDigit6_login.setText(String.valueOf(code.charAt(5)));
        }
    }

    // Button click animation
    private void buttonClickAnimation(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.1f, // Scale from 100% to 110%
                1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);
        view.startAnimation(scaleAnimation);
    }

    // Fade-in animation for EditText fields
    private void fadeInAnimation(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
        view.startAnimation(alphaAnimation);
    }
}
