import javax.swing.*;
import java.awt.Point;
import java.util.*;

public class Node {
    public boolean moved;
    Point coordinates = new Point(0,0);
    boolean isTerminal;
    int heuristic;
    boolean isPit;
    boolean wumpus;
    boolean mage;
    boolean hero;
    boolean isMaximizing;
    boolean isAgent;
    ArrayList<Node> nextValidMoves;
    boolean isBattle;
    boolean win;
    boolean loose;
    boolean samePiece;
    int val;
    Node[][] parent;
}
