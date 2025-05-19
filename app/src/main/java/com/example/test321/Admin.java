package com.example.test321;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin extends AppCompatActivity {

    EditText adminUsernameEditText, adminPasswordEditText;
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adminUsernameEditText = findViewById(R.id.admin_username);
        adminPasswordEditText = findViewById(R.id.admin_password);
        continueButton = findViewById(R.id.admin_continue_button);

        continueButton.setOnClickListener(view -> {
            String enteredUsername = adminUsernameEditText.getText().toString().trim();
            String enteredPassword = adminPasswordEditText.getText().toString().trim();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(Admin.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference adminRef = FirebaseDatabase.getInstance()
                    .getReference("Users").child("Admin");

            adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String correctUsername = snapshot.child("username").getValue(String.class);
                        String correctPassword = snapshot.child("password").getValue(String.class);

                        if (enteredUsername.equals(correctUsername) && enteredPassword.equals(correctPassword)) {
                            Toast.makeText(Admin.this, "Login successful", Toast.LENGTH_SHORT).show();


                              // Navigate to AdminDashboard activity
                            Intent intent = new Intent(Admin.this, AdminDashboard.class);
                            startActivity(intent);
                            // You can start a new activity for admin here
                            // startActivity(new Intent(Admin.this, AdminDashboard.class));
                            // finish();

                        } else {
                            Toast.makeText(Admin.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Admin.this, "Admin credentials not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Admin.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
