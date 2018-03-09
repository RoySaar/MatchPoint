package saar.roy.matchpoint.ui;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import saar.roy.matchpoint.data.MatchParticipation;
import saar.roy.matchpoint.R;
import saar.roy.matchpoint.data.Team;
import saar.roy.matchpoint.data.User;
import saar.roy.matchpoint.services.Callback;
import saar.roy.matchpoint.services.SearchServices;
import saar.roy.matchpoint.services.UserServices;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * Created by Eidan on 1/26/2018.
 */

public class CreateMatchDialogFragment extends DialogFragment implements View.OnClickListener {

    private String courtName;
    private String courtDescription;
    private User currentUser;
    private android.widget.SearchView searchView;
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        final View v = inflater.inflate(R.layout.dialog_create_match, null);
        services = new SearchServices();
        ArrayList<MatchParticipation> list = new ArrayList<>();
        participationAdapter = new ParticipationAdapter(getContext(),list);
        v.<ListView>findViewById(R.id.lvParticipations).setAdapter(participationAdapter);
        v.<TextView>findViewById(R.id.tvCourtName).setText(courtName);
        v.<TextView>findViewById(R.id.tvCourtDescription ).setText(courtDescription);
        v.<Button>findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        v.<Button>findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        CollapsingToolbarLayout mCollapsingToolbarLayout = v.findViewById(R.id.collapsing);
        mCollapsingToolbarLayout.setTitleEnabled(true);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.TitleBarCollapsed);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TitleBarExpanded);
        mCollapsingToolbarLayout.setExpandedTitleMargin(64,8,8,64);
        mCollapsingToolbarLayout.setTitle(courtName);
        Button btnAddFriend = v.findViewById(R.id.btnAddFriend);
        btnAddFriend.setOnClickListener(this);
        currentUser = UserServices.getInstance().getCurrentUser();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, currentUser.getFriendNames());
        v.<AutoCompleteTextView>findViewById(R.id.actvSearchFriends).setAdapter(adapter);
        return v;
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

    @Override
    public void onClick(View view) {
        String name = ((AutoCompleteTextView)getView().findViewById(R.id.actvSearchFriends)).getText().toString();
        DocumentReference ref = UserServices.getInstance().getCurrentUser().getUserByName(name);
        MatchParticipation mp = new MatchParticipation(ref);
        participationAdapter.add(mp);
    }
}