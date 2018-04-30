package saar.roy.matchpoint.data;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Roy-PC on 25-Jan-18.
 */

public class User {
    private Map<String ,DocumentReference> friends;
    private String name;
    private Date dateJoined;

    public User() {
        //this.friends = new HashMap<>();
    }

    public User(String name) {
        this.name = name;
        this.friends = new HashMap<String, DocumentReference>() {};
        this.dateJoined = new GregorianCalendar().getTime();
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public String getName() {
        return name;
    }

    public DocumentReference getUserByName(String name) {
        return friends.get(name);
    }

    public Map<String, DocumentReference> getFriends() {
        return friends;
    }

    public void addFriend(String name, DocumentReference ref) {
        friends.put(name, ref);
    }

    public List<String> getFriendNames() {
        return new ArrayList<>(friends.keySet());
    }
}
