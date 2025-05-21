package com.example.test321;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboard extends AppCompatActivity {
     Button logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        logoutBtn = findViewById(R.id.button_logout);
        logoutBtn.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear(); // or remove("isLoggedIn")
            editor.apply();
            Intent intent = new Intent(AdminDashboard.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        Button masterBtn = findViewById(R.id.button_master);
        masterBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, MasterActivity.class);
            startActivity(intent);
        });

    }
}