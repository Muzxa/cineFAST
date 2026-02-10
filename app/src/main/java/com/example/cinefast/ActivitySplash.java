package com.example.cinefast;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.animation.Animation;

public class ActivitySplash extends AppCompatActivity {
    ImageView cinefastLogo, cinefastBranding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    private void init()
    {
        cinefastLogo=findViewById(R.id.iv_cinefast_logo);
        cinefastBranding=findViewById(R.id.iv_cinefast_branding);

        Animation fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.iv_cinefast_logo_fade);
        Animation translateAnimation = AnimationUtils.loadAnimation(this, R.anim.iv_cinefast_branding_translation);

        cinefastBranding.startAnimation(translateAnimation);
        cinefastLogo.startAnimation(fadeAnimation);
    }
}