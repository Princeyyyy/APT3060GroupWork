package com.example.apt3060groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {
    private List<Order> orderList;
    private OnConfirmClickListener onConfirmClickListener;

    public interface OnConfirmClickListener {
        void onConfirmClick(Order order);
    }

    public OrdersAdapter(List<Order> orderList, OnConfirmClickListener onConfirmClickListener) {
        this.orderList = orderList;
        this.onConfirmClickListener = onConfirmClickListener;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrdersViewHolder extends RecyclerView.ViewHolder {
        TextView textProductName, textPrice, textDate;
        Button btnConfirm;

        OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.text_order_product_name);
            textPrice = itemView.findViewById(R.id.text_order_price);
            textDate = itemView.findViewById(R.id.text_order_date);
            btnConfirm = itemView.findViewById(R.id.btn_confirm_order);
        }

        void bind(Order order) {
            textProductName.setText(order.getProductName());
            textPrice.setText(String.format(Locale.getDefault(), "Ksh.%.2f", order.getProductPrice()));
            textDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(order.getOrderDate()));
            btnConfirm.setOnClickListener(v -> onConfirmClickListener.onConfirmClick(order));
        }
    }
}
