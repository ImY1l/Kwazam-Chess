import java.util.*;

// ====================== Board Class (Singleton) ======================
// Muhammad Faiz bin Ilyasa - 1221309435
class Board {
    private static Board instance;
    private final Piece[][] board;
    private static final int ROWS = 8;
    private static final int COLUMNS = 5;
    
    // Private constructor for Singleton pattern
    private Board() {
        board = new Piece[ROWS][COLUMNS];
        resetBoard();
    }
    
    // Get the singleton instance of the board
    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }
    
    //Get piece at specific position 
    public Piece getPieceAt(int row, int col) {
        return board[row][col];
    }
    
    // Reset the board to an empty state
    public void resetBoard() {
        for (int i = 0; i < ROWS; i++) {
            Arrays.fill(board[i], null);
        }
    }
    
    // Clear the board
    public void clearBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                board[row][col] = null; // Clear each cell of the board
            }
        }
    }

    // Place pieces on the board for both players
    public void placePieces(Player player1, Player player2) {
        resetBoard();
        
        for (Piece piece : player1.getPieces()) {
            if (!piece.isCaptured()) {
                int row = piece.getPosition()[0][0];
                int col = piece.getPosition()[0][1];
                board[row][col] = piece; 
            }
        }
        
        for (Piece piece : player2.getPieces()) {
            if (!piece.isCaptured()) {
                int row = piece.getPosition()[0][0];
                int col = piece.getPosition()[0][1];
                board[row][col] = piece;
            }
        }
        
        System.out.println("Board state after placing pieces:");
        System.out.println(this);
    }
    
    //Remove Piece
    public void removePiece(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLUMNS) {
            board[row][col] = null; // Remove the piece from the board
        }
    }
    
    // Check if the cell is occupied by another piece 
    public boolean isOccupied(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLUMNS) {
            return board[row][col] != null;
        }
        return false; // Out of bounds
    }
    
    // Prints the current state of the board
    public void printBoard() {
        System.out.println(this);
    }
    
    // Return a string representation of the board
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (board[i][j] == null) {
                    sb.append("[   ]"); // Empty cell
                } else {
                    sb.append("[").append(board[i][j].getClass().getSimpleName().substring(0, 3)).append("]");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}