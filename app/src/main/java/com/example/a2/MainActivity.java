package com.example.a2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import static com.example.a2.R.id.renjichoice_jiandan;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get man-machine battle button control by id
        Button newGame = (Button) findViewById(R.id.new_game);
        //Get two-player button control by id
        Button fight = (Button) findViewById(R.id.fight);
        // Get logout button control by id
        Button logout = findViewById(R.id.bt_main_logout);
        //Get feedback button control by id
        Button feedback = findViewById(R.id.feedback);
//        //Get the control button of networked battle by id
//        Button netFight = (Button) findViewById(R.id.conn_fight);
        // Get the control button pf Tutorial by id
        Button netFight = (Button) findViewById(R.id.conn_fight);
        // Get the control switch of music by id
        Switch musicSwitch = (Switch) findViewById(R.id.switch_music);


        //Register the listener for the man-machine battle button
        newGame.setOnClickListener(new OnClickListener() {
            Intent i ;
            @Override
            public void onClick(View v) {
                // When the man-machine battle button is clicked,
                // the AlterDialog popup window first appears
                AlertDialog mydialog= new AlertDialog.Builder(MainActivity.this).create();
                mydialog.show();
                // Assign value to popup content，
                // game_dialog is the game difficulty mode selection: simple and difficult
                mydialog.getWindow().setContentView(R.layout.game_dialog);
                // Register the listener for the simple mode in the pop-up window,
                // if the user clicks the simple mode,
                // Jump to the SingleGameActivity interface and send a flag = 1 message
                mydialog.getWindow()
                        .findViewById(renjichoice_jiandan)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                i = new Intent(MainActivity.this,SingleGameActivity.class);
                                i.putExtra("flag",1);
                                startActivity(i);
                            }
                        });
                // Register the listener for the hard mode in the pop-up window,
                // if the user clicks the hard mode,
                // Jump to the SingleGameActivity interface and send a message with flag = 2
                mydialog.getWindow()
                        .findViewById(R.id.renjichoice_kunnan)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                i = new Intent(MainActivity.this,SingleGameActivity.class);
                                //Settings appear from the right
                                i.putExtra("flag",2);
                                startActivity(i);
                            }
                        });
            }
        });

        //Register a listener for a two-player battle button
        // When the button is clicked, jump to the FightGameActivity.class interface
        fight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FightGameActivity.class));
            }
        });

        //Register a listener for logout button
        // When the button is clicked, jump to the LoginActivity.class interface
        logout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("Account Data", 0).edit();
                editor.putBoolean("REM_ISCheck", false);
                editor.putBoolean("AUTO_ISCHECK", false);
                editor.apply();

                Intent intent_login = new Intent();
                intent_login.setClass(MainActivity.this, LoginActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //关键的一句，将新的activity置为栈顶
                startActivity(intent_login);
                finish();
            }
        });

        feedback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                        "mailto", "wanglihua@deakin.edu.au", null));
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Go Bang Application");
//                intent.putExtra(Intent.EXTRA_TEXT, "What I would like to give feedback about:");
//                startActivity(intent);
                startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
            }
        });

        //为联网对战按钮注册监听器
        //当该按钮被点击时，跳转到ConnectionActivity.class界面
        netFight.setOnClickListener(new OnClickListener() {

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
