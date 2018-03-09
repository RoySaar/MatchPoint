package saar.roy.matchpoint.data;

import com.google.firebase.firestore.DocumentReference;

/**
 * Created by Roy-PC on 25-Jan-18.
 */

public class MatchParticipation {
    private DocumentReference user;
    private boolean confirmed;

    public MatchParticipation(DocumentReference user) {
        this.user = user;
        this.confirmed = false;
    }

    public DocumentReference getUser() {
        return user;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
