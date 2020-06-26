package Minesweeper;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    Random random = new Random();
    int prevRow, prevCol, startRow, startCol, totalCells, totalFlags, time;
    java.util.Timer timer;
    boolean started, ended;
    double mouseX, mouseY;
    Cell[][] grid;
    private Stage stage;
    SystemInfo info;
    javafx.scene.image.Image smiley, sadSmiley;
    @FXML
    ImageView Image;
    @FXML
    AnchorPane tPane, parentPane, board;
    @FXML
    Label StatusText, You, Mines, Timer;

    public Controller() throws FileNotFoundException {
        smiley = new Image(new FileInputStream("src/Minesweeper/smiley.png"));
        sadSmiley = new Image(new FileInputStream("src/Minesweeper/sadFace.png"));
    }

    @FXML
    public void initialize() {
        Image.setImage(smiley);
        info = SystemInfo.getInstance();
        totalCells = info.getColumns() * info.getRows();
        parentPane.setPrefSize(info.getWidth(), info.getHeight());
        tPane.setPrefWidth(parentPane.getPrefWidth());
        board.setPrefWidth(parentPane.getPrefWidth());
        Image.setLayoutX(tPane.getPrefWidth() / 2 - getSize(Image) / 2);
        grid = new Cell[info.getRows()][info.getColumns()];
        for (int i = 0; i < info.getRows(); i++) {
            for (int j = 0; j < info.getColumns(); j++) {
                grid[i][j] = new Cell();
                grid[i][j].setLayoutY(info.getRange() + i * (info.getMineSize() + info.getRange()));
                grid[i][j].setLayoutX(info.getRange() + j * (info.getMineSize() + info.getRange()));
                grid[i][j].createRect(info.getMineSize(), info.getMineSize());
            }
        }
        initBoard();
    }

    public void drawAll() {
        for (int i = 0; i < info.getRows(); i++) {
            for (int j = 0; j < info.getColumns(); j++) {
                if (grid[i][j].isHidden()) {
                    if (grid[i][j].isMine()) {
                        grid[i][j].drawMine();
                    } else {
                        if (grid[i][j].getNeighbours() == 0) {
                            grid[i][j].drawVoidCell();
                        } else {
                            grid[i][j].drawText();
                        }
                    }
                }
                if (grid[i][j].isFlag() && !grid[i][j].isMine()) {
                    grid[i][j].setFill(Color.RED);
                }
            }
        }
    }

    public void initBoard() {
        board.getChildren().clear();
        for (int i = 0; i < info.getRows(); i++) {
            for (int j = 0; j < info.getColumns(); j++) {
                grid[i][j].setFill(Color.DARKGREY);
                board.getChildren().addAll(grid[i][j]);
            }
        }
    }

    @FXML
    public void smileyClicked(MouseEvent mouseEvent) {
        timer.cancel();
        timer.purge();
        started = false;
        startNewGame();
    }

    public void startNewGame() {
        time = 0;
        timer = new Timer();
        Timer.setText("0");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    time++;
                    Timer.setText(Integer.toString(time));
                });
            }
        }, 0, 1000);
        Mines.setText(Integer.toString(info.getMines()));
        ended = false;
        grid = new Cell[info.getRows()][info.getColumns()];
        Image.setImage(smiley);
        You.setText("");
        StatusText.setText("");
        for (int i = 0; i < info.getRows(); i++) {
            for (int j = 0; j < info.getColumns(); j++) {
                grid[i][j] = new Cell();
                grid[i][j].setLayoutY(info.getRange() + i * (info.getMineSize() + info.getRange()));
                grid[i][j].setLayoutX(info.getRange() + j * (info.getMineSize() + info.getRange()));
                grid[i][j].createRect(info.getMineSize(), info.getMineSize());
            }
        }
        if (started) {
            placeMines(grid);
            calculateNeighbours();
        }
        initBoard();
    }

    @FXML
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.isSecondaryButtonDown()) {
            if (!grid[getRow()][getColumn()].isFlag()) {
                if (grid[getRow()][getColumn()].isHidden()) {
                    if (Integer.parseInt(Mines.getText()) != 0) {
                        grid[getRow()][getColumn()].drawFlag();
                        grid[getRow()][getColumn()].setFlag(true);
                        Mines.setText(Integer.toString(Integer.parseInt(Mines.getText()) - 1));
                    }
                }
            } else {
                Mines.setText(Integer.toString(Integer.parseInt(Mines.getText()) + 1));
                grid[getRow()][getColumn()].getChildren().clear();
                grid[getRow()][getColumn()].createRect(info.getMineSize(), info.getMineSize());
                grid[getRow()][getColumn()].setFill(Color.DARKGREY);
                grid[getRow()][getColumn()].setFlag(false);
                grid[getRow()][getColumn()].setHidden(true);
            }
            checkGameStatus();
            return;
        }
        if (!started) {
            started = true;
            startRow = getRow();
            startCol = getColumn();
            startNewGame();
        } else {
            startRow = -1;
            startCol = -1;
        }
        if (getRow() != -1 | getColumn() != -1) {
            if (grid[getRow()][getColumn()].isMine()) {
                grid[getRow()][getColumn()].setFill(Color.RED);
                grid[getRow()][getColumn()].drawMine();
            } else {
                if (grid[getRow()][getColumn()].getNeighbours() == 0) {
                    findVoidCells(getRow(), getColumn());
                } else {
                    grid[getRow()][getColumn()].drawText();
                }
            }
        }
        checkGameStatus();
    }

    public void highlight(int row, int col) {
        if (row == -1 | col == -1) {
            if (grid[prevRow][prevCol].isHidden()) {
                grid[prevRow][prevCol].setFill(Color.DARKGREY);
            } else {
                if (grid[prevRow][prevCol].getNeighbours() == 0 && !grid[prevRow][prevCol].isMine()) {
                    grid[prevRow][prevCol].drawVoidCell();
                } else {
                    grid[prevRow][prevCol].setFill(Color.DARKGREY);
                }
            }
        } else {
            if (prevRow == row && prevCol == col) {
                return;
            } else {
                if (grid[prevRow][prevCol].isHidden()) {
                    grid[prevRow][prevCol].setFill(Color.DARKGREY);
                } else {
                    if (grid[prevRow][prevCol].getNeighbours() == 0 && !grid[prevRow][prevCol].isMine()) {
                        grid[prevRow][prevCol].drawVoidCell();
                    } else {
                        grid[prevRow][prevCol].setFill(Color.DARKGREY);
                    }
                }
                grid[row][col].setFill(Color.web("#66ccff"));
            }
            prevCol = col;
            prevRow = row;
        }
    }

    @FXML
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (!ended) {
            highlight(getRow(), getColumn());
        }
    }

    @FXML
    public void mouseExitedBoard(MouseEvent mouseEvent) {
        mouseX = -100;
        mouseY = -100;
        if (grid[prevRow][prevCol].isHidden()) {
            grid[prevRow][prevCol].setFill(Color.DARKGREY);
        } else {
            if (grid[prevRow][prevCol].getNeighbours() == 0 && !grid[prevRow][prevCol].isMine()) {
                grid[prevRow][prevCol].drawVoidCell();
            } else {
                grid[prevRow][prevCol].setFill(Color.DARKGREY);
            }
        }
        if (StatusText.getText().equals("LOST")) {
            grid[prevRow][prevCol].setFill(Color.RED);
        }
    }

    public void changeScene(int columns, int rows, int mines) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Controller controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
        info.setColumns(columns);
        info.setRows(rows);
        info.setMines(mines);
    }

    public double getSize(ImageView img) {
        return Math.min(img.getFitHeight(), img.getFitWidth());
    }

    public void checkGameStatus() {
        int total = 0;
        totalFlags = Integer.parseInt(Mines.getText());
        for (int i = 0; i < info.getRows(); i++) {
            for (int j = 0; j < info.getColumns(); j++) {
                if (!grid[i][j].isHidden()) {
                    if (grid[i][j].isMine() && !grid[i][j].isFlag()) {
                        gameLost();
                        return;
                    }
                    total++;
                }
            }
        }
        if (total >= totalCells - totalFlags) {
            gameWon();
        }
    }

    public void gameWon() {
        timer.cancel();
        timer.purge();
        started = false;
        ended = true;
        You.setTextFill(Color.web("#00cc00"));
        StatusText.setTextFill(Color.web("#00cc00"));
        You.setText("YOU");
        StatusText.setText("WON");
    }

    public void gameLost() {
        timer.cancel();
        timer.purge();
        started = false;
        ended = true;
        Image.setImage(sadSmiley);
        drawAll();
        You.setTextFill(Color.RED);
        StatusText.setTextFill(Color.RED);
        You.setText("YOU");
        StatusText.setText("LOST");
    }

    public void placeMines(Cell[][] cells) {
        for (int i = 0; i < info.getMines(); ) {
            int X = random.nextInt(info.getRows());
            int Y = random.nextInt(info.getColumns());
            if (!cells[X][Y].isMine() && (X != startRow || Y != startCol)) {
                cells[X][Y].setMine(true);
                i++;
            }
        }
    }

    public void calculateNeighbours() {
        for (int i = 0; i < info.getRows(); i++) {
            for (int j = 0; j < info.getColumns(); j++) {
                neighbours(i, j);
            }
        }
    }

    public void neighbours(int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i != row || j != col) {
                    if (withinGrid(i, j)) {
                        if (grid[i][j].isMine()) {
                            grid[row][col].setNeighbours(grid[row][col].getNeighbours() + 1);
                        }
                    }
                }
            }
        }
    }

    private boolean withinGrid(int X, int Y) {
        return Y < info.getColumns() && X < info.getRows() && X >= 0 && Y >= 0;
    }

    public int getRow() {
        int tmp = (int) ((mouseY - info.getRange() * 2) / (info.getMineSize() + info.getRange()));
        if (tmp < 0 | tmp > info.getRows()) {
            return -1;
        } else {
            return tmp;
        }
    }

    public int getColumn() {
        int tmp = (int) (mouseX - info.getRange() * 2) / (info.getMineSize() + info.getRange());
        if (tmp < 0 | tmp > info.getColumns()) {
            return -1;
        } else {
            return tmp;
        }
    }

    public void findVoidCells(int row, int col) {
        if (grid[row][col].isHidden()) {
            if (grid[row][col].getNeighbours() != 0) {
                grid[row][col].drawText();
                return;
            }
            grid[row][col].drawVoidCell();
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (withinGrid(i, j)) {
                        findVoidCells(i, j);
                    }
                }
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
