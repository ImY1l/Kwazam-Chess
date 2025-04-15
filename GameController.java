import java.util.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// ====================== GameController Class (Controller) ======================
// Mohammed Amena Mohammed Abdulkarem - 1221305728
// Mohammed Yousef Mohammed Abdulkarem - 1221305727 
// Gavin Yee Hou Jien - 1221103458
// Muhammad Faiz bin Ilyasa - 1221309435
class GameController {
    private Game game;
    private GameView gameView;
    private Piece selectedPiece = null;
    private String player1Name = ""; // Store Player 1's name
    private String player2Name = ""; // Store Player 2's name

    public GameController(Game game, GameView gameView) {
        this.game = game;
        this.gameView = gameView;
        eventHandlers();
    }
    
    // Mohammed Amena Mohammed Abdulkarem - 1221305728
    // Muhammad Faiz bin Ilyasa - 1221309435
    private void eventHandlers() {
        gameView.getBoardPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / (gameView.getBoardPanel().getHeight() / 8);
                int col = e.getX() / (gameView.getBoardPanel().getWidth() / 5);

                if (gameView.isFlipped()) {
                    row = 7 - row;  // Adjust row for flipped board
                    col = 4 - col;
                }

                handleCellClick(row, col); // Handle the click
            }
        });
        
        // Add action listeners for buttons
        gameView.getrestartButton().addActionListener(e -> restartGame());
        gameView.getSaveButton().addActionListener(e -> saveGame());
        gameView.getEndButton().addActionListener(e -> endGame());
        
    }
 
    // Gavin Yee Hou Jien - 1221103458
    public void start() {
        game.startGame(); // Start the game
        gameView.update(game); // Update the view
    }
    
    // Mohammed Amena Mohammed Abdulkarem - 1221305728
    // Muhammad Faiz bin Ilyasa - 1221309435
    private void handleCellClick(int row, int col) {
        if (game == null) return; // Do nothing if no game is active
    
        Player currentPlayer = game.getCurrentPlayer();
        List<Piece> allPieces = game.getAllPieces();
    
        // Debugging: Print current state
        System.out.println("Current Player: " + currentPlayer.getName() + " (" + currentPlayer.getColor() + ")");
        System.out.println("Selected Piece: " + selectedPiece);
        System.out.println("New Position: [" + row + ", " + col + "]");
    
        // Check if a piece is selected
        if (selectedPiece == null) {
            // Select a piece
            for (Piece piece : allPieces) {
                if (!piece.isCaptured() && piece.getPosition()[0][0] == row && piece.getPosition()[0][1] == col
                        && piece.getPlayer() == currentPlayer) {
                    selectedPiece = piece; // Select the piece

                    // Get possible moves for the selected piece
                    List<int[][]> possibleMoves = selectedPiece.getPossibleMoves();

                     // Adjust highlighted moves for flipping
                    if (gameView.isFlipped()) {
                        List<int[][]> adjustedMoves = new ArrayList<>();
                        for (int[][] move : possibleMoves) {
                            int[][] adjustedMove = new int[1][2];
                            adjustedMove[0][0] = 7 - move[0][0];
                            adjustedMove[0][1] = 4 - move[0][1];
                            adjustedMoves.add(adjustedMove);
                        }
                        possibleMoves = adjustedMoves;
                    }

                    // Pass the possible moves to the view for highlighting
                    gameView.setHighlightedMoves(possibleMoves);
                    System.out.println("Selected piece: " + selectedPiece.getType());
                    gameView.update(game); // Update the view to highlight possible moves
                    break;
                }
            }
        } else {
            // Move the selected piece
            int[][] newPosition = {{row, col}};
            boolean moveSuccessful = game.makeMove(currentPlayer, selectedPiece, newPosition);
    
            selectedPiece = null; // Reset the selected piece
            gameView.setHighlightedMoves(new ArrayList<>()); // Clear highlighted moves
            gameView.update(game); // Update the view
                
            // Print move history for debugging
            game.printMoveHistory();
        }
    }

    // Gavin Yee Hou Jien - 1221103458
    private void startGame() {
        if (game != null) {
            for (Player player : game.getPlayers()) {
                for (Piece piece : player.getPieces()) {
                    piece.getMoveHistory().clear(); // Clear the move history
                }
            }
        }
        
        // Create a loop to ensure valid input for player names
        String player1Name = "";
        String player2Name = "";
    
        // Prompt for player 1's name
        while (player1Name == null || player1Name.trim().isEmpty()) {
            player1Name = JOptionPane.showInputDialog("Enter name for Player 1 (Blue):");
            if (player1Name == null || player1Name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Player 1 name cannot be empty. Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
        // Prompt for player 2's name
        while (player2Name == null || player2Name.trim().isEmpty()) {
            player2Name = JOptionPane.showInputDialog("Enter name for Player 2 (Red):");
            if (player2Name == null || player2Name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Player 2 name cannot be empty. Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
        // Create the board (Singleton)
        Board board = Board.getInstance();

        // Create two players
        Player player1 = new Player(player1Name, "Blue");
        Player player2 = new Player(player2Name, "Red");

        board.placePieces(player1, player2);
        
        // Create the game
        List<Player> players = Arrays.asList(player1, player2);
        Game game = new Game(board, players); // Create the Game object 
        game.registerObserver(gameView);
        selectedPiece = null; // Clear the selected piece
        gameView.setHighlightedMoves(new ArrayList<>()); // Clear highlighted moves

        gameView.update(game); // Update the view
        JOptionPane.showMessageDialog(gameView.getBoardPanel(), "New game started!");
        
        Piece.createPieces(player1, player2, game);
        
        board.placePieces(player1, player2);
        
        selectedPiece = null;
        
        // Create the view and controller
        GameView gameView = new GameView(game); // Pass the Game object to GameView
        GameController controller = new GameController(game, gameView); // Pass Game and GameView to GameController

        // Start the game
        controller.start();
    }
    // Mohammed Amena Mohammed Abdulkarem - 1221305728 
        private void restartGame() {
        // Dispose the current game frame
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(gameView.getBoardPanel());
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    
        // Reset the board and game state
        Board board = Board.getInstance();
        board.resetBoard();
    
        // Reset players and pieces
        List<Player> players = game.getPlayers();
        Player player1 = players.get(0);
        Player player2 = players.get(1);
    
        // Clear all pieces from the players
        player1.getPieces().clear();
        player2.getPieces().clear();
    
        // Reinitialize pieces
        Piece.createPieces(player1, player2, game);
    
        // Reset the game state
        game.setTurnCount(1);
        game.setCurrentPlayer(player1); // Set the starting player to Blue
        gameView.flipBoard(); // Reset the board flip state
        gameView.setHighlightedMoves(new ArrayList<>()); // Clear highlighted moves
    
        // Create a new GameView and GameController
        GameView newGameView = new GameView(game);
        GameController newController = new GameController(game, newGameView);
    
        // Pass the new GameView to the Game object
        game.setGameView(newGameView);
    
        // Start the new game
        newController.start();
    
        // Notify the user
        JOptionPane.showMessageDialog(newGameView.getBoardPanel(), "New game started!");
    }
    
    // Muhammad Faiz bin Ilyasa - 1221309435
    private void saveGame() {
        if (game == null) {
            JOptionPane.showMessageDialog(gameView.getBoardPanel(), "No game is currently active to save.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Save the current game state to a file
        List<Player> players = game.getPlayers();
        Player player1 = players.get(0); // Get player 1
        Player player2 = players.get(1); // Get player 2
    
        String filePath = player1.getName() + "VS" + player2.getName() + ".txt";
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the turn count
            writer.write("TurnCount: " + game.getTurnCount());
            writer.newLine();
    
            // Write the current player's turn
            writer.write("CurrentPlayer: " + game.getCurrentPlayer().getColor());
            writer.newLine();
    
            // Write the board flip state
            writer.write("BoardFlipped: " + gameView.isFlipped());
            writer.newLine();
    
            // Write player data
            for (Player player : players) {
                writer.write("Player: " + player.getName() + " " + player.getColor());
                writer.newLine();
    
                // Write piece data
                for (Piece piece : player.getPieces()) {
                    if (!piece.isCaptured()) {
                        writer.write(piece.getType() + " " + piece.getColor() + " ");
                        int[] position = piece.getPosition()[0]; // Assuming position is a 2D array
                        writer.write(position[0] + " " + position[1]);
    
                        // Save Tor/Xor transformation state
                        if (piece instanceof TransformingPiece) {
                            writer.write(" " + ((TransformingPiece) piece).getTransformationTurns());
                        }
    
                        writer.newLine();
                    }
                }
            }
    
            System.out.println("Game saved successfully!");
            JOptionPane.showMessageDialog(gameView.getBoardPanel(), "Game saved as " + filePath + ".");
            
            // Dispose the current game frame
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(gameView.getBoardPanel());
            if (currentFrame != null) {
                currentFrame.dispose();
            }
        
            // Show the main menu
            KwazamChess.main(null); // Call the main method to display the main menu
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gameView.getBoardPanel(), "Error saving the game. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Mohammed Yousef Mohammed Abdulkarem - 1221305727
    private void endGame() {
        // End the current game
        game = null;
        gameView.getBoardPanel().removeAll();
        gameView.getBoardPanel().revalidate();
        gameView.getBoardPanel().repaint();
        JOptionPane.showMessageDialog(gameView.getBoardPanel(), "Game ended!");
        
        // Dispose the current game frame
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(gameView.getBoardPanel());
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    
        // Show the main menu
        KwazamChess.main(null); // Call the main method to display the main menu
    }  
}
