import java.io.*;
import java.util.*;

public class pizzaDelivery {
    static String mapDirt(int d) {
        switch(d){
          /*
            case 1: return "U";
            case 2: return "R";
            case 3: return "D";
            case 4: return "L";
          */
          case 1: return "↑";
          case 2: return "→";
          case 3: return "↓";
          case 4: return "←";
        }
        return "";
    }
    public static void main(String[] args){
      maze board = new maze();
      agent ag = new agent(board);
      ag.tdQlearning();
      System.out.println("No pizza, No ingredients:");
      printBoard(board, ag, 0);
      System.out.println("No pizza, Yes ingredients:");
      printBoard(board, ag, 1);
      System.out.println("Yes pizza, No ingredients:");
      printBoard(board, ag, 2);
      System.out.println("Yes pizza, Yes ingredients:");
      printBoard(board, ag, 3);

      printBoardII(board, ag, 3);
    }
    public static void printBoard(maze board, agent robot, int t) {
      for(int i = 0; i < 9; i++) {
        for(int j = 0; j < 15; j++) {
          if(board.isWall(i, j)) {
            System.out.print("W" + '\t');
          } else {
            System.out.print(mapDirt(board.states[i][j].policyU[t]) + '\t');
          }
        }
        System.out.println();
      }
      System.out.println();
    }
    public static void printBoardII(maze board, agent robot, int t) {
      for(int i = 0; i < 9; i++) {
        for(int j = 0; j < 15; j++) {
          if(board.isWall(i, j)) {
            System.out.print("W" + "\t");
          } else {
            System.out.print(" " + '\t');
          }
        }
        System.out.println();
      }
      System.out.println();
    }
}
