package com.example.animehub;

import java.util.HashMap;

public class GeneraOptions {
    HashMap<String, Integer> anime;

    public GeneraOptions() {
        anime = new HashMap<>();

        addValues();
    }

    private void addValues() {
        anime.put("Action", 1);
        anime.put("Adventure", 2);
        anime.put("Cars", 3);
        anime.put("Comedy", 4);
        anime.put("Dementia", 5);
        anime.put("Demons", 6);
        anime.put("Mystery", 7);
        anime.put("Drama", 8);
        anime.put("Ecchi", 9);
        anime.put("Fantasy", 10);
        anime.put("Game", 11);
        anime.put("Hentai", 12);
        anime.put("Historical", 13);
        anime.put("Horror", 14);
        anime.put("Kids", 15);
        anime.put("Magic", 16);
        anime.put("Martial Arts", 17);
        anime.put("Mecha", 18);
        anime.put("Music", 19);
        anime.put("Parody", 20);
        anime.put("Samurai", 21);
        anime.put("Romance", 22);
        anime.put("School", 23);
        anime.put("Sci Fi", 24);
        anime.put("Shoujo", 25);
        anime.put("Shoujo Ai", 26);
        anime.put("Shounen", 27);
        anime.put("Shounen Ai", 28);
        anime.put("Space", 29);
        anime.put("Sports", 30);
        anime.put("Super Power", 31);
        anime.put("Vampire", 32);
        anime.put("Yaoi", 33);
        anime.put("Yuri", 34);
        anime.put("Harem", 35);
        anime.put("Slice Of Life", 36);
        anime.put("Supernatural", 37);
        anime.put("Military", 38);
        anime.put("Police", 39);
        anime.put("Psychological", 40);
        anime.put("Thriller", 41);
        anime.put("Seinen", 42);
        anime.put("Josei", 43);
    }

    public int getValue(String key) {
        return anime.get(key);
    }
}
