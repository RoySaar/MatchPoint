package saar.roy.matchpoint.services;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import saar.roy.matchpoint.data.Court;
import saar.roy.matchpoint.data.Match;
import saar.roy.matchpoint.data.MatchParticipation;

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

    public void getHours(final Callback<List<String>> callback,final Date date ,DocumentReference court,final String opens, final String closes) {
        final int open = Integer.parseInt(opens);
        final int close =  Integer.parseInt(closes);
        Calendar openDate = initializeDate(date, open);
        Calendar closeDate = initializeDate(date, close);
        db.collection("matches")
                .whereEqualTo("court",court)
                .whereLessThanOrEqualTo("date",closeDate.getTime())
                .whereGreaterThanOrEqualTo("date",openDate.getTime())
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                ArrayList<Integer> hours = new ArrayList<>();
                                if (!task.getResult().isEmpty()) {
                                    for (DocumentSnapshot document:task.getResult()) {
                                        Log.d("DateQuery","Arrived");
                                        Date time = document.getDate("date");
                                        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
                                        calendar.setTime(time);
                                        hours.add(calendar.get(Calendar.HOUR_OF_DAY));
                                        Log.d("Hour",String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
                                    }
                                }
                                else
                                    Log.d("DateQuery","Empty");
                                ArrayList<String> available = new ArrayList<>();
                                for (int i = open; i <= close; i++) {
                                    if (!hours.contains(i))
                                        available.add(String.valueOf(i));
                                }
                                callback.onCallback(available);
                            }
                        });
    }

    private static Calendar initializeDate(Date date, int hour) {
        Calendar openDate = new GregorianCalendar(TimeZone.getDefault());
        openDate.setTime(date);
        openDate.set(Calendar.HOUR,hour);
        openDate.set(Calendar.MINUTE,0);
        openDate.set(Calendar.SECOND,0);
        return openDate;
    }

    public void saveMatch(final Match match) {
        db.collection("matches").add(match).addOnSuccessListener(
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        for (MatchParticipation participation:match.getParticipations()) {
                            db.collection("matchInvites").add(
                                    new MatchInvite(participation.getUser(),documentReference));
                        }
                    }
                });

    }

}