package saar.roy.matchpoint.data;

import com.google.firebase.database.ServerValue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by Roy-PC on 25-Jan-18.
 */

public class Match {
    private List<MatchParticipation> participations;
    private Court court;
    private Map<String,String> time;

    public Match(List<MatchParticipation> participations, Court court) {
        this.participations = participations;
        this.court = court;
        this.time = ServerValue.TIMESTAMP;
    }

    public List<MatchParticipation> getParticipations() {
        return participations;
    }
}