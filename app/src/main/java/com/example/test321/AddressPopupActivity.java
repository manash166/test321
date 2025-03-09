package com.example.test321;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AddressPopupActivity extends AppCompatActivity {

    private ImageView closeButton;
    private RecyclerView addressRecyclerView;
    private AddressAdapter addressAdapter;
    private ArrayList<String> addressList;
    private DatabaseReference userAddressRef;
    private String username;
   private TextView addAddressButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_popup);

        closeButton = findViewById(R.id.close_button);
        addAddressButton = findViewById(R.id.add_address_button);
        addressRecyclerView = findViewById(R.id.address_recycler_view);

        addressList = new ArrayList<>();

        // Get Username from Intent
        username = getIntent().getStringExtra("username");
        if (username == null) {
            username = "default_user"; // Fallback if no username is passed
        }

        // Set RecyclerView
        addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressAdapter = new AddressAdapter(addressList);
        addressRecyclerView.setAdapter(addressAdapter);

        // Get Firebase Reference for Dynamic User
        userAddressRef = FirebaseDatabase.getInstance().getReference("Users").child(username).child("addresses");

        // Close Popup
        closeButton.setOnClickListener(v -> finish());

        // Open Add Address Activity
        addAddressButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddressPopupActivity.this, AddAddressActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Fetch Saved Addresses
        loadAddresses();
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
                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle Error
            }
        });
    }
}
