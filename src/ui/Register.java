package ui;

import application.Fonts;
import application.Main;
import application.Sizing;
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
import users.User;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;


public class Register {

    private Timeline envelopeTimeline; // for animations
    private Timeline pcTimeline; 

    private Timeline glitchTimeline; 
    // small chance for something..?
    private static final double GLITCH_CHANCE = 0.0099; // 0.99% chancec

    // Images and view for PC and error-glitch replacement
    private Image pc1Img;
    private Image pc2Img;
    private Image glitchImg;
    private Image huhImg;
    private ImageView pcImageView;

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
                // trim the five required register data
                if (parts.length != 5) {
                    
                    continue;
                }
                String first = parts[0].trim();
                String last = parts[1].trim();
                String username = parts[2].trim();
                String password = parts[3].trim();
                String degree = parts[4].trim();

                list.add(new User(first, last, username, password, degree));
            }

        } catch (IOException e) {
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("Cannot Load.");
            // trigger temporary glitch animation to indicate error
            triggerErrorGlitch();
            errorAlert.showAndWait();
            return list;
        }

        return list;
    }

    public void showRegister(Stage primaryStage) {
    	
        double baseWidth = Sizing.BASE_WIDTH;
        double baseHeight = Sizing.BASE_HEIGHT;
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
        Font registerFont = Font.loadFont(Fonts.SENSA_SERIF, 88);
        registerLabel.setFont(registerFont);
        registerLabel.setLayoutX(120);
        registerLabel.setLayoutY(200);
        root.getChildren().add(registerLabel);

        // First Name label and field 
        Label firstNameLabel = new Label("First Name");
        firstNameLabel.setFont(Font.loadFont(Fonts.COMING_SOON, 44));
        firstNameLabel.setLayoutX(100);
        firstNameLabel.setLayoutY(290);
        root.getChildren().add(firstNameLabel);

        TextField firstNameField = new TextField();
        firstNameField.setLayoutX(100);
        firstNameField.setLayoutY(360); // NASA 360 
        firstNameField.setPrefWidth(600);
        firstNameField.setPrefHeight(56);
        firstNameField.setFont(Fonts.loadMontserratRegular(24));
        firstNameField.setStyle("-fx-background-color: #ebebeb; " +
            "-fx-border-color: black; " +
            "-fx-border-radius: 15; " +
            "-fx-background-radius: 15; " +
            "-fx-text-fill: black;" +
            "-fx-border-width: 4;");
        root.getChildren().add(firstNameField);

        // Last Name label and field (new)
        Label lastNameLabel = new Label("Last Name");
        lastNameLabel.setFont(Font.loadFont(Fonts.COMING_SOON, 44));
        lastNameLabel.setLayoutX(100);
        lastNameLabel.setLayoutY(400); // NASA 400 SO 40 DISTANCE.
        root.getChildren().add(lastNameLabel);

        TextField lastNameField = new TextField();
        lastNameField.setLayoutX(100);
        lastNameField.setLayoutY(470); // NASA 70 DISTANCE
        lastNameField.setPrefWidth(600);
        lastNameField.setPrefHeight(56);
        lastNameField.setFont(Fonts.loadMontserratRegular(24));
        lastNameField.setStyle("-fx-background-color: #ebebeb; " +
            "-fx-border-color: black; " +
            "-fx-border-radius: 15; " +
            "-fx-background-radius: 15; " +
            "-fx-text-fill: black;" +
            "-fx-border-width: 4;");
        root.getChildren().add(lastNameField);

        // Username text
        Label usernameLabel = new Label("Username");
        Font usernameFont = Font.loadFont(Fonts.COMING_SOON, 44);
        usernameLabel.setFont(usernameFont);
        usernameLabel.setLayoutX(100);
        usernameLabel.setLayoutY(510);
        root.getChildren().add(usernameLabel);

        // Password text
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(usernameFont);
        passwordLabel.setLayoutX(100);
        passwordLabel.setLayoutY(620);
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
        Font degreeFont = Fonts.loadMontserratRegular(30);

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
        createButton.setLayoutX(768); // restored to original column
        createButton.setLayoutY(640);
        createButton.setPrefWidth(600);
        createButton.setPrefHeight(90);
        createButton.setFont(Fonts.loadSensaWild(40));
        root.getChildren().add(createButton);

        // Load envelope images
        Image envelope1 = new Image("file:Elements/Envelope1.png");
        Image envelope2 = new Image("file:Elements/Envelope2.png");
        ImageView envelopeView = new ImageView(envelope1);
        envelopeView.setPreserveRatio(true);
        envelopeView.setFitWidth(200);
        envelopeView.setLayoutX(1240);
        envelopeView.setLayoutY(520);
        envelopeView.setRotate(20);
        root.getChildren().add(envelopeView);
        // Envelope hover animation
        createButton.setOnMouseEntered(e -> startEnvelopeAnimation(envelopeView, envelope1, envelope2));
        createButton.setOnMouseExited(e -> stopEnvelopeAnimation(envelopeView, envelope1));
        
        // Load PC images and animate them
        pc1Img = new Image("file:Elements/PC1.png");
        pc2Img = new Image("file:Elements/PC2.png");
        // glitch images located in the hidden folder
        glitchImg = new Image("file:Elements/yourenotsupposedtobehere/glitch.png");
        huhImg = new Image("file:Elements/yourenotsupposedtobehere/huh.png");
        pcImageView = new ImageView(pc1Img);
        pcImageView.setPreserveRatio(true);
        pcImageView.setFitWidth(300);
        pcImageView.setLayoutX(0);
        pcImageView.setLayoutY(0);
        root.getChildren().add(pcImageView);

        // Timeline to swap images for normal PC animation
        pcTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    // random chance to trigger glitch animation instead of normal swap
                    if (Math.random() < GLITCH_CHANCE) {
                        triggerErrorGlitch();
                        return;
                    }
                    if (pcImageView.getImage() == pc1Img) {
                        pcImageView.setImage(pc2Img);
                        pcImageView.setRotate(7);
                    } else {
                        pcImageView.setImage(pc1Img);
                        pcImageView.setRotate(0);
                    }
                }));
        pcTimeline.setCycleCount(Timeline.INDEFINITE);
        pcTimeline.play();
        
        // Username text field
        TextField usernameField = new TextField();
        usernameField.setLayoutX(100);
        usernameField.setLayoutY(580);
        usernameField.setPrefWidth(600);
        usernameField.setPrefHeight(56);
        usernameField.setFont(Fonts.loadMontserratRegular(24));
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
        passwordField.setLayoutY(690);
        passwordField.setPrefWidth(600);
        passwordField.setPrefHeight(56);
        // Use system font for password field so masked characters render reliably
        passwordField.setFont(javafx.scene.text.Font.font("System", 24));
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
            String first = firstNameField.getText().trim();
            String last = lastNameField.getText().trim();
            String uname = usernameField.getText().trim();
            String pass = passwordField.getText().trim();
            ToggleButton selectedDegree = (ToggleButton) degreeGroup.getSelectedToggle();
            boolean usernameFound = false;
            
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);

            // First and Last name required
            if (first.isBlank() || last.isBlank()) {
                errorAlert.setHeaderText("Missing Name Fields!");
                errorAlert.setContentText("Please fill in both first and last name.");
                triggerErrorGlitch();
                errorAlert.showAndWait();
                return;
            }

            // Username and password must be provided
            if (uname.isBlank() || pass.isEmpty()) {
                errorAlert.setHeaderText("Missing Fields!");
                errorAlert.setContentText("Please fill in both username and password.");
                triggerErrorGlitch();
                errorAlert.showAndWait();
                return;
            }
            // Username contains only letters and numbers
            if (!isAlphanumeric(uname)) {
                errorAlert.setHeaderText("Invalid Username");
                errorAlert.setContentText("Username can only contain letters and numbers.\nNo spaces or special characters allowed.");
                triggerErrorGlitch();
                errorAlert.showAndWait();
                return;
            }

            // Password contains only letters and numbers
            if (!isAlphanumeric(pass)) {
                errorAlert.setHeaderText("Invalid Password");
                errorAlert.setContentText("Password can only contain letters and numbers.\nNo spaces or special characters allowed.");
                triggerErrorGlitch();
                errorAlert.showAndWait();
                return;
            }

            // a degree program must be selected
            if (selectedDegree == null) {
                errorAlert.setHeaderText("Missing Degree Program!");
                errorAlert.setContentText("Please select a degree program.");
                triggerErrorGlitch();
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
                triggerErrorGlitch();
                errorAlert.showAndWait();
                return;
            }

            // Append new user to users.csv
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
                    writer.write(first + "," + last + "," + uname + "," + pass + "," + degreeCode);
                    writer.newLine();
                }
            } catch (IOException ioEx) {
                errorAlert.setHeaderText("Error");
                errorAlert.setContentText("Unable to save account. Please try again.");
                triggerErrorGlitch();
                errorAlert.showAndWait();
                return;
            }

            
            String degreeCodeForMemory = "";
            ToggleButton selectedDegreeBtn = (ToggleButton) degreeGroup.getSelectedToggle();
            if (selectedDegreeBtn != null && selectedDegreeBtn.getUserData() != null) {
                degreeCodeForMemory = selectedDegreeBtn.getUserData().toString();
            }
            data.add(new User(first, last, uname, pass, degreeCodeForMemory));

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
        
        // Scaling and centering logic
        Runnable updateLayout = () -> {
            double scaleX = scene.getWidth() / baseWidth;
            double scaleY = scene.getHeight() / baseHeight;
            double scale = Math.max(scaleX, scaleY);

            root.setScaleX(scale);
            root.setScaleY(scale);

            root.setTranslateX(baseWidth * (scale - 1) / 2);
            root.setTranslateY(baseHeight * (scale - 1) / 2);
        };

        // Apply initially and on resize
        updateLayout.run();
        scene.widthProperty().addListener((obs, old, newVal) -> updateLayout.run());
        scene.heightProperty().addListener((obs, old, newVal) -> updateLayout.run());
        
        javafx.application.Platform.runLater(() -> updateLayout.run());
        
        
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

    // Trigger a short glitch animation using images from the hidden folder
    private void triggerErrorGlitch() {
        if (pcImageView == null) {
            return; // nothing to animate yet
        }
        if (glitchTimeline != null && glitchTimeline.getStatus() == Timeline.Status.RUNNING) {
            return; // already showing glitch
        }

        // pause normal PC animation
        if (pcTimeline != null && pcTimeline.getStatus() == Timeline.Status.RUNNING) {
            pcTimeline.pause();
        }

        // immediately show first glitch image
        if (glitchImg != null) {
            pcImageView.setImage(glitchImg);
            pcImageView.setRotate(0);
        }

        // toggle between glitch and huh for a short moment 
        glitchTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            if (pcImageView.getImage() == glitchImg) {
                pcImageView.setImage(huhImg);
            } else {
                pcImageView.setImage(glitchImg);
            }
        }));
        glitchTimeline.setCycleCount(2);
        glitchTimeline.setOnFinished(event -> {
            glitchTimeline = null;
            // restore normal pc image and rotation
            if (pc1Img != null) {
                pcImageView.setImage(pc1Img);
                pcImageView.setRotate(0);
            }
            if (pcTimeline != null) {
                pcTimeline.play();
            }
        });
        glitchTimeline.play();
    }

    // Method to stop animating the envelope and reset
    private void stopEnvelopeAnimation(ImageView envelopeView, Image defaultImage) {
        if (envelopeTimeline != null) {
            envelopeTimeline.stop();
            envelopeView.setImage(defaultImage); // reset to default
        }
    }
}
