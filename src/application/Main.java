// MAIN: Consists of Login and Information

package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane; 
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Pane root = new Pane();
            Scene scene = new Scene(root, 1536, 864);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("SemSync"); // Window title
            primaryStage.setMaximized(true); // Maximize window to fit screen
            primaryStage.show();
            
            // Load paper image
            ImageView background = new ImageView(new Image("file:Elements/LoginPaper.png"));
            background.setFitWidth(1536);
            background.setFitHeight(864);
            root.getChildren().add(background);
            
            // Load sprites
            Image star1 = new Image("file:Elements/Star1.png");
            Image star2 = new Image("file:Elements/Star2.png");
            
            Image sprite1 = new Image("file:Elements/Sprite1.png");
            Image sprite2 = new Image("file:Elements/Sprite2.png");

            // Load logo images 
            Image logo1 = new Image("file:Elements/Logo1.png");
            Image logo2 = new Image("file:Elements/Logo2.png");

            // Positioning animated logo
            ImageView logoView = new ImageView(logo1);
            logoView.setPreserveRatio(true);
            logoView.setFitWidth(940);
            logoView.setLayoutX(-40); 
            logoView.setLayoutY(40); 
            root.getChildren().add(logoView);
            
            // Positioning some sprites
            ImageView starView1 = new ImageView(star1);
            starView1.setPreserveRatio(true);
            starView1.setFitWidth(180);
            starView1.setLayoutX(830); 
            starView1.setLayoutY(10); 
            starView1.setRotate(20); 
            root.getChildren().add(starView1);
            
            ImageView starView2 = new ImageView(star2);
            starView2.setPreserveRatio(true);
            starView2.setFitWidth(180);
            starView2.setLayoutX(1400); 
            starView2.setLayoutY(330); 
            starView2.setRotate(170); 
            root.getChildren().add(starView2);
            
            ImageView spriteView = new ImageView(sprite1);
            spriteView.setPreserveRatio(true);
            spriteView.setFitWidth(1300);
            spriteView.setLayoutX(230); 
            spriteView.setLayoutY(0); 
            root.getChildren().add(spriteView);
            

            // Timeline to swap images every 0.5 seconds
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    if (logoView.getImage() == logo1 && starView1.getImage() == star1 && starView2.getImage() == star2 && spriteView.getImage() == sprite1) {
                        logoView.setImage(logo2);
                        starView1.setImage(star2);
                        starView2.setImage(star1);
                        spriteView.setImage(sprite2);
                    } else {
                        logoView.setImage(logo1);
                        starView1.setImage(star1);
                        starView2.setImage(star2);
                        spriteView.setImage(sprite1);
                    }
                })
            );
            timeline.setCycleCount(Timeline.INDEFINITE); // Run forever while program is running
            timeline.play();

            // Add text
            Label welcome = new Label("WELCOME!");
            Font welcomeFont = Font.loadFont(Fonts.SENSA_SERIF, 150);
            welcome.setFont(welcomeFont);
            welcome.setStyle("-fx-text-fill: black;");
            welcome.setLayoutX(1000);
            welcome.setLayoutY(25);
            
            Label username = new Label("Username: ");
            Font usernameFont = Font.loadFont(Fonts.COMING_SOON, 60);
            username.setFont(usernameFont);
            username.setStyle("-fx-text-fill: black;");
            username.setLayoutX(850);
            username.setLayoutY(170);
            
            Label password = new Label("Password: ");
            Font passwordFont = Font.loadFont(Fonts.COMING_SOON, 60);
            password.setFont(passwordFont);
            password.setStyle("-fx-text-fill: black;");
            password.setLayoutX(850);
            password.setLayoutY(370);
            
            Label tagline = new Label("Scheduling has never been this easy!");
            Font taglineFont = Font.loadFont(Fonts.COMING_SOON, 30);
            tagline.setFont(taglineFont);
            tagline.setStyle("-fx-text-fill: black;");
            tagline.setLayoutX(160);
            tagline.setLayoutY(550);

            root.getChildren().add(tagline);
            root.getChildren().add(username);
            root.getChildren().add(password);
            root.getChildren().add(welcome);
            
         // Username text field 
            TextField usernameField = new TextField();
            usernameField.setLayoutX(850);
            usernameField.setLayoutY(280);
            usernameField.setPrefWidth(600);
            usernameField.setPrefHeight(60);
            usernameField.setFont(javafx.scene.text.Font.font("Montserrat", 30));
            usernameField.setStyle(
                "-fx-background-color: #ebebeb; " + 
                "-fx-border-color: black; " +            
                "-fx-border-radius: 15; " +             
                "-fx-background-radius: 15; " +          
                "-fx-text-fill: black;" +
                "-fx-border-width: 4; "                  
            );
            root.getChildren().add(usernameField);

            // Password text field 
            PasswordField passwordField = new PasswordField();
            passwordField.setLayoutX(850);
            passwordField.setLayoutY(480);
            passwordField.setPrefWidth(600);
            passwordField.setPrefHeight(60);
            passwordField.setFont(javafx.scene.text.Font.font("Montserrat", 30));
            passwordField.setStyle(
                "-fx-background-color: #ebebeb; " +
                "-fx-border-color: black; " +
                "-fx-border-radius: 15; " +
                "-fx-background-radius: 15; " +
                "-fx-text-fill: black;" +
                "-fx-border-width: 4; " 
            );
            root.getChildren().add(passwordField);

            // Button for Login
            Button loginButton = new Button("Login");
            loginButton.setLayoutX(900);
            loginButton.setLayoutY(580);
            loginButton.setPrefWidth(500);
            loginButton.setPrefHeight(90);
            loginButton.setFont(Font.font("Montserrat", 30));
            loginButton.setStyle(
                "-fx-background-color: #439fd0; " +  
                "-fx-border-color: black; " +
                "-fx-border-width: 4; " +
                "-fx-border-radius: 50; " +
                "-fx-background-radius: 50; " +
                "-fx-text-fill: black;"
            );
            root.getChildren().add(loginButton);
            
            // Button for Create Account
            Button registerButton = new Button("Create Account");
            registerButton.setLayoutX(900);
            registerButton.setLayoutY(680);
            registerButton.setPrefWidth(500);
            registerButton.setPrefHeight(60);
            registerButton.setFont(Font.font("Montserrat", 30));
            registerButton.setStyle(
                "-fx-background-color: #9ad1ee; " +  
                "-fx-border-color: black; " +
                "-fx-border-width: 4; " +
                "-fx-border-radius: 50; " +
                "-fx-background-radius: 50; " +
                "-fx-text-fill: black;"
            );
            root.getChildren().add(registerButton);
            
            // Button for About Sem Sync
            
    
            

           

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
