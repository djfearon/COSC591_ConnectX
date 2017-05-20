package edu.frostburg.cosc591_connectx;

import java.util.Random;
import java.util.Scanner;

public class ConnectFour {

    private static int x;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Random r = new Random(System.currentTimeMillis());
        Piece playerColor = null;
        Piece aiColor = null;

        System.out.println("Welcome to Connect X!");
        x = 0;
        //Do not allow games of Connect 0, 1, or 2.
        while (x < 3) {
            System.out.print("Please enter how many pieces you would like to connect: ");
            x = s.nextInt();
        }

        //Set the piece color for the player and the AI
        int turn = r.nextInt(2);
        if (turn == 0) {
            playerColor = Piece.RED;
            aiColor = Piece.BLACK;
        } else {
            playerColor = Piece.BLACK;
            aiColor = Piece.RED;
        }

        System.out.println("Your piece color is " + playerColor.color);

        AIPlayer ai = new AIPlayer(7, aiColor);

        Board board = new Board(x);
        boolean gameOver = false;
        boolean draw = false;
        boolean humanTurn = true;

        System.out.println(board);

        do {
            if (humanTurn) {
                int move = -1;
                while (!board.isValidMove(move)) {
                    System.out.print("Enter a column: ");
                    move = s.nextInt();
                }
                board.move(move, playerColor);
            } else {
                int aiMove = -1;
                while (aiMove == -1) {
                    Board copy = board.clone();
                    aiMove = ai.getMove(copy, 0);
                }
                board.move(aiMove, aiColor);
            }

            gameOver = board.isGameOver();
            draw = board.isDraw();
            humanTurn = !humanTurn;
            System.out.println(board);
        } while (!gameOver && !draw);

        if (gameOver) {
            if (humanTurn) {
                System.out.println("The AI won!");
            } else {
                System.out.println("You won!");
            }
        } else if (draw) {
            System.out.println("The game is a draw!");
        }
    }
}
