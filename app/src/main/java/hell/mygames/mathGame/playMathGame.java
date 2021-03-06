package hell.mygames.mathGame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

import hell.mygames.R;


public class playMathGame extends ActionBarActivity implements View.OnClickListener {

    TextView multiplicant1;
    TextView multiplicant2;
    TextView leveltextView;
    TextView scoreTextView;
    Toast correctNess;
    Button option1;
    Button option2;
    Button option3;

    int level=1;
    int score=0;
    int correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_math_game);

        multiplicant1 = (TextView)findViewById(R.id.multiplicant1);
        multiplicant2 = (TextView)findViewById(R.id.multiplicant2);
        leveltextView = (TextView)findViewById(R.id.leveltextView);
        scoreTextView = (TextView)findViewById(R.id.scoreTextView);
        option1 = (Button)findViewById(R.id.option1);
        option2 = (Button)findViewById(R.id.option2);
        option3 = (Button)findViewById(R.id.option3);

        makeQuestion();
        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
    }




    private void makeQuestion() {
        leveltextView.setText("LEVEL : " + String.valueOf(level));
        scoreTextView.setText("SCORE : " + String.valueOf(score));

        int rangeNum=level*3;
        Random random = new Random();

        int A = random.nextInt(rangeNum)+1;
        int B = random.nextInt(rangeNum)+1;

        multiplicant1.setText(""+String.valueOf(A));
        multiplicant2.setText(""+String.valueOf(B));

        int optionA = A*B ;
        correctAnswer =A*B;

        int optionB = random.nextInt(2*A*B) ;
        int optionC = random.nextInt(2*A*B) ;

        switch (random.nextInt(3)+1)
        {
            case 1:
                option1.setText(""+optionA);
                option2.setText(""+optionB);
                option3.setText(""+optionC);
                break;
            case 2:
                option1.setText(""+optionB);
                option2.setText(""+optionA);
                option3.setText(""+optionC);
                break;
            case 3:
                option1.setText(""+optionC);
                option2.setText(""+optionB);
                option3.setText(""+optionA);
                break;
        }




    }

    @Override
    public void onClick(View view) {

        if(correctNess!=null) {
            correctNess.cancel();
        }
        Button b = (Button) findViewById(view.getId());
        String selectedAnswer = (String) b.getText();
        int selectedInt = Integer.parseInt(selectedAnswer);
        if(correctAnswer==selectedInt)
        {
            correct();
        }
        else
        {
            wrong();
        }
        correctNess.show();
        makeQuestion();
    }

    private void correct() {
        score += level ;
        level++;

        correctNess  = Toast.makeText(this,"Correct Answer ",Toast.LENGTH_SHORT);
    }

    private void wrong() {

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        int highScore = shared.getInt(getString(R.string.scoreMathGame),0);
        if(score>highScore) {
            SharedPreferences.Editor edit = shared.edit();
            edit.putInt(getString(R.string.scoreMathGame), score);
            edit.apply();
            correctNess  = Toast.makeText(this,"HIGHEST SCORE",Toast.LENGTH_SHORT);
        }else
        {
            correctNess  =  Toast.makeText(this,"Wrong Answer ",Toast.LENGTH_SHORT);
        }

        level=1;score=0;
    }

    @Override
    public void onBackPressed()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Want To Quit ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
         builder.show();

    }
}
