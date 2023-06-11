package GameModes;

import java.util.ArrayList;
import java.util.Stack;

public class Game {

    public static final int SUCCESSFUL = -1;
    public static final int MOVE_INVALID = -2;
    public static final int STACK_EMPTY = -3;
    public static final int GAME_IN_PROGRESS = -4;
    public static final int GAME_TIE = -5;
    public static final int BLUE_WINNER = -6;
    public static final int GREEN_WINNER = -7;

    private static final boolean BLUE = true;
    private static final boolean GREEN = false;

    private boolean blue_turn = true;

    Stack<String>[] board;
    Stack<String>[] greenStacks;
    Stack<String>[] blueStacks;
    
    public Game() {
        initBoard();
    }

    public int makeMove(int stack, String cell) {
        Stack<String> s = blue_turn ? blueStacks[stack-1] : greenStacks[stack-1];
        int index = stringToIndex(cell);

        if (s.empty()) return STACK_EMPTY;
        if (index == MOVE_INVALID) return MOVE_INVALID;

        if (!board[index].empty()) {
            int stackRank = Character.getNumericValue(s.peek().charAt(1));
            int boardRank = Character.getNumericValue(board[index].peek().charAt(1));
            if (stackRank <= boardRank) return MOVE_INVALID;
        }

        String piece = s.pop();
        board[index].push(piece);

        blue_turn = !blue_turn;

        return SUCCESSFUL;
    }

    public int checkGameState() {
        int winner = checkWinner();
        if (winner != GAME_IN_PROGRESS)
            return winner;
        if (getAvailableMoves(BLUE).isEmpty() && getAvailableMoves(GREEN).isEmpty())
            return GAME_TIE;
        return GAME_IN_PROGRESS;
    }

    public ArrayList<int[]> getAvailableMoves(boolean player) {
        ArrayList<int[]> moves = new ArrayList<>();

        Stack<String>[] stacks = player == BLUE ? blueStacks : greenStacks;

        for (int i = 0; i < 3; i++) {
            if (stacks[i].empty()) continue;
            for (int j = 0; j < 16; j++) {
                if (board[j].empty() || board[j].peek().charAt(1) < stacks[i].peek().charAt(1)) {
                    int[] ar = {i, j};
                    moves.add(ar);
                }
            }
        }

        return moves;
    }

    public String indexToString(int i) {
        StringBuilder coord = new StringBuilder();
        if (i < 4) coord.append('A');
        else if (i < 8) coord.append('B');
        else if (i < 12) coord.append('C');
        else coord.append('D');
        switch (i % 4) {
            case 0 -> coord.append(1);
            case 1 -> coord.append(2);
            case 2 -> coord.append(3);
            default ->  coord.append(4);
        }
        return coord.toString();
    }

    public int stringToIndex(String i) {
        int r, c;
        switch (i.charAt(0)) {
            case 'A' -> r = 0;
            case 'B' -> r = 1;
            case 'C' -> r = 2;
            case 'D' -> r = 3;
            default -> {return MOVE_INVALID;}
        }
        switch (i.charAt(1)) {
            case '1' -> c = 0;
            case '2' -> c = 1;
            case '3' -> c = 2;
            case '4' -> c = 3;
            default -> {return MOVE_INVALID;}
        }
        return 4 * r + c;
    }

    public int chooseStack(int s) {
        Stack<String> stack = blue_turn ? blueStacks[s-1] : greenStacks[s-1];
        return stack.empty() ? STACK_EMPTY : s;
    }

    private int checkWinner() {
        boolean[] won = {false, false};

        // Columns
        for (int i = 0; i < 4; i++) {
            Stack<String> cell1 = board[i];
            if (cell1.empty()) continue;
            char piece = cell1.peek().charAt(0);
            for (int j = 1; j < 4; j++) {
                Stack<String> cell2 = board[i + 4 * j];
                if (cell2.empty() || cell2.peek().charAt(0) != piece) break;
                if (j == 3) {
                    int index = piece == 'B' ? 0 : 1;
                    won[index] = true;
                }
            }
        }

        // Rows
        for (int i = 0; i < 13; i += 4) {
            Stack<String> cell1 = board[i];
            if (cell1.empty()) continue;
            char piece = cell1.peek().charAt(0);
            for (int j = 1; j < 4; j++) {
                Stack<String> cell2 = board[i + j];
                if (cell2.empty() || cell2.peek().charAt(0) != piece) break;
                if (j == 3) {
                    int index = piece == 'B' ? 0 : 1;
                    won[index] = true;
                }
            }
        }

        // Left Diagonal
        if (!board[0].empty()) {
            char piece = board[0].peek().charAt(0);
            for (int i = 5; i < 16; i += 5) {
                if (board[i].empty() || board[i].peek().charAt(0) != piece) break;
                if (i == 15) {
                    int index = piece == 'B' ? 0 : 1;
                    won[index] = true;
                }
            }
        }

        //Right Diagonal
        if (!board[3].empty()) {
            char piece = board[3].peek().charAt(0);
            for (int i = 6; i < 13; i += 3) {
                if (board[i].empty() || board[i].peek().charAt(0) != piece) break;
                if (i == 12) {
                    int index = piece == 'B' ? 0 : 1;
                    won[index] = true;
                }
            }
        }

        if (won[0] && won[1]) return GAME_TIE;
        else if (won[0]) return BLUE_WINNER;
        else if (won[1]) return GREEN_WINNER;

        return GAME_IN_PROGRESS;
    }

    private void initBoard() {
        board = new Stack[16];
        greenStacks = new Stack[3];
        blueStacks = new Stack[3];
        for (int i = 0; i < 16; i++) {
            board[i] = new Stack<>();
        }
        for (int i = 0; i < 3; i++) {
            greenStacks[i] = new Stack<>();
            blueStacks[i] = new Stack<>();
            for (int j = 1; j <= 4; j++) {
                greenStacks[i].push("G" + j);
                blueStacks[i].push("B" + j);
            }
        }
    }

    public void printBoard() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        String[] b = new String[16];
        for (int i = 0; i < 16; i++) {
            if (board[i].empty()) {
                b[i] = "__";
            } else {
                b[i] = board[i].peek();
            }
        }
        String turn = blue_turn ? "Blue Turn" : "Green Turn";
        System.out.println("-----------------------------\n");
        System.out.println('\n' + turn + '\n');
        System.out.println("   1  2  3  4");
        System.out.println("A |" + b[0] + "|" + b[1] + "|" + b[2] + "|" + b[3] + "|");
        System.out.println("B |" + b[4] + "|" + b[5] + "|" + b[6] + "|" + b[7] + "|");
        System.out.println("C |" + b[8] + "|" + b[9] + "|" + b[10] + "|" + b[11] + "|");
        System.out.println("D |" + b[12] + "|" + b[13] + "|" + b[14] + "|" + b[15] + "|\n\n");
        String[] gs = new String[3], bs = new String[3];
        for (int i = 0; i < 3; i++) {
            gs[i] = greenStacks[i].empty() ? "__" : greenStacks[i].peek();
            bs[i] = blueStacks[i].empty() ? "__" : blueStacks[i].peek();
        }
        System.out.println("Green Stacks:\t" + gs[0] + "\t" + gs[1] + "\t" + gs[2]);
        System.out.println("Blue Stacks:\t" + bs[0] + "\t" + bs[1] + "\t" + bs[2] + '\n');
        System.out.println("-----------------------------\n");
    }
}
