package saar.roy.matchpoint.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import saar.roy.matchpoint.data.Court;
import saar.roy.matchpoint.data.Match;

/**
 * Created by roy on 22/01/18.
 */

public class MapServices implements Services {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final String TAG = "Document:";

    public void getCourts(final Callback<List<Court>> callback) {
        db.collection("courts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Court> courts = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                courts.add(document.toObject(Court.class));

                            }
                            callback.onCallback(courts);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    };

    public void saveMatch(Match match) {

    };
}