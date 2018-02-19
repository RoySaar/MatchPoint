package saar.roy.matchpoint.data;

import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roy-PC on 25-Jan-18.
 */

public class User {
    private List<User> friends;
    private String name;

    public String getName() {
        return name;
    }

    public List<String> getFriendNames() {
        List<String> friendNames = new ArrayList<>();
        for(User user : friends) {
            friendNames.add(user.getName());
        }
        return friendNames;
    }

    public void addFriend(User user) {
        friends.add(user);
    }
}
