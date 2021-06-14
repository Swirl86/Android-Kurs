package com.example.lifecycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;

public class FormActivity extends OptionsMenuActivity {

    final int FLAG_FULLSCREEN = 1024;

    private EditText edtTxtName, edtTxtHobbies;
    private MaterialCardView btnSubmit;
    private Spinner countriesSpinner;
    private RadioGroup rgGender;
    private CheckBox agreeChecked;

    private Context context;
    private Intent intent;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_form);

        initValues();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (agreeChecked.isChecked()) {
                    String name = edtTxtName.getText().toString();
                    String hobbies = edtTxtHobbies.getText().toString();

                    hobbies = hobbies.equals("") ? "No hobbies  ¯\\_(ツ)_/¯" : hobbies;

                    if (!name.equals("")) {
                        String country = countriesSpinner.getSelectedItem().toString();
                        String gender = getGenderOption();

                        Bundle bundle = new Bundle();

                        bundle.putString("name", name);
                        bundle.putString("hobbies", hobbies);
                        bundle.putString("country", country);
                        bundle.putString("gender", gender);

                        saveBundleInformation(bundle);

                        intent = new Intent(FormActivity.this, SavedFormsActivity.class);
                        intent.putExtra("formBundle", bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } else {
                        makeToast("Please, enter your name \uD83D\uDCA9");
                    }
                } else {
                    makeToast("Please, agree to our terms to continue \uD83E\uDD1E");
                }
            }
        });
    }

    private void initValues() {

        context = getApplicationContext();
        sharedPreferences = getApplicationContext().getSharedPreferences("UserDB", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        edtTxtName = (EditText) findViewById(R.id.formName);
        edtTxtHobbies = (EditText) findViewById(R.id.formHobbies);

        btnSubmit = (MaterialCardView) findViewById(R.id.submitButton);

        countriesSpinner = (Spinner) findViewById(R.id.spinnerCountry);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        agreeChecked = (CheckBox) findViewById(R.id.checkBoxAgreement);
    }

    private void saveBundleInformation(Bundle bundle) {
        for (String key : bundle.keySet()) {
            editor.putString(key, (String) bundle.get(key));
        }
        editor.apply();
    }

    private String getGenderOption() {
        String gender = "";

        switch (rgGender.getCheckedRadioButtonId()) {
            case R.id.rbMale:
                gender = "Male";
                break;
            case R.id.rbFemale:
                gender = "Female";
                break;
            case R.id.rbOther:
                gender = "Other";
                break;
            default:
                gender = "Unknown";
                break;
        }

        return gender;
    }

    static Toast toast;

    private void makeToast(String str) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
}