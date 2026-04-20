package com.example.library;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

public class MoviesApp extends Application{
    public void start(Stage stage) {
        Text text1 = new Text("Genres:");
        Text text2 = new Text("Name:");
        Text text3 = new Text("Registered:");

        TextField textfield = new TextField();

        ComboBox comboBox1 = new ComboBox();
        ComboBox comboBox2 = new ComboBox();

        Button button1 = new Button("Save Movie");
        Button button2 = new Button("Remove Movie");

        GridPane gridpane = new GridPane();

        gridpane.setMinSize(600, 400);
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.setVgap(10);
        gridpane.setHgap(10);

        gridpane.setAlignment(Pos.CENTER);
        gridpane.add(text1, 0, 0);
        gridpane.add(comboBox1, 1, 0);

        gridpane.add(text2, 0, 2);
        gridpane.add(textfield, 1, 2);
        gridpane.add(button1, 1, 3);

        gridpane.add(text3, 0, 5);
        gridpane.add(comboBox2, 1, 5);
        gridpane.add(button2, 1, 6);

        button1.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:13pt;");

        text1.setStyle("-fx-font: normal bold 20px 'serif'");
        text2.setStyle("-fx-font: normal bold 20px 'serif'");
        text3.setStyle("-fx-font: normal bold 20px 'serif'");

        gridpane.setStyle("-fx-background-color: BEIGE;");

        Scene scene = new Scene(gridpane);
        stage.setTitle("Movie Library System");
        stage.setScene(scene);
        stage.show();



    }
}
