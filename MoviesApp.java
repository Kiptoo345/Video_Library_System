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
 * MoviesApp - Admin screen for managing movies.
 * Connects to the VLS Server via socket.
 */
public class MoviesApp extends Application {

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private ComboBox<String> genreCombo;
    private ComboBox<String> moviesCombo;
    private TextField textfield;

    private void connectToServer() {
        try {
            socket = new Socket("10.50.3.205", 5000); // server ipaddress
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            showAlert("Connection Error", "Cannot connect to server: " + e.getMessage());
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
            moviesCombo.getItems().clear();
            if (!response.equals("EMPTY")) {
                for (String movie : response.split(";")) {
                    if (!movie.isEmpty()) {
                        moviesCombo.getItems().add(movie);
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

        Text text1 = new Text("Genres:");
        Text text2 = new Text("Name:");
        Text text3 = new Text("Registered:");

        textfield = new TextField();
        genreCombo = new ComboBox<>();
        moviesCombo = new ComboBox<>();

        Button button1 = new Button("Save Movie");
        Button button2 = new Button("Remove Movie");

        loadGenres();
        loadMovies();

        button1.setOnAction(e -> {
            String title = textfield.getText().trim();
            String genre = genreCombo.getValue();
            if (title.isEmpty() || genre == null) {
                showAlert("Warning", "Enter movie name and select genre.");
                return;
            }
            try {
                out.println("SAVE_MOVIE," + title + "," + genre);
                String response = in.readLine();
                showAlert("Success", response);
                textfield.clear();
                loadMovies();
            } catch (Exception ex) {
                showAlert("Error", "Failed to save movie: " + ex.getMessage());
            }
        });

        button2.setOnAction(e -> {
            String selected = moviesCombo.getValue();
            if (selected == null) {
                showAlert("Warning", "Select a movie to remove.");
                return;
            }
            try {
                out.println("REMOVE_MOVIE," + selected);
                String response = in.readLine();
                showAlert("Success", response);
                loadMovies();
            } catch (Exception ex) {
                showAlert("Error", "Failed to remove movie: " + ex.getMessage());
            }
        });

        GridPane gridpane = new GridPane();
        gridpane.setMinSize(600, 400);
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.setVgap(10);
        gridpane.setHgap(50);
        gridpane.setAlignment(Pos.CENTER);

        gridpane.add(text1, 0, 0);
        gridpane.add(genreCombo, 1, 0);
        gridpane.add(text2, 0, 2);
        gridpane.add(textfield, 1, 2);
        gridpane.add(button1, 1, 3);
        gridpane.add(text3, 0, 5);
        gridpane.add(moviesCombo, 1, 5);
        gridpane.add(button2, 1, 6);

        button1.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");
        text1.setStyle("-fx-font: normal bold 20px 'serif'");
        text2.setStyle("-fx-font: normal bold 20px 'serif'");
        text3.setStyle("-fx-font: normal bold 20px 'serif'");
        gridpane.setStyle("-fx-background-color: BEIGE;");

        Scene scene = new Scene(gridpane);
        stage.setTitle("VLS - Manage Movies");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}