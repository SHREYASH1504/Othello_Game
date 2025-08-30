# 🟢⚫ Othello Game (Reversi) in Java

This is a **Java implementation of the classic Othello (Reversi) game** with:  
- **Game Logic** (rules engine)  
- **Graphical User Interface (GUI)** using Swing  
- **Artificial Intelligence (AI)** opponent using **Minimax with Alpha-Beta Pruning**  
- **Game Launcher**  

---

## 📂 Project Structure




---

## ⚙️ Features

- ✅ Full implementation of **Othello rules** (placing, flipping, scoring, valid moves)  
- ✅ **Interactive GUI** built with Java Swing  
- ✅ Human (Black) vs Computer (White) gameplay  
- ✅ **AI opponent** using **Minimax + Alpha-Beta pruning**  
- ✅ Real-time move validation and invalid move warnings  
- ✅ Game-over detection with winner announcement  

---

## 🧠 Core Components

### 1. **OthelloBoard.java** (Game Logic)
- Maintains board state (`char[][] board`)  
- Checks move validity & flips discs  
- Detects game-over & calculates scores  
- Implements copy constructor (used by AI simulations)  
- **Complexity**: Most operations `O(N)` where `N=8`

---

### 2. **OthelloAI.java** (Computer Opponent)
- Implements **Minimax with Alpha-Beta pruning**  
- AI evaluates possible moves up to a certain depth  
- Uses `evaluateBoard()` heuristic to score board states  
- Always plays as **White (W)**  
- **Complexity**: Improved from `O(b^d)` to `O(b^(d/2))` using pruning  
  - `b` = branching factor (possible moves)  
  - `d` = search depth  

---

### 3. **OthelloGUI.java** (User Interface)
- Built using **Java Swing**  
- Handles mouse clicks → translates pixel clicks to board moves  
- Renders board, grid lines, and black/white discs  
- Shows invalid move warnings (`JOptionPane`)  
- Displays winner at game end  
- Integrates with AI so the computer plays immediately after the human  

---

### 4. **OthelloGame.java** (Launcher)
- Simple **entry point** with `main()` method  
- Creates a `JFrame` and attaches `OthelloGUI` panel  

---

## 🎮 Gameplay
- You play as **Black (B)**  
- Computer plays as **White (W)**  
- Game alternates turns automatically  
- Winner is decided when no valid moves remain  

---

## 🚀 How to Run

1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/Othello_Game.git
   cd Othello_Game
