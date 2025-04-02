package com.example.test321;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private ArrayList<String> addressList;
    private ArrayList<String> keyList; // Store Firebase keys
    private DatabaseReference userAddressRef;
    private String userId; // Store the user ID
    private Context context;
    private String selectedAddress = "";

    public AddressAdapter(ArrayList<String> addressList, ArrayList<String> keyList, String userId, Context context) {
        this.addressList = addressList;
        this.keyList = keyList;
        this.userId = userId;
        this.context = context;
        this.userAddressRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("addresses");
    }
    public interface OnAddressSelectedListener {
        void onAddressSelected(String address);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (addressList == null || addressList.isEmpty() || keyList == null || keyList.isEmpty()) {
            Log.e("AddressAdapter", "Error: Trying to bind view with an empty list!");
            return; // Exit method to prevent crash
        }

        if (position >= addressList.size() || position >= keyList.size()) {
            Log.e("AddressAdapter", "Error: Position out of bounds! Position: " + position);
            return; // Exit method safely
        }

        String address = addressList.get(position);
        String key = keyList.get(position); // Get Firebase key for this address

        holder.addressTextView.setText(address);

        // Highlight selected address
        if (address.equals(selectedAddress)) {
            holder.addressTextView.setTextColor(context.getResources().getColor(R.color.black)); // Highlight color
        } else {
            holder.addressTextView.setTextColor(context.getResources().getColor(R.color.black)); // Default color
        }

        // Delete address on click
        holder.deleteIcon.setOnClickListener(v -> deleteAddress(position, key));
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

    private void deleteAddress(int position, String key) {
        userAddressRef.child(key).removeValue().addOnSuccessListener(aVoid -> {
            // Check if the position is valid before removing
            if (position >= 0 && position < addressList.size()) {
                addressList.remove(position);
                keyList.remove(position);
            }

            // If the list is empty, refresh RecyclerView completely
            if (addressList.isEmpty()) {
                notifyDataSetChanged();
            } else {
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, addressList.size());
            }

            Toast.makeText(context, "Address deleted", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed to delete address", Toast.LENGTH_SHORT).show();
        });
    }

    public void setSelectedAddress(String address) {
        this.selectedAddress = address;
        notifyDataSetChanged();
    }
}
