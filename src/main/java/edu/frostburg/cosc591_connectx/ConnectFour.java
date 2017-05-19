package edu.frostburg.cosc591_connectx;

import java.util.Scanner;

public class ConnectFour {
    
    private static int x;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Welcome to Connect X!");
        x = 0;
        while (x < 3) {
            System.out.print("Please enter how many pieces you would like to connect: ");
            x = s.nextInt();
        }

        AIPlayer ai = new AIPlayer(10, Piece.RED);

        Board board = new Board(x);
        board.move(3, Piece.BLACK);
        System.out.println(board);
        int move = ai.getMove(boardCopy(board), 0);
        System.out.println("AI move: " + move);
        board.move(move, Piece.RED);
        System.out.println(board);
        board.move(2, Piece.BLACK);
        System.out.println(board);
        move = ai.getMove(boardCopy(board), 0);
        System.out.println("AI move: " + move);
        board.move(move, Piece.RED);
        board.move(0, Piece.BLACK);
        System.out.println(board);
        move = ai.getMove(boardCopy(board), 0);
        System.out.println("AI move: " + move);
        board.move(move, Piece.RED);
        System.out.println(board);
    }

    private static Board boardCopy(Board board) {
        Board p = new Board(x);
        for (int i = 0; i < board.ROWS; i++) {
            for (int j = 0; j < board.COLUMNS; j++) {
                p.board[i][j] = board.board[i][j];
            }
        }
        return p;
    }
}
