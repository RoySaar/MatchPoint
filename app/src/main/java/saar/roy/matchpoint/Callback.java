package saar.roy.matchpoint;

import java.util.List;

/**
 * Created by Roy-PC on 27-Jan-18.
 */

public interface Callback <T> {
    void onCallback(List<Court> courts);
}
