package saar.roy.matchpoint.services;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import saar.roy.matchpoint.data.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by Roy-PC on 09-Feb-18.
 */

public class SearchTask extends AsyncTask<String, Void, List<User>> {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final String TAG = "Document:";

    @Override
    protected List<User> doInBackground(String... strings) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final List<User> users = new ArrayList<>();
        db.collection("users")
                .document(user.getUid())
                    .collection("friends")
                        .whereEqualTo("name",strings.toString())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        users.add(document.toObject(User.class));
                    }
                }
            }
        });
        return users;
    }
}
