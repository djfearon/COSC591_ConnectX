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

    public static final int MAX_SCORE = 1000000;
    private boolean turn;
    private int maxDepth;
    private final Piece piece;
    private final Piece opponentPiece;
    private LinkedList<Integer> scores;

    /**
     * Create a new AI Player that performs MiniMax evaluations to choose the
     * best moves for winning a game of Connect x.
     *
     * @param md The maximum depth that the algorithm is allowed to look ahead.
     * @param p The piece color of the AIPlayer
     */
    public AIPlayer(int md, Piece p) {
        maxDepth = md;
        turn = true;//Starts with the AI's turn
        piece = p;//Set the piece color
        opponentPiece = piece == Piece.BLACK ? Piece.RED : Piece.BLACK;//Set the opponent's piece color
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
        if (depth == maxDepth) {
            return new int[]{eval(board, depth)[0], -1};
        } else {
            int bestMove = -1;

            LinkedList<Integer> possibleMoves = getLegalMoves(board);
            LinkedList<Integer> scores = new LinkedList<>();

            if (aiTurn) {//Look ahead to minimize the human's chances of winning
                int bestScore = -MAX_SCORE;
                scores = new LinkedList();
                for (int i = 0; i < possibleMoves.size(); i++) {

                    int score = 0;
                    int move = possibleMoves.get(i);
                    board.move(move, piece);
                    //Run the Min function on the simulated turn
                    score = minimax(board, depth + 1, possibleMoves.get(i), false)[0];
                    scores.add(score);
                    
                    //If there is a higher score, store it for returning
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = possibleMoves.get(scores.indexOf(bestScore));
                    }

                    //Undo the simulated move
                    board.undoMove(possibleMoves.get(i));
                }
                return new int[]{bestScore, bestMove};
            } else {//Look ahead to maximize the AI's chances of winning
                int bestScore = MAX_SCORE;
                scores = new LinkedList<>();
                for (int i = 0; i < possibleMoves.size(); i++) {

                    int score = 0;
                    int move = possibleMoves.get(i);
                    //Simulate moves that the player can make
                    board.move(move, opponentPiece);
                    //Take the max score of the simulated moves
                    score = minimax(board, depth + 1, possibleMoves.get(i), true)[0];
                    scores.add(score);
                    //If there is a new lower score, store it for returning
                    if (score < bestScore) {
                        bestScore = score;
                        bestMove = possibleMoves.get(scores.indexOf(bestScore));
                    }
                    board.undoMove(possibleMoves.get(i));
                }
                return new int[]{bestScore, bestMove};
            }
        }
    }

    /*
     * Evaluate the state of the board and return a score for deciding the best move.
     * 
     * @param board The current simulated state of the board.
     * @param depth The depth of recursion that this evaluation occurs.
     * @return The evaluated score for decision making.
     */
    private int[] eval(Board board, int depth) {
        if (board.isGameOver() && !turn) {
            return new int[]{MAX_SCORE - depth};
        } else if (board.isGameOver() && turn) {
            return new int[]{depth - MAX_SCORE};
        }
        int totalScore = 0;

        //Check vertical direction
        for (int row = 0; row <= Board.ROWS - Board.REQUIRED; ++row) {
            for (int col = 0; col < Board.COLUMNS; ++col) {
                int score = evaluatePosition(board, col, row, 0, 1);
                if (score == MAX_SCORE) {
                    return new int[]{MAX_SCORE - depth, -1};
                }
                if (score == -MAX_SCORE) {
                    return new int[]{depth - MAX_SCORE, -1};
                }
                totalScore += score;
            }
        }

        //Check horizontal direction
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col <= Board.COLUMNS - Board.REQUIRED; ++col) {
                int score = evaluatePosition(board, col, row, 1, 0);
                if (score == MAX_SCORE) {
                    return new int[]{MAX_SCORE - depth, -1};
                }
                if (score == -MAX_SCORE) {
                    return new int[]{depth - MAX_SCORE, -1};
                }
                totalScore += score;
            }
        }
        //Check diagonal (top-left to bottom-right)
        for (int row = 0; row <= Board.ROWS - Board.REQUIRED; ++row) {
            for (int col = 0; col <= Board.COLUMNS - Board.REQUIRED; ++col) {
                int score = evaluatePosition(board, col, row, 1, 1);
                if (score == MAX_SCORE) {
                    return new int[]{MAX_SCORE - depth, -1};
                }
                if (score == -MAX_SCORE) {
                    return new int[]{depth - MAX_SCORE, -1};
                }
                totalScore += score;
            }
        }

        //Check diagonal (bottom-left to top-right)
        for (int row = Board.REQUIRED - 1; row < Board.ROWS; ++row) {
            for (int col = 0; col <= Board.COLUMNS - Board.REQUIRED; ++col) {
                int score = evaluatePosition(board, col, row, 1, -1);
                if (score == MAX_SCORE) {
                    return new int[]{MAX_SCORE - depth, -1};
                }
                if (score == -MAX_SCORE) {
                    return new int[]{depth - MAX_SCORE, -1};
                }
                totalScore += score;
            }
        }

        return new int[]{totalScore, -1};
    }

    /*
     * Evaluate the state of the board after simulating a move.
     *
     * @param board The simulated state of the board without the move.
     * @param col The column index of the simulated move.
     * @param row The row index of the simulated move.
     * @param deltaX The value to increment the search in the x direction.
     * @param deltaY The value to increment the search in the y direction.
     * @return The score for the simulated move.
     */
    private int evaluatePosition(Board board, int col, int row, int deltaX, int deltaY) {
        int aiPieces = 0;
        int humanPieces = 0;
        for (int i = 0; i < Board.REQUIRED; ++i) {
            Piece current = board.getPiece(row, col);
            if (current == piece) {
                ++aiPieces;
            } else if (current == opponentPiece) {
                ++humanPieces;
            }
            row += deltaY;
            col += deltaX;
        }
        if (aiPieces == Board.REQUIRED) {
            return MAX_SCORE;
        } else if (humanPieces == Board.REQUIRED) {
            return -MAX_SCORE;
        } else {
            return aiPieces;
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

}
