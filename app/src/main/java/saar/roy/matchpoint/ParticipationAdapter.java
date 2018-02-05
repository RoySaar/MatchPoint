package saar.roy.matchpoint;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Eidan on 2/2/2018.
 */



public class ParticipationAdapter extends ArrayAdapter<MatchParticipation> {

    AssetManager assetManager = getContext().getAssets();

    private LayoutInflater lif;

    public ParticipationAdapter (Context context, List<MatchParticipation> participations) {
        super(context,0,participations);
        lif = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
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
            ivConfirmed.setImageResource(R.drawable.confirmed);
        else
            ivConfirmed.setImageResource(R.drawable.unconfirmed);
        // Set text color according to team
        switch (participation.getTeam()) {
            case TEAM1:
                tvName.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                break;
            case TEAM2:
                tvName.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                break;
        }
        // Set listview item font as assistant
        final Typeface tvFont = Typeface.createFromAsset(assetManager,"fonts/assistant_semibold.ttf");
        tvName.setTypeface(tvFont);

        return convertView;
    }
}