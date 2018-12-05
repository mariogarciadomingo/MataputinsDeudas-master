package com.example.mario.mataputinsdeudas;

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
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.mario.mataputinsdeudas.PrincipalActivity.context;

/**
 * Created by mario on 25/01/2018.
 */

public class RVAdaptadorLog extends RecyclerView.Adapter<RVAdaptadorLog.ImatgeViewHolder> {
    static List<deuda> deudaList;
    int color;
    private int lastPosition = -1;

    public RVAdaptadorLog(@NonNull List<deuda> deudaList, int color) {

        if(!deudaList.isEmpty())
        Collections.sort(deudaList, new Comparator<deuda>() {
            public int compare(@NonNull deuda o1, @NonNull deuda o2) {
                return o1.fecha.compareTo(o2.fecha);
            }
        });
        Collections.reverse(deudaList);
        RVAdaptadorLog.deudaList
                = deudaList;
        this.color = color;


    }


    @NonNull
    @Override
    public ImatgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.log, parent, false);
        ImatgeViewHolder dvh = new ImatgeViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(ImatgeViewHolder holder, int position) {
        try {
            if(deudaList.get(position).getTitol().contains("ERROR"))
            {
                holder.titol.setBackgroundColor(Color.RED);
            }
            else
                holder.titol.setBackgroundColor(Color.GREEN);
        holder.titol.setText(deudaList.get(position).getTitol());
        holder.missatge.setText(deudaList.get(position).getId());
        holder.fecha.setText(deudaList.get(position).getFecha());
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
        TextView titol, missatge, fecha;

        public ImatgeViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardView);

            titol = itemView.findViewById(R.id.LogTittle);
            missatge = itemView.findViewById(R.id.LogMessage);
            fecha = itemView.findViewById(R.id.Fecha);

        }
    }


}
