package com.example.jelmer.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Gebruiker on 9/10/2015.
 *
 * Class for the title screen and general app initiation (such as statics, also used in other class)
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean aiTurnedOn;
    public static Menu menuObject;

    /* Creates and shows the title screen */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        aiTurnedOn = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startGameButton = (Button) findViewById(R.id.game_start_button);
        startGameButton.setOnClickListener(this);
    }

    /* Creates the menu in the title screen */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuObject = menu;

        return true;
    }

    /* Tells what to do when a setting is changed. Currently, AI can be toggled. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.ai_setting:
                toggleAI();
                setMenuItemTitles();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* When the button on the title screen is pressed, the game activity is created and shown. */
    @Override
    public void onClick(View v)
    {
        Intent i = new Intent(getApplicationContext(),Game.class);
        startActivity(i);
    }

    /* Toggles the AI, can be used from the settings menu */
    public static void toggleAI() {
        aiTurnedOn = !aiTurnedOn;
    }

    /* The title of the AI item changed when it used, has to be done when creating the menu a second time and whenever it is toggled */
    public static void setMenuItemTitles() {
        MenuItem item = menuObject.findItem(R.id.ai_setting);
        if(aiTurnedOn) {
            item.setTitle("AI is on!");
        } else {
            item.setTitle("AI is off.");
        }
    }
}