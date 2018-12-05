package com.example.mario.mataputinsdeudas;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.spec.ECField;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class LoginActivity extends AppCompatActivity {
    private EditText LogMario;
    private static final String TAG = "1";
    final String Anna = "anna@gmail.com";
    final String Laurita = "laurita@gmail.com";
    final String Lauron = "lauron@gmail.com";
    final String Mario = "mario@gmail.com";
    final String Blanca = "blanca@gmail.com";
    final String clave = "1234567";
    boolean programador = false;
    SharedPreferences preferences;
    FirebaseDatabase database;
    private  static String keyAnna="",keyMario="",keyLaurita="",keyLauron="",keyBlanca="";
    Button bMario, bAnna, bLaurita, blauron, bBlanca;
    ProgressBar bar;
    private Switch ModoProgramador;
    private FirebaseAuth mAuth;
    static DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        ModoProgramador = findViewById(R.id.ModoProgramador);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        inicializarSharedPreferences();
        ModoProgramador.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("Programador", isChecked);
                editor.commit();
            }
        });
        bAnna = findViewById(R.id.btAnne);
        bMario = findViewById(R.id.btMario);
        bLaurita = findViewById(R.id.btLaurita);
        blauron = findViewById(R.id.btLauron);
        bBlanca = findViewById(R.id.btBlanca);
        bar = findViewById(R.id.progressBar2);
        bAnna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                if((keyAnna!=null && keyAnna.equals(Build.BRAND + " " + Build.MODEL  ))||keyMario.equals(Build.BRAND + " " + Build.MODEL )){
                    SaveLog("Log:", "LogIn Anna");
                reiniciarValors();
                Login(Anna, clave);
                reiniciarValors();}
                else {
                    Toast.makeText(LoginActivity.this, "No se ha reconocido tu cara", Toast.LENGTH_SHORT).show();
                    SaveLog("Log:","LogIn Fallido Anna desde "+Build.BRAND + " " + Build.MODEL);
                }
                }catch (Exception e){
                   SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e));
                }
            }
        });
        LogMario = findViewById(R.id.LogMario);
        bMario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                if(keyMario!=null && keyMario.equals(Build.BRAND + " " + Build.MODEL )){
                    SaveLog("Log:","LogIn Mario");
                    reiniciarValors();
                    Login(Mario, clave);
                    reiniciarValors();}
                else {
                    Toast.makeText(LoginActivity.this, "No se ha reconocido tu cara", Toast.LENGTH_SHORT).show();
                    SaveLog("Log:","LogIn Fallido Mario desde "+Build.BRAND + " " + Build.MODEL);
                }}catch (Exception e){
                    SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e));
                }
        }

        });

        bLaurita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                if((keyLaurita!=null && keyLaurita.equals(Build.BRAND + " " + Build.MODEL ))||keyMario.equals(Build.BRAND + " " + Build.MODEL )){
                    SaveLog("Log:", "LogIn Laurita");
                    reiniciarValors();
                    Login(Laurita, clave);
                    reiniciarValors();}
                else {
                    Toast.makeText(LoginActivity.this, "No se ha reconocido tu cara", Toast.LENGTH_SHORT).show();
                    SaveLog("Log:" ,"LogIn Fallido Laurita desde "+Build.BRAND + " " + Build.MODEL);
                }}catch (Exception e){
                    SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e));
                }
            }
        });
        blauron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                if((keyLauron!=null && keyLauron.equals(Build.BRAND + " " + Build.MODEL ))||keyMario.equals(Build.BRAND + " " + Build.MODEL )){
                    SaveLog("Log:","LogIn Lauron");
                    reiniciarValors();
                    Login(Lauron, clave);
                    reiniciarValors();}
                else {
                    Toast.makeText(LoginActivity.this, "No se ha reconocido tu cara", Toast.LENGTH_SHORT).show();
                    SaveLog("Log:","LogIn Fallido Lauron desde "+Build.BRAND + " " + Build.MODEL);
                } }
                    catch (Exception e){
                        SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e));
                }
            }
        });
        bBlanca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                if((keyBlanca!=null && keyBlanca.equals(Build.BRAND + " " + Build.MODEL ))||keyMario.equals(Build.BRAND + " " + Build.MODEL )){
                    SaveLog("Log:","LogIn Blanca");
                    reiniciarValors();
                    Login(Blanca, clave);
                    reiniciarValors();}
                else {
                    Toast.makeText(LoginActivity.this, "No se ha reconocido tu cara", Toast.LENGTH_SHORT).show();
                    SaveLog("Log:","LogIn Fallido Blanca desde "+Build.BRAND + " " + Build.MODEL);
                }}catch (Exception e){
                    SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e));
                }
            }
        });
        InicialitzarContrasenyes();

        }catch (Exception e){
            SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e));
        }
    }

    private void InicialitzarContrasenyes() {
       database = FirebaseDatabase.getInstance();
       String prog = "";
       if(programador){
           prog = "programador";
       }
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    keyAnna = dataSnapshot.child("users").child("Anna").child("Contraseña").child("KEY").getValue() + "";
                }catch (Exception e)
                {
                    SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e));
                }
                try{
                keyBlanca = dataSnapshot.child("users").child("Blanca").child("Contraseña").child("KEY").getValue() + "";
                }catch (Exception e){    SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e));}
                    try{
                keyLaurita = dataSnapshot.child("users").child("Laurita").child("Contraseña").child("KEY").getValue() + "";
                }catch (Exception e){     SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e));}
                try{
                    keyMario =dataSnapshot.child("users").child("Mario").child("Contraseña").child("KEY").getValue() + "";
                }catch (Exception e){    SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e)); }
                try{
                    keyLauron= dataSnapshot.child("users").child("Lauron").child("Contraseña").child("KEY").getValue() + "";
                }catch (Exception e){     SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e));}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                SaveLog("ERROR: ",error.getMessage());
            }
        })
        ;
    }

    private void inicializarSharedPreferences() {
        programador = preferences.getBoolean("Programador", false);
        ModoProgramador.setChecked(programador);

    }

    private void reiniciarValors() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fondo", "");
        editor.putString("total4", 0 + "");
        editor.putString("total3", 0 + "");
        editor.putString("total2", 0 + "");
        editor.putString("total1", 0 + "");
        editor.putString("total4_Ant", 0 + "");
        editor.putString("total3_Ant", 0 + "");
        editor.putString("total2_Ant", 0 + "");
        editor.putString("total1_Ant", 0 + "");
        editor.putInt("ColorTexto", Color.BLACK);
        editor.putInt("ColorMateriales", Color.WHITE);
        editor.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void Login(@NonNull String email, @NonNull String password) {
        bar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Autentificando",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                            startActivity(intent);
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(@Nullable FirebaseUser currentUser) {
        if (currentUser != null) {
            Toast.makeText(this, ("creado el usuario " + currentUser.getEmail() + "ui: " + currentUser.getUid()), Toast.LENGTH_LONG);
            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public  void SaveLog(String log,String message)
    {
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss ");
        DateFormat hora = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss ");
        String forFecha = df.format(Calendar.getInstance().getTime());
        Context context = LoginActivity.this;
        if(myRef!=null)
        {
            try{
                myRef.child("LOG").child(hora.format(Calendar.getInstance().getTime())).child("Titulo").setValue(log + " "+ "Login" +", Version: "+context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName);}
            catch (Exception e){myRef.child("LOG").child(hora.format(Calendar.getInstance().getTime())).child("Titulo").setValue(log + " "+ "Login");}
            myRef.child("LOG").child(hora.format(Calendar.getInstance().getTime())).child("Mensaje").setValue(message);
            myRef.child("LOG").child(hora.format(Calendar.getInstance().getTime())).child("Fecha").setValue(forFecha);
        }

    }

}
