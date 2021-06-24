package com.example.animehub;

public class AnimeObject {

    String animeId, imageUrl, title, episodes, synopsis, airing, score;

    public AnimeObject() {
        this.animeId = "Unknown";
        this.imageUrl = "Unknown";
        this.title = "Unknown";
        this.episodes = "Unknown";
        this.synopsis = "Unknown";
        this.airing = "Unknown";
        this.score = "Unknown";
    }

    public AnimeObject(String animeId, String imageUrl, String title, String episodes, String synopsis, String airing, String score) {
        this.animeId = animeId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.episodes = episodes;
        this.synopsis = synopsis;
        this.airing = airing;
        this.score = score;
    }

    public String getAnimeId() {
        return animeId;
    }

    public void setAnimeId(String animeId) {
        this.animeId = animeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEpisodes() {
        return episodes;
    }

    public void setEpisodes(String episodes) {
        this.episodes = episodes;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getAiring() {
        return airing;
    }

    public void setAiring(String airing) {
        this.airing = airing;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
