package com.example.test321;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
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
        TextView address_textview=findViewById(R.id.address_textview);
        TextView userid_preview=findViewById(R.id.userid_preview);
        // Get Data from Intent
        String orderId = getIntent().getStringExtra("orderId");
        Log.d("orderID", "orderID is"+orderId);
        String serviceDetails = getIntent().getStringExtra("serviceDetails");
        int totalAmount = getIntent().getIntExtra("totalAmount", 0);
        String addressname =getIntent().getStringExtra("address");
        String username=getIntent().getStringExtra("userid");
        // Display Data
        address_textview.setText(addressname);
        orderIdText.setText("Order ID: " + orderId);
        serviceListText.setText(serviceDetails);
        totalAmountText.setText("₹" + totalAmount);
        userid_preview.setText(username);
//        serviceListText.setBackgroundColor(Color.RED);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) serviceListText.getLayoutParams();
        Log.d("Margins", "Left: " + layoutParams.leftMargin +
                ", Top: " + layoutParams.topMargin +
                ", Right: " + layoutParams.rightMargin +
                ", Bottom: " + layoutParams.bottomMargin);
        Log.d("Padding", "Left: " + serviceListText.getPaddingLeft() +
                ", Top: " + serviceListText.getPaddingTop() +
                ", Right: " + serviceListText.getPaddingRight() +
                ", Bottom: " + serviceListText.getPaddingBottom());




    }
}
