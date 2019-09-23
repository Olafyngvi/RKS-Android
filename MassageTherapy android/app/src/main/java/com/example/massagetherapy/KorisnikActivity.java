package com.example.massagetherapy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;


import java.util.Calendar;

public class KorisnikActivity extends AppCompatActivity {

    public static final String dan = "r1";
    public static final String mjesec = "r2";
    public static final String godina = "r3";
    private SharedPreferences sharedPrefe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnik);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        sharedPrefe=getSharedPreferences("riki", Context.MODE_PRIVATE);

        String ime=sharedPrefe.getString("Ime","Nema ime");
        TextView textView=findViewById(R.id.textView);
        textView.setText(ime);

    }

    public void OtvoriRezervacije(View vjuv){
        Intent intent=new Intent(this, MojeRezervacijeActivity.class);
        startActivity(intent);

    }

    public void EditPodataka(View vjuv){
        Intent intent=new Intent(this, EditPodatakaUserActivity.class);
        startActivity(intent);
    }
    public void OdjaviSE(View vjuv){
        SharedPreferences.Editor editor = sharedPrefe.edit();
        editor.clear();
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Odaberi", dialog);
            dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Zatvori", dialog);
            dialog.getDatePicker();
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Intent intent=new Intent(getActivity(), RezervacijaTerminaActivity.class);
            int d1=day;
            int m1=month;
            int g1=year;
            intent.putExtra(dan,d1);
            intent.putExtra(mjesec,m1);
            intent.putExtra(godina,g1);

            startActivity(intent);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
