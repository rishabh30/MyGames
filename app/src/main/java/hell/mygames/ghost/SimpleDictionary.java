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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private ArrayList<String> oddWords;
    private ArrayList<String> evenWords;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        evenWords = new ArrayList<>();
        oddWords = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH) {
                String temp  = line.trim();
                if(temp.length()%2==0)
                    evenWords.add(temp);
                else
                    oddWords.add(temp);
                words.add(temp);
            }
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix.isEmpty()){
            Random r = new Random();
            int index = r.nextInt(words.size());
            return  words.get(index);
        }
        return binarySearch(prefix,words);
    }

    private String binarySearch(String prefix,ArrayList<String> tempWords ) {
        String dictionaryWord ;
        int low = 0 ;
        int high = words.size()-1;
        while(high>=low){
            int mid = (high+low)/2;
            dictionaryWord =words.get(mid);
            if(dictionaryWord.startsWith(prefix)){
                return dictionaryWord;
            }
            if(dictionaryWord.compareTo(prefix)<0) {
                low = mid + 1;
            }else {
                high = mid - 1;
            }
        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        if(prefix.length()%2==1)
            selected = binarySearch(prefix,oddWords);

        if(selected==null)
            selected = binarySearch(prefix,evenWords);

        return selected;
    }
}