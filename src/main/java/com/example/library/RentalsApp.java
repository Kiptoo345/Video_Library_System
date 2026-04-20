package com.example.library;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

public class RentalsApp extends Application {
    public void start(Stage stage){

        Text text1 = new Text("Customer:");
        Text text2 = new Text("Genre:");
        Text text3 = new Text("Movies:");
        Text text4 = new Text("Borrowed:");
        Text text5 = new Text("Returned:");

        ComboBox combobox1 = new ComboBox();
        ComboBox combobox2 = new ComboBox();
        ComboBox combobox3 = new ComboBox();
        ComboBox combobox4 = new ComboBox();
        ComboBox combobox5 = new ComboBox();

        Button button1 = new Button("Save Rental");
        Button button2 = new Button("Return Movie");

        GridPane gridpane = new GridPane();
        gridpane.setMinSize(600, 400);
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.setVgap(10);
        gridpane.setHgap(10);
        gridpane.setAlignment(Pos.CENTER);

        gridpane.add(text1, 0, 0);
        gridpane.add(combobox1, 1, 0);

        gridpane.add(text2, 0, 1);
        gridpane.add(combobox2, 1, 1);

        gridpane.add(text3, 0, 2);
        gridpane.add(combobox3, 1, 2);
        gridpane.add(button1, 1, 3);

        gridpane.add(text4, 0, 4);
        gridpane.add(combobox4, 1, 4);
        gridpane.add(button2, 1, 5);

        gridpane.add(text5, 0, 6);
        gridpane.add(combobox5, 1, 6);

        button1.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");

        text1.setStyle("-fx-font: normal bold 20px 'serif'");
        text2.setStyle("-fx-font: normal bold 20px 'serif'");
        text3.setStyle("-fx-font: normal bold 20px 'serif'");
        text4.setStyle("-fx-font: normal bold 20px 'serif'");
        text5.setStyle("-fx-font: normal bold 20px 'serif'");

        gridpane.setStyle("-fx-background-color: BEIGE;");

        Scene scene = new Scene(gridpane);
        stage.setTitle("Movie Library System");
        stage.setScene(scene);
        stage.show();
    }
}
