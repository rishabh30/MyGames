package hell.mygames.wordladder;

import java.util.ArrayList;

public class Node {

    String word;
    ArrayList<Node> neighbours;
    int index;

    public Node(String word, int index) {
        this.word = word ;
        this.index = index ;
    }

    public int getIndex() {
        return index;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setNeighbours(ArrayList<Node> neighbours) {
        this.neighbours = neighbours;
    }


    public void addNeighbour(Node neighbour) {
        neighbours.add(neighbour);
    }

    public String getWord() {
        return word;
    }

    public ArrayList<Node> getNeighbours() {
        return neighbours;
    }
}
