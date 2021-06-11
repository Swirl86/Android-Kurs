package com.example.lifecycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class LogInActivity extends AppCompatActivity {

    final int FLAG_FULLSCREEN = 1024; // Hide extra white space above actionbar

    private final String USER_EMAIL = "admin@test.com";
    private final String USER_PASSWORD = "123456";

    private EditText email, password;
    private CheckBox rememberMe;
    private MaterialCardView login;
    private Context context;
    private Intent intent;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        initValues();

        String loggedIn = sharedPreferences.getString("RememberMe", "");

        if (loggedIn.equals("true")) {
            intent = new Intent(LogInActivity.this, FormActivity.class);
            startActivity(intent);

            makeToast("You are already logged in \uD83D\uDC96");
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = email.getText().toString();
                String inputPassword = password.getText().toString();

                if (valid(inputEmail, inputPassword)) {

                    editor.putString("email", inputEmail);
                    editor.apply();

                    intent = new Intent(LogInActivity.this, FormActivity.class);
                    startActivity(intent);
                    makeToast("Login successful \uD83D\uDC4D");
                } else {
                    makeToast("Wrong input, try again \uD83D\uDCA9");
                }
            }
        });

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String msg = rememberMe.isChecked() ? "Checked \u2713" : "Unchecked \u2717";
                String value = rememberMe.isChecked() ? "true" : "false";
                Toast.makeText(LogInActivity.this, msg, Toast.LENGTH_SHORT).show();
                editor.putString("RememberMe", value);
                editor.apply();
            }
        });

    }

    private void initValues() {
        context = getApplicationContext();

        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.formHobbies);
        rememberMe = findViewById(R.id.rememberMe);
        login = findViewById(R.id.loginButton);

        sharedPreferences = context.getSharedPreferences("UserDB", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private boolean valid(String inputEmail, String inputPassword) {
        return inputEmail.equals(USER_EMAIL) && inputPassword.equals(USER_PASSWORD);
    }

    static Toast toast;

    private void makeToast(String str) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }
}