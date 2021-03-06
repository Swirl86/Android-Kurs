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
import android.view.inputmethod.InputMethodManager;
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

public class MainActivity extends OptionsMenuActivity {

    private final String KEY_SEARCH_STATE = "search_state";
    // Maintain the state of recyclerview
    private final String KEY_RECYCLE_STATE = "recycle_state";
    private final String KEY_RECYCLE_POSITION = "recycle_position";
    private int mPosition = RecyclerView.NO_POSITION;

    private Bundle bundleRecyclerViewState;
    private Parcelable recyclerListState;

    // GET commands
    private final String url = "https://api.jikan.moe/v3/";
    private final String animeTitleSearch = "search/anime?q=";
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
    private Boolean firstSearch;

    public Context context;
    private Intent intent;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerviewListAdapter recyclerviewListAdapter;

    private String bundleSearchType;

    private EditText searchBar;
    private TextView searchType;
    private ProgressBar progressBar;
    private FloatingActionButton scrollUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initValues();
        callChosenSearch();

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && validTimeRange() && validInput()) {
                    String input = searchBar.getText().toString();
                    String searchTypeTitle = "Search Result For " + input;
                    searchType.setText(searchTypeTitle);
                    doSearch(animeTitleSearch, input);
                    dismissKeyboard();
                    recyclerView.requestFocus();
                    return true;
                }
                dismissKeyboard();
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
        String title = "";
        searchBar.setText("");
        switch (bundleSearchType) {
            case "action_ongoing":
                title = "Current Ongoing Anime";
                searchType.setText(title);
                doSearch(ongoingAnimeSearch, "");
                break;
            case "action_popular":
                title = "Highest Ranked Anime";
                searchType.setText(title);
                doSearch(popularAnimeSearch, "");
                break;
            default:
                title = "Upcoming Anime";
                searchType.setText(title);
                doSearch(topUpcomingAnimeSearch, "");
        }
    }

    private void doSearch(String type, String input) {

        String getURL = url + type + input.trim();

        getURL = type.equals(animeTitleSearch) ? getURL + "&limit=20" : getURL + "";

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, getURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonRequestTimeStamp = System.currentTimeMillis();

                            String valueType = type.equals(animeTitleSearch) ||
                                    type.equals(popularAnimeSearch) ? "results" : "top";
                            JSONArray results = response.getJSONArray(valueType);
                            new AnimeListHandler(context, results, recyclerView, valueType);
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
        if (newRequestTimeStamp - jsonRequestTimeStamp >= 4000) {
            return true;
        } else if (firstSearch) {
            return true;
        }
        Toast.makeText(context, "\u23f1\ufe0f Whoops, try again \u23f1\ufe0f", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean validInput() {
        if (searchBar.getText().toString().length() >= 3) {
            return true;
        }
        Toast.makeText(context, "Need at least 3 letters \ud83d\udc7b", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
    }

    private void initValues() {
        context = this;
        intent = getIntent();

        jsonRequestTimeStamp = 0L;
        newRequestTimeStamp = 0L;
        firstSearch = true;

        recyclerView = findViewById(R.id.mainSearchRecycler);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerviewListAdapter = new RecyclerviewListAdapter();
        recyclerView.setAdapter(recyclerviewListAdapter);
        searchBar = findViewById(R.id.searchBar);
        searchType = findViewById(R.id.searchType);
        searchType.setPaintFlags(searchType.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        progressBar = findViewById(R.id.progressBarMain);

        scrollUp = findViewById(R.id.scrollUpMain);
        scrollUp.setVisibility(View.GONE);

        // Menu search option clicked, sends type with bundle
        if (intent.hasExtra("search_type")) {
            bundleSearchType = intent.getExtras().getString("search_type", "");
        } else {
            bundleSearchType = "";
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // save RecyclerView state
        bundleRecyclerViewState = new Bundle();
        recyclerListState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundleRecyclerViewState.putParcelable(KEY_RECYCLE_STATE, recyclerListState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // restore RecyclerView state
        if (bundleRecyclerViewState != null) {
            recyclerListState = bundleRecyclerViewState.getParcelable(KEY_RECYCLE_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerListState);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putString(KEY_SEARCH_STATE, searchBar.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        searchBar.setText(savedInstanceState.getString(KEY_SEARCH_STATE));
        super.onSaveInstanceState(savedInstanceState);
    }

}