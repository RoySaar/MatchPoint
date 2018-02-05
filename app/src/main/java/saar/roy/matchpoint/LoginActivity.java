package saar.roy.matchpoint;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private AuthenticationServices authServices;
    public static final String LOG_AUTH_TAG = "FirebaseAuth";
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        authServices = new AuthenticationServices();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(LOG_AUTH_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(LOG_AUTH_TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    private void InitilizeRememberMe() {
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String email = pref.getString(PREF_EMAIL, "");
        String password = pref.getString(PREF_PASSWORD, "");
        if (!(email.equals(""))) {
            ((EditText)findViewById(R.id.textEmail)).setText(email);
            ((EditText)findViewById(R.id.textPassword)).setText(password);
            ((CheckBox)findViewById(R.id.rememberMe)).setChecked(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_AUTH_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.d(LOG_AUTH_TAG, "createUserWithEmail:onComplete:" + task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(String email, String password) {
        Log.d(LOG_AUTH_TAG, "signInWithEmail:email:" + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_AUTH_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(LOG_AUTH_TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            // Start dashboard activity
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }
                    }
                });
    }

    public void OnSignupButtonClick(View v) {
        String email = ((EditText)findViewById(R.id.textEmail)).getText().toString().trim();
        String password = ((EditText)findViewById(R.id.textPassword)).getText().toString().trim();
        Verification verification = authServices.verifyEmailAndPassword(email,password);
        if (verification == Verification.VALID)
            // Valid credentials, sign up
            signUp(email,password);
        else {
            // Display error message accordingly
            String message = "Generic sign up error";
            switch (verification) {
                case EMAIL_NOT_VALID:
                    message = "The email address is invalid";
                    break;
                // case PASSWORD_TOO_SHORT:
                //  message = "Password needs to be at least 8 characters long";
                // break;
                case EITHER_IS_NULL:
                    message = "Please fill in both email and password";
            }
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        }

    }

    public void OnSigninButtonClick(View v) {
        String email = ((EditText)findViewById(R.id.textEmail)).getText().toString().trim();
        String password = ((EditText)findViewById(R.id.textPassword)).getText().toString().trim();
        Verification verification = authServices.verifyEmailAndPassword(email,password);
        if (verification == Verification.EITHER_IS_NULL)
            // Email or password are empty
            Toast.makeText(this,"Please fill in both email and password",Toast.LENGTH_SHORT).show();
        else {
            signIn(email, password);
        }

    }

    private void rememberMe(String email, String password) {
        if ( ((CheckBox)findViewById(R.id.rememberMe)).isChecked() ) {
            // Remember me
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putString(PREF_EMAIL, email)
                    .putString(PREF_PASSWORD, password)
                    .apply();
        }
        else {
            // Don't remember me
            getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                    .edit()
                    .putString(PREF_EMAIL, "")
                    .putString(PREF_PASSWORD, "")
                    .apply();
        }
    }

}


