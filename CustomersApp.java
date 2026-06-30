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
 * CustomersApp - Admin screen for managing customers.
 */
public class CustomersApp extends Application {

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private ComboBox<String> comboBox;
    private TextField textfield1, textfield2, textfield3;

    private void connectToServer() {
        try {
            socket = new Socket("10.50.3.205", 5000); // server ipaddress
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            showAlert("Connection Error", "Cannot connect to server: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        try {
            out.println("GET_CLIENTS");
            String response = in.readLine();
            comboBox.getItems().clear();
            if (!response.equals("EMPTY")) {
                for (String client : response.split(";")) {
                    if (!client.isEmpty()) {
                        comboBox.getItems().add(client);
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load customers: " + e.getMessage());
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

        Text text1 = new Text("Name:");
        Text text2 = new Text("Phone:");
        Text text3 = new Text("Email:");
        Text text4 = new Text("Registered:");

        textfield1 = new TextField();
        textfield2 = new TextField();
        textfield3 = new TextField();
        comboBox = new ComboBox<>();

        Button button1 = new Button("Save Customer");
        Button button2 = new Button("Remove Customer");

        loadCustomers();

        button1.setOnAction(e -> {
            String name = textfield1.getText().trim();
            String phone = textfield2.getText().trim();
            String email = textfield3.getText().trim();
            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                showAlert("Warning", "Fill in all fields.");
                return;
            }
            try {
                out.println("SAVE_CLIENT," + name);
                String response = in.readLine();
                showAlert("Success", response);
                textfield1.clear();
                textfield2.clear();
                textfield3.clear();
                loadCustomers();
            } catch (Exception ex) {
                showAlert("Error", "Failed to save customer: " + ex.getMessage());
            }
        });

        button2.setOnAction(e -> {
            String selected = comboBox.getValue();
            if (selected == null) {
                showAlert("Warning", "Select a customer to remove.");
                return;
            }
            try {
                out.println("REMOVE_CLIENT," + selected);
                String response = in.readLine();
                showAlert("Success", response);
                loadCustomers();
            } catch (Exception ex) {
                showAlert("Error", "Failed to remove customer: " + ex.getMessage());
            }
        });

        GridPane gridpane = new GridPane();
        gridpane.setMinSize(600, 400);
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.setVgap(10);
        gridpane.setHgap(10);
        gridpane.setAlignment(Pos.CENTER);

        gridpane.add(text1, 0, 0);
        gridpane.add(textfield1, 1, 0);
        gridpane.add(text2, 0, 1);
        gridpane.add(textfield2, 1, 1);
        gridpane.add(text3, 0, 2);
        gridpane.add(textfield3, 1, 2);
        gridpane.add(button1, 1, 3);
        gridpane.add(text4, 0, 4);
        gridpane.add(comboBox, 1, 4);
        gridpane.add(button2, 1, 5);

        button1.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");
        text1.setStyle("-fx-font: normal bold 20px 'serif'");
        text2.setStyle("-fx-font: normal bold 20px 'serif'");
        text3.setStyle("-fx-font: normal bold 20px 'serif'");
        text4.setStyle("-fx-font: normal bold 20px 'serif'");
        gridpane.setStyle("-fx-background-color: BEIGE;");

        Scene scene = new Scene(gridpane);
        stage.setTitle("VLS - Manage Customers");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}