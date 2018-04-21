package saar.roy.matchpoint.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import saar.roy.matchpoint.R;
import saar.roy.matchpoint.services.UserServices;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;
    private MapFragment mapFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Change the fragment according to the selected nav menu item
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    selectedFragment = MapFragment.newInstance();
                    setTitle(R.string.title_map);
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = DashboardFragment.newInstance();
                    setTitle(R.string.title_dashboard);
                    break;
                case R.id.navigation_notifications:
                    setTitle(R.string.title_notifications);
                    break;
            }
            if (selectedFragment != currentFragment) {
                changeFragment(selectedFragment);
            }
            return true;
        }
    };
    private ListenerRegistration notificationListenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(MapFragment.newInstance());
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        notificationListenerRegistration = FirebaseFirestore.getInstance().collection("matchInvites")
                .whereEqualTo("user", UserServices.getInstance().getCurrentUserReference())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        for (final DocumentChange documentChange:documentSnapshots.getDocumentChanges()) {
                            Log.d("Reference",documentChange.getDocument().getReference().getPath());
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                documentChange.getDocument().getDocumentReference("match").get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot matchSnapshot) {
                                        matchSnapshot.getDocumentReference("court").get().
                                                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot courtSnapshot) {
                                                NotificationCompat.Builder mBuilder =
                                                        new NotificationCompat.Builder(MainActivity.this)
                                                                .setSmallIcon(R.drawable.pin_ico)
                                                                .setContentTitle("Match Invite")
                                                                .setContentText(courtSnapshot.getString("name"))
                                                                   .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                                NotificationManagerCompat notificationManager = NotificationManagerCompat
                                                        .from(MainActivity.this);
                                                notificationManager.notify(0, mBuilder.build());
                                            }
                                        });
                                    }
                                });

                            }
                            documentChange.getDocument().getReference().delete();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        notificationListenerRegistration.remove();
        super.onDestroy();
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment,"map_fragment");
        transaction.commit();
        currentFragment = fragment;
    }
}
