package hell.mygames.mathGame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import hell.mygames.R;


public class MathGame extends ActionBarActivity {


    TextView highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_game);
        highScore = (TextView)findViewById(R.id.highScore);

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        int score=shared.getInt(getString(R.string.scoreMathGame),0);
        highScore.setText("HighScore : "+score);

    }


    public void play(View view) {
        Intent intent = new Intent(this,playMathGame.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        int score=shared.getInt(getString(R.string.scoreMathGame),0);
        highScore.setText("HighScore : "+score);
    }
}
