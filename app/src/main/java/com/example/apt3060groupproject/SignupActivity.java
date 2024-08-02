package com.example.apt3060groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText fullNameField, emailField, passwordField, confirmPasswordField;
    private Button signupButton;
    private TextView loginLink;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fullNameField = findViewById(R.id.full_name_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        confirmPasswordField = findViewById(R.id.confirm_password_field);
        signupButton = findViewById(R.id.signup_button);
        loginLink = findViewById(R.id.login_link);
        progressBar = findViewById(R.id.progress_bar);

        signupButton.setOnClickListener(v -> signupUser());
        loginLink.setOnClickListener(v -> finish()); // Go back to LoginActivity
    }

    private void signupUser() {
        String fullName = fullNameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        if (validateInput(fullName, email, password, confirmPassword)) {
            showProgress(true);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Update user profile with display name
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(fullName) // Set your desired display name here
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Profile updated successfully
                                                    Log.d("Profile Update", "User profile updated.");
                                                }
                                            }
                                        });

                                saveUserToFirestore(user.getUid(), fullName, email);
                            }
                        } else {
                            showProgress(false);
                            showError("Sign up failed. " + task.getException().getMessage());
                        }
                    });
        }
    }

    private void saveUserToFirestore(String userId, String fullName, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("email", email);

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    showProgress(false);
                    // Sign up and data save success, go to HomeActivity
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    showProgress(false);
                    showError("Failed to save user data. " + e.getMessage());
                });
    }

    private boolean validateInput(String fullName, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(fullName)) {
            fullNameField.setError("Full name is required");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Email is required");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Password is required");
            return false;
        }
        if (password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordField.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        signupButton.setEnabled(!show);
    }

    private void showError(String errorMessage) {
        Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}