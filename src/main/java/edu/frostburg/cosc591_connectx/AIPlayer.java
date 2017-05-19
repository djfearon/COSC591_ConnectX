/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.frostburg.cosc591_connectx;

import static edu.frostburg.cosc591_connectx.Board.COLUMNS;
import static edu.frostburg.cosc591_connectx.Board.REQUIRED;
import static edu.frostburg.cosc591_connectx.Board.ROWS;
import java.util.LinkedList;

/**
 *
 * @author Dakota Fearon
 */
public class AIPlayer {

    private boolean turn;
    private int maxDepth;
    private Piece piece;
    private LinkedList<Integer> scores;

    /**
     * Create a new AI Player that performs MiniMax evaluations to choose the
     * best moves for winning a game of Connect x.
     *
     * @param md The maximum depth that the algorithm is allowed to look ahead.
     */
    public AIPlayer(int md, Piece p) {
        maxDepth = md;
        turn = true;
        piece = p;
        scores = new LinkedList<>();
    }

    /*
     * Perform a MiniMax evaluation to return the best move for the turn.
     * 
     * @param board The current state of the board.
     * @param depth The current depth of evaluation.
     * @return The best possible move found.
     */
    public int getMove(Board board, int depth) {
        scores = new LinkedList<>();
        turn = true;
        int move = minimax(board, depth, -1, turn)[1];
        System.out.println("Chosen move: " + move);
        return move;
    }

    /*
     * Perform the MiniMax evalution to find which column is the best move.
     * 
     * @param board The current state of the board.
     * @param depth The current depth of evaluation.
     * @param aiTurn A flag for which player's turn it is.
     * @return The best possible move found.
     */
    private int[] minimax(Board board, int depth, int col, boolean aiTurn) {
        if (board.isGameOver() || board.isDraw()) {
            return gameOver(board, turn);
        } else if (depth == maxDepth) {
            return new int[]{evalTest(board, depth, col)[0], -1};
        } else {
            int bestMove = -1;

            LinkedList<Integer> possibleMoves = getLegalMoves(board);
            LinkedList<Integer> scores = new LinkedList<>();

            if (aiTurn) {//Look ahead to minimize the human's chances of winning
                int bestScore = -100;
                turn = false;
                scores = new LinkedList();
                for (int i = 0; i < possibleMoves.size(); i++) {
                    int score = 0;
                    board.move(possibleMoves.get(i), piece, true);
                    score = minimax(board, depth + 1, possibleMoves.get(i), turn)[0];
                    score -= depth;
                    scores.add(score);
                    if (scores.size() == possibleMoves.size() && depth == 0) {
                        System.out.println("PossibleMoves Max: " + possibleMoves);
                        System.out.println("Max Scores: " + scores);
                    }
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = possibleMoves.get(scores.indexOf(bestScore));
                    }
                    board.undoMove(possibleMoves.get(i));
                }
                bestScore = getBestScore(scores, false);
                bestMove = possibleMoves.get(scores.indexOf(bestScore));
                return new int[]{bestScore, bestMove};
            } else {//Look ahead to maximize the AI's chances of winning
                int bestScore = 100;
                turn = true;
                scores = new LinkedList<>();
                for (int i = 0; i < possibleMoves.size(); i++) {
                    int score = 0;
                    if (piece == piece.RED) {
                        board.move(possibleMoves.get(i), Piece.BLACK, true);
                    } else {
                        board.move(possibleMoves.get(i), Piece.RED, true);
                    }
                    score = minimax(board, depth + 1, possibleMoves.get(i), turn)[0];
                    score -= depth;
                    scores.add(score);
                    if (scores.size() == possibleMoves.size() && depth == 0) {
                        System.out.println("PossibleMoves Min: " + possibleMoves);
                        System.out.println("Min Scores: " + scores);
                    }
                    if (score < bestScore) {
                        bestScore = score;
                        bestMove = possibleMoves.get(scores.indexOf(bestScore));
                    }
                    board.undoMove(possibleMoves.get(i));
                }
                bestScore = getBestScore(scores, false);
                bestMove = possibleMoves.get(scores.indexOf(bestScore));
                return new int[]{bestScore, bestMove};
            }
        }
    }

    private int getBestScore(LinkedList<Integer> scores, boolean min) {
        int best = scores.get(0);
        for (int s : scores) {
            if (min) {
                if (s < best) {
                    best = s;
                }
            } else if (!min) {
                if (s > best) {
                    best = s;
                }
            }
        }
        return best;
    }

    private int[] eval(Board board, int depth) {
        if (board.isGameOver() && !turn) {
            return new int[]{(maxDepth + 1) - depth};
        } else if (board.isGameOver() && turn) {
            return new int[]{depth - (maxDepth + 1)};
        }
        int score = 0;
        Piece[][] b = board.board;
        int[] count = new int[Board.REQUIRED - 1];
        int[] size = board.getSize();
        //iterate over columns
        for (int col = 0; col < Board.COLUMNS; ++col) {
            for (int row = 0; row < size[col]; ++row) {
                Piece piece = b[col][row];

            }
        }
        return new int[]{score, -1};
    }
    
    private int[] evalTest(Board board, int depth, int col) {
        if (board.isGameOver() && !turn) {
            return new int[]{depth - 100};
        } else if (board.isGameOver() && turn) {
            return new int[]{100 - depth};
        }
        int score = 0;
        Piece[][] b = board.board;
        int low;
        int high;
        int[] size = board.getSize();
        int row = Board.ROWS - size[col];
        if(row == Board.ROWS) row -= 1;
        //Check diagonal (bottom-left to top-right)
        for (low = 0; col - low - 1 >= 0 && row - low - 1 >= 0 && b[row - low - 1][col - low - 1] == piece; ++low);
        for (high = 0; col + high + 1 < COLUMNS && row + high + 1 < ROWS && b[row + high + 1][col + high + 1] == piece; ++high);
        score += Math.abs(high - low) + 1;

        //Check diagonal (top-left to bottom-right)
        for (low = 0; col - low - 1 >= 0 && row + low + 1 < ROWS && b[row + low + 1][col - low - 1] == piece; ++low);
        for (high = 0; col + high + 1 < COLUMNS && row - high - 1 >= 0 && b[row - high - 1][col + high + 1] == piece; ++high);
        score += Math.abs(high - low) + 1 ;

        //Check horizontal direction
        for (low = col; low > 0 && b[row][low - 1] == piece; --low);
        for (high = col; high < COLUMNS - 1 && b[row][high + 1] == piece; ++high);
        score += Math.abs(high - low) + 1;

        //Check vertical direction        
        for (high = row; high < ROWS - 1 && b[high + 1][col] == piece; ++high);
        score += Math.abs(high - row) + 1;
        
        
        Piece tempPiece = null;
        if(piece == Piece.RED){
            tempPiece = Piece.BLACK;
        } else {
            tempPiece = Piece.RED;
        }
        
        for (low = 0; col - low - 1 >= 0 && row - low - 1 >= 0 && b[row - low - 1][col - low - 1] == tempPiece; ++low);
        for (high = 0; col + high + 1 < COLUMNS && row + high + 1 < ROWS && b[row + high + 1][col + high + 1] == tempPiece; ++high);
        score += Math.abs(high - low) + 1 + (REQUIRED - Math.abs(high - low));

        //Check diagonal (top-left to bottom-right)
        for (low = 0; col - low - 1 >= 0 && row + low + 1 < ROWS && b[row + low + 1][col - low - 1] == tempPiece; ++low);
        for (high = 0; col + high + 1 < COLUMNS && row - high - 1 >= 0 && b[row - high - 1][col + high + 1] == tempPiece; ++high);
        score += Math.abs(high - low) + 1 + (REQUIRED - Math.abs(high - low));

        //Check horizontal direction
        for (low = col; low > 0 && b[row][low - 1] ==tempPiece; --low);
        for (high = col; high < COLUMNS - 1 && b[row][high + 1] == tempPiece; ++high);
        score += Math.abs(high - low) + 1 + (REQUIRED - Math.abs(high - low));

        //Check vertical direction        
        for (high = row; high < ROWS - 1 && b[high + 1][col] == tempPiece; ++high);
        score += Math.abs(high - low) + 1 + (REQUIRED - Math.abs(high - low)); 
        return new int[]{score, -1};
    }
    
//    private int getScore(int number) {
//        switch(number) {
//            case 1:
//                return ;
//            case 2:
//        }
//    }

    private int[] gameOver(Board board, boolean turn) {
        if (board.isGameOver() && !turn) {
            return new int[]{100, -1};
        } else if (board.isGameOver() && turn) {
            return new int[]{-100, -1};
        }
        return new int[]{0, -1};
    }

    /*
     * Compile a list of all legal moves for a given turn.
     * 
     * @param board The current state of the board.
     * @return Return all legal moves for the current turn.
     */
    private LinkedList getLegalMoves(Board board) {
        LinkedList validMoves = new LinkedList();
        for (int i = 0; i < board.COLUMNS; i++) {
            if (board.isValidMove(i)) {
                validMoves.add(i);
            }
        }
        return validMoves;
    }

}
