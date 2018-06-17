package saar.roy.matchpoint.services;

import saar.roy.matchpoint.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Jukebox {

    Map<String, Integer> musicBox;

    public Jukebox() {
        musicBox = new HashMap<>();

        musicBox.put("notification", R.raw.notification);
    }

    public int getRandomTrack(List<Integer> requests) {
        List<Integer> availableMusic = new LinkedList<>();

        for (int track : musicBox.values()) {
            if (requests.contains(track) == false)
                continue;

            availableMusic.add(track);
        }

        Random random = new Random();

        int randTrack = random.nextInt(availableMusic.size());

        int i = 0;

        for(int track : availableMusic) {
            if (i == randTrack)
                return track;

            i++;
        }
        return 0;
    }

    public int getTrack(String name) {
        return musicBox.get(name);
    }
}
