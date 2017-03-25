package hell.mygames.memoryGame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.logging.LogRecord;

import hell.mygames.R;


public class playMemoryGame extends ActionBarActivity implements View.OnClickListener {


    TextView leveltextView;
    TextView scoreTextView;
    TextView instruction;
    Button option1;
    Button option2;
    Button option3;
    Button option4;
    Button restart;

    boolean gameOn;
    boolean isWobble;       // button wobble to notice user
    boolean isResponding;

    int level=1;
    int score=0;
    int correctAnswer;
    long startTime = System.currentTimeMillis();
    private Handler  myHandler;

    int numberOfResponsesdone=0;
    int numberOfResponses=0;
    int savedResponses[]= new int [100];

    int highScore = 0;
    Animation wobble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_memory_game);

        leveltextView = (TextView)findViewById(R.id.leveltextView);
        scoreTextView = (TextView)findViewById(R.id.scoreTextView);
        instruction = (TextView)findViewById(R.id.instruction);
        option1 = (Button)findViewById(R.id.option1);
        option2 = (Button)findViewById(R.id.option2);
        option3 = (Button)findViewById(R.id.option3);
        option4 = (Button)findViewById(R.id.option4);
        restart = (Button)findViewById(R.id.restart);
        gameOn = false;

        wobble = AnimationUtils.loadAnimation(this,R.anim.wobble);


        myHandler=new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(isWobble)
                {
                    if(System.currentTimeMillis()-startTime>1200)
                    {
                        startTime=System.currentTimeMillis();
                        //do display sequence according to the level and is responding

                        if( numberOfResponses < level+2 )
                        {
                            //Wobble Buttons

                            savedResponses[numberOfResponses]= getButtonWobble();
                            numberOfResponses++;

                        }

                        if(numberOfResponses==level+2)
                        {
                            isWobble=false;
                            numberOfResponsesdone=0;
                            isResponding = true;

                            instruction.setText("ENTER THE SEQUENCE");
                        }

                    }
                }

                myHandler.sendEmptyMessageDelayed(0,1200);
            }
        };

        leveltextView.setText("LEVEL : " + String.valueOf(level));
        scoreTextView.setText("SCORE : " + String.valueOf(score));

        gameOn = true;
        isWobble = false ;
        isResponding = false;
        myHandler.sendEmptyMessage(0);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
    }

    private int getButtonWobble() {

        Random rand = new Random();
        int numberWobble = rand.nextInt(4)+1;

        switch(numberWobble)
        {
            case 1:option1.startAnimation(wobble);break;
            case 2:option2.startAnimation(wobble);break;
            case 3:option3.startAnimation(wobble);break;
            case 4:option4.startAnimation(wobble);break;
            default:option1.startAnimation(wobble);
        }

        return numberWobble;
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

    @Override
    public void onClick(View view) {

        if(isResponding)
        {
            Button option = (Button)findViewById(view.getId());
            int choice = Integer.parseInt((String) option.getText());
            switch(choice)
            {
                case 1:checkAnswer(1);break;
                case 2:checkAnswer(2);break;
                case 3:checkAnswer(3);break;
                case 4:checkAnswer(4);break;
                default:checkAnswer(1);
            }
            numberOfResponsesdone++;
        }

    }

    private void checkAnswer(int i) {

        if(i == savedResponses[numberOfResponsesdone])
        {
            // correct check for if it is last element
            if(numberOfResponsesdone == numberOfResponses -1)
            {
                Toast.makeText(this,"CORRECT ANSWER ",Toast.LENGTH_SHORT).show();
                level++;
                score += level ;
                start();
            }
        }
        else
        {
            // wrong back to pavilion wrong answer


                Toast.makeText(this, "WRONG ANSWER ", Toast.LENGTH_SHORT).show();

            pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        pause();
    }

    public void pause()
    {
        isResponding = false ;
        isWobble = false ;


        leveltextView.setText("LEVEL : " + String.valueOf(level));
        scoreTextView.setText("SCORE : " + String.valueOf(score));

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        int highScore = shared.getInt(getString(R.string.scoreMemoryGame),0);
        if(score>highScore) {
            SharedPreferences.Editor edit = shared.edit();
            edit.putInt(getString(R.string.scoreMemoryGame), score);
            edit.apply();
            Toast.makeText(this,"HIGHEST SCORE :" + score , Toast.LENGTH_SHORT).show();
        }


        instruction.setText("Press Restart for a New Game");
        restart.setText("Restart");

    }
    public void start()
    {
        isWobble = false;
        isResponding=false;
        instruction.setText("Press Play Sequence");

        leveltextView.setText("LEVEL : " + String.valueOf(level));
        scoreTextView.setText("SCORE : " + String.valueOf(score));


        numberOfResponses=0;
        numberOfResponsesdone=0;

        restart.setText("Restart");
    }


    public void restart()
    {
        level = 1;
        score = 0;

        isResponding = false ;
        isWobble = false ;

        numberOfResponsesdone=numberOfResponses=0;

        leveltextView.setText("LEVEL : " + String.valueOf(level));
        scoreTextView.setText("SCORE : " + String.valueOf(score));

        instruction.setText("PRESS START");
        restart.setText("Start");
    }

    public void startSequence(View view) {

        Button restartButton = (Button)findViewById(view.getId());

        if(restartButton.getText().equals("Start"))
        {
            start();
        }
        else
        {
            pause();
            restart();
        }
    }

    public void playSequence(View view) {

        if (instruction.getText().equals("Press Play Sequence")) {
            isWobble = true;
            isResponding = false;
            instruction.setText("Watch Carefully");
        }
    }
}
