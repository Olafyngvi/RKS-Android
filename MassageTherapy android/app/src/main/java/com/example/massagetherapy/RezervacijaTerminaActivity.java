package com.example.massagetherapy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RezervacijaTerminaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervacija_termina);
        recyclerView = (RecyclerView) findViewById(R.id.termins);

        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this );
        dlgAlert.setMessage("Nema slobodnih termina za uneseni datum!");
        dlgAlert.setTitle("Massage Therapy");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        recyclerView.setHasFixedSize(true);
        Intent intent=getIntent();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final int dan=intent.getIntExtra(KorisnikActivity.dan, 0);
        final int mjesec=intent.getIntExtra(KorisnikActivity.mjesec,0);
        final int godina=intent.getIntExtra(KorisnikActivity.godina,0);
        Log.e("log-datum-dan", "dan " + dan);
        Log.e("log-datum-mjesec", "mjesec " + mjesec);
        Log.e("log-datum-godina", "godina " + godina);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BuildConfig.SERVER_URL + "/api/rezervacije/" + dan + "/" + (mjesec+1)+"/"+godina, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Termin> termini = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Termin termin = new Termin();
                        termin.id = object.getInt("id");
                        termin.termin = object.getString("termin");
                        termini.add(termin);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mAdapter = new TerminiAdapter(RezervacijaTerminaActivity.this, termini,dan,mjesec,godina);
                    recyclerView.setAdapter(mAdapter);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dlgAlert.show();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }
}

