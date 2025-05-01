package com.example.test321;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup_activity extends AppCompatActivity {

    private EditText editTextPhone, password1, password2;
    private TextView goToLogin;
    private Button signupButton;

    DatabaseReference signupRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextPhone = findViewById(R.id.editTextPhone);
        password1 = findViewById(R.id.create_your_password1);
        password2 = findViewById(R.id.create_your_password2);
        goToLogin = findViewById(R.id.textViewGoToLogin);
        signupButton = findViewById(R.id.buttonSignup);

        // Firebase reference under "All/signup_user"
        signupRef = FirebaseDatabase.getInstance().getReference().child("All").child("signup_user");

        // Navigate to Login Activity
        goToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(signup_activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        signupButton.setOnClickListener(v -> {
            String phone = editTextPhone.getText().toString().trim();
            String pass1 = password1.getText().toString().trim();
            String pass2 = password2.getText().toString().trim();

            // Validate input
            if (TextUtils.isEmpty(phone)) {
                editTextPhone.setError("Phone number required");
                return;
            }
            if (TextUtils.isEmpty(pass1) || TextUtils.isEmpty(pass2)) {
                Toast.makeText(signup_activity.this, "Both password fields required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass1.equals(pass2)) {
                Toast.makeText(signup_activity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save to Firebase
            DatabaseReference userRef = signupRef.child(phone);
            userRef.child("password").setValue(pass1)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(signup_activity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                        editTextPhone.setText("");
                        password1.setText("");
                        password2.setText("");
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(signup_activity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
