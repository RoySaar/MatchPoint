package saar.roy.matchpoint.services;

/**
 * Created by Roy-PC on 04-Feb-18.
 */

public enum Verification {
    EITHER_IS_NULL {
        @Override
        public String toString() {
            return "Please fill in both email and password";
        }
    },PASSWORD_TOO_SHORT,EMAIL_NOT_VALID,VALID
}
