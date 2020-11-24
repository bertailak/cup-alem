
import java.io.*;
import java.util.*;

public class Main {

    static int[] dx = {-1, 0, 1, 0, 0};
    static int[] dy = {0, 1, 0, -1, 0};
    static int ROW;
    static int COL;
    static char[][] chars;
    static char place = '.';
    static char wall = '!';
    static char brick = ';';
    static char bomb = 'b';
    static char monster = 'm';

//    static char[][] chars = {{'b', 'b', 'b', 'b', 'b'},
//    {'b', 'b', 'b', 'b', 'b'},
//    {'b', 'b', 'b', 'b', 'b'},
//    {'b', 'b', 'b', 'b', 'b'},
//    {'.', 'b', 'b', 'b', 'b'}};
    static class Cell {

        int x;
        int y;
        int distance;
        int bomb;

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
        if (isInside(i, j)) {
            if (chars[i][j] == bomb) {
                res = false;
            }
        }
        if (isInside(i - 2, j)) {
            if (chars[i - 2][j] == bomb) {
                res = false;
            }
        }
        if (isInside(i - 1, j)) {
            if (chars[i - 1][j] == bomb) {
                res = false;
            }
        }
        if (isInside(i, j - 2)) {
            if (chars[i][j - 2] == bomb) {
                res = false;
            }
        }
        if (isInside(i, j - 1)) {
            if (chars[i][j - 1] == bomb) {
                res = false;
            }
        }
        if (isInside(i + 1, j)) {
            if (chars[i + 1][j] == bomb) {
                res = false;
            }
        }
        if (isInside(i + 2, j)) {
            if (chars[i + 2][j] == bomb) {
                res = false;
            }
        }
        if (isInside(i, j + 1)) {
            if (chars[i][j + 1] == bomb) {
                res = false;
            }
        }
        if (isInside(i, j + 2)) {
            if (chars[i][j + 2] == bomb) {
                res = false;
            }
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

                if (isInsideGrid(rows, cols)) {
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

    static int shortestPath(int[][] grid, int startX,
            int startY, int endX, int endY) {
        int[][] dist = getMinCostPath(grid, startX, startY, endX, endY);
        int direction = 4;
        while (true) {
            int minIndex = 4;
            for (int i = 0; i < 4; i++) {
                int rows = endX + dx[i];
                int cols = endY + dy[i];
                if (isInsideGrid(rows, cols)
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

        return direction;
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
            if (istest && false) {
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
                    && isSafePos(c.x, c.y)
                    && chars[c.x][c.y] != brick
                    && chars[c.x][c.y] != monster) {
                row = c.x;
                col = c.y;
                finded = true;
                break;
            }
            for (int i = 0; i < dx.length; i++) {
                if (isInsideGrid(c.x + dx[i], c.y + dy[i])
                        && !visited[c.x + dx[i]][c.y + dy[i]]) {
                    if ((aim == brick || aim == monster || (aim == place
                            && chars[c.x + dx[i]][c.y + dy[i]] != brick
                            && chars[c.x + dx[i]][c.y + dy[i]] != monster))) {
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

    static void setBomb(int x, int y, int perimeter, char elem) {
        chars[x][y] = elem;
        for (int j = 0; j < dx.length - 1; j++) {
            for (int i = 1; i <= perimeter; i++) {
                if (isInside(x + i * dx[j], y + i * dy[j])) {
                    if (chars[x + i * dx[j]][y + i * dy[j]] != wall && chars[x + i * dx[j]][y + i * dy[j]] != bomb) {
                        if (chars[x + i * dx[j]][y + i * dy[j]] == brick) {
                            chars[x + i * dx[j]][y + i * dy[j]] = elem;
                            break;
                        } else {
                            chars[x + i * dx[j]][y + i * dy[j]] = elem;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String actions[] = {"down", "left", "up", "right", "stay", "bomb"};
        Scanner scan = new Scanner(System.in);
        if (istest) {
            scan = new Scanner(new File("input.txt"));
        }

        while (true) {
            int player_id, tick, n, p_id, x, y, param_1, param_2;
            String str, type;

            boolean hasBrick = false;

            COL = scan.nextInt();
            ROW = scan.nextInt();
            player_id = scan.nextInt();
            tick = scan.nextInt();
            str = scan.nextLine();
            if (!istest) {
                System.err.println(COL + " " + ROW + " " + player_id + " " + tick);
            }

            Cell pl = new Cell(0, 0, 0, 0);
            Cell r = new Cell(0, 0, 0, 0);
            int[][] grid = new int[ROW][COL];
            chars = new char[ROW][COL];

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
                        grid[i][j] = 10;
                        hasBrick = true;
                    } else if (chars[i][j] == wall) {
                        grid[i][j] = Integer.MAX_VALUE;
                    }
                }
            }
            n = scan.nextInt();

            if (!istest) {
                System.err.println(n);
            }

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
                        pl = new Cell(x, y, 0, param_1);
                    } else {
                        if (param_1 > 0) {
                            chars[x][y] = monster;
                        } else {
                            chars[x][y] = brick;
                        }
                    }
                } else if (type.equals("r")) {
                    r = new Cell(x, y, 0, 0);
                } else if (type.equals("m")) {
                    chars[x][y] = monster;
                    setBomb(x, y, 2, monster);
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
                    setBomb(x, y, param_2 + 1 - param_1, bomb);
                }
                if (!istest) {
                    System.err.println(type + " " + p_id + " " + y + " " + x + " " + param_1 + " " + param_2);
                }
            }

            printMapchar(chars);
            if (!isSafePos(pl.x, pl.y) || pl.bomb == 0) {
                r = escape(chars, pl.x, pl.y, place);
                if (istest) {
                    System.out.println("portal: " + r.x + " " + r.y);
                }
            } else if (r.x == 0 && r.y == 0) {
                r = escape(chars, pl.x, pl.y, brick);
                if (istest) {
                    System.out.println("brick: " + r.x + " " + r.y);
                }
            }

            int direction = shortestPath(grid, pl.x, pl.y, r.x, r.y);
            if ((chars[pl.x - dx[direction]][pl.y - dy[direction]] == brick
                    || chars[pl.x - dx[direction]][pl.y - dy[direction]] == monster)
                    && pl.bomb > 0) {
                direction = 5;
            } else {
//                if (!isSafePos(pl.x - dx[direction], pl.y - dy[direction])) {
//                    direction = 4;
//                }
            }

            System.out.println(actions[direction]);
            if (istest) {
                break;
            } else {
                System.err.println(actions[direction]);
            }
        }
    }
    static boolean istest = 1 == 1;
}
