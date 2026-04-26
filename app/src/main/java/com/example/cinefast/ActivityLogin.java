package com.example.cinefast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityLogin extends AppCompatActivity {

    Button btnLogin;
    ImageButton btnBack;
    TextView tvForgotPassword, tvRegister;
    EditText etEmail, etPassword;
    CheckBox cbRememberMe;
    FirebaseAuth mAuth;
    private final int MIN_PASSWORD_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initFirebaseAuth();
        attachListeners();
    }

    public void initViews() {

        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegister = findViewById(R.id.tv_register);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        cbRememberMe = findViewById(R.id.cb_remember_me);
        btnBack = findViewById(R.id.btn_back);
    }
    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void attachListeners() {

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(!validateEmail(email)){
                Toast.makeText(this, "Invalid email. Try again", Toast.LENGTH_LONG).show();
                etEmail.setText("");
                return;
            }

            if(password.length() < MIN_PASSWORD_LENGTH){
                Toast.makeText(this, "Password must be at least " + String.valueOf(MIN_PASSWORD_LENGTH) + " characters long", Toast.LENGTH_LONG).show();
                clearPasswordFields();
                return;
            }

            btnLogin.setClickable(false);
            cbRememberMe.setClickable(false);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                cacheUserInfo(email, cbRememberMe.isChecked());
                                Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ActivityLogin.this, "Log in failed. Please check your credentials.",
                                        Toast.LENGTH_SHORT).show();
                                clearPasswordFields();
                                etEmail.setText("");
                            }
                        }
                    });

            cbRememberMe.setClickable(true);
            btnLogin.setClickable(true);

        });
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Forgot Password feature not implemented yet.", Toast.LENGTH_SHORT).show();
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void clearPasswordFields(){
        if(etPassword != null){
            etPassword.setText("");
        }
    }

    private boolean validateEmail(String email){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }

    private void cacheUserInfo(String email, boolean rememberUser){
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putBoolean("remember_user", rememberUser);
        editor.apply();
    }
}