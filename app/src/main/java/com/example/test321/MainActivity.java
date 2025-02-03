package com.example.test321;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView welcometxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        welcometxt = findViewById(R.id.welcomeText);

        // Get the username from Intent and set it in the welcome text
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            welcometxt.setText("Welcome " + username);
        }

        // Button to open the navigation drawer
        findViewById(R.id.buttonOpenDrawer).setOnClickListener(v -> openDrawer());

        // Set up the Navigation View item click listener
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Get reference to the existing header view
        View headerView = navigationView.getHeaderView(0);
        if (headerView != null) {
            ImageButton btnCloseDrawer = headerView.findViewById(R.id.btnCloseDrawer);
            btnCloseDrawer.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));
        }
    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_address:
                Toast.makeText(this, "Address clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_terms_conditions:
                Toast.makeText(this, "Terms & Conditions clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        // Close the drawer after an item is selected
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
