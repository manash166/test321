package com.example.test321;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddressPopupActivity extends AppCompatActivity implements AddressAdapter.OnAddressSelectedListener {

    private ImageView closeButton;
    private RecyclerView addressRecyclerView;
    private AddressAdapter addressAdapter;
    private ArrayList<String> addressList;
    private ArrayList<String> keyList;
    private String username;
    private TextView addAddressButton;
    private TextView defaultAddress;
    private DatabaseReference userAddressRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_popup);

        closeButton = findViewById(R.id.close_button);
        addAddressButton = findViewById(R.id.add_address_button);
        addressRecyclerView = findViewById(R.id.address_recycler_view);
        defaultAddress = findViewById(R.id.default_address);

        // Initialize address lists
        addressList = new ArrayList<>();
        keyList = new ArrayList<>();

       //


        // Get username from Intent
        username = getIntent().getStringExtra("username");
        if (username == null) {
            username = "default_user"; // Fallback if no username is passed
        }

        // Setup RecyclerView
        addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressAdapter = new AddressAdapter(addressList, keyList, username, this, this);
        addressRecyclerView.setAdapter(addressAdapter);

       String phonenumber=getIntent().getStringExtra("phonenumber");
        Log.d("AddressPopupActivity", "AddressPopupActivity ,phonenumber is"+phonenumber);
        // Firebase Reference
        userAddressRef = FirebaseDatabase.getInstance().getReference("Users").child(phonenumber).child("addresses");

        // Load saved addresses
        loadAddresses();

        // Close popup
        closeButton.setOnClickListener(v -> finish());

        // Open Add Address Activity
        addAddressButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddressPopupActivity.this, AddAddressActivity.class);
            intent.putExtra("username", username);
            startActivityForResult(intent, 1);
        });
    }

    // Load addresses from Firebase
    private void loadAddresses() {
        userAddressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressList.clear();
                keyList.clear();

                for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                    String key = addressSnapshot.getKey();
                    String address = addressSnapshot.getValue(String.class);

                    if (address != null) {
                        addressList.add(address);
                        keyList.add(key);
                    }
                }

                // ✅ Fetch the default address separately
                userAddressRef.getParent().child("default_address").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String savedAddress = snapshot.getValue(String.class);
                            if (savedAddress != null) {
                                defaultAddress.setText(savedAddress);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddressPopupActivity.this, "Failed to load default address", Toast.LENGTH_SHORT).show();
                    }
                });

                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddressPopupActivity.this, "Failed to load addresses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handle the result from AddAddressActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String newAddress = data.getStringExtra("new_address");
            if (newAddress != null) {
                updateDefaultAddress(newAddress);
                loadAddresses(); // Reload from Firebase to ensure accuracy
            }
        }
    }

    // Update default address in the TextView and send it back to the main activity
    public void updateDefaultAddress(String address) {
        if (address != null && !address.isEmpty()) {
            defaultAddress.setText(address); // Update the local display

            // Save to Firebase under user's data
            userAddressRef.getParent().child("default_address").setValue(address);


            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_address", address);
            setResult(RESULT_OK, resultIntent); // Send updated address back to MainActivity
        } else {
            defaultAddress.setText("No Address Found");
        }
    }

    // Implement the onAddressSelected method to update the default address
    @Override
    public void onAddressSelected(String address) {
        updateDefaultAddress(address);
    }
}
