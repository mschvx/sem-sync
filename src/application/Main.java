// MAIN: Consists of Login and Information

package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class Main extends Application {

    Path path = Path.of(System.getProperty("user.dir"));

    // Load method to get the data from CSV file
    // Doesn't properly work yet since wala pang database. (P.S naglagay muna ako ng placeholder for the mean time)
    private ObservableList<User> loadData(Path path) {
        ObservableList<User> list = FXCollections.<User>observableArrayList();
        Path folder = Paths.get("placeholder");
        Path file = folder.resolve("placeholder.csv");

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);

        if (!Files.exists(file)) {
            errorAlert.setHeaderText("Error!");
            errorAlert.setContentText("File not Found!");
            errorAlert.showAndWait();
            return list;
        }

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

                list.add(new User(username, password));
            }

        } catch (IOException e) {
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("Cannot Load.");
            errorAlert.showAndWait();
            return list;
        }

        return list;
    }

    // For animation:
    private Timeline planeTimeline; // animation reference

    @Override
    public void start(Stage primaryStage) {
        try {
            Pane root = new Pane(); // keeps CSS background fixed
            root.getStyleClass().add("root"); // ensure background from CSS applies

            Pane content = new Pane(); // holds everything that scales
            root.getChildren().add(content);

            Scene scene = new Scene(root, 1536, 864);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("SemSync - Log In"); // Window title
            primaryStage.setMaximized(true); // Maximize window to fit screen
            primaryStage.show();

            // Reference resolution for scaling 
                double baseWidth = Sizing.BASE_WIDTH;
                double baseHeight = Sizing.BASE_HEIGHT;

            // Scale based on screen
            content.scaleXProperty().bind(scene.widthProperty().divide(baseWidth));
            content.scaleYProperty().bind(scene.heightProperty().divide(baseHeight));

            // Load paper image
            ImageView background = new ImageView(new Image("file:Elements/LoginPaper.png"));
            background.setFitHeight(1000);
            background.setLayoutX(0);
            background.setLayoutY(-100);
            content.getChildren().add(background);

            // Load sprites
            Image star1 = new Image("file:Elements/Star1.png");
            Image star2 = new Image("file:Elements/Star2.png");

            Image sprite1 = new Image("file:Elements/Sprite1.png");
            Image sprite2 = new Image("file:Elements/Sprite2.png");

            Image plane1 = new Image("file:Elements/Plane1.png");
            Image plane2 = new Image("file:Elements/Plane2.png");

            // Load logo images
            Image logo1 = new Image("file:Elements/Logo1.png");
            Image logo2 = new Image("file:Elements/Logo2.png");

            // Positioning animated logo
            ImageView logoView = new ImageView(logo1);
            logoView.setPreserveRatio(true);
            logoView.setFitWidth(940);
            logoView.setLayoutX(-40);
            logoView.setLayoutY(40);
            content.getChildren().add(logoView);

            // Positioning some sprites
            ImageView starView1 = new ImageView(star1);
            starView1.setPreserveRatio(true);
            starView1.setFitWidth(180);
            starView1.setLayoutX(830);
            starView1.setLayoutY(10);
            starView1.setRotate(20);
            content.getChildren().add(starView1);

            ImageView starView2 = new ImageView(star2);
            starView2.setPreserveRatio(true);
            starView2.setFitWidth(180);
            starView2.setLayoutX(1400);
            starView2.setLayoutY(330);
            starView2.setRotate(170);
            content.getChildren().add(starView2);

            ImageView spriteView = new ImageView(sprite1);
            spriteView.setPreserveRatio(true);
            spriteView.setFitWidth(1300);
            spriteView.setLayoutX(230);
            spriteView.setLayoutY(0);
            content.getChildren().add(spriteView);

            ImageView planeView = new ImageView(plane2);
            planeView.setPreserveRatio(true);
            planeView.setFitWidth(300);
            planeView.setLayoutX(650);
            planeView.setLayoutY(550);
            content.getChildren().add(planeView);

            // Timeline to swap images every 0.5 seconds
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.5), event -> {
                        if (logoView.getImage() == logo1 && starView1.getImage() == star1 && starView2.getImage() == star2
                                && spriteView.getImage() == sprite1) {
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
                    }));
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
            tagline.setLayoutX(120);
            tagline.setLayoutY(550);

            content.getChildren().add(tagline);
            content.getChildren().add(username);
            content.getChildren().add(password);
            content.getChildren().add(welcome);

            // Username text field
            TextField usernameField = new TextField();
            usernameField.setLayoutX(850);
            usernameField.setLayoutY(280);
            usernameField.setPrefWidth(600);
            usernameField.setPrefHeight(60);
            usernameField.setFont(javafx.scene.text.Font.font("Montserrat", 30));
            usernameField.setStyle("-fx-background-color: #ebebeb; " +
                    "-fx-border-color: black; " +
                    "-fx-border-radius: 15; " +
                    "-fx-background-radius: 15; " +
                    "-fx-text-fill: black;" +
                    "-fx-border-width: 4; ");
            content.getChildren().add(usernameField);

            // Password text field
            PasswordField passwordField = new PasswordField();
            passwordField.setLayoutX(850);
            passwordField.setLayoutY(480);
            passwordField.setPrefWidth(600);
            passwordField.setPrefHeight(60);
            passwordField.setFont(javafx.scene.text.Font.font("Montserrat", 30));
            passwordField.setStyle("-fx-background-color: #ebebeb; " +
                    "-fx-border-color: black; " +
                    "-fx-border-radius: 15; " +
                    "-fx-background-radius: 15; " +
                    "-fx-text-fill: black;" +
                    "-fx-border-width: 4; ");
            content.getChildren().add(passwordField);

            // Button for Login
            Button loginButton = new Button("Login");
            loginButton.setLayoutX(900);
            loginButton.setLayoutY(580);
            loginButton.setPrefWidth(500);
            loginButton.setPrefHeight(90);
            loginButton.setFont(Fonts.loadSensaWild(40));
            loginButton.getStyleClass().add("btn-login"); // Hover effect
            content.getChildren().add(loginButton);

            // Button for Create Account
            Button registerButton = new Button("Create Account");
            registerButton.setLayoutX(900);
            registerButton.setLayoutY(680);
            registerButton.setPrefWidth(500);
            registerButton.setPrefHeight(60);
            registerButton.setFont(Fonts.loadSensaWild(40));
            registerButton.getStyleClass().add("btn-register"); // Hover effect
            content.getChildren().add(registerButton);

            // Button for About Sem Sync
            Button aboutButton = new Button("About Sem Sync");
            aboutButton.setLayoutX(220);
            aboutButton.setLayoutY(620);
            aboutButton.setPrefWidth(300);
            aboutButton.setPrefHeight(60);
            aboutButton.setFont(javafx.scene.text.Font.font("Montserrat", 20));
            aboutButton.getStyleClass().add("btn-about"); // Hover effect
            content.getChildren().add(aboutButton);

            // Go to about page if clicked
            aboutButton.setOnAction(e -> {
                About about = new About();
                about.showAbout(primaryStage);
            });

            // Event handlers for buttons
            // When hovering over login or register, start plane animation
            loginButton.setOnMouseEntered(e -> startPlaneAnimation(planeView, plane1, plane2));
            registerButton.setOnMouseEntered(e -> startPlaneAnimation(planeView, plane1, plane2));

            // When mouse exits, stop the plane animation and reset
            loginButton.setOnMouseExited(e -> stopPlaneAnimation(planeView, plane2));
            registerButton.setOnMouseExited(e -> stopPlaneAnimation(planeView, plane2));

            // Loading data from CSV File
            ObservableList<User> data = loadData(path);

            // Button Click for Login
            // Doesn't properly work yet since wala pang database
            loginButton.setOnAction(e -> {
                try {
                    String uname = usernameField.getText();
                    String pass = passwordField.getText();
                    User currentUser = null;
                    boolean usernameFound = false;

                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    
                    for(char c : uname.toCharArray()) {
                    	if(!Character.isLetterOrDigit(c)) {
                            errorAlert.setHeaderText("Error!");
                            errorAlert.setContentText("Special characters are not allowed!");
                            errorAlert.showAndWait();
                            return;                   		
                    	}
                    }
                    
                    for(char c : pass.toCharArray()) {
                    	if(!Character.isLetterOrDigit(c)) {
                            errorAlert.setHeaderText("Error!");
                            errorAlert.setContentText("Special characters are not allowed!");
                            errorAlert.showAndWait();
                            return;                   		
                    	}
                    }

                    if (data == null) {
                        errorAlert.setHeaderText("Error!");
                        errorAlert.setContentText("Could not load data!");
                        errorAlert.showAndWait();
                        return;
                    }

                    for (User user : data) {
                        if (user.getUsername().equals(uname)) {
                            usernameFound = true;

                            if (user.getPassword().equals(pass)) {
                                currentUser = user;
                            }
                            break;
                        }
                    }

                    if (currentUser != null) {
                        Dashboard db = new Dashboard();
                        db.showDashboard(primaryStage);
                    } else if (usernameFound) {
                        errorAlert.setHeaderText("Login Failed!");
                        errorAlert.setContentText("Wrong Password!");
                        errorAlert.showAndWait();
                    } else {
                        errorAlert.setHeaderText("Login Failed!");
                        errorAlert.setContentText("Account does not exist!");
                        errorAlert.showAndWait();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            
            // Button click for register
            registerButton.setOnAction(e -> {
            	Register register = new Register();
            	register.showRegister(primaryStage);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    } // Correct closing brace for start method

    // Method to animate the plane doodle
    private void startPlaneAnimation(ImageView planeView, Image plane1, Image plane2) {
        if (planeTimeline != null && planeTimeline.getStatus() == Timeline.Status.RUNNING) {
            return; // if already animating, don't do anything
        }
        // else swap every 0.5 seconds as long as the mouse is on the button
        planeTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    if (planeView.getImage() == plane1) {
                        planeView.setImage(plane2);
                    } else {
                        planeView.setImage(plane1);
                    }
                }));
        planeTimeline.setCycleCount(Timeline.INDEFINITE);
        planeTimeline.play();
    }

    // Method to stop animating the plane doodle
    private void stopPlaneAnimation(ImageView planeView, Image plane2) {
        if (planeTimeline != null) {
            planeTimeline.stop();
            planeView.setImage(plane2); // reset to default
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
