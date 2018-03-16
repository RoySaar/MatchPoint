package saar.roy.matchpoint.services;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
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

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Document:";

    public static MapServices getInstance() {
        if (instance == null)
            instance = new MapServices();
        return instance;
    }

    private MapServices() {
    }

    public void getCourtMarkers(final Callback<Map<DocumentReference,Court>> callback, final Bitmap icon) {
        db.collection("courts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<DocumentReference,Court> map = new HashMap<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                map.put(document.getReference(),document.toObject(Court.class));
                            }
                            callback.onCallback(map);
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

}