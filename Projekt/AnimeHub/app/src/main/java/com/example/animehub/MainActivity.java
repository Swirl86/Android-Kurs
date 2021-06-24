package com.example.animehub;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends OptionsMenuActivity {

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    private final String url = "https://api.jikan.moe/v3/";
    private final String animeSearch = "search/anime?q=";
    private final String topAnimeSearch = "top/anime/1/upcoming";

    public static Context context = null;

    private DBHelper dBhelper;
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private AnimeListHandler animeListHandler;

    private EditText searchBar;
    private TextView searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // deleteFiles(getApplicationContext().getCacheDir());
        initValues();

        doSearch(topAnimeSearch, "");

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String input = searchBar.getText().toString();
                    doSearch(animeSearch, input);
                    return true;
                }
                return false;
            }
        });
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

        // TODO load 5 per page, set limit or load all
        getURL = type.equals(animeSearch) ? getURL + "&limit=5" : getURL + "";

        // Set title depending on search type
        searchType.setText(type.equals(animeSearch) ? "Search Result For " + input : "Top Upcoming Anime");

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, getURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String valueType = type.equals(animeSearch) ? "results" : "top";
                            JSONArray results = response.getJSONArray(valueType);

                            animeListHandler = new AnimeListHandler(results, recyclerView, valueType);

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

    private void initValues() {
        context = this;
        dBhelper = new DBHelper(context);

        recyclerView = findViewById(R.id.mainSearchRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerviewAdapter = new RecyclerviewAdapter();
        recyclerView.setAdapter(recyclerviewAdapter);

        searchBar = findViewById(R.id.searchBar);
        searchType = findViewById(R.id.searchType);
        searchType.setPaintFlags(searchType.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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

        doSearch(animeSearch, searchBar.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }
}