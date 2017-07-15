package hell.mygames;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.widget.GridLayoutManager;

public class GameMenu extends AppCompatActivity {
    private static final String LOG_TAG = "GameMenu";
    RecyclerView mRecyclerView;
    CustomAdapter mAdapter;
    String [] gameList={"MathGame","MemoryGame","Anagram","Scarnes Dice","Ghost","Puzzle8"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout)).setTitle("GAMES MENU");

        mRecyclerView = (RecyclerView)findViewById(R.id.gameMenu);
        mAdapter = new CustomAdapter(gameList, new CustomAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(TextView v, CustomAdapter.ViewHolder vh) {
                String gameSelected = String.valueOf(v.getText());
                Log.d(LOG_TAG,""+gameSelected);
                Toast.makeText(GameMenu.this, gameSelected, Toast.LENGTH_SHORT).show();
                Class con = null;
                String midPackage = "";
                if(gameSelected.equals( gameList[0]) ){
                    midPackage = "mathGame.";
                }else if (gameSelected.equals(gameList[1])){
                    midPackage = "memoryGame.";
                }else if (gameSelected.equals(gameList[2])){
                    midPackage = "anagrams.";
                    gameSelected = "AnagramsActivity";
                }else if (gameSelected.equals(gameList[3])){
                    midPackage = "scarnedice.";
                    gameSelected = "DiceActivity";
                }else if (gameSelected.equals(gameList[4])){
                    midPackage = "ghost.";
                    gameSelected = "GhostActivity";
                }else if (gameSelected.equals(gameList[5])){
                    midPackage = "puzzle8.";
                    gameSelected = "PuzzleActivity";
                }
                try {
                    con= Class.forName("hell.mygames."+midPackage+gameSelected);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP && gameSelected.equals( gameList[0]) ) {
                    Bundle bundle = ActivityOptions
                            .makeSceneTransitionAnimation(
                                    GameMenu.this, v, v.getTransitionName()
                            ).toBundle();
                    Intent intent = new Intent(GameMenu.this,con);
                    startActivity(intent,bundle);
                }else{
                    Intent intent = new Intent(GameMenu.this,con);
                    startActivity(intent);
                }


            }
        });
       // mRecyclerView.setLayoutManager( new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        mRecyclerView.setLayoutManager( new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(mAdapter);
    }


}
