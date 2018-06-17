package saar.roy.matchpoint.services;

import android.support.annotation.NonNull;
import android.telecom.Call;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
                        if (task.isSuccessful()) {
                            final User user = task.getResult().toObject(User.class);
                            currentUser = user;
                        }
                    }
                });
    }

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
                    for (DocumentChange matchSnapshot:documentSnapshots.getDocumentChanges()) {
                        if (matchSnapshot != null) {
                            DocumentSnapshot matchSnap = matchSnapshot.getDocument();
                            Log.d("DocumentSnapshot",((Date)matchSnap.get("date")).toString());
                            Match match = matchSnap.toObject(Match.class);
                            matches.add(match);
                        }
                    }
                    for (Match match:matches) {
                        if(match.getOwner().getPath().equals(userReference.getPath()))
                            returnMatches.add(match);
                        for (MatchParticipation participation:match.getParticipations()) {
                            if (participation.getUser().getPath().equals(userReference.getPath())){
                                returnMatches.add(match);
                            }
                        }
                    }
                    Log.d("Return Matches",String.valueOf(returnMatches.size()));
                    Log.d("Matches",String.valueOf(matches.size()));
                    callback.onCallback(returnMatches);
                }
            }
        });
    }

    public boolean alreadyFriend(final String name) {
        return getCurrentUser().getFriendNames().contains(name);
    }

    public void addFriend(final Callback<Void> callback, final String name) {
        db.collection("users").whereEqualTo("name",name).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        DocumentReference friendRef = documentSnapshots.getDocuments().get(0).getReference();
                        HashMap<String ,DocumentReference> friends = getCurrentUser().getFriends();
                        friends.put(name,friendRef);
                        db.collection("users").document(FirebaseAuth.getInstance().getUid()).update("friends",friends).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                callback.onCallback(aVoid);
                            }
                        });
                    }
                });
    }

    public void findUsersByName(final Callback<List<User>> callback, final String name) {
        db.collection("users").whereEqualTo("name",name)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()){
                    callback.onCallback(null);
                }
                else {
                    ArrayList<User> userList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot:task.getResult()) {
                        userList.add(documentSnapshot.toObject(User.class));
                    }
                    callback.onCallback(userList);
                }
            }
        });
    }



    public void createUserInDatabase(final Callback callback, String uid,String name) {
        db.collection("users").document(uid).set(new User(name)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onCallback(aVoid);
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