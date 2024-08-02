package com.example.apt3060groupproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdminProductsAdapter adapter;
    private List<Product> productList;
    private FirebaseFirestore db;
    private FloatingActionButton fabAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminProductsAdapter(productList, this::showUpdateDeleteDialog);
        recyclerView.setAdapter(adapter);

        fabAddProduct = findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener(v -> showAddProductDialog());

        loadProducts();
    }

    private void loadProducts() {
        productList.clear();
        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ProductManagementActivity.this, "Error loading products", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        Spinner spinnerBranch = dialogView.findViewById(R.id.spinnerBranch);
        EditText etPrice = dialogView.findViewById(R.id.etPrice);
        EditText etCount = dialogView.findViewById(R.id.etCount);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.branch_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranch.setAdapter(adapter);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = etName.getText().toString();
            String description = etDescription.getText().toString();
            String branch = spinnerBranch.getSelectedItem().toString();
            double price = Double.parseDouble(etPrice.getText().toString());
            int count = Integer.parseInt(etCount.getText().toString());

            Product newProduct = new Product(null, name, description, branch, price, count);
            addProduct(newProduct);
        });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void addProduct(Product product) {
        db.collection("products")
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    product.setId(documentReference.getId());
                    productList.add(product);
                    adapter.notifyItemInserted(productList.size() - 1);
                    Toast.makeText(ProductManagementActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(ProductManagementActivity.this, "Error adding product", Toast.LENGTH_SHORT).show());
    }

    private void showUpdateDeleteDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_delete_product, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        Spinner spinnerBranch = dialogView.findViewById(R.id.spinnerBranch);
        EditText etPrice = dialogView.findViewById(R.id.etPrice);
        EditText etCount = dialogView.findViewById(R.id.etCount);

        etName.setText(product.getName());
        etDescription.setText(product.getDescription());
        etPrice.setText(String.valueOf(product.getPrice()));
        etCount.setText(String.valueOf(product.getCount()));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.branch_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranch.setAdapter(adapter);
        spinnerBranch.setSelection(adapter.getPosition(product.getBranch()));

        builder.setPositiveButton("Update", (dialog, which) -> {
            product.setName(etName.getText().toString());
            product.setDescription(etDescription.getText().toString());
            product.setBranch(spinnerBranch.getSelectedItem().toString());
            product.setPrice(Double.parseDouble(etPrice.getText().toString()));
            product.setCount(Integer.parseInt(etCount.getText().toString()));

            updateProduct(product);
        });
        builder.setNegativeButton("Delete", (dialog, which) -> deleteProduct(product));
        builder.setNeutralButton("Cancel", null);

        builder.create().show();
    }

    private void updateProduct(Product product) {
        db.collection("products").document(product.getId())
                .set(product)
                .addOnSuccessListener(aVoid -> {
                    int index = productList.indexOf(product);
                    productList.set(index, product);
                    adapter.notifyItemChanged(index);
                    Toast.makeText(ProductManagementActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(ProductManagementActivity.this, "Error updating product", Toast.LENGTH_SHORT).show());
    }

    private void deleteProduct(Product product) {
        db.collection("products").document(product.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    int index = productList.indexOf(product);
                    productList.remove(index);
                    adapter.notifyItemRemoved(index);
                    Toast.makeText(ProductManagementActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(ProductManagementActivity.this, "Error deleting product", Toast.LENGTH_SHORT).show());
    }
}