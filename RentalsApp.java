package com.example.library;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import java.io.*;
import java.net.Socket;

/**
 * RentalsApp - Customer screen for renting and returning movies.
 */
public class RentalsApp extends Application {

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private ComboBox<String> customerCombo, genreCombo, movieCombo, borrowedCombo, returnCombo;
    private String selectedClientId;

    private void connectToServer() {
        try {
            socket = new Socket("10.50.3.205", 5000); // change to server ipaddress
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            showAlert("Connection Error", "Cannot connect to server: " + e.getMessage());
        }
    }

    private void loadClients() {
        try {
            out.println("GET_CLIENTS");
            String response = in.readLine();
            customerCombo.getItems().clear();
            if (!response.equals("EMPTY")) {
                for (String client : response.split(";")) {
                    if (!client.isEmpty()) {
                        customerCombo.getItems().add(client);
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load clients: " + e.getMessage());
        }
    }

    private void loadGenres() {
        try {
            out.println("GET_GENRES");
            String response = in.readLine();
            genreCombo.getItems().clear();
            if (!response.equals("EMPTY")) {
                for (String genre : response.split(";")) {
                    if (!genre.isEmpty()) {
                        genreCombo.getItems().add(genre);
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load genres: " + e.getMessage());
        }
    }

    private void loadMovies() {
        try {
            out.println("GET_MOVIES");
            String response = in.readLine();
            movieCombo.getItems().clear();
            if (!response.equals("EMPTY")) {
                for (String movie : response.split(";")) {
                    if (!movie.isEmpty()) {
                        movieCombo.getItems().add(movie);
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load movies: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) {
        connectToServer();

        Text text1 = new Text("Customer:");
        Text text2 = new Text("Genre:");
        Text text3 = new Text("Movies:");
        Text text4 = new Text("Borrowed:");
        Text text5 = new Text("Returned:");

        customerCombo = new ComboBox<>();
        genreCombo = new ComboBox<>();
        movieCombo = new ComboBox<>();
        borrowedCombo = new ComboBox<>();
        returnCombo = new ComboBox<>();

        Button button1 = new Button("Rent Movie");
        Button button2 = new Button("Return Movie");

        loadClients();
        loadGenres();
        loadMovies();

        customerCombo.setOnAction(e -> {
            String selected = customerCombo.getValue();
            if (selected != null) {
                selectedClientId = selected.split(":")[0];
            }
        });

        button1.setOnAction(e -> {
            if (selectedClientId == null || movieCombo.getValue() == null) {
                showAlert("Warning", "Select customer and movie.");
                return;
            }
            try {
                out.println("RENT_MOVIE," + selectedClientId + "," + movieCombo.getValue());
                String response = in.readLine();
                showAlert("Success", response);
                loadMovies();
            } catch (Exception ex) {
                showAlert("Error", "Failed to rent: " + ex.getMessage());
            }
        });

        button2.setOnAction(e -> {
            if (selectedClientId == null || returnCombo.getValue() == null) {
                showAlert("Warning", "Select customer and movie to return.");
                return;
            }
            try {
                out.println("RETURN_MOVIE," + selectedClientId + "," + returnCombo.getValue());
                String response = in.readLine();
                showAlert("Success", response);
                loadMovies();
            } catch (Exception ex) {
                showAlert("Error", "Failed to return: " + ex.getMessage());
            }
        });

        GridPane gridpane = new GridPane();
        gridpane.setMinSize(600, 400);
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.setVgap(10);
        gridpane.setHgap(10);
        gridpane.setAlignment(Pos.CENTER);

        gridpane.add(text1, 0, 0);
        gridpane.add(customerCombo, 1, 0);
        gridpane.add(text2, 0, 1);
        gridpane.add(genreCombo, 1, 1);
        gridpane.add(text3, 0, 2);
        gridpane.add(movieCombo, 1, 2);
        gridpane.add(button1, 1, 3);
        gridpane.add(text4, 0, 4);
        gridpane.add(borrowedCombo, 1, 4);
        gridpane.add(text5, 0, 5);
        gridpane.add(returnCombo, 1, 5);
        gridpane.add(button2, 1, 6);

        button1.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");
        text1.setStyle("-fx-font: normal bold 20px 'serif'");
        text2.setStyle("-fx-font: normal bold 20px 'serif'");
        text3.setStyle("-fx-font: normal bold 20px 'serif'");
        text4.setStyle("-fx-font: normal bold 20px 'serif'");
        text5.setStyle("-fx-font: normal bold 20px 'serif'");
        gridpane.setStyle("-fx-background-color: BEIGE;");

        Scene scene = new Scene(gridpane);
        stage.setTitle("VLS - Rent Movies");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}