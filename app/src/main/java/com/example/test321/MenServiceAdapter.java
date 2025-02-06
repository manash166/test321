package com.example.test321;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MenServiceAdapter extends RecyclerView.Adapter<MenServiceAdapter.ViewHolder> {
    private Context context;
    private List<MenService> serviceList;

    public MenServiceAdapter(Context context, List<MenService> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
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

        holder.btnAdd.setOnClickListener(v ->
                Toast.makeText(context, service.getServiceName() + " added!", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, price;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.textServiceName);
            price = itemView.findViewById(R.id.textPrice);
            btnAdd = itemView.findViewById(R.id.buttonAdd);
        }
    }
}

