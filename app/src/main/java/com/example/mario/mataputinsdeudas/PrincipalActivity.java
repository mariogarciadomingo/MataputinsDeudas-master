package com.example.mario.mataputinsdeudas;

import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glidebitmappool.GlideBitmapFactory;
import com.glidebitmappool.GlideBitmapPool;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.msoftworks.easynotify.EasyNotify;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.media.session.PlaybackState.ACTION_PLAY;


public class PrincipalActivity extends AppCompatActivity {
    final static int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = PrincipalActivity.class.getSimpleName();
    public static ImageView ImUsuario1, ImUsuario2, ImUsuario3, ImUsuario4, fons, Imtu, ImStat;
    public static TextView tot1, tot2, tot3, tot4, total, tot1ant, tot2ant, tot3ant, tot4ant, msg;
    public static double total1, total2, total3, total4;
    public static String[] usuarios;
    public static TextView usuario1, usuario2, usuario3, usuario4, tu, titolTotal, moroso;
    public static File dir;
    static Button Endeudar1, Endeudar2, Endeudar3, Endeudar4, Perdonar1, Perdonar2, Perdonar3, Perdonar4, Historial;
    static DatabaseReference myRef, ref, versionref, dblog;
    static String nom;
    static Context context;
    static EasyNotify easyNotify;
    static Double temp = 0.0;
    static String tempString = "";
    final int PICK_IMAGE_REQUEST_PORFILE = 2;
    final String Anna = "anna@gmail.com";
    final String Laurita = "laurita@gmail.com";
    final String Lauron = "lauron@gmail.com";
    final String Mario = "mario@gmail.com";
    final String Blanca = "blanca@gmail.com";
    public ImageButton Btupdate;
    public int color = 0;
    boolean programador = false;
    EditText EdDinero, Edescripcion;
    FirebaseDatabase database;
    LinearLayout LinearUsuario1, LinearUsuario2, LinearUsuario3, LinearUsuario4, LinearUsurios;
    ConstraintLayout ConstrainUsuario, Fondo;
    SwipeRefreshLayout swiperefresh;
    static String token = "";
    boolean versionAntigua = false;
    String url = "";
    @Nullable

    String Pfondo;
    SharedPreferences preferences;
    //grupal
    ImageButton btgrupal, btPersonalzar;
    private FirebaseAuth mAuth;
    @Nullable
    private FirebaseUser user;
    private ValueEventListener listener;
    static Date fecha = Calendar.getInstance().getTime();
    static DateFormat df = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss ");
    static DateFormat dh = new SimpleDateFormat("yyyyMMddHHmmss ");
    static MediaPlayer mediaPlayer;
    static BitmapFactory.Options o = new BitmapFactory.Options();
    static BitmapFactory.Options o2 = new BitmapFactory.Options();
    public static void DesarFireDisseny(int colorText, int colorMaterials, boolean usu1, boolean usu2, boolean usu3, boolean usu4, boolean total, boolean imatges, boolean chao) {
        SaveLog("Log:", "Cambio Diseño(" + nom + ")");
        myRef.child(nom).child("Disseny").child("TextColor").setValue(colorText);
        myRef.child(nom).child("Disseny").child("TextMaterials").setValue(colorMaterials);
        myRef.child(nom).child("Disseny").child("Usuario1").setValue(usu1);
        myRef.child(nom).child("Disseny").child("Usuario2").setValue(usu2);
        myRef.child(nom).child("Disseny").child("Usuario3").setValue(usu3);
        myRef.child(nom).child("Disseny").child("Usuario4").setValue(usu4);
        myRef.child(nom).child("Disseny").child("Total").setValue(total);
        myRef.child(nom).child("Disseny").child("Imatges").setValue(imatges);
        myRef.child(nom).child("Disseny").child("Chao").setValue(chao);
        if(chao){
       // myRef.child(nom).child("Disseny").child("TextColor").setValue(Color.RGBToHSV(9,9,9,100));
       // myRef.child(nom).child("Disseny").child("TextMaterials").setValue(Color.RED);
        }
    }

    public static void firmar(ArrayList<Integer> usuarios, EditText descripcion, EditText dinero) {
        SaveLog("Log:", "Deuda grupal (" + nom + ")");
        tempString = descripcion.getText().toString();
        temp = Double.parseDouble(dinero.getText().toString()) / usuarios.size();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i) != -1) {
                dinero.setText(temp + "");
                descripcion.setText(tempString);
                if (usuarios.get(i) == 0)
                    deuda(dinero, usuarios.get(i), total1, descripcion);
                else if (usuarios.get(i) == 1)
                    deuda(dinero, usuarios.get(i), total2, descripcion);
                else if (usuarios.get(i) == 2)
                    deuda(dinero, usuarios.get(i), total3, descripcion);
                else if (usuarios.get(i) == 3)
                    deuda(dinero, usuarios.get(i), total4, descripcion);
            }
        }
    }

    private static void deuda(EditText dinero, int usuario, double total, @NonNull EditText concepto) {
        if (!dinero.getText().toString().equals("")) {
            if (Double.parseDouble(dinero.getText().toString()) > 0) {
                //int i = 0;
                //while (i<200){
                temp = Double.parseDouble(dinero.getText().toString());
                Long tempDinero = Math.round(temp * 100);
                temp = Double.parseDouble(tempDinero.toString()) / 100;
                Date fecha = Calendar.getInstance().getTime();
                String conceptoText = concepto.getText().toString();

                String id = dh.format(fecha);
                String forFecha = df.format(fecha);
                SaveLog("Log:", "Deuda de " + nom + " para " + usuarios[usuario] + " de " + total + "€");
                SaveLog("Alert:", "Hay que eliminar la version antigua cuando todos tengan la version 7 en Deuda");
                //antigua
                myRef.child(nom).child(usuarios[usuario]).setValue(total + temp);
                myRef.child(nom).child(usuarios[usuario] + "_Anterior").setValue(temp);
                myRef.child(usuarios[usuario]).child(nom).setValue(-(total + temp));
                myRef.child(usuarios[usuario]).child(nom + "_Anterior").setValue(-temp);
                //nueva
                myRef.child(nom).child("Deudas").child(usuarios[usuario]).child("Valor").setValue(total + temp);
                myRef.child(nom).child("Deudas").child(usuarios[usuario]).child("Valor_Anterior").setValue(temp);
                myRef.child(usuarios[usuario]).child("Deudas").child(nom).child("Valor").setValue(-(total + temp));
                myRef.child(usuarios[usuario]).child("Deudas").child(nom).child("Valor_Anterior").setValue(-(temp));

                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("descripcion").setValue(usuarios[usuario] + " te debe " + conceptoText);
                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("valor").setValue(temp);
                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("usuario").setValue(usuarios[usuario]);
                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("fecha").setValue(forFecha);
                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("modelo").setValue(Build.BRAND + " " + Build.MODEL);
                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("totalAnterior").setValue(total);

                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("descripcion").setValue("Debes a " + nom + " " + conceptoText);
                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("valor").setValue(-temp);
                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("modelo").setValue(Build.BRAND + " " + Build.MODEL);
                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("usuario").setValue(nom);
                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("totalAnterior").setValue(total);
                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("fecha").setValue(forFecha);
                if (mediaPlayer != null)
                    mediaPlayer.release();
                if (usuarios[usuario].equals("Anna")) {
                    mediaPlayer = MediaPlayer.create(context, R.raw.itocabron);
                    mediaPlayer.start();
                } else {
                    mediaPlayer = MediaPlayer.create(context, R.raw.caja);
                    mediaPlayer.start();

                }
                if (conceptoText.toLowerCase().contains("porros") | conceptoText.toLowerCase().contains("pirri") | conceptoText.toLowerCase().contains("porro") | conceptoText.toLowerCase().contains("jimmy") | conceptoText.toLowerCase().contains("maria") | conceptoText.toLowerCase().contains("jimy")) {
                    if (usuario1.getText().toString() != "Jimmy") {
                        fons.setImageResource(R.drawable.jimmy);
                        usuario1.setText("Jimmy");
                        usuario2.setText("Maria");
                        usuario3.setText("Felicity");
                        usuario4.setText("Asobob");
                        ImUsuario1.setImageResource(R.drawable.jimmy1);
                        ImUsuario2.setImageResource(R.drawable.jimmy2);
                        ImUsuario3.setImageResource(R.drawable.jimmy3);
                        ImUsuario4.setImageResource(R.drawable.jimmy4);
                        Historial.setText("Jimmear");
                    }

                } else if (conceptoText.toLowerCase().contains("alcol") | conceptoText.toLowerCase().contains("alcohol") | conceptoText.toLowerCase().contains("gin") | conceptoText.toLowerCase().contains("gim") | conceptoText.toLowerCase().contains("ginebra") | conceptoText.toLowerCase().contains("vodka") | conceptoText.toLowerCase().contains("cubata") | conceptoText.toLowerCase().contains("bebida") | conceptoText.toLowerCase().contains("birra") | conceptoText.toLowerCase().contains("litrona") | conceptoText.toLowerCase().contains("sangria") | conceptoText.toLowerCase().contains("malta") | conceptoText.toLowerCase().contains("cerveza") | conceptoText.toLowerCase().contains("alcolito")) {
                    if (usuario1.getText().toString() != "Putinov") {
                        fons.setImageResource(R.drawable.bebidas);
                        usuario1.setText("Putinov");
                        usuario2.setText("Sussynov");
                        usuario3.setText("Russnov");
                        usuario4.setText("Putibob");
                        ImUsuario1.setImageResource(R.drawable.borracho1);
                        ImUsuario2.setImageResource(R.drawable.borracho2);
                        ImUsuario3.setImageResource(R.drawable.borracho3);
                        ImUsuario4.setImageResource(R.drawable.borracho4);
                        Historial.setText("Esta noche Fiesta!");
                    }
                }
                // i++;}
                dinero.setText("");
                concepto.setText("");

            }
        }
    }

    public static void SaveLog(String log, String message) {


        tempString = df.format(Calendar.getInstance().getTime());
        if (dblog != null) {
            try {
                dblog.child("LOG").child(dh.format(Calendar.getInstance().getTime())).child("Titulo").setValue(log + " " + nom + ", Version: " + context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName);
            } catch (Exception e) {
                dblog.child("LOG").child(dh.format(Calendar.getInstance().getTime())).child("Titulo").setValue(log + " " + nom);
            }
            dblog.child("LOG").child(dh.format(Calendar.getInstance().getTime())).child("Mensaje").setValue(message);
            dblog.child("LOG").child(dh.format(Calendar.getInstance().getTime())).child("Fecha").setValue(tempString);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            GlideBitmapPool.initialize(10 * 1024 * 1024);
            setContentView(R.layout.activity_principal);


            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(PrincipalActivity.this, LoginActivity.class));
                System.exit(0);
            }
            FindViews();
            OnClicks();
            String prog = "";
            carregarNoms();
            Declaraciones();
            inicializarSharedPreferences();
            if (programador) {
                prog = "programador/";
                tu.setText(tu.getText() + "(Test)");
            } else {
                carregarImatgesMemoria();
            }
            myRef = database.getReference(prog + "users");
            ref = database.getReference(prog + "users/" + nom);
            versionref = database.getReference("version");
            dblog = database.getReference();
            SaveLog("Log:", "Session Iniciada " + nom);
            gestorDatos();
            CambiarColor();
            CargarConfiguracion();
            token = "Mario";
            easyNotify = new EasyNotify("AAAAbhbdXg8:APA91bFkAzHEpGtqQ5VqFtIJ9OlX0UeIHeg9HdCkQr4-5OF_4YRqb3w4rD8lSkHeY16WawXhSjRTsbNvFVTh6rUOc7GnrxhgVexHbH3XAL7Pg5vzMz_myqL_AYlW0ATH11f9zoF2Spj3");
            easyNotify.setSendBy(EasyNotify.TOPIC);
        } catch (Exception e) {
            SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
        }
    }
    public static void EnviarNotificacion(String titulo,String Mensaje,String el)
    {
        try {


        easyNotify.setTitle("YOUR_TITLE_STRING");
        easyNotify.setBody(Mensaje);
        easyNotify.setClickAction("PrincipalActivity");
        easyNotify.setSound("default");
        easyNotify.setTopic(token);
        easyNotify.nPush();
            easyNotify.setEasyNotifyListener(new EasyNotify.EasyNotifyListener() {
                @Override
                public void onNotifySuccess(String s) {
                   // Toast.makeText(PrincipalActivity.this, s, Toast.LENGTH_SHORT).show();
                    SaveLog("Alert: ", s + token);
                }

                @Override
                public void onNotifyError(String s) {
                    SaveLog("ERROR: ", s + token);
            }

            });
        }
        catch (Exception e){SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));}
    }

    private void CargarConfiguracion() {
        boolean imatges = preferences.getBoolean("Imagenes", true);
        ImUsuario1.setVisibility(Visibilidad(imatges));
        ImUsuario2.setVisibility(Visibilidad(imatges));
        ImUsuario3.setVisibility(Visibilidad(imatges));
        ImUsuario4.setVisibility(Visibilidad(imatges));
        boolean usu1 = preferences.getBoolean("Usuario1Bool", true);
        boolean usu2 = preferences.getBoolean("Usuario2Bool", true);
        boolean usu3 = preferences.getBoolean("Usuario3Bool", true);
        boolean usu4 = preferences.getBoolean("Usuario4Bool", true);
        boolean totalBool = preferences.getBoolean("TotalBool", true);
        LinearUsuario1.setVisibility(Visibilidad(usu1));
        LinearUsuario2.setVisibility(Visibilidad(usu2));
        LinearUsuario3.setVisibility(Visibilidad(usu3));
        LinearUsuario4.setVisibility(Visibilidad(usu4));
        titolTotal.setVisibility(Visibilidad(totalBool));
        total.setVisibility(Visibilidad(totalBool));


    }

    private void Declaraciones() {

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        context = this;
        if (!preferences.getBoolean("SilencioOA", false)) {
            mediaPlayer = MediaPlayer.create(context, R.raw.ao);
            mediaPlayer.start();
        }

        dir = wrapper.getDir("Images", MODE_PRIVATE);


        database = FirebaseDatabase.getInstance();
        Pfondo = preferences.getString("fondo", "");
        try {
            if (Pfondo != "") {

                fons.setImageBitmap(GlideBitmapFactory.decodeFile(Pfondo));
            } else {
                try {

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference pathReference = storageRef.child(tu.getText().toString()).child("fondo.jpg");
                    final File dir = wrapper.getDir("Images", MODE_PRIVATE);
                    final File fondo = new File(dir, tu.getText().toString() + "fondo" + ".jpg");
                    swiperefresh.setRefreshing(true);
                    pathReference.getFile(fondo).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            fons.setImageBitmap(GlideBitmapFactory.decodeFile(fondo.getAbsolutePath()));
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("fondo", fondo.getAbsolutePath());
                            editor.commit();
                            swiperefresh.setRefreshing(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            swiperefresh.setRefreshing(false);
                        }
                    });

                } catch (Exception e) {
                    SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                }
            }
        } catch (Exception e) {
            SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
        }
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        LinearUsurios.startAnimation(animation);

    }

    private void OnClicks() {

        ImUsuario1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalActivity.this, ChatActivity.class).putExtra("Usuario", usuarios[0]).putExtra("tu", nom).putExtra("pos", 0), ActivityOptions.makeScaleUpAnimation(ImUsuario1, 0, 0, 400, 400).toBundle());

            }
        });
        ImUsuario2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalActivity.this, ChatActivity.class).putExtra("Usuario", usuarios[1]).putExtra("tu", nom).putExtra("pos", 1), ActivityOptions.makeScaleUpAnimation(ImUsuario2, 0, 0, 400, 400).toBundle());

            }
        });
        ImUsuario3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalActivity.this, ChatActivity.class).putExtra("Usuario", usuarios[2]).putExtra("tu", nom).putExtra("pos", 2), ActivityOptions.makeScaleUpAnimation(ImUsuario3, 0, 0, 400, 400).toBundle());

            }
        });
        ImUsuario4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalActivity.this, ChatActivity.class).putExtra("Usuario", usuarios[3]).putExtra("tu", nom).putExtra("pos", 3), ActivityOptions.makeScaleUpAnimation(ImUsuario4


                        , 0, 0, 400, 400).toBundle());

            }
        });
        Btupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadData();
            }

        });


        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        SaveLog("Log:", "Refresh(" + nom + ")");

                        if (!programador) {
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            swiperefresh.setRefreshing(false);
                                        }
                                    });
                                }
                            }, 6000);

                            descarregarImatges();
                        } else
                            swiperefresh.setRefreshing(false);
                    }
                }
        );
        tu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    SaveLog("Log:", "Cerrando Sesión (" + nom + ")");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("fondo", "");
                    editor.commit();
                    user = null;
                    mAuth.signOut();
                    mAuth.signOut();
                    mAuth = null;
                    myRef.removeEventListener(listener);
                    //startActivity(new Intent(PrincipalActivity.this, LoginActivity.class), ActivityOptions.makeScaleUpAnimation(tu, 0, 0, 400, 400).toBundle());
                    //android.os.Process.killProcess(android.os.Process.myPid());
                    finish();


                } catch (Exception e) {
                    SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                    e.printStackTrace();
                }
                return false;
            }
        });
        Historial.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                EscogerFondo();
                return true;
            }
        });
        btPersonalzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveLog("Log:", " Abrir Personalizar(" + nom + ")");
                startActivity(new Intent(PrincipalActivity.this, PersonalizarActivity.class), ActivityOptions.makeScaleUpAnimation(btPersonalzar, 0, 0, 400, 400).toBundle());
            }
        });
        Historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveLog("Log:", "Abrir Historial (" + nom + ")");
                startActivity(new Intent(PrincipalActivity.this, HistorialActivity.class), ActivityOptions.makeScaleUpAnimation(Historial, 0, 0, 400, 400).toBundle());
            }
        });
        Imtu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveLog("Log:", "Cambiando Foto de perfil(" + nom + ")");
                startActivityForResult(Intent.createChooser(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), "Selecciona una imagen"), PICK_IMAGE_REQUEST_PORFILE);
            }
        });
        Endeudar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdDinero = findViewById(R.id.EdDinero4);
                Edescripcion= findViewById(R.id.Edescripcion4);
                deuda(EdDinero, 3, total4, Edescripcion);
                ConstrainUsuario = findViewById(R.id.Usuario4Layout);
                AnimarUsuarios(false, ConstrainUsuario);
            }
        });
        Perdonar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdDinero = findViewById(R.id.EdDinero4);
                Edescripcion= findViewById(R.id.Edescripcion4);
                quitarDeuda(EdDinero, 3, total4, Edescripcion);
                ConstrainUsuario = findViewById(R.id.Usuario4Layout);
                AnimarUsuarios(false, ConstrainUsuario);
            }

        });
        LinearUsuario4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstrainUsuario = findViewById(R.id.Usuario4Layout);
                if (ConstrainUsuario.getVisibility() == View.GONE) {
                    AnimarUsuarios(true, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario3Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario2Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario1Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                } else {
                    AnimarUsuarios(false, ConstrainUsuario);
                }
                if (total4 < 0.01) {
                    Perdonar4.setVisibility(View.INVISIBLE);
                } else
                    Perdonar4.setVisibility(View.VISIBLE);
            }
        });
        Endeudar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdDinero = findViewById(R.id.EdDinero3);
                Edescripcion= findViewById(R.id.Edescripcion3);
                deuda(EdDinero, 2, total3, Edescripcion);
                ConstrainUsuario = findViewById(R.id.Usuario3Layout);
                AnimarUsuarios(false, ConstrainUsuario);
            }


        });
        Perdonar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdDinero = findViewById(R.id.EdDinero3);
                Edescripcion= findViewById(R.id.Edescripcion3);
                quitarDeuda(EdDinero, 2, total3, Edescripcion);
                ConstrainUsuario = findViewById(R.id.Usuario3Layout);
                AnimarUsuarios(false, ConstrainUsuario);
            }
        });
        LinearUsuario3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstrainUsuario = findViewById(R.id.Usuario3Layout);
                if (ConstrainUsuario.getVisibility() == View.GONE) {
                    AnimarUsuarios(true, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario4Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario2Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario1Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                } else {
                    AnimarUsuarios(false, ConstrainUsuario);
                }
                if (total3 < 0.01) {
                    Perdonar3.setVisibility(View.INVISIBLE);
                } else
                    Perdonar3.setVisibility(View.VISIBLE);
            }
        });
        Endeudar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdDinero = findViewById(R.id.EdDinero2);
                Edescripcion= findViewById(R.id.Edescripcion2);
                deuda(EdDinero, 1, total2, Edescripcion);
                ConstrainUsuario = findViewById(R.id.Usuario2Layout);

                AnimarUsuarios(false, ConstrainUsuario);
            }
        });
        Perdonar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdDinero = findViewById(R.id.EdDinero2);
                Edescripcion= findViewById(R.id.Edescripcion2);
                quitarDeuda(EdDinero, 1, total2, Edescripcion);
                ConstrainUsuario = findViewById(R.id.Usuario2Layout);
                AnimarUsuarios(false, ConstrainUsuario);
            }
        });
        LinearUsuario2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ConstrainUsuario = findViewById(R.id.Usuario2Layout);
                if (ConstrainUsuario.getVisibility() == View.GONE) {
                    AnimarUsuarios(true, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario3Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario4Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario1Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                } else {
                    AnimarUsuarios(false, ConstrainUsuario);
                }
                if (total2 < 0.01) {
                    Perdonar2.setVisibility(View.INVISIBLE);
                } else
                    Perdonar2.setVisibility(View.VISIBLE);
            }
        });
        Endeudar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdDinero = findViewById(R.id.EdDinero);
                Edescripcion= findViewById(R.id.Edescripcion1);
                deuda(EdDinero, 0, total1, Edescripcion);
                ConstrainUsuario = findViewById(R.id.Usuario1Layout);
                AnimarUsuarios(false, ConstrainUsuario);
            }
        });
        Perdonar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdDinero = findViewById(R.id.EdDinero);
                Edescripcion= findViewById(R.id.Edescripcion1);
                quitarDeuda(EdDinero, 0, total1, Edescripcion);
                ConstrainUsuario = findViewById(R.id.Usuario1Layout);
                AnimarUsuarios(false, ConstrainUsuario);
            }

        });
        LinearUsuario1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ConstrainUsuario = findViewById(R.id.Usuario1Layout);
                if (ConstrainUsuario.getVisibility() == View.GONE) {
                    AnimarUsuarios(true, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario3Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario2Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                    ConstrainUsuario = findViewById(R.id.Usuario4Layout);
                    AnimarUsuarios(false, ConstrainUsuario);
                } else {
                    AnimarUsuarios(false, ConstrainUsuario);
                }
                if (total1 < 0.01) {
                    Perdonar1.setVisibility(View.INVISIBLE);
                } else
                    Perdonar1.setVisibility(View.VISIBLE);
            }
        });
        btgrupal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveLog("Log:", "Abrir Grupal(" + nom + ")");
                startActivity(new Intent(PrincipalActivity.this, GrupalActivity.class), ActivityOptions.makeScaleUpAnimation(btgrupal, 0, 0, 400, 400).toBundle());
            }
        });

    }

    private void FindViews() {


        Btupdate = findViewById(R.id.UpdateBt);
        moroso = findViewById(R.id.txtMorosos);
        swiperefresh = findViewById(R.id.swiperefresh);
        Fondo = findViewById(R.id.fondo);
        Historial = findViewById(R.id.btHistorial);
        Imtu = findViewById(R.id.imtu);
        fons = findViewById(R.id.imageView);
        tu = findViewById(R.id.Nombre);
        titolTotal = findViewById(R.id.TitolTotal);
        btPersonalzar = findViewById(R.id.btPersonalizar);
        LinearUsurios = findViewById(R.id.LinearUsuarios);
        ImUsuario1 = findViewById(R.id.ImUsuario1);
        ImUsuario2 = findViewById(R.id.ImUsuario2);
        ImUsuario3 = findViewById(R.id.ImUsuario3);
        ImUsuario4 = findViewById(R.id.ImUsuario4);
        tot1 = findViewById(R.id.Total1);
        tot2 = findViewById(R.id.Total2);
        tot3 = findViewById(R.id.Total3);
        tot4 = findViewById(R.id.Total4);
        total = findViewById(R.id.total);
        Endeudar4 = findViewById(R.id.Endeudar4);

        Perdonar4 = findViewById(R.id.Pagado4);

        usuario4 = findViewById(R.id.Usuario4);
        LinearUsuario4 = findViewById(R.id.LinearUsuario4);

        Endeudar3 = findViewById(R.id.Endeudar3);

        Perdonar3 = findViewById(R.id.Pagado3);

        usuario3 = findViewById(R.id.Usuario3);
        LinearUsuario3 = findViewById(R.id.LinearUsuario3);

        Endeudar2 = findViewById(R.id.Endeudar2);

        Perdonar2 = findViewById(R.id.Pagado2);

        usuario2 = findViewById(R.id.Usuario2);
        LinearUsuario2 = findViewById(R.id.LinearUsuario2);

        Endeudar1 = findViewById(R.id.Endeudar1);

        Perdonar1 = findViewById(R.id.Pagado1);

        usuario1 = findViewById(R.id.Usuario1);
        LinearUsuario1 = findViewById(R.id.PLinearUsuario);

        btgrupal = findViewById(R.id.btGrupal);
        tot1ant = findViewById(R.id.AntU1);
        ImStat = findViewById(R.id.ImAnU1);
        tot2ant = findViewById(R.id.antU2);
        tot3ant = findViewById(R.id.antU3);
        tot4ant = findViewById(R.id.antU4);

    }

    public void EscogerFondo() {
        SaveLog("Log:", "Cambiando Fondo (" + nom + ")");
        startActivityForResult(Intent.createChooser(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    private void carregarNoms() {

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (user.getEmail().equals(Anna)) {
            nom = "Anna";
            usuarios = new String[]{"Laurita", "Lauron", "Mario", "Blanca"};
        }
        if (user.getEmail().equals(Laurita)) {
            nom = "Laurita";
            usuarios = new String[]{"Anna", "Lauron", "Mario", "Blanca"};
        }
        if (user.getEmail().equals(Lauron)) {
            nom = "Lauron";
            usuarios = new String[]{"Laurita", "Anna", "Mario", "Blanca"};
        }
        if (user.getEmail().equals(Mario)) {
            nom = "Mario";
            usuarios = new String[]{"Laurita", "Lauron", "Anna", "Blanca"};
        }
        if (user.getEmail().equals(Blanca)) {
            nom = "Blanca";
            usuarios = new String[]{"Laurita", "Lauron", "Anna", "Mario"};
        }

        tu.setText(nom);

        usuario1.setText(usuarios[0]);

        usuario2.setText(usuarios[1]);

        usuario3.setText(usuarios[2]);

        usuario4.setText(usuarios[3]);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Usuario1", usuarios[0]);
        editor.putString("Usuario2", usuarios[1]);
        editor.putString("Usuario3", usuarios[2]);
        editor.putString("Usuario4", usuarios[3]);
        editor.commit();
    }

    private void carregarImatgesMemoria() {
        generarImatges();
    }

    private void generarImatges() {

        if (user.getEmail().equals(Anna)) {

            Imtu.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/anna.jpg"));
            ImUsuario1.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/laurita.jpg"));
            ImUsuario2.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/lauron.jpg"));
            ImUsuario3.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/mario.jpg"));
            ImUsuario4.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/blanca.jpg"));

        }
        if (user.getEmail().equals(Laurita)) {
            Imtu.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/laurita.jpg"));
            ImUsuario1.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/anna.jpg"));
            ImUsuario2.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/lauron.jpg"));
            ImUsuario3.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/mario.jpg"));
            ImUsuario4.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/blanca.jpg"));
        }
        if (user.getEmail().equals(Lauron)) {
            Imtu.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/lauron.jpg"));
            ImUsuario1.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/laurita.jpg"));
            ImUsuario2.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/anna.jpg"));
            ImUsuario3.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/mario.jpg"));
            ImUsuario4.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/blanca.jpg"));
        }
        if (user.getEmail().equals(Mario)) {
            Imtu.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/mario.jpg"));
            ImUsuario1.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/laurita.jpg"));
            ImUsuario2.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/lauron.jpg"));
            ImUsuario3.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/anna.jpg"));
            ImUsuario4.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/blanca.jpg"));
        }
        if (user.getEmail().equals(Blanca)) {
            Imtu.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/blanca.jpg"));
            ImUsuario1.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/laurita.jpg"));
            ImUsuario2.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/lauron.jpg"));
            ImUsuario3.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/anna.jpg"));
            ImUsuario4.setImageBitmap(GlideBitmapFactory.decodeFile(dir + "/mario.jpg"));
        }


    }

    private void inicializarSharedPreferences() {
        if (!preferences.getBoolean("Abierto", false)) {
            descarregarImatges();
        }
        versionAntigua = preferences.getBoolean("VersionAntigua", false);
        programador = preferences.getBoolean("Programador", false);
        total1 = Double.parseDouble(preferences.getString("total1", "0"));
        total2 = Double.parseDouble(preferences.getString("total2", "0"));
        total3 = Double.parseDouble(preferences.getString("total3", "0"));
        total4 = Double.parseDouble(preferences.getString("total4", "0"));
        tot1.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                .format(total1));
        tot2.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                .format(total2));
        tot3.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                .format(total3));
        tot4.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                .format(total4));
        temp = (total1 + total2 + total3 + total4);
        total.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                .format(temp));
        tot1ant.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                .format(Double.parseDouble(preferences.getString("total1_Ant", "0"))));
        tot2ant.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                .format(Double.parseDouble(preferences.getString("total2_Ant", "0"))));
        tot3ant.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                .format(Double.parseDouble(preferences.getString("total3_Ant", "0"))));
        tot4ant.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                .format(Double.parseDouble(preferences.getString("total4_Ant", "0"))));
        tot1ant.setTextColor(ColorNumeros(Double.parseDouble(preferences.getString("total1_Ant", "0"))));
        tot2ant.setTextColor(ColorNumeros(Double.parseDouble(preferences.getString("total2_Ant", "0"))));
        tot3ant.setTextColor(ColorNumeros(Double.parseDouble(preferences.getString("total3_Ant", "0"))));
        tot4ant.setTextColor(ColorNumeros(Double.parseDouble(preferences.getString("total4_Ant", "0"))));

        ImStat = findViewById(R.id.ImAnU1);

        ImStat.setImageResource(EstablirFlecha(Double.parseDouble(preferences.getString("total1_Ant", "0"))));
        ImStat = findViewById(R.id.ImAnU2);

        ImStat.setImageResource(EstablirFlecha(Double.parseDouble(preferences.getString("total2_Ant", "0"))));
        ImStat = findViewById(R.id.imAnU3);

        ImStat.setImageResource(EstablirFlecha(Double.parseDouble(preferences.getString("total3_Ant", "0"))));
        ImStat = findViewById(R.id.imAnU4);

        ImStat.setImageResource(EstablirFlecha(Double.parseDouble(preferences.getString("total4_Ant", "0"))));


        tot1.setTextColor(ColorNumeros(total1));
        tot2.setTextColor(ColorNumeros(total2));
        tot3.setTextColor(ColorNumeros(total3));
        tot4.setTextColor(ColorNumeros(total4));
        total.setTextColor(ColorNumeros(temp));
        AnimarNumeros("0", total1, tot1);
        AnimarNumeros("0", total2, tot2);
        AnimarNumeros("0", total3, tot3);
        AnimarNumeros("0", total4, tot4);
        AnimarNumeros("0", temp, total);
        CargarConfiguracion();

    }

    public int Visibilidad(boolean status) {
        if (status)
            return (View.VISIBLE);
        else
            return (View.GONE);
    }

    private void CambiarColor() {
        int col = preferences.getInt("ColorTexto", Color.BLACK);
        tu.setTextColor(col);
        usuario1.setTextColor(col);
        usuario2.setTextColor(col);
        usuario3.setTextColor(col);
        usuario4.setTextColor(col);
        titolTotal.setTextColor(col);
        col = preferences.getInt("ColorMateriales", Color.WHITE);
        LinearUsuario1.setBackgroundColor(col);
        LinearUsuario2.setBackgroundColor(col);
        LinearUsuario3.setBackgroundColor(col);
        LinearUsuario4.setBackgroundColor(col);
        total.setBackgroundColor(col);
        titolTotal.setBackgroundColor(col);

    }

    private void gestorDatos() {
        listener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    try {
                        if (Boolean.parseBoolean(dataSnapshot.child("Bloqueado").getValue() + "")) {
                            Intent intent = new Intent(PrincipalActivity.this, BannedActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                    }
                    try {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("ColorTexto", Integer.parseInt(dataSnapshot.child("Disseny").child("TextColor").getValue() + ""));
                        editor.putInt("ColorMateriales", Integer.parseInt(dataSnapshot.child("Disseny").child("TextMaterials").getValue() + ""));
                        editor.putBoolean("Usuario1Bool", Boolean.parseBoolean(dataSnapshot.child("Disseny").child("Usuario1").getValue() + ""));
                        editor.putBoolean("Usuario2Bool", Boolean.parseBoolean(dataSnapshot.child("Disseny").child("Usuario2").getValue() + ""));
                        editor.putBoolean("Usuario3Bool", Boolean.parseBoolean(dataSnapshot.child("Disseny").child("Usuario3").getValue() + ""));
                        editor.putBoolean("Usuario4Bool", Boolean.parseBoolean(dataSnapshot.child("Disseny").child("Usuario4").getValue() + ""));
                        editor.putBoolean("TotalBool", Boolean.parseBoolean(dataSnapshot.child("Disseny").child("Total").getValue() + ""));
                        editor.putBoolean("Imagenes", Boolean.parseBoolean(dataSnapshot.child("Disseny").child("Imatges").getValue() + ""));
                        editor.putBoolean("Chao", Boolean.parseBoolean(dataSnapshot.child("Disseny").child("Chao").getValue() + ""));
                        editor.putBoolean("Contraseña", Boolean.parseBoolean(dataSnapshot.child("Contraseña").child("Activada").getValue() + ""));
                        editor.commit();
                        CambiarColor();
                        CargarConfiguracion();
                    } catch (Exception e) {
                        SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                    }
                    int col = preferences.getInt("ColorMateriales", Color.WHITE);

                    total4 = EstablirTotal(dataSnapshot, tot4, 3);
                    if (total4 > 10) {
                        LinearUsuario4.setBackgroundColor(Color.YELLOW);
                    } else {
                        LinearUsuario4.setBackgroundColor(col);
                    }
                    if (total4 > 23) {
                        LinearUsuario4.setBackgroundColor(Color.RED);
                    }
                    total3 = EstablirTotal(dataSnapshot, tot3, 2);
                    if (total3 > 10) {
                        LinearUsuario3.setBackgroundColor(Color.YELLOW);
                    } else {
                        LinearUsuario3.setBackgroundColor(col);
                    }
                    if (total3 > 23) {
                        LinearUsuario3.setBackgroundColor(Color.RED);
                    }
                    total2 = EstablirTotal(dataSnapshot, tot2, 1);
                    if (total2 > 10) {
                        LinearUsuario2.setBackgroundColor(Color.YELLOW);
                    } else {
                        LinearUsuario2.setBackgroundColor(col);
                    }
                    if (total2 > 23) {
                        LinearUsuario2.setBackgroundColor(Color.RED);
                    }
                    total1 = EstablirTotal(dataSnapshot, tot1, 0);
                    if (total1 > 10) {
                        LinearUsuario1.setBackgroundColor(Color.YELLOW);
                    } else {
                        LinearUsuario1.setBackgroundColor(col);
                    }
                    if (total1 > 23) {
                        LinearUsuario1.setBackgroundColor(Color.RED);
                    }
                    total.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                            .format(total1 + total2 + total3 + total4));
                    total.setTextColor(ColorNumeros(total1 + total2 + total3 + total4));
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("total4", total4 + "");
                    editor.putString("total3", total3 + "");
                    editor.putString("total2", total2 + "");
                    editor.putString("total1", total1 + "");
                    editor.putBoolean("Abierto", true);
                    ImStat = findViewById(R.id.imAnU4);

                    editor.putString("total4_Ant", EstablirAnt(dataSnapshot, tot4ant, 3, ImStat, total4) + "");
                    ImStat = findViewById(R.id.imAnU3);

                    editor.putString("total3_Ant", EstablirAnt(dataSnapshot, tot3ant, 2, ImStat, total3) + "");
                    ImStat = findViewById(R.id.ImAnU2);

                    editor.putString("total2_Ant", EstablirAnt(dataSnapshot, tot2ant, 1, ImStat, total2) + "");
                    ImStat = findViewById(R.id.ImAnU1);

                    editor.putString("total1_Ant", EstablirAnt(dataSnapshot, tot1ant, 0, ImStat, total1) + "");
                    editor.commit();
                    try {
                        if (dataSnapshot.child("Leidos").child("foto").getValue() != null) {
                            if (!Boolean.parseBoolean(dataSnapshot.child("Leidos").child("foto").getValue().toString())) {
                                descarregarImatges();
                            }
                        }
                    } catch (Exception e) {
                        SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                    }

                    if (versionAntigua) {

                        Btupdate.setVisibility(View.VISIBLE);
                        try {
                            try {
                                moroso.setText("Actualización Pendiente");
                            } catch (Exception e) {
                                SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                            }
                        } catch (Exception e) {
                            SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                        }
                    } else {
                        Btupdate.setVisibility(View.GONE);
                        Btupdate.setVisibility(View.GONE);
                        moroso.setText("");
                    }
                } catch (Exception e) {
                    SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                }
                try {
                    msg = findViewById(R.id.msgU1);
                    if (Boolean.parseBoolean(dataSnapshot.child("Leidos").child(usuarios[0]).getValue() + "") || !dataSnapshot.child("Leidos").hasChild(usuarios[0])) {

                        msg.setVisibility(View.GONE);
                    } else {
                        msg.setVisibility(View.VISIBLE);
                    }
                    msg = findViewById(R.id.msgU2);
                    if (Boolean.parseBoolean(dataSnapshot.child("Leidos").child(usuarios[1]).getValue() + "") || !dataSnapshot.child("Leidos").hasChild(usuarios[1])) {
                        msg.setVisibility(View.GONE);
                    } else {
                        msg.setVisibility(View.VISIBLE);
                    }
                    msg = findViewById(R.id.msgU3);
                    if (Boolean.parseBoolean(dataSnapshot.child("Leidos").child(usuarios[2]).getValue() + "") || !dataSnapshot.child("Leidos").hasChild(usuarios[2])) {
                        msg.setVisibility(View.GONE);
                    } else {
                        msg.setVisibility(View.VISIBLE);
                    }
                    msg = findViewById(R.id.msgU4);
                    if (Boolean.parseBoolean(dataSnapshot.child("Leidos").child(usuarios[3]).getValue() + "") || !dataSnapshot.child("Leidos").hasChild(usuarios[3])) {
                        msg.setVisibility(View.GONE);
                    } else {
                        msg.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        ref.addValueEventListener(listener);
        versionref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("VersionAntigua", (!dataSnapshot.child("code").getValue().toString().equals(context.getPackageManager()
                            .getPackageInfo(context.getPackageName(), 0).versionName)));
                    editor.commit();
                } catch (Exception e) {
                    Toast.makeText(context, "e", Toast.LENGTH_LONG);
                    SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                }

                url = dataSnapshot.child("Url").getValue() + "";
                inicializarSharedPreferences();
                if (versionAntigua) {

                    Btupdate.setVisibility(View.VISIBLE);
                    try {
                        moroso.setText("Actualización Pendiente");
                    } catch (Exception e) {
                        SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                    }

                } else {
                    moroso.setVisibility(View.GONE);
                    Btupdate.setVisibility(View.GONE);
                    Btupdate.setVisibility(View.GONE);
                }
                versionref = null;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    private Double EstablirTotal(DataSnapshot total, TextView TV, int Usuario) {

        try {
            if (total.child(usuarios[Usuario]).getValue() != null) {
                temp = Double.parseDouble(total.child(usuarios[Usuario]).getValue() + "");
                TV.setTextColor(ColorNumeros(temp));
            } else {
                temp = 0.00;
                TV.setTextColor(Color.GRAY);
            }
        } catch (Exception e) {
            temp = 0.00;
            TV.setTextColor(Color.GRAY);
            SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
        }
        TV.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                .format(temp));
        return temp;
    }

    private Double EstablirAnt(DataSnapshot total, TextView TV, int Usuario, ImageView IM, Double tot) {

        try {
            if (total.child(usuarios[Usuario] + "_Anterior").getValue() != null) {
                temp = (Double.parseDouble(total.child(usuarios[Usuario] + "_Anterior").getValue() + ""));
                TV.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                        .format(temp));
                TV.setTextColor(ColorNumeros(temp));
                IM.setImageResource(EstablirFlecha(temp));
            }
        } catch (Exception e) {
            SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));

        }
        return temp;
    }

    private int EstablirFlecha(Double total) {
        if (total > 0) {
            return R.drawable.arrowup;
        } else if (total < 0)
            return R.drawable.arrowdown;
        else
            return 0;
    }

    private int ColorNumeros(Double total) {
        if (total > 0)
            return Color.BLUE;
        else if (total == 0) {
            return Color.GRAY;
        } else
            return Color.RED;
    }

    private Double AnimarNumeros(String primer, Double segon, final TextView Tvuser) {
        ValueAnimator animator = ValueAnimator.ofFloat(Float.parseFloat(primer + ""), Float.parseFloat(segon + ""));
        animator.setDuration(900);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Tvuser.setText(NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                        .format(Double.parseDouble(animation.getAnimatedValue().toString())));
            }
        });
        animator.start();
        return segon;
    }

    private void AnimarUsuarios(boolean visible, ConstraintLayout usuario) {
        TransitionManager.beginDelayedTransition(Fondo);
        usuario.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void quitarDeuda(EditText dinero, int usuario, double total, @NonNull EditText concepto) {
        if (mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.caja);
        mediaPlayer.start();
        if (!dinero.getText().toString().equals("")) {
            if (Double.parseDouble(dinero.getText().toString()) > 0) {
                if (Double.parseDouble(dinero.getText().toString()) > total) {
                    dinero.setText(total + "");
                }
                temp = Double.parseDouble(dinero.getText().toString());
                Long tempDinero = Math.round(temp * 100);
                temp = Double.parseDouble(tempDinero.toString()) / 100;

                //antiguo
                SaveLog("Alert:", "Hay que eliminar la version antigua cuando todos tengan la version 7 en Deuda");
                myRef.child(nom).child(usuarios[usuario]).setValue(total - temp);
                myRef.child(nom).child(usuarios[usuario] + "_Anterior").setValue(-temp);
                myRef.child(usuarios[usuario]).child(nom).setValue(-(total - temp));
                myRef.child(usuarios[usuario]).child(nom + "_Anterior").setValue(+(temp));

                //nuevo
                myRef.child(nom).child("Deudas").child(usuarios[usuario]).child("Valor").setValue(total - temp);
                myRef.child(nom).child("Deudas").child(usuarios[usuario]).child("Valor_Anterior").setValue(-temp);
                myRef.child(usuarios[usuario]).child("Deudas").child(nom).child("Valor").setValue(-(total - temp));
                myRef.child(usuarios[usuario]).child("Deudas").child(nom).child("Valor_Anterior").setValue(+(temp));

                SaveLog("Log:", "Deuda Perdonada de " + nom + " para " + usuarios[usuario] + " de " + total + "€");

                String id = dh.format(fecha);
                String forFecha = df.format(fecha);
                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("descripcion").setValue(usuarios[usuario] + " te ha pagado " + concepto.getText().toString());
                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("modelo").setValue(Build.BRAND + " " + Build.MODEL);
                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("valor").setValue(temp);
                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("usuario").setValue(usuarios[usuario]);
                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("totalAnterior").setValue(total);
                myRef.child(nom).child("historial").child(id + usuarios[usuario]).child("fecha").setValue(forFecha);

                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("descripcion").setValue("Has pagado " + concepto.getText().toString() + " a " + nom);
                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("valor").setValue(-temp);
                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("modelo").setValue(Build.BRAND + " " + Build.MODEL);
                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("usuario").setValue(nom);
                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("totalAnterior").setValue(total);
                myRef.child(usuarios[usuario]).child("historial").child(id + nom).child("fecha").setValue(forFecha);

                dinero.setText("");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                fons.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                ContextWrapper wrapper = new ContextWrapper(getApplication());
                File file = wrapper.getDir("Images", MODE_PRIVATE);
                file = new File(file, "fondo" + ".jpg");
                OutputStream stream;
                stream = new FileOutputStream(file);
                MediaStore.Images.Media.getBitmap(getContentResolver(), uri).compress(Bitmap.CompressFormat.JPEG, 50, stream);
                stream.flush();
                stream.close();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("fondo", file.getAbsolutePath());
                editor.commit();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                // From our app
                StorageReference storageRef = storage.getReference().child(tu.getText().toString()).child("fondo.jpg");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                MediaStore.Images.Media.getBitmap(getContentResolver(), uri).compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] dat = baos.toByteArray();
                swiperefresh.setRefreshing(true);
                storageRef.putBytes(dat).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        swiperefresh.setRefreshing(false);

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        swiperefresh.setRefreshing(false);
                    }
                });
                file = null;

            } catch (IOException e) {
                SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST_PORFILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                // From our app
                StorageReference storageRef = storage.getReference().child(nom.toLowerCase() + ".jpg");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ThumbnailUtils.extractThumbnail(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), 500, 500).compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] dat = baos.toByteArray();
                Imtu.setImageBitmap(ThumbnailUtils.extractThumbnail(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), 500, 500));
                swiperefresh.setRefreshing(true);
                storageRef.putBytes(dat).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        swiperefresh.setRefreshing(false);
                        myRef.child("Blanca").child("Leidos").child("foto").setValue(false);
                        myRef.child("Anna").child("Leidos").setValue(false);
                        myRef.child("Laurita").child("Leidos").setValue(false);
                        myRef.child("Lauron").child("Leidos").setValue(false);
                        myRef.child("Mario").child("Leidos").setValue(false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        swiperefresh.setRefreshing(false);
                    }
                });

            } catch (IOException e) {
                SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                e.printStackTrace();
            }
        }
    }

    private void descarregarImatges() {

        try {
            SaveLog("Log:", "Descargando Imagenes (" + nom + ")");
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference pathReference = storageRef.child("laurita.jpg");
            ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
            File dir = wrapper.getDir("Images", MODE_PRIVATE);
            pathReference.getFile(new File(dir, "laurita" + ".jpg"));
            pathReference = storageRef.child("anna.jpg");
            pathReference.getFile(new File(dir, "anna" + ".jpg"));
            pathReference = storageRef.child("lauron.jpg");
            pathReference.getFile(new File(dir, "lauron" + ".jpg"));
            pathReference = storageRef.child("blanca.jpg");
            pathReference.getFile(new File(dir, "blanca" + ".jpg"));
            pathReference = storageRef.child("mario.jpg");
            pathReference.getFile(new File(dir, "mario" + ".jpg")).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                   try{
                    generarImatges();
                    myRef.child(nom).child("Leidos").child("foto").setValue(true);
                    //antiguo
                    SaveLog("Alert:", "Hay que eliminar la version antigua cuando todos tengan la version 7 en \n myRef.child(nom).child(\"foto\").removeValue();");
                    myRef.child(nom).child("foto").removeValue();
                    swiperefresh.setRefreshing(false);}catch (Exception e)
                   {
                       SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));

                   }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    swiperefresh.setRefreshing(false);
                    SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));

                }
            });

        } catch (Exception e) {
            SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        inicializarSharedPreferences();
        Pfondo = preferences.getString("fondo", "");
        try {
            if (Pfondo != "") {


                fons.setImageBitmap(GlideBitmapFactory.decodeFile(Pfondo));

            }
        } catch (Exception e) {
            SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
        }
        CambiarColor();
    }

    private void DownloadData() {
        SaveLog("Log:", "Descargando Actualización (" + nom + ")");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

    }





}