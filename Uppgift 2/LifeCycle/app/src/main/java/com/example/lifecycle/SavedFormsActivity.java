package com.example.lifecycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SavedFormsActivity extends OptionsMenuActivity {

    final int FLAG_FULLSCREEN = 1024;

    private TextView textEmail, textName, textHobbies;
    private Spinner countriesSpinner;
    private RadioButton rbOption;
    private CheckBox agreeChecked;
    private ImageButton deleteButton;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Context context;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_saved_forms);

        initValues();

        Bundle formBundle = intent.getBundleExtra("formBundle");

        if (formBundle != null) {
            getBundleInData(formBundle);
        } else {
            getSharedPreferencesData();
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean gotData = !sharedPreferences.getString("name", "").equals("");
                if (gotData) {
                    String email = sharedPreferences.getString("email", "");
                    String rememberMe = sharedPreferences.getString("RememberMe", "");
                    editor.clear();

                    editor.putString("RememberMe", rememberMe);
                    editor.putString("email", email);
                    editor.apply();
                    finish();
                } else {
                    Toast.makeText(context, "Nothing to delete \uD83D\uDE44", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initValues() {

        sharedPreferences = getApplicationContext().getSharedPreferences("UserDB", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        context = getApplicationContext();
        intent = getIntent();

        textEmail = (TextView) findViewById(R.id.savedFormEmail);
        textName = (TextView) findViewById(R.id.savedFormName);
        textHobbies = (TextView) findViewById(R.id.savedFormHobbies);

        countriesSpinner = (Spinner) findViewById(R.id.savedSpinnerCountry);
        countriesSpinner.setEnabled(false);

        agreeChecked = (CheckBox) findViewById(R.id.savedCheckBoxAgreement);

        deleteButton = (ImageButton) findViewById(R.id.deleteButton);
    }

    private void getBundleInData(Bundle formBundle) {
        String formNameValue = formBundle.getString("name");
        String formHobbiesValue = formBundle.getString("hobbies");
        String formCountryValue = formBundle.getString("country");
        String formGenderValue = formBundle.getString("gender");

        textName.setText(formNameValue);
        textHobbies.setText(formHobbiesValue);

        setRadioButton(formGenderValue);

        int county = ((ArrayAdapter<String>) countriesSpinner.getAdapter()).getPosition(formCountryValue);
        countriesSpinner.setSelection(county);
        agreeChecked.setChecked(true);
    }


    private void getSharedPreferencesData() {
        textEmail.append(sharedPreferences.getString("email", ""));
        textName.setText(sharedPreferences.getString("name", ""));
        textHobbies.setText(sharedPreferences.getString("hobbies", ""));

        setRadioButton(sharedPreferences.getString("gender", "Other"));

        String formCountryValue = sharedPreferences.getString("country", "Unknown");
        int county = ((ArrayAdapter<String>) countriesSpinner.getAdapter()).getPosition(formCountryValue);
        countriesSpinner.setSelection(county);
        agreeChecked.setChecked(true);
    }

    private void setRadioButton(String option) {
        switch (option) {
            case "Male":
                rbOption = findViewById(R.id.savedRbMale);
                rbOption.setChecked(true);
                break;
            case "Female":
                rbOption = findViewById(R.id.savedRbFemale);
                rbOption.setChecked(true);
                break;
            default:
                rbOption = findViewById(R.id.savedRbOther);
                rbOption.setChecked(true);
                break;
        }
    }
}