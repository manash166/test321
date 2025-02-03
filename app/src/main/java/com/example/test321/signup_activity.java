package com.example.test321;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup_activity extends AppCompatActivity {
    EditText etName, etPassword;
    Button btnSignup;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.editTextSignupName);
        etPassword = findViewById(R.id.editTextSignupPassword);
        btnSignup = findViewById(R.id.buttonSignup);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(signup_activity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save user to Firebase
                databaseReference.child(name).child("password").setValue(password);
                Toast.makeText(signup_activity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();

                // Move to Login
                startActivity(new Intent(signup_activity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
