package com.example.mieszkania;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    public static final String OFFERS_FILE_NAME = "offers.dat";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public static ListView listViewOffers;
    public static HousingOfferAdapter offerAdapter;
    public static List<HousingOffer> offers;
    public static List<HousingOffer> originalOffers;
    private EditText editTextSearch;
    private Button buttonSearch;
    private Button buttonReset;
    private Button buttonAddOffer;
    private Button buttonPrevPage;
    private Button buttonNextPage;
    private Button buttonLogin;
    private TextView textViewUsername;

    private Button buttonZnajdz;

    private int currentPage = 1;
    private int itemsPerPage = 10;
    public static String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja listy ofert (przykładowe dane)
        offers = loadOffersFromFile();
        if (offers == null) {
            offers = generateSampleOffers();
            saveOffersToFile(offers);
        }
        originalOffers = new ArrayList<>(offers);

        // Inicjalizacja UI elementów
        editTextSearch = findViewById(R.id.edittext_search);
        buttonSearch = findViewById(R.id.button_search);
        buttonReset = findViewById(R.id.button_reset);
        buttonPrevPage = findViewById(R.id.button_prev_page);
        buttonNextPage = findViewById(R.id.button_next_page);
        listViewOffers = findViewById(R.id.listview_offers);
        buttonLogin = findViewById(R.id.button_login);
        textViewUsername = findViewById(R.id.textview_username);
        buttonAddOffer = findViewById(R.id.button_add_offer);
        buttonZnajdz = findViewById(R.id.button_find_me);

        Intent intent = getIntent();
        if (intent.hasExtra("USERNAME")) {
            loggedInUsername = intent.getStringExtra("USERNAME");
            textViewUsername.setText("Zalogowany użytkownik: " + loggedInUsername);
            buttonLogin.setVisibility(View.GONE);
            buttonAddOffer.setVisibility(View.VISIBLE);
        }

        offerAdapter = new HousingOfferAdapter(this, offers);
        listViewOffers.setAdapter(offerAdapter);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = editTextSearch.getText().toString().trim();
                if (searchText.isEmpty()) {
                    resetOffers();
                } else {
                    searchOffers(searchText);
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOffers();
            }
        });

        buttonPrevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1) {
                    currentPage--;
                    updatePage();
                }
            }
        });

        buttonNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalPages = calculateTotalPages();
                if (currentPage < totalPages) {
                    currentPage++;
                    updatePage();
                }
            }
        });

        buttonAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddOfferActivity.class);
                intent.putExtra("USERNAME", loggedInUsername);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        listViewOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HousingOffer selectedOffer = offers.get(position);
                Intent intent = new Intent(MainActivity.this, OfferDetailsActivity.class);
                intent.putExtra("OFFER", selectedOffer);
                startActivity(intent);
            }
        });

        buttonZnajdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findMe(new LocationCallback() {
                    @Override
                    public void onLocationFound(String city) {
                        editTextSearch.setText(city);
                        searchOffers(city);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        updatePage();
    }

    private List<HousingOffer> generateSampleOffers() {
        List<HousingOffer> sampleOffers = new ArrayList<>();
        sampleOffers.add(new HousingOffer("Warszawa", "ul. Przykładowa 1", 45.0, 2, "mieszkanie", "na wynajem", 2500.0, generateUserId()));
        sampleOffers.add(new HousingOffer("Kraków", "ul. Przykładowa 2", 60.0, 3, "mieszkanie", "kupno", 350000.0, generateUserId()));
        sampleOffers.add(new HousingOffer("Gdańsk", "ul. Przykładowa 3", 75.0, 4, "dom", "kupno", 450000.0, generateUserId()));
        return sampleOffers;
    }

    private String generateUserId() {
        return UUID.randomUUID().toString();
    }

    private void searchOffers(String searchText) {
        List<HousingOffer> filteredOffers = new ArrayList<>();
        for (HousingOffer offer : originalOffers) {
            if (offer.getMiasto().toLowerCase().contains(searchText.toLowerCase()) ||
                    offer.getAdres().toLowerCase().contains(searchText.toLowerCase())) {
                filteredOffers.add(offer);
            }
        }
        offers.clear();
        offers.addAll(filteredOffers);
        updatePage();
    }

    private void resetOffers() {
        offers.clear();
        offers.addAll(originalOffers);
        editTextSearch.setText("");
        updatePage();
    }

    private void updatePage() {
        int start = (currentPage - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, offers.size());
        List<HousingOffer> pageOffers = new ArrayList<>(offers.subList(start, end));
        offerAdapter.clear();
        offerAdapter.addAll(pageOffers);
        listViewOffers.setSelection(0);
        updatePageButtons();
    }

    private void updatePageButtons() {
        int totalPages = calculateTotalPages();
        buttonPrevPage.setEnabled(currentPage > 1);
        buttonNextPage.setEnabled(currentPage < totalPages);
    }

    private int calculateTotalPages() {
        return (int) Math.ceil((double) offers.size() / itemsPerPage);
    }

    private void saveOffersToFile(List<HousingOffer> offers) {
        try (FileOutputStream fos = openFileOutput(OFFERS_FILE_NAME, MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(offers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<HousingOffer> loadOffersFromFile() {
        try (FileInputStream fis = openFileInput(OFFERS_FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<HousingOffer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void findMe(LocationCallback callback) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location location = task.getResult();
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            String city = addresses.get(0).getLocality();
                            callback.onLocationFound(city);
                        } else {
                            callback.onError("Nie znaleziono miasta");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onError("Błąd przy uzyskiwaniu miasta");
                    }
                } else {
                    callback.onError("Nie udało się uzyskać lokalizacji");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findMe(new LocationCallback() {
                    @Override
                    public void onLocationFound(String city) {
                        editTextSearch.setText(city);
                        searchOffers(city);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(this, "Uprawnienia do lokalizacji są wymagane", Toast.LENGTH_LONG).show();
            }
        }
    }
}
