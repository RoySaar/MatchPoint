package saar.roy.matchpoint.services;

import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

/**
 * Created by Eidan on 3/23/2018.
 */

class MatchInvite {

    DocumentReference user;
    DocumentReference match;

    public DocumentReference getMatch() {
        return match;
    }

    public DocumentReference getUser() {
        return user;
    }

    public MatchInvite(DocumentReference user, DocumentReference match) {
        this.match = match;
        this.user = user;
    }
}
