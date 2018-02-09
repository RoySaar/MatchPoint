package saar.roy.matchpoint.services;

import java.util.List;

import saar.roy.matchpoint.data.Court;
import saar.roy.matchpoint.data.Match;

/**
 * Created by Roy-PC on 26-Jan-18.
 */

public interface Services {
    void getCourts(Callback<List<Court>> callback);
    void saveMatch(Match match);
}

