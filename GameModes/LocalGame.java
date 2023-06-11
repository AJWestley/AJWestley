package GameModes;

import java.util.Scanner;

public class LocalGame {
    private Game game;
    private Scanner scanner;

    public LocalGame(Scanner scanner) {
        game = new Game();
        this.scanner = scanner;
    }

    public void play() {
        String input;
        while (true) {
            game.printBoard();
            int gs = game.checkGameState();
            if (gs == Game.GAME_TIE) {
                System.out.println("\nGAME TIED");
                break;
            } else if (gs == Game.BLUE_WINNER) {
                System.out.println("\nBLUE WINS");
                break;
            } else if (gs == Game.GREEN_WINNER) {
                System.out.println("\nGREEN WINS");
                break;
            }
            int stack_choice = Game.STACK_EMPTY;
            while (stack_choice == Game.STACK_EMPTY) {
                System.out.print("\nChoose a stack: ");
                input = scanner.nextLine();
                char s = input.charAt(0);
                if (!Character.isDigit(s)) {
                    System.out.println("Invalid Stack");
                    continue;
                }
                int i = Integer.parseInt(Character.toString(s));
                if (i < 1 || i > 3) {
                    System.out.println("Invalid Stack");
                    continue;
                }
                stack_choice = game.chooseStack(i);
            }
            int move = Game.MOVE_INVALID;
            while (move != Game.SUCCESSFUL) {
                System.out.print("\nChoose a cell: ");
                input = scanner.nextLine();
                String cell = input.substring(0, 2);
                move = game.makeMove(stack_choice, cell);
                switch (move) {
                    case Game.STACK_EMPTY -> System.out.println("Stack Empty");
                    case Game.MOVE_INVALID -> System.out.println("Invalid Move");
                }
            }
        }

        System.out.println("\nGameModes.Game Over\nPress enter to close the program");
        input = scanner.nextLine();
    }

}
