package com.example.apt3060groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button btnHeadquarters, btnBranchOne, btnBranchTwo, btnBranchThree, btnLogout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize buttons
        btnHeadquarters = findViewById(R.id.btn_headquarters);
        btnBranchOne = findViewById(R.id.btn_branch_one);
        btnBranchTwo = findViewById(R.id.btn_branch_two);
        btnBranchThree = findViewById(R.id.btn_branch_three);
        btnLogout = findViewById(R.id.btn_logout);

        // Set click listeners
        btnHeadquarters.setOnClickListener(v -> openProductsActivity("Headquarters"));
        btnBranchOne.setOnClickListener(v -> openProductsActivity("Branch One"));
        btnBranchTwo.setOnClickListener(v -> openProductsActivity("Branch Two"));
        btnBranchThree.setOnClickListener(v -> openProductsActivity("Branch Three"));
        btnLogout.setOnClickListener(v -> logout());
    }

    private void openProductsActivity(String branchName) {
        Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
        intent.putExtra("BRANCH_NAME", branchName);
        startActivity(intent);
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
