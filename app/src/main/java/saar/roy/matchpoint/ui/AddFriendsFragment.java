package saar.roy.matchpoint.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.User;
import saar.roy.matchpoint.services.Callback;
import saar.roy.matchpoint.services.UserServices;

/**
 * Created by Roy-PC on 23-Apr-18.
 */

public class AddFriendsFragment extends Fragment implements View.OnClickListener {

    UserAdapter userAdapter;

    public static AddFriendsFragment newInstance() {
        AddFriendsFragment fragment = new AddFriendsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        userAdapter = new UserAdapter(getContext(),new ArrayList<User>());
        v.<Button>findViewById(R.id.btnSearchFriends).setOnClickListener(this);
        v.<ListView>findViewById(R.id.lvFriendsResult).setAdapter(userAdapter);
        v.<ListView>findViewById(R.id.lvFriendsResult).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = userAdapter.getItem(i).getName();
                Log.d("Name",name);
                if (UserServices.getInstance().alreadyFriend(name)) {
                    Toast.makeText(getContext(),"Already friends",Toast.LENGTH_SHORT).show();
                }
                else {
                    Callback<Void> callback = new Callback<Void>() {
                        @Override
                        public void onCallback(Void aVoid) {
                            userAdapter.clear();
                            Toast.makeText(getContext(), "Friend added!", Toast.LENGTH_SHORT).show();
                        }
                    };
                    UserServices.getInstance().addFriend(callback, name);
                }
            }
        });
        return v;
    }

    @Override
    public void onClick(View view) {
        String name = ((EditText)getView().findViewById(R.id.etSearchFriends)).getText().toString().trim();
        if (!UserServices.getInstance().getCurrentUser().getName().equals(name)) {
            Callback<List<User>> callback = new Callback<List<User>>() {
                @Override
                public void onCallback(List<User> users) {
                    if (users == null) {
                        Toast.makeText(getContext(), "User was not found", Toast.LENGTH_SHORT).show();
                    } else {
                        userAdapter.clear();
                        for (User user : users) {
                            userAdapter.add(user);
                        }
                    }
                }
            };
            UserServices.getInstance().findUsersByName(callback, name);
        }
        else {
            Toast.makeText(getContext(),"Cannot add yourself as a friend",Toast.LENGTH_SHORT).show();
        }
    }

}
