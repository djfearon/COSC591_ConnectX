package edu.frostburg.cosc591_connectx;

public class ConnectFour {

    public static void main(String[] args) {

        Board board = new Board();
        System.out.println(board);
        board.move(3, Piece.RED);
        System.out.println(board);
        board.move(3, Piece.BLACK);
        System.out.println(board);

    }
}
