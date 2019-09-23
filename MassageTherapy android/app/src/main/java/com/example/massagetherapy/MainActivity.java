package com.example.massagetherapy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
    }

    public void OtvoriGaleriju(View vjuv){
        Uri uri = Uri.parse("http://www.instagram.com/massageessentialtime/");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.instagram.com/massageessentialtime/")));
        }
    }

    public void OtvoriCjene(View vjuv){
        Intent inten=new Intent(this, UslugeActivity.class);
        startActivity(inten);

    }
 public void OtvoriKatalog(View vjuv){
        Intent inten=new Intent(this, KatalogActivity.class);
        startActivity(inten);

    }

    public void RezervisiTermin(View vjuv){
        Intent inten=new Intent(this, LoginActivity.class);
        startActivity(inten);
    }
}

