import java.util.*;

public class AlphaBeta {

    int rows,columns,d;
    PriorityQueue<Node> moves = new PriorityQueue<>();

    ArrayList<Node> currBoard = new ArrayList<>();
    Board b_obj = new Board();

    public Node[][] alphaBeta(Node[][] board,Node node, int depth, int alpha, int beta, boolean isAgent){
      if(depth == d || b_obj.gameOver){
          return board;
      }
        ArrayList<Node[][]> nextBoards = new ArrayList<>();
        int i;
      Node[][] best = null;
      nextBoards = b_obj.getNextBoards(currBoard, board, isAgent);
        int x;
        for(x=0;x<nextBoards.size();x++){
         System.out.println();
         b_obj.printBoard(nextBoards.get(x));
        }

      if(isAgent){
          nextBoards = setValues(nextBoards,true);
          int val = Integer.MIN_VALUE;
          for(i=0;i<nextBoards.size();i++){
              Node[][] successor = nextBoards.get(i);
              Node[][] nextMoveBoard = alphaBeta(successor,null,depth+1,alpha,beta,false);
               val = Math.max(val,checkValue(nextMoveBoard));
//              if(val>max){
//                  max = val;
//                  best = nextMoveBoard;
//              }
              alpha = Math.max(alpha,val);
              if(alpha >= beta){

                  return nextMoveBoard[0][0].parent;
              }
          }
      }
      else{
          int val = Integer.MAX_VALUE;

          nextBoards = setValues(nextBoards,false);

          for(i=0;i<nextBoards.size();i++){

              Node[][] successor = nextBoards.get(i);
              Node[][] nextMoveBoard = alphaBeta(successor,null,depth+1,alpha,beta,true);
               val = Math.min(val,checkValue(nextMoveBoard));
//              if(val<min){
//                  min = val;
//                  best = nextMoveBoard;
//              }
              beta = Math.min(beta,val);
              if(beta <= alpha){
                  return nextMoveBoard;
              }
          }

      }

      return null;
    }

    public int checkValue(Node[][] board){
        int i,j;
        for(i=0;i<rows;i++){
            for(j=0;j<columns;j++){
                if(board[i][j].moved){
                    if(board[i][j].isBattle){
                        if(board[i][j].win && (board[i][j].isMaximizing || board[i][j].isAgent) ) {//Agent wins battle
                            return 100;
                        }
                        else if(board[i][j].loose && !board[i][j].samePiece){//Agent looses battle
                            return -100;
                        }
                        else{
                            return -100;
                        }
                    }
                    else{
                        return 0;
                        //Return a value for the node
                    }
                }
            }
        }
        return -1000;
    }

    public ArrayList<Node[][]> setValues(ArrayList<Node[][]> nextMoves, boolean isMaximizing){
        int i;
        for(i=0;i<nextMoves.size();i++){
            Node[][] board = nextMoves.get(0);
            nextMoves.remove(board);
            board[0][0].val = checkValue(board);
            nextMoves.add(board);
        }
        if(isMaximizing) {
            Collections.sort(nextMoves, new Cost());
        }
        else{
            Collections.sort(nextMoves,Collections.reverseOrder(new Cost()));
        }
        return nextMoves;
    }


    public int safeValue(Node node, Node[][] board){
        int i,j;
        ArrayList<Node> checkNext = new ArrayList<>();
        checkNext = b_obj.getNextMoves(node,board);
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
     System.out.print("Enter depth: ");
     ab.d = sc.nextInt();

     Board obj = new Board(ab.rows, ab.columns);
     obj.board = obj.createBoard(obj.board);
     obj.printBoard(obj.board);

     ab.b_obj.row = ab.rows;
     ab.b_obj.column = ab.columns;
     ab.b_obj.board = obj.board;
     ab.b_obj.pieces = ab.rows;

       ab.alphaBeta(obj.board,null,0,0,0,true);

    // GUI.main(ab.rows,ab.columns, obj.board);

   }
}

//        https://github.com/RodneyShag/Othello/blob/master/src/strategies/AlphaBetaStrategy.java