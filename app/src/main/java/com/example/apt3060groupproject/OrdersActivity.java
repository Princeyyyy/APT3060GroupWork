package com.example.apt3060groupproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private static final String TAG = "OrdersActivity";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private OrdersAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recycler_view_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        orderAdapter = new OrdersAdapter(orderList, this::confirmOrder);
        recyclerView.setAdapter(orderAdapter);

        loadOrders();
    }

    private void loadOrders() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "You must be logged in to view orders", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db.collection("orders")
                .whereEqualTo("userId", user.getUid())
                .whereEqualTo("confirmed", false)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "loadOrders: Successfully retrieved " + queryDocumentSnapshots.size() + " documents");
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Order order = document.toObject(Order.class);
                        order.setId(document.getId());
                        orderList.add(order);
                        Log.d(TAG, "loadOrders: Added order - " + order.getProductName());
                    }
                    orderAdapter.notifyDataSetChanged();
                    Log.d(TAG, "loadOrders: Notified adapter of data change. Total orders: " + orderList.size());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "loadOrders: Error loading orders", e);
                    Toast.makeText(OrdersActivity.this, "Error loading orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void confirmOrder(Order order) {
        db.collection("orders").document(order.getId())
                .update("isConfirmed", true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order confirmed", Toast.LENGTH_SHORT).show();
                    orderList.remove(order);
                    orderAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error confirming order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}