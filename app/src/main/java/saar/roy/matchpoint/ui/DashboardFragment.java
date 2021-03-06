package saar.roy.matchpoint.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.Duration;

import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.Court;
import saar.roy.matchpoint.data.Match;
import saar.roy.matchpoint.data.MatchParticipation;
import saar.roy.matchpoint.data.User;
import saar.roy.matchpoint.services.Callback;
import saar.roy.matchpoint.services.UserServices;

/**
 * Created by Roy-PC on 07-Feb-18.
 */

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private SpotsDialogHandler dialogHandler;
    private MatchListAdapter matchAdapter;
    private ArrayList<String> headerList;
    private HashMap<String,List<String>> titleMap;

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        dialogHandler = SpotsDialogHandler.getInstance();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((Button) v.findViewById(R.id.btnRefresh)).setOnClickListener(this);
        MatchListAdapter matchAdapter = new MatchListAdapter(getContext(),headerList,titleMap);
        ((TextView) v.findViewById(R.id.tvUsername))
                .setText(UserServices.getInstance().getCurrentUser().getName());
        ((TextView)v.findViewById(R.id.tvUserInfo))
                .setText(SimpleDateFormat.getDateInstance().format
                        (UserServices.getInstance().getCurrentUser().getDateJoined().getTime()));
        ArrayAdapter<String> friendNamesAdapter = new ArrayAdapter<String>(getContext()
                ,android.R.layout.simple_list_item_1,new ArrayList<String>());
        for (String name:UserServices.getInstance().getCurrentUser().getFriendNames()) {
            friendNamesAdapter.add(name);
        }
        ((ListView) v.findViewById(R.id.lvFriends)).setAdapter(friendNamesAdapter);
        return v;
    }


    @Override
    public void onClick(View view) {
        headerList = new ArrayList<>();
        titleMap = new HashMap<>();
        Callback<ArrayList<Match>> callback = new Callback<ArrayList<Match>>() {
            @Override
            public void onCallback(final ArrayList<Match> matches) {
                if (matches == null) {
                    Snackbar.make(getView().findViewById(R.id.elvMatches),"No Upcoming Matches.",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    for (final Match match:matches) {
                        headerList.add(SimpleDateFormat.getDateTimeInstance().format(match.getDate()));
                        match.getCourt().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                ArrayList<String> list = new ArrayList<>();
                                list.add(documentSnapshot.toObject(Court.class).getName());
                                titleMap.put(headerList.get(matches.indexOf(match)),list);
                            }
                        });
                    }
                    MatchListAdapter matchAdapter = new MatchListAdapter(getContext(),headerList,titleMap);
                    ((ExpandableListView)getView().findViewById(R.id.elvMatches)).setAdapter(matchAdapter);
                }
            }
        };
        UserServices.getInstance().fetchUpcomingMatches(callback);
    }
}
