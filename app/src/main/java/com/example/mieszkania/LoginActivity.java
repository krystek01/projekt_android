package com.example.mieszkania;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;

    private List<User> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicjalizacja pól EditText i przycisków
        editTextUsername = findViewById(R.id.loginEditText);
        editTextPassword = findViewById(R.id.passwordEditText);
        buttonLogin = findViewById(R.id.loginButton);
        buttonRegister = findViewById(R.id.registerButton);

        // Wczytanie użytkowników z pliku
        userList = loadUsersFromFile();

        // Obsługa przycisku "Zaloguj się"
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Obsługa przycisku "Zarejestruj się"
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
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
//            stworzenie pliku z użytkownikami
            User user1 = new User("admin", "admin","admin");
            User user2 = new User("user", "user","user");
            users.add(user1);
            users.add(user2);

        }
        return users;
    }

    // Metoda do obsługi logowania użytkownika
    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Podaj nazwę użytkownika i hasło", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sprawdzenie poprawności danych logowania
        boolean loginSuccessful = false;
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loginSuccessful = true;
                break;
            }
        }

        if (loginSuccessful) {
            Toast.makeText(this, "Zalogowano pomyślnie: " + username, Toast.LENGTH_SHORT).show();
            // Przejście do kolejnej aktywności po zalogowaniu
            // np. MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("USERNAME", username); // Przekazanie nazwy użytkownika do MainActivity
            startActivity(intent);
            finish(); // Zamyka obecne Activity, aby użytkownik nie mógł wrócić przyciskiem "wstecz"
        } else {
            Toast.makeText(this, "Nieprawidłowa nazwa użytkownika lub hasło", Toast.LENGTH_SHORT).show();
        }
    }

    // Metoda do przejścia do ekranu rejestracji
    private void goToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        // Nie wywołujemy finish() tutaj, aby użytkownik mógł wrócić do ekranu logowania
    }
}
