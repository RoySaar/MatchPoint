package saar.roy.matchpoint.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;
import saar.roy.matchpoint.services.AuthenticationServices;
import saar.roy.matchpoint.R;
import saar.roy.matchpoint.services.Callback;
import saar.roy.matchpoint.services.UserServices;
import saar.roy.matchpoint.services.Verification;

import static saar.roy.matchpoint.services.Verification.EITHER_IS_NULL;
import static saar.roy.matchpoint.services.Verification.EMAIL_NOT_VALID;
import static saar.roy.matchpoint.services.Verification.VALID;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private AuthenticationServices authServices;
    public static final String LOG_AUTH_TAG = "FirebaseAuth";
    private SpotsDialogHandler dialogHandler;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;



    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        dialogHandler = SpotsDialogHandler.getInstance();
        dialogHandler.setText("Logging in...");
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        if (loginPreferences.getBoolean("saveLogin", false)){
            ((TextView)(findViewById(R.id.tvEmail))).setText(loginPreferences.getString("email", ""));
            ((CheckBox)(findViewById(R.id.cbRememberMe))).setChecked(true);
        }
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

    public void onSigninButtonClick(View v) {
        String email = ((EditText)findViewById(R.id.tvEmail)).getText().toString().trim();
        String password = ((EditText)findViewById(R.id.tvPassword)).getText().toString();
        Verification verification = AuthenticationServices.verifyEmailAndPassword(email,password);
        if (verification == EITHER_IS_NULL)
            // Email or password are empty
            Toast.makeText(this,"Please fill in both email and password",Toast.LENGTH_SHORT).show();
        else {
            if (((CheckBox)(findViewById(R.id.cbRememberMe))).isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("email", email);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }
            signIn(email, password);
        }
    }

    public void OnGuestSigninButtonClick(View v) {
        dialogHandler.show(LoginActivity.this);
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_AUTH_TAG, "signInAnonymously:success");
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_AUTH_TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    public void onSignupButtonClick(View v) {
        String email = ((EditText)findViewById(R.id.tvEmail)).getText().toString().trim();
        String password = ((EditText)findViewById(R.id.tvPassword)).getText().toString().trim();
        Verification verification = AuthenticationServices.verifyEmailAndPassword(email,password);
        if (verification == Verification.VALID)
            // Valid credentials, sign up
            signUp(email,password);
        else {
            // Display error message accordingly
            switch (verification) {
                case EMAIL_NOT_VALID:
                    Toast.makeText(this,EMAIL_NOT_VALID.toString(),Toast.LENGTH_SHORT).show();
                    break;
                // case PASSWORD_TOO_SHORT:
                //  message = "Password needs to be at least 8 characters long";
                // break;
                case EITHER_IS_NULL:
                    Toast.makeText(this,EITHER_IS_NULL.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onForgotPasswordClick(final View v){
        Toast.makeText(LoginActivity.this,"Clicked",Toast.LENGTH_SHORT).show();
        String email = ((TextView)findViewById(R.id.tvEmail)).getText().toString();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(v,"Password reset email has been sent.",Snackbar.LENGTH_SHORT).setAction("VIEW", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mailClient = new Intent(Intent.ACTION_VIEW);
                        mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
                        startActivity(mailClient);
                    }
                });
            }
        });
    }

    public void signIn(String email, String password) {
        Log.d(LOG_AUTH_TAG, "signInWithEmail:email:" + email);
        Verification verification = AuthenticationServices.verifyEmailAndPassword(email,password);
        Log.d(LOG_AUTH_TAG,verification.toString());
        if(verification != VALID) {
            Toast.makeText(LoginActivity.this,verification.toString(),Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                UserServices.getInstance().fetchCurrentUser();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
        }
    }

    public void signUp(final String email, final String password) {
        Verification verification = AuthenticationServices.verifyEmailAndPassword(email,password);
        final String name = ((EditText)findViewById(R.id.tvName)).getText().toString().trim();
        if(verification != VALID) {
            Toast.makeText(LoginActivity.this,verification.toString(),Toast.LENGTH_SHORT).show();
        }
        else if(name.equals("")) {
            Toast.makeText(LoginActivity.this,EITHER_IS_NULL.toString(),Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(LOG_AUTH_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Log.d(LOG_AUTH_TAG, "createUserWithEmail:onComplete:" + task.getException());
                                Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                Callback<Void> callback = new Callback<Void>() {
                                    @Override
                                    public void onCallback(Void obj) {
                                        signIn(email, password);
                                    }
                                };
                                UserServices.getInstance().createUserInDatabase(callback, mAuth.getUid(),name);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        // Hide the loading dialog
        if(dialogHandler.isShowing())
            dialogHandler.hide();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        //FirebaseAuth.getInstance().signOut();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            UserServices.getInstance().fetchCurrentUser();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}


