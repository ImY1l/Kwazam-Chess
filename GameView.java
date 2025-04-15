import java.util.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

// ====================== GameView Class (View) ======================
// Mohammed Amena Mohammed Abdulkarem - 1221305728
// Mohammed Yousef Mohammed Abdulkarem - 1221305727 
// Gavin Yee Hou Jien - 1221103458
// Muhammad Faiz bin Ilyasa - 1221309435
class GameView implements GameObserver {
    private JFrame frame;
    private JPanel boardPanel;
    private JPanel controlPanel;
    private boolean isFlipped = false; // Track if the board is flipped
    private Map<String, ImageIcon> pieceImages;
    private JButton restartButton;
    private JButton saveButton;
    private JButton loadButton; 
    private JButton endButton;    
    private List<int[][]> highlightedMoves = new ArrayList<>();
     
    public GameView(Game game) {
        loadPieceImages(); // Load images for pieces
        initialize();
        game.registerObserver(this);
    }

    // Mohammed Amena Mohammed Abdulkarem - 1221305728
    // Muhammad Faiz bin Ilyasa - 1221309435
    private void loadPieceImages() {
        pieceImages = new HashMap<>();
        int imgWidth = 60; 
        int imgHeight = 60;
        
        // Load images based on the flip state
        String suffix = isFlipped ? "Flipped" : "";
        pieceImages.put("Red_RAM", resizeImageIcon(new ImageIcon("redRam" + suffix + ".jpg"), imgWidth, imgHeight));
        pieceImages.put("Red_BIZ", resizeImageIcon(new ImageIcon("redBiz.jpg"), imgWidth, imgHeight));
        pieceImages.put("Red_TOR", resizeImageIcon(new ImageIcon("redTor.jpg"), imgWidth, imgHeight));
        pieceImages.put("Red_XOR", resizeImageIcon(new ImageIcon("redXor.jpg"), imgWidth, imgHeight));
        pieceImages.put("Red_SAU", resizeImageIcon(new ImageIcon("redSau" + suffix + ".jpg"), imgWidth, imgHeight));
        pieceImages.put("Blue_RAM", resizeImageIcon(new ImageIcon("blueRam" + suffix + ".jpg"), imgWidth, imgHeight));
        pieceImages.put("Blue_BIZ", resizeImageIcon(new ImageIcon("blueBiz.jpg"), imgWidth, imgHeight));
        pieceImages.put("Blue_TOR", resizeImageIcon(new ImageIcon("blueTor.jpg"), imgWidth, imgHeight));
        pieceImages.put("Blue_XOR", resizeImageIcon(new ImageIcon("blueXor.jpg"), imgWidth, imgHeight));
        pieceImages.put("Blue_SAU", resizeImageIcon(new ImageIcon("blueSau" + suffix + ".jpg"), imgWidth, imgHeight));
    }
    
    // Mohammed Amena Mohammed Abdulkarem - 1221305728
    // Muhammad Faiz bin Ilyasa - 1221309435
    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        if (icon == null) {
            return null; // Return null if the icon is null
        }
        Image image = icon.getImage(); // Get the image from the icon
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Resize the image
        return new ImageIcon(resizedImage); // Return the resized icon
    }
    
    // Muhammad Faiz bin Ilyasa - 1221309435
    private void initialize() {
        frame = new JFrame("Kwazam Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 750); // Set the frame size
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create the board panel with a GridLayout
        boardPanel = new JPanel(new GridLayout(8, 5)); // 8 rows, 5 columns
        boardPanel.setPreferredSize(new Dimension(500, 600)); // Set preferred size for the board
        
        // Create row labels (1-8)
        JPanel leftRowLabels = new JPanel(new GridLayout(8, 1)); // 8 rows, 1 column
        for (int i = 8; i >= 1; i--) { // Add row numbers in reverse order
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(20, 40)); // Adjust label size
            leftRowLabels.add(label);
        }
        
        JPanel rightRowLabels = new JPanel(new GridLayout(8, 1)); // 8 rows, 1 column
        for (int i = 8; i >= 1; i--) { // Add row numbers in reverse order
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(20, 40)); // Adjust label size
            rightRowLabels.add(label);
        }
        
        // Create column labels (A-E)
        JPanel topColumnLabels = new JPanel(new GridLayout(1, 5)); // 1 row, 5 columns
        for (char c = 'A'; c <= 'E'; c++) { // Add column letters
            JLabel label = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(40, 20)); // Adjust label size
            topColumnLabels.add(label);
        }
        
        JPanel bottomColumnLabels = new JPanel(new GridLayout(1, 5)); // 1 row, 5 columns
        for (char c = 'A'; c <= 'E'; c++) { // Add column letters
            JLabel label = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(40, 20)); // Adjust label size
            bottomColumnLabels.add(label);
        }
        
        mainPanel.add(leftRowLabels, BorderLayout.WEST); // Row labels to the left
        mainPanel.add(topColumnLabels, BorderLayout.NORTH); // Column labels at the top
        mainPanel.add(boardPanel, BorderLayout.CENTER); // Board in the center
        mainPanel.add(rightRowLabels, BorderLayout.EAST); // Row labels to the right
        mainPanel.add(bottomColumnLabels, BorderLayout.SOUTH); // Column labels at the bottom
        
        frame.add(mainPanel, BorderLayout.CENTER);

        // Create the control panel with buttons
        controlPanel = new JPanel();
        restartButton = new JButton("Restart");
        saveButton = new JButton("Save Game");
        endButton = new JButton("End");
        
        // Add buttons to the control panel
        controlPanel = new JPanel();
        controlPanel.add(restartButton);
        controlPanel.add(saveButton);
        controlPanel.add(endButton);
        frame.add(controlPanel, BorderLayout.NORTH);
    
        frame.setVisible(true);
    }
     
    // Update the view when the game state changes
    @Override
    public void update(Game game) {
        if (game == null) return;
    
        // Clear the board state
        boardPanel.removeAll(); // Clear the board
    
        // Get the board state
        List<Piece> allPieces = game.getAllPieces();
        Player currentPlayer = game.getCurrentPlayer();
    
        // Create a map of piece positions for quick lookup
        // Mohammed Yousef Mohammed Abdulkarem - 1221305727
        Map<String, Piece> piecePositionMap = new HashMap<>();
        for (Piece piece : allPieces) {
            if (!piece.isCaptured()) {
                int row = piece.getPosition()[0][0];
                int col = piece.getPosition()[0][1];
                piecePositionMap.put(row + "," + col, piece);
            }
        }
    
        // Draw the board
        // Muhammad Faiz bin Ilyasa - 1221309435
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 5; col++) {
                JLabel cell = new JLabel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell.setHorizontalAlignment(JLabel.CENTER);
                cell.setVerticalAlignment(JLabel.CENTER);
                cell.setPreferredSize(new Dimension(40, 40)); // Set cell size
                cell.setOpaque(true);
                  
                // Adjust row for flipping
                int displayRow = isFlipped ? 7 - row : row;
                int displayCol = isFlipped ? 4 - col : col;
    
                // Check if there is a piece at this position
                Piece piece = piecePositionMap.get(displayRow + "," + displayCol);
                if (piece != null) {
                    // Construct the key using piece color and type
                    String key = piece.getColor() + "_" + piece.getType();
                    ImageIcon icon = pieceImages.get(key);
                    if (icon != null) {
                        cell.setIcon(icon); // Set the piece image
                    } else {
                        System.err.println("Image not found for key: " + key);
                    }
                }
                
                for (int[][] move : highlightedMoves) {
                    int moveRow = move[0][0]; // Get the row of the move
                    int moveCol = move[0][1]; // Get the column of the move
                
                    // Adjust for flipping
                    int displayMoveRow = isFlipped ? 7 - moveRow : moveRow;
                    int displayMoveCol = isFlipped ? 4 - moveCol : moveCol;
                
                    // Check if the current cell matches the move
                    if (displayRow == displayMoveRow && displayCol == displayMoveCol) {
                        // Use the Board class to check occupancy
                        Board board = Board.getInstance();
                        
                        //DEBUGGING BOARD
                        board.printBoard();
                        
                        if (board.isOccupied(displayMoveRow, displayMoveCol)) {
                            // Get the piece at the target position
                            Piece occupyingPiece = board.getPieceAt(displayMoveRow, displayMoveCol);
                
                            // Check if the piece is the enemy's based on color
                            if (!occupyingPiece.getColor().equals(currentPlayer.getColor())) {
                                // Highlight enemy positions as yellow
                                cell.setBackground(Color.YELLOW);
                            } else {
                                // Highlight positions occupied by the current player as red
                                cell.setBackground(Color.RED);
                            }
                        } else {
                            // Highlight unoccupied positions as yellow
                            cell.setBackground(Color.YELLOW);
                        }
                    }
                }
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }
    
     public JPanel getBoardPanel() {
        return boardPanel;
    }
    
    public JButton getrestartButton() {
        return restartButton;
    }
    
    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getEndButton() {
        return endButton;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void flipBoard() {
        isFlipped = !isFlipped;
        loadPieceImages();
    }

    public void setHighlightedMoves(List<int[][]> highlightedMoves) {
        this.highlightedMoves = highlightedMoves;
    }

    public void clearBoard() {
        boardPanel.removeAll();
        boardPanel.revalidate();
        boardPanel.repaint();
    }
} 
