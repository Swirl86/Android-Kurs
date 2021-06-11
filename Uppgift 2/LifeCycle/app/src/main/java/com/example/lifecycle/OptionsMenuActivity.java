package com.example.lifecycle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class OptionsMenuActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    Intent intent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_form:
                //makeToast("Form option selected");
                intent = new Intent(OptionsMenuActivity.this, FormActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_saved:
                //makeToast("Saved option selected");
                intent = new Intent(OptionsMenuActivity.this, SavedFormsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                //("Logout option selected");
                sharedPreferences = this.getSharedPreferences("UserDB", MODE_PRIVATE);
                sharedPreferencesEditor = sharedPreferences.edit();

                sharedPreferencesEditor.putString("RememberMe", "false");
                sharedPreferencesEditor.apply();

                intent = new Intent(OptionsMenuActivity.this, LogInActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
