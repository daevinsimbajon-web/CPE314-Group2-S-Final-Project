package com.triviagame.players;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.triviagame.db.PlayerDao;

public class Players {

    private final PlayerDao playerDao;

    public Players() {
        playerDao = new PlayerDao();
    }

    public void show(Scanner scanner) throws SQLException {

        boolean running = true;

        while (running) {

            System.out.println("\n===== PLAYERS =====");
            System.out.println("1. View All Players");
            System.out.println("2. Add New Player");
            System.out.println("3. Back to Home");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    viewPlayers();
                    break;

                case "2":
                    addPlayer(scanner);
                    break;

                case "3":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public void viewPlayers() throws SQLException {

        List<PlayerDao.PlayerRecord> players =
                playerDao.getAllPlayers();

        System.out.println("\n--- Registered Players ---");

        if (players.isEmpty()) {
            System.out.println("No players registered yet.");
            return;
        }

        for (PlayerDao.PlayerRecord player : players) {

            System.out.println(
                "ID: " + player.id()
                + " | Name: " + player.name()
            );
        }
    }

    private void addPlayer(Scanner scanner)
            throws SQLException {

        System.out.print("Enter player name: ");

        String name = scanner.nextLine().trim();

        if (name.isBlank()) {
            System.out.println("Player name cannot be empty.");
            return;
        }

        int playerId = playerDao.addPlayer(name);

        System.out.println(
            "Player registered successfully! ID: " + playerId
        );
    }
}
