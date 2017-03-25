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

package hell.mygames.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    ArrayList<String> wordList;
    HashSet<String> wordSet;
    HashMap< String,ArrayList<String> > lettersToWord;
    HashMap< Integer, ArrayList<String> > sizeToWords;
    public static int wordLength=DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWords = new HashMap<>();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            if(lettersToWord.containsKey(sortLetters(word))==false){
                lettersToWord.put(sortLetters(word),new ArrayList<String>());
            }
            lettersToWord.get(sortLetters(word)).add(word);
            wordSet.add(word);

            if (sizeToWords.containsKey(word.length())==false){
                sizeToWords.put(word.length(),new ArrayList<String>());
            }
            sizeToWords.get(word.length()).add(word);
        }
    }

    public String sortLetters(String word) {

        char wchar[] = word.toCharArray();
        Arrays.sort(wchar);
        String sorted_word = new String(wchar);
        return sorted_word;
    }

    public boolean isGoodWord(String word, String base) {
        //Log.d("isGoodWord : ", word+":::"+base);
        if(wordSet.contains(word)){
            if(word.contains(base)==false) {
                return true;
            }
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i< wordList.size(); i++){
            if(isGoodWord(wordList.get(i),targetWord)){
                result.add(wordList.get(i));
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        for(char i='a'; i<='z'; i++){
            String bigWord = i + word ;
            bigWord = sortLetters(bigWord);
            if(lettersToWord.containsKey(bigWord)) {
                ArrayList<String> addResult = lettersToWord.get(bigWord);
                for (int j=0; j<addResult.size(); j++){
                    if(isGoodWord(addResult.get(j),word)){
                        result.add(addResult.get(j));
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        Log.d("PICK", Integer.toString(wordLength));
        ArrayList<String> stringArrayList = sizeToWords.get(wordLength);
        int index = random.nextInt(stringArrayList.size());
        int i=index+1;
        Log.d("Two Size ",Integer.toString(sizeToWords.get(wordLength).size())+"::"+stringArrayList.size());

        while(i>1){
            Log.d("String Word",stringArrayList.get(i));
            i = i % stringArrayList.size();

            if(getAnagramsWithOneMoreLetter(stringArrayList.get(i)).size()>=MIN_NUM_ANAGRAMS){
                break;
            }else {
                stringArrayList.remove(i);
            }
        }
        Log.d("Two Size ",Integer.toString(sizeToWords.get(wordLength).size())+"::"+stringArrayList.size());
        if(wordLength<=MAX_WORD_LENGTH)
            wordLength++;
        return stringArrayList.get(i);
    }
}