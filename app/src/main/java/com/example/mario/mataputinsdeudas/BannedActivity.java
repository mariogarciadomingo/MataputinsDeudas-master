package com.example.mario.mataputinsdeudas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class BannedActivity extends AppCompatActivity {
    ImageView ImTu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banned);
        ImTu = findViewById(R.id.ImUsu);
        ImTu.setImageDrawable(PrincipalActivity.Imtu.getDrawable());
    }
}
