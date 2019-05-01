package com.example.ananyagupta.trappyy;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private TextView mMsg1Tv;
    private AutoCompleteTextView mSearchEt;
    private SharedPreferences mSp;
    private EditText editText;
    private MyHelper mMyHelper;
    private SQLiteDatabase mdb;
    private ArrayList<String> options;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        //mMsg1Tv = (TextView) findViewById(R.id.textView2);
        mMyHelper = new MyHelper(HomeActivity.this, "trips2" , null, 1);
        mdb = mMyHelper.getReadableDatabase();
        Cursor c = mdb.query("trips2",null,null,null,null,null, null);

        mSearchEt = (AutoCompleteTextView) findViewById(R.id.editText2);
        //tv=(TextView)findViewById(R.id.editText2);
        options = new ArrayList<String>();
        while(c.moveToNext())
        {
            String p = c.getString(2);
            if(!options.contains(p))
                options.add(p);
        }
        mSearchEt.setText("");
        mMsg1Tv = (TextView) findViewById(R.id.user_text);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,options);
        mSearchEt.setThreshold(1);//will start working from first character
        mSearchEt.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        //mSearchEt.setTextColor(Color.BLUE);
        mSearchEt.setAdapter(adapter);

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchTrip();
                    return true;
                }
                return false;
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        if(name!=null)
            mMsg1Tv.setText("Hi "+name+", tap here to share travel stories!");
        else
            mMsg1Tv.setText("Hi there, tap here to share travel stories!");

        final TapTargetSequence tp = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.editText2), "Search", "Search for stories about different places uploaded by other users")
                                .outerCircleColor(R.color.blue)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.white)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text// Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)// Specify a custom drawable to draw as the target
                                .targetRadius(40),
                        TapTarget.forView(findViewById(R.id.addStory), "Trip", "Share your wonderful travel experiences with others by clicking here")
                                .outerCircleColor(R.color.blue)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.white)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text// Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)// Specify a custom drawable to draw as the target
                                .targetRadius(60)
                        .transparentTarget(true),
                        TapTarget.forView(findViewById(R.id.currency), "Explore", "Explore more utilities")
                                .outerCircleColor(R.color.blue)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.white)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text// Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)// Specify a custom drawable to draw as the target
                                .targetRadius(60))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        //Toast.makeText(HomeActivity.this, "djjd", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                });
        tp.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSearchEt.setText("");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("591907888973-v3b425ufstuki32gabgtrouam8jqrlra.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mSearchEt.setText("");
    }

    public void logOut() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(HomeActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(HomeActivity.this);
        }
        builder.setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        // ...
                                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(i);
                                    }
                                });
                        Toast.makeText(HomeActivity.this,"Signed Out",Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void addDetails(View view) {
        Intent intent = new Intent(HomeActivity.this,AddTripDetails.class);
        startActivity(intent);
    }

    public void searchTrip() {
        Cursor c = mdb.query("trips2",null,null,null,null,null, null);
        int k=0;
        while (c.moveToNext()) {
            //Toast.makeText(this, "Found data in database", Toast.LENGTH_SHORT).show();
            if (c.getString(2).equals(mSearchEt.getText().toString())) {
                k=1;
                Intent intent = new Intent(HomeActivity.this, ShowTripActivity.class);
                intent.putExtra("place", mSearchEt.getText().toString());
                startActivity(intent);
                break;
            }
            if(k==1)
                break;
        }
        if(k==0)
        {
            Toast.makeText(this, "No stories uploaded for this place :(", Toast.LENGTH_SHORT).show();
            mSearchEt.setText("");
        }
    }

    public void goToCurrency(View view) {
        Intent intent = new Intent(HomeActivity.this, CurrencyConverterActivity.class);
        startActivity(intent);
    }

    public void goToTimeZone(View view) {
        Intent intent = new Intent(HomeActivity.this, TimeConverterActivity.class);
        startActivity(intent);
    }

    public void goToNearby(View view) {
        Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    public void goToScanner(View view) {
        Intent intent = new Intent(HomeActivity.this, QRScannerActivity.class);
        startActivity(intent);
    }

    public void goToUber(View view) {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.ubercab");
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.ubercab"));
            startActivity(intent);
        }
    }
}
