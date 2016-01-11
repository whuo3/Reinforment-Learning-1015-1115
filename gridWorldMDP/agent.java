import java.util.*;

public class agent{
    state cur;
    state next;
    maze board;
    int numIt = 0;
    //Chances to next state
    double center;
    double left;
    double right;
    double gamma;

    //Optimistic reward estimates
    int     Ne;

    public agent(maze board){
        this.center = 0.8;
        this.left = 0.1;
        this.right = 0.1;

        this.cur = board.start;
        this.board = board;

        this.gamma = 0.99;
        //Bar for select action to try
        this.Ne = 60;
    }

    //Set action to optimal solution. Return max of V(s') given available actions.
    /**************************************** Value Iteration ****************************************/
    public double optimal(state curState){
        double centralUtil;
        double leftUtil;
        double rightUtil;
        double maxUtil = Double.NEGATIVE_INFINITY;

        for (int i = 1; i < 5; i++) {
            centralUtil = (this.center * curState.dir[i].expUtil);
            leftUtil = (this.left * curState.dir[i-1].expUtil);
            rightUtil = (this.right * curState.dir[i+1].expUtil);
            double tempUtil = centralUtil + leftUtil + rightUtil;
            if (tempUtil > maxUtil) {
                maxUtil = tempUtil;
            }
        }
        return maxUtil;
    }
    public void updatePolicys() {
      for(int i = 0; i < 6; i++) {
        for(int j = 0; j < 6; j++) {
          double t = Double.NEGATIVE_INFINITY;
          for(int k = 1; k < 5; k++) {
            if(t < board.states[i][j].dir[k].expUtil) {
              t = board.states[i][j].dir[k].expUtil;
              board.states[i][j].policyU = k;
            }
          }
        }
      }
    }
    public void valueIterateWithTerminal(double diff){
        int bar = 25; //number of all states that are not walls and terminal
        int k = 0;    //number of states that Converge
        int iterateT = 0;
        while(true) {
            k = 0;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    board.states[i][j].hist_Val.add(board.states[i][j].expUtil);
                    if(!board.isTerminal(i, j) && !board.isWall(i, j)) {
                      //BellMan Equation
                      board.states[i][j].expUtil = board.states[i][j].reward + this.gamma * this.optimal(board.states[i][j]);
                      if(Math.abs(board.states[i][j].expUtil - board.states[i][j].hist_Val.get(board.states[i][j].hist_Val.size() - 1)) < diff)
                          k++;
                    }
                }
            }
            if(k == bar)
                break;
            iterateT += 1;
        }
        updatePolicys();
        System.out.println();
        System.out.println("Value Iteration with terminal convergence takes " + iterateT + " iterations:");
        System.out.println();
    }
    public void valueIterateWithoutTerminal(double diff){
        int bar = 32; //number of all states that are not walls and terminal
        int k = 0;    //number of states that Converge
        int iterateT = 0;
        for(int i = 0; i < 6; i++) {
          for(int j = 0; j < 6; j++) {
            if(board.isTerminal(i, j)) {
              board.states[i][j].expUtil = 0.0;
            }
          }
        }
        while(true){
            k = 0;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    board.states[i][j].hist_Val.add(board.states[i][j].expUtil);
                    if(!board.isWall(i, j)) {
                      //BellMan Equation
                      board.states[i][j].expUtil = board.states[i][j].reward + this.gamma * this.optimal(board.states[i][j]);
                      if(Math.abs(board.states[i][j].expUtil - board.states[i][j].hist_Val.get(board.states[i][j].hist_Val.size() - 1)) < diff)
                          k++;
                    }
                }
            }
            if(k == bar)
                break;
            iterateT += 1;
        }
        updatePolicys();
        System.out.println();
        System.out.println("Value Iteration with non terminal convergence takes " + iterateT + " iterations:");
        System.out.println();
    }

    /**************************************** Policy Iteration ****************************************/
    public double util(state curState) {
      int policy = curState.policyU;
      double centralUtil = (this.center * curState.dir[policy].expUtil);
      double leftUtil = (this.left * curState.dir[policy - 1].expUtil);
      double rightUtil = (this.right * curState.dir[policy + 1].expUtil);
      double tempUtil = centralUtil + leftUtil + rightUtil;
      return tempUtil;
    }
    public boolean updatePolicy(state curState) {
      double t = Double.NEGATIVE_INFINITY;
      int old = curState.policyU;
      for(int k = 1; k < 5; k++) {
        if(t < curState.dir[k].expUtil) {
          t = curState.dir[k].expUtil;
          curState.policyU = k;
        }
      }
      return old != curState.policyU;
    }
    public void policyIterationWithTerminal() {
      int k = 0;    //number of states that Converge
      int iterateT = 0;
      int numNotChangeInconsecutive = 0;
      while(true){
          k = 0;
          boolean changed = false;
          for (int i = 0; i < 6; i++) {
              for (int j = 0; j < 6; j++) {
                  board.states[i][j].hist_Val.add(board.states[i][j].expUtil);
                  if(!board.isTerminal(i, j) && !board.isWall(i, j)) {
                    changed = updatePolicy(board.states[i][j]);
                    //BellMan Equation
                    board.states[i][j].expUtil = board.states[i][j].reward + this.gamma * util(board.states[i][j]);
                    if(changed) {
                        k++;
                        numNotChangeInconsecutive = 0;
                    }
                  }
              }
          }
          if(k == 0) {
            numNotChangeInconsecutive++;
          }
          if(numNotChangeInconsecutive == 5) {
            break;
          }
          iterateT += 1;
      }
      System.out.println();
      System.out.println("Policy Iteration with terminal convergence takes " + iterateT + " iterations:");
      System.out.println();
    }

    void policyIterationWithNonTerminal() {
      int k = 0;    //number of states that Converge
      int iterateT = 0;
      int numNotChangeInconsecutive = 0;
      for(int i = 0; i < 6; i++) {
        for(int j = 0; j < 6; j++) {
          if(board.isTerminal(i, j)) {
            board.states[i][j].expUtil = 0.0;
          }
        }
      }
      while(true){
          k = 0;
          boolean changed = false;
          for (int i = 0; i < 6; i++) {
              for (int j = 0; j < 6; j++) {
                  board.states[i][j].hist_Val.add(board.states[i][j].expUtil);
                  if(!board.isWall(i, j)) {
                    changed = updatePolicy(board.states[i][j]);
                    //BellMan Equation
                    board.states[i][j].expUtil = board.states[i][j].reward + this.gamma * this.util(board.states[i][j]);
                    if(changed) {
                      numNotChangeInconsecutive = 0;
                      k++;
                    }
                  }
              }
          }
          if(k == 0)
              numNotChangeInconsecutive++;
          if(numNotChangeInconsecutive == 200) {
            break;
          }
          iterateT += 1;
      }
      updatePolicys();
      System.out.println();
      System.out.println("Policy Iteration without terminal convergence takes " + iterateT + " iterations:");
      System.out.println();
    }


    /**************************************** Reinforcement learning: TD Q-Learning ****************************************/
    Random rn = new Random();
    int getAction(state curState){
        double maxQ = Double.NEGATIVE_INFINITY;
        double temp;
        int action = -1;
        //Select max Q(s, a)
        for (int i = 1; i < 5; i++) {
            if (curState.nAct[i-1] < Ne)
                temp = rn.nextInt(10); //Make the temp as large as possible so that it will try the current position
            else
                temp = curState.Q[i-1];

            if (temp > maxQ) {
                maxQ = temp;
                action = i;
            }
        }
        return action;
    }

    state takeAction(int dir) {
        int t = rn.nextInt(10);
        if (t ==  0) //With 0.1 probability, go to left
            return cur.dir[dir+1];
        else if (t ==  1) //With 0.1 probability, go to right
            return cur.dir[dir-1];
        else //With 0.8 probability, go to left
            return cur.dir[dir];
    }

    //TDQ learning main process
    void tdQlearning(){
        int act; //The preferred actio of s
        //learning rate
        double alpha;
        double temp;
        double maxQ;

        for(int l = 0; l < 5000; l++){
            cur = board.start;
            act = getAction(cur);
            while(true) {
              cur.visit++;
              cur.nAct[act-1] += 1;
              alpha = 60.0 / (59 + cur.nAct[act-1]);

              next = takeAction(act);
              maxQ = Double.NEGATIVE_INFINITY;
              for (int k = 0; k < 4; k++) {
                  temp = next.Q[k];
                  if (temp > maxQ) {
                      maxQ = temp;
                  }
              }

              //Update Q(s,a)
              cur.Q[act-1] = cur.Q[act-1] + alpha * (cur.reward + gamma * maxQ - cur.Q[act-1]);
              //System.out.println(cur.Q[act-1]);

              cur = next;
              act = getAction(cur);

              //At terminal state
              if(board.isTerminal(cur.i, cur.j)) {
                break;
              }
          }
          //Calculate Utility and add it to History List
          for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 6; j++) {
              double maxTemp = Double.NEGATIVE_INFINITY;
              for(int k = 0; k < 4; k++) {
                if(maxTemp < board.states[i][j].Q[k]) {
                  maxTemp = board.states[i][j].Q[k];
                }
              }
              board.states[i][j].hist_Q.add(maxTemp);
            }
          }
        }
        for(int i = 0; i < 6; i++) {
          for(int j = 0; j < 6; j++) {
            if(!board.isTerminal(i, j) && !board.isWall(i, j)) {
              double Qmax = Double.NEGATIVE_INFINITY;
              for(int k = 1; k < 5; k++) {
                if(Qmax < board.states[i][j].Q[k - 1]) {
                  Qmax = board.states[i][j].Q[k - 1];
                  board.states[i][j].policyU = k;
                }
              }
              board.states[i][j].expUtil = Qmax;
            }
          }
        }
        System.out.println();
        System.out.println("TD Q-learning:");
        System.out.println();
    }
}
