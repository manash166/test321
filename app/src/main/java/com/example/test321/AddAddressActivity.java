package com.example.test321;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddAddressActivity extends AppCompatActivity {

    private EditText addressInput;
    private Button continueButton, currentLocationButton;
    private DatabaseReference userAddressRef;
    private String phonenumber;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private final ActivityResultLauncher<String> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getCurrentLocation();
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        addressInput = findViewById(R.id.address_input);
        addressInput.setEnabled(false); // Step 1: Disable initially
        continueButton = findViewById(R.id.continue_button);
        currentLocationButton = findViewById(R.id.current_location_button);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyB7UvM1K9QJS0VkiUDW9qn7nKtU1tWepnU"); // Replace with your key
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("IN");
        autocompleteFragment.setHint("Enter your address");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                String address = place.getAddress();
                LatLng latLng = place.getLatLng();
                addressInput.setEnabled(true); // Step 2A: Enable on place select

                if (address != null) {
                    addressInput.setText(address);
                }

                if (mMap != null && latLng != null) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            }

            @Override
            public void onError(@NonNull com.google.android.gms.common.api.Status status) {
                Log.e("PlaceSelected", "Error: " + status.getStatusMessage());
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    mMap = googleMap;

                    LatLng guwahati = new LatLng(26.1445, 91.7362);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(guwahati, 15));
                }
            });
        }

        phonenumber = getIntent().getStringExtra("phonenumber");

        if (phonenumber == null || phonenumber.isEmpty()) {
            Toast.makeText(this, "Phone number not received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userAddressRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(phonenumber)
                .child("addresses");

        continueButton.setOnClickListener(v -> {
            String address = addressInput.getText().toString().trim();

            if (!address.isEmpty()) {
                saveAddress(address);
            } else {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show();
            }
        });

        currentLocationButton.setOnClickListener(v -> {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        });
    }

    private void getCurrentLocation() {
        addressInput.setEnabled(true); // Step 2B: Enable after current location success

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            1
                    );

                    if (addresses != null && !addresses.isEmpty()) {
                        String address = addresses.get(0).getAddressLine(0);
                        addressInput.setText(address);

                        if (mMap != null) {
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to fetch address", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAddress(String address) {
        DatabaseReference newAddressRef = userAddressRef.push();
        newAddressRef.setValue(address).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userAddressRef.getParent().child("default_address").setValue(address);
                Toast.makeText(this, "Address Added Successfully", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("new_address", address);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Failed to add address", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
