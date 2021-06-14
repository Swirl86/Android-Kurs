package com.example.lifecycle;

import android.content.Context;
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

    private Context context;

    private DBHelper db;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_saved_forms);

        initValues();

        getUserDetails();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = sharedPreferences.getString("Email", "");
                if (db.deleteUserDetails(email)) {
                    finish();
                } else {
                    Toast.makeText(context, "Nothing to delete \uD83D\uDE44", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initValues() {

        context = getApplicationContext();
        db = new DBHelper(context);

        sharedPreferences = context.getSharedPreferences("localPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        textEmail = (TextView) findViewById(R.id.savedFormEmail);
        textName = (TextView) findViewById(R.id.savedFormName);
        textHobbies = (TextView) findViewById(R.id.savedFormHobbies);

        countriesSpinner = (Spinner) findViewById(R.id.savedSpinnerCountry);
        countriesSpinner.setEnabled(false);

        agreeChecked = (CheckBox) findViewById(R.id.savedCheckBoxAgreement);

        deleteButton = (ImageButton) findViewById(R.id.deleteButton);
    }

    private void getUserDetails() {
        String email = sharedPreferences.getString("Email", "");
        UserDetailsEntity details = db.getUserDetails(email);

        textName.setText(details.getName());
        textHobbies.setText(details.getHobbies());

        setRadioButton(details.getGender());

        int county = ((ArrayAdapter<String>) countriesSpinner.getAdapter()).getPosition(details.getCountry());
        countriesSpinner.setSelection(county);
        agreeChecked.setChecked(true);

        System.out.println(details.toString());
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