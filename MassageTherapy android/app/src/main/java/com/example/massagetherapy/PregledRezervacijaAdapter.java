package com.example.massagetherapy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PregledRezervacijaAdapter extends RecyclerView.Adapter<PregledRezervacijaAdapter.MyViewHolder> {
    private Activity context;
    private List<RezervacijeFull> mDataset;
    private SharedPreferences sharedPref;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public MyViewHolder(FrameLayout v) {
            super(v);
            textView = v.findViewById(R.id.textView);
        }
    }

    public PregledRezervacijaAdapter(Activity context, List<RezervacijeFull> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public PregledRezervacijaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                          int viewType) {
        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adminrezervacije, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final RezervacijeFull rezv = mDataset.get(position);
        holder.textView.setText("Termin: "+rezv.Termin+"h"+"\n"+"Ime i prezime: "+rezv.Korisnik+"\nTelefon: "+rezv.Broj);
    }

    public int getItemCount() {
        return mDataset.size();
    }
}
