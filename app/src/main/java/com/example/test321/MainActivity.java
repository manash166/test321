package com.example.test321;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView welcometxt,goback;
    private ImageButton btnMen, btnWomen, btnChildren;
    private ConstraintLayout layout;
    private RecyclerView recyclerView_men;
    private List<MenService> menServices;
    private MenServiceAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        welcometxt = findViewById(R.id.welcomeText);
        btnMen = findViewById(R.id.btnMen);
        btnWomen = findViewById(R.id.btnWomen);
        btnChildren = findViewById(R.id.btnChildren);
        layout = findViewById(R.id.relativeLayout);
        goback=findViewById(R.id.goback_textview);
        recyclerView_men=findViewById(R.id.recyclerview_men);
        // Initialize Men Services List
        menServices = new ArrayList<>();
        menServices.add(new MenService("Normal Hair Cut", "₹200"));
        menServices.add(new MenService("Shaving", "₹150"));
        menServices.add(new MenService("Hair Color", "₹300"));
        menServices.add(new MenService("Facial", "₹350"));
        menServices.add(new MenService("Massage", "₹400"));

        adapter = new MenServiceAdapter(this, menServices);
        recyclerView_men.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_men.setAdapter(adapter);
        recyclerView_men.setVisibility(View.GONE); // Hide initially


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



        // Set click listeners for ImageButtons
        btnMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(btnMen);
            }

        });

        btnWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(btnWomen);
            }
        });

        btnChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(btnChildren);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // Inflate Custom Layout
                View customView = getLayoutInflater().inflate(R.layout.dialog_custom, null);
                builder.setView(customView);

                // Get references to buttons
                Button btnYes = customView.findViewById(R.id.btn_yes);
                Button btnNo = customView.findViewById(R.id.btn_no);

                AlertDialog dialog = builder.create();
                dialog.show();

                // Set Transparent Background (Optional)
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                // Handle Button Clicks
                btnYes.setOnClickListener(v -> {
                    // Clear all activities from the back stack and restart MainActivity
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();  // This will close the current instance of MainActivity and remove it from the back stack
                });

                btnNo.setOnClickListener(v -> dialog.dismiss());
            }
        });

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
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectButton(ImageButton selectedButton) {
        // Hide all buttons first
        btnMen.setVisibility(View.GONE);
        btnWomen.setVisibility(View.GONE);
        btnChildren.setVisibility(View.GONE);

        // Show only the selected button
        selectedButton.setVisibility(View.VISIBLE);

        // Animate the selected button
        animateButton(selectedButton);

        // Show a toast for selection
        if (selectedButton == btnMen) {
            Toast.makeText(this, "Men Category Selected", Toast.LENGTH_SHORT).show();
       goback.setVisibility(View.VISIBLE);
            recyclerView_men.setVisibility(View.VISIBLE);
        } else if (selectedButton == btnWomen) {
            Toast.makeText(this, "Women Category Selected", Toast.LENGTH_SHORT).show();
            goback.setVisibility(View.VISIBLE);
        } else if (selectedButton == btnChildren) {
            Toast.makeText(this, "Children Category Selected", Toast.LENGTH_SHORT).show();
            goback.setVisibility(View.VISIBLE);
        }

    }

    private void animateButton(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.1f,  // Scale from 100% to 110% in X-axis
                1.0f, 1.1f,  // Scale from 100% to 110% in Y-axis
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);
        view.startAnimation(scaleAnimation);
    }
}
