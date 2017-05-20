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
        turn = true;
        piece = p;
        opponentPiece = piece == Piece.BLACK ? Piece.RED : Piece.BLACK;
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
            return new int[]{eval(board, depth)[0], -1};
        } else {
            int bestMove = -1;

            LinkedList<Integer> possibleMoves = getLegalMoves(board);
            LinkedList<Integer> scores = new LinkedList<>();

            if (aiTurn) {//Look ahead to minimize the human's chances of winning
                int bestScore = -MAX_SCORE;
                turn = false;
                scores = new LinkedList();
                for (int i = 0; i < possibleMoves.size(); i++) {
                    int score = 0;
                    int move = possibleMoves.get(i);
                    board.move(move, piece, true);
                    score = minimax(board, depth + 1, possibleMoves.get(i), turn)[0];
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
                int bestScore = MAX_SCORE;
                turn = true;
                scores = new LinkedList<>();
                for (int i = 0; i < possibleMoves.size(); i++) {
                    int score = 0;
                    int move = possibleMoves.get(i);
                    if (piece == Piece.RED) {
                        board.move(move, Piece.BLACK, true);
                    } else {
                        board.move(move, Piece.RED, true);
                    }
                    score = minimax(board, depth + 1, possibleMoves.get(i), turn)[0];
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
            return new int[]{-MAX_SCORE};
        } else if (board.isGameOver() && turn) {
            return new int[]{MAX_SCORE};
        }
        int totalScore = 0;

        //Check vertical direction
        for (int row = 0; row <= Board.ROWS - Board.REQUIRED; ++row) {
            for (int col = 0; col < Board.COLUMNS; ++col) {
                int score = evaluatePosition(board, col, row, 0, 1);
                if (score == MAX_SCORE) {
                    return new int[]{MAX_SCORE, -1};
                }
                if (score == -MAX_SCORE) {
                    return new int[]{-MAX_SCORE, -1};
                }
                totalScore += score;
            }
        }

        //Check horizontal direction
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col <= Board.COLUMNS - Board.REQUIRED; ++col) {
                int score = evaluatePosition(board, col, row, 1, 0);
                if (score == MAX_SCORE) {
                    return new int[]{MAX_SCORE, -1};
                }
                if (score == -MAX_SCORE) {
                    return new int[]{-MAX_SCORE, -1};
                }
                totalScore += score;
            }
        }
        //Check diagonal (top-left to bottom-right)
        for (int row = 0; row <= Board.ROWS - Board.REQUIRED; ++row) {
            for (int col = 0; col <= Board.COLUMNS - Board.REQUIRED; ++col) {
                int score = evaluatePosition(board, col, row, 1, 1);
                if (score == MAX_SCORE) {
                    return new int[]{MAX_SCORE, -1};
                }
                if (score == -MAX_SCORE) {
                    return new int[]{-MAX_SCORE, -1};
                }
                totalScore += score;
            }
        }

        //Check diagonal (bottom-left to top-right)
        for (int row = Board.REQUIRED - 1; row < Board.ROWS; ++row) {
            for (int col = 0; col <= Board.COLUMNS - Board.REQUIRED; ++col) {
                int score = evaluatePosition(board, col, row, 1, -1);
                if (score == MAX_SCORE) {
                    return new int[]{MAX_SCORE, -1};
                }
                if (score == -MAX_SCORE) {
                    return new int[]{-MAX_SCORE, -1};
                }
                totalScore += score;
            }
        }

        return new int[]{totalScore, -1};
    }

    private int evaluatePosition(Board board, int col, int row, int deltaX, int deltaY) {
        int aiPieces = 0;
        int humanPieces = 0;
        for (int i = 0; i < Board.REQUIRED; ++i) {
            if (board.board[row][col] == piece) {
                ++aiPieces;
            } else if (board.board[row][col] == opponentPiece) {
                ++humanPieces;
            }
            row += deltaY;
            col += deltaX;
        }
        if (aiPieces == Board.REQUIRED) {
            return MAX_SCORE;
        } else if (humanPieces == Board.REQUIRED) {
            return -MAX_SCORE;
//        } else if (humanPieces != 0) {
//            return 0;
        } else {
            return aiPieces;
        }
    }

    private int[] gameOver(Board board, boolean turn) {
        if (board.isGameOver() && !turn) {
            return new int[]{MAX_SCORE, -1};
        } else if (board.isGameOver() && turn) {
            return new int[]{-MAX_SCORE, -1};
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
