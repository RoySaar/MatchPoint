package saar.roy.matchpoint.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.User;
import saar.roy.matchpoint.services.UserServices;

/**
 * Created by Roy-PC on 07-Feb-18.
 */

public class DashboardFragment extends Fragment {

    private SpotsDialogHandler dialogHandler;

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
        dialogHandler.hide();
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
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
}
