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

import android.util.Log;

import java.util.HashMap;
import java.util.Random;

public class TrieNode {
    private static final String LOG_TAG = "TrieNode";
    private HashMap<String, TrieNode> children;
    private boolean isWord;
    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
     //   Log.d(LOG_TAG+" : ADD :",s);
        TrieNode t;
        if(  children.containsKey( s.substring(0,1) ) == false  ){
            t = new TrieNode();
            children.put(s.substring(0,1),t);
        }
        t = children.get(s.substring(0,1));
        if(s.length()==1){
            isWord = true ;
        }else {
            t.add(s.substring(1));
        }
    }

    public boolean isWord(String s) {
        Log.d(LOG_TAG,s);
        if( children.containsKey( s.substring(0,1) ) ){
            TrieNode t = children.get(s.substring(0,1));
            if(s.length()==1){
                return isWord;
            }
            return t.isWord(s.substring(1));
        }
        return false;
    }

    public String getAnyWordStartingWith(String s) {
        Log.d(LOG_TAG+" : getAny :",s);
        if(s.length()==0){
            if(children.size()!=0){
                Log.d(LOG_TAG,children.keySet().iterator().next());
                return children.keySet().iterator().next();
            }
            return null;
        }
        if( children.containsKey( s.substring(0,1) ) ){
            TrieNode t = children.get(s.substring(0,1));
            if(s.length()==1){
                if(children.size()==0) {
                    return s;
                }else {
                    String temp = t.getAnyWordStartingWith(s.substring(1));
                    if(temp!=null)
                        return s + temp ;
                    return s;
                }
            }
            String temp = t.getAnyWordStartingWith(s.substring(1));
            if(temp!=null)
                return s.substring(0,1) + temp ;
            return s.substring(0,1);
        }
        return null;
    }

    public String getGoodWordStartingWith(String s) {
        Log.d(LOG_TAG+" : getGood :",s);
        if(s.length()==0){
            if(children.size()!=0){
                Log.d(LOG_TAG,children.keySet().iterator().next());
                Object[] solSet=  children.keySet().toArray();
                Random r =new Random( );
                int index = r.nextInt(solSet.length);
                Log.d(LOG_TAG+" : getGood :",s + "  " + solSet.length);
                if(children.get(solSet[index]).getAnyWordStartingWith("")==null)
                    return  (String) solSet[index];
                return  solSet[index] + children.get(solSet[index]).getGoodWordStartingWith("") ;

            }
            return null;
        }
        if( children.containsKey( s.substring(0,1) ) ){
            TrieNode t = children.get(s.substring(0,1));
            if(s.length()==1){
                if(children.size()==0) {
                    return s;
                }else{
                    String temp = t.getGoodWordStartingWith(s.substring(1));
                    if(temp!=null)
                        return s + temp ;
                    return s;
                }
            }
            String temp = t.getGoodWordStartingWith(s.substring(1));
            if(temp!=null)
                return s.substring(0,1) + temp ;
            return s.substring(0,1);
        }
        return null;
    }
}