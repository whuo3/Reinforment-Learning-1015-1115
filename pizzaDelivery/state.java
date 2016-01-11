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
    /*
    policyU[0] -> no Pizza no Ingredient
    policyU[1] -> no Pizza yes Ingredient
    policyU[2] -> yes Pizza no Ingredient
    policyU[3] -> yes Pizza yes Ingredient
    */
    int[] policyU = new int[4];

    //TD-Q learning

    //Q, a table of action values indexed by state and action, initially zero
    //No pizza, no ingredients
    double[] QnoPizzaNoIng = new double[4];
    //No pizza, yes ingredients
    double[] QnoPizzaYesIng = new double[4];
    //Yes pizza, no ingredients
    double[] QyesPizzaNoIng = new double[4];
    //Yes pizza, yes ingredients
    double[] QyesPizzaYesIng = new double[4];

    //Nsa , a table of frequencies for state–action pairs, initially zero
    int[] noPizzaNoIngAct = new int[4];
    int[] noPizzaYesIngAct = new int[4];
    int[] yesPizzaNoIngAct = new int[4];
    int[] yesPizzaYesIngAct = new int[4];
    //Number of times being visited
    int visit;

    public state(int i, int j){
        this.expUtil = 0;
        this.reward = -.1;
        this.i = i;
        this.j = j;

        this.visit = 0;
        for (int k = 0; k < 4; k++) {
            noPizzaNoIngAct[k] = 0;
            noPizzaYesIngAct[k] = 0;
            yesPizzaNoIngAct[k] = 0;
            yesPizzaYesIngAct[k] = 0;
            QnoPizzaNoIng[k] = 0.0;
            QnoPizzaYesIng[k] = 0.0;
            QyesPizzaNoIng[k] = 0.0;
            QyesPizzaYesIng[k] = 0.0;
        }

    }

}
