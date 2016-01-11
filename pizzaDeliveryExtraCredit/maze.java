import java.util.*;

public class maze {
  state start;

  state[][] states = new state[9][15];

  char[][] feature = new char[9][15];

  void setStudent(int i, int j, int val) {
    feature[i][j] = 'S';
    states[i][j].reward = val;
    //states[i][j].expUtil = 0.0;
  }
  boolean isStudent(int i, int j) {
    return feature[i][j] == 'S';
  }
  void setWall(int i, int j) {
    feature[i][j] = 'W';
  }
  boolean isWall(int i, int j) {
    return feature[i][j] == 'W';
  }
  void setPizzaShop(int i, int j) {
    feature[i][j] = 'P';
  }
  boolean isPizzaShop(int i, int j) {
    return feature[i][j] == 'P';
  }
  void setGroceryStore(int i, int j) {
    feature[i][j] = 'G';
  }
  boolean isGroceryStore(int i, int j) {
    return feature[i][j] == 'G';
  }
  void setHome(int i, int j) {
    feature[i][j] = 'H';
  }
  boolean isHome(int i, int j) {
    return feature[i][j] == 'H';
  }
  public maze(){
    for (int i = 0; i < 9;i++) {
      for (int j = 0; j < 15; j++) {
        feature[i][j] = '*';
        states[i][j] = new state(i,j);
      }
    }
    for (int i = 0; i < 9; i++) {
      setWall(i, 0);
      setWall(i, 14);
    }
    for (int i = 0; i < 15; i++) {
      setWall(0, i);
      setWall(8, i);
    }
    setWall(4, 1);
    setWall(4, 2);
    setWall(4, 3);
    setWall(4, 4);
    setWall(3, 4);

    setWall(4, 10);
    setWall(4, 11);
    setWall(4, 12);
    setWall(4, 13);
    setWall(3, 10);
    setWall(5, 10);

    setGroceryStore(6, 2);
    setGroceryStore(6, 12);

    setPizzaShop(1, 9);
    setPizzaShop(7, 6);
    setPizzaShop(7, 7);

    setStudent(2, 2, 5);
    setStudent(2, 12, 5);

    setHome(4, 7);

    start = states[6][2];

    for (int i = 0; i < 9;i++) {
      for (int j = 0; j < 15; j++) {
        states[i][j].dir[1] = states[i][j];
        states[i][j].dir[2] = states[i][j];
        states[i][j].dir[3] = states[i][j];
        states[i][j].dir[4] = states[i][j];

        //Set direction accordingly: 1->north, 2->east, 3->south, 4->west
        if (i >= 2) {
          if (feature[i-1][j] != 'W')
            states[i][j].dir[1] = states[i-1][j];
        }
        if (j <= 12) {
          if (feature[i][j+1] != 'W')
            states[i][j].dir[2] = states[i][j+1];
        }
        if (i <= 7) {
          if (feature[i+1][j] != 'W')
            states[i][j].dir[3] = states[i+1][j];
        }
        if (j >= 2) {
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
