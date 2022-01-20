package TapasGames.Game.MiniGames;

import java.util.Random;

public class Board {
    private Field[][] grid;
    private int mines;

    public Board(int sizeX, int sizeY, int mines) {
        System.out.println("Board is beeing created!");
        if (sizeX < 1 || sizeY < 1 || mines < 0 || sizeX * sizeY < mines) {
            throw new IllegalArgumentException("Size and mines aren't properly defined");
        }
        this.mines = mines;
        this.grid = new Field[sizeX][sizeY];
        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                this.grid[i][j] = new Field();
            }
        }
        for (int i = 0; i < mines; ++i) {
            Random r;
            int r2;
            int r3;
            for (r = new Random(), r2 = r.nextInt(sizeX), r3 = r.nextInt(sizeY); this.grid[r2][r3].isMine(); r2 = r.nextInt(sizeX), r3 = r.nextInt(sizeY)) {
            }
            this.grid[r2][r3].setMine();
        }
    }

    public int neighbors(final int posX, final int posY) {
        int temp = 0;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if ((i != 0 || j != 0) && posX + i >= 0 && posY + j >= 0 && posX + i < this.grid.length && posY + j < this.grid[0].length && this.grid[posX + i][posY + j].isMine()) {
                    ++temp;
                }
            }
        }
        return temp;
    }

    public Field getGrid(final int x, final int y) {
        if (x > this.grid.length || y > this.grid[0].length) {
            return null;
        }
        return this.grid[x][y];
    }

}
