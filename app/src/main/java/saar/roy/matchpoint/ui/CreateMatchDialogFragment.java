package saar.roy.matchpoint.ui;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import saar.roy.matchpoint.data.MatchParticipation;
import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.Team;
import saar.roy.matchpoint.data.User;
import saar.roy.matchpoint.services.Callback;
import saar.roy.matchpoint.services.SearchServices;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * Created by Eidan on 1/26/2018.
 */

public class CreateMatchDialogFragment extends DialogFragment implements android.widget.SearchView.OnQueryTextListener {

    private String courtName;
    private String courtDescription;
    private android.widget.SearchView searchView;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    private SearchServices services;
    ParticipationAdapter participationAdapter;
    public static final int DIALOG_ANIM_DURATION = 500;


    static public CreateMatchDialogFragment newInstance() {
        Bundle args = new Bundle();
        CreateMatchDialogFragment fragment = new CreateMatchDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setCancelable(false);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.create_match_dialog, null);
        services = new SearchServices();
        ArrayList<MatchParticipation> list = new ArrayList<>();
        participationAdapter = new ParticipationAdapter(getContext(),list);
        //ParticipationAdapter participationAdapter =
          //      new ParticipationAdapter(getContext(),new ArrayList<MatchParticipation>());
        ListView lvParticipations = v.findViewById(R.id.lvParticipations);
        lvParticipations.setAdapter(participationAdapter);
        searchView = v.findViewById(R.id.svFriends);
        searchView.setOnQueryTextListener(this);
        TextView tvCourtName = v.findViewById(R.id.tvCourtName);
        TextView tvCourtDescription = v.findViewById(R.id.tvCourtDescription );
        tvCourtName.setText(courtName);
        tvCourtDescription.setText(courtDescription);
        Button submitBtn = v.findViewById(R.id.submitBtn);
        Button cancelBtn = v.findViewById(R.id.cancelBtn);
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
        mCollapsingToolbarLayout = v.findViewById(R.id.collapsing);
        mCollapsingToolbarLayout.setTitleEnabled(true);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.TitleBarCollapsed);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TitleBarExpanded);
        mCollapsingToolbarLayout.setExpandedTitleMargin(64,8,8,64);
        mCollapsingToolbarLayout.setTitle(courtName);
        //setDialogAnimations();
        return v;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        findUser(query);
        return true;
    }

    public void findUser(String query) {
        Callback callback = new Callback<User>() {
            @Override
            public void onCallback(User user) {
                // Make sure that the lobby isn't full
                if (participationAdapter.getCount() == 4) {
                    Snackbar.make(getActivity().findViewById(R.id.navigation),"הלובי מלא", LENGTH_SHORT ).show();
                }
                else if(!Objects.equals(user.getName(), "")) {
                    participationAdapter.add(new MatchParticipation(user, Team.TEAM1));
                }
            }
        };
        services.findUser(query,callback);
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public void setDialogAnimations() {
        getDialog().setOnShowListener(new DialogInterface
                .OnShowListener() {
            @Override public void onShow(DialogInterface dialog) {
                final View view = getDialog().getWindow()
                        .getDecorView();
                final int centerX = view.getWidth() / 2;
                final int centerY = view.getHeight() / 2;
                float startRadius = 20;
                float endRadius = view.getHeight();
                Animator animator = ViewAnimationUtils.createCircularReveal(view
                        ,centerX
                        ,centerY
                        ,startRadius
                        ,endRadius);
                animator.setDuration(DIALOG_ANIM_DURATION);
                animator.start();
            }
        });
        getDialog().setOnDismissListener(new DialogInterface
                .OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialog) {
                final View view = getDialog().getWindow()
                        .getDecorView();
                final int centerX = view.getWidth() / 2;
                final int centerY = view.getHeight() / 2;
                float startRadius = view.getHeight();
                float endRadius = 0;
                Animator animator = ViewAnimationUtils.createCircularReveal(view
                        ,centerX
                        ,centerY
                        ,startRadius
                        ,endRadius);
                animator.setDuration(DIALOG_ANIM_DURATION);
                animator.start();
            }
        });
    }

    public void setCourt(String courtName,String courtDescription) {
        this.courtName = courtName;
        this.courtDescription = courtDescription;
    }
}