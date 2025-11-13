// Register page

package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;


public class Register {	
    private javafx.animation.Timeline envelopeTimeline; // for envelope animation

    public void showRegister(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1536, 864);      
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        scene.getStylesheets().add(Dashboard.class.getResource("application.css").toExternalForm());
        
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
        String[] degrees = {
            "BS Computer Science",
            "MS Computer Science",
            "Master of Information Technology",
            "PhD Computer Science"
        };
        double startY = 360;
        Font degreeFont = Font.font("Montserrat", 30); 

        // Add toggles beside the names of degprog
        ToggleGroup degreeGroup = new ToggleGroup();
        for (String degree : degrees) {
            ToggleButton degBtn = new ToggleButton();
            degBtn.getStyleClass().add("degree-btn");
            degBtn.setToggleGroup(degreeGroup);
            degBtn.setUserData(degree);
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
        
        // Load sprites
        Image envelope1 = new Image("file:Elements/Envelope1.png");
        Image envelope2 = new Image("file:Elements/Envelope2.png");
        
        // Envelope
        ImageView envelopeView = new ImageView(envelope1);
        envelopeView.setPreserveRatio(true);
        envelopeView.setFitWidth(200);
        envelopeView.setLayoutX(1250);
        envelopeView.setLayoutY(520);
        envelopeView.setRotate(20);
    // shadow for envelope
    DropShadow envelopeShadow = new DropShadow();
    envelopeShadow.setRadius(10);
    envelopeShadow.setOffsetX(4);
    envelopeShadow.setOffsetY(4);
    envelopeShadow.setColor(Color.rgb(0, 0, 0, 0.35));
    envelopeView.setEffect(envelopeShadow);
        root.getChildren().add(envelopeView);
        

        // Start envelope animation when hovering over the create button
        createButton.setOnMouseEntered(e -> startEnvelopeAnimation(envelopeView, envelope1, envelope2));

        // Stop envelope animation and reset when mouse exits
        createButton.setOnMouseExited(e -> stopEnvelopeAnimation(envelopeView, envelope1));

        // Load PC sprites 
        Image pc1 = new Image("file:Elements/PC1.png");
        Image pc2 = new Image("file:Elements/PC2.png");

        ImageView pcView = new ImageView(pc1);
        pcView.setPreserveRatio(true);
        pcView.setFitWidth(250);
        pcView.setLayoutX(30);
        pcView.setLayoutY(0);
        pcView.setRotate(-5);
        root.getChildren().add(pcView);

        // Timeline to swap every 0.5 sec.
        Timeline pcTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    if (pcView.getImage() == pc1) {
                        pcView.setImage(pc2);
                    } else {
                        pcView.setImage(pc1);
                    }
                }));
        pcTimeline.setCycleCount(Timeline.INDEFINITE);
        pcTimeline.play();


        // Set up window properties
        primaryStage.setScene(scene);
        primaryStage.setTitle("SemSync - Register"); 
        primaryStage.setX(visualBounds.getMinX());
        primaryStage.setY(visualBounds.getMinY());
        primaryStage.setWidth(visualBounds.getWidth());
        primaryStage.setHeight(visualBounds.getHeight());
        primaryStage.show();
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
