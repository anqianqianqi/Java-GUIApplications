package GUI.SignInPage;

import java.util.*;

public class TicTacToe {
        static ArrayList alreadyToken = new ArrayList();
        static ArrayList tookenByPlayer1 = new ArrayList();
        static ArrayList tookenByPlayer2 = new ArrayList();
        static ArrayList legalInput = new ArrayList();
        static String pos = null;
        static String player1Pos;
        static String player2Pos;
        static char player1Symbol = 'X';
        static char player2Symbol = 'O';

        static void printGameBoard(char[][]gameBoard){
                for (char[] row:gameBoard){
                        for(char c:row){
                                System.out.print(c);
                        }
                        System.out.println();
                }
        }

        static void setUpLegalInput(){
                for (int i = 1; i < 10; i++){
                        String number = Integer.toString(i);
                        legalInput.add(number);
                }
        }

       static String Player1(Scanner input){
               System.out.println("Player1: Enter a position from 1-9");
               pos = input.nextLine();
               if (checkIfLegal(pos)){
               }else{
                       System.out.println("Input is not from 1-9. Try again");
                       Player1(input);
               }
               if (checkIfExist(pos)){
                       System.out.println("Position already token. Try again");
                       Player1(input);
               }else{
               tookenByPlayer1.add(pos);
               alreadyToken.add(pos);
               }
               return(pos);
       }
        static String Player2(Scanner input){
                System.out.println("Player2: Enter a position from 1-9");
                pos = input.nextLine();
                if (checkIfLegal(pos)){
                }else{
                        System.out.println("Input is not from 1-9. Try again");
                        Player2(input);
                }
                if (checkIfExist(pos)){
                        System.out.println("Position already token. Try again");
                        Player2(input);
                }else{
                alreadyToken.add(pos);
                tookenByPlayer2.add(pos);}
                return(pos);
        }

        static String CPUplayer(){
                Random rand = new Random();
                int cpuPos = rand.nextInt(9)+1;
                String pos = Integer.toString(cpuPos);
                if (checkIfExist(pos)){
                        //System.out.println("Position already token. Try again");
                        CPUplayer();
                }else{
                        alreadyToken.add(pos);
                        tookenByPlayer1.add(pos);}
                return(pos);
        }

       static boolean checkIfExist(String pos){
               boolean b = false;
                for (Object p : alreadyToken){
                        if(pos.equals(p)){
                                b = true;
                                break;
                        }
                }return b;
        }

        static boolean checkIfLegal(String pos){
                boolean b = false;
                for (Object p : legalInput){
                        if(pos.equals(p)){
                                b = true;
                                break;
                        }
                }return b;
        }

        static void placePiece(String pos, char[][] gameBoard, char symbol){
                switch(pos){
                        case "1":
                                gameBoard[0][0] = symbol;
                                break;
                        case "2":
                                gameBoard[0][2] = symbol;
                                break;
                        case "3":
                                gameBoard[0][4] = symbol;
                                break;
                        case "4":
                                gameBoard[2][0] = symbol;
                                break;
                        case "5":
                                gameBoard[2][2] = symbol;
                                break;
                        case "6":
                                gameBoard[2][4] = symbol;
                                break;
                        case "7":
                                gameBoard[4][0] = symbol;
                                break;
                        case "8":
                                gameBoard[4][2] = symbol;
                                break;
                        case "9":
                                gameBoard[4][4] = symbol;
                                break;
                }
        }

        static boolean chekIfWin(List playerList){
                List<List> totalwin = new ArrayList<List>();
               List topRow = Arrays.asList("1","2","3");
                List midRow = Arrays.asList("4","5","6");
                List botRow = Arrays.asList("7","8","9");
                List lefCol = Arrays.asList("1","4","7");
                List midCol = Arrays.asList("2","5","8");
                List rigCol = Arrays.asList("3","6","9");
                List crossLef = Arrays.asList("1","5","9");
                List crossRig = Arrays.asList("3","5","7");

                totalwin.add(topRow);
                totalwin.add(midRow);
                totalwin.add(botRow);
                totalwin.add(lefCol);
                totalwin.add(midCol);
                totalwin.add(rigCol);
                totalwin.add(crossLef);
                totalwin.add(crossRig);
                boolean b = false;
                for (List l:totalwin){
                        if (playerList.containsAll(l)){
                              b = true;
                        }
                }
                return b;
        }

        static void startGame(){
                char[][] gameBoard = {{' ' , '|',' ' ,'|',' '},{'-','+','-','+','-'},
                        {' ' , '|',' ' ,'|',' '},{'-','+','-','+','-'},{' ' , '|',' ' ,'|',' '}};
                printGameBoard(gameBoard);
                Scanner input = new Scanner(System.in);
                setUpLegalInput();
                //System.out.println(legalInput);
                //System.out.println(alreadyToken);
                int i = 0;
                while (true){
                      player1Pos = Player1(input);
                      placePiece(player1Pos,gameBoard,player1Symbol);
                      i++;
                      printGameBoard(gameBoard);
                      if (chekIfWin(tookenByPlayer1)){
                              System.out.println("Congrats Player1!");
                              printGameBoard(gameBoard);
                              break;
                      }
                      player2Pos = Player2(input);
                        placePiece(player2Pos,gameBoard,player2Symbol);
                        i++;
                        //placePiece(CPUplayer(),gameBoard,player2Symbol);
                      if (chekIfWin(tookenByPlayer2)){
                              System.out.println("Player2 win!");
                              printGameBoard(gameBoard);
                              break;
                      }
                      if (i == 8){
                                System.out.println("Tie");
                                printGameBoard(gameBoard);
                                break;
                        }
                      printGameBoard(gameBoard);
            }
        }

        public static void main(String[] args){
            startGame();
        }
}
