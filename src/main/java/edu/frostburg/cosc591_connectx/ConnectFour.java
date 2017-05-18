package edu.frostburg.cosc591_connectx;

import java.util.Scanner;

public class ConnectFour {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Welcome to Connect X!");
        int x = 0;
        while (x < 3) {
            System.out.print("Please enter how many pieces you would like to connect: ");
            x = s.nextInt();
        }

        Board board = new Board(x);
        System.out.println(board);
        board.move(3, Piece.RED);
        System.out.println(board);
        board.move(3, Piece.BLACK);
        System.out.println(board);
    }
}
