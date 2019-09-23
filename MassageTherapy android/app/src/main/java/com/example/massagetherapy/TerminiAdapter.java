package com.example.massagetherapy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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

public class TerminiAdapter extends RecyclerView.Adapter<TerminiAdapter.MyViewHolder> {
    private Activity context;
    private List<Termin> mDataset;
    private  SharedPreferences sharedPref;
    int dan;
    int mjesec;
    int godina;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public MyViewHolder(FrameLayout v) {
            super(v);
            textView = v.findViewById(R.id.textView);
        }
    }

    public TerminiAdapter(Activity context, List<Termin> myDataset, int dan, int mjesec, int godina) {
        this.context = context;
        mDataset = myDataset;
        this.dan=dan;
        this.mjesec=mjesec;
        this.godina=godina;
    }

    @Override
    public TerminiAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listatermina, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Termin termin = mDataset.get(position);
        holder.textView.setText(termin.termin);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref = context.getSharedPreferences("riki", Context.MODE_PRIVATE);

                int id = sharedPref.getInt("id", 0);
                Log.e("riki", "a " + id);
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("terminId", termin.id);
                    jsonObject.put("userId",id);
                    jsonObject.put("dan",dan);
                    jsonObject.put("mjesec",(mjesec+1));
                    jsonObject.put("godina",godina);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BuildConfig.SERVER_URL + "/api/rezervacije/", jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(context, UspjesnoActivity.class);
                        context.startActivity(intent);
                        context.finish();
                        Log.d("riki-login-response", response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
                        dlgAlert.setMessage("Već imate rezervisan termin na ovaj datum!");
                        dlgAlert.setTitle("Massage Therapy");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                });
                Volley.newRequestQueue(context).add(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
