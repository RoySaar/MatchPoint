package saar.roy.matchpoint.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.Match;
import saar.roy.matchpoint.data.Notification;
import saar.roy.matchpoint.data.User;
import saar.roy.matchpoint.services.Callback;
import saar.roy.matchpoint.services.NotificationServices;
import saar.roy.matchpoint.services.UserServices;

/**
 * Created by Roy-PC on 23-Apr-18.
 */

public class NotificationsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private SpotsDialogHandler dialogHandler;
    UserAdapter userAdapter;

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
        //dialogHandler.hide();
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        userAdapter = new UserAdapter(getContext(),new ArrayList<User>());
        ListView resultList = v.findViewById(R.id.lvNotifications);
        resultList.setAdapter(userAdapter);
        resultList.setOnItemClickListener(this);
        //Button searchBtn = v.findViewById(R.id.btnSearchFriend);
        //searchBtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        Callback<List<User>> callback = new Callback<List<User>>() {
            @Override
            public void onCallback(List<User> users) {
                if (users != null) {
                    userAdapter.clear();
                    for (User user : users) {
                        Toast.makeText(getContext(),user.getName(),Toast.LENGTH_SHORT).show();
                        userAdapter.add(user);
                    }
                }
            }
        };
       // String name = ((EditText)getView().findViewById(R.id.tvNameSearch)).getText().toString().trim();
        //UserServices.getInstance().findUsersByName(callback,name);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
