package com.example.mario.mataputinsdeudas;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.mario.mataputinsdeudas.PrincipalActivity.context;

/**
 * Created by mario on 25/01/2018.
 */

public class RVAdaptador extends RecyclerView.Adapter<RVAdaptador.ImatgeViewHolder> {
    static List<deuda> deudaList;
    int color;
    File dir;
    private int lastPosition = -1;

    public RVAdaptador(@NonNull List<deuda> deudaList, int color) {

        Collections.sort(deudaList, new Comparator<deuda>() {
            public int compare(@NonNull deuda o1, @NonNull deuda o2) {
                return o1.id.compareTo(o2.id);
            }
        });
        Collections.reverse(deudaList);
        RVAdaptador.deudaList
                = deudaList;
        this.color = color;

        dir = PrincipalActivity.dir;
    }

    static void Eliminar(String id) {
        HistorialActivity.eliminar(id);
    }

    @NonNull
    @Override
    public ImatgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila, parent, false);
        ImatgeViewHolder dvh = new ImatgeViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(ImatgeViewHolder holder, int position) {
        try {
            holder.ImProducte.setImageBitmap(BitmapFactory.decodeFile(dir + "/" + deudaList.get(position).getImProducte()));

        holder.titol.setText(deudaList.get(position).getTitol());
        holder.preu.setText(deudaList.get(position).getPreu());
        holder.titol.setTextColor(color);
        holder.fecha.setTextColor(color);
        holder.fecha.setText(deudaList.get(position).getFecha());
        if (deudaList.get(position).getTitol().contains("Debes")) {
            holder.preu.setTextColor(Color.RED);
            holder.cv.setCardBackgroundColor(Color.argb(200, 255, 102, 0));
        }
        if (deudaList.get(position).getTitol().contains("te debe")) {
            holder.preu.setTextColor(Color.BLUE);
            holder.cv.setCardBackgroundColor(Color.argb(200, 51, 102, 255));
        }
        if (deudaList.get(position).getTitol().toLowerCase().contains("has pagado")) {
            holder.preu.setTextColor(Color.BLUE);
            holder.cv.setCardBackgroundColor(Color.argb(200, 255, 204, 102));
        }
        if (deudaList.get(position).getTitol().contains("te ha pagado")) {
            holder.preu.setTextColor(Color.argb(200, 0, 51, 0));
            holder.cv.setCardBackgroundColor(Color.argb(200, 51, 204, 51));
        }
        setAnimation(holder.itemView, position);
        } catch (Exception e) {
            PrincipalActivity.SaveLog("ERROR: ",e.getMessage()+" "+Log.getStackTraceString(e));
        }
    }

    @Override
    public int getItemCount() {
        return deudaList.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public static class ImatgeViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView ImProducte;
        TextView titol, preu, fecha, id;
        Button eliminar;

        public ImatgeViewHolder(@NonNull View itemView) {
            super(itemView);
            eliminar = itemView.findViewById(R.id.btEliminar);
            cv = itemView.findViewById(R.id.cardView);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (eliminar.getVisibility() == View.VISIBLE)
                        eliminar.setVisibility(View.GONE);
                    else
                        eliminar.setVisibility(View.VISIBLE);
                }
            });
            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Eliminar(deudaList.get(getAdapterPosition()).getId());

                }
            });

            ImProducte = itemView.findViewById(R.id.ImProducte);

            titol = itemView.findViewById(R.id.LogMessage);
            preu = itemView.findViewById(R.id.preuProducte);
            fecha = itemView.findViewById(R.id.Fecha);

        }
    }


}
