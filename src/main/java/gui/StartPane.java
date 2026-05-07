package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StartPane extends VBox {

    public StartPane(Stage primaryStage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
        this.setStyle("-fx-background-color: #222;");

        Label title = new Label("SELECT YOUR CHARACTER");
        title.setFont(new Font("Arial", 40));
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        HBox charSelection = new HBox(20);
        charSelection.setAlignment(Pos.CENTER);

        Button knightBtn = createCharButton("KNIGHT (High HP)", "#555");
        Button mageBtn = createCharButton("MAGE (Wide Attack)", "#555");
        Button ninjaBtn = createCharButton("NINJA (Fast Move)", "#555");

        knightBtn.setOnAction(e -> startGame(primaryStage, "knight"));
        mageBtn.setOnAction(e -> startGame(primaryStage, "mage"));
        ninjaBtn.setOnAction(e -> startGame(primaryStage, "ninja"));

        charSelection.getChildren().addAll(knightBtn, mageBtn, ninjaBtn);
        this.getChildren().addAll(title, charSelection);
    }

    private Button createCharButton(String name, String color) {
        Button btn = new Button(name);
        btn.setPrefSize(180, 100);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        return btn;
    }

    private void startGame(Stage stage, String type) {
        GamePane gamePane = new GamePane(type);
        Scene gameScene = new Scene(gamePane, 800, 600);
        gamePane.setupInput(gameScene);

        stage.setScene(gameScene);
        gamePane.startGameLoop();
    }
}