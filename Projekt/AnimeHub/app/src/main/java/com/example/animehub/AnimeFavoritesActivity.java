package com.example.animehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class AnimeFavoritesActivity extends OptionsMenuActivity implements RecyclerviewAdapter.ListItemClickListener {

    public static Context context = null;
    private DBHelper dBhelper;
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;

    private ArrayList<AnimeObject> animeObjects;
    private AnimeObject animeObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_favorites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initValues();
        Log.d("onFavClick", "onFavClick: ");

        animeObjects = dBhelper.getAnimeList(animeObjects);
        recyclerviewAdapter = new RecyclerviewAdapter(animeObjects, this);
        recyclerView.setAdapter(recyclerviewAdapter);
    }

    private void initValues() {

        animeObjects = new ArrayList<>();

        context = getApplicationContext();
        dBhelper = new DBHelper(context);

        recyclerView = findViewById(R.id.favoriteListRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onAnimeClick(int position) {
        Toast.makeText(MainActivity.context, animeObjects.get(position).getTitle(), Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("anime_id", animeObjects.get(position).animeId);
        bundle.putString("data_type", "top"); // TODO if more types are added update here

        Intent intent = new Intent(context, AnimeDetailsActivity.class);
        //intent.putExtra("anime_id", animeObjects.get(position).animeId);
        intent.putExtra("animeInfo", bundle);
        startActivity(intent);
    }
}