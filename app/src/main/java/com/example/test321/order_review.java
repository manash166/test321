package com.example.test321;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class order_review extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);

        // Find Views
        TextView orderIdText = findViewById(R.id.orderIdText);
        TextView serviceListText = findViewById(R.id.serviceListText);
        TextView totalAmountText = findViewById(R.id.totalAmountText);

        // Get Data from Intent
        String orderId = getIntent().getStringExtra("orderId");
        String serviceDetails = getIntent().getStringExtra("serviceDetails");
        int totalAmount = getIntent().getIntExtra("totalAmount", 0);

        // Display Data
        orderIdText.setText("Order ID: " + orderId);
        serviceListText.setText(serviceDetails);
        totalAmountText.setText("Total Amount: ₹" + totalAmount);
    }
}
