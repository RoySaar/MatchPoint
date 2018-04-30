package saar.roy.matchpoint.services;

import android.support.annotation.NonNull;
import android.telecom.Call;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import saar.roy.matchpoint.data.Match;
import saar.roy.matchpoint.data.MatchParticipation;
import saar.roy.matchpoint.data.User;

/**
 * Created by Eidan on 2/16/2018.
 */

public class UserServices {
    private static UserServices instance = null;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User currentUser;

    public static UserServices getInstance() {
        if (instance == null)
            instance = new UserServices();
        return instance;
    }

    private UserServices() {
    }

    // Get current user as User
    public void fetchCurrentUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            throw new RuntimeException("No user is logged in");
        }
        getCurrentUserReference()
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        final User user = task.getResult().toObject(User.class);
                        currentUser = user;
                    }
                });
    }

    //TODO: fix this (fetching matches, no names from participation)
    public void fetchUpcomingMatches(final Callback<ArrayList<Match>> callback) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            throw new RuntimeException("No user is logged in");
        }
        final DocumentReference userReference = getCurrentUserReference();
        db.collection("matches")
                .whereGreaterThanOrEqualTo("date", Calendar.getInstance().getTime())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    callback.onCallback(null);
                }
                else {
                    final ArrayList<Match> matches = new ArrayList<>();
                    final ArrayList<Match> returnMatches = new ArrayList<>();
                    for (DocumentSnapshot matchSnapshot:documentSnapshots.getDocuments()) {
                        matches.add(matchSnapshot.toObject(Match.class));
                    }
                    for (Match match:matches) {
                        for (MatchParticipation participation:match.getParticipations()) {
                            if (participation.getUser() == userReference){
                                returnMatches.add(match);
                            }
                        }
                    }
                    callback.onCallback(returnMatches);
                }
            }
        });
    }

    public void createUserInDatabase(final Callback callback, String name) {
        db.collection("users").add(new User(name)).addOnSuccessListener(
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // TODO: Finish the signup
                        callback.onCallback();
                    }
                });
    }

    public DocumentReference getCurrentUserReference() {
        return FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
