package saar.roy.matchpoint.services;

import android.support.annotation.NonNull;
import android.telecom.Call;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import saar.roy.matchpoint.data.User;

/**
 * Created by Eidan on 2/16/2018.
 */

public class UserServices {
    private static UserServices instance = null;

    private User currentUser;

    public static UserServices getInstance() {
        if (instance == null)
            instance = new UserServices();
        return instance;
    }

    private UserServices() { }

    public void fetchCurrentUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            throw new RuntimeException("No user is logged in");
        }
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = task.getResult().toObject(User.class);
                        for(DocumentReference ref: (DocumentReference[])task.getResult().get("friends")) {

                        }
                        currentUser = user;
                    }
                });
    }
}
