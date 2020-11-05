import java.util.Comparator;

public class Cost implements Comparator<Node[][]> {
    @Override
    public int compare(Node[][] board1, Node[][] board2) {
        AlphaBeta ab = new AlphaBeta();
        int v1 = board1[0][0].val;
        int v2 = board2[0][0].val;

        return v2-v1;
    }
}
