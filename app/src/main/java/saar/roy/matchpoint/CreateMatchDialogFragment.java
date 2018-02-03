package saar.roy.matchpoint;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Eidan on 1/26/2018.
 */

public class CreateMatchDialogFragment extends DialogFragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.create_match_dialog, null);
        ParticipationAdapter participationAdapter =
                new ParticipationAdapter(getContext(),new ArrayList<MatchParticipation>());
        ListView lvParticipations = v.findViewById(R.id.lvParticipations);
        lvParticipations.setAdapter(participationAdapter);
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