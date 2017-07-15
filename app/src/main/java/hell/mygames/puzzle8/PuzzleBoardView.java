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

package hell.mygames.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
        puzzleBoard = null ;
    }

    public void initialize(Bitmap imageBitmap) {
        int width = getWidth();
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            Random random = new Random();
            int randSteps = random.nextInt(NUM_SHUFFLE_STEPS) + 1;

            for(int i=1; i<=randSteps; i++){
                ArrayList<PuzzleBoard> neighbours = puzzleBoard.neighbours();
                puzzleBoard = neighbours.get( random.nextInt(neighbours.size())  );
            }




            puzzleBoard.reset();
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve() {

        //Implement A* search

        PriorityQueue<PuzzleBoard> priorityQueue = new PriorityQueue<>(1, new Comparator<PuzzleBoard>() {
            @Override
            public int compare(PuzzleBoard o1, PuzzleBoard o2) {
                return o1.priority() - o2.priority() ;
            }
        });

        PuzzleBoard temp = new PuzzleBoard(puzzleBoard,-1);
        temp.setPreviousBoard(null);
        priorityQueue.add(temp);

        int k=0;
        while(true){

            PuzzleBoard p = priorityQueue.peek();
            if(p.isStart()){
                break;
            }
            priorityQueue.remove();
            k++;
            //Log.d(" "+k + " board ",priorityQueue.()+"");

            ArrayList<PuzzleBoard> neighbours = p.neighbours();
            for(int i=0; i<neighbours.size(); i++){
                if(neighbours.get(i)!=p.previousBoard)
                    priorityQueue.add(neighbours.get(i));
            }


        }
        ArrayList<PuzzleBoard> solution = new ArrayList<>();
        PuzzleBoard solutionBoard = priorityQueue.peek();
        while (solutionBoard!=null){
            solution.add(solutionBoard);
            solutionBoard = solutionBoard.previousBoard;
        }

        //Log.d("SOLVED",solution.size()+"");
        Collections.reverse(solution);
        animation = solution ;
        invalidate();



    }
}
