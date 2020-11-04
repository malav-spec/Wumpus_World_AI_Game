import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;


public class GUI extends JFrame implements ActionListener {

    int rows, columns;
    Node[][] board;
    int move = 0;
    Node clicked;
    Node released;
    JButton[][] buttons;
    Board obj = new Board();
    Toast toast = new Toast();
    ArrayList<Node> nextMoves = new ArrayList<>();


    public GUI(){

    }

    public GUI(int r, int c, Node[][] b){
        this.rows = r;
        this.columns = c;
        this.board = b;
        obj.row = r;
        obj.column = c;
    }


    public void drawBoard(){

        System.out.println("In drawBoard\n");

         buttons = new JButton[rows][columns];
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(rows, columns));

        for(int i =0; i < rows; i++){
            for (int j = 0; j < columns; j++) {

                if(i == 0){

                    if (board[i][j].wumpus) {
                        buttons[i][j] = new JButton("");
                        try {
                            Image img = ImageIO.read(getClass().getResource("Agent_Wumpus.jpg"));
                            buttons[i][j].setIcon(new ImageIcon(img));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //  buttons[i][j].setBackground(Color.BLACK);
                        buttons[i][j].setName("AgentWumpus");
                    } else if (board[i][j].hero) {
                        buttons[i][j] = new JButton("");
                        try {
                            Image img = ImageIO.read(getClass().getResource("Agent_Hero.png"));
                            buttons[i][j].setIcon(new ImageIcon(img));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // buttons[i][j].setBackground(Color.BLACK);
                        buttons[i][j].setName("AgentHero");
                    } else if (board[i][j].mage) {
                        buttons[i][j] = new JButton("");
                        try {
                            Image img = ImageIO.read(getClass().getResource("Agent_Mage.jpg"));
                            buttons[i][j].setIcon(new ImageIcon(img));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // buttons[i][j].setBackground(Color.BLACK);
                        buttons[i][j].setName("AgentMage");

                    }
                }

                else if (i == rows - 1){
                    if (board[i][j].wumpus) {
                        buttons[i][j] = new JButton("");
                        try {
                            Image img = ImageIO.read(getClass().getResource("Wumpus.jpg"));
                            buttons[i][j].setIcon(new ImageIcon(img));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //  buttons[i][j].setBackground(Color.BLACK);
                        buttons[i][j].setName("Wumpus");
                    } else if (board[i][j].hero) {
                        buttons[i][j] = new JButton("");
                        try {
                            Image img = ImageIO.read(getClass().getResource("Hero.jpg"));
                            buttons[i][j].setIcon(new ImageIcon(img));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // buttons[i][j].setBackground(Color.BLACK);
                        buttons[i][j].setName("Hero");
                    } else if (board[i][j].mage) {
                        buttons[i][j] = new JButton("");
                        try {
                            Image img = ImageIO.read(getClass().getResource("Mage.jpg"));
                            buttons[i][j].setIcon(new ImageIcon(img));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // buttons[i][j].setBackground(Color.BLACK);
                        buttons[i][j].setName("Mage");
                    }

                }

               else if (board[i][j].isPit) {
                    buttons[i][j] = new JButton("");
                    try {
                        Image img = ImageIO.read(getClass().getResource("Pit.png"));
                        buttons[i][j].setIcon(new ImageIcon(img));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // buttons[i][j].setOpaque(true);
                    //buttons[i][j].setBackground(Color.DARK_GRAY);
                    buttons[i][j].setName("Pit");
               }

               else {
                    buttons[i][j] = new JButton("");
                    //buttons[i][j].setOpaque(true);
                }

                buttons[i][j].setActionCommand(i + " " + j);
                buttons[i][j].addActionListener(this::actionPerformed);
                pane.add(buttons[i][j]);
            }
        }
        System.out.println("Finished\n");
    }


    //    @Override
    public void actionPerformed(ActionEvent e)
    {

        JButton clickedButton = (JButton)e.getSource();
        String points;
        int i;
        String temp = "";
        String x="";
        String y;


        if(move == 0){

            new Thread(new Runnable() {//A new thread to display if it is the users turn. It is async.
                public void run()
                {
                    toast.s = "Your turn";
                    toast.ToastMessage(toast.s,800,0);
                    toast.display();
                }
            }).start();

            points = clickedButton.getActionCommand();
            for(i=0;i<points.length();i++){
                char chr = points.charAt(i);
                if(chr == ' '){
                    x = temp;
                    temp = "";
                }else{
                    temp = temp + Character.toString(chr);
                }
            }
            y = temp;
            int cx = Integer.parseInt(x);
            int cy = Integer.parseInt(y);
            clicked = obj.getNode(cx,cy, this.board);
            if(isEmpty(clicked) || clicked.isAgent){
                move = 0;
                return;
            }
            if(clicked.isPit){
                move = 0;
                return;
            }
            markSteps(clicked);
            if(nextMoves.size() == 0){
                move = 0;
                return;
            }
            move = 1;
        }
        else{

            points = clickedButton.getActionCommand();
            for(i=0;i<points.length();i++){
                char chr = points.charAt(i);
                if(chr == ' '){
                    x = temp;
                    temp = "";
                }else{
                    temp = temp + Character.toString(chr);
                }
            }
            y = temp;
            int cx = Integer.parseInt(x);
            int cy = Integer.parseInt(y);
            released = obj.getNode(cx,cy, this.board);

            if(released == clicked){
                removeSteps();
                move = 0;
                return;
            }

            if(released.isPit){
                move = 0;
                buttons[clicked.coordinates.x][clicked.coordinates.y].setIcon(null);
                clicked.isMaximizing = false;
                clicked.hero = false;
                clicked.wumpus=false;
                clicked.mage = false;
                removeSteps();
                return;
            }

            if(!nextMoves.contains(released)){
                move = 1;
                return;
            }

            //battle();
            removeSteps();

            buttons[clicked.coordinates.x][clicked.coordinates.y].setIcon(null);
            clicked.isMaximizing = false;
            move = 0;
            updateBoard();
        }

    }

    public void updateBoard(){
        int i,j;
        for(i=0;i<rows;i++){
            for(j=0;j<columns;j++){
                if(i == released.coordinates.x && j == released.coordinates.y){

                    board[i][j].isMaximizing = true;

                    if(clicked.wumpus){

                        if(released.hero){//Wumpus battles Hero
                            move = 0;
                            buttons[clicked.coordinates.x][clicked.coordinates.y].setIcon(null);
                            board[clicked.coordinates.x][clicked.coordinates.y].isMaximizing = false;
                            board[i][j].isAgent = true;
                            board[i][j].isMaximizing = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].hero = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].wumpus=false;
                            board[clicked.coordinates.x][clicked.coordinates.y].mage = false;
                            return;
                        }
                        else if (released.mage){//Wumpus Battles Mage
                            move = 0;
                            buttons[clicked.coordinates.x][clicked.coordinates.y].setIcon(null);
                            board[clicked.coordinates.x][clicked.coordinates.y].isMaximizing = false;
                            board[i][j].isAgent = false;
                            board[i][j].wumpus = true;
                            board[i][j].mage = false;
                            board[i][j].isMaximizing = true;
                            board[clicked.coordinates.x][clicked.coordinates.y].hero = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].wumpus=false;
                            board[clicked.coordinates.x][clicked.coordinates.y].mage = false;

                            try {
                                Image img = ImageIO.read(getClass().getResource("Wumpus.jpg"));
                                buttons[i][j].setIcon(new ImageIcon(img));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            return;
                        }
                        else if(released.wumpus){
                            move = 0;
                            buttons[clicked.coordinates.x][clicked.coordinates.y].setIcon(null);
                            buttons[i][j].setIcon(null);
                            board[clicked.coordinates.x][clicked.coordinates.y].isMaximizing = false;
                            board[i][j].isAgent = false;
                            board[i][j].wumpus = false;
                            board[i][j].isMaximizing = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].hero = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].wumpus=false;
                            board[clicked.coordinates.x][clicked.coordinates.y].mage = false;
                            return;
                        }

                        board[i][j].wumpus = true;
                        try {
                            Image img = ImageIO.read(getClass().getResource("Wumpus.jpg"));
                            buttons[i][j].setIcon(new ImageIcon(img));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        board[clicked.coordinates.x][clicked.coordinates.y].wumpus = false;

                    }
                    else if(clicked.hero){

                        if(released.mage){

                            move = 0;
                            buttons[clicked.coordinates.x][clicked.coordinates.y].setIcon(null);
                            board[clicked.coordinates.x][clicked.coordinates.y].isMaximizing = false;
                            board[i][j].isAgent = true;
                            board[i][j].isMaximizing = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].hero = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].wumpus=false;
                            board[clicked.coordinates.x][clicked.coordinates.y].mage = false;
                            return;

                        }
                        else if(released.wumpus){
                            buttons[clicked.coordinates.x][clicked.coordinates.y].setIcon(null);
                            board[clicked.coordinates.x][clicked.coordinates.y].isMaximizing = false;
                            board[i][j].isAgent = false;
                            board[i][j].wumpus = false;
                            board[i][j].hero = true;
                            board[i][j].isMaximizing = true;
                            board[clicked.coordinates.x][clicked.coordinates.y].hero = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].wumpus=false;
                            board[clicked.coordinates.x][clicked.coordinates.y].mage = false;
                            try {
                                Image img = ImageIO.read(getClass().getResource("Hero.jpg"));
                                buttons[i][j].setIcon(new ImageIcon(img));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        else if(released.hero){
                            move = 0;
                            buttons[clicked.coordinates.x][clicked.coordinates.y].setIcon(null);
                            buttons[i][j].setIcon(null);
                            board[clicked.coordinates.x][clicked.coordinates.y].isMaximizing = false;
                            board[i][j].isAgent = false;
                            board[i][j].hero = false;
                            board[i][j].isMaximizing = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].hero = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].wumpus=false;
                            board[clicked.coordinates.x][clicked.coordinates.y].mage = false;
                            return;
                        }

                        board[i][j].hero = true;
                        try {
                            Image img = ImageIO.read(getClass().getResource("Hero.jpg"));
                            buttons[i][j].setIcon(new ImageIcon(img));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        board[clicked.coordinates.x][clicked.coordinates.y].hero = false;

                    }
                    else if(clicked.mage){

                        if(released.hero){
                            buttons[clicked.coordinates.x][clicked.coordinates.y].setIcon(null);
                            board[clicked.coordinates.x][clicked.coordinates.y].isMaximizing = false;
                            board[i][j].isAgent = false;
                            board[i][j].hero = false;
                            board[i][j].mage = true;
                            board[i][j].isMaximizing = true;
                            board[clicked.coordinates.x][clicked.coordinates.y].hero = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].wumpus=false;
                            board[clicked.coordinates.x][clicked.coordinates.y].mage = false;

                            try {
                                Image img = ImageIO.read(getClass().getResource("Mage.jpg"));
                                buttons[i][j].setIcon(new ImageIcon(img));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(released.wumpus){
                            buttons[clicked.coordinates.x][clicked.coordinates.y].setIcon(null);
                            board[clicked.coordinates.x][clicked.coordinates.y].isMaximizing = false;
                            board[i][j].isAgent = true;
                            board[i][j].isMaximizing = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].hero = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].wumpus=false;
                            board[clicked.coordinates.x][clicked.coordinates.y].mage = false;
                            return;
                        }
                        else if(released.mage){
                            move = 0;
                            buttons[clicked.coordinates.x][clicked.coordinates.y].setIcon(null);
                            buttons[i][j].setIcon(null);
                            board[clicked.coordinates.x][clicked.coordinates.y].isMaximizing = false;
                            board[i][j].isAgent = false;
                            board[i][j].mage = false;
                            board[i][j].isMaximizing = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].hero = false;
                            board[clicked.coordinates.x][clicked.coordinates.y].wumpus=false;
                            board[clicked.coordinates.x][clicked.coordinates.y].mage = false;
                            return;
                        }

                        board[i][j].mage = true;
                        try {
                            Image img = ImageIO.read(getClass().getResource("Mage.jpg"));
                            buttons[i][j].setIcon(new ImageIcon(img));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        board[clicked.coordinates.x][clicked.coordinates.y].mage = false;
                    }

                    //buttons[clicked.coordinates.x][clicked.coordinates.y].setBackground(Color.WHITE);
                    return;
                }
            }
        }
    }

    public boolean isEmpty(Node node){
        if((node.wumpus || node.hero || node.mage || node.isPit) && node.isMaximizing ){
            return false;
        }
        return true;
    }

    public void markSteps(Node node){
        int i,j;

        if(node.coordinates.x - 1 >=0){
            if(node.coordinates.y+1 < columns){//check URD
                Node node1 = obj.getNode(node.coordinates.x-1,node.coordinates.y+1,this.board);
                if(isEmpty(node1)){
                   nextMoves.add(node1);
                }
            }
            if(node.coordinates.y-1 >= 0){//check ULD
                Node node1 = obj.getNode(node.coordinates.x-1,node.coordinates.y-1,this.board);
                if(isEmpty(node1)){
                    nextMoves.add(node1);
                }
            }
            //check UP
            Node node1 = obj.getNode(node.coordinates.x-1,node.coordinates.y,this.board);
            if(isEmpty(node1)){
                nextMoves.add(node1);
            }
        }

        if(node.coordinates.x+1 < rows) {
            if (node.coordinates.y + 1 < columns) {//check URD
                Node node1 = obj.getNode(node.coordinates.x + 1, node.coordinates.y + 1, this.board);
                if(isEmpty(node1)){
                    nextMoves.add(node1);
                }
            }
            if (node.coordinates.y - 1 >= 0) {//check ULD
                Node node1 = obj.getNode(node.coordinates.x + 1, node.coordinates.y - 1, this.board);
                if(isEmpty(node1)){
                    nextMoves.add(node1);
                }
            }
            Node node1 = obj.getNode(node.coordinates.x + 1, node.coordinates.y, this.board);
            if(isEmpty(node1)){
                nextMoves.add(node1);
            }
        }

        if(node.coordinates.y+1 < columns){
            Node node1 = obj.getNode(node.coordinates.x, node.coordinates.y+1, this.board);
            if(isEmpty(node1)){
                nextMoves.add(node1);
            }
        }

        if(node.coordinates.y-1 >= 0){
            Node node1 = obj.getNode(node.coordinates.x, node.coordinates.y-1, this.board);
            if(isEmpty(node1)){
                nextMoves.add(node1);
            }
        }

        highlightSteps();
    }

    public void highlightSteps(){

        int i;
        for(i=0;i<nextMoves.size();i++){
            Node n = nextMoves.get(i);
            if(isEmpty(n) && !n.isAgent && !n.isMaximizing && !n.isPit){
                buttons[n.coordinates.x][n.coordinates.y].setBackground(Color.cyan);
            }
            else{
                buttons[n.coordinates.x][n.coordinates.y].setBackground(Color.red);
            }

        }
    }

    public void removeSteps(){

        int i;
        for(i=0;i<nextMoves.size();i++){
            Node n = nextMoves.get(i);
            buttons[n.coordinates.x][n.coordinates.y].setBackground(null);
        }
        nextMoves.clear();
    }

    public static void main(int r, int c, Node[][] Board){

        GUI gui = new GUI(r,c,Board);
        gui.drawBoard();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        gui.setSize(screenSize.width, screenSize.height);
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
    }
}
