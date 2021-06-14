package com.example.lifecycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class SignUpActivity extends AppCompatActivity {

    final int FLAG_FULLSCREEN = 1024;

    private TextView loginLink;
    private EditText email, password, rePassword;
    private MaterialCardView signUpBtn;
    private Context context;
    private Intent intent;
    private DBHelper db;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        initValues();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = email.getText().toString();
                String passwordInput = password.getText().toString();
                String rePasswordInput = rePassword.getText().toString();

                boolean validInput = checkInput(emailInput, passwordInput, rePasswordInput);
                if (validInput) {
                    Boolean userExists = db.checkEmail(emailInput);

                    if (userExists) {
                        makeToast("User already exists \uD83D\uDE44 \nPlease Login");
                    } else {
                        Boolean sigUpResult = db.insertUserData(emailInput, passwordInput);
                        handleResult(sigUpResult);
                    }
                }
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                makeToast("Please Login");
            }
        });

    }

    private void handleResult(Boolean newUser) {
        String msg = newUser ? "Sign Up Successful \uD83D\uDC4D" : "Sign Up Failed \uD83D\uDCA9";
        makeToast(msg);

        if (newUser) {
            intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }

    private boolean checkInput(String emailInput, String passwordInput, String rePasswordInput) {
        if (emptyInput(emailInput, passwordInput, rePasswordInput)) {
            makeToast("Need all input, try again \uD83D\uDCA9");
            return false;
        } else if (!notEqualPassword(passwordInput, rePasswordInput)) {
            makeToast("Password does not match \uD83D\uDCA9");
            return false;
        }
        return true;
    }

    private boolean notEqualPassword(String passwordInput, String rePasswordInput) {
        return passwordInput.equals(rePasswordInput);
    }

    private boolean emptyInput(String emailInput, String passwordInput, String rePasswordInput) {
        return emailInput.equals("") || passwordInput.equals("") || rePasswordInput.equals("");
    }

    private void initValues() {
        context = getApplicationContext();

        loginLink = findViewById(R.id.loginLink);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        rePassword = findViewById(R.id.signupRePassword);
        signUpBtn = findViewById(R.id.signUpBtn);

        sharedPreferences = context.getSharedPreferences("UserDB", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        db = new DBHelper(context);
    }

    static Toast toast;

    private void makeToast(String str) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }
}