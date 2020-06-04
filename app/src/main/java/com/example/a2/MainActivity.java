package com.example.a2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2.ForegroundService.BackMusicService;

/**
 * This is the Home page after login
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get AI single game button control by id
        Button singleGame = findViewById(R.id.single);
        //Get two-player fight game button control by id
        Button fightGame = findViewById(R.id.fight);
        // Get logout button control by id
        Button logout = findViewById(R.id.logout);
        //Get feedback button control by id
        Button feedback = findViewById(R.id.feedback);
        // Get the control button of About by id
        Button about = findViewById(R.id.about);
        // Get the control switch of music by id
        Switch musicSwitch = findViewById(R.id.switch_music);


        // Register the listener for the AI Single Game button
        // When the single game button is clicked, jump to the SingleGameActivity.class interface
        singleGame.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SingleGameActivity.class));
            }
        });

        // Register a listener for a two-player battle button
        // When the button is clicked, jump to the FightGameActivity.class interface
        fightGame.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FightGameActivity.class));
            }
        });

        // Register a listener for logout button
        // When the button is clicked, jump to the LoginActivity.class interface
        logout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Clear the status of automatic login and remember password on the login interface
                SharedPreferences.Editor editor = getSharedPreferences("Account Data", 0).edit();
                editor.putBoolean("REM_ISCheck", false);
                editor.putBoolean("AUTO_ISCHECK", false);
                editor.apply();

                // Jump to LoginActivity, and finish the app
                Intent intent_login = new Intent();
                intent_login.setClass(MainActivity.this, LoginActivity.class);
                //The key code, put the new activity at the top of the stack
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });

        // Register a listener for the Feedback button
        // When the button is clicked, jump to the FeedbackActivity.class interface
        feedback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
            }
        });

        // Register a listener for the About button
        // When the button is clicked, jump to the AboutActivity.class interface
        about.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });

        //attaching listener to switch of music service
        //Start background music service
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    // If the switch button is on
                    startService(new Intent(MainActivity.this, BackMusicService.class));
                    // Show the switch button checked status as toast message
                    Toast.makeText(MainActivity.this, "Music Service started.",
                            Toast.LENGTH_LONG).show();
                } else {
                    // If the switch button is off
                    stopService(new Intent(MainActivity.this, BackMusicService.class));
                    // Show the switch button checked status as toast message
                    Toast.makeText(MainActivity.this, "Music Service stopped.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
