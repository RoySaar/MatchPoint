package saar.roy.matchpoint;

import java.util.List;

/**
 * Created by Roy-PC on 26-Jan-18.
 */

public interface Services {
    void getCourts(Callback<List<Court>> callback);
    void saveMatch(Match match);
}

