/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hell.mygames.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import hell.mygames.R;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    Toast addToast;

    private ProgressBar spinner;
    /*
Since we are not using a standard EditText field, we will need to do some keyboard handling.
Proceed to override the GhostActivity.onKeyUp method. If the key that the user pressed is
not a letter,default to returning the value of super.onKeyUp(). Otherwise, add the letter to
the word fragment.Also check whether the current word fragment is a complete word and, if it
is, update the game status label to indicate so (this is not the right behavior for the game
 but will allow you to verify that your code is working for now).
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        Log.d("SPINNER"," GOING ON");
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

        try{
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new FastDictionary(inputStream);
        }catch (IOException e){
            Toast toast  = Toast.makeText(this,"Couldn't Load Dictionary",Toast.LENGTH_SHORT);
            toast.show();
        }
        Log.d("SPINNER"," GOING OFF");
        spinner.setVisibility(View.GONE);
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        Button btnChallenge = (Button)findViewById(R.id.challenge);
        btnChallenge.setEnabled(true);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            TextView label = (TextView) findViewById(R.id.gameStatus);
            label.setText("Computer Wins");
        }
    };

    private void computerTurn() {
        Button btnChallenge = (Button)findViewById(R.id.challenge);
        btnChallenge.setEnabled(false);
        TextView ghostText = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        String ghostWord = ghostText.getText().toString();
        if(ghostText.getText().toString().length()>4&&dictionary.isWord(ghostWord)) {
            label.setText("Computer Challenge For Whole Word");
            timerHandler.postDelayed(timerRunnable, 3000);
            return;
        }
        // Do computer turn stuff then make it the user's turn again
        String longerWord = dictionary.getGoodWordStartingWith(ghostWord);
        if(longerWord==null||longerWord.length()<ghostWord.length()){
            //Challenge User and declare winner
            label.setText("Computer Challenge for no Longer Word");
            timerHandler.postDelayed(timerRunnable, 3000);
            return;
        }else{
            String letter = longerWord.substring(ghostWord.length(),ghostWord.length()+1);
            ghostWord+=letter;
            ghostText.setText(ghostWord);
            if(addToast!=null) {
                addToast.cancel();
            }
            addToast = Toast.makeText(this,"Computer Adds "+letter,Toast.LENGTH_SHORT);
            addToast.show();
        }
        userTurn = true;
        label.setText(USER_TURN);
        btnChallenge.setEnabled(true);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        TextView ghostText = (TextView) findViewById(R.id.ghostText);
        Button btnChallenge = (Button)findViewById(R.id.challenge);
        String ghostWord;
        char c = (char) event.getUnicodeChar();
       if( keyCode>=KeyEvent.KEYCODE_A && keyCode<=KeyEvent.KEYCODE_Z && btnChallenge.isEnabled() ){
           ghostWord = ghostText.getText().toString();
           ghostWord += c;
           ghostText.setText(ghostWord);
           TextView label = (TextView) findViewById(R.id.gameStatus);
           label.setText(COMPUTER_TURN);
//           if(dictionary.isWord(ghostWord)){
//               label.setText("WORD FOUND");
//               return true;
//           }
            computerTurn();
           return true;
       }
        return super.onKeyUp(keyCode, event);
    }

    public void onChallenge(View view) {
        TextView ghostText =(TextView)findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        Button btnChallenge = (Button)findViewById(R.id.challenge);
        String ghostWord = ghostText.getText().toString();
        if(ghostWord.length()>=4&&dictionary.isWord(ghostWord)){
            label.setText("User Wins");
        }else if(dictionary.getAnyWordStartingWith(ghostWord)!=null
                    &&dictionary.getAnyWordStartingWith(ghostWord).length()>ghostWord.length()){
            label.setText("Computer Wins : " + dictionary.getGoodWordStartingWith(ghostWord) );

        }else{
            label.setText("User Wins");
        }
        btnChallenge.setEnabled(false);
    }

    public void restartGame(View view) {
        onStart(view);
    }
}