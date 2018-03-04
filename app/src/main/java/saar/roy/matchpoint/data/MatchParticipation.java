package saar.roy.matchpoint.data;

/**
 * Created by Roy-PC on 25-Jan-18.
 */

public class MatchParticipation {
    private User user;
    private boolean confirmed;

    public MatchParticipation(User user) {
        this.user = user;
        this.confirmed = false;
    }

    public User getUser() {
        return user;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
