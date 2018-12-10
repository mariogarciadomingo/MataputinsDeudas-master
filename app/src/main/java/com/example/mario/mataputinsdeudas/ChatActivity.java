package com.example.mario.mataputinsdeudas;




import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    static DatabaseReference myRef, Ref;
    FirebaseDatabase database;
    ValueEventListener event;
    String tu;
    String el;
    private ArrayList<String> ids;
    private CircleImageView perfil;
    private ImageView back;
    private TextView nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            tu = getIntent().getStringExtra("tu");
            el = getIntent().getStringExtra("Usuario");
            setContentView(R.layout.activity_chat);
            final ChatView chatView = (ChatView) findViewById(R.id.chat_view);
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Chats");
            Ref = database.getReference("users");
            perfil = findViewById(R.id.profile_image);
            nom = findViewById(R.id.TVnom);
            nom.setText(el);
            int pos = getIntent().getIntExtra("pos", 0);
            if (pos == 0) {
                perfil.setImageDrawable(PrincipalActivity.ImUsuario1.getDrawable());
            } else if (pos == 1) {
                perfil.setImageDrawable(PrincipalActivity.ImUsuario2.getDrawable());
            } else if (pos == 2) {
                perfil.setImageDrawable(PrincipalActivity.ImUsuario3.getDrawable());
            } else if (pos == 3) {
                perfil.setImageDrawable(PrincipalActivity.ImUsuario4.getDrawable());
            }
            back = findViewById(R.id.BTback);
            Ref.child(tu).child("Leidos").child(el).removeValue();
            event = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if(ids==null)
                        {
                            ids=new ArrayList<>();
                        }
                        //chatView.clearMessages();
                        ChatMessage mens;
                        for (DataSnapshot fechas : dataSnapshot.child(tu).child(el).getChildren()) {
                            if(!ids.contains(fechas.getKey())){
                            if (fechas.hasChild("Mensaje") && (fechas.hasChild("Tipo")) && (fechas.hasChild("Fecha"))) {
                                String mensaje = fechas.child("Mensaje").getValue(String.class);
                                long fecha = fechas.child("Fecha").getValue(Long.class);
                                String tipo = fechas.child("Tipo").getValue(String.class);
                                String id = fechas.getKey();
                                if (tipo.equals("SENT")) {
                                    mens = new ChatMessage(mensaje, fecha, ChatMessage.Type.SENT);
                                } else
                                    mens = new ChatMessage(mensaje, fecha, ChatMessage.Type.RECEIVED);
                                chatView.addMessage(mens);
                                ids.add(id);
                                }

                            }
                        }


                    } catch (Exception e) {
                        PrincipalActivity.SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    PrincipalActivity.SaveLog("ERROR: ", error.getMessage() + "en ChatActivity");
                    // Failed to read value
                    //Log.w(TAG, "Failed to read value.", error.toException());
                }
            };
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myRef.removeEventListener(event);
                    finish();

                }
            });

            myRef.addValueEventListener(event);

            chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
                @Override
                public boolean sendMessage(ChatMessage chatMessage) {
                    try {
                        DateFormat dh = new SimpleDateFormat("yyyyMMddHHmmss ");
                        Date fecha = Calendar.getInstance().getTime();
                        String id = dh.format(fecha);

                        int cont = 0;
                        //while (cont<100) {
                        ids.add(id + chatMessage.getMessage().hashCode() + cont);
                        myRef.child(tu).child(el).child(id + chatMessage.getMessage().hashCode() + cont).child("Mensaje").setValue(chatMessage.getMessage());
                        myRef.child(tu).child(el).child(id + chatMessage.getMessage().hashCode() + cont).child("Tipo").setValue(chatMessage.getType());
                        myRef.child(tu).child(el).child(id + chatMessage.getMessage().hashCode() + cont).child("Fecha").setValue(chatMessage.getTimestamp());
                        myRef.child(el).child(tu).child(id + chatMessage.getMessage().hashCode() + cont).child("Mensaje").setValue(chatMessage.getMessage());// perform actual message sending
                        myRef.child(el).child(tu).child(id + chatMessage.getMessage().hashCode() + cont).child("Tipo").setValue(ChatMessage.Type.RECEIVED);
                        myRef.child(el).child(tu).child(id + chatMessage.getMessage().hashCode() + cont).child("Fecha").setValue(chatMessage.getTimestamp());
                        Ref.child(el).child("Leidos").child(tu).setValue(false);
                        ////}
                        PrincipalActivity.SaveLog("Log: ", "Mensaje enviado " + tu + " a " + el);
                        return true;
                    } catch (Exception e) {
                        PrincipalActivity.SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                        return false;
                    }
                }
            });


        } catch (Exception e) {
            PrincipalActivity.SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myRef.removeEventListener(event);

    }

    @Override
    protected void onResume() {
        super.onResume();
        myRef.addValueEventListener(event);
    }

}
