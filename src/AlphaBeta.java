import java.util.*;

public class AlphaBeta {

    int rows,columns;
    PriorityQueue<Node> moves = new PriorityQueue<>();
    ArrayList<Board> nextBoards = new ArrayList<>();


    public int alphaBeta(Node node, int depth, int aplpha, int beta, boolean isMaximizing){
      if(depth == 0 || node.isTerminal){
          return node.heuristic;
      }

      int value;

      if(isMaximizing){
          value = Integer.MIN_VALUE;

      }
      else{
          value = Integer.MAX_VALUE;
      }

      return 0;
    }

    public static void main(String args[]){
     Scanner sc = new Scanner(System.in);
     AlphaBeta ab = new AlphaBeta();

     double a = 5.0/3;

     System.out.println(Math.round(a));
     System.out.print("Enter number of rows: ");
     ab.rows = sc.nextInt();
     System.out.print("Enter number of columns: ");
     ab.columns = sc.nextInt();

     Board obj = new Board(ab.rows, ab.columns);
     obj.board = obj.createBoard(obj.board);
     obj.printBoard(obj.board);

     ArrayList<Node> list = new ArrayList<>();
     ArrayList<Node[][]> boards = new ArrayList<>();
     boards = obj.getNextBoards(list,obj.board);
     int x;
     for(x=0;x<boards.size();x++){
         System.out.println();
         obj.printBoard(boards.get(x));
     }

     GUI.main(ab.rows,ab.columns, obj.board);

   }
}
