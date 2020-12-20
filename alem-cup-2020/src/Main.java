
import java.io.*;
import java.util.*;

public class Main {

    static int[] dx = {-1, 0, 1, 0, 0};
    static int[] dy = {0, 1, 0, -1, 0};
    static int ROW;
    static int COL;
    static char[][] chars;
    static int[][] tunnel;
    static char place = '.';
    static char wall = '!';
    static char brick = ';';
    static char bomb = 'b';
    static char Bomb = 'B';
    static char monster = 'm';
    static String featuresT = "";
    static String featuresR = "";
    static String featuresA = "";

    static class Cell {

        int x;
        int y;
        int distance;
        int bomb;
        int teleport;
        int jump;

        Cell(int x, int y, int distance, int bomb) {
            this.x = x;
            this.y = y;
            this.distance = distance;
            this.bomb = bomb;
        }
    }

    static class distanceComparator
            implements Comparator<Cell> {

        public int compare(Cell a, Cell b) {
            if (a.distance < b.distance) {
                return -1;
            } else if (a.distance > b.distance) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    static boolean isSafePos(int i, int j) {
        boolean res = true;
        if (chars[i][j] == bomb || chars[i][j] == wall || chars[i][j] == Bomb) {
            res = false;
        }

        if (isInside(i + 1, j + 1)) {
            if (chars[i + 1][j + 1] == monster) {
                res = false;
            }
        }
        if (isInside(i + 1, j - 1)) {
            if (chars[i + 1][j - 1] == monster) {
                res = false;
            }
        }
        if (isInside(i - 1, j + 1)) {
            if (chars[i - 1][j + 1] == monster) {
                res = false;
            }
        }
        if (isInside(i - 1, j - 1)) {
            if (chars[i - 1][j - 1] == monster) {
                res = false;
            }
        }

        return res;
    }

    static boolean isInsideGrid(int i, int j) {
        return (isInside(i, j)
                && chars[i][j] != bomb
                && chars[i][j] != Bomb
                && chars[i][j] != wall);
    }

    static boolean isInside(int i, int j) {
        return (i >= 0 && i < ROW
                && j >= 0 && j < COL);
    }

    static int[][] getMinCostPath(int[][] grid, int startX,
            int startY, int endX, int endY) {
        int[][] dist = new int[ROW][COL];

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                dist[i][j] = Integer.MAX_VALUE;
            }
        }

        dist[startX][startY] = grid[startX][startY];

        PriorityQueue<Cell> pq = new PriorityQueue<Cell>(
                (ROW) * (COL), new distanceComparator());

        pq.add(new Cell(startX, startY, dist[startX][startY], 0));
        while (!pq.isEmpty()) {
            Cell curr = pq.poll();
            for (int i = 0; i < 4; i++) {
                int rows = curr.x + dx[i];
                int cols = curr.y + dy[i];

                if (isInside(rows, cols) && chars[rows][cols] != wall
                        && chars[rows][cols] != Bomb
                        && chars[rows][cols] != brick) {
                    if (dist[rows][cols]
                            > dist[curr.x][curr.y]
                            + grid[rows][cols]) {

                        if (dist[rows][cols] != Integer.MAX_VALUE) {
                            Cell adj = new Cell(rows, cols,
                                    dist[rows][cols], 0);

                            pq.remove(adj);
                        }
                        dist[rows][cols] = dist[curr.x][curr.y]
                                + grid[rows][cols];

                        pq.add(new Cell(rows, cols,
                                dist[rows][cols], 0));
                    }
                }
            }
        }
        return dist;
    }

    static Cell shortestPath(int[][] grid, int startX,
            int startY, int endX, int endY) {
        int[][] dist = getMinCostPath(grid, startX, startY, endX, endY);
        Cell res = new Cell(0, 0, dist[endX][endY], 0);
        int direction = 4;
        while (true) {
            int minIndex = 4;
            for (int i = 0; i < 4; i++) {
                int rows = endX + dx[i];
                int cols = endY + dy[i];
                if (isInside(rows, cols) && chars[rows][cols] != wall && chars[rows][cols] != Bomb
                        && dist[rows][cols] < dist[endX + dx[minIndex]][endY + dy[minIndex]]
                        || (startX == endX + dx[i] && startY == endY + dy[i])) {
                    minIndex = i;
                }
            }
            if (minIndex == 4) {
                break;
            }
            if (startX == endX + dx[minIndex] && startY == endY + dy[minIndex]) {
                direction = minIndex;
                break;
            } else {
                endX = endX + dx[minIndex];
                endY = endY + dy[minIndex];
            }
        }

        printMapint(dist, endX, endY);
        res.x = direction;

        return res;
    }

    static void printMapint(int[][] dist, int endX, int endY) {
        if (istest) {
            System.out.println((endX) + " " + (endY));
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    System.out.print((dist[i][j] == Integer.MAX_VALUE ? "INF" : dist[i][j]) + " \t");
                }
                System.out.println();
            }
        }
    }

    static void printMapdouble(double[][] dist, int endX, int endY) {
        if (istest) {
            System.out.println((endX) + " " + (endY));
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    System.out.print((dist[i][j] == Integer.MAX_VALUE ? "INF" : Math.round(dist[i][j] * 100.0) / 100.0) + " \t");
                }
                System.out.println();
            }
        }
    }

    static void printMapchar(char[][] dist) {
        if (istest) {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    System.out.print(dist[i][j] + " \t");
                }
                System.out.println();
            }
        }
    }

    static void printMapbool(boolean[][] dist) {
        if (istest) {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    System.out.print((dist[i][j] ? 1 : 0) + " \t");
                }
                System.out.println();
            }
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    static Cell getManyBrick(char[][] grid, Cell pl) {
        double[][] volume = new double[ROW][COL];
        int row = pl.x;
        int col = pl.y;
        boolean finded = true;

        double v = Math.sqrt(Math.sqrt((ROW * COL)));

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (chars[i][j] == place) {
                    for (int k = 0; k < dx.length - 1; k++) {
                        for (int l = 1; l <= pl.distance; l++) {
                            if (isInside(i + dx[k] * l, j + dy[k] * l)) {
                                if (chars[i + dx[k] * l][j + dy[k] * l] == brick) {
                                    if (volume[i][j] < 0) {
                                        volume[i][j] *= 1.3;
                                    } else {
                                        volume[i][j] -= v;
                                    }
//                                    if ((Math.abs(pl.x - i + dx[k] * l) + Math.abs(pl.y - i + dy[k] * l))
//                                            < (Math.abs(pl.x - row) + Math.abs(pl.y - col))) {
//                                        row = i + dx[k] * l;
//                                        col = i + dy[k] * l;
//                                    }
                                    break;
                                } else if (chars[i + dx[k] * l][j + dy[k] * l] == wall) {
                                    break;
                                }
                            }
                        }
                    }
                }
                if (featuresT.contains("[" + (i) + ":" + (j) + "-")) {
                    if (volume[i][j] < 0) {
                        volume[i][j] *= 1.5 * v / (pl.distance - 1);
                    } else {
                        volume[i][j] -= 2 * v / (pl.distance - 1);
                    }
                }
                if (featuresR.contains("[" + (i) + ":" + (j) + "-") && chars[i][j] != bomb) {
                    if (volume[i][j] < 0) {
                        volume[i][j] *= 1 + 0.25 / (pl.distance - 1);
                    } else {
                        volume[i][j] -= v / (pl.distance - 1);
                    }
                }
                if (featuresA.contains("[" + (i) + ":" + (j) + "-") && chars[i][j] != bomb) {
                    if (volume[i][j] < 0) {
                        volume[i][j] *= 1 + 0.25 / (pl.bomb + 1);
                    } else {
                        volume[i][j] -= v / (pl.bomb + 1);
                    }
                }
            }
        }
        if (volume[pl.x][pl.y] == 0) {
            volume[pl.x][pl.y] = Integer.MAX_VALUE;
            finded = false;
        }
        boolean[][] visited = new boolean[ROW][COL];

        LinkedList<Cell> queue = new LinkedList<Cell>();
        queue.add(new Cell(pl.x, pl.y, 0, 0));
        visited[pl.x][pl.y] = true;

        int iter = -1;
        while (queue.size() > 0) {
            iter++;
            Cell c = queue.poll();
            if (istest && false) {
                System.out.println("target:" + row + " " + col);
                for (int i = 0; i < queue.size(); i++) {
                    Cell c2 = queue.get(i);
                    System.out.print(c2.x + ":" + c2.y + "; ");
                }
                System.out.println();
                printMapdouble(volume, c.x, c.y);
            }
            if (volume[c.x][c.y] < volume[row][col]) {
                row = c.x;
                col = c.y;
                finded = true;
            }
            for (int i = 0; i < dx.length - 1; i++) {
                if (isInside(c.x + dx[i], c.y + dy[i])
                        && (chars[c.x + dx[i]][c.y + dy[i]] == place || chars[c.x + dx[i]][c.y + dy[i]] == bomb)
                        && !visited[c.x + dx[i]][c.y + dy[i]]) {
                    queue.add(new Cell(c.x + dx[i], c.y + dy[i], 0, 0));
                    visited[c.x + dx[i]][c.y + dy[i]] = true;
                    volume[c.x + dx[i]][c.y + dy[i]] += Math.sqrt(Math.sqrt(iter));
                }
            }
        }
        printMapdouble(volume, row, col);

        return new Cell(row, col, 0, finded ? 1 : 0);
    }

    static String GetTunnelPath(Cell pl, int x, int y) {
        String path = "<" + x + ":" + y + "-" + tunnel[x][y];

        int j = 100;
        while (j > 0) {
            j--;
            int step = tunnel[x][y];

            for (int i = 0; i < dx.length - 1; i++) {
                if (isInside(x + dx[i], y + dy[i])
                        && tunnel[x + dx[i]][y + dy[i]] != Integer.MAX_VALUE
                        && tunnel[x][y] < tunnel[x + dx[i]][y + dy[i]]) {
                    x = x + dx[i];
                    y = y + dy[i];
                    break;
                }
            }
            path = path + "<" + x + ":" + y + "-" + tunnel[x][y];

            if (tunnel[x][y] == 100
                    || (false && pl.x == x && pl.y == y)) {
                break;
            }
        }
        return path;
    }

    static void GetTunnel() {
        printMapint(tunnel, 0, 0);
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (tunnel[i][j] == 0) {
                    int dir = 0;
                    int exit = 0;
                    for (int k = 0; k < dx.length - 1; k++) {
                        if (isInside(i + dx[k], j + dy[k])) {
                            dir++;
                            if (tunnel[i + dx[k]][j + dy[k]] == Integer.MAX_VALUE) {
                                exit++;
                            }
                        }
                    }
                    if (dir - exit == 1) {
                        SetTunnel(i, j, 1);
                    }
                }
            }
        }
        printMapint(tunnel, 0, 0);
    }

    static boolean inTunnel(Cell pl) {
        boolean res = false;

        if (tunnel[pl.x][pl.y] == 1) {
            res = true;
        }

        return res;
    }

    static void SetTunnel(int i, int j, int vol) {
        int dir = 0;
        for (int k = 0; k < dx.length - 1; k++) {
            if (isInside(i + dx[k], j + dy[k])
                    && (tunnel[i + dx[k]][j + dy[k]] == 0 || tunnel[i + dx[k]][j + dy[k]] == 100)) {
                dir++;
            }
        }
        if (dir == 1 && tunnel[i][j] != 100 && (tunnel[i][j] == 0 || tunnel[i][j] > vol)) {
            tunnel[i][j] = vol;
            for (int k = 0; k < dx.length - 1; k++) {
                if (isInside(i + dx[k], j + dy[k])
                        && (tunnel[i + dx[k]][j + dy[k]] == 0 || tunnel[i + dx[k]][j + dy[k]] == 100)) {
                    SetTunnel(i + dx[k], j + dy[k], vol + 1);
                }
            }
        } else if (dir > 1) {
            tunnel[i][j] = 100;
        }
    }

    static void SetTunnelClear(int i, int j) {
        if (tunnel[i][j] == 100) {
            tunnel[i][j] = 99;
        } else {
            for (int k = 0; k < dx.length - 1; k++) {
                if (isInside(i + dx[k], j + dy[k]) && tunnel[i + dx[k]][j + dy[k]] > tunnel[i][j]) {
                    SetTunnelClear(i + dx[k], j + dy[k]);
                }
            }
        }
    }

    static Cell escape(char[][] grid, int startX,
            int startY, char aim) {

        int row = startX;
        int col = startY;
        boolean finded = false;
        boolean[][] visited = new boolean[ROW][COL];

        LinkedList<Cell> queue = new LinkedList<Cell>();
        queue.add(new Cell(startX, startY, 0, 0));
        visited[startX][startY] = true;

        while (queue.size() > 0) {
            if (istest) {
                for (int i = 0; i < queue.size(); i++) {
                    Cell c2 = queue.get(i);
                    System.out.print(c2.x + ":" + c2.y + "; ");
                }
                System.out.println();
                printMapbool(visited);
            }
            Cell c = queue.poll();
            if (aim == brick && (chars[c.x][c.y] == brick || chars[c.x][c.y] == monster)) {
                row = c.x;
                col = c.y;
                finded = true;
                break;
            } else if (aim == monster && chars[c.x][c.y] == monster) {
                row = c.x;
                col = c.y;
                finded = true;
                break;
            } else if (aim == place
                    && isInside(c.x, c.y)
                    && chars[c.x][c.y] == place) {
                row = c.x;
                col = c.y;
                finded = true;
                break;
            }
            for (int i = 0; i < dx.length; i++) {
                if (isInside(c.x + dx[i], c.y + dy[i])
                        && chars[c.x + dx[i]][c.y + dy[i]] != wall
                        && chars[c.x + dx[i]][c.y + dy[i]] != Bomb
                        && !visited[c.x + dx[i]][c.y + dy[i]]) {
                    if ((aim == brick || aim == monster || (aim == place
                            && chars[c.x + dx[i]][c.y + dy[i]] != brick
                            && chars[c.x + dx[i]][c.y + dy[i]] != Bomb
                            && chars[c.x + dx[i]][c.y + dy[i]] != wall))) {
                        queue.add(new Cell(c.x + dx[i], c.y + dy[i], 0, 0));
                        visited[c.x + dx[i]][c.y + dy[i]] = true;
                    }
                }
            }
            if (finded) {
                break;
            }
        }

        return new Cell(row, col, 0, 0);
    }

    static void setBomb(int x, int y, int perimeter, char elem, Cell pl) {
//        chars[x][y] = elem;
        for (int j = 0; j < dx.length - 1; j++) {
            for (int i = 1; i <= perimeter; i++) {
                if (isInside(x + i * dx[j], y + i * dy[j])) {
                    if (chars[x + i * dx[j]][y + i * dy[j]] == wall
                            || (elem == wall && pl.x == x + i * dx[j] && pl.y == y + i * dy[j])) {
                        break;
                    } else {
                        if (chars[x + i * dx[j]][y + i * dy[j]] == brick) {
                            chars[x + i * dx[j]][y + i * dy[j]] = wall;
                            break;
                        } else {
                            chars[x + i * dx[j]][y + i * dy[j]] = elem;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String actions[] = {"down", "left", "up", "right", "stay", "bomb", "jump"};
        Scanner scan = new Scanner(System.in);
        if (istest) {
            scan = new Scanner(new File("input.txt"));
        }

        while (true) {
            int player_id, tick, n, p_id, x, y, param_1, param_2;
            String str, type;

            boolean hasBrick = false;
            featuresT = "";
            featuresR = "";
            featuresA = "";

            COL = scan.nextInt();
            ROW = scan.nextInt();
            player_id = scan.nextInt();
            tick = scan.nextInt();
            str = scan.nextLine();
            if (!istest) {
                System.err.println(COL + " " + ROW + " " + player_id + " " + tick);
            }

            Cell pl = new Cell(0, 0, 0, 0);
            Cell pl2 = new Cell(0, 0, 0, 0);
            Cell r = new Cell(0, 0, 0, 0);
            int[][] grid = new int[ROW][COL];
            chars = new char[ROW][COL];
            tunnel = new int[ROW][COL];

            for (int i = 0; i < ROW; i++) {
                str = scan.nextLine();
                if (!istest) {
                    System.err.println(str);
                }
                for (int j = 0; j < str.length(); j++) {
                    chars[i][j] = str.charAt(j);
                    if (chars[i][j] == place) {
                        grid[i][j] = 1;
                    } else if (chars[i][j] == brick) {
                        tunnel[i][j] = Integer.MAX_VALUE;
                        grid[i][j] = 10;
                        hasBrick = true;
                    } else if (chars[i][j] == wall) {
                        grid[i][j] = Integer.MAX_VALUE;
                        tunnel[i][j] = Integer.MAX_VALUE;
                    }
                }
            }
            n = scan.nextInt();

            if (!istest) {
                System.err.println(n);
            }

            ArrayList<Cell> bombs = new ArrayList<Cell>();
            ArrayList<Cell> players = new ArrayList<Cell>();
            type = scan.nextLine();

            for (int i = 0; i < n; i++) {
                type = scan.next();
                p_id = scan.nextInt();
                y = scan.nextInt();
                x = scan.nextInt();
                param_1 = scan.nextInt();
                param_2 = scan.nextInt();
                if (type.equals("p")) {
                    if (player_id == p_id) {
                        pl = new Cell(x, y, param_2, param_1);
                    } else {
                        pl2 = new Cell(x, y, param_2, param_1);
                    }
                } else if (type.equals("r")) {
                    r = new Cell(x, y, 0, 0);
                } else if (type.equals("m")) {
                    chars[x][y] = monster;
                    setBomb(x, y, 2, monster, pl);
                    if (isInsideGrid(x + 1, y + 1)
                            && chars[x + 1][y + 1] != brick) {
                        chars[x + 1][y + 1] = monster;
                    }
                    if (isInsideGrid(x + 1, y - 1)
                            && chars[x + 1][y - 1] != brick) {
                        chars[x + 1][y - 1] = monster;
                    }
                    if (isInsideGrid(x - 1, y + 1)
                            && chars[x - 1][y + 1] != brick) {
                        chars[x - 1][y + 1] = monster;
                    }
                    if (isInsideGrid(x - 1, y - 1)
                            && chars[x - 1][y - 1] != brick) {
                        chars[x - 1][y - 1] = monster;
                    }

                } else if (type.equals("b")) {
                    bombs.add(new Cell(x, y, param_2, param_1));
                } else if (type.equals("f_t") || type.equals("f_j")) {
                    featuresT += "[" + x + ":" + y + "-";
                } else if (type.equals("f_r")) {
                    featuresR += "[" + x + ":" + y + "-";
                } else if (type.equals("f_a")) {
                    featuresA += "[" + x + ":" + y + "-";
                }
                if (!istest) {
                    System.err.println(type + " " + p_id + " " + y + " " + x + " " + param_1 + " " + param_2);
                }
            }

            n = scan.nextInt();

            if (!istest) {
                System.err.println(n);
            }
            for (int i = 0; i < n; i++) {
                int pId = scan.nextInt();
                int v = scan.nextInt();
                if (!istest) {
                    System.err.println(pId + " " + v);
                }
                if (player_id == pId) {
                    if (v == 1) {
                        pl.teleport = 1;
                    }
                    if (v == 0) {
                        pl.jump = 1;
                    }
                } else {
                    if (v == 1) {
                        pl2.teleport = 1;
                    }
                    if (v == 0) {
                        pl2.jump = 1;
                    }
                }
            }

//            if (istest) {
//                System.out.println("PL2: " + pl2path.x + " " + pl2path.distance);
//            }
            for (Cell b : bombs) {
                chars[b.x][b.y] = wall;
                tunnel[b.x][b.y] = Integer.MAX_VALUE;
                setBomb(b.x, b.y, b.distance + 1, bomb, pl);
                if (!(b.x == pl.x && b.y == pl.y)) {
                    setBomb(b.x, b.y, Math.min(b.distance, 4) + 1 - b.bomb, wall, pl);
                }
            }

            printMapchar(chars);
            GetTunnel();
            Cell pl2path = shortestPath(grid, pl.x, pl.y, pl2.x, pl2.y);

            if (istest) {
                System.out.println("pl2path.distance: " + pl2path.distance);
            }

            int direction = 4;
            String actionvalue = actions[direction];
            String path = "";
            if (tunnel[pl2.x][pl2.y] > 0
                    && tunnel[pl2.x][pl2.y] < 100) {
                path = GetTunnelPath(pl, pl2.x, pl2.y);
                if (istest) {
                    System.out.println("path: " + path);
                }
            }

            if (pl2path.distance < Integer.MAX_VALUE
                    && tunnel[pl2.x][pl2.y] > 0
                    && tunnel[pl2.x][pl2.y] < 100
                    && tunnel[pl2.x][pl2.y] < tunnel[pl.x][pl.y]
                    && pl.bomb > 0 && path.contains("<" + pl.x + ":" + pl.y + "-")) {
                direction = 5;
                actionvalue = actions[direction];
            } else {
//            if (pl2.teleport > 0 || pl2path.distance < 10) {
//                SetTunnelClear(pl.x, pl.y);
//                for (int i = 0; i < ROW; i++) {
//                    for (int j = 0; j < COL; j++) {
//                        if (tunnel[i][j] == 100) {
//                            for (int k = 0; k < dx.length - 1; k++) {
//                                if (isInside(i + dx[k], j + dy[k])
//                                        && tunnel[i + dx[k]][j + dy[k]] != 0
//                                        && tunnel[i + dx[k]][j + dy[k]] != Integer.MAX_VALUE) {
////                                    grid[i + dx[k]][j + dy[k]] = Integer.MAX_VALUE;
//                                    chars[i + dx[k]][j + dy[k]] = wall;
//                                }
//                            }
//                        }
//                    }
//                }
//                printMapchar(chars);
//            }

                if (pl.teleport > 0 && pl.bomb > 0
                        && tunnel[pl2.x][pl2.y] > 0
                        && tunnel[pl2.x][pl2.y] < 100
                        && path.split("<").length > 3) {
//                    String path = GetTunnelPath(pl, pl2);
                    pl2.bomb = Integer.parseInt(path.split("<")[1].split("-")[1]);
                    System.out.println(pl2.bomb);
                    for (String s1 : path.split("<")) {
                        if (s1.length() > 0) {
                            if (Integer.parseInt(s1.split("-")[1]) > pl2.bomb + 1) {
                                actionvalue = "tp " + Integer.parseInt(s1.split("-")[0].split(":")[1]) + " " + Integer.parseInt(s1.split("-")[0].split(":")[0]);
                            }
                        }
                    }
                } else {
                    if (!isSafePos(pl.x, pl.y)) {
                        if (pl.distance < 5) {
                            r = getManyBrick(chars, pl);
                        } else {
                            r = escape(chars, pl.x, pl.y, place);
                        }
                        if (istest) {
                            System.out.println("portal: " + r.x + " " + r.y);
                        }
                    } else if (r.x == 0 && r.y == 0 && hasBrick) {
                        r = getManyBrick(chars, pl);
                        if (istest) {
                            System.out.println("brick: " + r.x + " " + r.y);
                        }
                    }
                    hasBrick = false;
                    for (int i = 0; i < ROW; i++) {
                        for (int j = 0; j < COL; j++) {
                            if (chars[i][j] == brick) {
                                hasBrick = true;
                            }
                        }

                    }
                    if (!hasBrick) {

                        if (!isSafePos(pl.x, pl.y)) {
                            r = escape(chars, pl.x, pl.y, place);
                        } else {
                            r.x = ROW / 2;
                            r.y = COL / 2;
                        }
                        if (istest) {
                            System.out.println("center: " + r.x + " " + r.y);
                        }
                    }

                    Cell res = shortestPath(grid, pl.x, pl.y, r.x, r.y);
                    direction = res.x;
                    if (pl.x == r.x && pl.y == r.y && pl.bomb > 0 && r.bomb > 0) {
                        direction = 5;
                    } else {
                        if (chars[pl.x - dx[direction]][pl.y - dy[direction]] == wall && pl.jump > 0) {
                            direction = 6;
                        } else if ((chars[pl.x - dx[direction]][pl.y - dy[direction]] == brick
                                || chars[pl.x - dx[direction]][pl.y - dy[direction]] == monster)
                                && chars[pl.x][pl.y] != bomb
                                && pl.bomb > 0) {
                            direction = 5;
                        }
                    }
                    actionvalue = actions[direction];
                }
            }
            if (istest) {
                System.out.println(actionvalue);
                break;
            } else {
                System.err.println(actionvalue);
                System.out.println(actionvalue);
            }
        }
    }
    static boolean istest = 1 == 1;
}
