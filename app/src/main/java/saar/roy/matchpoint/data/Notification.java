package saar.roy.matchpoint.data;

import com.google.firebase.firestore.DocumentReference;

/**
 * Created by Roy-PC on 23-Apr-18.
 */

public class Notification {
    private DocumentReference matchReference;
    private Match match;
    private boolean confirmed;

    public Notification(DocumentReference matchReference, Match match){
        this.matchReference = matchReference;
        this.match = match;
        confirmed = false;
    }
}
