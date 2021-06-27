package com.example.animehub;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class AnimeDetailsActivity extends OptionsMenuActivity {

    private final String url = "https://api.jikan.moe/v3/anime/";
    private Context context;
    private DBHelper dBhelper;

    private String animeId, imageUrl, airing, score, title, episodes, synopsis;
    private String dataType;

    private TextView titleTextView, episodesTextView, durationTextView,
            statusTextView, premieredTextView, synopsisTextView;

    private ImageView imageAnime;
    private MaterialButton favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initValues();
        fetchAnimeData();

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onAnimeClick", "favoriteBtn CLICKED ");
                if (dBhelper.detailsExists(animeId)) {
                    favoriteButton.setIconTintResource(R.color.white);
                    dBhelper.deleteAnimeObject(animeId);
                } else {
                    favoriteButton.setIconTintResource(R.color.red);// Create table entry
                    AnimeObject details = new AnimeObject(animeId, imageUrl, title,
                            episodes, synopsis, airing, score);

                    dBhelper.insertWeatherDetails(details);
                }
            }
        });
    }

    private void fetchAnimeData() {
        RequestQueue queue = Volley.newRequestQueue(context);

        String getURL = url + animeId;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, getURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("onAnimeClick", "onResponse: " + response);

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

                            imageUrl = response.getString("image_url");
                            Picasso.get().load(imageUrl).into(imageAnime);

                            title = getNotNullValue(response.getString("title"));
                            synopsis = getNotNullValue(response.getString("synopsis"));

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

    private String getNotNullValue(String value) {
        return value.equals("null") ? "Unknown" : value;
    }

    private void initValues() {

        context = getApplicationContext();
        dBhelper = new DBHelper(context);

        Bundle bundle = getIntent().getBundleExtra("animeInfo");
        animeId = bundle.getString("anime_id", "DefaultValue");
        dataType = bundle.getString("data_type", "DefaultValue");
        Log.d("onAnimeClick", "bundle: " + animeId + "  " + dataType);

        // animeId = getIntent().getExtras().getString("anime_id", "defaultKey");

        imageAnime = findViewById(R.id.detailViewImage);
        titleTextView = findViewById(R.id.detailViewTitle);
        episodesTextView = findViewById(R.id.detailViewEpisodes);
        durationTextView = findViewById(R.id.detailViewDuration);
        statusTextView = findViewById(R.id.detailViewStatus);
        premieredTextView = findViewById(R.id.detailViewPremiered);

        synopsisTextView = findViewById(R.id.detailViewDescription);
        synopsisTextView.setMovementMethod(new ScrollingMovementMethod());

        favoriteButton = findViewById(R.id.favoriteButton);

        if (dBhelper.detailsExists(animeId)) {
            favoriteButton.setIconTintResource(R.color.red);
        }
    }
}