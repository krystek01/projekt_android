package com.example.mieszkania;

import static com.example.mieszkania.MainActivity.offers;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class OfferDetailsActivity extends AppCompatActivity {

    private TextView textViewMiasto;
    private TextView textViewAdres;
    private TextView textViewPowierzchnia;
    private TextView textViewLiczbaPokoi;
    private TextView textViewTypNieruchomosci;
    private TextView textViewTyp;
    private TextView textViewCena;
    private ImageView imageViewZdjecie;
    private TextView textViewUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        textViewMiasto = findViewById(R.id.textview_miasto);
        textViewAdres = findViewById(R.id.textview_adres);
        textViewPowierzchnia = findViewById(R.id.textview_powierzchnia);
        textViewLiczbaPokoi = findViewById(R.id.textview_liczba_pokoi);
        textViewTypNieruchomosci = findViewById(R.id.textview_typ_nieruchomosci);
        textViewTyp = findViewById(R.id.textview_typ);
        textViewCena = findViewById(R.id.textview_cena);
        imageViewZdjecie = findViewById(R.id.imageview_zdjecie);
        textViewUsername = findViewById(R.id.textview_username2);
        HousingOffer offer = (HousingOffer) getIntent().getSerializableExtra("OFFER");

        if (offer != null) {
            textViewMiasto.setText(offer.getMiasto());
            textViewAdres.setText(offer.getAdres());
            textViewPowierzchnia.setText(String.valueOf(offer.getPowierzchnia()) + " m²");
            textViewLiczbaPokoi.setText(String.valueOf(offer.getLiczbaPokoi()));
            textViewTypNieruchomosci.setText(offer.getTypNieruchomosci());
            textViewTyp.setText(offer.getTyp());
            textViewCena.setText(String.valueOf(offer.getCena()) + " PLN");
            textViewUsername.setText(offer.getIdUzytkownika());

            List<Bitmap> zdjecia = offer.getZdjecia();
            if (zdjecia != null && !zdjecia.isEmpty()) {
                Bitmap pierwszeZdjecie = zdjecia.get(0);
                imageViewZdjecie.setImageBitmap(pierwszeZdjecie);
            } else {
                imageViewZdjecie.setImageResource(R.drawable.placeholder_image); // Placeholder image
            }
        }


    }
}
