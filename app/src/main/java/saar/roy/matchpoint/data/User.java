package saar.roy.matchpoint.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Roy-PC on 25-Jan-18.
 */

public class User {
    private String id;
    private List<String> friendIds;
    private UserStatistics statistics;
    private LatLng currentLocation;
    boolean searchingForMatch;
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User(){};

    public String getName() {
        return name;
    }
}
