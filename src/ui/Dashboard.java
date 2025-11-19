package ui;

import application.Fonts;
import application.Main;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import users.DegreeLookup;
import users.DegreeProgram;
import users.User;
import javafx.scene.text.Font;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;

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
        Image menuImage = null;
        ImageView menuBtn = new ImageView();
        File menuFile = new File("Elements/Menu.png");
        menuImage = new Image(menuFile.toURI().toString());
        menuBtn.setImage(menuImage);
        menuBtn.setFitWidth(130);
        menuBtn.setFitHeight(130);
        menuBtn.setPreserveRatio(true);
        menuBtn.setLayoutX(12);
        menuBtn.setLayoutY(10);


        // sidebar left 
        Pane sidebar = new Pane();
        sidebar.setLayoutX(0);
        sidebar.setLayoutY(0);
        double sidebarWidth = 370;
        double contentShift = 200;   // control this if masyadong malayo yung animation nya sa right
        sidebar.setPrefWidth(sidebarWidth);
        sidebar.setPrefHeight(visualBounds.getHeight());
        // hidden by default; menu button will toggle it
        sidebar.setVisible(false);
        sidebar.setTranslateX(-sidebarWidth);

        // load 
        File imgFile = new File("Elements/Sidebar.png");
        Image sidebarImg = new Image(imgFile.toURI().toString());
        ImageView bg = new ImageView(sidebarImg);
        bg.setFitWidth(sidebarWidth);
        bg.setFitHeight(visualBounds.getHeight());
        bg.setPreserveRatio(false);
        bg.setLayoutX(0);
        bg.setLayoutY(0);
        sidebar.getChildren().add(bg);


        // Sidebar content
        int buttonXPos = 30;
        Label sideTitle = new Label("MENU");
        sideTitle.setStyle("-fx-text-fill: white; -fx-letter-spacing: 1px;");
        sideTitle.setFont(Fonts.loadSensaSerif(110));
        sideTitle.setLayoutX(40);
        sideTitle.setLayoutY(10);
        sideTitle.setStyle("-fx-text-fill: black; -fx-letter-spacing: 1px;");


        Button btnAbout = new Button("ABOUT");
        btnAbout.setLayoutX(buttonXPos);
        btnAbout.setLayoutY(140);
        btnAbout.setPrefWidth(sidebarWidth - 80);
        btnAbout.setPrefHeight(48);
        btnAbout.setFont(Fonts.loadSensaWild(22));
        btnAbout.getStyleClass().add("btn-about");
        btnAbout.getStyleClass().add("sidebar-pill");
        btnAbout.setOnAction(ev -> new About().showAbout(primaryStage));

        Button btnTutorial = new Button("TUTORIAL");
        btnTutorial.setLayoutX(buttonXPos);
        btnTutorial.setLayoutY(200);
        btnTutorial.setPrefWidth(sidebarWidth - 80);
        btnTutorial.setPrefHeight(48);
        btnTutorial.setFont(Fonts.loadSensaWild(22));
        btnTutorial.getStyleClass().add("btn-register");
        btnTutorial.getStyleClass().add("sidebar-pill");
        btnTutorial.setOnAction(ev -> { /* waley pa */ });

        Button btnCatalogue = new Button("CATALOGUE");
        btnCatalogue.setLayoutX(buttonXPos);
        btnCatalogue.setLayoutY(260);
        btnCatalogue.setPrefWidth(sidebarWidth - 80);
        btnCatalogue.setPrefHeight(48);
        btnCatalogue.setFont(Fonts.loadSensaWild(22));
        btnCatalogue.getStyleClass().add("btn-create");
        btnCatalogue.getStyleClass().add("sidebar-pill");
        btnCatalogue.setOnAction(ev -> { /* wla pa*/ });

        // eye image
        File eyelidFile = new File("Elements/eyelid.png");
        Image eyelidImg = new Image(eyelidFile.toURI().toString());
        ImageView eyelidView = new ImageView(eyelidImg);
        eyelidView.setPreserveRatio(true);
        eyelidView.setFitWidth(sidebarWidth - 80); // for aesthetic
        eyelidView.setLayoutX(buttonXPos);
        eyelidView.setLayoutY(360);

        File eyeFile = new File("Elements/eye.png");
        Image eyeImg = new Image(eyeFile.toURI().toString());
        ImageView eyeView = new ImageView(eyeImg);
        eyeView.setPreserveRatio(true);
        eyeView.setFitWidth(sidebarWidth - 80);
        eyeView.setLayoutX(buttonXPos);
        eyeView.setLayoutY(360);

        // Anger overlay image 
        final Image angerImg = new Image(new File("Elements/yourenotsupposedtobehere/ANGER.png").toURI().toString());
        ImageView angerView = new ImageView(angerImg);
        angerView.setPreserveRatio(true);
        angerView.setFitWidth(sidebarWidth - 80);
        angerView.setLayoutX(buttonXPos);
        angerView.setLayoutY(360);
        angerView.setVisible(false);

        Button btnCredits = new Button("CREDITS");
        btnCredits.setLayoutX(buttonXPos);
        btnCredits.setLayoutY(570);
        btnCredits.setPrefWidth(sidebarWidth - 80);
        btnCredits.setPrefHeight(48);
        btnCredits.setFont(Fonts.loadSensaWild(22));
        btnCredits.getStyleClass().add("btn-about");
        btnCredits.setOnAction(ev -> new Credits().showCredits(primaryStage));
        btnCredits.getStyleClass().add("sidebar-pill");

        Button btnReferences = new Button("REFERENCES");
        btnReferences.setLayoutX(buttonXPos);
        btnReferences.setLayoutY(630);
        btnReferences.setPrefWidth(sidebarWidth - 80);
        btnReferences.setPrefHeight(48);
        btnReferences.setFont(Fonts.loadSensaWild(22));
        btnReferences.getStyleClass().add("btn-register");
        btnReferences.getStyleClass().add("sidebar-pill");
        btnReferences.setOnAction(ev -> new References().showReferences(primaryStage));

        // Logout button: 
        Button btnLogout = new Button("LOG OUT");
        btnLogout.setLayoutX(buttonXPos);
        btnLogout.setLayoutY(700);
        btnLogout.setPrefWidth(sidebarWidth - 80);
        btnLogout.setPrefHeight(58); 
        btnLogout.setFont(Fonts.loadMontserratBold(22));
        btnLogout.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #ff827f, #ff4c4c);"
            + " -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 22px;"
            + " -fx-border-radius: 28; -fx-background-radius: 28; -fx-padding: 12 18 12 18;"
            + " -fx-effect: none; -fx-border-color: rgba(255,255,255,0.12); -fx-border-width: 1;"
            + " -fx-cursor: default; -fx-min-height: 56px;");
        // when hover hide eyelid eand eye.png
        btnLogout.setOnMouseEntered(ev -> {
            try {
                eyelidView.setVisible(false);
                eyeView.setVisible(false);
                angerView.setVisible(true);
            } catch (Exception ex) {
            }
        });
        btnLogout.setOnMouseExited(ev -> {
            try {
                angerView.setVisible(false);
                eyeView.setVisible(true);
                eyelidView.setVisible(true);
            } catch (Exception ex) {
            }
        });
        btnLogout.setFocusTraversable(false);

        btnLogout.setOnAction(ev -> {
            try {
                Main.isLoggedIn = false;
                Main.loggedInUser = null;
                Main main = new Main();
                main.start(primaryStage); // go back to login screen
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // sidebar add to the screen all
        sidebar.getChildren().addAll(sideTitle, btnAbout, btnTutorial, btnCatalogue, eyeView, eyelidView, angerView, btnCredits, btnReferences, btnLogout);

        // --- Contents ---
        Pane content = new Pane();
        content.setLayoutX(0);
        content.setLayoutY(0);

        root.getChildren().addAll(sidebar, content, menuBtn);

        // --- Profile image ---
        final Image profile1 = new Image(new File("Elements/Profile1.png").toURI().toString());
        final Image profile2 = new Image(new File("Elements/Profile2.png").toURI().toString());
        final ImageView profileView = new ImageView(profile1);
        profileView.setPreserveRatio(true);
        profileView.setFitWidth(200); 
        // bind to scene 
        profileView.layoutXProperty().bind(scene.widthProperty().subtract(profileView.fitWidthProperty()).subtract(0));
        profileView.setLayoutY(10);
        root.getChildren().add(profileView);
        profileView.toFront();

        // profile animation
        final Timeline profileTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), ev -> {
            if (profileView.getImage() == profile1) {
                profileView.setImage(profile2);
            } else {
                profileView.setImage(profile1);
            }
        }));
        profileTimeline.setCycleCount(Timeline.INDEFINITE);

        profileView.setOnMouseEntered(ev -> profileTimeline.play());
        profileView.setOnMouseExited(ev -> {
            profileTimeline.stop();
            profileView.setImage(profile1);
        });


        // Make the eye follow the mouse cursor by rotating the eye image around its center
        final javafx.scene.transform.Rotate eyeRotate = new javafx.scene.transform.Rotate(0, 0, 0);
        eyeView.getTransforms().add(eyeRotate);
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, ev -> {
            try {
                javafx.geometry.Bounds localBounds = eyeView.getBoundsInLocal();
                double pivotLocalX = localBounds.getWidth() * 0.5;
                double pivotLocalY = localBounds.getHeight() * 0.5;
                eyeRotate.setPivotX(pivotLocalX);
                eyeRotate.setPivotY(pivotLocalY);

                javafx.geometry.Point2D pivotScene = eyeView.localToScene(pivotLocalX, pivotLocalY);
                double dx = ev.getSceneX() - pivotScene.getX();
                double dy = ev.getSceneY() - pivotScene.getY();
                double angle = Math.toDegrees(Math.atan2(dy, dx));
                // rotate to retain original position (since for some reason nasa right side ang default)
                final double ROTATION_OFFSET = -90.0; // subtract 90 degrees
                eyeRotate.setAngle(angle + ROTATION_OFFSET);
            } catch (Exception ex) {
                // none
            }
        });

        // Menu hover rotation every 0.5 seconds
        final Timeline menuTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), ev -> {
            if (menuBtn.getRotate() == 3) {
                menuBtn.setRotate(-3);
            } else {
                menuBtn.setRotate(3);
            }
        }));
        menuTimeline.setCycleCount(Timeline.INDEFINITE);

        menuBtn.setOnMouseEntered(e -> menuTimeline.play());
        menuBtn.setOnMouseExited(e -> {
            menuTimeline.stop();
            menuBtn.setRotate(0);
        });

        // Toggle sidebar animation 
        SimpleBooleanProperty open = new SimpleBooleanProperty(false);
        final double menuNudge = 185; // how far the menu button moves right when sidebar opens
        menuBtn.setOnMouseClicked(e -> {
            if (!open.get()) {
                // when sidebar clicked: make visible, slide in, shift content right
                sidebar.setVisible(true);
                sidebar.toFront(); // always sidebar to front so all buttosn will always be clickable
                TranslateTransition tSidebar = new TranslateTransition(Duration.millis(260), sidebar);
                tSidebar.setFromX(-sidebarWidth);
                tSidebar.setToX(0);
                TranslateTransition tContent = new TranslateTransition(Duration.millis(260), content);
                tContent.setToX(contentShift);
                TranslateTransition tMenu = new TranslateTransition(Duration.millis(260), menuBtn);
                tMenu.setToX(menuNudge);
                tSidebar.play(); tContent.play(); tMenu.play();
                menuBtn.toFront();
                //  profile stays on top of everything
                try { profileView.toFront(); } catch (Exception ex) { }
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
                tSidebar.setOnFinished(ev -> { sidebar.setVisible(false); menuBtn.toFront(); try { profileView.toFront(); } catch (Exception ex) {} });
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
        itemsLabel.setFont(Fonts.loadMontserratRegular(28));
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
