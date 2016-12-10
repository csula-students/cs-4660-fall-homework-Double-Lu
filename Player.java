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
    static final int COEFF = 20;

    int[] aware = new int[4];  // hold aware value for each position
    int[][] arena = new int[30][20];  // hold the play ground
    Map<Integer, LinkedHashSet<String>> path;

    int numPlayer;
    int id;
    int count;
    int scorePerRound;
    int currentXPos;
    int currentYPos;
    int currentX;
    int currentY;
    int opponentX;
    int opponentY;
    int NW = 150;
    int SW = 150;
    int NE = 150;
    int SE = 150;

    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);

        Player p = new Player();
        p.play(in);
    }

    public void config(int p, int id) {

        numPlayer = p;
        this.id = id;

        count = 2;
        scorePerRound = numPlayer * 4 + 2;
        currentXPos = 2 + 4 * this.id + 2;
        currentYPos = 2 + 4 * this.id + 3;
        currentX = 0;
        currentY = 0;
        opponentX = 0;
        opponentY = 0;

        path = new HashMap<Integer, LinkedHashSet<String>>();
        for (int i = 0; i < numPlayer; i++) {
            path.put(i, new LinkedHashSet<String>());
        }
    }

    public void nextStep() {

        int i = 0;

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

        // Find empty frontier
        aware[LEFT] += (NW / COEFF + SW / COEFF) / 2;
        aware[RIGHT] += (NE / COEFF + SE / COEFF) / 2;
        aware[UP] += (NW / COEFF + NE / COEFF) / 2;
        aware[DOWN] += (SW / COEFF + SE / COEFF) / 2;

        // Avoid nearest boundaries
        if (currentX < XMIN + 3) aware[RIGHT] += COEFF;
        if (currentX > XMAX - 3) aware[LEFT] += COEFF;
        if (currentY < YMIN + 2) aware[DOWN] += COEFF;
        if (currentY > YMAX - 2) aware[UP] += COEFF;

        if (currentX == XMIN + 1) {
            aware[DOWN] += COEFF;
            aware[UP] += COEFF;
        }
        if (currentX == XMAX - 1) {
            aware[DOWN] += COEFF;
            aware[UP] += COEFF;
        }
        if (currentY == YMIN + 1) {
            aware[LEFT] += COEFF;
            aware[RIGHT] += COEFF;
        }
        if (currentY == YMAX - 1) {
            aware[LEFT] += COEFF;
            aware[RIGHT] += COEFF;
        }

        // Find walls
        if (currentX == XMIN) aware[LEFT] = DEADEND;
        if (currentX == XMAX) aware[RIGHT] = DEADEND;
        if (currentY == YMIN) aware[UP] = DEADEND;
        if (currentY == YMAX) aware[DOWN] = DEADEND;

        // test walls
        for (i = 0; i < numPlayer; i++) {
            Set<String> points = path.get(i);
            // System.err.println(points);
            if (points != null) {
                if (points.contains((currentX - 1) + " " + currentY)) aware[LEFT] = DEADEND;
                if (points.contains((currentX + 1) + " " + currentY)) aware[RIGHT] = DEADEND;
                if (points.contains(currentX + " " + (currentY - 1))) aware[UP] = DEADEND;
                if (points.contains(currentX + " " + (currentY + 1))) aware[DOWN] = DEADEND;
            }
        }

        // find the direction who has the max awareness score
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
        int currentId = 0;

        while (true) {

            // Read information
            int n = input.nextInt();
            int position = count % scorePerRound;

            if (position == 0) {
                //Players is " + n
            } else if (position == 1) {
                //My number is " + n
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

                // store points into grid and player's path
                if (position % 2 == 0) x = n; // is x point
                else if (position % 2 == 1) { // is y point

                    currentId = (position - 2) / 4;
                    if (n < 0) {
                        path.put(currentId, null);
                    } else {

                        arena[x][n] = 1;

                        Set<String> points = path.get(currentId);
                        points.add(x + " " + n);

                        if (position % 4 == 0 || position % 4 == 1) {
                            if (x < 15 && n < 10) NW--;
                            else if (x >= 15 && n < 10) NE--;
                            else if (x < 15 && n >= 10) SW--;
                            else if (x >= 15 && n >= 10) SE--;
                        }
                    }
                }
            }
            count++;

            // standard output if it is the end
            if (count % scorePerRound == 0) {

                // compute scores and print action
                nextStep();

            }
        }

    }
}