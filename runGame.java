import GameModes.LocalGame;

import java.util.Scanner;

public class runGame {

    public static void main(String[] args) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("\n*** Tic-Stack-Toe ***");
        System.out.println("1: Play vs AI");
        System.out.println("2: Play vs a Friend");
        System.out.println("3: Play Online\n");

        Scanner scanner = new Scanner(System.in);
        char input;
        while (true) {
            System.out.print("Game Mode: ");
            input = scanner.nextLine().charAt(0);
            boolean valid_input = input == '1' || input == '2' || input == '3';

            if (valid_input) break;

            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("\nPlease Pick a Valid Game Mode:");
            System.out.println("1: Play vs AI");
            System.out.println("2: Play vs a Friend");

            System.out.println("3: Play Online\n");
        }

        int gamemode = Character.getNumericValue(input);
        switch (gamemode) {
            case 1 -> {
                System.out.println("Playing vs AI");
            }
            case 2 -> {
                System.out.println("Playing vs Friend");
                LocalGame game = new LocalGame(scanner);
                game.play();
            }
            case 3 -> {
                System.out.println("Playing Online");
            }
        }
        scanner.close();
    }
}