package com.example.animehub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OptionsMenuActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                makeToast("Go to Favorites!");
                intent = new Intent(OptionsMenuActivity.this, AnimeFavoritesActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            case R.id.action_account:
                makeToast("Login/Logout");
               /* intent = new Intent(OptionsMenuActivity.this, FormActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);*/
                return true;
            case R.id.action_search:
                makeToast("Go to search by title!");
               /* intent = new Intent(OptionsMenuActivity.this, FormActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);*/
                return true;
            case R.id.action_ongoing:
                makeToast("See ongoing!");
               /* intent = new Intent(OptionsMenuActivity.this, FormActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);*/
                return true;
            case R.id.action_upcoming:
                makeToast("See upcoming!");
               /* intent = new Intent(OptionsMenuActivity.this, FormActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);*/
                return true;
            case R.id.action_latest:
                makeToast("See latest updates!");
               /* intent = new Intent(OptionsMenuActivity.this, FormActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);*/
                return true;
            case R.id.action_popular:
                makeToast("See most popular!");
               /* intent = new Intent(OptionsMenuActivity.this, FormActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
