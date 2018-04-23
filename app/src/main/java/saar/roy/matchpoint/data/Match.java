package saar.roy.matchpoint.data;

import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import saar.roy.matchpoint.services.UserServices;

/**
 * Created by Roy-PC on 25-Jan-18.
 */

public class Match {
    private List<MatchParticipation> participations;
    private DocumentReference court;
    private Date date;
    private DocumentReference owner;

    public Match(List<MatchParticipation> participations, DocumentReference court,Date date) {
        this.participations = participations;
        this.court = court;
        this.date = date;
        this.owner = UserServices.getInstance().getCurrentUserReference();
    }

    public DocumentReference getCourt() {
        return court;
    }

    public Date getDate() {
        return date;
    }

    public DocumentReference getOwner() {
        return owner;
    }

    public List<MatchParticipation> getParticipations() {
        return participations;
    }
}