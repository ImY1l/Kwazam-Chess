import java.util.*;
import javax.swing.*;

// ====================== Player Class ======================
// Mohammed Amena Mohammed Abdulkarem - 1221305728
// Mohammed Yousef Mohammed Abdulkarem - 1221305727
class Player {
    private String name; //Name of the player 
    private String color; // "Red" or "Blue"
    private List<Piece> pieces; // List of pieces owned by the player
    private boolean isTurn; // If it is player's turn 
   
    // Constructor of player class 
    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.pieces = new ArrayList<>(); // Initialize pieces
        this.isTurn = false; // Initialize with turn as false
    }
    
    // Get the player's name
    public String getName() {
        return name;
    }
    
    // Get the player's color
    public String getColor() {
        return color;
    }
    
    // Get the player's pieces
    public List<Piece> getPieces() {
        return pieces;
    }
    
    //Check if its player turn 
    public boolean isTurn() {
        return isTurn;
    }
    
    // Set player turn 
    public void setTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }

    // Add a piece to the player's collection
    public void addPiece(Piece piece) {
        if (piece != null && piece.getPlayer() == this) {
            pieces.add(piece);
        }
    }
    
    // Add multiple pieces to the player's collection
     public void addPieces(Piece... pieces) {
        for (Piece piece : pieces) {
            addPiece(piece);
        }
    }
    
    // Remove a piece from the player's collection
    public void removePiece(Piece piece) {
        if (piece != null && piece.isCaptured()) {
        pieces.remove(piece); // Remove the captured piece from the player's list
        }
    }
    
    // Check if the player has lost 
    public boolean hasLost() {
        for (Piece piece : pieces) {
            if (piece.getType() == PieceType.SAU && piece.isCaptured()) {
                return true;
            }
        }
        return false;
    }
    
    // Attempt to move a piece to a new position
    // Mohammed Yousef Mohammed Abdulkarem - 1221305727
    public boolean makeMove(Piece piece, int[][] newPosition, Game game) {
        if (piece == null) {
            System.err.println("Piece is null.");
            return false;
        }
        if (piece.getPlayer() != this) {
            System.err.println("Piece does not belong to the current player.");
            return false;
        }
    
        boolean moveSuccessful = piece.move(newPosition, game.getAllPieces());
        if (moveSuccessful) {
            System.out.println("Move successful for " + piece.getType() + " to " + Arrays.deepToString(newPosition));
            return true;
        } else {
            System.err.println("Invalid move for " + piece.getType() + " to " + Arrays.deepToString(newPosition));
            return false;
        }
    }
    
    //Capture the opponent piece 
    // Mohammed Amena Mohammed Abdulkarem - 1221305728
    public void captureOpponentPiece(Piece opponentPiece, int[][] newPosition) {
        if (opponentPiece != null && !opponentPiece.getColor().equals(this.color)) {
            opponentPiece.capture(); // Mark the opponent's piece as captured
            Board.getInstance().removePiece(newPosition[0][0], newPosition[0][1]); // Remove from board
            opponentPiece.getPlayer().removePiece(opponentPiece); // Remove from opponent's piece list
        }
        
        // Check if the captured piece is a Sau (game over)
        if (opponentPiece.getType() == PieceType.SAU) {
            JOptionPane.showMessageDialog(null, "Game over! " + this.name + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            
            System.exit(0);
        }
    }
    
    public void setPieces(List<Piece> pieces) {
        this.pieces = new ArrayList<>(pieces); // Ensure modifications are safe
    }
  
}