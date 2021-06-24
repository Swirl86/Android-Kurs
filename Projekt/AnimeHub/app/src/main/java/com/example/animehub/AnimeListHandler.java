package com.example.animehub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnimeListHandler extends MainActivity implements RecyclerviewAdapter.ListItemClickListener {

    private RecyclerviewAdapter recyclerviewAdapter;
    private JSONArray jsonArray;

    private ArrayList<AnimeObject> animeObjects;
    private AnimeObject animeObject;
    private String dataType;

    public AnimeListHandler(JSONArray jsonObjects, RecyclerView recyclerView, String valueType) throws JSONException {

        jsonArray = jsonObjects;
        animeObjects = new ArrayList<>();
        dataType = valueType;

        if (valueType.equals("top")) {
            setTopResultData();
        } else {
            setSearchResultData();
        }

        // recyclerView = recyclerView.findViewById(R.id.animeRecycler);
        // recyclerView.setLayoutManager( new LinearLayoutManager(this));
        recyclerviewAdapter = new RecyclerviewAdapter(animeObjects, this);
        recyclerView.setAdapter(recyclerviewAdapter);
    }

    private void setSearchResultData() throws JSONException {

        JSONObject obj;
        for (int i = 0; i < jsonArray.length(); i++) {
            obj = jsonArray.getJSONObject(i);

            String airing = obj.getString("airing");
            airing = airing == "true" ? "Ongoing" : "Not Ongoing";

            animeObject = new AnimeObject(obj.getString("mal_id"),
                    obj.getString("image_url"), obj.getString("title"),
                    obj.getString("episodes"), obj.getString("synopsis"),
                    airing, obj.getString("score"));

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
                    "", "Rank: " + obj.getString("rank"));

            animeObjects.add(animeObject);
        }
    }

    private String getNotNullValue(String value) {
        return value.equals("null") ? "Unknown" : value;
    }

    @Override
    public void onAnimeClick(int position) {

        Toast.makeText(MainActivity.context, animeObjects.get(position).getTitle(), Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("anime_id", animeObjects.get(position).animeId);
        bundle.putString("data_type", dataType);

        Intent intent = new Intent(MainActivity.context, AnimeDetailsActivity.class);
        //intent.putExtra("anime_id", animeObjects.get(position).animeId);
        intent.putExtra("animeInfo", bundle);
        MainActivity.context.startActivity(intent);
    }

}
