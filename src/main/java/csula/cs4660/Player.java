import java.util.*;

class Player {

    static final int LEFT = 0;
    static final int RIGHT = 1;
    static final int UP = 2;
    static final int DOWN = 3;
    static final int DEADEND = -1;
    static final int XMIN = 0;
    static final int XMAX = 29;
    static final int YMIN = 0;
    static final int YMAX = 19;

    int[] aware = new int[4];
    int[][] arena = new int[30][20];

    int numPlayers;
    int id;
    int count;
    int scorePerRound;
    int currentXPos;
    int currentYPos;
    int currentX;
    int currentY;
    int opponentX;
    int opponentY;

    public static void main(String args[]) {

        Scanner input = new Scanner(System.in);

        Player p = new Player();
        p.play(input);

    }

    public void config(int p, int id) {

        numPlayers = p;
        this.id = id;

        count = 2;
        scorePerRound = numPlayers * 4 + 2;
        currentXPos = 2 + 4 * this.id + 2;
        currentYPos = 2 + 4 * this.id + 3;
        currentX = 0;
        currentY = 0;
        opponentX = 0;
        opponentY = 0;

    }

    public void nextStep() {

        int i = 0;

        // Space between me and neighbors
        int WEST = 0;
        i = currentX - 1;
        while (i >= 0 && arena[i][currentY] == 0) {
            WEST++;
            i--;
        }

        int EAST = 0;
        i = currentX + 1;
        while (i <= 29 && arena[i][currentY] == 0) {
            EAST++;
            i++;
        }

        int NORTH = 0;
        i = currentY - 1;
        while (i >= 0 && arena[currentX][i] == 0) {
            NORTH++;
            i--;
        }

        int SOUTH = 0;
        i = currentY + 1;
        while (i <= 19 && arena[currentX][i] == 0) {
            SOUTH++;
            i++;
        }

        // add space to the unoccupied frontier
        aware[LEFT] += WEST;
        aware[RIGHT] += EAST;
        aware[UP] += NORTH;
        aware[DOWN] += SOUTH;

        // Find walls
        if (currentX == XMIN) aware[LEFT] = DEADEND;
        if (currentX == XMAX) aware[RIGHT] = DEADEND;
        if (currentY == YMIN) aware[UP] = DEADEND;
        if (currentY == YMAX) aware[DOWN] = DEADEND;

        // find the direction who has the maximum frontier
        int step = 0;
        int max = -1;
        for (i = 0; i < aware.length; i++) {
            if (i == 0)
                System.err.println("West Frontier: \t" + aware[i]);
            if (i == 1)
                System.err.println("East Frontier: \t" + aware[i]);
            if (i == 2)
                System.err.println("North Frontier: \t" + aware[i]);
            if (i == 3)
                System.err.println("South Frontier: \t" + aware[i]);
            if (aware[i] > max) {
                step = i;
                max = aware[i];
            }
            aware[i] = 0;
        }

        if (step == LEFT)
            System.out.println("LEFT");
        if (step == RIGHT)
            System.out.println("RIGHT");
        if (step == UP)
            System.out.println("UP");
        if (step == DOWN)
            System.out.println("DOWN");

    }

    public void play(Scanner input) {

        config(input.nextInt(), input.nextInt());

        int x = 0;
        int currentPlayerId = 0;

        while (true) {

            int n = input.nextInt();
            int position = count % scorePerRound;

            if (position == 0) {
            } else if (position == 1) {
            } else {
                if (position == currentXPos) {
                    currentX = n;
                } else if (position == currentYPos) {
                    currentY = n;
                } else if (position % 4 == 0) { //opponent current x position
                    opponentX = n;
                } else if (position % 4 == 1) { // opponent current y position
                    opponentY = n;
                }

                if (position % 2 == 0) x = n; // is x point
                else if (position % 2 == 1) { // is y point

                    currentPlayerId = (position - 2) / 4;
                    if (n < 0) {
                    } else {

                        //store areas that are occupied in arena
                        arena[x][n] = 1;

                    }
                }
            }

            count++;

            // Write action to standard output if it's the end
            if (count % scorePerRound == 0) {

                // compute scores and print action
                nextStep();

            }
        }

    }
}