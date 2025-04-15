import java.util.*;

// ====================== Abstract Piece Class ======================
// Mohammed Amena Mohammed Abdulkarem - 1221305728
// Mohammed Yousef Mohammed Abdulkarem - 1221305727
abstract class Piece {
    protected String color; // Color of the piece ("Red" or "Blue")
    protected PieceType type;   // Type of the piece (e.g., RAM, BIZ, etc.)
    protected int[][] position; // Current position of the piece
    protected Player player;    // Player who owns the piece
    protected boolean isCaptured;   // Whether the piece is captured
    protected List<int[][]> moveHistory;    // History of the piece's moves
    
    // Constructor
    // Mohammed Yousef Mohammed Abdulkarem - 1221305727 
    public Piece(int[][] position, String color, PieceType type, Player player) {
        this.position = copyPosition(position);
        this.color = color;
        this.type = type;
        this.player = player;
        this.isCaptured = false;
        this.moveHistory = new ArrayList<>(); 
        this.moveHistory.add(copyPosition(position));
    }
    
    // Mohammed Amena Mohammed Abdulkarem - 1221305728
    public static void createPieces(Player player1, Player player2, Game game) {
        // Add pieces to players
        player1.addPieces(
            new Xor(new int[][]{{7, 0}}, "Blue", player1, game),    // Xor at (7, 0)
            new Biz(new int[][]{{7, 1}}, "Blue", player1),    // Biz at (7, 1)
            new Sau(new int[][]{{7, 2}}, "Blue", player1),    // Sau at (7, 2)
            new Biz(new int[][]{{7, 3}}, "Blue", player1),    // Biz at (7, 3)
            new Tor(new int[][]{{7, 4}}, "Blue", player1, game),    // Tor at (7, 4)
            new Ram(new int[][]{{6, 0}}, "Blue", player1),    // Ram at (6, 0)
            new Ram(new int[][]{{6, 1}}, "Blue", player1),    // Ram at (6, 1)
            new Ram(new int[][]{{6, 2}}, "Blue", player1),    // Ram at (6, 2)
            new Ram(new int[][]{{6, 3}}, "Blue", player1),    // Ram at (6, 3)
            new Ram(new int[][]{{6, 4}}, "Blue", player1)     // Ram at (6, 4)
        );
    
        player2.addPieces(
            new Tor(new int[][]{{0, 0}}, "Red", player2, game),   // Tor at (0, 0)
            new Biz(new int[][]{{0, 1}}, "Red", player2),   // Biz at (0, 1)
            new Sau(new int[][]{{0, 2}}, "Red", player2),   // Sau at (0, 2)
            new Biz(new int[][]{{0, 3}}, "Red", player2),   // Biz at (0, 3)
            new Xor(new int[][]{{0, 4}}, "Red", player2, game),   // Xor at (0, 4)
            new Ram(new int[][]{{1, 0}}, "Red", player2),   // Ram at (1, 0)
            new Ram(new int[][]{{1, 1}}, "Red", player2),   // Ram at (1, 1)
            new Ram(new int[][]{{1, 2}}, "Red", player2),   // Ram at (1, 2)
            new Ram(new int[][]{{1, 3}}, "Red", player2),   // Ram at (1, 3)
            new Ram(new int[][]{{1, 4}}, "Red", player2)    // Ram at (1, 4)
        );
    }
    
    // Create a deep copy of the position array
    // Mohammed Amena Mohammed Abdulkarem - 1221305728
    // Mohammed Yousef Mohammed Abdulkarem - 1221305727 
    public int[][] copyPosition(int[][] position) {
        // Create a new 2D array with the same size as the original
        int[][] copy = new int[position.length][];
        for (int i = 0; i < position.length; i++) {
            copy[i] = position[i].clone();  // Create a deep copy of each inner array (row)
        }
        return copy;
    }
    
    // Get the piece's color
    public String getColor() {
        return color;
    }
    
    // Get the piece's type
    public PieceType getType() {
        return type;
    }
    
    // Set the piece's position
    public void setPosition(int[][] position) {
        this.position = copyPosition(position); // To ensures that the original position array is not modified from outside
    }
    
    // Get the piece's position    
    public int[][] getPosition() {
        return position; 
    }
    
    // Get the player who owns the piece
    public Player getPlayer() {
        return player;
    }
    
    // Check if the piece is captured
    public boolean isCaptured() {
        return isCaptured;
    }
    
    // Capture the piece
    public void capture() {
        this.isCaptured = true;
    }
    
    // To store the movements of the piece 
    public List<int[][]> getMoveHistory() {
        return moveHistory;
    }
    
    //String representation of the piece  
    @Override
    public String toString() {
        return "Piece{" +
               "color='" + color + '\'' +
               ", type=" + type +
               ", position=" + Arrays.toString(position[0])+
               ", isCaptured=" + isCaptured +
               '}';
    }
    
    // Abstract methods to be implemented by subclasses 
    public abstract boolean move(int[][] newPosition, List<Piece> pieces);
    public abstract boolean validMove(int[][] newPosition);
    public abstract List<int[][]> getPossibleMoves();
    public abstract Piece copy();
}

// Ram piece
// Mohammed Amena Mohammed Abdulkarem - 1221305728
class Ram extends Piece {
    private int direction; // 1 for forward, -1 for backward
   
    public Ram(int[][] position, String color, Player player) {
        super(position, color, PieceType.RAM, player);
        this.direction = (color.equals("Red")) ? 1 : -1; // Red moves down (forward), Blue moves up (forward) 
    }

    @Override
    public boolean validMove(int[][] newPosition) {
        int row = newPosition[0][0];
        int col = newPosition[0][1];
        int currentRow = position[0][0];
        int currentCol = position[0][1];

        // Allow moving downward (changing row) while staying in the same column
        // Allow moving in the current direction
        return row == currentRow + direction && col == currentCol;
    }

    @Override
    public List<int[][]> getPossibleMoves() {
        List<int[][]> possibleMoves = new ArrayList<>();
        int[][] moveOption = new int[1][2];
        int newRow = position[0][0] + direction;

        // Ensure the move is within bounds
        if (newRow >= 0 && newRow < 8) {
            moveOption[0][0] = newRow;
            moveOption[0][1] = position[0][1];
            possibleMoves.add(copyPosition(moveOption));
        }
        return possibleMoves;
    }
    
    @Override
    public boolean move(int[][] newPosition, List<Piece> pieces) {
        List<int[][]> possibleMoves = getPossibleMoves();
        boolean isValidMove = false;

        // Check if the new position is valid
        for (int[][] mov : possibleMoves) {
            if (mov[0][0] == newPosition[0][0] && mov[0][1] == newPosition[0][1]) {
                isValidMove = true;
                break;
            }
        }

        if (isValidMove && validMove(newPosition)) {
            // Check for capturing opponent's piece
            for (Piece pic : pieces) {
                if (pic.getPosition()[0][0] == newPosition[0][0] && pic.getPosition()[0][1] == newPosition[0][1]) {
                    if (!pic.getColor().equals(this.color)) {
                        pic.capture(); // Capture opponent's piece
                        player.captureOpponentPiece(pic, newPosition); // Use Player to handle capture
             
                    } else {
                        return false; // Invalid move if it's the same color
                    }
                }
            }

            // Update position
            setPosition(newPosition);
            moveHistory.add(copyPosition(newPosition));

            // Check if the Ram has reached the end of the board
            if ((color.equals("Red") && newPosition[0][0] == 7) || 
                (color.equals("Blue") && newPosition[0][0] == 0)) {
                direction *= -1; // Reverse direction
            }
            
            else if ((color.equals("Red") && newPosition[0][0] == 0) || 
                (color.equals("Blue") && newPosition[0][0] == 7)) {
                direction *= -1; // Reverse direction
            }

            return true;
        }
        return false;
    }

    @Override
    public Piece copy() {
        return new Ram(this.position.clone(), this.color, this.player);
    }
}

// Biz piece
// Mohammed Amena Mohammed Abdulkarem - 1221305728
class Biz extends Piece {
    public Biz(int[][] position, String color, Player player) {
        super(position, color, PieceType.BIZ, player);
    }

    @Override
    public boolean validMove(int[][] newPosition) {
        int[][] currentPos = position; 
        int[][] newPos = newPosition;

        // L-shape movement patterns
        int[][] lShapes = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] lShape : lShapes) {
            if (newPos[0][0] == currentPos[0][0] + lShape[0] && newPos[0][1] == currentPos[0][1] + lShape[1]) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<int[][]> getPossibleMoves() {
        List<int[][]> possibleMoves = new ArrayList<>();
        int[][] currentPos = position;

        // L-shape movement patterns
        int[][] lShapes = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] lShape : lShapes) {
            int[][] potentialMove = {{currentPos[0][0] + lShape[0], currentPos[0][1] + lShape[1]}};
            //Check if the position in board 
            if (potentialMove[0][0] >= 0 && potentialMove[0][0] < 8 && potentialMove[0][1] >= 0 && potentialMove[0][1] < 5) {
                possibleMoves.add(copyPosition(potentialMove));
            }
        }
        return possibleMoves;
    }
    
    @Override
    public boolean move(int[][] newPosition, List<Piece> pieces) {
        List<int[][]> possibleMoves = getPossibleMoves();
        boolean isValidMove = false;

        for (int[][] possibleMov : possibleMoves) {
            if (possibleMov[0][0] == newPosition[0][0] && possibleMov[0][1] == newPosition[0][1]) {
                isValidMove = true;
                break;
            }
        }

        if (isValidMove && validMove(newPosition)) {
            for (Piece pic : pieces) {
                if (pic.getPosition()[0][0] == newPosition[0][0] && pic.getPosition()[0][1] == newPosition[0][1]) {
                    if (!pic.getColor().equals(this.color)) {
                        pic.capture();  // Capture opponent's piece
                        player.captureOpponentPiece(pic, newPosition); // Use Player to handle capture
             
                    } else {
                        return false;  // Invalid move if it's the same color
                    }
                }
            }
            setPosition(newPosition);  // Update position
            moveHistory.add(copyPosition(newPosition)); // Save move history
            return true;
        }

        return false;
    }
    
    @Override
    public Piece copy() {
        return new Biz(this.position.clone(), this.color, this.player);
    }
}

// Tor piece
// Mohammed Yousef Mohammed Abdulkarem - 1221305727 
class Tor extends Piece implements TransformingPiece {
    private int transformationTurns = 2; // Default transformation turns
    private Game game;
    
    public Tor(int[][] position, String color, Player player, Game game) {
        super(position, color, PieceType.TOR, player);
        this.game = game;
    }
    
     @Override
    public int getTransformationTurns() {
        return transformationTurns;
    }

    @Override
    public void setTransformationTurns(int turns) {
        this.transformationTurns = turns;
    }

    @Override
    public boolean validMove(int[][] newPosition) {
        int currentRow = position[0][0];
        int currentCol = position[0][1];
        int newRow = newPosition[0][0];
        int newCol = newPosition[0][1];
    
        // Check if the move is orthogonal
        return (newRow == currentRow && newCol != currentCol) || // Horizontal move
               (newCol == currentCol && newRow != currentRow);   // Vertical move
    }

    @Override
    public List<int[][]> getPossibleMoves() {
        List<int[][]> possibleMoves = new ArrayList<>();
        int currentRow = position[0][0];
        int currentCol = position[0][1];
    
        // Check horizontal moves (left and right)
        for (int col = currentCol - 1; col >= 0; col--) { // Left
            if (game.getBoard().isOccupied(currentRow, col)) {
                possibleMoves.add(new int[][]{{currentRow, col}});
                break; // Stop if a piece is blocking
            }
            possibleMoves.add(new int[][]{{currentRow, col}});
        }
        for (int col = currentCol + 1; col < 5; col++) { // Right
            if (game.getBoard().isOccupied(currentRow, col)) {
                possibleMoves.add(new int[][]{{currentRow, col}});
                break; // Stop if a piece is blocking
            }
            possibleMoves.add(new int[][]{{currentRow, col}});
        }
    
        // Check vertical moves (up and down)
        for (int row = currentRow - 1; row >= 0; row--) { // Up
            if (game.getBoard().isOccupied(row, currentCol)) {
                possibleMoves.add(new int[][]{{row, currentCol}});
                break; // Stop if a piece is blocking
            }
            possibleMoves.add(new int[][]{{row, currentCol}});
        }
        for (int row = currentRow + 1; row < 8; row++) { // Down
            if (game.getBoard().isOccupied(row, currentCol)) {
                possibleMoves.add(new int[][]{{row, currentCol}});
                break; // Stop if a piece is blocking
            }
            possibleMoves.add(new int[][]{{row, currentCol}});
        }
    
        return possibleMoves;
    }

    @Override
    public boolean move(int[][] newPosition, List<Piece> pieces) {
        // Validate the move
        if (!validMove(newPosition)) {
            return false; // Invalid move
        }
    
        // Check for obstacles in the path
        int currentRow = position[0][0];
        int currentCol = position[0][1];
        int newRow = newPosition[0][0];
        int newCol = newPosition[0][1];
    
        if (currentRow == newRow) {
            // Horizontal move
            int step = newCol > currentCol ? 1 : -1;
            for (int col = currentCol + step; col != newCol; col += step) {
                for (Piece piece : pieces) {
                    if (piece.getPosition()[0][0] == currentRow && piece.getPosition()[0][1] == col) {
                        return false; // Obstacle found
                    }
                }
            }
        } else if (currentCol == newCol) {
            // Vertical move
            int step = newRow > currentRow ? 1 : -1;
            for (int row = currentRow + step; row != newRow; row += step) {
                for (Piece piece : pieces) {
                    if (piece.getPosition()[0][0] == row && piece.getPosition()[0][1] == currentCol) {
                        return false; // Obstacle found
                    }
                }
            }
        }
    
        // Check if the target position is occupied by an opponent's piece
        for (Piece pic : pieces) {
            if (pic.getPosition()[0][0] == newRow && pic.getPosition()[0][1] == newCol) {
                if (!pic.getColor().equals(this.color)) {
                    pic.capture(); // Capture opponent's piece
                    player.captureOpponentPiece(pic, newPosition); // Use Player to handle capture
             
                } else {
                    return false; // Cannot capture own piece
                }
            }
        }
    
        // Update position
        setPosition(newPosition);
        moveHistory.add(copyPosition(newPosition));
    
        return true; // Move successful
    }
    
    @Override
    public Piece copy() {
        return new Tor(this.position.clone(), this.color, this.player, this.game);
    }
}

// Xor piece
// Mohammed Yousef Mohammed Abdulkarem - 1221305727 
class Xor extends Piece implements TransformingPiece{
     private int transformationTurns = 2; // Default transformation turns
    private Game game;
    
    public Xor(int[][] position, String color, Player player, Game game) {
        super(position, color, PieceType.XOR, player);
        this.game = game;
    }
    
     @Override
    public int getTransformationTurns() {
        return transformationTurns;
    }

    @Override
    public void setTransformationTurns(int turns) {
        this.transformationTurns = turns;
    }

    @Override
    public boolean validMove(int[][] newPosition) {
        int[] currentPos = position[0];

        // Check for diagonal movement
        return Math.abs(newPosition[0][0] - currentPos[0]) == Math.abs(newPosition[0][1] - currentPos[1]);
    }

    @Override
    public List<int[][]> getPossibleMoves() {
        List<int[][]> possibleMoves = new ArrayList<>();
        int[] currentPos = position[0];
        Board board = Board.getInstance();
    
        // Directions for diagonal movement: [rowStep, colStep]
        int[][] directions = {
            {1, 1},   // Down-right
            {1, -1},  // Down-left
            {-1, 1},  // Up-right
            {-1, -1}  // Up-left
        };
    
        for (int[] direction : directions) {
            int row = currentPos[0] + direction[0];
            int col = currentPos[1] + direction[1];
    
            while (row >= 0 && row < 8 && col >= 0 && col < 5) {
                // Check if the position is occupied
                if (board.isOccupied(row, col)) {
                    Piece occupyingPiece = board.getPieceAt(row, col);
    
                    // Add the position regardless of who occupies it
                    possibleMoves.add(new int[][]{{row, col}});
    
                    // Stop further exploration in this direction
                    break;
                }
    
                // Add the position if unoccupied
                possibleMoves.add(new int[][]{{row, col}});
    
                // Move to the next cell in this direction
                row += direction[0];
                col += direction[1];
            }
        }
    
        return possibleMoves;
    }

    @Override
    public boolean move(int[][] newPosition, List<Piece> pieces) {
        if (Arrays.deepEquals(newPosition, position)) {
            //System.out.println("Move rejected: Cannot move to the same position.");
            return false;
        }
    
        if (!validMove(newPosition)) {
            System.out.println("Move rejected: Invalid move.");
            return false; // Invalid move
        }
        
        // Check for obstacles in the path
        int currentRow = position[0][0];
        int currentCol = position[0][1];
        int newRow = newPosition[0][0];
        int newCol = newPosition[0][1];
    
        int rowStep = newRow > currentRow ? 1 : -1;
        int colStep = newCol > currentCol ? 1 : -1;
    
        for (int row = currentRow + rowStep, col = currentCol + colStep; row != newRow; row += rowStep, col += colStep) {
            for (Piece piece : pieces) {
                if (piece.getPosition()[0][0] == row && piece.getPosition()[0][1] == col) {
                    return false; // Obstacle found
                }
            }
        }
        
        List<int[][]> possibleMoves = getPossibleMoves();
        boolean isValidMove = false;

        for (int[][] possibleMove : possibleMoves) {
            if (possibleMove[0][0] == newPosition[0][0] && possibleMove[0][1] == newPosition[0][1]) {
                isValidMove = true;
                break;
            }
        }

        if (isValidMove && validMove(newPosition)) {
            for (Piece pic : pieces) {
                if (pic.getPosition()[0][0] == newPosition[0][0] && pic.getPosition()[0][1] == newPosition[0][1]) {
                    if (!pic.getColor().equals(this.color)) {
                        pic.capture();  // Capture opponent's piece
                        player.captureOpponentPiece(pic, newPosition); // Use Player to handle capture
                    } else {
                        return false;  // Invalid move if it's the same color
                    }
                }
            }

            setPosition(newPosition);  // Update position
            moveHistory.add(copyPosition(newPosition)); // Save move history

            return true;
        }

        return false;
    }
    
    @Override
    public Piece copy() {
        return new Xor(this.position.clone(), this.color, this.player, this.game);
    }
}

// Sau piece
// Mohammed Yousef Mohammed Abdulkarem - 1221305727
class Sau extends Piece {
    public Sau(int[][] position, String color, Player player) {
        super(position, color, PieceType.SAU, player);
    }

    @Override
    public boolean validMove(int[][] newPosition) {
        int dx = Math.abs(newPosition[0][0] - position[0][0]);
        int dy = Math.abs(newPosition[0][1] - position[0][1]);

        // Sau can move one step in any direction
        return dx <= 1 && dy <= 1;
    }

    @Override
    public List<int[][]> getPossibleMoves() {
        List<int[][]> possibleMoves = new ArrayList<>();

        // Generate all possible moves in any direction (one step)
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    int[][] potentialMove = {{position[0][0] + i, position[0][1] + j}};
                    // Check if the move is within bounds
                    if (potentialMove[0][0] >= 0 && potentialMove[0][0] < 8 && potentialMove[0][1] >= 0 && potentialMove[0][1] < 5) {
                        possibleMoves.add(potentialMove);
                        System.out.println("Sau possible move: " + Arrays.toString(potentialMove[0])); // Debugging statement
                    }
                }
            }
        }

        return possibleMoves;
    }
    
    @Override
     public boolean move(int[][] newPosition, List<Piece> pieces) {
        List<int[][]> possibleMoves = getPossibleMoves();
        boolean isValidMove = false;

        // Check if the new position is valid
        for (int[][] possibleMove : possibleMoves) {
            if (possibleMove[0][0] == newPosition[0][0] && possibleMove[0][1] == newPosition[0][1]) {
                isValidMove = true;
                break;
            }
        }

        if (isValidMove && validMove(newPosition)) {
            // Check if there is a piece at the new position
            for (Piece pic : pieces) {
                if (pic.getPosition()[0][0] == newPosition[0][0] && pic.getPosition()[0][1] == newPosition[0][1]) {
                    // Only capture the piece if it belongs to the opponent
                    if (!pic.getColor().equals(this.color)) {
                        pic.capture();  // Capture opponent's piece
                        player.captureOpponentPiece(pic, newPosition); // Use Player to handle capture
                        return true;  // Sau piece captured ends the game
                    } else {
                        return false;  // Cannot capture a piece of the same color
                    }
                }
            }

            // If no piece is at the new position, move the Sau piece
            setPosition(newPosition);  // Update position
            moveHistory.add(copyPosition(newPosition)); // Save move history
            return true;
        }
        return false;
    }
    
    @Override
    public Piece copy() {
     return new Sau(this.position.clone(), this.color, this.player);
    }
}
