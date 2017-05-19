package edu.frostburg.cosc591_connectx;

import java.util.LinkedList;

/**
 * The game board for ConnectX
 *
 * @author Kerwin Yoder
 * @version 2017.04.29
 */
public class Board {

    /**
     * The number of columns in the board
     */
    public static int COLUMNS;

    /**
     * The number of rows in the board
     */
    public static int ROWS;

    /**
     * The number of pieces in a row required to win the game
     */
    public static int REQUIRED;
    public Piece[][] board;
    private int[] size;
    private int totalSize;
    private int lastMove;

    /**
     * Create a new game board.
     */
    public Board(int x) {

        REQUIRED = x;//Set the number of required pieces in a row
        ROWS = x + 2;//Set the number of rows
        COLUMNS = x + 3;//Set the number of columns

        board = new Piece[ROWS][COLUMNS];
        size = new int[COLUMNS];
        totalSize = 0;
        lastMove = -1;
    }

    /**
     * Inserts a piece at the given column
     *
     * @param column the column in which to insert the piece (0 - 7)
     * @param piece the piece to insert
     * @return true if a piece was inserted and false if the column is already
     * filled
     */
    public boolean move(int column, Piece piece, boolean simulated) {
        if (isValidMove(column)) {
            int row = Board.ROWS - 1 - size[column];
            board[row][column] = piece;
            if (!simulated) {
                lastMove = column;
            }
            ++size[column];
            ++totalSize;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the game is over based on the last move
     *
     * @return true if the game is over and false otherwise
     */
    public boolean isGameOver() {
        // if no pieces have been moved, the game is not over
        if (totalSize == 0) {
            return false;
        }

        //if the board is filled, the game is over
        if (isBoardFilled()) {
            return true;
        }

        //if the last move is a winning move, the game is over
        int col = lastMove;
        if(col == -1){
            col = 0;
        }
        int row = Board.ROWS - size[col];
        if (row == Board.ROWS) {
            row = Board.ROWS - 1;
        } else if(row == -1){
            row = 0;
        }
        Piece piece = board[row][col];
        return isWinningMove(col, piece);
    }

    /*
     * Checks if a move to the given column is a winning move
     *
     * @param col the column of the move to check
     * @param piece the type of piece making the move
     * @return true if a move by the given piece to the given column would win the game
     */
    private boolean isWinningMove(int col, Piece piece) {
        int low;
        int high;
        int row = Board.ROWS - size[col];
        if(row == Board.ROWS) row -= 1;
        //Check diagonal (bottom-left to top-right)
        for (low = 0; col - low - 1 >= 0 && row - low - 1 >= 0 && board[row - low - 1][col - low - 1] == piece; ++low);
        for (high = 0; col + high + 1 < COLUMNS && row + high + 1 < ROWS && board[row + high + 1][col + high + 1] == piece; ++high);
        if (Math.abs(high - low) + 1 >= REQUIRED) {
            return true;
        }

        //Check diagonal (top-left to bottom-right)
        for (low = 0; col - low - 1 >= 0 && row + low + 1 < ROWS && board[row + low + 1][col - low - 1] == piece; ++low);
        for (high = 0; col + high + 1 < COLUMNS && row - high - 1 >= 0 && board[row - high - 1][col + high + 1] == piece; ++high);
        if (Math.abs(high - low) + 1 >= REQUIRED) {
            return true;
        }

        //Check horizontal direction
        for (low = col; low > 0 && board[row][low - 1] == piece; --low);
        for (high = col; high < COLUMNS - 1 && board[row][high + 1] == piece; ++high);
        if (Math.abs(high - low) + 1 >= REQUIRED) {
            return true;
        }

        //Check vertical direction        
        for (high = row; high < ROWS - 1 && board[high + 1][col] == piece; ++high);
        if (Math.abs(high - row) + 1 >= REQUIRED) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the game is a draw (tie)
     *
     * @return true if the game is a draw and false otherwise
     */
    public boolean isDraw() {
        return isBoardFilled() && !isWinningMove(lastMove, board[Board.ROWS - size[lastMove]][lastMove]);
    }

    //Check if the board is completely filled
    private boolean isBoardFilled() {
        return totalSize == ROWS * COLUMNS;
    }

    //Checks if the move is valid
    public boolean isValidMove(int col) {
        return col >= 0 && col < Board.COLUMNS && size[col] < Board.ROWS;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        //Add the top edge of the board
        for (int i = 0; i < COLUMNS * 2; ++i) {
            if (i % 2 == 0) {
                builder.append(" ");
            } else {
                builder.append("_");

            }
        }
        builder.append("\r\n");

        //Add the rest of the board
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                builder.append('|');
                Piece piece = board[i][j];
                if (piece != null) {
                    builder.append(piece.color);
                } else {
                    builder.append("_");
                }
            }
            builder.append("|\r\n");
        }
        return builder.toString();
    }

    public void undoMove(int column) {
        int row = Board.ROWS - 1 - size[column];
        if (row == -1) {
            row = 0;
        }
        board[row][column] = null;
        --size[column];
        --totalSize;
    }
}
