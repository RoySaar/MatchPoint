package saar.roy.matchpoint.services;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import saar.roy.matchpoint.data.User;

/**
 * Created by Roy-PC on 10-Feb-18.
 */

public class SearchServices {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final String TAG = "Document:";

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public void findUser(String name, final Callback<User> callback) {
        assert currentUser != null;
        db.collection("users")
                .document(currentUser.getUid())
                .collection("friends")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        callback.onCallback(document.toObject(User.class));
                    }
                }
            }
        });
    }
}
