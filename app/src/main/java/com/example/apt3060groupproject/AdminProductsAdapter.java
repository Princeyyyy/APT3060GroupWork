package com.example.apt3060groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class AdminProductsAdapter extends RecyclerView.Adapter<AdminProductsAdapter.AdminProductsViewHolder> {

    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public AdminProductsAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_product, parent, false);
        return new AdminProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductsViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class AdminProductsViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvBranch, tvPrice, tvCount;

        AdminProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvBranch = itemView.findViewById(R.id.tvBranch);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCount = itemView.findViewById(R.id.tvCount);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onProductClick(productList.get(position));
                }
            });
        }

        void bind(Product product) {
            tvName.setText(product.getName());
            tvDescription.setText(product.getDescription());
            tvBranch.setText("Branch: " + product.getBranch());
            tvPrice.setText(String.format(Locale.getDefault(), "Price: Ksh.%.2f", product.getPrice()));
            tvCount.setText("Count: " + product.getCount());
        }
    }
}
