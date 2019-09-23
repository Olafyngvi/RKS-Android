package com.example.massagetherapy;

import android.app.Activity;
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

public class MojeRezervacijeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private  SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moje_rezervacije);
        recyclerView = (RecyclerView) findViewById(R.id.MojeRezervacije);
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this );
        dlgAlert.setMessage("Nemate trenutno aktivnih rezervacija!");
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
        sharedPref=getSharedPreferences("riki", Context.MODE_PRIVATE);
        editor=sharedPref.edit();
        int id=sharedPref.getInt("id",0);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BuildConfig.SERVER_URL + "/api/rezervacije/" + id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Rezervacije> rezervacije = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Rezervacije rezv = new Rezervacije();
                        rezv.id = object.getInt("id");
                        rezv.dan = object.getInt("dan");
                        rezv.mjesec = object.getInt("mjesec");
                        rezv.godina = object.getInt("godina");
                        rezv.termin=object.getString("termin");
                        rezervacije.add(rezv);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("RezervacijePrazne", "a " +response.length());

                    mAdapter = new RezervacijeAdapter(MojeRezervacijeActivity.this, rezervacije);
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

