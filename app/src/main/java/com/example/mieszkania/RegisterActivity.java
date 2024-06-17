package com.example.mieszkania;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private Button buttonBackToLogin;

    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        // Inicjalizacja pól EditText i przycisków
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin);

        // Wczytanie użytkowników z pliku
        userList = loadUsersFromFile();

        // Obsługa przycisku "Zarejestruj się"
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        // Obsługa przycisku "Powrót do logowania"
        buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLogin();
            }
        });
    }

    // Metoda do wczytywania danych użytkowników z pliku
    private List<User> loadUsersFromFile() {
        List<User> users = new ArrayList<>();
        try (FileInputStream fis = openFileInput("users.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                if (obj instanceof User) {
                    users.add((User) obj);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            Toast.makeText(this, "Błąd podczas wczytywania danych użytkowników", Toast.LENGTH_SHORT).show();
        }
        return users;
    }

    // Metoda do obsługi rejestracji użytkownika
    private void register() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        // Walidacja pól
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Podaj nazwę użytkownika");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Podaj adres email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Podaj hasło");
            return;
        }

        // Sprawdzenie, czy użytkownik o podanej nazwie użytkownika lub adresie email już istnieje
        if (isUserAlreadyExists(username, email)) {
            Toast.makeText(this, "Użytkownik o podanej nazwie użytkownika lub adresie email już istnieje", Toast.LENGTH_SHORT).show();
            return;
        }

        // Można dodać bardziej zaawansowane walidacje, np. sprawdzenie poprawności adresu email

        // Zapis do pliku
        saveUserToFile(username, email, password);

        // Przykład prostego feedbacku dla użytkownika
        Toast.makeText(this, "Zarejestrowano użytkownika: " + username, Toast.LENGTH_SHORT).show();

        // Po zarejestrowaniu użytkownika, przejście do ekranu logowania
        backToLogin();
    }

    // Metoda do sprawdzania, czy użytkownik o podanej nazwie użytkownika lub adresie email już istnieje
    private boolean isUserAlreadyExists(String username, String email) {
        for (User user : userList) {
            if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    // Metoda do zapisywania danych użytkownika do pliku
    private void saveUserToFile(String username, String email, String password) {
        User user = new User(username, email, password);

        try (FileOutputStream fos = openFileOutput("users.dat", MODE_APPEND);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd podczas zapisywania danych użytkownika", Toast.LENGTH_SHORT).show();
        }
    }

    // Metoda do powrotu do ekranu logowania
    private void backToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Zamyka obecne Activity, aby użytkownik nie mógł wrócić przyciskiem "wstecz"
    }
}
