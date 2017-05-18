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

    private int maxDepth;
    private Piece piece;

    /**
     * Create a new AI Player that performs MiniMax evaluations to choose the 
     * best moves for winning a game of Connect x.
     * 
     * @param md The maximum depth that the algorithm is allowed to look ahead.
     */
    public AIPlayer(int md) {
        maxDepth = md;
    }
    
    /*
     * Perform a MiniMax evaluation to return the best move for the turn.
     * 
     * @param board The current state of the board.
     * @param depth The current depth of evaluation.
     * @return The best possible move found.
     */
    public int getMove(Board board, int depth){
        return minimax(board, depth, true);
    }

    /*
     * Perform the MiniMax evalution to find which column is the best move.
     * 
     * @param board The current state of the board.
     * @param depth The current depth of evaluation.
     * @param aiTurn A flag for which player's turn it is.
     * @return The best possible move found.
     */
    private int minimax(Board board, int depth, boolean aiTurn) {
        int bestMove;
        int bestScore = -100;
        
        LinkedList<Integer> possibleMoves = getLegalMoves(board);
        LinkedList<Integer> scores = new LinkedList<>();
        
        if(aiTurn){//Look ahead to minimize the human's chances of winning
            int score = min(board, depth);
            scores.add(score);
            bestScore = Math.max(score, bestScore);
        } else {//Look ahead to maximize the AI's chances of winning
            int score = max(board, depth);
            scores.add(score);
            bestScore = Math.min(score, bestScore);
        }
        
        //Make the best move
        int moveIndex = scores.indexOf(bestScore);
        bestMove = possibleMoves.get(moveIndex);
        return bestMove;
    }

    /*
     * Perform evaluation to find the best move to minimize the player's success.
     * 
     * @param board The current state of the board.
     * @param depth The current depth of evaluation.
     * @return The best score found.
     */
    private int min(Board board, int depth) {
        //if the game is over, return the ending value (positive or negative based on who won)
        //Otherwise
        
        LinkedList<Integer> possibleMoves = new LinkedList<>();
        LinkedList<Integer> scores = new LinkedList<>();
        
        int bestScore = -100;
        possibleMoves = getLegalMoves(board);
        //For each legal move in turn
        for (int i : possibleMoves) {
            //Make the current move
            board.move(i, piece);
            //Record the current score reported by min()
            int score = 0;
            if (depth < maxDepth) {
                score = max(board, depth);
                scores.add(score);
            }
            //if the score is greater than the best, the best is now this
            if (score < bestScore) {
                bestScore = score;
            }
            //undo the move that was made
            undoMove(board, i);
        }
        //return the best score
        return bestScore;
    }

    /*
     * Perform evaluation to find the best move to maximize the AI's success.
     * 
     * @param board The current state of the board.
     * @param depth The current depth of evaluation.
     * @return The best score found.
     */
    private int max(Board board, int depth) {
        //if the game is over, return the ending value (positive or negative based on who won)
        //Otherwise
        
        LinkedList<Integer> possibleMoves = new LinkedList<>();
        LinkedList<Integer> scores = new LinkedList<>();
        
        int bestScore = 100;
        possibleMoves = getLegalMoves(board);
        //For each legal move in turn
        for (int i : possibleMoves) {
            //Make the current move
            board.move(i, piece);
            //Record the current score reported by min()
            int score = 0;
            if (depth < maxDepth) {
                score = min(board, depth);
                scores.add(score);
            }
            //if the score is greater than the best, the best is now this
            if (score > bestScore) {
                bestScore = score;
            }
            //undo the move that was made
            undoMove(board, i);
        }
        //return the best score
        return bestScore;
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
