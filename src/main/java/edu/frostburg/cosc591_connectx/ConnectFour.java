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

        AIPlayer ai = new AIPlayer(5, Piece.RED);

        Board board = new Board(x);

        do{
            System.out.println(board);
            System.out.print("Enter a column: ");
            int move = s.nextInt();
            board.move(move, Piece.BLACK, false);

            System.out.println(board);

            int aiMove = -1;
            while(aiMove == -1){
                aiMove = ai.getMove(boardCopy(board), 0);
            }
            board.move(aiMove, Piece.RED, false);
            System.out.println(board);
        }while (!board.isGameOver() || !board.isDraw()) ;
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
