package com.example.mieszkania;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class AddOfferActivity extends AppCompatActivity {

    private EditText editTextMiasto;
    private EditText editTextAdres;
    private EditText editTextPowierzchnia;
    private EditText editTextLiczbaPokoi;
    private EditText editTextTypNieruchomosci;
    private EditText editTextTyp;
    private EditText editTextCena;

    private List<HousingOffer> offers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        // Initialize EditText fields
        editTextMiasto = findViewById(R.id.edittext_miasto);
        editTextAdres = findViewById(R.id.edittext_adres);
        editTextPowierzchnia = findViewById(R.id.edittext_powierzchnia);
        editTextLiczbaPokoi = findViewById(R.id.edittext_liczba_pokoi);
        editTextTypNieruchomosci = findViewById(R.id.edittext_typ_nieruchomosci);
        editTextTyp = findViewById(R.id.edittext_typ);
        editTextCena = findViewById(R.id.edittext_cena);

        // Initialize the Add Offer button
        Button buttonAddOffer = findViewById(R.id.button_add_offer_confirm);
        buttonAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOffer();
            }
        });
    }

    private void addOffer() {
        String miasto = editTextMiasto.getText().toString().trim();
        String adres = editTextAdres.getText().toString().trim();
        String powierzchniaStr = editTextPowierzchnia.getText().toString().trim();
        String liczbaPokoiStr = editTextLiczbaPokoi.getText().toString().trim();
        String typNieruchomosci = editTextTypNieruchomosci.getText().toString().trim();
        String typ = editTextTyp.getText().toString().trim();
        String cenaStr = editTextCena.getText().toString().trim();

        // Validate input fields
        if (miasto.isEmpty() || adres.isEmpty() || powierzchniaStr.isEmpty() || liczbaPokoiStr.isEmpty()
                || typNieruchomosci.isEmpty() || typ.isEmpty() || cenaStr.isEmpty()) {
            Toast.makeText(this, "Proszę wypełnić wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double powierzchnia = Double.parseDouble(powierzchniaStr);
            int liczbaPokoi = Integer.parseInt(liczbaPokoiStr);
            double cena = Double.parseDouble(cenaStr);

            // Create a new housing offer
            HousingOffer newOffer = new HousingOffer(miasto, adres, powierzchnia, liczbaPokoi, typNieruchomosci, typ, cena, MainActivity.loggedInUsername);

            // Add the new offer to the list of offers
            MainActivity.offers.add(newOffer);
            MainActivity.originalOffers.add(newOffer);

            // Save offers to file
            saveOffersToFile(MainActivity.offers);

            // Notify adapter about the changes
            MainActivity.offerAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Dodano ofertę pomyślnie", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity after adding the offer
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Nieprawidłowy format danych liczbowych", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to save offers to file
    private void saveOffersToFile(List<HousingOffer> offers) {
        try (FileOutputStream fos = openFileOutput(MainActivity.OFFERS_FILE_NAME, MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(offers);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd zapisu danych ofert", Toast.LENGTH_SHORT).show();
        }
    }
}
