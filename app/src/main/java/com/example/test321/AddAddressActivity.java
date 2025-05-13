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
    private String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        addressInput = findViewById(R.id.address_input);
        continueButton = findViewById(R.id.continue_button);

          // Get Phonenumber from Intent
        phonenumber = getIntent().getStringExtra("phonenumber");

        if (phonenumber == null || phonenumber.isEmpty()) {
            Toast.makeText(this, "Phone number not received", Toast.LENGTH_SHORT).show();
            finish(); // Exit safely instead of crashing
            return;
        }

        Log.d("AddAddressActivity", "Phone number received: " + phonenumber);

// Now safe to use:
        userAddressRef = FirebaseDatabase.getInstance().getReference("Users").child(phonenumber).child("addresses");

        // Handle Continue Button
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInput.getText().toString().trim();

                if (!address.isEmpty()) {
                    saveAddress(address);
                } else {
                    Toast.makeText(AddAddressActivity.this, "Please enter an address", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    // Handle SaveAddress Here
    private void saveAddress(String address) {
        DatabaseReference newAddressRef = userAddressRef.push();  // this works only if "addresses" is not a string

        newAddressRef.setValue(address).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Also set the default address if needed
                userAddressRef.getParent().child("default_address").setValue(address);

                Toast.makeText(AddAddressActivity.this, "Address Added Successfully", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("new_address", address);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(AddAddressActivity.this, "Failed to add address", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
