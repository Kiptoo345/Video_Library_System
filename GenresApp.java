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
 * GenresApp - Admin screen for managing movie genres.
 * Connects to the VLS Server via socket and performs genre operations.
 */
public class GenresApp extends Application {

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private ComboBox<String> comboBox;
    private TextField textField1;

    /**
     * Initializes the socket connection to the server.
     */
    private void connectToServer() {
        try {
            socket = new Socket("10.50.3.205", 5000); // ipaddresss of server
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to VLS Server");
        } catch (Exception e) {
            showAlert("Connection Error", "Cannot connect to server: " + e.getMessage());
        }
    }

    /**
     * Loads all genres from the server and populates the combobox.
     */
    private void loadGenres() {
        try {
            out.println("GET_GENRES");
            String response = in.readLine();
            comboBox.getItems().clear();
            if (!response.equals("EMPTY")) {
                for (String genre : response.split(";")) {
                    if (!genre.isEmpty()) {
                        comboBox.getItems().add(genre);
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load genres: " + e.getMessage());
        }
    }

    /**
     * Shows an alert dialog to the user.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) {
        connectToServer();

        Text text1 = new Text("Name");
        Text text2 = new Text("Registered");

        textField1 = new TextField();
        comboBox = new ComboBox<>();

        Button button1 = new Button("Save");
        Button button2 = new Button("Remove");

        loadGenres();

        button1.setOnAction(e -> {
            String name = textField1.getText().trim();
            if (name.isEmpty()) {
                showAlert("Warning", "Please enter a genre name.");
                return;
            }
            try {
                out.println("SAVE_GENRE," + name);
                String response = in.readLine();
                showAlert("Success", response);
                textField1.clear();
                loadGenres();
            } catch (Exception ex) {
                showAlert("Error", "Failed to save genre: " + ex.getMessage());
            }
        });

        button2.setOnAction(e -> {
            String selected = comboBox.getValue();
            if (selected == null) {
                showAlert("Warning", "Select a genre to remove.");
                return;
            }
            try {
                out.println("REMOVE_GENRE," + selected);
                String response = in.readLine();
                showAlert("Success", response);
                loadGenres();
            } catch (Exception ex) {
                showAlert("Error", "Failed to remove genre: " + ex.getMessage());
            }
        });

        GridPane gridpane = new GridPane();
        gridpane.setMinSize(600, 400);
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.setVgap(10);
        gridpane.setHgap(10);
        gridpane.setAlignment(Pos.CENTER);

        gridpane.add(text1, 0, 0);
        gridpane.add(textField1, 1, 0);
        gridpane.add(button1, 1, 1);
        gridpane.add(text2, 0, 2);
        gridpane.add(comboBox, 1, 2);
        gridpane.add(button2, 1, 3);

        button1.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");
        text1.setStyle("-fx-font: normal bold 20px 'serif'");
        text2.setStyle("-fx-font: normal bold 20px 'serif'");
        gridpane.setStyle("-fx-background-color: BEIGE;");

        Scene scene = new Scene(gridpane);
        stage.setTitle("VLS - Manage Genres");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}