package com.example.test321;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private ArrayList<String> addressList;
    private ArrayList<String> keyList;
    private String userId;
    private Context context;
    private String selectedAddress = "";
    private OnAddressSelectedListener listener;

    public interface OnAddressSelectedListener {
        void onAddressSelected(String address);
    }

    public AddressAdapter(ArrayList<String> addressList, ArrayList<String> keyList, String userId, Context context, OnAddressSelectedListener listener) {
        this.addressList = addressList;
        this.keyList = keyList;
        this.userId = userId;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String address = addressList.get(position);
        String key = keyList.get(position); // Get the key for the current address

        holder.addressTextView.setText(address);

        // Highlight selected address
        if (address.equals(selectedAddress)) {
            holder.addressTextView.setTextColor(context.getResources().getColor(R.color.black)); // Highlight color
        } else {
            holder.addressTextView.setTextColor(context.getResources().getColor(R.color.black)); // Default color
        }

        // Handle address selection
        holder.addressTextView.setOnClickListener(v -> {
            selectedAddress = address;
            notifyDataSetChanged(); // Refresh the list to update highlight

            if (listener != null) {
                listener.onAddressSelected(address); // Send address to activity
            }
        });

        // Handle delete action with both position and key
        holder.deleteIcon.setOnClickListener(v -> deleteAddress(position, key));
    }

    private void deleteAddress(int position, String key) {
        if (addressList.isEmpty()) {
            Log.w("AddressAdapter", "Address list is empty, cannot delete.");
            return;  // Early return if the list is empty

        }

        if (position >= 0 && position < addressList.size()) {
            // Proceed with deletion only if the position is valid
            addressList.remove(position);
            keyList.remove(position);

            // Remove from Firebase
            DatabaseReference addressRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(userId)
                    .child("addresses")
                    .child(key);

            addressRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("AddressAdapter", "Address deleted successfully");
                        notifyDataSetChanged(); // Notify adapter to refresh the view
                    })
                    .addOnFailureListener(e -> {
                        Log.e("AddressAdapter", "Failed to delete address", e);
                        Toast.makeText(context, "Failed to delete address", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.w("AddressAdapter", "Invalid position for deletion: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView addressTextView;
        Button deleteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.item_address);
            deleteIcon = itemView.findViewById(R.id.delete_button);
        }
    }

    public void setSelectedAddress(String address) {
        this.selectedAddress = address;
        notifyDataSetChanged();
    }
}
