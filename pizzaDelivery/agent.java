import java.util.*;
import java.lang.RuntimeException;

public class agent{
    state cur;
    state next;
    maze board;
    boolean hasPizza;
    boolean hasIngredients;
    //Chances to next state

    double gamma;

    //Optimistic reward estimates
    int     Ne;

    public agent(maze board) {
        this.hasPizza = false;
        this.hasIngredients = false;

        this.cur = board.start;
        this.board = board;

        this.gamma = 0.99;
        //Bar for select action to try
        this.Ne = 20;
    }

    public boolean hasPizza() {
      return hasPizza;
    }
    public boolean hasIngredients() {
      return hasIngredients;
    }
    public boolean getPizza() {
      if(hasPizza) {
        return false;
      }
      hasPizza = true;
      return true;
    }
    public void getIngredients() {
      hasIngredients = true;
    }

    /**************************************** Reinforcement learning: TD Q-Learning ****************************************/
    Random rn = new Random();
    int getAction(state curState){
        double maxQ = Double.NEGATIVE_INFINITY;
        double temp;
        int action = -1;
        double[] Q = getQ(curState);
        int[] nAct = getNumAct(curState);

        //Select max Q(s, a)
        for (int i = 1; i < 5; i++) {
          if (nAct[i-1] < Ne)
            temp = rn.nextInt(1000); //Make the temp as large as possible so that it will try the current position
          else
            temp = Q[i-1];

          if (temp > maxQ) {
            maxQ = temp;
            action = i;
          }
        }
        return action;
    }

    state takeAction(int dir) {
      int t = rn.nextInt(20);
      if(board.feature[cur.i][cur.j] == 'P' && hasPizza()) {
        if (t == 0)
          return cur.dir[dir+1];
        else if (t == 1)
          return cur.dir[dir-1];
        else if (t == 2 || t == 3 || t == 4 || t == 5 || t == 6 || t == 7)
          return cur;
        else
          return cur.dir[dir];
      }
      if(board.feature[cur.i][cur.j] == 'P' && hasIngredients()) {
        if(!getPizza()) {
          throw new RuntimeException();
        }
        hasIngredients = false;
      }
      if(board.feature[cur.i][cur.j] == 'G') {
        getIngredients();
      }
      if(board.feature[cur.i][cur.j] == 'S') {
        hasPizza = false;
      }
      if(hasPizza()) {      //Carrying a pizza but not in pizza shop
        if (t == 0)
          return cur.dir[dir+1];
        else if (t == 1)
          return cur.dir[dir-1];
        else if (t == 2 || t == 3)
          return cur;
        else
          return cur.dir[dir];
      } else {
        if (t == 0)
          return cur.dir[dir+1];
        else if (t == 1)
          return cur.dir[dir-1];
        else              //not Carrying a pizza
          return cur.dir[dir];
      }
    }
    //Given current state return Q value
    public double[] getQ(state curState) {
      if(hasPizza() && hasIngredients()) {
        return curState.QyesPizzaYesIng;
      } else if(hasPizza()) {
        return curState.QyesPizzaNoIng;
      } else if(hasIngredients()) {
        return curState.QnoPizzaYesIng;
      } else {
        return curState.QnoPizzaNoIng;
      }
    }
    //Given current state return table of numbers of action
    public int[] getNumAct(state curState) {
      if(hasPizza() && hasIngredients()) {
        return curState.yesPizzaYesIngAct;
      } else if(hasPizza()) {
        return curState.yesPizzaNoIngAct;
      } else if(hasIngredients()) {
        return curState.noPizzaYesIngAct;
      } else {
        return curState.noPizzaNoIngAct;
      }
    }
    public double getReward(state curState) {
      if(board.feature[curState.i][curState.j] == 'S' && hasPizza) {
        return curState.reward;
      }
      return -.1;
    }

    //TDQ learning main process
    void tdQlearning(){
        int act; //The preferred actio of s
        //learning rate
        double alpha;
        double temp;
        double maxQ;
        cur = board.start;

        act = getAction(cur);
        for(int l = 0; l < 500000000; l++){
          double[] curQ = getQ(cur);
          int[] curAct = getNumAct(cur);
          double[] nextQ;
          int[] nextAct;
          double curReward;

          cur.visit++;
          curAct[act-1] += 1;
          alpha = 60.0 / (59 + curAct[act-1]);

          curReward = getReward(cur);
          next = takeAction(act);
          nextQ = getQ(next);
          nextAct = getNumAct(next);

          maxQ = Double.NEGATIVE_INFINITY;
          for (int k = 0; k < 4; k++) {
              temp = nextQ[k];
              if (temp > maxQ) {
                  maxQ = temp;
              }
          }
          //Update Q(s,a)
          curQ[act-1] = curQ[act-1] + alpha * (curReward + gamma * maxQ - curQ[act-1]);
          //System.out.println(cur.Q[act-1]);
          cur = next;
          act = getAction(cur);
            //At terminal state
        }
        for(int i = 0; i < 9; i++) {
          for(int j = 0; j < 15; j++) {
            if(!board.isWall(i, j)) {
              double QmaxNoPizzaNoIng = Double.NEGATIVE_INFINITY;
              double QmaxNoPizzaYesIng = Double.NEGATIVE_INFINITY;
              double QmaxYesPizzaNoIng = Double.NEGATIVE_INFINITY;
              double QmaxYesPizzaYesIng = Double.NEGATIVE_INFINITY;
              for(int k = 1; k < 5; k++) {
                if(QmaxNoPizzaNoIng < board.states[i][j].QnoPizzaNoIng[k - 1]) {
                  QmaxNoPizzaNoIng = board.states[i][j].QnoPizzaNoIng[k - 1];
                  board.states[i][j].policyU[0] = k;
                }
                if(QmaxNoPizzaYesIng < board.states[i][j].QnoPizzaYesIng[k - 1]) {
                  QmaxNoPizzaYesIng = board.states[i][j].QnoPizzaYesIng[k - 1];
                  board.states[i][j].policyU[1] = k;
                }
                if(QmaxYesPizzaNoIng < board.states[i][j].QyesPizzaNoIng[k - 1]) {
                  QmaxYesPizzaNoIng = board.states[i][j].QyesPizzaNoIng[k - 1];
                  board.states[i][j].policyU[2] = k;
                }
                if(QmaxYesPizzaYesIng < board.states[i][j].QyesPizzaYesIng[k - 1]) {
                  QmaxYesPizzaYesIng = board.states[i][j].QyesPizzaYesIng[k - 1];
                  board.states[i][j].policyU[3] = k;
                }
              }
            }
          }
        }
        System.out.println();
        System.out.println("TD Q-learning:");
        System.out.println();
    }
}
