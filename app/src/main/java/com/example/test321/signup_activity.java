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
    EditText etName, phonenumber ,etPassword_1,etPassword_2;
    Button btnSignup;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.editTextSignupName);
        etPassword_1 = findViewById(R.id.create_your_password1);
        etPassword_2=findViewById(R.id.create_your_password2);
//        phonenumber=findViewById(R.id.phonenumber);
        btnSignup = findViewById(R.id.buttonSignup);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String password1 = etPassword_1.getText().toString().trim();
                String password2 = etPassword_2.getText().toString().trim();

                if (name.isEmpty() || password1.isEmpty() ||password2.isEmpty()) {
                    Toast.makeText(signup_activity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });
    }
}
