package com.triviagame.home;

import java.sql.SQLException;
import java.util.Scanner;

import com.triviagame.db.Database;

import com.triviagame.Main;
import com.triviagame.players.Players;
import com.triviagame.results.Results;

public class Home {

    public static void main(String[] args) {

        try {
            Database.initializeSchema();

            Scanner scanner = new Scanner(System.in);

            Players players = new Players();
            Results results = new Results();

            boolean running = true;

            while (running) {

                System.out.println("\n========================");
                System.out.println("      TRIVIA GAME");
                System.out.println("========================");

                System.out.println("1. Play Game");
                System.out.println("2. Players");
                System.out.println("3. Results");
                System.out.println("4. Exit");

                System.out.print("\nChoose an option: ");

                String choice = scanner.nextLine().trim();

                try {

                    switch (choice) {

                        case "1":
                            Main.startGame(scanner);
                            break;

                        case "2":
                            players.show(scanner);
                            break;

                        case "3":
                            results.show(scanner);
                            break;

                        case "4":
                            running = false;
                            System.out.println(
                                "Thank you for playing!"
                            );
                            break;

                        default:
                            System.out.println(
                                "Invalid option. Try again."
                            );
                    }

                } catch (SQLException e) {

                    System.out.println(
                        "Database operation failed: "
                        + e.getMessage()
                    );
                }
            }

            scanner.close();

        } catch (SQLException e) {

            System.err.println(
                "Unable to initialize database."
            );

            e.printStackTrace();
        }
    }
}
