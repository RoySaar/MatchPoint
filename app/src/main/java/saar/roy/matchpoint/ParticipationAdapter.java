package saar.roy.matchpoint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.List;

/**
 * Created by Eidan on 2/2/2018.
 */

public class ParticipationAdapter extends ArrayAdapter<MatchParticipation> {
    public ParticipationAdapter (Context context, List<MatchParticipation> participations) {
        super(context,0,participations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MatchParticipation participation = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.participation_item,parent,false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        ImageView ivConfirmed = (ImageView) convertView.findViewById(R.id.ivConfirmed);
        tvName.setText(participation.getUser().getName());
        if (participation.isConfirmed())
            ivConfirmed.setBackgroundResource(R.drawable.confirmed);
        else
            ivConfirmed.setBackgroundResource(R.drawable.unconfirmed);
        switch (participation.getTeam()) {
            case TEAM1:
                tvName.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                break;
            case TEAM2:
                tvName.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                break;
        }
        return convertView;
    }
}