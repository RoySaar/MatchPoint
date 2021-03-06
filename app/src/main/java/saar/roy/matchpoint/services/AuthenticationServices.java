package saar.roy.matchpoint.services;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import saar.roy.matchpoint.data.Court;
import saar.roy.matchpoint.data.Match;

/**
 * Created by Roy-PC on 04-Feb-18.
 */

public class AuthenticationServices {

    // Valid email pattern
    public static final Pattern VALID_EMAIL_ADDRESS =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static Verification verifyEmailAndPassword(String email, String password) {
        if (email == null || email.equals("") || password == null || email.equals(""))
            return Verification.EITHER_IS_NULL;
        //  if (password.length() > 8)
        // Password is too short
        // return Verification.PASSWORD_TOO_SHORT;
        Matcher matcher = VALID_EMAIL_ADDRESS.matcher(email);
        if (!matcher.find())
            return Verification.EMAIL_NOT_VALID;
        else
            return Verification.VALID;
    }
}
