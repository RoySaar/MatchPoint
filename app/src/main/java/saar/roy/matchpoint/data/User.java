package saar.roy.matchpoint.data;

import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roy-PC on 25-Jan-18.
 */

public class User {
    private List<DocumentReference> friends;
    private String name;

    public User() {
        this.friends = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public DocumentReference getUserByName(String name) {
        for (DocumentReference user : friends) {
            if (user.get().equals(name))
                return user;
        }
        return null;
    }

    public List<DocumentReference> getFriends() {
        return friends;
    }

    public void addFriend(DocumentReference ref) {
        friends.add(ref);
    }
}
