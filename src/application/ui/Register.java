package application.ui;

import application.Fonts;
import application.Main;
import application.models.User;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;


public class Register {

    private Timeline envelopeTimeline; // <- declare this at class level

    Path path = Path.of(System.getProperty("user.dir"));

    // used for validating if user created does not already exist
    private ObservableList<User> loadData(Path path) {
        ObservableList<User> list = FXCollections.<User>observableArrayList();
        Path folder = Paths.get("Database");
        Path file = folder.resolve("users.csv");

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                String username = parts[0].trim();
                String password = parts[1].trim();

                String degree = "";
                if (parts.length >= 3) {
                    degree = parts[2].trim();
                }

                list.add(new User(username, password, degree));
            }

        } catch (IOException e) {
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("Cannot Load.");
            errorAlert.showAndWait();
            return list;
        }

        return list;
    }

    public void showRegister(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1536, 864);
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());

        // Load background image
        ImageView background = new ImageView(new Image("file:Elements/RegisterPaper.png"));
        background.setFitHeight(1000);
        background.setLayoutX(0);
        background.setLayoutY(-50);
        root.getChildren().add(background);

        // Register text
        Label registerLabel = new Label("REGISTER");
        Font registerFont = Font.loadFont(Fonts.SENSA_SERIF, 120);
        registerLabel.setFont(registerFont);
        registerLabel.setLayoutX(150);
        registerLabel.setLayoutY(200);
        root.getChildren().add(registerLabel);

        // Username text
        Label usernameLabel = new Label("Username");
        Font usernameFont = Font.loadFont(Fonts.COMING_SOON, 60);
        usernameLabel.setFont(usernameFont);
        usernameLabel.setLayoutX(100);
        usernameLabel.setLayoutY(300);
        root.getChildren().add(usernameLabel);

        // Password text
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(usernameFont);
        passwordLabel.setLayoutX(100);
        passwordLabel.setLayoutY(480);
        root.getChildren().add(passwordLabel);

        // Select Degree Program text
        Label degreeLabel = new Label("Select Degree Program");
        degreeLabel.setFont(usernameFont);
        degreeLabel.setLayoutX(750);
        degreeLabel.setLayoutY(260);
        root.getChildren().add(degreeLabel);

        // Degree program options
        String[] degreeNames = {
            "BS Computer Science",
            "MS Computer Science",
            "Master of Information Technology",
            "PhD Computer Science"
        };

        String[] degreeCodes = {
            "BachelorCS",
            "MasterCS",
            "MasterIT",
            "PHDCS"
        };
        double startY = 360;
        Font degreeFont = Font.font("Montserrat", 30);

        // Add toggles beside the names of degprog
        ToggleGroup degreeGroup = new ToggleGroup();
        for (int i = 0; i < degreeNames.length; i++) {
            String degree = degreeNames[i];
            String code = degreeCodes[i];
            ToggleButton degBtn = new ToggleButton();
            degBtn.getStyleClass().add("degree-btn");
            degBtn.setToggleGroup(degreeGroup);
            degBtn.setUserData(code);
            degBtn.setPrefWidth(46);
            degBtn.setPrefHeight(46);
            degBtn.setLayoutX(780);
            degBtn.setLayoutY(startY - 6);

            // effects when clicked
            degBtn.setOnAction(e -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(140), degBtn);
                st.setFromX(1.0);
                st.setFromY(1.0);
                st.setToX(0.86);
                st.setToY(0.86);
                st.setCycleCount(2);
                st.setAutoReverse(true);
                st.play();
            });

            // degree label
            Label deg = new Label(degree);
            deg.setFont(degreeFont);
            deg.setLayoutX(840);
            deg.setLayoutY(startY);

            root.getChildren().addAll(degBtn, deg);
            startY += 70;
        }

        // Go Back Button
        Button goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("btn-back");
        goBackButton.setLayoutX(1220);
        goBackButton.setLayoutY(30);
        goBackButton.setPrefWidth(250);
        goBackButton.setPrefHeight(50);
        goBackButton.setFont(Fonts.loadSensaWild(40));
        root.getChildren().add(goBackButton);

        // Go Back event handler
        goBackButton.setOnAction(e -> {
            Main main = new Main();
            main.start(primaryStage); // open back to login
        });
        
        // Create Account button
        Button createButton = new Button("Create Account");
        createButton.getStyleClass().add("btn-create");
        createButton.setLayoutX(768); // adjust as needed
        createButton.setLayoutY(640);
        createButton.setPrefWidth(600);
        createButton.setPrefHeight(90);
        createButton.setFont(Fonts.loadSensaWild(40));
        root.getChildren().add(createButton);
        
        // Username text field
        TextField usernameField = new TextField();
        usernameField.setLayoutX(100);
        usernameField.setLayoutY(400);
        usernameField.setPrefWidth(600);
        usernameField.setPrefHeight(60);
        usernameField.setFont(javafx.scene.text.Font.font("Montserrat", 30));
        usernameField.setStyle("-fx-background-color: #ebebeb; " +
                "-fx-border-color: black; " +
                "-fx-border-radius: 15; " +
                "-fx-background-radius: 15; " +
                "-fx-text-fill: black;" +
                "-fx-border-width: 4;");
        root.getChildren().add(usernameField);

        // Password text field
        PasswordField passwordField = new PasswordField();
        passwordField.setLayoutX(100);
        passwordField.setLayoutY(580);
        passwordField.setPrefWidth(600);
        passwordField.setPrefHeight(60);
        passwordField.setFont(javafx.scene.text.Font.font("Montserrat", 30));
        passwordField.setStyle("-fx-background-color: #ebebeb; " +
                "-fx-border-color: black; " +
                "-fx-border-radius: 15; " +
                "-fx-background-radius: 15; " +
                "-fx-text-fill: black;" +
                "-fx-border-width: 4;");
        root.getChildren().add(passwordField);
        
     // Loading data from CSV File
        ObservableList<User> data = loadData(path);
        
        // logic for creating account
        createButton.setOnAction(e -> {
            String uname = usernameField.getText().trim();
            String pass = passwordField.getText().trim();
            ToggleButton selectedDegree = (ToggleButton) degreeGroup.getSelectedToggle();
            boolean usernameFound = false;
            
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            

            // Username and password must be provided
            if (uname.isBlank() || pass.isEmpty()) {
                errorAlert.setHeaderText("Missing Fields!");
                errorAlert.setContentText("Please fill in both username and password.");
                errorAlert.showAndWait();
                return;
            }
            // Username contains only letters and numbers
            if (!isAlphanumeric(uname)) {
                errorAlert.setHeaderText("Invalid Username");
                errorAlert.setContentText("Username can only contain letters and numbers.\nNo spaces or special characters allowed.");
                errorAlert.showAndWait();
                return;
            }

            // Password contains only letters and numbers
            if (!isAlphanumeric(pass)) {
                errorAlert.setHeaderText("Invalid Password");
                errorAlert.setContentText("Password can only contain letters and numbers.\nNo spaces or special characters allowed.");
                errorAlert.showAndWait();
                return;
            }

            // a degree program must be selected
            if (selectedDegree == null) {
                errorAlert.setHeaderText("Missing Degree Program!");
                errorAlert.setContentText("Please select a degree program.");
                errorAlert.showAndWait();
                return;
            }

            // username is unique
            for (User user : data) {
                if (user.getUsername().equals(uname)) {
                    usernameFound = true;
                    break;
                }
            }
            if (usernameFound) {
                errorAlert.setHeaderText("Invalid Username");
                errorAlert.setContentText("This username is already taken. Please choose another one.");
                errorAlert.showAndWait();
                return;
            }

            // Append new user to users file 
                Path folder = Paths.get("Database");
                Path file = folder.resolve("users.csv");
            try {
                Files.createDirectories(folder);
                ToggleButton selectedDegreeBtn = (ToggleButton) degreeGroup.getSelectedToggle();
                String degreeCode = "";
                if (selectedDegreeBtn != null && selectedDegreeBtn.getUserData() != null) {
                    degreeCode = selectedDegreeBtn.getUserData().toString();
                }
                try (BufferedWriter writer = Files.newBufferedWriter(file, java.nio.charset.StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                    writer.write(uname + "," + pass + "," + degreeCode);
                    writer.newLine();
                }
            } catch (IOException ioEx) {
                errorAlert.setHeaderText("Error");
                errorAlert.setContentText("Unable to save account. Please try again.");
                errorAlert.showAndWait();
                return;
            }

            // update in-memory list so user can login immediately
            // use degreeCode from the selected button
            String degreeCodeForMemory = "";
            ToggleButton selectedDegreeBtn = (ToggleButton) degreeGroup.getSelectedToggle();
            if (selectedDegreeBtn != null && selectedDegreeBtn.getUserData() != null) {
                degreeCodeForMemory = selectedDegreeBtn.getUserData().toString();
            }
            data.add(new User(uname, pass, degreeCodeForMemory));

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Account Created");
            success.setHeaderText(null);
            success.setContentText("Account created. Now go log in!");
            success.showAndWait();

            // go back to log in page
            Main main = new Main();
            main.start(primaryStage);
        });


        // Set up window properties
        primaryStage.setScene(scene);
        primaryStage.setTitle("SemSync - Register"); 
        primaryStage.setX(visualBounds.getMinX());
        primaryStage.setY(visualBounds.getMinY());
        primaryStage.setWidth(visualBounds.getWidth());
        primaryStage.setHeight(visualBounds.getHeight());
        primaryStage.show();
    }
    
    
    // method for detecting special character inputs
    private boolean isAlphanumeric(String text) {
        return text.matches("^[A-Za-z0-9]+$");
    }

    // Method to animate the envelope doodle while hovering
    private void startEnvelopeAnimation(ImageView envelopeView, Image envelope1, Image envelope2) {
        if (envelopeTimeline != null && envelopeTimeline.getStatus() == Timeline.Status.RUNNING) {
            return; // already animating
        }
        envelopeTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    if (envelopeView.getImage() == envelope1) {
                        envelopeView.setImage(envelope2);
                    } else {
                        envelopeView.setImage(envelope1);
                    }
                }));
        envelopeTimeline.setCycleCount(Timeline.INDEFINITE);
        envelopeTimeline.play();
    }

    // Method to stop animating the envelope and reset
    private void stopEnvelopeAnimation(ImageView envelopeView, Image defaultImage) {
        if (envelopeTimeline != null) {
            envelopeTimeline.stop();
            envelopeView.setImage(defaultImage); // reset to default
        }
    }
}
