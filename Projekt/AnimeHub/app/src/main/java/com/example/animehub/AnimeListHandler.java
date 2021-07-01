package com.example.animehub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnimeListHandler extends Fragment implements RecyclerviewListAdapter.ListItemClickListener {

    private RecyclerviewListAdapter recyclerviewListAdapter;
    private JSONArray jsonArray;

    private ArrayList<AnimeObject> animeObjects;
    private AnimeObject animeObject;
    private String dataType;
    private Context context;

    public AnimeListHandler() {
        // Required empty public constructor
    }

    public AnimeListHandler(Context context, JSONArray jsonObjects,
                            RecyclerView recyclerView, String valueType) throws JSONException {

        this.context = context;
        jsonArray = jsonObjects;
        animeObjects = new ArrayList<>();
        dataType = valueType;

        if (valueType.equals("top")) {
            setTopResultData();
        } else {
            setSearchResultData();
        }

        recyclerviewListAdapter = new RecyclerviewListAdapter(animeObjects, this);
        recyclerView.setAdapter(recyclerviewListAdapter);
    }

    private void setSearchResultData() throws JSONException {

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
    }

    private void setTopResultData() throws JSONException {

        JSONObject obj;
        for (int i = 0; i < jsonArray.length(); i++) {
            obj = jsonArray.getJSONObject(i);


            animeObject = new AnimeObject(obj.getString("mal_id"),
                    obj.getString("image_url"), obj.getString("title"),
                    getNotNullValue(obj.getString("episodes")),
                    "Start Date: " + getNotNullValue(obj.getString("start_date")),
                    "", "Rank: " + obj.getString("rank"),
                    obj.getString("url"));

            animeObjects.add(animeObject);
        }
    }

    private String getNotNullValue(String value) {
        return value.equals("null") ? "Unknown" : value;
    }

    @Override
    public void onAnimeClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("anime_id", animeObjects.get(position).animeId);
        bundle.putString("data_type", dataType);

        Intent intent = new Intent(this.context, AnimeDetailsActivity.class);
        intent.putExtra("animeInfo", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
