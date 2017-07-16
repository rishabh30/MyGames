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

package hell.mygames.wordladder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class PathDictionary {
    private static int MAX_WORD_LENGTH = 4;
    private static HashSet<String> words = new HashSet<>();
    HashMap<String,Node> graph;


    public PathDictionary(InputStream inputStream , int maxLength) throws IOException {
        if (inputStream == null) {
            return;
        }
        MAX_WORD_LENGTH = maxLength;
        graph = new HashMap<>();

        Log.i("Word ladder", "Loading dict");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        Log.i("Word ladder", "Loading dict");
        int i=0;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() != MAX_WORD_LENGTH) {
                continue;
            }

            Node temp = new Node(word,i);

            graph.put(word,temp);

            AddDiffOneStrings(temp,word);

            words.add(word);
            i++;
        }
        Log.d("GRAPH SIZE ",graph.size()+"");

    }

    private void AddDiffOneStrings(Node temp, String word) {

        ArrayList<Node> neighbours = new ArrayList<>() ;

        //adding neighbours

        for (int i = 0; i < word.length(); i++) {
            for (char j = 'a'; j <= 'z'; j++) {
                String string = word.substring(0, i) + j + word.substring(i + 1);
                if (isWord(string) && !string.equals(word)) {

                    // add on both sides
                    Node neighbour = graph.get(string);
                    if(neighbour!=null)
                        neighbour.addNeighbour(temp);
                    neighbours.add(neighbour);

                }
            }
        }

        temp.setNeighbours(neighbours);

    }

    public boolean isWord(String word) {
        return words.contains(word.toLowerCase());
    }

    private ArrayList<Node> neighbours(String word) {
        return graph.get(word).getNeighbours();
    }

    public String[] findPath(String start, String end) {

        Node startNode = graph.get(start);
        Node endNode = graph.get(end);

        if(startNode==null||endNode==null||start.length()!=end.length())
            return null;
        boolean[] visited = new boolean[graph.size()+1];
        Node[] parent = new Node[graph.size() + 1];

        boolean foundSolution = false;

        ArrayDeque<Node> queue = new ArrayDeque<>();

        queue.add(startNode);
        parent[startNode.getIndex()]=null;//no parent

        while(queue.isEmpty()==false&&foundSolution ==false){

            Node u = queue.peek();
            visited[u.getIndex()]=true;
            queue.pop();

            ArrayList<Node> neighbours = neighbours(u.getWord());

            for(int i=0; i<neighbours.size(); i++){

                Node v = neighbours.get(i);
                if(v==null)continue;
                if(visited[v.getIndex()]==true)
                    continue;

                parent[v.getIndex()]=u;

                if(v.getIndex()==endNode.getIndex()) {
                    foundSolution = true;
                    break;
                }

                queue.add(v);

            }

        }


        if(foundSolution==true){

            ArrayList<String> sol=new ArrayList<>();
            while(endNode!=null){
                sol.add(endNode.getWord());
                endNode = parent[endNode.getIndex()];

            }
            //sol.remove(0);
            Collections.reverse(sol);
            //sol.remove(0);
            Log.e("SOLUTION " ,sol.toString());
            String[] solution = new String[sol.size()];
            for(int i=0; i<sol.size(); i++){
                solution[i] = sol.get(i);
            }

            return solution;
        }

        return null;
    }
}
