package com.example.animehub;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OptionsMenuActivity extends AppCompatActivity {

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
            case R.id.action_favorites:
                intent = new Intent(OptionsMenuActivity.this, AnimeFavoritesActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            case R.id.action_home:
                intent = new Intent(OptionsMenuActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
