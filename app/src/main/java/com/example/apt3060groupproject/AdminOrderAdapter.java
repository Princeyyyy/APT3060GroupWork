package com.example.apt3060groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.AdminOrderViewHolder> {
    private List<Order> orderList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public AdminOrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_order, parent, false);
        return new AdminOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class AdminOrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvUserName, tvProductName, tvProductPrice, tvBranch, tvOrderDate, tvStatus;

        AdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvBranch = itemView.findViewById(R.id.tvBranch);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        void bind(Order order) {
            tvOrderId.setText("Order ID: " + order.getId());
            tvUserName.setText("Customer: " + order.getUserName());
            tvProductName.setText("Product: " + order.getProductName());
            tvProductPrice.setText(String.format(Locale.getDefault(), "Price: Ksh.%.2f", order.getProductPrice()));
            tvBranch.setText("Branch: " + order.getProductBranch());
            tvOrderDate.setText("Date: " + dateFormat.format(order.getOrderDate()));
            tvStatus.setText("Status: " + (order.isConfirmed() ? "Confirmed" : "Pending"));
        }
    }
}
