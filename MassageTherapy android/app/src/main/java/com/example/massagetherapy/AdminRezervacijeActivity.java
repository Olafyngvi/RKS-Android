package com.example.massagetherapy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class AdminRezervacijeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rezervacija);
        recyclerView = (RecyclerView) findViewById(R.id.Rezervacije);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        recyclerView.setHasFixedSize(true);
        Intent intent=getIntent();
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this );
        dlgAlert.setMessage("Za uneseni datum nema rezervacija!");
        dlgAlert.setTitle("Massage Therapy");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        int dan=intent.getIntExtra(AdminPregledActivity.dan,0);
        int mjesec=intent.getIntExtra(AdminPregledActivity.mjesec,0);
        int godina=intent.getIntExtra(AdminPregledActivity.godina,0);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BuildConfig.SERVER_URL + "/api/admin/" + dan+"/"+(mjesec+1)+"/"+godina, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<RezervacijeFull> rezervacije = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        RezervacijeFull rezv = new RezervacijeFull();
                        rezv.Termin=object.getString("termin");
                        rezv.Korisnik=object.getString("klijent");
                        rezv.Broj=object.getString("brojTelefona");
                        rezervacije.add(rezv);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter = new PregledRezervacijaAdapter(AdminRezervacijeActivity.this, rezervacije);
                recyclerView.setAdapter(mAdapter);
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
