package com.example.animehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AnimeFavoritesActivity extends OptionsMenuActivity implements RecyclerviewListAdapter.ListItemClickListener {

    public static Context context = null;
    private DBHelper dBhelper;
    private RecyclerView recyclerView;
    private RecyclerviewListAdapter recyclerviewListAdapter;

    private ImageButton deleteButton;
    private FloatingActionButton scrollUp;

    private ArrayList<AnimeObject> animeObjects;
    //private AnimeObject animeObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_favorites);

        initValues();

        animeObjects = dBhelper.getAnimeList(animeObjects);
        recyclerviewListAdapter = new RecyclerviewListAdapter(animeObjects, this);
        recyclerView.setAdapter(recyclerviewListAdapter);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dBhelper.isEmpty()) {
                    Toast.makeText(AnimeFavoritesActivity.this,
                            "Nothing to delete.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AnimeFavoritesActivity.this);
                    builder.setMessage("Do you want to delete the list?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dBhelper.deleteAllObjects();
                                    Toast.makeText(AnimeFavoritesActivity.this,
                                            "List is deleted \uD83D\uDC4D", Toast.LENGTH_SHORT).show();
                                    finish();
                                    intent = new Intent(context, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   /* Toast.makeText(AnimeFavoritesActivity.this,
                                            "\uD83D\uDC96", Toast.LENGTH_SHORT).show();*/
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    builder.setTitle("Delete List");
                    alert.show();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int x, int y) {
                // y is the change in the vertical scroll position
                if (y < 0) {
                    //scroll up
                    scrollUp.setVisibility(View.GONE);
                } else if (y > 0) {
                    //scroll down
                    scrollUp.setVisibility(View.VISIBLE);
                }
            }
        });

        scrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
                scrollUp.setVisibility(View.GONE);
            }
        });
    }

    private void initValues() {

        animeObjects = new ArrayList<>();

        deleteButton = findViewById(R.id.deleteButton);

        scrollUp = findViewById(R.id.scrollUpFavorites);
        scrollUp.setVisibility(View.GONE);

        context = getApplicationContext();
        dBhelper = new DBHelper(context);

        recyclerView = findViewById(R.id.favoriteListRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onAnimeClick(int position) {
        //Toast.makeText(MainActivity.context, animeObjects.get(position).getTitle(), Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("anime_id", animeObjects.get(position).animeId);
        bundle.putString("data_type", "top"); // TODO if more types are added update here

        Intent intent = new Intent(context, AnimeDetailsActivity.class);
        //intent.putExtra("anime_id", animeObjects.get(position).animeId);
        intent.putExtra("animeInfo", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}