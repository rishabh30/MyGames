package hell.mygames.scarnedice;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import hell.mygames.R;

public class DiceActivity extends AppCompatActivity {

    /*
    Implementing the game
    Four global variables to store:
        the user's overall score state
        the user's turn score
        the computer's overall score
        the computer's turn score
        */
    public int userOverall=0;
    public int userTurn=0;
    public int computerOverall=0;
    public int computerTurn=0;
    public int selectedNum=0;

    ImageView diceImg;
    TextView textView;
    Button reset ;
    Button hold ;
    Button roll;


    Animation rotate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);


        diceImg = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);
        reset = (Button)findViewById(R.id.reset);
        hold = (Button)findViewById(R.id.hold);
        roll = (Button)findViewById(R.id.roll);

        userOverall=0;
        userTurn=0;
        computerOverall=0;
        computerTurn=0;
        selectedNum=0;

        rotate = AnimationUtils.loadAnimation(this,R.anim.wobble);

        new AlertDialog.Builder(this).setTitle("RULES").setMessage("" +
                "FIRST ONE TO REACH 100 WINS !! \n" +
                "THROW DICE AS MANY TIME U WANT \n" +
                "IF 1 COMES YOUR TURN SCORE BECOME ZERO " ).setNeutralButton("OK", null).show();

        textView.setText("Your score: " + userOverall + "  computer score: " + computerOverall );
    }


    /*
    A click handler for the "Roll" button that will:
        randomly select a dice value
        update the display to reflect the rolled value*/

    public int getScore(){
        Random random  =new Random();
        int selectNum = random.nextInt(6)+1;
        Drawable drawable=null;

        switch(selectNum){
            case 1:
                drawable = getResources().getDrawable(R.drawable.dice1);
                break;
            case 2:
                drawable = getResources().getDrawable(R.drawable.dice2);
                break;
            case 3:
                drawable = getResources().getDrawable(R.drawable.dice3);
                break;
            case 4:
                drawable = getResources().getDrawable(R.drawable.dice4);
                break;
            case 5:
                drawable = getResources().getDrawable(R.drawable.dice5);
                break;
            case 6:
                drawable = getResources().getDrawable(R.drawable.dice6);
                break;
        }
        diceImg.setImageDrawable(drawable);
        Log.d("getScore():"," "+selectNum);
        return  selectNum;
    }

    public void roll(View view) {

        Log.d("Turn ", "User");
        diceImg.startAnimation(rotate);
        selectedNum = getScore();
        if(selectedNum==1){
            userTurn=0;
        }else{
            userTurn+=selectedNum;
        }

        textView.setText("Your score: " + userOverall + "  computer score: " + computerOverall
                            + " your turn score: " + userTurn );



        if(selectedNum == 1){
            ComputerTurn();
        }

    }


    void WinnerHelper(String winner){
        new AlertDialog.Builder(this).setMessage( winner + " WINS " ).setNeutralButton("OK", null).show();

        userOverall=0;
        userTurn=0;
        computerOverall=0;
        computerTurn=0;
        textView.setText("Your score: " + userOverall + "  computer score: " + computerOverall );
    }

    /*
        When ResetButton is clicked, reset the 4 global variables to 0 and update the label text
        When HoldButton is clicked, updating the user's overall score,
            reset the user round score and update the label.
    */
    public void hold(View view) {

        userOverall+=userTurn;
        userTurn=0;

        textView.setText("Your score: " + userOverall + "  computer score: " + computerOverall );
        if(userOverall>=100){
            WinnerHelper("USER");
            return;
        }
        ComputerTurn();
    }

    public void reset(View view) {
        userOverall=0;
        userTurn=0;
        computerOverall=0;
        computerTurn=0;
        textView.setText("Your score: " + userOverall + "  computer score: " + computerOverall );
    }

    /*
    *   simple strategy for the computer:
    *   if the computer's round score is less than 20,re-roll, otherwise hold.
    */
    public void ComputerTurn() {
        Log.d("Turn ", "Computer");
        roll.setEnabled(false);
        hold.setEnabled(false);
        selectedNum = 0 ;
        timerHandler.postDelayed(timerRunnable, 2000);
    }

    public void EndComputerTurn(){
        Log.d("SelectedNum : ",""+selectedNum);
        if(selectedNum==1){
            computerTurn=0;
        }
        computerOverall+=computerTurn;
        computerTurn=0;

        textView.setText("Your score: " + userOverall + "  computer score: " + computerOverall
                + " Computer turn score: " + computerTurn );
        cleanUpComputer();
    }

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            selectedNum = getScore();
            computerTurn+=selectedNum;
            diceImg.startAnimation(rotate);
            textView.setText("Your score: " + userOverall + "  computer score: " + computerOverall
                    + " Computer turn score: " + computerTurn );
            if(selectedNum!=1&&computerOverall+computerTurn>=100){
                EndComputerTurn();
                WinnerHelper("COMPUTER");
            } else if(computerTurn<15&&selectedNum!=1) {
                timerHandler.postDelayed(this, 2000);
            }else{
                EndComputerTurn();
            }
        }
    };
    public void cleanUpComputer() {
        roll.setEnabled(true);
        hold.setEnabled(true);
        textView.setText("Your score: " + userOverall + "  computer score: " + computerOverall );
    }
}