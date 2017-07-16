package hell.mygames.wordladder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import hell.mygames.R;
public class SolverActivity extends AppCompatActivity {

    String[] sol ;

    LinearLayout linearLayout ;
    TextView startTextView , endTextView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_solver);

        linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
        startTextView =(TextView)findViewById(R.id.startTextView);
        endTextView = (TextView)findViewById(R.id.endTextView);




        Intent intent = getIntent();
        sol = intent.getStringArrayExtra("solution");


        startTextView.setText(""+sol[0]);


        endTextView.setText(""+sol[sol.length-1]);


        addViews();





    }

    private void addViews() {

        while(linearLayout.getChildCount()>0&&linearLayout.getChildCount()>4)
            linearLayout.removeViewAt(1);

        int size = sol.length -2 ;
        //number of views to be added
        int i=0;
        while(i<size) {

            //LinearLayout.LayoutParams()
            LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText myEditText = new EditText(getBaseContext());


            mRparams.setMargins(5, 5, 5, 5);

            myEditText.setLayoutParams(mRparams);
            myEditText .setImeOptions(EditorInfo.IME_ACTION_NEXT);
            myEditText .setInputType(InputType.TYPE_CLASS_TEXT);
            myEditText .setMaxLines(1);

            myEditText.setBackgroundColor(Color.rgb(175,238,238));
            myEditText.setTextSize(18);
            myEditText.setTextColor(Color.BLACK);
            myEditText.setText("");
            linearLayout.addView(myEditText,1);
            i++;
//            linearLayout.addView();
        }

    }

    public void addWord(View view) {

        Log.d("add","1");
        LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        EditText myEditText = new EditText(getBaseContext());

        mRparams.setMargins(5, 5, 5, 5);
        myEditText.setLayoutParams(mRparams);
        myEditText .setImeOptions(EditorInfo.IME_ACTION_NEXT);

        myEditText.requestFocusFromTouch();
        InputMethodManager lManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        lManager.showSoftInput(myEditText, 0);


        myEditText.setBackgroundColor(Color.rgb(175,238,238));
        myEditText .setInputType(InputType.TYPE_CLASS_TEXT);
        myEditText .setMaxLines(1);
        myEditText.setTextSize(15);
        myEditText.setTextColor(Color.BLACK);
        myEditText.setText("");
        linearLayout.addView(myEditText,linearLayout.getChildCount()-3);


        Log.d("onSolve"," "+linearLayout.getChildCount());
    }

    public void removeWord(View view) {

        Log.d("remove","1");
        if(linearLayout.getChildCount()>0&&linearLayout.getChildCount()>4)
            linearLayout.removeViewAt(linearLayout.getChildCount()-4);
        Log.d("onSolve"," "+linearLayout.getChildCount());
    }

    public void onSolve(View view) {


        Log.d("onSolve","1");
        //remove remaining four
        while(linearLayout.getChildCount()>0&&linearLayout.getChildCount()>4)
            linearLayout.removeViewAt(1);

        int size = sol.length -2 ;
        //number of views to be added
        int i=0;
        while(i<size) {

            //LinearLayout.LayoutParams()
            LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText myEditText = new EditText(getBaseContext());
            myEditText.setLayoutParams(mRparams);
            myEditText .setImeOptions(EditorInfo.IME_ACTION_NEXT);
            myEditText .setInputType(InputType.TYPE_CLASS_TEXT);
            myEditText .setMaxLines(1);
            mRparams.setMargins(5, 5, 5, 5);

            myEditText.setBackgroundColor(Color.rgb(175,238,238));
            myEditText.setTextSize(18);
            myEditText.setTextColor(Color.BLACK);
            myEditText.setText(""+sol[sol.length - i - 2]);
            linearLayout.addView(myEditText,1);
            i++;
//            linearLayout.addView();
        }

        Log.d("onSolve"," "+linearLayout.getChildCount());


    }

    public void onCheckSolution(View view) {


        boolean isSolution =true ;
        Log.d("onSolve","1");

        int size = sol.length -2 ;
        //number of views to be added
        int i=0;
        while(i<size) {

            //LinearLayout.LayoutParams()

            EditText edt = (EditText) linearLayout.getChildAt(i+1);
            String str = String.valueOf(edt.getText()).trim();
            Log.d("I",i+":::"+str+"::::"+sol[sol.length - i - 2] );
            if(str.equals( sol[i + 1] )==false) {
                isSolution = false;
                break;
            }
                i++;
//            linearLayout.addView();
        }

        if(isSolution==true){
            Toast toast = Toast.makeText(this, "WELL DONE SOLVED",
                    Toast.LENGTH_SHORT);
            toast.show();
        }else{
            Toast toast = Toast.makeText(this, "TRY AGAIN",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        Log.d("onSolve"," "+linearLayout.getChildCount());



    }

    public void onExit(View view) {
        finish();
    }
}
