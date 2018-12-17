package com.example.mario.mataputinsdeudas;
import android.content.Context;
import android.content.ContextWrapper;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    static DatabaseReference myRef, Ref,myRefOld;
    FirebaseDatabase database;
    ValueEventListener event,event2 ;
    String tu;
    String test = "";
    String el;
    static String mensajes;
    private ArrayList<String> ids;
    static private ArrayList<Mens> messages;
    private CircleImageView perfil;
    private ImageView back;
    private TextView nom;
     ChatView chatView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            messages = null;
            mensajes = null;
            ids = null;
            tu = getIntent().getStringExtra("tu");
            el = getIntent().getStringExtra("Usuario");
            setContentView(R.layout.activity_chat);
            chatView = (ChatView) findViewById(R.id.chat_view);
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference(test+"Chats");
            myRefOld= database.getReference(test+"ChatsOld/"+tu+"/"+el);
            if(test!=""){
            //myRef.removeValue();
            //myRefOld.removeValue();
                }
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
            Read();
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
                        if(messages==null)
                        {
                            messages=new ArrayList<>();
                        }
                        //chatView.clearMessages();
                        ChatMessage mens;
                        boolean añadidos=false;
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
                                //chatView.addMessage(mens);
                                messages.add(new Mens(mens,id));
                                ids.add(id);
                                myRefOld.child(id).child("Mensaje").setValue(mensaje);
                                myRefOld.child(id).child("Tipo").setValue(tipo);
                                myRefOld.child(id).child("Fecha").setValue(fecha);
                                myRef.child(tu).child(el).child(id).removeValue();
                                añadidos = true;
                                }

                            }
                        if(añadidos){
                        toJSon();
                        Write();
                        Read();

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
                        if(ids==null)
                        {
                            ids=new ArrayList<>();
                        }
                        if(messages==null)
                        {
                            messages=new ArrayList<>();
                        }
                        DateFormat dh = new SimpleDateFormat("yyyyMMddHHmmss ");



                        int cont = 0;
                       // while (cont<10000) {
                        Date fecha = Calendar.getInstance().getTime();
                        String id = dh.format(fecha)+cont;
                        ids.add(id + chatMessage.getMessage().hashCode());
                        messages.add(new Mens(chatMessage,id + chatMessage.getMessage().hashCode() ));
                        myRefOld.child(id + chatMessage.getMessage().hashCode()).child("Mensaje").setValue(chatMessage.getMessage());
                        myRefOld.child(id + chatMessage.getMessage().hashCode()).child("Tipo").setValue(chatMessage.getType());
                        myRefOld.child(id + chatMessage.getMessage().hashCode()).child("Fecha").setValue(chatMessage.getTimestamp());
                        myRef.child(el).child(tu).child(id + chatMessage.getMessage().hashCode()).child("Mensaje").setValue(chatMessage.getMessage());// perform actual message sending
                        myRef.child(el).child(tu).child(id + chatMessage.getMessage().hashCode()).child("Tipo").setValue(ChatMessage.Type.RECEIVED);
                        myRef.child(el).child(tu).child(id + chatMessage.getMessage().hashCode()).child("Fecha").setValue(chatMessage.getTimestamp());
                        Ref.child(el).child("Leidos").child(tu).setValue(false);
                        cont++;
                      //  }
                        PrincipalActivity.SaveLog("Log: ", "Mensaje enviado " + tu + " a " + el);

                        return true;
                    } catch (Exception e) {
                        PrincipalActivity.SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                        return false;
                    }
                }
            });

           // Read();
        } catch (Exception e) {
            PrincipalActivity.SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        myRef.removeEventListener(event);
        Write();
        Ref.child(tu).child("Leidos").child(el).removeValue();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.removeEventListener(event);
        Write();
        Ref.child(tu).child("Leidos").child(el).removeValue();
    }
    @Override
    protected void onResume() {
        super.onResume();
        myRef.addValueEventListener(event);
        Read();
        Ref.child(tu).child("Leidos").child(el).removeValue();
    }
   public void Write()
    {
        try {
           // PrincipalActivity.SaveLog("Log: ", "Mensajes guardados en memoria " + tu + " a " + el);
            final String xmlFile = "userData";
            FileOutputStream fos = null;
            ContextWrapper wrapper = new ContextWrapper(getApplication());
            File file = wrapper.getDir("chat", MODE_PRIVATE);
            file = new File(file, tu+el+"userData"+el+".txt");
            file.createNewFile();
            FileOutputStream outputStreamWriter = getApplicationContext().openFileOutput(file.getName(),Context.MODE_PRIVATE);
                    new OutputStreamWriter(new FileOutputStream (file));

            outputStreamWriter.write(toJSon().getBytes());
            outputStreamWriter.close();
        }
          catch (Exception e) {
        PrincipalActivity.SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
    }

    }
   public static String toJSon() {
       try {
           JSONArray jsonArr = new JSONArray();
           for (Mens pn : messages ) {
               JSONObject pnObj = new JSONObject();
               pnObj.put("Id", pn.getId());
               pnObj.put("type", pn.getChatMessage().getType());
               pnObj.put("message", pn.getChatMessage().getMessage());
               pnObj.put("date", pn.getChatMessage().getTimestamp());
               jsonArr.put(pnObj);
           }
           mensajes = jsonArr.toString();
           return jsonArr.toString();
       }
       catch(JSONException ex) {
           PrincipalActivity.SaveLog("ERROR: ", ex.getMessage() + " " + Log.getStackTraceString(ex));
       }
       return "";

   }
    public void Read()
    {


        try {

            ContextWrapper wrapper = new ContextWrapper(getApplication());
            File file = wrapper.getDir("chat", MODE_PRIVATE);
            file = new File(file, tu+el+"userData"+el+".txt");
            if(file.exists()){
                    try {
                        FileInputStream inputStream = getApplicationContext().openFileInput(file.getName());
                        if ( inputStream != null ) {
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String receiveString = "";
                            StringBuilder stringBuilder = new StringBuilder();

                            while ( (receiveString = bufferedReader.readLine()) != null ) {
                                stringBuilder.append(receiveString);
                            }
                            inputStream.close();
                            mensajes = stringBuilder.toString();
                            //PrincipalActivity.SaveLog("LOG: Leido", mensajes);

                        }
                    }
                    catch (Exception e) {
                        PrincipalActivity.SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));

                    }
                }
                else
                    {
                        descargarMensajesAntiguos();
                    }
        JsontoMessages();
        }catch (Exception e){
            PrincipalActivity.SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
        }



    }

    private void descargarMensajesAntiguos() {
        event2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    if(ids==null)
                    {
                        ids=new ArrayList<>();
                    }
                    if(messages==null)
                    {
                        messages=new ArrayList<>();
                    }

                    ChatMessage mens;
                    for (DataSnapshot fechas : dataSnapshot.getChildren()) {
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
                                //chatView.addMessage(mens);
                                messages.add(new Mens(mens,id));
                                ids.add(id);
                            }

                        }
                    }
                    if(!messages.isEmpty()){

                        toJSon();
                        Write();
                        Read();
                        myRefOld.removeEventListener(event2);

                    }



                } catch (Exception e) {
                    PrincipalActivity.SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                PrincipalActivity.SaveLog("ERROR: ", error.getMessage() + "en ChatActivity");

            }
        };
        myRefOld.addValueEventListener(event2);
    }

    public void JsontoMessages() {

    try {
        messages = new ArrayList<>();
        ids = new ArrayList<>();
        ArrayList<ChatMessage> temp =new ArrayList<>();
        chatView.clearMessages();
        if (mensajes!=null) {
           // JSONObject jObj = new JSONObject(mensajes);

            JSONArray jsonArr = new JSONArray(mensajes);
            JSONArray sortedJsonArray = new JSONArray();

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonValues.add(jsonArr.getJSONObject(i));
            }
            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "Name";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    Long valA = (long)0;
                    Long valB = (long)0;

                    try {
                        valA = (long) a.get("date");
                        valB = (long) b.get("date");
                    }
                    catch (JSONException e) {
                        //do something
                    }

                    return valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });
            for (int i = 0; i < jsonArr.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }
            for (int i = 0; i < sortedJsonArray.length(); i++) {
                JSONObject obj = sortedJsonArray.getJSONObject(i);
                String tipo = obj.getString("type");
                String id = obj.getString("Id");
                String mensaje = obj.getString("message");
                Long fecha = obj.getLong("date");
                ChatMessage mens;
                if (tipo.equals("SENT")) {
                    mens = new ChatMessage(mensaje, fecha, ChatMessage.Type.SENT);
                } else
                    mens = new ChatMessage(mensaje, fecha, ChatMessage.Type.RECEIVED);
                messages.add(new Mens(mens, id));
                ids.add(id);
                temp.add(mens);

            }
            chatView.addMessages(temp);
        }
    }catch (Exception e)
    {
        PrincipalActivity.SaveLog("ERROR: ", e.getMessage() + " " + Log.getStackTraceString(e));
    }
    }}
