package ui;

import application.Fonts;
import application.Main;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import users.DegreeLookup;
import users.DegreeProgram;
import users.User;
import javafx.scene.text.Font;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Dashboard {

    // Show dashboard for a specific user (so we can display degree greeting)
    public void showDashboard(Stage primaryStage, User user) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1536, 864);
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("SemSync - Dashboard"); // Window title
        primaryStage.setX(visualBounds.getMinX());
        primaryStage.setY(visualBounds.getMinY());
        primaryStage.setWidth(visualBounds.getWidth());
        primaryStage.setHeight(visualBounds.getHeight());

        // Determine greeting from degree
        String degreeCode = (user != null) ? user.getDegree() : null;
        DegreeProgram program = DegreeLookup.fromCode(degreeCode);
        String shortName = (program != null) ? program.getCourseCode() : "";

        String greeting = "Hello, " + user.getUsername().toUpperCase() + " -" + (shortName.isEmpty() ? "" : " " + shortName) + "!";

        Label greetLabel = new Label(greeting);
        Font greetFont = Font.loadFont(Fonts.SENSA_SERIF, 80);
        greetLabel.setFont(greetFont);
        greetLabel.setLayoutX(60);
        greetLabel.setLayoutY(60);
        root.getChildren().add(greetLabel);

        // -- Dropdown --
        ComboBox<String> dropdown = new ComboBox<>();
        dropdown.getItems().addAll("From","dataset","add kau");
        dropdown.setPromptText("Select course:");
        dropdown.setLayoutX(220);
        dropdown.setLayoutY(180);
        dropdown.setPrefWidth(400);
        dropdown.setStyle("-fx-font-size: 18px;");

        // -- Buttons --
        Button btnAdd = new Button("Add");
        btnAdd.setLayoutX(220);
        btnAdd.setLayoutY(260);
        btnAdd.setPrefWidth(120);
        btnAdd.setFont(Fonts.loadSensaWild(24));

        Button btnDelete = new Button("Delete");
        btnDelete.setLayoutX(360);
        btnDelete.setLayoutY(260);
        btnDelete.setPrefWidth(120);
        btnDelete.setFont(Fonts.loadSensaWild(24));


        // -- List of items added by user--
        Label itemsLabel = new Label("Your Items");
        itemsLabel.setFont(Font.font("Montserrat", 28));
        itemsLabel.setLayoutX(220);
        itemsLabel.setLayoutY(330);

        ObservableList<String> listItems = FXCollections.observableArrayList();
        listItems.add("None");
        ListView<String> listView = new ListView<>(listItems);
        listView.setLayoutX(220);
        listView.setLayoutY(370);
        listView.setPrefWidth(1000);
        listView.setPrefHeight(420);

        // Add to root
        root.getChildren().addAll(dropdown, btnAdd, btnDelete, itemsLabel, listView);

        primaryStage.show();
    }

}
