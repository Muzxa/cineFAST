package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.*;

public class ActivityRegister extends AppCompatActivity {
    Button btnRegister;
    ImageButton btnBack;
    EditText etRegisterEmail, etRegisterPassword, etRegisterConfirmPassword;
    FirebaseAuth mAuth;
    private final int MIN_PASSWORD_LENGTH = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initFirebaseAuth();
        attachListeners();
    }

    private void initViews() {
        btnRegister = findViewById(R.id.btn_register);
        btnBack = findViewById(R.id.btn_back);
        etRegisterEmail = findViewById(R.id.et_register_email);
        etRegisterPassword = findViewById(R.id.et_register_password);
        etRegisterConfirmPassword = findViewById(R.id.et_register_confirm_password);
    }
    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void attachListeners() {
        btnRegister.setOnClickListener(v -> {
            String email = etRegisterEmail.getText().toString().trim();
            String password = etRegisterPassword.getText().toString().trim();
            String passwordConfirmation = etRegisterConfirmPassword.getText().toString().trim();

            if(!validateEmail(email)){
                Toast.makeText(this, "Invalid email. Try again", Toast.LENGTH_LONG).show();
                etRegisterEmail.setText("");
                return;
            }

            if(password.length() < MIN_PASSWORD_LENGTH){
                Toast.makeText(this, "Password must be at least " + String.valueOf(MIN_PASSWORD_LENGTH) + " characters long", Toast.LENGTH_LONG).show();
                clearPasswordFields();
                return;
            }

            if(!password.equals(passwordConfirmation)) {
                Toast.makeText(this, "Passwords do not match. Try again", Toast.LENGTH_LONG).show();
                clearPasswordFields();
                return;
            }
            btnRegister.setClickable(false);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ActivityRegister.this, "Registration Successful. Log into your account.",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ActivityRegister.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            btnRegister.setClickable(true);
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void clearPasswordFields(){
        if(etRegisterConfirmPassword != null && etRegisterPassword != null){
            etRegisterPassword.setText("");
            etRegisterConfirmPassword.setText("");
        }
    }

    private boolean validateEmail(String email){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }
}