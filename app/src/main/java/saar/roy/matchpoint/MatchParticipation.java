package saar.roy.matchpoint;

/**
 * Created by Roy-PC on 25-Jan-18.
 */

class MatchParticipation {
    private User user;
    private boolean confirmed;
    private Team team;

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
