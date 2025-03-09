package com.example.test321;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private ArrayList<String> addressList;
    private String selectedAddress = "";

    public AddressAdapter(ArrayList<String> addressList) {
        this.addressList = addressList;
    }

    public void setSelectedAddress(String address) {
        this.selectedAddress = address;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        String address = addressList.get(position);
        holder.addressTextView.setText(address);

        // Highlight selected item in blue
        if (address.equals(selectedAddress)) {
            holder.addressTextView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_blue_light));
        } else {
            holder.addressTextView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
        }

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (v.getContext() instanceof AddressPopupActivity) {
                ((AddressPopupActivity) v.getContext()).updateDefaultAddress(address);
            }
            setSelectedAddress(address);
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView addressTextView;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.item_address);
        }
    }
}
