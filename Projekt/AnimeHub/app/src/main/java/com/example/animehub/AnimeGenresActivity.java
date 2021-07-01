package com.example.animehub;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnimeGenresActivity extends OptionsMenuActivity implements RecyclerviewListAdapter.ListItemClickListener {

    private final String url = "https://api.jikan.moe/v3/search/anime?q=&page=1&genre=";

    private Context context;
    private RecyclerView recyclerView;
    private RecyclerviewListAdapter recyclerviewListAdapter;
    private ArrayList<AnimeObject> animeObjects;
    private AnimeObject animeObject;

    private FloatingActionButton scrollUp;
    private TextView title;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_genres);

        initValues();

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

        doGetRequest();

    }

    private void doGetRequest() {
        Bundle bundle = getIntent().getBundleExtra("genreInfo");
        String genre = bundle.getString("genre_id", "1");
        String type = bundle.getString("genre_type", "Action");
        title.append(" " + type);

        String getURL = url + genre + "&order_by=score";

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, getURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray result = response.getJSONArray("results");
                            setResultData(result);
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("volley", "onErrorResponse: " + error);
            }
        });

        queue.add(jsonRequest);
    }

    private void setResultData(JSONArray jsonArray) throws JSONException {

        JSONObject obj;
        for (int i = 0; i < jsonArray.length(); i++) {
            obj = jsonArray.getJSONObject(i);

            String airing = obj.getString("airing");
            airing = airing.equals("true") ? "Ongoing" : "Not Ongoing";

            animeObject = new AnimeObject(obj.getString("mal_id"),
                    obj.getString("image_url"), obj.getString("title"),
                    obj.getString("episodes"), obj.getString("synopsis"),
                    airing, obj.getString("score") + " \u2b50",
                    obj.getString("url"));
            animeObjects.add(animeObject);
        }

        recyclerviewListAdapter = new RecyclerviewListAdapter(animeObjects, this);
        recyclerView.setAdapter(recyclerviewListAdapter);
    }

    private void initValues() {
        context = getApplicationContext();
        recyclerView = findViewById(R.id.generaListRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        animeObjects = new ArrayList<>();

        scrollUp = findViewById(R.id.scrollUpGenera);
        scrollUp.setVisibility(View.GONE);
        title = findViewById(R.id.generaTitle);

        progressBar = findViewById(R.id.progressBarGenre);
    }

    @Override
    public void onAnimeClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("anime_id", animeObjects.get(position).animeId);
        bundle.putString("data_type", "results");

        Intent intent = new Intent(context, AnimeDetailsActivity.class);
        intent.putExtra("animeInfo", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}