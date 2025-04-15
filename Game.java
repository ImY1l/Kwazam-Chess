import java.util.*;
import javax.swing.*;

// ====================== Game Class ======================
// Mohammed Amena Mohammed Abdulkarem - 1221305728
// Mohammed Yousef Mohammed Abdulkarem - 1221305727
// Gavin Yee Hou Jien - 1221103458
// Muhammad Faiz bin Ilyasa - 1221309435
class Game {
    private Board board;
    private List<Player> players;
    private Player currentPlayer;
    private int turnCount;
    private boolean gameOver;
    private List<GameObserver> observers = new ArrayList<>(); // For Observer pattern
    private GameView gameView;
    
    //Game constructor
    public Game(Board board, List<Player> players) {
        if (players.size() != 2) {
            throw new IllegalArgumentException("Game must have two players");
        }
        this.board = board;
        this.players = players;
        this.turnCount = 1;
        this.currentPlayer = players.get(0);  
        this.gameOver = false;
    }

    //Set the game when start play
    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }
    
    // Register observers GUI
    public void registerObserver(GameObserver observer) {
        observers.add(observer);
    }

    // Notify observers of game state changes
    private void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update(this);
        }
    }

    // Check if the game is over
    public boolean isGameOver() {
        return gameOver; 
    }
    
    // Check if the game has ended
    private boolean gameEnded() {
        for (Player player : players) {
            if (player.hasLost()) {
                return true;
            }
        }
        return false;
    }
    
    // Get the opponent of the given player
    public Player getOpponent(Player player) {
        if (players.get(0).equals(player)) {
            return players.get(1);
        } else if (players.get(1).equals(player)) {
            return players.get(0);
        } else {
            throw new IllegalArgumentException("Player not found");
        }
    }
    
    // Switch the turn to the next player
    public void switchTurn() {
        currentPlayer = getOpponent(currentPlayer);
        // Automatically flip the board when switching turns
        gameView.flipBoard(); // Flip the board based on the current player's turn
        notifyObservers(); // Notify observers of turn change to update the view 
        System.out.println("Switched turn to: " + currentPlayer.getName() + " (" + currentPlayer.getColor() + ")");
    }
    
    // Get the current player
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    // Set the current player
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }
    
    // Get the game board
    public Board getBoard() {
        return board;
    }
    
    // Returns the list of players in the game
    public List<Player> getPlayers() {
        return players;
    }
    
    // Get the turn count
    public int getTurnCount() {
        return turnCount;
    }
    
    // Set the turn count
    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }
    
    // Increment the turn count
    public void incrementTurnCount() {
        turnCount++;
    }

    //Get all pieces from all players in the game
    public List<Piece> getAllPieces() {
        List<Piece> allPieces = new ArrayList<>();
        for (Player player : players) {
            allPieces.addAll(player.getPieces());
        }
        return allPieces;
    }
    
    // Start the game
    public void startGame() {
        System.out.println("Game started! " + currentPlayer.getName() + " goes first.");
        notifyObservers();
    }
    
    // Handle a player's turn
    private void playTurn() {
        if (gameEnded()) {
            gameOver = true;
            String winner = getOpponent(currentPlayer).getName();
            JOptionPane.showMessageDialog(null, "Game over! Winner: " + winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            notifyObservers();
        } else {
            System.out.println("Turn: " + turnCount);
            
            // After Red's move, increment turn count (each turn is Blue's move + Red's move)
            if (currentPlayer.getColor().equals("Red")) {
                incrementTurnCount();  // Increment only after Red's move
            }
    
            // Check if it's time to transform the pieces (after two full turns)
            if (turnCount % 2 != 0 && currentPlayer.getColor().equals("Red")) {
                transformPiece();  // Perform transformation after 2 full turns 
            }
            
            switchTurn();
        }
    }
    
    // Handle a player's move
    public boolean makeMove(Player player, Piece piece, int[][] newPosition) {
        if (player.makeMove(piece, newPosition, this)) {
            System.out.println("Move successful for " + piece.getType() + " to " + Arrays.toString(newPosition[0]));
            board.placePieces(players.get(0), players.get(1)); // Refresh board state
            gameView.update(this); // Ensure GUI refresh
            playTurn(); // Call playTurn after a successful move
            return true; // Move successful
        }
        return false; // Move failed
    }
    
    //Transforms a piece (Tor <-> Xor)
    public void transformPiece() {
        for (Player player : players) {
            List<Piece> updatedPieces = new ArrayList<>();
            
            // Iterate over each piece of the player
            for (Piece piece : player.getPieces()) {
                if (piece instanceof Tor) {
                    // Transform Tor to Xor
                    updatedPieces.add(new Xor(piece.getPosition(), piece.getColor(), player, this));
                } else if (piece instanceof Xor) {
                    // Transform Xor to Tor
                    updatedPieces.add(new Tor(piece.getPosition(), piece.getColor(), player, this));
                } else {
                    // Keep other pieces unchanged
                    updatedPieces.add(piece);
                }
            }
            
            // Update the player's piece list
            player.setPieces(updatedPieces);
        }
        
        // Update the board to reflect the transformations
        board.placePieces(players.get(0), players.get(1));
        
        // Update the game view
        if (gameView != null) {
            SwingUtilities.invokeLater(() -> gameView.update(this));
        } else {
            System.err.println("GameView is null during transformation");
        }
    
        System.out.println("All Tor pieces transformed to Xor and vice versa.");
    }
    
    public void printMoveHistory() {
        System.out.println("Move History for current player (" + currentPlayer.getName() + "):");
    
        // Iterate over the current player's pieces
        for (Piece piece : currentPlayer.getPieces()) {
            List<int[][]> moveHistory = piece.getMoveHistory();
    
            // Only display move history if the piece has moved (more than one position in history)
            if (moveHistory.size() > 1) {
                System.out.println(piece.getType() + " (" + piece.getColor() + "):");
                for (int i = 0; i < moveHistory.size(); i++) {
                    int[][] move = moveHistory.get(i);
                    System.out.print("Move " + (i + 1) + ": ");
                    for (int[] position : move) {
                        System.out.print("[" + position[0] + ", " + position[1] + "] ");
                    }
                    System.out.println();
                }
            }
        }
    } 
}