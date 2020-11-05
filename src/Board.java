import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Board {
    int row, column;
    boolean gameOver;
    int pieces;
    int value;
    ArrayList<Node> PitLocation = new ArrayList<>();

    public Board(){
    }

    Node[][] board;
    Node[][] Originalboard;

    public Board(int r,int c){
        this.row = r;
        this.column = c;
        Originalboard = new Node[row][column];
        board = new Node[row][column];
        pieces = r;
    }

    public Node[][] createBoard(Node[][] board){
        int i,j,count = 0;
        for (i=0; i<row; i++){
            count  = 0;
            for(j=0;j<column;j++){
                Node node = new Node();
                node.coordinates.x = i;
                node.coordinates.y = j;
                node.isPit = false;
                board[i][j] = node;
            }
        }
        board = markPits(board);
        board = markPieces(board);

        this.board = board;
        return board;
    }

    public Node[][] markPits(Node[][] board){
        int i,j,count = 0;
        for(i=1;i<row-1;i++){
            count = 0;
            for(j=0;j<column;j++){

                double p = (column-1.0)/3.0;

                Random rnd = new Random();

                    while (count < Math.round(p)) {
                        int x = rnd.nextInt(column-1);
                        if(board[i][x].isPit){
                            continue;
                        }
                            board[i][x].isPit = true;
                            PitLocation.add(board[i][x]);
                            count++;
                            continue;

                    }
                    break;
            }
        }
        this.board = board;
        return board;
    }

    public Node[][] markPieces(Node[][] board){
        int i,j;
        for(j=0;j<column;j++){//Place Wumpus
          board[0][j].wumpus = true;
          board[0][j].isAgent = true;
          board[row-1][j].wumpus = true;
          board[row-1][j].isMaximizing = true;
          j+=2;
        }

        for (j=1;j<column;j++){//Place Hero
         board[0][j].hero = true;
         board[0][j].isAgent = true;
         board[row-1][j].hero = true;
         board[row-1][j].isMaximizing = true;
         j+=2;
        }

        for (j=2;j<column;j++){//Place mage
            board[0][j].mage = true;
            board[0][j].isAgent = true;
            board[row-1][j].mage = true;
            board[row-1][j].isMaximizing = true;
            j+=2;
        }

        this.board = board;
        return board;
    }

    public Node getNode(int cx, int cy, Node[][] b){

        int i,j;
        for(i=0;i<row;i++){
            for(j=0;j<column;j++){
                if(i == cx && j == cy){
                    return b[i][j];
                }
            }
        }
        return null;
    }

    public ArrayList<Node> getNextPlayerMoves(Node node, Node[][] b){
        ArrayList<Node> nextMove = new ArrayList<>();
        if(node.coordinates.x - 1 >=0){
            if(node.coordinates.y+1 < column){//check URD
                Node node1 = getNode(node.coordinates.x-1,node.coordinates.y+1,b);
                if(isEmpty(node1) && !node1.isMaximizing){
                    nextMove.add(node1);
                }
            }
            if(node.coordinates.y-1 >= 0){//check ULD
                Node node1 = getNode(node.coordinates.x-1,node.coordinates.y-1,b);
                if(isEmpty(node1) && !node1.isMaximizing){
                    nextMove.add(node1);
                }
            }
            //check UP
            Node node1 = getNode(node.coordinates.x-1,node.coordinates.y,b);
            if(isEmpty(node1) && !node1.isMaximizing){
                nextMove.add(node1);
            }
        }

        if(node.coordinates.x+1 < row) {
            if (node.coordinates.y + 1 < column) {//check DRD
                Node node1 = getNode(node.coordinates.x + 1, node.coordinates.y + 1, b);
                if(isEmpty(node1) && !node1.isMaximizing){
                    nextMove.add(node1);
                }
            }
            if (node.coordinates.y - 1 >= 0) {//check ULD
                Node node1 = getNode(node.coordinates.x + 1, node.coordinates.y - 1, b);
                if(isEmpty(node1) && !node1.isMaximizing){
                    nextMove.add(node1);
                }
            }
            Node node1 = getNode(node.coordinates.x + 1, node.coordinates.y, b);
            if(isEmpty(node1) && !node1.isMaximizing){
                nextMove.add(node1);
            }
        }

        if(node.coordinates.y+1 < column){
            Node node1 = getNode(node.coordinates.x, node.coordinates.y+1, b);
            if(isEmpty(node1) && !node1.isMaximizing){
                nextMove.add(node1);
            }
        }

        if(node.coordinates.y-1 >= 0){
            Node node1 = getNode(node.coordinates.x, node.coordinates.y-1, b);
            if(isEmpty(node1) && !node1.isMaximizing){
                nextMove.add(node1);
            }
        }

        return nextMove;

    }

    public ArrayList<Node> getNextMoves(Node node, Node[][] b){
        ArrayList<Node> nextMove = new ArrayList<>();
        if(node.coordinates.x - 1 >=0){
            if(node.coordinates.y+1 < column){//check URD
                Node node1 = getNode(node.coordinates.x-1,node.coordinates.y+1,b);
                if(isEmpty(node1) && !node1.isAgent){
                    nextMove.add(node1);
                }
            }
            if(node.coordinates.y-1 >= 0){//check ULD
                Node node1 = getNode(node.coordinates.x-1,node.coordinates.y-1,b);
                if(isEmpty(node1) && !node1.isAgent){
                    nextMove.add(node1);
                }
            }
            //check UP
            Node node1 = getNode(node.coordinates.x-1,node.coordinates.y,b);
            if(isEmpty(node1) && !node1.isAgent){
                nextMove.add(node1);
            }
        }

        if(node.coordinates.x+1 < row) {
            if (node.coordinates.y + 1 < column) {//check DRD
                Node node1 = getNode(node.coordinates.x + 1, node.coordinates.y + 1, b);
                if(isEmpty(node1) && !node1.isAgent){
                    nextMove.add(node1);
                }
            }
            if (node.coordinates.y - 1 >= 0) {//check ULD
                Node node1 = getNode(node.coordinates.x + 1, node.coordinates.y - 1, b);
                if(isEmpty(node1) && !node1.isAgent){
                    nextMove.add(node1);
                }
            }
            Node node1 = getNode(node.coordinates.x + 1, node.coordinates.y, b);
            if(isEmpty(node1) && !node1.isAgent){
                nextMove.add(node1);
            }
        }

        if(node.coordinates.y+1 < column){
            Node node1 = getNode(node.coordinates.x, node.coordinates.y+1, b);
            if(isEmpty(node1) && !node1.isAgent){
                nextMove.add(node1);
            }
        }

        if(node.coordinates.y-1 >= 0){
            Node node1 = getNode(node.coordinates.x, node.coordinates.y-1, b);
            if(isEmpty(node1) && !node1.isAgent){
                nextMove.add(node1);
            }
        }

        return nextMove;

    }

    public ArrayList<Node[][]> getNextBoards(ArrayList<Node> currentBoard, Node[][] b,boolean isAgent){

        ArrayList<Node[][]> allBoards = new ArrayList<>();
        ArrayList<Node> moves = new ArrayList<>();
        int i,j;
        for(i=0;i<row;i++){
            for(j=0;j<column;j++){//Get all possible moves of all pieces for current board
                Node temp = getNode(i,j,b);
                if(temp.isAgent && isAgent) {
                    moves = getNextMoves(temp, b);
                    b[i][j].nextValidMoves = moves;
                    currentBoard.add(b[i][j]);

                }
                else if(!isAgent && temp.isMaximizing){
                    moves = getNextPlayerMoves(temp,b);
                    b[i][j].nextValidMoves = moves;
                    currentBoard.add(b[i][j]);
                }
            }
        }

        for(i=0;i<currentBoard.size();i++) {
            for (j = 0; j < currentBoard.get(i).nextValidMoves.size(); j++) {

                Node[][] Board = new Node[row][column];

                Board = copyBoard(b,Board);

                if(isAgent) {
                    Board = movePiece(Board, Board[currentBoard.get(i).coordinates.x][currentBoard.get(i).coordinates.y], Board[currentBoard.get(i).nextValidMoves.get(j).coordinates.x][currentBoard.get(i).nextValidMoves.get(j).coordinates.y]);
                }
                else{
                    Board = movePlayerPiece(Board, Board[currentBoard.get(i).coordinates.x][currentBoard.get(i).coordinates.y], Board[currentBoard.get(i).nextValidMoves.get(j).coordinates.x][currentBoard.get(i).nextValidMoves.get(j).coordinates.y]);
                }
                Board[0][0].parent = b;
                allBoards.add(Board);

            }
            currentBoard.get(i).nextValidMoves.clear();
        }

        return allBoards;
    }

    public Node[][] copyBoard(Node[][] b, Node[][] nb){
        // nb = new Node[row][column];
        int i,j;
        for(i=0;i<row;i++){
            for(j=0;j<column;j++){
                Node temp = getNode(i,j,b);
                Node newNode = new Node();
                newNode = copyNode(temp,newNode);
                nb[i][j] = newNode ;
            }
        }

        return nb;
    }

    private Node copyNode(Node temp, Node newNode) {
         newNode.coordinates = temp.coordinates;
         newNode.isTerminal = temp.isTerminal;
         newNode.heuristic = temp.heuristic;
         newNode.isPit = temp.isPit;
         newNode.wumpus = temp.wumpus;
         newNode.mage = temp.mage;
         newNode.hero = temp.hero;
         newNode.isMaximizing = temp.isMaximizing;
         newNode.isAgent = temp.isAgent;
         newNode.nextValidMoves = temp.nextValidMoves;

         return newNode;
    }

    public Node[][] movePlayerPiece(Node[][] nb, Node prev,Node next){


        nb[next.coordinates.x][next.coordinates.y].moved = true;

        if(nb[prev.coordinates.x][prev.coordinates.y].wumpus){

            if(nb[next.coordinates.x][next.coordinates.y].isAgent) {

                if (nb[next.coordinates.x][next.coordinates.y].hero) {
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].loose = true;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].mage) {//Agent wins
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].wumpus = true;
                    nb[next.coordinates.x][next.coordinates.y].win = true;
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = true;
                    nb[next.coordinates.x][next.coordinates.y].mage = false;
                    nb[next.coordinates.x][next.coordinates.y].isAgent = false;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].wumpus){
                    nb[next.coordinates.x][next.coordinates.y].loose = true;//Agent and player both loose
                    nb[next.coordinates.x][next.coordinates.y].isAgent = false;
                    nb[next.coordinates.x][next.coordinates.y].wumpus = false;
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = false;
                    nb[next.coordinates.x][next.coordinates.y].samePiece = true;
                }
            }
            else{
                nb[next.coordinates.x][next.coordinates.y].wumpus = true;
                nb[next.coordinates.x][next.coordinates.y].isMaximizing = true;
            }
        }
        else if(nb[prev.coordinates.x][prev.coordinates.y].hero){

            if(nb[next.coordinates.x][next.coordinates.y].isAgent) {

                if (nb[next.coordinates.x][next.coordinates.y].wumpus) {
                    nb[next.coordinates.x][next.coordinates.y].win = true;//Agent wins
                    nb[next.coordinates.x][next.coordinates.y].hero = true;
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = true;
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].wumpus = false;
                    nb[next.coordinates.x][next.coordinates.y].isAgent = false;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].mage) {
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].loose = true;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].hero){
                    nb[next.coordinates.x][next.coordinates.y].loose = true;//Agent and player both loose
                    nb[next.coordinates.x][next.coordinates.y].isAgent = false;
                    nb[next.coordinates.x][next.coordinates.y].hero = false;
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = false;
                    nb[next.coordinates.x][next.coordinates.y].samePiece = true;
                }
            }

            else{
                nb[next.coordinates.x][next.coordinates.y].hero = true;
                nb[next.coordinates.x][next.coordinates.y].isMaximizing = true;
            }
        }
        else if(nb[prev.coordinates.x][prev.coordinates.y].mage){

            if(nb[next.coordinates.x][next.coordinates.y].isAgent) {

                if (nb[next.coordinates.x][next.coordinates.y].wumpus) {
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].loose = true;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].hero) {//Agent wins
                    nb[next.coordinates.x][next.coordinates.y].win = true;//Agent wins
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = true;
                    nb[next.coordinates.x][next.coordinates.y].mage = true;
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].wumpus = false;
                    nb[next.coordinates.x][next.coordinates.y].isAgent = false;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].mage){
                    nb[next.coordinates.x][next.coordinates.y].loose = true;//Agent and player both loose
                    nb[next.coordinates.x][next.coordinates.y].isAgent = false;
                    nb[next.coordinates.x][next.coordinates.y].mage = false;
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = false;
                    nb[next.coordinates.x][next.coordinates.y].samePiece = true;
                }
            }
            else{
                nb[next.coordinates.x][next.coordinates.y].mage = true;
                nb[next.coordinates.x][next.coordinates.y].isMaximizing = true;
            }
        }

        nb[prev.coordinates.x][prev.coordinates.y].mage = false;
        nb[prev.coordinates.x][prev.coordinates.y].hero = false;
        nb[prev.coordinates.x][prev.coordinates.y].wumpus = false;
        nb[prev.coordinates.x][prev.coordinates.y].isMaximizing = false;
        nb[prev.coordinates.x][prev.coordinates.y].heuristic = 0;

        return nb;
    }

    public Node[][] movePiece(Node[][] nb, Node prev,Node next){


        nb[next.coordinates.x][next.coordinates.y].moved = true;

        if(nb[prev.coordinates.x][prev.coordinates.y].wumpus){

            if(nb[next.coordinates.x][next.coordinates.y].isMaximizing) {

                if (nb[next.coordinates.x][next.coordinates.y].hero) {
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].loose = true;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].mage) {//Agent wins
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].wumpus = true;
                    nb[next.coordinates.x][next.coordinates.y].win = true;
                    nb[next.coordinates.x][next.coordinates.y].isAgent = true;
                    nb[next.coordinates.x][next.coordinates.y].mage = false;
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = false;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].wumpus){
                    nb[next.coordinates.x][next.coordinates.y].loose = true;//Agent and player both loose
                    nb[next.coordinates.x][next.coordinates.y].isAgent = false;
                    nb[next.coordinates.x][next.coordinates.y].wumpus = false;
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = false;
                    nb[next.coordinates.x][next.coordinates.y].samePiece = true;
                }
            }
            else{
                nb[next.coordinates.x][next.coordinates.y].wumpus = true;
                nb[next.coordinates.x][next.coordinates.y].isAgent = true;
            }
        }
        else if(nb[prev.coordinates.x][prev.coordinates.y].hero){

            if(nb[next.coordinates.x][next.coordinates.y].isMaximizing) {

                if (nb[next.coordinates.x][next.coordinates.y].wumpus) {
                    nb[next.coordinates.x][next.coordinates.y].win = true;//Agent wins
                    nb[next.coordinates.x][next.coordinates.y].hero = true;
                    nb[next.coordinates.x][next.coordinates.y].isAgent = true;
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].wumpus = false;
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = false;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].mage) {
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].loose = true;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].hero){
                    nb[next.coordinates.x][next.coordinates.y].loose = true;//Agent and player both loose
                    nb[next.coordinates.x][next.coordinates.y].isAgent = false;
                    nb[next.coordinates.x][next.coordinates.y].hero = false;
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = false;
                    nb[next.coordinates.x][next.coordinates.y].samePiece = true;
                }
            }

            else{
                nb[next.coordinates.x][next.coordinates.y].hero = true;
                nb[next.coordinates.x][next.coordinates.y].isAgent = true;
            }
        }
        else if(nb[prev.coordinates.x][prev.coordinates.y].mage){

            if(nb[next.coordinates.x][next.coordinates.y].isMaximizing) {

                if (nb[next.coordinates.x][next.coordinates.y].wumpus) {
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].loose = true;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].hero) {//Agent wins
                    nb[next.coordinates.x][next.coordinates.y].win = true;//Agent wins
                    nb[next.coordinates.x][next.coordinates.y].isAgent = true;
                    nb[next.coordinates.x][next.coordinates.y].mage = true;
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].wumpus = false;
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = false;
                }
                else if (nb[next.coordinates.x][next.coordinates.y].mage){
                    nb[next.coordinates.x][next.coordinates.y].loose = true;//Agent and player both loose
                    nb[next.coordinates.x][next.coordinates.y].isAgent = false;
                    nb[next.coordinates.x][next.coordinates.y].mage = false;
                    nb[next.coordinates.x][next.coordinates.y].isBattle = true;
                    nb[next.coordinates.x][next.coordinates.y].isMaximizing = false;
                    nb[next.coordinates.x][next.coordinates.y].samePiece = true;
                }
            }
            else{
                nb[next.coordinates.x][next.coordinates.y].mage = true;
                nb[next.coordinates.x][next.coordinates.y].isAgent = true;
            }
        }

        nb[prev.coordinates.x][prev.coordinates.y].mage = false;
        nb[prev.coordinates.x][prev.coordinates.y].hero = false;
        nb[prev.coordinates.x][prev.coordinates.y].wumpus = false;
        nb[prev.coordinates.x][prev.coordinates.y].isAgent = false;
        nb[prev.coordinates.x][prev.coordinates.y].heuristic = 0;

        return nb;
    }

    public boolean isEmpty(Node node){
        if(node.isPit){
            return false;
        }
        return true;
    }


    public void printBoard(Node[][] board){
        int i,j;
        for (i=0; i<row; i++){
            for(j=0;j<column;j++){

                if(board[i][j].wumpus){
                    System.out.print( "Wumpus ");
                }
                else if(board[i][j].hero){
                    System.out.print( "Hero ");
                }
                else if(board[i][j].mage){
                    System.out.print( "Mage ");
                }
                else if(board[i][j].isPit){
                    System.out.print( board[i][j].coordinates.x + "," + board[i][j].coordinates.y + "PIT ");
                }
               else{
                    System.out.print( board[i][j].coordinates.x + "," + board[i][j].coordinates.y + " ");
                }
            }
            System.out.println();
        }
    }
}
