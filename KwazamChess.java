import java.util.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Main class to run the game
// Gavin Yee Hou Jien - 1221103458
public class KwazamChess {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Game Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
        frame.getContentPane().setBackground(new Color(0xf2e1c3));
        frame.setLayout(null);

        // Create the title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBounds(100,50,600,150);
        titlePanel.setBackground(new Color(0xf2e1c3));
        frame.add(titlePanel);
        
        // Create the title label
        JLabel titleLabel = new JLabel("Kwazam Chess Game");
        titleLabel.setForeground(Color.black);
        titlePanel.add(titleLabel);
        titleLabel.setFont(new Font("Times New Roman",Font.PLAIN,60));
        
        // Create the start game button panel
        JPanel startBtnPanel = new JPanel();
        startBtnPanel.setBounds(300,200,200,100);
        startBtnPanel.setBackground(new Color(0xf2e1c3));
        frame.add(startBtnPanel);
        
        // Create the start game button
        JButton startBtn = new JButton("Start Game");
        startBtn.setBackground(new Color(0xf2e1c3));
        startBtn.setForeground(Color.black);
        startBtn.setFont(new Font("Times New Roman",Font.PLAIN,30));
        startBtnPanel.add(startBtn);       
        
        // Create the load game button panel
        JPanel loadBtnPanel = new JPanel();
        loadBtnPanel.setBounds(300,300,200,100);
        loadBtnPanel.setBackground(new Color(0xf2e1c3));
        frame.add(loadBtnPanel);
        
        // Create the load game button
        JButton loadBtn = new JButton("Load Game");
        loadBtn.setBackground(new Color(0xf2e1c3));
        loadBtn.setForeground(Color.black);
        loadBtn.setFont(new Font("Times New Roman",Font.PLAIN,30));
        loadBtnPanel.add(loadBtn); 
        
        // Create the instruction button panel
        JPanel instrucBtnPanel = new JPanel();
        instrucBtnPanel.setBounds(300,400,200,100);
        instrucBtnPanel.setBackground(new Color(0xf2e1c3));
        frame.add(instrucBtnPanel);
        
        // Create the instruction button
        JButton instrucBtn = new JButton("Instructions");
        instrucBtn.setBackground(new Color(0xf2e1c3));
        instrucBtn.setForeground(Color.black);
        instrucBtn.setFont(new Font("Times New Roman",Font.PLAIN,30));
        instrucBtnPanel.add(instrucBtn);   
        
        // Create the exit button panel
        JPanel exitBtnPanel = new JPanel();
        exitBtnPanel.setBounds(300,500,200,100);
        exitBtnPanel.setBackground(new Color(0xf2e1c3));
        frame.add(exitBtnPanel);
        
        // Create the exit button
        JButton exitBtn = new JButton("Exit");
        exitBtn.setBackground(new Color(0xf2e1c3));
        exitBtn.setForeground(Color.black);
        exitBtn.setFont(new Font("Times New Roman",Font.PLAIN,30));
        exitBtnPanel.add(exitBtn);   
        
        // Add action listeners for the buttons
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                GameStart.startGame();
            }
        });
        
        // Add action listener for load game button
        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load Game selected");
                frame.dispose(); // Close the main menu
                loadGame(); // Call the loadGame method
            }
        });
        
        instrucBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String instructions = "Welcome to Kwazam Chess!\n\n"
                    + "Game Board:\n"
                    + "The game is played on a 5x8 board.\n\n"
                    + "Game Pieces and Rules:\n\n"
                    + "Ram:\n"
                    + "Moves forward 1 step at a time.\n"
                    + "If it reaches the end of the board, it turns around and starts heading in the opposite direction.\n"
                    + "Cannot skip over other pieces.\n\n"
                    + "Biz:\n"
                    + "Moves in a 3x2 'L' shape in any orientation (similar to a knight in standard chess).\n"
                    + "The only piece that can skip over other pieces.\n\n"
                    + "Tor:\n"
                    + "Moves orthogonally (up, down, left, right) any distance.\n"
                    + "Cannot skip over other pieces.\n"
                    + "Transforms into the Xor piece after 2 turns.\n\n"
                    + "Xor:\n"
                    + "Moves diagonally any distance.\n"
                    + "Cannot skip over other pieces.\n"
                    + "Transforms back into the Tor piece after 2 turns.\n\n"
                    + "Sau:\n"
                    + "Moves 1 step in any direction.\n"
                    + "The game ends when the Sau is captured by the other side.\n\n"
                    + "Special Rules:\n"
                    + "Only the Biz piece can skip over other pieces.\n"
                    + "Every 2 turns (one blue move + one red move = 1 turn), all Tor pieces transform into Xor pieces, "
                    + "and all Xor pieces transform into Tor pieces.\n\n"
                    + "Objective:\n"
                    + "Capture the opponent's Sau to win the game!\n\n"
                    + "Good luck and have fun!";
                
                JOptionPane.showMessageDialog(frame, instructions, "Game Instructions", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Game",JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        
        frame.setVisible(true);
    }
    
    
    // Method to load the game from a file
    private static void loadGame() {
        String filePath = JOptionPane.showInputDialog(null, "Enter the save file name:");
    
        if (filePath == null || filePath.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid file name. Please try again.");
            return;
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Temporary storage for players and pieces
            List<String[]> playerDataList = new ArrayList<>();
            List<String[]> pieceDataList = new ArrayList<>();
            int turnCount = 0;
            String currentPlayerColor = "";
            boolean boardFlipped = false;
    
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TurnCount:")) {
                    turnCount = Integer.parseInt(line.split(" ")[1]); // Parse turn count
                } else if (line.startsWith("CurrentPlayer:")) {
                    currentPlayerColor = line.split(" ")[1]; // Parse current player's color
                } else if (line.startsWith("BoardFlipped:")) {
                    boardFlipped = Boolean.parseBoolean(line.split(" ")[1]); // Parse board flip state
                } else if (line.startsWith("Player:")) {
                    playerDataList.add(line.split(" "));
                } else {
                    pieceDataList.add(line.split(" "));
                }
            }
    
            // Check if player data is valid
            if (playerDataList.size() < 2) {
                JOptionPane.showMessageDialog(null, "Error: Save file is missing player data.");
                return;
            }
    
            // Create the board and players
            Board board = Board.getInstance();
            board.resetBoard();
    
            Player player1 = null;
            Player player2 = null;
    
            // Create players
            for (int i = 0; i < playerDataList.size(); i++) {
                String[] playerData = playerDataList.get(i);
                if (playerData.length < 3) {
                    System.err.println("Invalid player data: " + Arrays.toString(playerData));
                    continue;
                }
    
                String playerName = playerData[1];
                String playerColor = playerData[2];
    
                if (i == 0) {
                    player1 = new Player(playerName, playerColor);
                } else {
                    player2 = new Player(playerName, playerColor);
                }
            }
    
            // Check if players were created successfully
            if (player1 == null || player2 == null) {
                JOptionPane.showMessageDialog(null, "Error: Player data is missing or invalid in the save file.");
                return;
            }
    
            // Initialize the game with both players
            List<Player> players = Arrays.asList(player1, player2);
            Game game = new Game(board, players);
            game.setTurnCount(turnCount); // Set the turn count
    
            // Set the board flip state
            GameView gameView = new GameView(game);
            game.setGameView(gameView); // Ensure the Game object has a reference to the GameView
            Player currentPlayer = currentPlayerColor.equals(player1.getColor()) ? player1 : player2;
            game.setCurrentPlayer(currentPlayer); // Set the current player directly

            if (boardFlipped) {
                gameView.flipBoard();
            }
    
            // Create and assign pieces
            for (String[] pieceData : pieceDataList) {
                if (pieceData.length < 4) {
                    System.err.println("Invalid piece data: " + Arrays.toString(pieceData));
                    continue;
                }
    
                String type = pieceData[0];
                String color = pieceData[1];
                int row = Integer.parseInt(pieceData[2]);
                int col = Integer.parseInt(pieceData[3]);
    
                Player owner = color.equals(player1.getColor()) ? player1 : player2;
                Piece piece = createPiece(type, new int[][]{{row, col}}, color, owner, game);
    
                // Restore Tor/Xor transformation state
                if (piece instanceof TransformingPiece && pieceData.length >= 5) {
                    int transformationTurns = Integer.parseInt(pieceData[4]);
                    ((TransformingPiece) piece).setTransformationTurns(transformationTurns);
                }
    
                owner.addPiece(piece);
            }
    
            // Use placePieces with both players
            board.placePieces(player1, player2);
    
            // Restart the game interface
            GameController controller = new GameController(game, gameView);
            controller.start();
    
            JOptionPane.showMessageDialog(null, "Game loaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading the game. Please check the file and try again.");
        }
    }

    // Helper method to create pieces based on type
    private static Piece createPiece(String type, int[][] position, String color, Player owner, Game game) {
        switch (type) {
            case "RAM":
                return new Ram(position, color, owner);
            case "BIZ":
                return new Biz(position, color, owner);
            case "TOR":
                return new Tor(position, color, owner, game);
            case "XOR":
                return new Xor(position, color, owner, game);
            case "SAU":
                return new Sau(position, color, owner);
            default:
                throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }
} 