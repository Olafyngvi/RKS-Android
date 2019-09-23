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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    public static final String BROJ_TELEFONA = "com.example.massagetherapy.BrojT";
    public static final String PASSWORD = "com.example.massagetherapy.PASS";
    public static final String IMEREZIME = "com.example.massagetherapy.IME";
    private EditText brojtelefona;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getSupportActionBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        sharedPref = getSharedPreferences("riki", Context.MODE_PRIVATE);

        String name = sharedPref.getString("riki","");

        editor = sharedPref.edit();

        brojtelefona = (EditText) findViewById(R.id.tel);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button prijaviSe = (Button) findViewById(R.id.email_sign_in_button);
        prijaviSe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        final Intent reg=new Intent(this,RegistracijaActivity.class);

        Button registurjSE = (Button) findViewById(R.id.email_reg_button);
        registurjSE.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(reg);
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
        brojtelefona.setError(null);
        mPasswordView.setError(null);
        final String ime = brojtelefona.getText().toString();
        final String password = mPasswordView.getText().toString();
        final Intent korisnik=new Intent(this, KorisnikActivity.class);
        final Intent admin=new Intent(this,AdminPregledActivity.class);
        final Intent login=new Intent(this, LoginActivity.class);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startActivity(login);
                finish();
            }
        });

        final int[] id = new int[1];
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(ime)) {
            brojtelefona.setError(getString(R.string.error_invalid_brojT));
            focusView = brojtelefona;
            cancel = true;
        }
        if(!isPasswordValid(password) && !(TextUtils.isEmpty(password))) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if(!isBrojValid(ime) && !(TextUtils.isEmpty(ime))) {
            brojtelefona.setError(getString(R.string.error_incorrect_broj));
            focusView = brojtelefona;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("brojTelefona", ime);
                jsonObject.put("Lozinka", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BuildConfig.SERVER_URL + "/api/login/"+0, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        id[0] =response.getInt("id");
                        Log.e("Riki3", "a " + id[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        Log.e("Riki1", "a " + response.getInt("id"));
                        editor.putInt("id", response.getInt("id")).apply();
                        editor.putString("Ime", response.getString("ime")).apply();
                        editor.putString("brojT",response.getString("brojTelefona")).apply();
                        editor.putString("Pass",response.getString("lozinka")).apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(id[0] ==1){

                        startActivity(admin);
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
                    if (error instanceof NetworkError || error instanceof TimeoutError) {
                        alert.setMessage("Provjerite internet konekciju!");
                    } else if (error instanceof ServerError || error instanceof ParseError) {
                        alert.setMessage("Problemi sa serverom!");
                    } else  {
                        alert.setMessage("Uneseni broj telefona i/ili password nisu ispravni!");
                    }
                    alert.show();
                    Log.d("riki-login-response", error.toString());
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

