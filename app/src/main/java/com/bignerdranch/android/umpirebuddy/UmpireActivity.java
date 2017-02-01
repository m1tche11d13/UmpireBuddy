package com.bignerdranch.android.umpirebuddy;

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

public class UmpireActivity extends AppCompatActivity {

    private static int mBallCount = 0;
    private static int mStrikeCount = 0;

    private Button mBallButton;
    private Button mStrikeButton;

    private TextView mBallTextView;
    private TextView mStrikeTextView;

    private String mBallString = "Ball: 0";
    private String mStrikeString = "Strike: 0";

    private void updateCount(Character c){
        if(c == 'b'){
            ++mBallCount;
            if(mBallCount == 4){
                mBallCount = 0;
                mStrikeCount = 0;
                Toast.makeText(UmpireActivity.this, R.string.walk_toast, Toast.LENGTH_SHORT).show();
            }
        }
        if(c == 's'){
            ++mStrikeCount;
            if(mStrikeCount == 3){
                mStrikeCount = 0;
                mBallCount = 0;
                Toast.makeText(UmpireActivity.this, R.string.out_toast, Toast.LENGTH_SHORT).show();
            }
        }
        mStrikeString = "Strike: " + mStrikeCount;
        mBallString = "Ball: " + mBallCount;

        mBallTextView.setText(mBallString);
        mStrikeTextView.setText(mStrikeString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umpire);

        mBallTextView = (TextView) findViewById(R.id.ball_text_view);
        mStrikeTextView = (TextView) findViewById(R.id.strike_text_view);

        //Updates the count with a meaningless character to set both to 0 initially
        updateCount('_');

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
                mBallCount = 0;
                mStrikeCount = 0;
                updateCount('_');
                return true;
            case R.id.about:
                //showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
