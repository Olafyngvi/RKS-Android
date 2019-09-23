package com.example.massagetherapy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EditPodatakaAdmin extends AppCompatActivity {
    private EditText imeprez;
    private EditText pass;
    private EditText brojTelefona;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private View mProgressView;
    private View mEditFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_podataka_admin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        imeprez  = (EditText) findViewById(R.id.Ime);
        pass=(EditText) findViewById((R.id.Pass));
        brojTelefona=(EditText) findViewById(R.id.Tel);
        sharedPref=getSharedPreferences("riki", Context.MODE_PRIVATE);
        editor=sharedPref.edit();
        String Ime=sharedPref.getString("Ime","Nema imenana");
        String Pass=sharedPref.getString("Pass","0");
        String brojT=sharedPref.getString("brojT","00");
        mEditFormView = findViewById(R.id.edit_form);
        mProgressView = findViewById(R.id.edit_progress);
        EditText ime=findViewById(R.id.Ime);
        EditText editText = findViewById(R.id.Pass);
        EditText editText2=findViewById(R.id.Tel);
        ime.setText(Ime);
        editText.setText(Pass);
        editText2.setText(brojT);
        Button spremi = (Button) findViewById(R.id.editBtn);
        spremi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void attemptLogin() {
        imeprez.setError(null);
        pass.setError(null);
        brojTelefona.setError(null);
        String ime = imeprez.getText().toString();
        String password = pass.getText().toString();
        String brojT = brojTelefona.getText().toString();
        final int[] id = new int[1];
        final Intent korisnik=new Intent(this, KorisnikActivity.class);
        final Intent admin=new Intent(this,AdminPregledActivity.class);
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(brojT)){
            brojTelefona.setError(getString(R.string.error_invalid_brojT));
            focusView = brojTelefona;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)){
            pass.setError(getString(R.string.error_invalid_password));
            focusView = pass;
            cancel = true;
        }
        if (TextUtils.isEmpty(ime)) {
            imeprez.setError(getString(R.string.error_field_required));
            focusView = imeprez;
            cancel = true;
        }
        if(!isBrojValid(brojT) && !(TextUtils.isEmpty(brojT))) {
            brojTelefona.setError(getString(R.string.error_incorrect_broj));
            focusView = brojTelefona;
            cancel = true;
        }   if(!isPasswordValid(password) && !(TextUtils.isEmpty(password))) {
            pass.setError(getString(R.string.error_incorrect_password));
            focusView = pass;
            cancel = true;
        }
        if(!isImeValid(ime) && !(TextUtils.isEmpty(ime))) {
            imeprez.setError(getString(R.string.error_ime_required));
            focusView = imeprez;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            int KorsID=sharedPref.getInt("id",0);
            editor.putString("Ime", ime).apply();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Id",KorsID);
                jsonObject.put("Ime", ime);
                jsonObject.put("Lozinka", password);
                jsonObject.put("brojtelefona",brojT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BuildConfig.SERVER_URL + "/api/login/"+KorsID,jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        id[0] =response.getInt("id");
                        Log.e("Riki3", "a " + id[0]);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(id[0] ==1){
                        startActivity(new Intent(admin));
                        finish();
                    }
                    else {
                        startActivity(korisnik);
                        Log.d("riki-login-response", response.toString());
                        finish();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("riki-login-response", error.toString());
                    error.printStackTrace();
                }
            });
            Volley.newRequestQueue(this).add(request);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }
    private boolean isBrojValid(String brojT) {
        return brojT.length() >= 9;
    }
    private boolean isImeValid(String ime) {
        return ime.length() >= 7;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mEditFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mEditFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEditFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mEditFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
