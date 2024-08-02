package com.example.apt3060groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private Button loginButton;
    private TextView signupLink;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Check if user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, check email and redirect accordingly
            redirectUser(currentUser.getEmail());
            finish();
            return;
        }

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        loginButton = findViewById(R.id.login_button);
        signupLink = findViewById(R.id.signup_link);
        progressBar = findViewById(R.id.progress_bar);

        loginButton.setOnClickListener(v -> loginUser());
        signupLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (validateInput(email, password)) {
            showProgress(true);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        showProgress(false);
                        if (task.isSuccessful()) {
                            // Login success, check email and redirect accordingly
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                redirectUser(user.getEmail());
                            }
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            showError();
                        }
                    });
        }
    }

    private void redirectUser(String email) {
        if ("admin@gmail.com".equals(email)) {
            startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Email is required");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Password is required");
            return false;
        }
        return true;
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!show);
    }

    private void showError() {
        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
    }
}
