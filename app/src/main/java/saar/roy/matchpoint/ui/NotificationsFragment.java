package saar.roy.matchpoint.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.Match;
import saar.roy.matchpoint.data.Notification;
import saar.roy.matchpoint.services.NotificationServices;

/**
 * Created by Roy-PC on 23-Apr-18.
 */

public class NotificationsFragment extends Fragment {

    private SpotsDialogHandler dialogHandler;

    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
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
        dialogHandler.hide();
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        NotificaitonAdapter notificaitonAdapter = new NotificaitonAdapter(getContext(),new ArrayList<Notification>());
        notificaitonAdapter.add(new Notification(null,null));
        ((ListView)v.findViewById(R.id.lvNotifications)).setAdapter(notificaitonAdapter);
        return v;
    }
}
