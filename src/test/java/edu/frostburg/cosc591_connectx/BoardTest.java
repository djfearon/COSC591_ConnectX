package edu.frostburg.cosc591_connectx;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the Board class.
 *
 * @author Kerwin Yoder
 * @version 2017.04.29
 */
public class BoardTest {

    /**
     * Test of move method, of class Board.
     */
    @Test
    public void testMove() {
        Board board = new Board(4);

        //Test lower column bound
        boolean result = board.move(-1, Piece.BLACK);
        assertFalse(result);

        //Test each valid column index
        for (int i = 0; i < Board.COLUMNS; ++i) {
            result = board.move(i, Piece.BLACK);
            assertTrue(result);
        }

        //Test upper column bound
        result = board.move(Board.COLUMNS, Piece.BLACK);
        assertFalse(result);
        for (int i = 1; i < Board.ROWS; ++i) {
            result = board.move(i, Piece.BLACK);
            assertTrue(result);
        }

        //Test upper row bound
        result = board.move(Board.COLUMNS, Piece.BLACK);
        assertFalse(result);
    }

    /**
     * Test of isGameOver method, of class Board.
     */
    @Test
    public void testIsGameOver() {
        Board board = new Board(4);

        //Test with no moves
        boolean result = board.isGameOver();
        assertFalse(result);

        //Test vertical case
        for (int i = 0; i < Board.REQUIRED - 1; ++i) {
            board.move(4, Piece.RED);
            result = board.isGameOver();
            assertFalse(result);
        }
        board.move(4, Piece.RED);
        result = board.isGameOver();
        assertTrue(result);

        //Test horizontal case
        board = new Board(4);
        for (int i = 0; i < Board.REQUIRED - 1; ++i) {
            board.move(i, Piece.RED);
            result = board.isGameOver();
            assertFalse(result);
        }
        board.move(Board.REQUIRED - 1, Piece.RED);
        result = board.isGameOver();
        assertTrue(result);

        //Test diagonal (bottom-left to top-right) case
        board = new Board(4);
        board.move(0, Piece.RED);
        assertFalse(board.isGameOver());
        board.move(1, Piece.BLACK);
        assertFalse(board.isGameOver());
        board.move(1, Piece.RED);
        assertFalse(board.isGameOver());
        board.move(2, Piece.BLACK);
        assertFalse(board.isGameOver());
        board.move(2, Piece.RED);
        assertFalse(board.isGameOver());
        board.move(3, Piece.BLACK);
        assertFalse(board.isGameOver());
        board.move(2, Piece.RED);
        assertFalse(board.isGameOver());
        board.move(3, Piece.BLACK);
        assertFalse(board.isGameOver());
        board.move(3, Piece.RED);
        assertFalse(board.isGameOver());
        board.move(5, Piece.BLACK);
        assertFalse(board.isGameOver());
        board.move(3, Piece.RED);
        result = board.isGameOver();
        assertTrue(result);

        //Test diagonal (top-left to bottom-right) case
        board = new Board(4);
        board.move(4, Piece.RED);
        assertFalse(board.isGameOver());
        board.move(3, Piece.BLACK);
        assertFalse(board.isGameOver());
        board.move(3, Piece.RED);
        assertFalse(board.isGameOver());
        board.move(2, Piece.BLACK);
        assertFalse(board.isGameOver());
        board.move(2, Piece.RED);
        assertFalse(board.isGameOver());
        board.move(1, Piece.BLACK);
        assertFalse(board.isGameOver());
        board.move(2, Piece.RED);
        assertFalse(board.isGameOver());
        board.move(1, Piece.BLACK);
        assertFalse(board.isGameOver());
        board.move(1, Piece.RED);
        assertFalse(board.isGameOver());
        board.move(6, Piece.BLACK);
        assertFalse(board.isGameOver());
        board.move(1, Piece.RED);
        result = board.isGameOver();
        assertTrue(result);
    }

    /**
     * Test of isDraw method, of class Board.
     */
    @Test
    public void testIsDraw() {
        Board board = new Board(4);

        //Test empty board
        boolean result = board.isDraw();
        assertFalse(result);

        //Test draw
        for (int i = 0; i < Board.COLUMNS; i += 2) {
            for (int j = 0; j < 3; ++j) {
                board.move(i, Piece.RED);
            }
            for (int j = 0; j < 3; ++j) {
                board.move(i, Piece.BLACK);
            }
        }
        for (int i = 1; i < Board.COLUMNS; i += 2) {
            for (int j = 0; j < 3; ++j) {
                board.move(i, Piece.BLACK);
            }
            for (int j = 0; j < 3; ++j) {
                board.move(i, Piece.RED);
            }
        }
        result = board.isDraw();
        assertTrue(result);

        //Test winning on last move
        board = new Board(4);
        for (int i = 0; i < Board.COLUMNS - 2; i += 2) {
            for (int j = 0; j < 3; ++j) {
                board.move(i, Piece.RED);
            }
            for (int j = 0; j < 3; ++j) {
                board.move(i, Piece.BLACK);
            }
        }
        for (int i = 1; i < Board.COLUMNS - 2; i += 2) {
            for (int j = 0; j < 3; ++j) {
                board.move(i, Piece.BLACK);
            }
            for (int j = 0; j < 3; ++j) {
                board.move(i, Piece.RED);
            }
        }
        for (int i = 0; i < 3; ++i) {
            board.move(Board.COLUMNS - 2, Piece.BLACK);
            board.move(Board.COLUMNS - 2, Piece.RED);
        }
        board.move(Board.COLUMNS - 2, Piece.RED);
        board.move(Board.COLUMNS - 2, Piece.BLACK);
        board.move(Board.COLUMNS - 2, Piece.RED);
        board.move(Board.COLUMNS - 1, Piece.BLACK);
        board.move(Board.COLUMNS - 1, Piece.RED);
        board.move(Board.COLUMNS - 1, Piece.BLACK);
        result = board.isDraw();
        assertFalse(result);
    }
}
