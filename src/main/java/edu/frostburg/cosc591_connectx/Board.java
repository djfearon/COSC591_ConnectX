package edu.frostburg.cosc591_connectx;

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
    public static final int COLUMNS = 7;

    /**
     * The number of rows in the board
     */
    public static final int ROWS = 6;

    /**
     * The number of pieces in a row required to win the game
     */
    public static final int REQUIRED = 4;
    private Piece[][] board;
    private int[] size;
    private int totalSize;
    private int lastMove;

    /**
     * Create a new game board.
     */
    public Board() {
        board = new Piece[COLUMNS][ROWS];
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
    public boolean move(int column, Piece piece) {
        if (isValidMove(column)) {
            int row = size[column];
            board[column][row] = piece;
            lastMove = column;
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
        int row = size[col] - 1;
        Piece piece = board[col][row];
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
        int row = size[col] - 1;
        //Check diagonal (bottom-left to top-right)
        for (low = 0; col - low - 1 >= 0 && row - low - 1 >= 0 && board[col - low - 1][row - low - 1] == piece; ++low);
        for (high = 0; col + high + 1 < COLUMNS && row + high + 1 < ROWS && board[col + high + 1][row + high + 1] == piece; ++high);
        if (Math.abs(high - low) + 1 >= REQUIRED) {
            return true;
        }

        //Check diagonal (top-left to bottom-right)
        for (low = 0; col - low - 1 >= 0 && row + low + 1 < ROWS && board[col - low - 1][row + low + 1] == piece; ++low);
        for (high = 0; col + high + 1 < COLUMNS && row - high - 1 >= 0 && board[col + high + 1][row - high - 1] == piece; ++high);
        if (Math.abs(high - low) + 1 >= REQUIRED) {
            return true;
        }

        //Check horizontal direction
        for (low = col; low > 0 && board[low - 1][row] == piece; --low);
        for (high = col; high < COLUMNS - 1 && board[high + 1][row] == piece; ++high);
        if (Math.abs(high - low) + 1 >= REQUIRED) {
            return true;
        }

        //Check vertical direction        
        row = size[col] - 1;
        for (int i = row; i >= 0 && board[col][i] == piece; --i) {
            if (row - i + 1 == REQUIRED) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the game is a draw (tie)
     *
     * @return true if the game is a draw and false otherwise
     */
    public boolean isDraw() {
        return isBoardFilled() && !isWinningMove(lastMove, board[lastMove][size[lastMove] - 1]);
    }

    //Check if the board is completely filled
    private boolean isBoardFilled() {
        return totalSize == ROWS * COLUMNS;
    }

    //Checks if the move is valid
    private boolean isValidMove(int col) {
        return col >= 0 && col < Board.COLUMNS && size[col] < Board.ROWS;
    }
}
