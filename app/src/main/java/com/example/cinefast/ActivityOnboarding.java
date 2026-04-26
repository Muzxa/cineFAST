package com.example.cinefast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityOnboarding extends AppCompatActivity {

    Button onboardingButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }


    private void init()
    {
        onboardingButton = findViewById(R.id.button_onboarding);
        onboardingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);
                String email = preferences.getString("email", null);
                boolean rememberUser = preferences.getBoolean("remember_user", false);

                if(rememberUser && email != null){
                    Intent intent = new Intent(ActivityOnboarding.this, ActivityHome.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(ActivityOnboarding.this, ActivityLogin.class);
                    startActivity(intent);
                }
            }
        });
    }
}