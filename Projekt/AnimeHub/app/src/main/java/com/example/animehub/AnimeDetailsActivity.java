package com.example.animehub;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class AnimeDetailsActivity extends OptionsMenuActivity {

    private final String searchUrl = "https://api.jikan.moe/v3/anime/";
    private Context context;
    private Intent intent;
    private DBHelper dBhelper;
    private GeneraOptions generaOptions;

    private String animeId, imageUrl, airing, score, title, episodes, synopsis, url;
    private String dataType;

    private TextView titleTextView, episodesTextView, durationTextView,
            statusTextView, premieredTextView, synopsisTextView;

    private ImageView imageAnime;
    private MaterialButton favoriteButton, urlButton;
    private ChipGroup genresLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_details);

        initValues();
        fetchAnimeData();

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dBhelper.detailsExists(animeId)) {
                    favoriteButton.setIconTintResource(R.color.white);
                    dBhelper.deleteAnimeObject(animeId);
                } else {
                    favoriteButton.setIconTintResource(R.color.red);// Create table entry
                    AnimeObject details = new AnimeObject(animeId, imageUrl, title,
                            episodes, synopsis, airing, score, url);

                    dBhelper.insertAnimeDetails(details);
                }
            }
        });

        urlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void fetchAnimeData() {
        RequestQueue queue = Volley.newRequestQueue(context);

        String getURL = searchUrl + animeId;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, getURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            episodes = response.getString("episodes");

                            if (dataType.equals("top")) {
                                airing = "";
                                score = "Status: " + response.getString("status");
                                episodes = episodes.equals("null") ? "?" : episodes;
                            } else {
                                airing = response.getString("airing") == "true" ? "Ongoing" : "Not Ongoing";
                                score = getNotNullValue(response.getString("score"));
                                episodes = getNotNullValue(episodes);
                            }


                            setGenres(response.getJSONArray("genres"));

                            imageUrl = response.getString("image_url");
                            Picasso.get().load(imageUrl).into(imageAnime);

                            title = getNotNullValue(response.getString("title"));
                            synopsis = getNotNullValue(response.getString("synopsis"));

                            url = getNotNullValue(response.getString("url"));

                            titleTextView.setText(title);
                            episodesTextView.setText(episodes);
                            durationTextView.setText(getNotNullValue(response.getString("duration")));
                            statusTextView.setText(getNotNullValue(response.getString("status")));
                            premieredTextView.setText(getNotNullValue(response.getString("premiered")));
                            synopsisTextView.setText(synopsis);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        });

        queue.add(jsonRequest);
    }

    private void setGenres(JSONArray genres) throws JSONException {
        JSONObject genreJSONObject;
        Chip genre;
        String type = "";
        int id = -1;

        genresLayout.removeAllViews();

        for (int i = 0; i < genres.length(); i++) {
            genreJSONObject = genres.getJSONObject(i);
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            // genre.setLayoutParams(params);

            id = genreJSONObject.getInt("mal_id");
            type = genreJSONObject.getString("name");
            genre = (Chip) new Chip(genresLayout.getContext());
            genre.setId(id);
            genre.setText(type);
            genre.setChipBackgroundColor(ColorStateList.valueOf(getRandomColor()));
            genre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int genre = ((Chip) view).getId();
                    String type = ((Chip) view).getText().toString();

                    Bundle bundle = new Bundle();
                    bundle.putString("genre_id", String.valueOf(genre));
                    bundle.putString("genre_type", type);

                    intent = new Intent(context, AnimeGenresActivity.class);
                    // intent.putExtra("genre", genre);
                    intent.putExtra("genreInfo", bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });

            genresLayout.addView(genre);
        }
    }

    // minimumBrightnessRatio between 0 and 1. 1 = all white, 0 = any possible color
    private int getRandomColor() {
        Random random = new Random();
        float minimumBrightnessRatio = 0.5F;
        int minimumColorValue = (int) (255f * minimumBrightnessRatio);
        int variableColorValue = 255 - minimumColorValue;

        int r = minimumColorValue + random.nextInt(variableColorValue);
        int g = minimumColorValue + random.nextInt(variableColorValue);
        int b = minimumColorValue + random.nextInt(variableColorValue);

        return Color.argb(255, r, g, b);
    }

    private String getNotNullValue(String value) {
        return value.equals("null") ? "Unknown" : value;
    }

    private void initValues() {

        context = getApplicationContext();
        dBhelper = new DBHelper(context);
        generaOptions = new GeneraOptions();

        Bundle bundle = getIntent().getBundleExtra("animeInfo");
        animeId = bundle.getString("anime_id", "1");
        dataType = bundle.getString("data_type", "Action");

        imageAnime = (ImageView) findViewById(R.id.detailViewImage);
        titleTextView = (TextView) findViewById(R.id.detailViewTitle);
        episodesTextView = (TextView) findViewById(R.id.detailViewEpisodes);
        durationTextView = (TextView) findViewById(R.id.detailViewDuration);
        statusTextView = (TextView) findViewById(R.id.detailViewStatus);
        premieredTextView = (TextView) findViewById(R.id.detailViewPremiered);

        synopsisTextView = (TextView) findViewById(R.id.detailViewDescription);
        synopsisTextView.setMovementMethod(new ScrollingMovementMethod());

        genresLayout = (ChipGroup) findViewById(R.id.chipGroup);

        favoriteButton = (MaterialButton) findViewById(R.id.favoriteButton);
        urlButton = (MaterialButton) findViewById(R.id.urlButton);

        if (dBhelper.detailsExists(animeId)) {
            favoriteButton.setIconTintResource(R.color.red);
        }
    }
}