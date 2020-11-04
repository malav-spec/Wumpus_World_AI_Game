import javax.swing.*;
import java.awt.Point;
import java.util.*;

public class Node {
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
}
