package com.example.mieszkania;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HousingOffer implements Serializable {
    private String miasto;
    private String adres;
    private double powierzchnia;
    private int liczbaPokoi;
    private String typNieruchomosci; // "mieszkanie" lub "dom"
    private String typ; // "na wynajem" lub "kupno"
    private double cena;
    private transient List<Bitmap> zdjecia; // Transient field for storing Bitmap images
    private String idUzytkownika; // ID użytkownika

    public HousingOffer(String miasto, String adres, double powierzchnia, int liczbaPokoi, String typNieruchomosci, String typ, double cena, String idUzytkownika) {
        this.miasto = miasto;
        this.adres = adres;
        this.powierzchnia = powierzchnia;
        this.liczbaPokoi = liczbaPokoi;
        this.typNieruchomosci = typNieruchomosci;
        this.typ = typ;
        this.cena = cena;
        this.idUzytkownika = idUzytkownika;
        this.zdjecia = new ArrayList<>();
    }

    public String getMiasto() {
        return miasto;
    }

    public String getAdres() {
        return adres;
    }

    public double getPowierzchnia() {
        return powierzchnia;
    }

    public int getLiczbaPokoi() {
        return liczbaPokoi;
    }

    public String getTypNieruchomosci() {
        return typNieruchomosci;
    }

    public String getTyp() {
        return typ;
    }

    public double getCena() {
        return cena;
    }

    public String getIdUzytkownika() {
        return idUzytkownika;
    }

    public List<Bitmap> getZdjecia() {
        return zdjecia;
    }

    public void addZdjecie(Bitmap zdjecie) {
        this.zdjecia.add(zdjecie);
    }

    // Convert Bitmap to Base64 string
    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Convert Base64 string to Bitmap
    public static Bitmap decodeFromBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
