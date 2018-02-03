package saar.roy.matchpoint;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Eidan on 1/26/2018.
 */

public class CreateMatchDialogFragment extends DialogFragment {

    private String courtName;
    private String courtDescription;

    static public CreateMatchDialogFragment newInstance() {
        Bundle args = new Bundle();
        CreateMatchDialogFragment fragment = new CreateMatchDialogFragment();
        fragment.setArguments(args);
        return fragment;
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCourt(String courtName,String courtDescription) {
        this.courtName = courtName;
        this.courtDescription = courtDescription;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.create_match_dialog, null);
        ArrayList<MatchParticipation> list = new ArrayList<>();
        list.add(new MatchParticipation(new User("ניסים גרמה"),Team.TEAM2));
        ParticipationAdapter participationAdapter = new ParticipationAdapter(getContext(),list);
        //ParticipationAdapter participationAdapter =
          //      new ParticipationAdapter(getContext(),new ArrayList<MatchParticipation>());
        ListView lvParticipations = v.findViewById(R.id.lvParticipations);
        lvParticipations.setAdapter(participationAdapter);
        TextView tvCourtName = (TextView) v.findViewById(R.id.tvCourtName);
        TextView tvCourtDescription = (TextView) v.findViewById(R.id.tvCourtDescription );
        if (courtName != null && courtDescription != null) {
            tvCourtName.setText(courtName);
            tvCourtDescription.setText(courtDescription);
        }
        Button submitBtn = (Button)v.findViewById(R.id.submitBtn);
        Button cancelBtn = (Button)v.findViewById(R.id.cancelBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        return v;
    }
}