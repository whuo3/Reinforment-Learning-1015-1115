import java.util.*;

public class maze {
    state start;

    state[][] states = new state[6][6];

    char[][] feature = new char[6][6];

    void setTerminal(int i, int j, int val){
        feature[i][j] = 'T';
        states[i][j].expUtil = val;
        states[i][j].reward = val;
        for(int k = 0; k < 4; k++) {
          states[i][j].Q[k] = val;
        }
    }
    void setWall(int i, int j){
        feature[i][j] = 'W';
    }
    boolean isWall(int i, int j){
        return feature[i][j] == 'W';
    }
    boolean isTerminal(int i, int j){
        return feature[i][j] == 'T';
    }
    public maze(){
        for (int i = 0; i < 6;i++) {
            for (int j = 0; j < 6; j++) {
                feature[i][j] = '*';
                states[i][j] = new state(i,j);
            }
        }
        setWall(1, 3);
        setWall(2, 3);
        setWall(3, 3);
        setWall(5, 3);
        start = states[3][1];

        setTerminal(0, 1, -1);
        setTerminal(1, 4, -1);
        setTerminal(2, 5, 3);
        setTerminal(5, 0, 1);
        setTerminal(5, 1, -1);
        setTerminal(5, 4, -1);
        setTerminal(5, 5, -1);

        for (int i = 0; i < 6;i++) {
            for (int j = 0; j < 6; j++) {
                states[i][j].dir[1] = states[i][j];
                states[i][j].dir[2] = states[i][j];
                states[i][j].dir[3] = states[i][j];
                states[i][j].dir[4] = states[i][j];

                //Set direction accordingly: 1->north, 2->east, 3->south, 4->west
                if (i > 0) {
                    if (feature[i-1][j] != 'W')
                        states[i][j].dir[1] = states[i-1][j];
                }
                if (j < 5) {
                    if (feature[i][j+1] != 'W')
                       states[i][j].dir[2] = states[i][j+1];
                }
                if (i < 5) {
                    if (feature[i+1][j] != 'W')
                        states[i][j].dir[3] = states[i+1][j];
                }
                if (j > 0) {
                    if (feature[i][j-1] != 'W')
                       states[i][j].dir[4] = states[i][j-1];
                }

                //For round up
                states[i][j].dir[0] = states[i][j].dir[4];
                states[i][j].dir[5] = states[i][j].dir[1];
            }
        }
    }
}
