package com.example.massagetherapy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public  class RezervacijeAdapter extends RecyclerView.Adapter<RezervacijeAdapter.MyViewHolder> {
    private Activity context;
    private List<Rezervacije> mDataset;
    private SharedPreferences sharedPref;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public MyViewHolder(FrameLayout v) {
            super(v);
            textView = v.findViewById(R.id.textView2);
        }
    }

    public RezervacijeAdapter(Activity context, List<Rezervacije> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public RezervacijeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listarezervacija, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RezervacijeAdapter.MyViewHolder holder, int position) {
        if(mDataset==null){
            holder.textView.setText( "Nemate trenutno aktivnih rezervacija!");
        }
        else {
            final Rezervacije rezv = mDataset.get(position);
            holder.textView.setText("" + rezv.dan + "." + rezv.mjesec + "." + rezv.godina + "\n" + rezv.termin + "h");
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("terminId", 0);
                        jsonObject.put("userId",0);
                        jsonObject.put("dan",0);
                        jsonObject.put("mjesec",(0+1));
                        jsonObject.put("godina",0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BuildConfig.SERVER_URL + "/api/rezervacije/"+rezv.id, jsonObject, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Intent intent = new Intent(context, MojeRezervacijeActivity.class);
                                            context.startActivity(intent);
                                            context.finish();
                                            Log.d("riki-login-response", response.toString());
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Intent intent = new Intent(context, MojeRezervacijeActivity.class);

                                            context.startActivity(intent);
                                            context.finish();
                                        }
                                    });
                                    Volley.newRequestQueue(context).add(request);
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Da li ste sigurni da želite otkazati rezervaciju?").setPositiveButton("Da", dialogClickListener)
                            .setNegativeButton("Ne", dialogClickListener).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
