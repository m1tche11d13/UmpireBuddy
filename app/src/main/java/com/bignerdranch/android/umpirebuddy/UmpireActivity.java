package com.bignerdranch.android.umpirebuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class UmpireActivity extends AppCompatActivity {

    TextToSpeech tts;

    private static final String PREFS = "preferences";
    private static final String OUT_KEY = "out_key";
    private static final String STRIKE_KEY = "strike_key";
    private static final String BALL_KEY = "ball_key";

    private static int mBallCount = 0;
    private static int mStrikeCount = 0;
    private static int mOutCount = 0;

    private Button mBallButton;
    private Button mStrikeButton;

    private TextView mBallTextView;
    private TextView mStrikeTextView;
    private TextView mOutTextView;

    private String mBallString = "Ball: 0";
    private String mStrikeString = "Strike: 0";
    private String mOutString = "Out: 0";

    private void resetCount(){
        mBallCount = 0;
        mStrikeCount = 0;
        mOutCount = 0;
        updateCount('_');
    }

    private void updateCount(Character c){
        if(c == 'b'){
            ++mBallCount;
            if(mBallCount == 4){    //Batter walks
                mBallCount = 0;
                mStrikeCount = 0;
                Toast.makeText(UmpireActivity.this, R.string.walk_toast, Toast.LENGTH_SHORT).show();
                tts.speak("Walk", TextToSpeech.QUEUE_FLUSH, null);
            }
        }
        if(c == 's'){
            ++mStrikeCount;

            if(mStrikeCount == 3){  //Batter strikes out
                ++mOutCount;
                mStrikeCount = 0;
                mBallCount = 0;
                Toast.makeText(UmpireActivity.this, R.string.out_toast, Toast.LENGTH_SHORT).show();
                tts.speak("Strikeout", TextToSpeech.QUEUE_FLUSH, null);
            }
        }
        mStrikeString = "Strike: " + mStrikeCount;
        mBallString = "Ball: " + mBallCount;
        mOutString = "Total Outs: " + mOutCount;

        mBallTextView.setText(mBallString);
        mStrikeTextView.setText(mStrikeString);
        mOutTextView.setText(mOutString);
    }

    @Override
    protected void onPause(){
        super.onPause();

        //Saves the out count into a preferences file when the onPause function is called
        SharedPreferences prefs = getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(OUT_KEY, mOutCount);
        editor.apply();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umpire);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.US);
                }
            }
        });

        mBallTextView = (TextView) findViewById(R.id.ball_text_view);
        mStrikeTextView = (TextView) findViewById(R.id.strike_text_view);
        mOutTextView = (TextView) findViewById(R.id.out_text_view);

        mBallButton = (Button) findViewById(R.id.ball_button);
        mBallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCount('b');

            }
        });

        mStrikeButton = (Button) findViewById(R.id.strike_button);
        mStrikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCount('s');

            }
        });

        if(savedInstanceState != null){
            mOutCount = savedInstanceState.getInt(OUT_KEY);
            mBallCount = savedInstanceState.getInt(BALL_KEY);
            mStrikeCount = savedInstanceState.getInt(STRIKE_KEY);
        }

        SharedPreferences prefs = getSharedPreferences(PREFS, 0);
        mOutCount = prefs.getInt(OUT_KEY, 0);

        //Updates the count with a meaningless character to set both to 0 initially
        updateCount('_');
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(OUT_KEY, mOutCount);
        savedInstanceState.putInt(BALL_KEY, mBallCount);
        savedInstanceState.putInt(STRIKE_KEY, mStrikeCount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reset:
                resetCount();
                return true;
            case R.id.about:
                Intent i = new Intent(UmpireActivity.this, AboutActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
