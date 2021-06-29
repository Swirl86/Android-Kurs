package com.example.animehub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerviewListAdapter extends RecyclerView.Adapter<RecyclerviewListAdapter.ViewHolder> {

    private ArrayList<AnimeObject> localAnimeObjects;
    private ListItemClickListener localClickListener;

    // Every List item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleValue, episodesValue, synopsisValue, airingValue, scoreValue;
        private MaterialButton urlValue;
        private ImageView imageAnime;

        private ListItemClickListener clickListener;

        public ViewHolder(View itemView, ListItemClickListener onAnimeClickListener) {
            super(itemView);
            imageAnime = itemView.findViewById(R.id.imageValue);
            titleValue = itemView.findViewById(R.id.titleValue);
            episodesValue = itemView.findViewById(R.id.episodesValue);
            synopsisValue = itemView.findViewById(R.id.synopsisValue);
            airingValue = itemView.findViewById(R.id.airingValue);
            scoreValue = itemView.findViewById(R.id.scoreValue);
            urlValue = itemView.findViewById(R.id.urlValue);
            clickListener = onAnimeClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onAnimeClick(position);
        }

        public TextView getTitleValue() {
            return titleValue;
        }

        public TextView getEpisodesValue() {
            return episodesValue;
        }

        public TextView getSynopsisValue() {
            return synopsisValue;
        }

        public TextView getAiringValue() {
            return airingValue;
        }

        public TextView getScoreValue() {
            return scoreValue;
        }

        public ImageView getImageAnime() {
            return imageAnime;
        }

        public MaterialButton getUrlValue() {
            return urlValue;
        }
    }

    public interface ListItemClickListener {
        void onAnimeClick(int position);
    }

    public RecyclerviewListAdapter() {
        localAnimeObjects = new ArrayList<>();

    }

    public RecyclerviewListAdapter(ArrayList<AnimeObject> objects, ListItemClickListener listItemClickListener) {
        localAnimeObjects = objects;
        localClickListener = listItemClickListener;
    }

    public void setLocalAnimeObjects(ArrayList<AnimeObject> localAnimeObjects) {
        this.localAnimeObjects = localAnimeObjects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Fill out the viewGroup
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.anime_layout,
                viewGroup, false);

        return new ViewHolder(view, localClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerviewListAdapter.ViewHolder holder, int position) {
        holder.getTitleValue().setText(localAnimeObjects.get(position).getTitle());
        holder.getEpisodesValue().setText(localAnimeObjects.get(position).getEpisodes());
        holder.getSynopsisValue().setText(localAnimeObjects.get(position).getSynopsis());
        holder.getAiringValue().setText(localAnimeObjects.get(position).getAiring());
        holder.getScoreValue().setText(localAnimeObjects.get(position).getScore());
        holder.getUrlValue().setText(localAnimeObjects.get(position).getUrl());

        ImageView animeImage = holder.getImageAnime();
        Picasso.get().load(localAnimeObjects.get(position).getImageUrl()).into(animeImage);
    }

    @Override
    public int getItemCount() {
        return localAnimeObjects.size();
    }
}
