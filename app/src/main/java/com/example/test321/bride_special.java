package com.example.test321;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;


public class bride_special extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bride_special);


        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);


        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(v -> {

            finish();
        });

        // Optional: set title in code
        getSupportActionBar().setTitle("Bride Special Package");

    }
}