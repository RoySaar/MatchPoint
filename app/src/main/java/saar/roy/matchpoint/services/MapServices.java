package saar.roy.matchpoint.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saar.roy.matchpoint.data.Court;
import saar.roy.matchpoint.data.Match;

/**
 * Created by roy on 22/01/18.
 */

public class MapServices {
    private static MapServices instance = null;

    private Court currentCourt;

    private List<Court> courts;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Document:";

    public static MapServices getInstance() {
        if (instance == null)
            instance = new MapServices();
        return instance;
    }

    private MapServices() {
    }

    public void setCourts(List<Court> courts) {
        this.courts = courts;
    }

    public void setCurrentCourt(String courtName) {
        for (Court court: courts) {
            if (court.getName().equals(courtName))
                currentCourt = court;
        }
    }

    public void getCourts(final Callback<List<Court>> callback) {
        db.collection("courts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Court> courts = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Court court = document.toObject(Court.class);
                                courts.add(court);
                            }
                            callback.onCallback(courts);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    ;

    public void saveMatch(Match match) {
        db.collection("matches").add(match);
    }

    public Court getCurrentCourt() {
        return currentCourt;
    }
}