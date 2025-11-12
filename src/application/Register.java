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
import javafx.scene.text.Font;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class Register {	

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
        Font degreeFont = Font.font("Montserrat", 30); // Equal spacing using for loop for aesthetic
        for (String degree : degrees) {
            Label deg = new Label(degree);
            deg.setFont(degreeFont);
            deg.setLayoutX(830);
            deg.setLayoutY(startY);
            root.getChildren().add(deg);
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


        // Set up window properties
        primaryStage.setScene(scene);
        primaryStage.setTitle("SemSync - Register"); 
        primaryStage.setX(visualBounds.getMinX());
        primaryStage.setY(visualBounds.getMinY());
        primaryStage.setWidth(visualBounds.getWidth());
        primaryStage.setHeight(visualBounds.getHeight());
        primaryStage.show();
    }
}
