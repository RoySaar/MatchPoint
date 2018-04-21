package saar.roy.matchpoint.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import saar.roy.matchpoint.data.Match;
import saar.roy.matchpoint.data.MatchParticipation;
import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.User;
import saar.roy.matchpoint.services.MapServices;
import saar.roy.matchpoint.services.UserServices;

/**
 * Created by Eidan on 1/26/2018.
 */

public class CreateMatchFragment extends Fragment implements View.OnClickListener {

    private String courtName;
    private String courtDescription;
    private User currentUser;
    private DocumentReference courtReference;
    private ParticipationAdapter participationAdapter;
    private Calendar matchDate;


    static public CreateMatchFragment newInstance() {
        Bundle args = new Bundle();
        CreateMatchFragment fragment = new CreateMatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setCancelable(false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container
            , Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dialog_create_match, null);
        ArrayList<MatchParticipation> list = new ArrayList<>();
        participationAdapter = new ParticipationAdapter(getContext(), list);

        v.<ListView>findViewById(R.id.lvParticipations).setAdapter(participationAdapter);
        v.<TextView>findViewById(R.id.tvCourtName).setText(courtName);
        v.<TextView>findViewById(R.id.tvCourtDescription).setText(courtDescription);
        v.<Button>findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (participationAdapter.getCount() != 0)
                    saveMatch();
                else
                    Toast.makeText(getContext(), "The Lobby Is Empty.", Toast.LENGTH_SHORT).show();
            }
        });
        v.<Button>findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO return to map
            }
        });
        CollapsingToolbarLayout mCollapsingToolbarLayout = v.findViewById(R.id.collapsing);
        mCollapsingToolbarLayout.setTitleEnabled(true);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.TitleBarCollapsed);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TitleBarExpanded);
        mCollapsingToolbarLayout.setExpandedTitleMargin(64, 8, 8, 64);
        mCollapsingToolbarLayout.setTitle(courtName);
        Button btnAddFriend = v.findViewById(R.id.btnAddFriend);
        btnAddFriend.setOnClickListener(this);
        currentUser = UserServices.getInstance().getCurrentUser();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, currentUser.getFriendNames());
        v.<AutoCompleteTextView>findViewById(R.id.actvSearchFriends).setAdapter(adapter);
        matchDate = new GregorianCalendar(TimeZone.getDefault());
        TextView tvMatchDialog = v.findViewById(R.id.tvMatchDate);
        tvMatchDialog.setText(SimpleDateFormat.getDateInstance().format(matchDate.getTime()));
        tvMatchDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext()
                ,android.R.layout.simple_list_item_1,new ArrayList<String>());
        arrayAdapter.add("hello");
        Spinner spinner = v.findViewById(R.id.spnrMatchTime);
        spinner.setAdapter(arrayAdapter);
        return v;
    }

    public void showDateDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                matchDate.set(year,month,day);
                TextView tvMatchDialog = getView().findViewById(R.id.tvMatchDate);
                tvMatchDialog.setText(SimpleDateFormat.getDateInstance().format(matchDate.getTime()));
            }
        },matchDate.get(Calendar.YEAR), matchDate.get(Calendar.MONTH),matchDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void setCourt(String courtName, String courtDescription, DocumentReference ref) {
        this.courtName = courtName;
        this.courtDescription = courtDescription;
        this.courtReference = ref;
    }

    @Override
    public void onClick(View view) {
        String name = ((AutoCompleteTextView) getView().findViewById(R.id.actvSearchFriends))
                .getText().toString();
        DocumentReference ref = UserServices.getInstance().getCurrentUser().getUserByName(name);
        MatchParticipation mp = new MatchParticipation(ref);
        if (participationAdapter.getCount() < 4) {
            boolean validAdd = true;
            for (int i = 0; i < participationAdapter.getCount(); i++) {
                if (participationAdapter.getItem(i).getUser() == mp.getUser()) {
                    Toast.makeText(getContext(), "This Player Is Already In The Lobby."
                            , Toast.LENGTH_SHORT).show();
                    validAdd = false;
                }
            }
            if (validAdd)
                participationAdapter.add(mp);
        } else
            Toast.makeText(getContext(), "The Lobby Is Full.", Toast.LENGTH_SHORT).show();
    }

    public void saveMatch() {
        MapServices.getInstance().saveMatch(
                new Match(participationAdapter.getParticipations(),
                        courtReference,matchDate.getTime())
        );
        // TODO return to map
    }
}