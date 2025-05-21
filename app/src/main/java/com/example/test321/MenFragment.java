package com.example.test321;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MenFragment extends Fragment {

    private LinearLayout servicesContainer;
    private Button editButton, saveButton;
    private boolean isEditable = false;
    private HashMap<String, EditText[]> serviceViews = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_men, container, false);

        servicesContainer = view.findViewById(R.id.services_container);
        editButton = view.findViewById(R.id.button_edit);
        saveButton = view.findViewById(R.id.button_save);

        // Ensure all are visible
        servicesContainer.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);

        Toolbar toolbar = view.findViewById(R.id.men_toolbar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

// Enable back arrow
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

// Handle back arrow click
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());


        loadServicesFromFirebase();

        editButton.setOnClickListener(v -> setEditMode(true));

        saveButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert)
                    .setTitle("Confirm Save")
                    .setMessage("Do you want to save the changes?")
                    .setPositiveButton("Yes", (dialog, which) -> saveToFirebase())
                    .setNegativeButton("No", null)
                    .show();

        });

        return view;
    }

    private void loadServicesFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("services&prices/Men");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                servicesContainer.removeAllViews();
                serviceViews.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    String serviceName = child.getKey();
                    String price = child.getValue(String.class);

                    EditText nameET = new EditText(getContext());
                    nameET.setText(serviceName);
                    nameET.setEnabled(false);

                    EditText priceET = new EditText(getContext());
                    priceET.setText(price);
                    priceET.setEnabled(false);

                    serviceViews.put(serviceName, new EditText[]{nameET, priceET});

                    servicesContainer.addView(nameET);
                    servicesContainer.addView(priceET);
                }

                // Show container after loading
                servicesContainer.setVisibility(View.VISIBLE);
                setEditMode(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Optional: handle error
            }
        });
    }

    private void setEditMode(boolean editable) {
        isEditable = editable;
        for (EditText[] pair : serviceViews.values()) {
            for (EditText et : pair) {
                et.setEnabled(editable);
            }
        }
    }

    private void saveToFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("services&prices/Men");
        Map<String, Object> updates = new HashMap<>();

        for (EditText[] pair : serviceViews.values()) {
            String newName = pair[0].getText().toString();
            String newPrice = pair[1].getText().toString();
            updates.put(newName, newPrice);
        }

        ref.setValue(updates);
        setEditMode(false);
    }
}
