package com.example.test321;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView welcometxt,goback,mainactivity_default_address;
    private ImageButton btnMen, btnWomen, btnChildren;
    private String username,phonenumber;

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
        mainactivity_default_address=findViewById(R.id.textView6);
        goback=findViewById(R.id.goback_textview);
        LinearLayout bottom_part=findViewById(R.id.bottom_part);
        recyclerView_men=findViewById(R.id.recyclerview_men);
        TextView totalAmountTextView = findViewById(R.id.textView4);

        bottom_part.setVisibility(View.GONE);
        LinearLayout flagship=findViewById(R.id.flagship_part);

        username=getIntent().getStringExtra("username");
        phonenumber=getIntent().getStringExtra("phoneNumber");

        Log.d("MainActivity", "Phone: " + phonenumber + ", Username: " + username);

        // Example usage: show username in a Toast
        Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
        Log.d("phone number in MainActivity", "phone number in MainActivity"+phonenumber);
        if (phonenumber == null || phonenumber.isEmpty()) {
            Toast.makeText(this, "Phone number missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(phonenumber);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get name
                    String name = snapshot.child("name").getValue(String.class);
                    welcometxt.setText("Welcome " + name);
                    String username = name; // if needed later

                    // Get default address
                    String defaultAddress = snapshot.child("default_address").getValue(String.class);
                    mainactivity_default_address.setText(defaultAddress);

                    // Example: Get all addresses
                    for (DataSnapshot addressSnap : snapshot.child("addresses").getChildren()) {
                        String key = addressSnap.getKey();
                        String value = addressSnap.getValue(String.class);
                        Log.d("Address", key + ": " + value);
                    }

                } else {
                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Firebase error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



//           Schedule date and time
        TextView textLater = findViewById(R.id.text_later);

        MaterialSwitch switchSchedule = findViewById(R.id.switch_schedule);
        switchSchedule.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ScheduleBottomSheet.show(MainActivity.this, textLater);
            }
        });

        //          For Animating the location in Address icon ,Calendar Icon
        ImageView animatedIcon = findViewById(R.id.animated_icon);
        ImageView animatedCalendar=findViewById(R.id.icon_service);

        // Load GIF with Glide and crossfade animation
        Glide.with(this)
                .asGif() // Ensures animation for GIFs
                .load(R.drawable.test) // Make sure test.gif is in res/drawable
                .transition(DrawableTransitionOptions.withCrossFade(500)) // 500ms crossfade
                .into(animatedIcon);

        // Load GIF with Glide and schedule icon animation
        Glide.with(this)
                .asGif() // Ensures animation for GIFs
                .load(R.drawable.calendar) // Make sure test.gif is in res/drawable
                .transition(DrawableTransitionOptions.withCrossFade(500)) // 500ms crossfade
                .into(animatedCalendar);

        loadDefaultAddress();
        // Change Address Button (TextView)
        MaterialButton btn_change_address = findViewById(R.id.change_address_button);
         btn_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "Opening AddressPopupActivity");
                Toast.makeText(MainActivity.this, "Opening Address Popup", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AddressPopupActivity.class);
                intent.putExtra("username", username); // Ensure 'username' is initialized
                startActivityForResult(intent, 1); // Correct method to receive result

            }
        });

        // Handle Continue Button Click
        TextView continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(v -> openOrderReview());

        DatabaseReference menServicesRef = FirebaseDatabase.getInstance()
                .getReference("services&prices").child("Men");
        // Initialize Men Services List
        menServices = new ArrayList<>();
        // Initialize the adapter and pass totalAmountTextView to track price updates

        adapter = new MenServiceAdapter(this, menServices, totalAmountTextView);
        recyclerView_men.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_men.setAdapter(adapter);
        recyclerView_men.setVisibility(View.GONE); // Hide initially
        fetchMenServices(menServicesRef);
        // Get the username from Intent and set it in the welcome text
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
                flagship.setVisibility(View.INVISIBLE);
                selectButton(btnMen);

            }

        });

        btnWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagship.setVisibility(View.INVISIBLE);
                selectButton(btnWomen);
            }
        });

        btnChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagship.setVisibility(View.INVISIBLE);
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

                if (btnYes == null || btnNo == null) {
                    Log.e("MainActivity", "btnYes or btnNo is null. Check dialog_custom.xml.");
                    return;
                }

                AlertDialog dialog = builder.create();
                dialog.show();

                // Handle Button Clicks
                btnYes.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.putExtra("username", username); // ✅ Pass the username
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dialog.dismiss();
                });


                btnNo.setOnClickListener(v -> dialog.dismiss());
            }
        });

        username=getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Error: Username is missing.", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Username is null. Check intent passing.");
            finish();  // ✅ Stop the app to prevent Firebase crash
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String updatedAddress = data.getStringExtra("selected_address");
            if (updatedAddress != null) {
                TextView addressTextView = findViewById(R.id.textView6);
                addressTextView.setText(updatedAddress); // Update the default address
                Log.d("MainActivity", "Updated address: " + updatedAddress);
            }
        }
    }

    // Method to load the default address from Firebase
    private void loadDefaultAddress() {
        DatabaseReference defaultAddressRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(phonenumber)  // Using username instead of userId
                .child("default_address");
   defaultAddressRef.addListenerForSingleValueEvent(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull DataSnapshot snapshot) {
           if (snapshot.exists()) {
               String defaultAddress = snapshot.getValue(String.class);
               mainactivity_default_address.setText(defaultAddress);
           } else {
               Toast.makeText(MainActivity.this, "No address found", Toast.LENGTH_SHORT).show();
           }
       }
       @Override
       public void onCancelled(@NonNull DatabaseError error) {

       }
   });

    }


    private void openOrderReview() {
        List<MenService> selectedServices = adapter.getSelectedServices();
        TextView addressTextView = findViewById(R.id.textView6);
        String address = addressTextView.getText().toString().trim();

        // Check if the address is empty or equals "No Address Found"
        if (address.isEmpty() || address.equalsIgnoreCase("No Address Found")) {
            Toast.makeText(this, "Please Add Your Address First", Toast.LENGTH_SHORT).show();
            return; // Stop further execution if no address
        }

        if (selectedServices.isEmpty()) {
            Toast.makeText(this, "No services selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate total amount and prepare service details
        int totalAmount = 0;
        StringBuilder serviceDetails = new StringBuilder();
        for (int i = 0; i < selectedServices.size(); i++) {
            MenService service = selectedServices.get(i);
            serviceDetails.append(service.getServiceName()).append(":").append(service.getPrice());

            // Extract and add price to total amount
            totalAmount += extractPrice(service.getPrice());



            // Add newline if it's not the last item
            if (i < selectedServices.size() - 1) {
                serviceDetails.append("\n");
            }
        }


        // Generate a unique order ID
        String orderId = "ORD-" + (System.currentTimeMillis() % 100000);

        // Send data to order_review activity
        Intent intent = new Intent(this, order_review.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("serviceDetails", serviceDetails.toString());
        intent.putExtra("totalAmount", totalAmount);
        intent.putExtra("address",address);
        intent.putExtra("userid",username);
        startActivity(intent);
        Log.d("checktotalamount", "checktotalamount: "+totalAmount);
    }

    // Helper method to extract numeric price from a string (e.g., "₹200" -> 200)
    private int extractPrice(String price) {

        return Integer.parseInt(price.replaceAll("[^0-9]", ""));
    }

    private void fetchMenServices(DatabaseReference menServicesRef) {
        menServicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menServices.clear();  // Clear previous data

                for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                    String serviceName = serviceSnapshot.getKey(); // Service Name
                    String price = "₹" + serviceSnapshot.getValue(String.class); // Price

                    menServices.add(new MenService(serviceName, price));
                }

                adapter.notifyDataSetChanged();  // Update RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_address) {
            // Open AddressPopupActivity
            Intent intent = new Intent(this, AddressPopupActivity.class);
            intent.putExtra("username", username);
            startActivityForResult(intent, 1); // Use the same request code to capture updates
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else if (id == R.id.nav_terms_conditions) {
            Toast.makeText(this, "Terms & Conditions clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectButton(ImageButton selectedButton) {
        LinearLayout bottom_part = findViewById(R.id.bottom_part);
        bottom_part.setVisibility(View.VISIBLE);
        LinearLayout schedule_part= findViewById(R.id.schedule_layer);
        schedule_part.setVisibility(View.VISIBLE);

        // Hide all buttons
        btnMen.setVisibility(View.GONE);
        btnWomen.setVisibility(View.GONE);
        btnChildren.setVisibility(View.GONE);

        // Show the selected button
        selectedButton.setVisibility(View.VISIBLE);

        // Center by adjusting margins
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) selectedButton.getLayoutParams();
        params.setMargins(320, 0, 0, 30);  // Adjust left margin
        selectedButton.setLayoutParams(params);

        // Show and center the goback text
        goback.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams gobackParams = (LinearLayout.LayoutParams) goback.getLayoutParams();
        gobackParams.setMargins(320, 0, 0, 30); // Same margin for alignment
        goback.setLayoutParams(gobackParams);

        animateButton(selectedButton);

        if (selectedButton == btnMen) {
            Toast.makeText(this, "Men Category Selected", Toast.LENGTH_SHORT).show();
            recyclerView_men.setVisibility(View.VISIBLE);
        } else if (selectedButton == btnWomen) {
            Toast.makeText(this, "Women Category Selected", Toast.LENGTH_SHORT).show();
        } else if (selectedButton == btnChildren) {
            Toast.makeText(this, "Children Category Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void animateButton(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.2f,  // Scale from 100% to 120%
                1.0f, 1.2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);  // Faster animation (300ms)
        scaleAnimation.setFillAfter(true); // Keep final scale
        view.startAnimation(scaleAnimation);
    }
}
