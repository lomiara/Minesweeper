package Minesweeper;

public class SystemInfo {
    private static SystemInfo instance = new SystemInfo();
    private int mines = 10;
    private int rows = 10;
    private int columns = 10;
    private int mineSize = 20;
    private int range = 2;
    private double height = rows * (mineSize+range) + 64 + 3;
    private double width = columns * (mineSize+range) + 3;

    private SystemInfo(){}

    public static SystemInfo getInstance() {
        return instance;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
        height = rows * (mineSize+range) + 64;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
        width = columns * (mineSize+range);
    }

    public int getMineSize() {
        return mineSize;
    }

    public void setMineSize(int mineSize) {
        this.mineSize = mineSize;
    }

    public int getRange(){
        return range;
    }
}
