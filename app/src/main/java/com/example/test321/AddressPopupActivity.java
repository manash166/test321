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
import java.util.Collections;

public class AddressPopupActivity extends AppCompatActivity {

    private ImageView closeButton;
    private RecyclerView addressRecyclerView;
    private AddressAdapter addressAdapter;
    private ArrayList<String> addressList;
    private DatabaseReference userAddressRef;
    private String username;
    private TextView addAddressButton;
    private TextView defaultAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_popup);

        closeButton = findViewById(R.id.close_button);
        addAddressButton = findViewById(R.id.add_address_button);
        addressRecyclerView = findViewById(R.id.address_recycler_view);
        defaultAddress = findViewById(R.id.default_address);

        addressList = new ArrayList<>();

        // Get username from Intent
        username = getIntent().getStringExtra("username");
        if (username == null) {
            username = "default_user"; // Fallback if no username is passed
        }

        // Setup RecyclerView
        addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressAdapter = new AddressAdapter(addressList);
        addressRecyclerView.setAdapter(addressAdapter);

        // Firebase Reference
        userAddressRef = FirebaseDatabase.getInstance().getReference("Users").child(username).child("addresses");

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

    private void loadAddresses() {
        userAddressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressList.clear();
                for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                    String address = addressSnapshot.getValue(String.class);
                    addressList.add(address);
                }

                // Set the first (topmost) address as default if available
                if (!addressList.isEmpty()) {
                    updateDefaultAddress(addressList.get(0)); // Use first item instead of reversing
                    addressAdapter.setSelectedAddress(addressList.get(0));
                } else {
                    updateDefaultAddress("No address found");
                }

                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(AddressPopupActivity.this, "Failed to load addresses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String newAddress = data.getStringExtra("new_address");
            if (newAddress != null) {
                // Add new address to the top
                addressList.add(0, newAddress);
                addressAdapter.setSelectedAddress(newAddress);
                addressAdapter.notifyDataSetChanged();

                // Update both local and MainActivity address
                updateDefaultAddress(newAddress);
            }
        }
    }




    //* Updated AddressPopupActivity.java *//

    // Send selected address back to MainActivity
// Update default address TextView
    // Updated method to send selected address back to MainActivity
    public void updateDefaultAddress(String address) {
        if (address != null && !address.isEmpty()) {
            defaultAddress.setText(address); // Update local TextView
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_address", address);
            setResult(RESULT_OK, resultIntent); // Send address back to MainActivity
        }
    }



}
