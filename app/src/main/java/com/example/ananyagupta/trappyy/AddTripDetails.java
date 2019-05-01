package com.example.ananyagupta.trappyy;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class AddTripDetails extends AppCompatActivity {

    private TextView mDurationEt;
    private MyHelper mMyHelper;
    private SQLiteDatabase mdb;
    private EditText mCityEt;
    private EditText mDescriptionEt;
    private EditText mRestaurantEt;
    private EditText mHotelEt;
    private EditText mInterestingEt;
    private EditText mHashTagEt;
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_details);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        mDurationEt = (TextView) findViewById(R.id.editText3);
        mCityEt = (EditText) findViewById(R.id.editText);
        mDescriptionEt = (EditText) findViewById(R.id.editText4);
        mRestaurantEt = (EditText) findViewById(R.id.editText5);
        mHotelEt = (EditText) findViewById(R.id.editText6);
        mInterestingEt = (EditText) findViewById(R.id.editText7);
        mHashTagEt = (EditText) findViewById(R.id.editText8);

        mMyHelper = new MyHelper(AddTripDetails.this,"trips2",null,1);
        mdb = mMyHelper.getWritableDatabase();
    }

    public void shareTrip(View view) {
        if(mCityEt.getText().toString().equals("") || mDurationEt.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please enter a place and duration of visit", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues cv = new ContentValues();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            mSp = getSharedPreferences("trip_id", MODE_PRIVATE);
            int id = mSp.getInt("id", 0);
            cv.put("id", id);
            //Toast.makeText(this, id+"", Toast.LENGTH_SHORT).show();
            id++;
            SharedPreferences.Editor editor = getSharedPreferences("trip_id", MODE_PRIVATE).edit();
            editor.putInt("id", id);
            editor.commit();
            cv.put("email", email);
            cv.put("place", mCityEt.getText().toString());
            cv.put("duration", mDurationEt.getText().toString());
            cv.put("description", mDescriptionEt.getText().toString());
            cv.put("restaurants", mRestaurantEt.getText().toString());
            cv.put("hotels", mHotelEt.getText().toString());
            cv.put("interesting", mInterestingEt.getText().toString());
            cv.put("hashtags", mHashTagEt.getText().toString());
            long i = mdb.insert("trips2", null, cv);
            if (i == -1)
                Toast.makeText(this, "Sorry there was an error! Please try again later", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Your trip details were saved!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
