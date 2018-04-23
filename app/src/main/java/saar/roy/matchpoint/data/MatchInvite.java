package saar.roy.matchpoint.data;

import com.google.firebase.firestore.DocumentReference;

/**
 * Created by Roy-PC on 16-Mar-18.
 */

public class MatchInvite {

    DocumentReference match;
    DocumentReference user;

    public MatchInvite(DocumentReference match, DocumentReference user) {
        this.match = match;
        this.user = user;
    }

    public DocumentReference getUser() {
        return user;
    }

    public DocumentReference getMatch() {
        return match;
    }
}
