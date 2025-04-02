package com.example.test321;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.util.Log;  // Add this import at the top

public class AddAddressActivity extends AppCompatActivity {
    private EditText addressInput;
    private Button continueButton;
    private DatabaseReference userAddressRef;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        addressInput = findViewById(R.id.address_input);
        continueButton = findViewById(R.id.continue_button);

        // Get Username from Intent
        username = getIntent().getStringExtra("username");
        if (username == null) {
            username = "default_user"; // Fallback if no username is passed
        }

        // Firebase Reference
        userAddressRef = FirebaseDatabase.getInstance().getReference("Users").child(username).child("addresses");

        // Handle Continue Button
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInput.getText().toString().trim();

                if (!address.isEmpty()) {
                    Log.d("AddAddressActivity", "Saving address: " + address);  // ✅ Log successful input
                    saveAddress(address);
                } else {
                    Log.e("AddAddressActivity", "Error: Address field is empty!");  // ✅ Log error for empty input
                    Toast.makeText(AddAddressActivity.this, "Please enter an address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveAddress(String address) {
        userAddressRef.push().setValue(address).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("new_address", address);
                setResult(RESULT_OK, resultIntent);
                finish(); // Return to AddressPopupActivity
            } else {
                Toast.makeText(AddAddressActivity.this, "Failed to add address", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
