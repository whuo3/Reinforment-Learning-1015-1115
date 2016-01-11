import java.io.*;
import java.util.*;

public class MDP {
    static String mapDirt(int d) {
        switch(d){
            case 1: return "↑";
            case 2: return "→";
            case 3: return "↓";
            case 4: return "←";
        }
        return "";
    }
    public static void main(String[] args){
        //robot.cur = board.start;
        //robot.tdQlearning(0.0001);

        // maze board = new maze();
        // agent robot = new agent(board);
        // robot.numIt = 30;
        // robot.valueIterateWithTerminal(0.0001);
        // for(int i = 0; i < 6; i++) {
        //   for(int j = 0; j < 6; j++) {
        //     System.out.print(i + " " + j + "\t");
        //   }
        // }
        // System.out.println();
        // for(int k = 0; k < board.states[0][0].hist_Val.size(); k++) {
        //   for(int i = 0; i < 6; i++) {
        //     for(int j = 0; j < 6; j++) {
        //       System.out.print(board.states[i][j].hist_Val.get(k) + "\t");
        //     }
        //   }
        //   System.out.println();
        // }

        /*******************Print Value Iteration With terminal*******************/
        // maze board = new maze();
        // agent robot = new agent(board);
        // robot.valueIterateWithTerminal(0.0001);
        //printBoard(board, robot, true);

        // /*******************Print Value Iteration With non-terminal*******************/
        // board = new maze();
        // robot = new agent(board);
        // robot.valueIterateWithoutTerminal(0.0001);
        // printBoard(board, robot, false);
        //
        // /*******************Print Policy Iteration With terminal*******************/
        // board = new maze();
        // robot = new agent(board);
        // robot.policyIterationWithTerminal();
        // printBoard(board, robot, true);
        //
        // /*******************Print Policy Iteration With non-terminal*******************/
        // board = new maze();
        // robot = new agent(board);
        // robot.policyIterationWithNonTerminal();
        // printBoard(board, robot, false);

        /*******************TD Q-learning*******************/
        maze boardII = new maze();
        agent robotII = new agent(boardII);
        robotII.tdQlearning();
        //printBoard(boardII, robotII, true);

        for(int i = 0; i < 6; i++) {
          for(int j = 0; j < 6; j++) {
            System.out.print(i + " " + j + "\t");
          }
        }
        System.out.println();
        for(int k = 0; k < boardII.states[0][0].hist_Q.size(); k++) {
          double rmse = 0.0;
          for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 6; j++) {
              System.out.print(boardII.states[i][j].hist_Q.get(k) + "\t");
              //List<Double> l = board.states[i][j].hist_Val;
              //rmse += (boardII.states[i][j].hist_Q.get(k) - l.get(l.size() - 1)) * (boardII.states[i][j].hist_Q.get(k) - l.get(l.size() - 1));
              //System.out.print();
            }
          }
          System.out.println();
          //rmse /= 36;
          //rmse = Math.sqrt(rmse);
          //System.out.println(rmse);
        }
    }
    public static void printBoard(maze board, agent robot, boolean withTerminal) {
      for(int i = 0; i < 6; i++){
          for(int j = 0; j < 6; j++){
              if(board.feature[i][j] == 'W')
                  System.out.print("W\t\t");
              else
                  System.out.printf("%f\t",board.states[i][j].expUtil);
          }
          System.out.println();
      }
      System.out.println();
      for(int i = 0; i < 6; i++){
          for(int j = 0; j < 6; j++){
              if(board.isWall(i, j)) {
                System.out.print("W\t");
              } else if(withTerminal && board.isTerminal(i, j)) {
                System.out.printf("%.0f\t", board.states[i][j].expUtil);
              } else {
                System.out.printf("%s\t", mapDirt(board.states[i][j].policyU));
              }
          }
          System.out.println();
      }
      System.out.println();
    }
}
