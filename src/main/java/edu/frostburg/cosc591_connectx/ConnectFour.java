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
        boolean gameOver = false;
        boolean draw = false;
        boolean humanTurn = true;
        
        System.out.println(board);

        do {
            if (humanTurn) {
                System.out.print("Enter a column: ");
                int move = s.nextInt();
                board.move(move, Piece.BLACK, false);
            } else {
                int aiMove = -1;
                while (aiMove == -1) {
                    Board copy = boardCopy(board);
                    copy.setSize(board.getSize());
                    aiMove = ai.getMove(copy, 0);
                }
                board.move(aiMove, Piece.RED, false);
            }

            gameOver = board.isGameOver();
            draw = board.isDraw();
            humanTurn = !humanTurn;
            System.out.println(board);
        } while (!gameOver && !draw);
        
        if(gameOver){
            if(humanTurn){
                System.out.println("The AI won!");
            } else {
                System.out.println("You won!");
            }
        } else if (draw){
            System.out.println("The game is a draw!");
        }
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
