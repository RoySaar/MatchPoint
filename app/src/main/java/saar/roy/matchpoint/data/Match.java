package saar.roy.matchpoint.data;

import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Roy-PC on 25-Jan-18.
 */

public class Match {
    private List<MatchParticipation> participations;
    private DocumentReference court;

    public Match(List<MatchParticipation> participations, DocumentReference court) {
        this.participations = participations;
        this.court = court;
    }


    public DocumentReference getCourt() {
        return court;
    }

    public List<MatchParticipation> getParticipations() {
        return participations;
    }
}