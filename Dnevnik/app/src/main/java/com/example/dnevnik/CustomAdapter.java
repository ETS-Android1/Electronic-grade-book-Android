package com.example.dnevnik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList datum, nastavnik, predmet, ocena;

    CustomAdapter(Context context, ArrayList datum, ArrayList nastavnik, ArrayList predmet, ArrayList ocena){
        this.context = context;
        this.datum = datum;
        this.nastavnik = nastavnik;
        this.predmet = predmet;
        this.ocena = ocena;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtDatum.setText(String.valueOf(datum.get(position)));
        holder.txtNastavnik.setText(String.valueOf(nastavnik.get(position)));
        holder.txtPredmet.setText(String.valueOf(predmet.get(position)));
        holder.txtOcena.setText(String.valueOf(ocena.get(position)));
    }

    @Override
    public int getItemCount() {
        return predmet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtDatum, txtNastavnik, txtPredmet, txtOcena;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDatum = itemView.findViewById(R.id.txtDatum);
            txtNastavnik = itemView.findViewById(R.id.txtNastavnik);
            txtPredmet = itemView.findViewById(R.id.txtPredmet);
            txtOcena = itemView.findViewById(R.id.txtOcena);
        }
    }
}
