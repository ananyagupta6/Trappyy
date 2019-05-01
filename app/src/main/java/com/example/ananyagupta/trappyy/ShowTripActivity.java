package com.example.ananyagupta.trappyy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ShowTripActivity extends AppCompatActivity {

    private TextView mPlaceTv;
    private TextView mDurationTv;
    private TextView mDescriptionTv;
    private String place;
    private MyHelper mMyHelper;
    private SQLiteDatabase mdb;
    private Cursor c;
    private TextView mDurTv;
    private TextView mEmailTv;
    private RelativeLayout mThings;
    private TextView mThingsTv;
    private RelativeLayout mPLacesToEat;
    private TextView mPLacesToEatTv;
    private RelativeLayout mHotels;
    private TextView mHotelsTv;
    private RelativeLayout mInteresting;
    private TextView mInterestingTv;
    private RelativeLayout mHashtags;
    private TextView mHashtagsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trip);

        mPlaceTv = (TextView) findViewById(R.id.textView13);
        mDurTv = (TextView) findViewById(R.id.textView14);
        mDurationTv = (TextView) findViewById(R.id.textView15);
        mEmailTv = (TextView) findViewById(R.id.textView27);
        mThings = (RelativeLayout) findViewById(R.id.thingstodo);
        mThingsTv = (TextView) findViewById(R.id.todo);
        mPLacesToEat = (RelativeLayout) findViewById(R.id.placestoeat);
        mPLacesToEatTv = (TextView) findViewById(R.id.eatdrink);
        mHotels = (RelativeLayout) findViewById(R.id.hotels);
        mHotelsTv = (TextView) findViewById(R.id.hotelstv);
        mInteresting = (RelativeLayout) findViewById(R.id.interesting);
        mInterestingTv =(TextView) findViewById(R.id.interestingness);
        mHashtags = (RelativeLayout) findViewById(R.id.hashtags);
        mHashtagsTv = (TextView) findViewById(R.id.heading5);

        mThings.setVisibility(View.GONE);
        mPLacesToEat.setVisibility(View.GONE);
        mHotels.setVisibility(View.GONE);
        mInteresting.setVisibility(View.GONE);
        mHashtags.setVisibility(View.GONE);

        Intent intent = getIntent();
        place = intent.getStringExtra("place");
        mPlaceTv.setText(place);

        mMyHelper = new MyHelper(ShowTripActivity.this, "trips2" , null, 1);
        mdb = mMyHelper.getReadableDatabase();
        c = mdb.query("trips2",null,null,null,null,null, null);

        displayData();
    }

    private void displayData() {
        int k=1;
        while (c.moveToNext())
        {
            //Toast.makeText(this, "Found data in database", Toast.LENGTH_SHORT).show();
            if(c.getString(2).equals(place))
            {
                mThings.setVisibility(View.GONE);
                mPLacesToEat.setVisibility(View.GONE);
                mHotels.setVisibility(View.GONE);
                mInteresting.setVisibility(View.GONE);
                mHashtags.setVisibility(View.GONE);
                k=0;
                mDurationTv.setText(c.getString(4));
                mEmailTv.setText(c.getString(1));

                if(!c.getString(3).equals("")){
                    mThings.setVisibility(View.VISIBLE);
                    mThingsTv.setText(c.getString(3));
                }

                if(!c.getString(5).equals("")){
                    mPLacesToEat.setVisibility(View.VISIBLE);
                    mPLacesToEatTv.setText(c.getString(5));
                }

                if(!c.getString(6).equals("")){
                    mHotels.setVisibility(View.VISIBLE);
                    mHotelsTv.setText(c.getString(6));
                }

                if(!c.getString(7).equals("")){
                    mInteresting.setVisibility(View.VISIBLE);
                    mInterestingTv.setText(c.getString(7));
                }

                if(!c.getString(8).equals("")){
                    mHashtags.setVisibility(View.VISIBLE);
                    mHashtagsTv.setText(c.getString(8));
                }

                break;
            }
        }
        if(k==1)
            Toast.makeText(this, "No more stories!", Toast.LENGTH_SHORT).show();
    }

    public void goNext(View view) {
        if(!c.moveToNext())
        {
            Toast.makeText(this, "No more stories!", Toast.LENGTH_SHORT).show();
            c.moveToPrevious();
        }
        else {
            c.moveToPrevious();
            displayData();
        }
    }

    public void goBack(View view) {
        //c.moveToPrevious();
        int k=1;
        if(!c.moveToPrevious())
        {
            Toast.makeText(this, "No more stories!", Toast.LENGTH_SHORT).show();
            c.moveToNext();
        }
        else {
            c.moveToNext();
            while (c.moveToPrevious())
            {
                //Toast.makeText(this, "Found data in database", Toast.LENGTH_SHORT).show();
                if(c.getString(2).equals(place))
                {
                    mThings.setVisibility(View.GONE);
                    mPLacesToEat.setVisibility(View.GONE);
                    mHotels.setVisibility(View.GONE);
                    mInteresting.setVisibility(View.GONE);
                    mHashtags.setVisibility(View.GONE);
                    k=0;
                    mDurationTv.setText(c.getString(4));
                    mEmailTv.setText(c.getString(1));

                    if(!c.getString(3).equals("")){
                        mThings.setVisibility(View.VISIBLE);
                        mThingsTv.setText(c.getString(3));
                    }

                    if(!c.getString(5).equals("")){
                        mPLacesToEat.setVisibility(View.VISIBLE);
                        mPLacesToEatTv.setText(c.getString(5));
                    }

                    if(!c.getString(6).equals("")){
                        mHotels.setVisibility(View.VISIBLE);
                        mHotelsTv.setText(c.getString(6));
                    }

                    if(!c.getString(7).equals("")){
                        mInteresting.setVisibility(View.VISIBLE);
                        mInterestingTv.setText(c.getString(7));
                    }

                    if(!c.getString(8).equals("")){
                        mHashtags.setVisibility(View.VISIBLE);
                        mHashtagsTv.setText(c.getString(8));
                    }

                    break;
                }
            }
            if(k==1)
                Toast.makeText(this, "No more stories!", Toast.LENGTH_SHORT).show();
        }
    }
}
