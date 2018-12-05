package com.example.mario.mataputinsdeudas;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ChatActivity extends AppCompatActivity {
    FirebaseDatabase database;
    static DatabaseReference myRef;
    private ArrayList<ChatMessage> messages;
    String tu;
    String el;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tu=getIntent().getStringExtra("tu");
        el=getIntent().getStringExtra("Usuario");
        setContentView(R.layout.activity_chat);
        final ChatView chatView = (ChatView) findViewById(R.id.chat_view);
        database = FirebaseDatabase.getInstance();

        myRef = database.getReference( "Chats");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatView.clearMessages();
                if (messages == null)
                messages = new ArrayList<ChatMessage>();
                ChatMessage mens = null;
                for (DataSnapshot fechas : dataSnapshot.child(tu).child(el).getChildren()) {
                    if(fechas.hasChild("Mensaje") && (fechas.hasChild("Tipo")) && (fechas.hasChild("Fecha"))){
                        String mensaje = fechas.child("Mensaje").getValue(String.class);
                        long fecha = fechas.child("Fecha").getValue(Long.class);
                        String tipo = fechas.child("Tipo").getValue(String.class);
                        if(tipo.equals("SENT"))
                    {mens = new ChatMessage(mensaje,fecha,ChatMessage.Type.SENT);}
                   else
                       mens = new ChatMessage(mensaje,fecha,ChatMessage.Type.RECEIVED);}


                           messages.add(mens);
                           chatView.addMessage(mens);


                }



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){
                DateFormat dh = new SimpleDateFormat("yyyyMMddHHmmss ");
                Date fecha = Calendar.getInstance().getTime();
                String id = dh.format(fecha);
                if (messages == null)
                    messages = new ArrayList<ChatMessage>();
                messages.add(new ChatMessage(chatMessage.getMessage(),chatMessage.getTimestamp(),chatMessage.getType()));
                int cont = 0;
                //while (cont<10000) {
                    myRef.child(tu).child(el).child(id + chatMessage.getMessage().hashCode() + cont).child("Mensaje").setValue(chatMessage.getMessage());
                    myRef.child(tu).child(el).child(id + chatMessage.getMessage().hashCode() + cont).child("Tipo").setValue(chatMessage.getType());
                    myRef.child(tu).child(el).child(id + chatMessage.getMessage().hashCode() + cont).child("Fecha").setValue(chatMessage.getTimestamp());
                    myRef.child(el).child(tu).child(id + chatMessage.getMessage().hashCode() + cont).child("Mensaje").setValue(chatMessage.getMessage());// perform actual message sending
                    myRef.child(el).child(tu).child(id + chatMessage.getMessage().hashCode() + cont).child("Tipo").setValue(ChatMessage.Type.RECEIVED);
                    myRef.child(el).child(tu).child(id + chatMessage.getMessage().hashCode() + cont).child("Fecha").setValue(chatMessage.getTimestamp());
                    cont++;
               // }
                return true;
            }
        });


    }

}