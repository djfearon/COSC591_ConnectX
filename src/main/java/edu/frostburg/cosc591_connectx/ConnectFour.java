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
            System.out.println("Games can consist of chaining 3 or more pieces");
            System.out.print("Please enter how many pieces you would like to connect: ");
            x = s.nextInt();
        }

        //Set the piece color for the player and the AI
        boolean humanTurn = false;
        int turn = r.nextInt(2);
        //Make the player play red, let them go first
        if (turn == 0) {
            playerColor = Piece.RED;
            aiColor = Piece.BLACK;
            humanTurn = true;
        } else {//Make the player play black, AI goes first
            playerColor = Piece.BLACK;
            aiColor = Piece.RED;
        }

        //Notify the player of their piece color
        System.out.println("Your piece color is " + playerColor.color);

        //Create a new AI player with a 7 turn look-ahead
        AIPlayer ai = new AIPlayer(7, aiColor);

        //Generate a new board based on the required size of the piece chain
        Board board = new Board(x);
        boolean gameOver = false;
        boolean draw = false;

        System.out.println(board);

        //Loop and alternate turns until the game is over or there is a draw
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

        //Report which player won, or if the game was a draw
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
