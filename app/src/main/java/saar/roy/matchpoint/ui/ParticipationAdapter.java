package saar.roy.matchpoint.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.MatchParticipation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Eidan on 2/2/2018.
 */


public class ParticipationAdapter extends ArrayAdapter<MatchParticipation> {

    private AssetManager assetManager = getContext().getAssets();
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
        TextView tvName = convertView.findViewById(R.id.tvName);
        ImageView ivConfirmed = convertView.findViewById(R.id.ivConfirmed);
        tvName.setText(participation.getUser().getName());
        if (participation.isConfirmed())
            ivConfirmed.setImageResource(R.drawable.confirmed);
        else
            ivConfirmed.setImageResource(R.drawable.unconfirmed);
        final Typeface tvFont = Typeface.createFromAsset(assetManager,"fonts/assistant_semibold.ttf");
        tvName.setTypeface(tvFont);
        return convertView;
    }
}