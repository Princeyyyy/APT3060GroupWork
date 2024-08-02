package com.example.apt3060groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminOrderAdapter adapter;
    private List<Order> allOrders;
    private List<Order> filteredOrders;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ChipGroup chipGroup;
    private Button btnAll, btnTotals, btnViewProducts, btnLogout;
    private String currentBranchFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        allOrders = new ArrayList<>();
        filteredOrders = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminOrderAdapter(filteredOrders);
        recyclerView.setAdapter(adapter);

        chipGroup = findViewById(R.id.chipGroup);
        btnAll = findViewById(R.id.btnAll);
        btnTotals = findViewById(R.id.btnTotals);
        btnViewProducts = findViewById(R.id.btnViewProducts);
        btnLogout = findViewById(R.id.btnLogout);

        setUpChipListeners();
        setUpButtonListeners();

        // Initially load all orders
        loadAllOrders();
    }

    private void setUpChipListeners() {
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentBranchFilter = null;
                btnAll.setEnabled(false);
                filterOrders(null);
            } else {
                Chip chip = findViewById(checkedIds.get(0));
                currentBranchFilter = chip.getText().toString();
                btnAll.setEnabled(true);
                filterOrders(currentBranchFilter);
            }
        });
    }

    private void setUpButtonListeners() {
        btnAll.setOnClickListener(v -> {
            chipGroup.clearCheck();
            currentBranchFilter = null;
            filterOrders(null);
            btnAll.setEnabled(false);
        });

        btnTotals.setOnClickListener(v -> showReport());
        btnViewProducts.setOnClickListener(v -> viewProducts());

        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadAllOrders() {
        db.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        allOrders.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Order order = document.toObject(Order.class);
                            allOrders.add(order);
                        }
                        filterOrders(currentBranchFilter);
                    } else {
                        Toast.makeText(AdminHomeActivity.this, "Error loading orders", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filterOrders(String branchFilter) {
        if (branchFilter == null) {
            filteredOrders.clear();
            filteredOrders.addAll(allOrders);
        } else {
            filteredOrders = allOrders.stream()
                    .filter(order -> order.getProductBranch().equals(branchFilter))
                    .collect(Collectors.toList());
        }
        adapter.notifyDataSetChanged();
    }

    private void showReport() {
        Intent intent = new Intent(this, OrderReportActivity.class);
        intent.putExtra("orders", (Serializable) filteredOrders);
        intent.putExtra("branchFilter", currentBranchFilter);
        startActivity(intent);
    }

    private void viewProducts() {
        Intent intent = new Intent(this, ProductManagementActivity.class);
        startActivity(intent);
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(AdminHomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}