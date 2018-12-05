package com.example.mario.mataputinsdeudas;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.flask.colorpicker.OnColorChangedListener;

public class PersonalizarActivity extends AppCompatActivity {
    ImageView fons, ImTexto, ImMateriales, ImTu, Imel;
    TextView tuNom, Usuario, total;

    LinearLayout LinearUsuario;
    Switch swFotos, swUsuario1, swUsuario2, swUsuario3, swUsuario4, swTotal, swSilencio;
    ConstraintLayout ColorText, ColorMateterials;
    SharedPreferences preferences;
    com.flask.colorpicker.ColorPickerView colorPickerViewText, colorPickerViewMaterials;
    Button Guardar, Ddescartar;
    Context context;
    int colorTexto = 0;
    int colorMateriales = 0;
    boolean imagenes = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizar);
        inicialitzarComponents();

    }

    private void inicialitzarSharedPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        colorTexto = preferences.getInt("ColorTexto", Color.BLACK);
        colorMateriales = preferences.getInt("ColorMateriales", Color.WHITE);
        imagenes = preferences.getBoolean("Imagenes", true);
        ImTexto.setBackgroundColor(colorTexto);
        tuNom.setTextColor(colorTexto);
        tuNom.setText(PrincipalActivity.tu.getText());
        Usuario.setText(PrincipalActivity.usuario1.getText());
        Usuario.setTextColor(colorTexto);
        total = findViewById(R.id.PTotal1);
        total.setTextColor(Color.BLUE);
        ImTu = findViewById(R.id.imtu2);
        ImTu.setImageDrawable(PrincipalActivity.Imtu.getDrawable());
        Imel = findViewById(R.id.PImUsuario1);
        Imel.setImageDrawable(PrincipalActivity.ImUsuario1.getDrawable());
        colorPickerViewText = findViewById(R.id.color_picker_view);
        colorPickerViewMaterials = findViewById(R.id.color_picker_view_Mat);
        colorPickerViewText.setInitialColor(colorTexto, true);
        colorPickerViewMaterials.setInitialColor(colorMateriales, true);
        colorPickerViewText.setDensity(15);
        colorPickerViewMaterials.setDensity(15);
        colorPickerViewText.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                colorTexto = selectedColor;
                ImTexto.setBackgroundColor(selectedColor);
                tuNom.setTextColor(selectedColor);
                Usuario.setTextColor(selectedColor);

            }
        });
        colorPickerViewMaterials.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                colorTexto = colorPickerViewText.getSelectedColor();
                colorMateriales = selectedColor;
                ImMateriales.setBackgroundColor(selectedColor);
                LinearUsuario.setBackgroundColor(selectedColor);

            }
        });


        ImMateriales.setBackgroundColor(colorMateriales);
        LinearUsuario.setBackgroundColor(colorMateriales);
        String Pfondo = preferences.getString("fondo", "");
        if (!imagenes) {
            Imel.setVisibility(View.GONE);
        }
        swFotos.setChecked(imagenes);
        try {
            if (Pfondo != "") {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                fons.setImageBitmap(BitmapFactory.decodeFile(Pfondo, options));
            }
        } catch (Exception e) {
        }
        swUsuario1.setText(preferences.getString("Usuario1", "Usuario1"));
        swUsuario2.setText(preferences.getString("Usuario2", "Usuario2"));
        swUsuario3.setText(preferences.getString("Usuario3", "Usuario3"));
        swUsuario4.setText(preferences.getString("Usuario4", "Usuario4"));
        swUsuario1.setChecked(preferences.getBoolean("Usuario1Bool", true));
        swUsuario2.setChecked(preferences.getBoolean("Usuario2Bool", true));
        swUsuario3.setChecked(preferences.getBoolean("Usuario3Bool", true));
        swUsuario4.setChecked(preferences.getBoolean("Usuario4Bool", true));
        swTotal.setChecked(preferences.getBoolean("TotalBool", true));
        swSilencio.setChecked(preferences.getBoolean("SilencioOA", false));

    }

    private void desarSharedPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ColorTexto", colorTexto);
        editor.putInt("ColorMateriales", colorMateriales);
        editor.putBoolean("Imagenes", swFotos.isChecked());
        editor.putBoolean("Usuario1Bool", swUsuario1.isChecked());
        editor.putBoolean("Usuario2Bool", swUsuario2.isChecked());
        editor.putBoolean("Usuario3Bool", swUsuario3.isChecked());
        editor.putBoolean("Usuario4Bool", swUsuario4.isChecked());
        editor.putBoolean("TotalBool", swTotal.isChecked());
        editor.putBoolean("SilencioOA", swSilencio.isChecked());

        editor.commit();
    }

    private void inicialitzarComponents() {
        swFotos = findViewById(R.id.swFotos);
        swUsuario1 = findViewById(R.id.swUsu1);
        swTotal = findViewById(R.id.swTotal);
        swUsuario2 = findViewById(R.id.swUsu2);
        swUsuario3 = findViewById(R.id.swUsu3);
        swUsuario4 = findViewById(R.id.swUsu4);
        swSilencio = findViewById(R.id.swSilencioOA);

        ColorText = findViewById(R.id.ColorTextLayout);
        ColorMateterials = findViewById(R.id.ColorMaterialLayout);
        Guardar = findViewById(R.id.PGuardar);
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ColorText.getVisibility() == View.VISIBLE) {
                    colorTexto = colorPickerViewText.getSelectedColor();
                    ImTexto.setBackgroundColor(colorPickerViewText.getSelectedColor());
                    tuNom.setTextColor(colorPickerViewText.getSelectedColor());
                    Usuario.setTextColor(colorPickerViewText.getSelectedColor());
                    ColorText.setVisibility(View.GONE);
                } else if (ColorMateterials.getVisibility() == View.VISIBLE) {
                    colorMateriales = colorPickerViewMaterials.getSelectedColor();
                    ImMateriales.setBackgroundColor(colorPickerViewMaterials.getSelectedColor());
                    LinearUsuario.setBackgroundColor(colorPickerViewMaterials.getSelectedColor());
                    ColorMateterials.setVisibility(View.GONE);
                } else {
                    PrincipalActivity.DesarFireDisseny(colorTexto, colorMateriales, swUsuario1.isChecked(), swUsuario2.isChecked(), swUsuario3.isChecked(), swUsuario4.isChecked(), swTotal.isChecked(), swFotos.isChecked());
                    desarSharedPreferences();
                    finish();
                }

            }
        });
        Ddescartar = findViewById(R.id.PCancelar);
        Ddescartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ColorText.getVisibility() == View.VISIBLE) {
                    inicialitzarSharedPreferences();
                    ColorText.setVisibility(View.GONE);
                } else if (ColorMateterials.getVisibility() == View.VISIBLE) {
                    inicialitzarSharedPreferences();
                    ColorMateterials.setVisibility(View.GONE);
                } else {

                    finish();
                }

            }
        });
        fons = findViewById(R.id.PFons);
        tuNom = findViewById(R.id.PNom);
        Usuario = findViewById(R.id.PUsuario);
        ImTexto = findViewById(R.id.PImTexto);
        ImMateriales = findViewById(R.id.PImMateriales);
        LinearUsuario = findViewById(R.id.PLinearUsuario);
        ImTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorText.setVisibility(View.VISIBLE);
            }
        });
        ImMateriales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorMateterials.setVisibility(View.VISIBLE);
            }
        });

        swFotos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Imel.setVisibility(View.VISIBLE);
                else
                    Imel.setVisibility(View.GONE);

            }
        });

        context = this;

        CompoundButton.OnCheckedChangeListener on = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ao);
                    mediaPlayer.start();
                } else {
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.deu);
                    mediaPlayer.start();
                }
            }
        };
        inicialitzarSharedPreferences();
        swUsuario1.setOnCheckedChangeListener(on);
        swUsuario2.setOnCheckedChangeListener(on);
        swUsuario3.setOnCheckedChangeListener(on);
        swUsuario4.setOnCheckedChangeListener(on);
    }


}
