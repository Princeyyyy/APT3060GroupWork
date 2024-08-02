package com.example.apt3060groupproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;
import java.util.Locale;

public class OrderReportActivity extends AppCompatActivity {

    private TextView tvTotalOrders, tvTotalAmount, tvAverageOrderValue, tvConfirmedOrders, tvPendingOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_report);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Report");

        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvAverageOrderValue = findViewById(R.id.tvAverageOrderValue);
        tvConfirmedOrders = findViewById(R.id.tvConfirmedOrders);
        tvPendingOrders = findViewById(R.id.tvPendingOrders);

        List<Order> orders = (List<Order>) getIntent().getSerializableExtra("orders");
        String branchFilter = getIntent().getStringExtra("branchFilter");

        if (orders != null) {
            displayReport(orders, branchFilter);
        }
    }

    private void displayReport(List<Order> orders, String branchFilter) {
        int totalOrders = orders.size();
        double totalAmount = 0;
        int confirmedOrders = 0;
        int pendingOrders = 0;

        for (Order order : orders) {
            totalAmount += order.getProductPrice();
            if (order.isConfirmed()) {
                confirmedOrders++;
            } else {
                pendingOrders++;
            }
        }

        double averageOrderValue = totalOrders > 0 ? totalAmount / totalOrders : 0;

        tvTotalOrders.setText(String.format("Total Orders: %d", totalOrders));
        tvTotalAmount.setText(String.format(Locale.getDefault(), "Total Amount: Ksh.%.2f", totalAmount));
        tvAverageOrderValue.setText(String.format(Locale.getDefault(), "Average Order Value: Ksh.%.2f", averageOrderValue));
        tvConfirmedOrders.setText(String.format("Confirmed Orders: %d", confirmedOrders));
        tvPendingOrders.setText(String.format("Pending Orders: %d", pendingOrders));

        getSupportActionBar().setSubtitle(branchFilter != null ? branchFilter : "All Branches");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}