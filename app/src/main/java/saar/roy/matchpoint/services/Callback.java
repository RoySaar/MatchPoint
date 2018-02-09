package saar.roy.matchpoint.services;

import java.util.List;

import saar.roy.matchpoint.data.Court;

/**
 * Created by Roy-PC on 27-Jan-18.
 */

public interface Callback <T> {
    void onCallback(T obj);
}
