
import java.io.*;
import java.util.*;

public class Main {

    static boolean istest = true;
    static int[] dx = {-1, 0, 1, 0, 0};
    static int[] dy = {0, 1, 0, -1, 0};
    static int ROW;
    static int COL;
    static char[][] chars;
    static char place = '.';
    static char wall = '!';
    static char brick = ';';
    static char bomb = 'b';

//    static char[][] chars = {{'b', 'b', 'b', 'b', 'b'},
//    {'b', 'b', 'b', 'b', 'b'},
//    {'b', 'b', 'b', 'b', 'b'},
//    {'b', 'b', 'b', 'b', 'b'},
//    {'.', 'b', 'b', 'b', 'b'}};
    static class Cell {

        int x;
        int y;
        int distance;

        Cell(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
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

    static boolean isInsideGrid(int i, int j) {
        return (isInside(i, j)
                && chars[i][j] != bomb
                && chars[i][j] != wall);
    }

    static boolean isInside(int i, int j) {
        return (i >= 0 && i < ROW
                && j >= 0 && j < COL);
    }

    static int shortestPath(int[][] grid, int startX,
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

        pq.add(new Cell(startX, startY, dist[startX][startY]));
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
                                    dist[rows][cols]);

                            pq.remove(adj);
                        }
                        dist[rows][cols] = dist[curr.x][curr.y]
                                + grid[rows][cols];

                        pq.add(new Cell(rows, cols,
                                dist[rows][cols]));
                    }
                }
            }
        }

        int direction = 4;
        while (true) {
            int minn = Integer.MAX_VALUE;
            int minIndex = 4;
            for (int i = 0; i < 4; i++) {
                int rows = endX + dx[i];
                int cols = endY + dy[i];
                if (isInsideGrid(rows, cols) && dist[rows][cols] < dist[endX + dx[minIndex]][endY + dy[minIndex]]) {
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

        if (istest) {
            System.out.println((endX) + " " + (endY));
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    System.out.print(dist[i][j] == Integer.MAX_VALUE ? "INF " : dist[i][j] + " ");
                }
                System.out.println();
            }
        }

        return direction;
    }

    static Cell escape(char[][] grid, int startX,
            int startY) {

        int row = startX;
        int col = startY;

        int rad = Math.abs(ROW - startX) + Math.abs(COL - startY);

        for (int i = 1; i <= rad; i++) {
            for (int j = 0; j <= i / 2 + i % 2; j++) {
                if (isInsideGrid(startX + j, startY + i - j) && grid[startX + j][startY + i - j] == place) {
                    row = startX + j;
                    col = startY + i - j;
                    break;
                }
                if (isInsideGrid(startX + j, startY - i + j) && grid[startX + j][startY - i + j] == place) {
                    row = startX + j;
                    col = startY - i + j;
                    break;
                }
                if (isInsideGrid(startX + i - j, startY + j) && grid[startX + i - j][startY + j] == place) {
                    row = startX + i - j;
                    col = startY + j;
                    break;
                }
                if (isInsideGrid(startX + i - j, startY - j) && grid[startX + i - j][startY - j] == place) {
                    row = startX + i - j;
                    col = startY - j;
                    break;
                }
                if (isInsideGrid(startX - j, startY - i + j) && grid[startX - j][startY - i + j] == place) {
                    row = startX - j;
                    col = startY - i + j;
                    break;
                }
                if (isInsideGrid(startX - j, startY + i - j) && grid[startX - j][startY + i - j] == place) {
                    row = startX - j;
                    col = startY + i - j;
                    break;
                }
                if (isInsideGrid(startX - i + j, startY - j) && grid[startX - i + j][startY - j] == place) {
                    row = startX - i + j;
                    col = startY - j;
                    break;
                }
                if (isInsideGrid(startX - i + j, startY + j) && grid[startX - i + j][startY + j] == place) {
                    row = startX - i + j;
                    col = startY + j;
                    break;
                }
            }
            if (row != startX || col != startY) {
                break;
            }
        }
        return new Cell(row, col, 0);
    }

    public static void main(String[] args) throws Exception {
        String actions[] = {"down", "up", "left", "right", "stay", "bomb"};
        Scanner scan = new Scanner(System.in);
        if (istest) {
            scan = new Scanner(new File("input.txt"));
        }

        while (true) {
            int player_id, tick, n, p_id, x, y, param_1, param_2;
            String str, type;

            COL = scan.nextInt();
            ROW = scan.nextInt();
            player_id = scan.nextInt();
            tick = scan.nextInt();
            str = scan.nextLine();
            if (!istest) {
                System.err.println(COL + " " +ROW + " " +  player_id + " " + tick);
            }

            Cell pl = new Cell(0, 0, 0);
            Cell r = new Cell(ROW - 1, COL - 1, 0);
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
                        pl = new Cell(x, y, 0);
                    } else {
                        chars[x][y] = brick;
                    }
                } else if (type.equals("r")) {
                    r = new Cell(x, y, 0);
                } else if (type.equals("b")) {
                    chars[x][y] = bomb;
                    for (int j = 1; j <= param_2 + 1 - param_1; j++) {
                        if (isInside(x + j, y)) {
                            chars[x + j][y] = bomb;
                        }
                        if (isInside(x - j, y)) {
                            chars[x - j][y] = bomb;
                        }
                        if (isInside(x, y + j)) {
                            chars[x][y + j] = bomb;
                        }
                        if (isInside(x, y - j)) {
                            chars[x][y - j] = bomb;
                        }
                    }
                }
                if (!istest) {
                    System.err.println(type + " " + p_id + " " + x + " " + y + " " + param_1 + " " + param_2);
                }
            }

            if (chars[pl.x][pl.y] == bomb) {
                r = escape(chars, pl.x, pl.y);
            }

            int direction = shortestPath(grid, pl.x, pl.y, r.x, r.y);
            if (chars[pl.x - dx[direction]][pl.y - dy[direction]] == brick) {
                direction = 5;
            }

            //System.err.println(direction);
            System.out.println(actions[direction]);
            if (istest) {
                break;
            }
        }
    }
}
