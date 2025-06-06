package com.example.test321;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenServiceAdapter extends RecyclerView.Adapter<MenServiceAdapter.ViewHolder> {
    private Context context;
    private List<MenService> serviceList;
    private List<MenService> selectedServices = new ArrayList<>();
    private TextView totalAmountTextView;  // Reference to total amount TextView
    private int totalAmount = 0; // Keep track of the total price

    public MenServiceAdapter(Context context, List<MenService> serviceList, TextView totalAmountTextView) {
        this.context = context;
        this.serviceList = serviceList;
        this.totalAmountTextView = totalAmountTextView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_men_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenService service = serviceList.get(position);
        holder.serviceName.setText(service.getServiceName());
        holder.price.setText(service.getPrice());

        holder.btnAdd.setOnClickListener(v -> {
            if (holder.btnAdd.getText().toString().equals("Add")) {
                holder.btnAdd.setText("Added");
                holder.btnAdd.setBackgroundColor(Color.GREEN);
                selectedServices.add(service);
                totalAmount += extractPrice(service.getPrice()); // Add price
                Toast.makeText(context, service.getServiceName() + " added!", Toast.LENGTH_SHORT).show();
            } else {
                holder.btnAdd.setText("Add");
                holder.btnAdd.setBackgroundColor(Color.LTGRAY);
                selectedServices.remove(service);
                totalAmount -= extractPrice(service.getPrice()); // Subtract price
                Toast.makeText(context, service.getServiceName() + " removed!", Toast.LENGTH_SHORT).show();
            }
            updateTotalAmount();  // Update the total amount dynamically
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    // Method to get selected services
    public List<MenService> getSelectedServices() {
        return selectedServices;
    }

    // Helper method to extract numeric price from a string (e.g., "₹200" -> 200)
    private int extractPrice(String price) {
        return Integer.parseInt(price.replaceAll("[^0-9]", ""));
    }

    // Update the total amount displayed in textView4
    private void updateTotalAmount() {
        totalAmountTextView.setText("₹" + totalAmount);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, price;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.textServiceName);
            price = itemView.findViewById(R.id.textPrice);
            btnAdd = itemView.findViewById(R.id.buttonAdd);
            btnAdd.setText("Add"); // Default state
            btnAdd.setBackgroundColor(Color.LTGRAY);

        }
    }
}
