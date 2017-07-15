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

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;
    int steps ;
    PuzzleBoard previousBoard;



    PuzzleBoard(Bitmap bitmap, int parentWidth) {

        int parentHeight = (int) Math.floor(1.25 * parentWidth );
        Bitmap newbitmap = Bitmap.createScaledBitmap(bitmap,parentWidth,(parentHeight),false);

       // tiles = new ArrayList<>();
        steps = 0;

        //tiles.add( new PuzzleTile( Bitmap.createBitmap(bitmap,0,0,parentWidth,parentWidth) , 0  ) );
        /*
        */
        int tilexSize = parentWidth / NUM_TILES ;
        int tileySize = parentWidth / NUM_TILES ;

        tiles = new ArrayList<>();


        for(int i=0 ; i<3 ; i ++){
            for(int j=0; j<3; j++){
                Bitmap b = Bitmap.createBitmap(newbitmap,j*tilexSize,i*tileySize,tilexSize,tileySize);
                PuzzleTile tile = new PuzzleTile(b,i*NUM_TILES+j);
                tiles.add(tile);
            }
        }
        tiles.remove(NUM_TILES*NUM_TILES-1);
        tiles.add(null);

    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
    }

    PuzzleBoard(PuzzleBoard otherBoard, int steps) {
        previousBoard = otherBoard;
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
        this.steps = steps + 1;
    }

    void setPreviousBoard(PuzzleBoard previousBoard){
        this.previousBoard = previousBoard ;
    }


    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }

         }




    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {

        ArrayList<PuzzleBoard> neighboursMoves = new ArrayList<>();



        int findEmptyTilePos = 0 ;

        for(int i=0; i<tiles.size(); i++){
            if(tiles.get(i)==null)
                findEmptyTilePos = i;
        }

        int pos_emptyX = findEmptyTilePos%NUM_TILES , pos_emptyY = findEmptyTilePos / NUM_TILES ;


        for(int i=0; i<NEIGHBOUR_COORDS.length ; i++){
                int neigh_x = pos_emptyX+NEIGHBOUR_COORDS[i][0] , neigh_y = pos_emptyY+NEIGHBOUR_COORDS[i][1];
                if(inBoundary(neigh_x,neigh_y)){

                    PuzzleBoard temp = new PuzzleBoard(this,steps);
                    temp.swapTiles(findEmptyTilePos,neigh_y*NUM_TILES+neigh_x);
                    neighboursMoves.add(temp);

            }
        }
        return neighboursMoves;
    }

    boolean inBoundary(int i,int j){
        if(i>=0 && i<NUM_TILES && j>=0 && j<NUM_TILES)
            return true;
        return false;
    }


    public int priority() {
        int man_distance =0;
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if(tile==null)continue;
            int number =  tile.getNumber();
            man_distance += Math.abs( (i % NUM_TILES) - (number%NUM_TILES)  )
                    +  Math.abs( i / NUM_TILES  - number/NUM_TILES  ) ;
        }

        return man_distance + steps;
    }

    public boolean isStart() {
        for (int i = 0; i < NUM_TILES * NUM_TILES-1; i++) {
            PuzzleTile tile = tiles.get(i);

            if(tile==null || tile.getNumber()!=i)
                return false;
        }
        return  true;
    }
}