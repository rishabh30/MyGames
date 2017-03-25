package hell.mygames.memoryGame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import hell.mygames.R;


public class MemoryGame extends ActionBarActivity {

    TextView highScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        highScore = (TextView)findViewById(R.id.highScore);

        new AlertDialog.Builder(this).setTitle("RULES").setMessage("REMEMBER THE SEQUENCE PLAYED !! ").setNeutralButton("OK", null).show();


        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        int score=shared.getInt(getString(R.string.scoreMemoryGame),0);
        highScore.setText("HighScore : "+score);

    }





    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        int score=shared.getInt(getString(R.string.scoreMemoryGame),0);
        highScore.setText("HighScore : "+score);
    }


    public void play(View view) {
        Intent intent = new Intent(this,playMemoryGame.class);
        startActivity(intent);
        finish();
    }
}
