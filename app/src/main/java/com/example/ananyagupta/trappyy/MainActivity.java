package com.example.ananyagupta.trappyy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    private Button mGoogleLoginBtn;
    private Button mEmailLoginBtn;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmailField;
    private EditText mPasswordField;
    private ProgressDialog mProgressDialog;
    private int statusCode;
    private SharedPreferences mSp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        getSupportActionBar().hide();

        mGoogleLoginBtn = (Button) findViewById(R.id.google_login_button);
        mEmailLoginBtn = (Button) findViewById(R.id.custom_signin_button);
        mEmailField = (EditText)findViewById(R.id.email_edittext);
        mPasswordField = (EditText)findViewById(R.id.password_edittext);
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setCancelable(false);//you can not cancel it by pressing back button
        mProgressDialog.setMessage("Logging you in ...");

        mGoogleLoginBtn.setOnClickListener(MainActivity.this);
        mEmailLoginBtn.setOnClickListener(MainActivity.this);

        mSp = getSharedPreferences("trip_id", MODE_PRIVATE);
        int id = mSp.getInt("id",0);
        if(id==0)
        {
            SharedPreferences.Editor editor = getSharedPreferences("trip_id", MODE_PRIVATE).edit();
            editor.putInt("id",1);
            editor.apply();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("591907888973-v3b425ufstuki32gabgtrouam8jqrlra.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null){
                }
            }
        };
    }

    public void forgotPassword(View view) {
        boolean valid = true;
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        if(!valid) return;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = mEmailField.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "A mail has been sent to you to reset your password.", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(task.getException().getClass()==FirebaseAuthUserCollisionException.class) {
                                Toast.makeText(MainActivity.this, "User already exists with a different login mode.", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Login unsuccessful or User already exists with a different login mode.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    public void startSignUpActivity(View view) {
        Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(MainActivity.this, "Login unsuccessful.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Toast.makeText(this, "Google play services error.", Toast.LENGTH_LONG).show();
        Toast.makeText(MainActivity.this, "Login unsuccessful.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.google_login_button :
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent,RC_SIGN_IN);
                break;

            case R.id.custom_signin_button:
                signInWithEmail(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mProgressDialog.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            statusCode = result.getStatus().getStatusCode();
            handleSignInResult(result);
        }

    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        }else {
            Toast.makeText(this, "Login Unsuccessful. ", Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
            // Signed out, show unauthenticated UI.
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            addUser("google");
                        }else{
                            mProgressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Login unsuccessful.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void signInWithEmail(String email, String password) {
        if (!validateForm()) {
            Toast.makeText(MainActivity.this, "Email and password required.", Toast.LENGTH_LONG).show();
            return;
        }
        mProgressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //if(mAuth.getCurrentUser().isEmailVerified()) {
                            addUser("email");
                            //}
                           /* else {
                                mProgressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Email not verified. Please verify and login again.", Toast.LENGTH_LONG).show();
                            }*/

                        } else {
                            // If sign in fails, display a message to the user.
                            mProgressDialog.dismiss();
                            if(task.getException().getClass()==FirebaseAuthUserCollisionException.class) {
                                Toast.makeText(MainActivity.this, "User already exists with a different login mode.", Toast.LENGTH_LONG).show();
                            }
                            else if(task.getException().getClass() == FirebaseAuthInvalidCredentialsException.class){
                                Toast.makeText(MainActivity.this, "Email or password may be incorrect.", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Login unsuccessful or User already exists with a different login mode.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        return valid;
    }

    private void addUser(String str){
        final String s = str;
        Toast.makeText(MainActivity.this, "Signed in with "+s+"!", Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

