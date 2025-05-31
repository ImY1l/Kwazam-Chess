# 🧩 Kwazam Chess
Kwazam Chess is a two-player strategy game implemented in Java, featuring a fixed 5×8 board and uniquely designed pieces with distinct movement rules. It was developed using three design patterns (MVC, Singleton, Observer) as part of an Object-Oriented Analysis and Design (OOAD) course.

## 🎲 Game Overview
- **Board Size:** 5 rows × 8 columns  
- **Objective:** Capture the opponent's **Sau** piece to win  
- **Features:**
  - Unique piece movement logic
  - Piece transformation (Tor ⇄ Xor)
  - Graphical User Interface using Java Swing

## ♟️ Pieces
| Piece  | Movement Description                                         |
|--------|--------------------------------------------------------------|
| Ram    | Moves 1 step forward; reverses direction at board edge       |
| Biz    | Moves in an L-shape (3x2); can jump over pieces              |
| Tor    | Moves orthogonally; transforms into Xor after 2 turns       |
| Xor    | Moves diagonally; cannot jump over other pieces              |
| Sau    | King-like; losing this piece ends the game                  |

## 📁 Project Structure
- `Board.java` – Manages the board and piece layout  
- `Game.java` – Game logic and state management  
- `GameController.java` – Input and action handler  
- `GameView.java` – Swing-based GUI renderer  
- `Piece.java` & `PieceType.java` – Piece properties and types  
- `TransformingPiece.java` – Handles transformation logic  
- `Player.java` – Stores player info  
- `KwazamChess.java` – Entry point (main class)

## 🚀 Run Locally
   ```bash
git clone https://github.com/ImY1l/Kwazam-Chess.git
cd Kwazam-Chess
javac KwazamChess.java
java KwazamChess
```
