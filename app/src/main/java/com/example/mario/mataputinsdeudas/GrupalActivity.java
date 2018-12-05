package com.example.mario.mataputinsdeudas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class GrupalActivity extends AppCompatActivity {
    ImageButton btgrupal;
    CheckBox gUsuario1, gUsuario2, gUsuario3, gUsuario4, gUsuario5;
    EditText gDinero, gDescripcion;
    Button gBtFirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupal);


        gUsuario1 = findViewById(R.id.GUsuario1);
        gUsuario1.setText(PrincipalActivity.usuarios[0]);
        gUsuario2 = findViewById(R.id.GUsuario2);
        gUsuario2.setText(PrincipalActivity.usuarios[1]);
        gUsuario3 = findViewById(R.id.GUsuario3);
        gUsuario3.setText(PrincipalActivity.usuarios[2]);
        gUsuario4 = findViewById(R.id.GUsuario4);
        gUsuario4.setText(PrincipalActivity.usuarios[3]);
        gUsuario5 = findViewById(R.id.GUsuario5);
        gBtFirmar = findViewById(R.id.btFirmar);
        gDinero = findViewById(R.id.edGDinero);
        gDescripcion = findViewById(R.id.Gdescripcion);

        gBtFirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Integer> usuarios = new ArrayList<>();
                if (gUsuario1.isChecked())
                    usuarios.add(0);
                if (gUsuario2.isChecked())
                    usuarios.add(1);
                if (gUsuario3.isChecked())
                    usuarios.add(2);
                if (gUsuario4.isChecked())
                    usuarios.add(3);
                if (gUsuario5.isChecked())
                    usuarios.add(-1);
                if (!gDinero.getText().toString().equals("") & usuarios.size() > 0) {

                    PrincipalActivity.firmar(usuarios, gDescripcion, gDinero);
                    gDinero.clearFocus();
                    gDescripcion.clearFocus();
                    finish();
                }

            }
        });


    }


}
