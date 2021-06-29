package com.example.animehub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.File;

public class MainActivity extends OptionsMenuActivity {

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    // GET commands
    private final String url = "https://api.jikan.moe/v3/";
    private final String animeSearch = "search/anime?q=";
    private final String topUpcomingAnimeSearch = "top/anime/1/upcoming";
    private final String ongoingAnimeSearch = "top/anime/1/airing";
    private final String popularAnimeSearch = "search/anime?q=&order_by=score&sort=desc&page=1";

    /*  https://jikan.docs.apiary.io/#introduction
        Rate Limiting:
        Daily Limit: Unlimited
        30 requests / minute
        2 requests / second

        Add a delay of 4 (FOUR) SECONDS between each request
     */
    public long jsonRequestTimeStamp;
    public long newRequestTimeStamp;

    // Static so AnimeListHandler can use Context
    public static Context context = null;
    private Intent intent;
    private DBHelper dBhelper;
    private RecyclerView recyclerView;
    private RecyclerviewListAdapter recyclerviewListAdapter;

    private String bSearchType;

    private EditText searchBar;
    private TextView searchType;
    private ProgressBar progressBar;
    private FloatingActionButton scrollUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // deleteFiles(getApplicationContext().getCacheDir());
        initValues();

        callChosenSearch();

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && validTimeRange() && validString()) {
                    String input = searchBar.getText().toString();
                    if (input.equals("")) {
                        searchType.setText("Top Upcoming Anime");
                        doSearch(topUpcomingAnimeSearch, "");
                    } else {
                        searchType.setText("Search Result For " + input);
                        doSearch(animeSearch, input);
                    }
                    return true;
                }
                return false;
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
                //Position 0 scroll to the beginning of recyclerView
                recyclerView.smoothScrollToPosition(0);
                scrollUp.setVisibility(View.GONE);
            }
        });
    }

    private void callChosenSearch() {
        System.out.println("----> " + bSearchType);

        switch (bSearchType) {
            case "action_ongoing":
                searchType.setText("Top Ongoing Anime");
                doSearch(ongoingAnimeSearch, "");
                break;
            case "action_popular":
                searchType.setText("Top Popular Anime");
                doSearch(popularAnimeSearch, "");
                break;
            default:
                searchType.setText("Top Upcoming Anime");
                doSearch(topUpcomingAnimeSearch, "");
        }
    }

    private void deleteFiles(File dir) {
        if (dir != null) {
            if (dir.listFiles() != null && dir.listFiles().length > 0) {
                // RECURSIVELY DELETE FILES IN DIRECTORY
                for (File file : dir.listFiles()) {
                    deleteFiles(file);
                }
            } else {
                // JUST DELETE FILE
                dir.delete();
            }
        }
    }

    private void doSearch(String type, String input) {

        String getURL = url + type + input.trim();

        getURL = type.equals(animeSearch) ? getURL + "&limit=5" : getURL + "";

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, getURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonRequestTimeStamp = System.currentTimeMillis();
                            String valueType = type.equals(animeSearch) || type.equals(popularAnimeSearch) ? "results" : "top";
                            JSONArray results = response.getJSONArray(valueType);

                            new AnimeListHandler(results, recyclerView, valueType);
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


    private boolean validTimeRange() {
        newRequestTimeStamp = System.currentTimeMillis();
        return newRequestTimeStamp - jsonRequestTimeStamp >= 4000;
    }


    private boolean validString() {
        if (searchBar.getText().toString().length() >= 3) {
            return true;
        }
        Toast.makeText(context, "Search need to be at least 3 letters", Toast.LENGTH_SHORT).show();
        return false;
    }


    private void initValues() {
        context = this;
        intent = getIntent();
        dBhelper = new DBHelper(context);

        jsonRequestTimeStamp = 0L;
        newRequestTimeStamp = 4000L;

        recyclerView = findViewById(R.id.mainSearchRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerviewListAdapter = new RecyclerviewListAdapter();
        recyclerView.setAdapter(recyclerviewListAdapter);

        searchBar = findViewById(R.id.searchBar);
        searchType = findViewById(R.id.searchType);
        searchType.setPaintFlags(searchType.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        progressBar = findViewById(R.id.progressBarMain);

        scrollUp = findViewById(R.id.scrollUpMain);
        scrollUp.setVisibility(View.GONE);

        if (intent.hasExtra("search_type")) {
            bSearchType = intent.getExtras().getString("search_type", "");
        } else {
            bSearchType = "";
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putString("searchInput", searchBar.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        searchBar.setText(savedInstanceState.getString("searchInput"));

        doSearch(animeSearch, searchBar.getText().toString()); // TODO handle 4 SECONDS DELAY

        super.onSaveInstanceState(savedInstanceState);
    }
}