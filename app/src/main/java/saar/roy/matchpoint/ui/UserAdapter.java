package saar.roy.matchpoint.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.User;
import saar.roy.matchpoint.services.UserServices;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Roy-PC on 15-Feb-18.
 */

public class UserAdapter extends ArrayAdapter<User> {

    private LayoutInflater lif;

    public UserAdapter(@NonNull Context context, List<User> users) {
        super(context, 0, users);
        lif = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.user_item,parent,false);
        }
        TextView tvSearchName = convertView.findViewById(R.id.tvSearchName);
        tvSearchName.setText(user.getName());
        return convertView;
    }
}
