/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.frostburg.cosc591_connectx;

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
        int move = minimax(board, depth, turn)[1];
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
    private int[] minimax(Board board, int depth, boolean aiTurn) {
        if (board.isGameOver() || board.isDraw()) {
            return gameOver(turn);
        } else if (depth == maxDepth) {
            return new int[]{eval(board, depth), -1};
        } else {
            int bestMove = -1;

            LinkedList<Integer> possibleMoves = getLegalMoves(board);
            LinkedList<Integer> scores = new LinkedList<>();

            if (aiTurn) {//Look ahead to minimize the human's chances of winning
                int bestScore = -10;
                turn = false;
                scores = new LinkedList();
                for (int i = 0; i < possibleMoves.size(); i++) {
                    int score = 0;
                    board.move(possibleMoves.get(i), Piece.RED, true);
                    score = minimax(board, depth + 1, turn)[0];
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
                int bestScore = 10;
                turn = true;
                scores = new LinkedList<>();
                for (int i = 0; i < possibleMoves.size(); i++) {
                    int score = 0;
                    board.move(possibleMoves.get(i), Piece.BLACK, false);
                    score = minimax(board, depth + 1, turn)[0];
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
            } else if(!min){
                if (s > best) {
                    best = s;
                }
            }
        }
        return best;
    }

    private int eval(Board board, int depth) {
        if (!turn) {
            return (maxDepth + 1) - depth;
        } else if (turn) {
            return depth - (maxDepth + 1);
        }
        return 0;
    }

    private int[] gameOver(boolean turn) {
        if (!turn) {
            return new int[]{-(maxDepth + 1), -1};
        } else {
            return new int[]{(maxDepth + 1), -1};
        }
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

    /*
     * Revokes a move that was made during an evaluation state.
     * 
     * @param board The state of the board after evaluating a move.
     * @param i The column on the board where the move was made.
     */
    private void undoMove(Board board, int i) {
        throw new UnsupportedOperationException("This operation is not yet supported.");
    }

}
