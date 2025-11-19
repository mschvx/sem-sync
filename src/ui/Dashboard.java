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
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;

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


        // menu button
        Button menuBtn = new Button("☰");
        menuBtn.getStyleClass().add("btn-menu");
        menuBtn.setLayoutX(12);
        menuBtn.setLayoutY(40);
        menuBtn.setPrefWidth(56);
        menuBtn.setPrefHeight(56);
        menuBtn.setStyle("-fx-font-size: 22px; -fx-background-radius: 28; -fx-border-radius: 28;");

        // sidebar left
        Pane sidebar = new Pane();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setLayoutX(0);
        sidebar.setLayoutY(0);
        double sidebarWidth = 260;
        sidebar.setPrefWidth(sidebarWidth);
        sidebar.setPrefHeight(visualBounds.getHeight());
        sidebar.setVisible(false); // hidden default; will transition when clicked
        sidebar.setTranslateX(-sidebarWidth); // start off-screen

        // Sidebar content
        Label sideTitle = new Label("MENU");
        sideTitle.setStyle("-fx-text-fill: white; -fx-letter-spacing: 1px;");
        sideTitle.setFont(Fonts.loadSensaWild(34));
        sideTitle.setLayoutX(40);
        sideTitle.setLayoutY(40);

        Button btnAbout = new Button("About");
        btnAbout.setLayoutX(40);
        btnAbout.setLayoutY(80);
        btnAbout.setPrefWidth(180);
        btnAbout.getStyleClass().add("btn-sidebar");
        btnAbout.setFont(Fonts.loadMontserratRegular(18));
        btnAbout.setOnAction(ev -> {
            new About().showAbout(primaryStage);
        });

        Button btnCredits = new Button("Credits");
        btnCredits.setLayoutX(40);
        btnCredits.setLayoutY(120);
        btnCredits.setPrefWidth(180);
        btnCredits.getStyleClass().add("btn-sidebar");
        btnCredits.setFont(Fonts.loadMontserratRegular(18));
        btnCredits.setOnAction(ev -> {
            new Credits().showCredits(primaryStage);
        });

        Button btnReferences = new Button("References");
        btnReferences.setLayoutX(40);
        btnReferences.setLayoutY(160);
        btnReferences.setPrefWidth(180);
        btnReferences.getStyleClass().add("btn-sidebar");
        btnReferences.setFont(Fonts.loadMontserratRegular(18));
        btnReferences.setOnAction(ev -> {
            new References().showReferences(primaryStage);
        });

        Button btnDash = new Button("Dashboard");
        btnDash.setLayoutX(40);
        btnDash.setLayoutY(210);
        btnDash.setPrefWidth(180);
        btnDash.getStyleClass().add("btn-sidebar");
        btnDash.setFont(Fonts.loadMontserratRegular(18));

        Button btnLogout = new Button("Logout");
        btnLogout.setLayoutX(40);
        btnLogout.setLayoutY(270);
        btnLogout.setPrefWidth(180);
        btnLogout.getStyleClass().add("btn-logout");
        btnLogout.setFont(Fonts.loadMontserratRegular(16));
        // Logout will return to main login screen and mark logged out
        btnLogout.setOnAction(ev -> {
            try {
                Main.isLoggedIn = false;
                Main.loggedInUser = null;
                Main main = new Main();
                main.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        sidebar.getChildren().addAll(sideTitle, btnAbout, btnCredits, btnReferences, btnDash, btnLogout);

        // --- Content pane: put dashboard UI here so it can shift ---
        Pane content = new Pane();
        content.setLayoutX(0);
        content.setLayoutY(0);

        root.getChildren().addAll(sidebar, content, menuBtn);

        // Toggle sidebar animation 
        SimpleBooleanProperty open = new SimpleBooleanProperty(false);
        final double menuNudge = 48; // how far the menu button moves right when sidebar opens
        menuBtn.setOnAction(e -> {
            if (!open.get()) {
                // when sidebar clicked: make visible, slide in, shift content right
                sidebar.setVisible(true);
                TranslateTransition tSidebar = new TranslateTransition(Duration.millis(260), sidebar);
                tSidebar.setFromX(-sidebarWidth);
                tSidebar.setToX(0);
                TranslateTransition tContent = new TranslateTransition(Duration.millis(260), content);
                tContent.setToX(sidebarWidth);
                TranslateTransition tMenu = new TranslateTransition(Duration.millis(260), menuBtn);
                tMenu.setToX(menuNudge);
                tSidebar.play(); tContent.play(); tMenu.play();
                open.set(true);
            } else {
                // if close then slide sidebar out, shift content back, then hide
                TranslateTransition tSidebar = new TranslateTransition(Duration.millis(260), sidebar);
                tSidebar.setFromX(0);
                tSidebar.setToX(-sidebarWidth);
                TranslateTransition tContent = new TranslateTransition(Duration.millis(260), content);
                tContent.setToX(0);
                TranslateTransition tMenu = new TranslateTransition(Duration.millis(260), menuBtn);
                tMenu.setToX(0);
                tSidebar.setOnFinished(ev -> sidebar.setVisible(false));
                tSidebar.play(); tContent.play(); tMenu.play();
                open.set(false);
            }
        });

        // Determine greeting from degree
        String degreeCode = (user != null) ? user.getDegree() : null;
        DegreeProgram program = DegreeLookup.fromCode(degreeCode);
        String shortName = (program != null) ? program.getCourseCode() : "";

        String greeting = "Hello, " + user.getUsername().toUpperCase() + " -" + (shortName.isEmpty() ? "" : " " + shortName) + "!";

        Label greetLabel = new Label(greeting);
        Font greetFont = Font.loadFont(Fonts.SENSA_SERIF, 80);
        greetLabel.setFont(greetFont);
        greetLabel.setLayoutX(220);
        greetLabel.setLayoutY(60);
        content.getChildren().add(greetLabel);

        // -- Dropdown --
        ComboBox<String> dropdown = new ComboBox<>();
        dropdown.getItems().addAll("From", "dataset", "add kau");
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

        // -- List of items added by user --
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

        // Add to pane
        content.getChildren().addAll(dropdown, btnAdd, btnDelete, itemsLabel, listView);

        primaryStage.show();
    }
}
