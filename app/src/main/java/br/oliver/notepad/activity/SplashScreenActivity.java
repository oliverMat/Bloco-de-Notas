package br.oliver.notepad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import br.oliver.notepad.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView iv_splash = findViewById(R.id.iv_splash);

        Glide.with(iv_splash.getContext()).load(R.drawable.logo2_512x512).into(iv_splash);

        new Handler(Looper.getMainLooper()).postDelayed(this::mostrarMainActivity, 350);
    }

    private void mostrarMainActivity() {
        Intent intent = new Intent(SplashScreenActivity.this,CategoriaActivity.class);
        startActivity(intent);
        finish();
    }

}