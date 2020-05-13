package com.example.a2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playbgmRing();
        // Assign the layout login activity_main2 to Main2Activity
        setContentView(R.layout.activity_main2);
        Button logIn=(Button)findViewById(R.id.login);
        logIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get the user name input box this control
                EditText editUsername=(EditText)findViewById(R.id.userID);
                // Extract the user name entered by the user into the input box and save it in nameInLogin
                String nameInLogin=editUsername.getText().toString();
                // Define the intention of a page jump
                Intent intent=new Intent(Main2Activity.this, MainActivity.class);
                // And pass the username nameInLogin to the game page
                intent.putExtra("editUsername",nameInLogin);
                // Get the control of the password input box
                EditText editPassword=(EditText)findViewById(R.id.password);
                // Extract the password entered by the user into the input box and save it in passwordInLogin
                String passwordInLogin=editPassword.getText().toString();
                // And pass the password passwordInLogin to the game page
                intent.putExtra("editPassword",passwordInLogin);
                // If the user name and password entered by the user are the same as the user name
                // and password saved in string.xml,
                // the login is successful and the page is redirected
                if(nameInLogin.equals(getResources().getString(R.string.OldUserName))&&
                        passwordInLogin.equals(getResources().getString(R.string.OldPassword))){
                    startActivity(intent);//Jump to the game page
                }
                // If the user name and password do not match, print a prompt box to remind
                else{
                    Toast.makeText(Main2Activity.this,"Incorrect username or password", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public AssetManager assetManager;
    public MediaPlayer playbgmRing() {
        MediaPlayer mediaPlayer = null;
        try {
            mediaPlayer = new MediaPlayer();
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("backgroundMusic.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }
}
