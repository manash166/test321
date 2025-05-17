package com.example.test321;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.util.Log;  // Add this import at the top

import java.util.Arrays;
import java.util.List;

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

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyB7UvM1K9QJS0VkiUDW9qn7nKtU1tWepnU");  // Replace with your actual key
        }

            addressInput.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(AddAddressActivity.this);
            startActivityForResult(intent, 1);

        });

        phonenumber = getIntent().getStringExtra("phonenumber");

        if (phonenumber == null || phonenumber.isEmpty()) {
            Toast.makeText(this, "Phone number not received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("AddAddressActivity", "Phone number received: " + phonenumber);

        userAddressRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(phonenumber)
                .child("addresses");

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

    // ✅ Add this outside onCreate()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                addressInput.setText(place.getAddress());
                Log.d("AddAddressActivity", "Selected Address: " + place.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                com.google.android.gms.common.api.Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("AddAddressActivity", "Autocomplete error: " + status.getStatusMessage());
            }
        }
    }

    private void saveAddress(String address) {
        DatabaseReference newAddressRef = userAddressRef.push();

        newAddressRef.setValue(address).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
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
