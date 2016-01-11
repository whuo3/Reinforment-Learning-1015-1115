import java.util.*;

public class state{
    //state position
    int i, j;
    //state directions
    state[] dir = new state[6];
    //reward
    double reward;
    //expect Utility
    double expUtil;
    //policy to pick
    int policyU;
    //History for max value iteration expect
    ArrayList<Double> hist_Val = new ArrayList<Double>();


    //TD-Q learning

    //Q, a table of action values indexed by state and action, initially zero
    double[] Q = new double[4];
    //Nsa , a table of frequencies for state–action pairs, initially zero
    int[] nAct = new int[4];

    double finalQ;
    //Number of times being visited
    int visit;
    int policy_Q;
    ArrayList<Double> hist_Q = new ArrayList<Double>();


    public state(int i, int j){
        this.expUtil = 0;
        this.reward = -.04;
        this.policyU = 1;
        this.i = i;
        this.j = j;


        this.visit = 0;
        this.finalQ=0;
        for (int k=0; k<4;k++) {
            nAct[k]=0;
            Q[k]=0;
        }

    }

}
