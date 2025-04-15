import java.util.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GameStart {
    public static void startGame() {
        // Create the board (Singleton)
        Board board = Board.getInstance();
        board.resetBoard();

        // Prompt for player names
        String player1Name = JOptionPane.showInputDialog("Enter name for Player 1 (Blue):");
        String player2Name = JOptionPane.showInputDialog("Enter name for Player 2 (Red):");

        // Create two players
        Player player1 = new Player(player1Name, "Blue");
        Player player2 = new Player(player2Name, "Red");

        // Create the game
        List<Player> players = Arrays.asList(player1, player2);
        Game game = new Game(board, players);

        // Initialize pieces for both players
        Piece.createPieces(player1, player2, game);

        // Place pieces on the board
        board.placePieces(player1, player2);

        // Create the view and controller
        GameView gameView = new GameView(game);
        GameController controller = new GameController(game, gameView);

        // Pass the GameView to the Game instance
        game.setGameView(gameView);

        // Start the game
        controller.start();
    }
}