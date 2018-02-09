package saar.roy.matchpoint.data;

/**
 * Created by Roy-PC on 25-Jan-18.
 */

public class MatchParticipation {
    private User user;
    private boolean confirmed;
    private Team team;

    public MatchParticipation(User user, Team team) {
        this.user = user;
        this.team = team;
        this.confirmed = true;
    }

    public User getUser() {
        return user;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
