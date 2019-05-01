package com.example.ananyagupta.trappyy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmailEt;
    private EditText mPasswordEt;
    private EditText mConfirmEt;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        getSupportActionBar().hide();

        mEmailEt = (EditText)findViewById(R.id.signup_email_edittext);
        mPasswordEt = (EditText)findViewById(R.id.signup_password_edittext);
        mConfirmEt = (EditText)findViewById(R.id.signup_confirmpassword_edittext);
        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(SignUpActivity.this);
        mProgressDialog.setCancelable(false);//you can not cancel it by pressing back button
        mProgressDialog.setMessage("Signing you up ...");
    }

    @Override
    public void onBackPressed() {
        startLoginActivity();
    }

    private void startLoginActivity(){
        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void signupUser(View view) {
        startSignUpMethod(mEmailEt.getText().toString(),mPasswordEt.getText().toString());
    }

    private void startSignUpMethod(String email, String password) {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connManager .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (!wifi.isConnected()&& !mobile.isConnected()){
            Toast.makeText(SignUpActivity.this, "No internet connectivity.", Toast.LENGTH_SHORT).show();
            return;
            // If Wi-Fi connected
        }
        if (!validateForm()) {
            return;
        }
        mProgressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //addUser();
                            Toast.makeText(SignUpActivity.this, "Sign Up successful!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            mProgressDialog.dismiss();
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignUpActivity.this, "User with this email already exists. Please login with your details.", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, "Sign up failed. Password must be atleast 6 digits long.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = mEmailEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailEt.setError("Required.");
            valid = false;
        } else {
            mEmailEt.setError(null);
        }
        String password = mPasswordEt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordEt.setError("Required.");
            valid = false;
        } else {
            mPasswordEt.setError(null);
        }
        String confirm_password = mConfirmEt.getText().toString();
        if (TextUtils.isEmpty(confirm_password)) {
            mConfirmEt.setError("Required.");
            valid = false;
        } else {
            mConfirmEt.setError(null);
        }
        if(!password.equals(confirm_password)) {
            mConfirmEt.setError("Doesn't match.");
            valid=false;
        }  else {
            mConfirmEt.setError(null);
        }
        return valid;
    }
}

