package com.example.apt3060groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String branchName;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get branch name from intent
        branchName = getIntent().getStringExtra("BRANCH_NAME");
        if (branchName == null) {
            Toast.makeText(this, "Error: Branch not specified", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setTitle(branchName);

        recyclerView = findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this::placeOrder);
        recyclerView.setAdapter(productAdapter);

        loadProducts();
    }

    private void loadProducts() {
        db.collection("products")
                .whereEqualTo("branch", branchName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        product.setId(document.getId());
                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProductsActivity.this, "Error loading products: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void placeOrder(Product product) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "You must be logged in to place an order", Toast.LENGTH_SHORT).show();
            return;
        }

        if (product.getCount() <= 0) {
            Toast.makeText(this, "This product is out of stock", Toast.LENGTH_SHORT).show();
            return;
        }

        Order order = new Order(
                user.getUid(),
                user.getDisplayName(),
                user.getEmail(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getBranch()
        );

        db.collection("orders")
                .add(order)
                .addOnSuccessListener(documentReference -> {
                    String orderId = documentReference.getId();

                    // Update the order document with the generated ID
                    documentReference.update("id", orderId)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                updateProductCount(product);

                                // Navigate to OrdersActivity
                                Intent intent = new Intent(ProductsActivity.this, OrdersActivity.class);
                                intent.putExtra("ORDER_ID", orderId);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error updating order ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error placing order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProductCount(Product product) {
        db.collection("products").document(product.getId())
                .update("count", FieldValue.increment(-1))
                .addOnSuccessListener(aVoid -> {
                    product.setCount(product.getCount() - 1);
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error updating product count: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}