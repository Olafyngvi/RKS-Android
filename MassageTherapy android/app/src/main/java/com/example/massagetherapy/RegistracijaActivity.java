package com.example.massagetherapy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class RegistracijaActivity extends AppCompatActivity {

    private EditText imeprez;
    private EditText pass;
    private EditText brojTelefona;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);
        setupActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        imeprez  = (EditText) findViewById(R.id.editText);
        pass=(EditText) findViewById((R.id.editText2));
        brojTelefona=(EditText) findViewById(R.id.editText3);

        sharedPref = getSharedPreferences("riki", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        Intent intent = getIntent();

        mLoginFormView = findViewById(R.id.login_form1);
        mProgressView = findViewById(R.id.login_progress1);

        Button spremi = (Button) findViewById(R.id.buttonReg);
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
            getSupportActionBar();
        }
    }

    private void attemptLogin() {

        imeprez.setError(null);
        pass.setError(null);
        brojTelefona.setError(null);
        String ime = imeprez.getText().toString();
        String password = pass.getText().toString();
        String brojT = brojTelefona.getText().toString();
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Provjerite imate li pristup internetu!");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                finish();
            }
        });

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
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Ime", ime);
                jsonObject.put("Lozinka", password);
                jsonObject.put("brojtelefona",brojT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BuildConfig.SERVER_URL + "/api/login/"+0, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // uspjesno
                    try {
                        editor.putInt("id", response.getInt("id")).apply();
                        editor.putString("Ime", response.getString("Ime")).apply();
                        editor.putString("brojT",response.getString("brojTelefona")).apply();
                        editor.putString("Pass",response.getString("lozinka")).apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(RegistracijaActivity.this, KorisnikActivity.class));
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("riki-login-response", error.toString());
                    alert.show();
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
