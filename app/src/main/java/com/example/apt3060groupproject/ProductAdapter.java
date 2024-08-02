package com.example.apt3060groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private OnOrderClickListener onOrderClickListener;

    public interface OnOrderClickListener {
        void onOrderClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnOrderClickListener onOrderClickListener) {
        this.productList = productList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDescription, textPrice, textCount;
        Button btnOrder;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_product_name);
            textDescription = itemView.findViewById(R.id.text_product_description);
            textPrice = itemView.findViewById(R.id.text_product_price);
            textCount = itemView.findViewById(R.id.text_product_count);
            btnOrder = itemView.findViewById(R.id.btn_order);
        }

        void bind(Product product) {
            textName.setText(product.getName());
            textDescription.setText(product.getDescription());
            textPrice.setText(String.format(Locale.getDefault(), "Ksh.%.2f", product.getPrice()));
            textCount.setText(String.format(Locale.getDefault(), "Available: %d", product.getCount()));
            btnOrder.setEnabled(product.getCount() > 0);
            btnOrder.setOnClickListener(v -> onOrderClickListener.onOrderClick(product));
        }
    }
}
