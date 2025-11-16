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
import ui.About;
import ui.Dashboard;
import ui.Register;
import users.User;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class Main extends Application {

    Path path = Path.of(System.getProperty("user.dir"));

    // Track consecutive wrong login attempts
    private int wrongAttempts = 0;
    // Load method to get the data from CSV file
    private ObservableList<User> loadData(Path path) {
        ObservableList<User> list = FXCollections.<User>observableArrayList();
        Path folder = Paths.get("Database");
        Path file = folder.resolve("users.csv");

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

    // For animation:
    private Timeline planeTimeline; // animation reference

    // On wrong attempts effects
    private void registerWrongAttempt(Pane root, Label tagline) {
        wrongAttempts++;

        // Find existing overlay ImageView by id (if any)
        javafx.scene.image.ImageView overlay = null;
        for (javafx.scene.Node n : root.getChildren()) {
            if (n instanceof javafx.scene.image.ImageView && "loginErrorOverlay".equals(n.getId())) {
                overlay = (javafx.scene.image.ImageView) n;
                break;
            }
        }

        try {
            if (wrongAttempts == 1) {
                // First wrong attempt: change root background to BG1
                root.setStyle("-fx-background-image: url(\"file:Elements/yourenotsupposedtobehere/BG1.png\"); -fx-background-size: cover; -fx-background-repeat: no-repeat; -fx-background-position: center;");
                if (overlay != null) root.getChildren().remove(overlay);

            } else if (wrongAttempts == 2) {
                // Second wrong attempt: change root background to BG2
                root.setStyle("-fx-background-image: url(\"file:Elements/yourenotsupposedtobehere/BG2.png\"); -fx-background-size: cover; -fx-background-repeat: no-repeat; -fx-background-position: center;");
                if (overlay != null) root.getChildren().remove(overlay);

            } else if (wrongAttempts == 3) {
                // Third wrong attempt: Add a splatter overlay on top of the root
                if (overlay == null) {
                    overlay = new javafx.scene.image.ImageView();
                    overlay.setId("loginErrorOverlay");
                    overlay.setMouseTransparent(true);
                    overlay.setPreserveRatio(false);
                    overlay.fitWidthProperty().bind(root.widthProperty());
                    overlay.fitHeightProperty().bind(root.heightProperty());
                    root.getChildren().add(overlay);
                }

                overlay.setImage(new javafx.scene.image.Image("file:Elements/yourenotsupposedtobehere/Splatter.png"));
                overlay.toFront();
                // replace the tagline text 
                if (tagline != null) {
                    tagline.setText("YOU’RE MAKING THIS DIFFICULT FOR YOURSELF");
                    tagline.setLayoutX(70);
                    tagline.setFont(Font.loadFont(Fonts.COMING_SOON, 30));
                    tagline.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                    tagline.setVisible(true);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            double baseWidth = Sizing.BASE_WIDTH;
            double baseHeight = Sizing.BASE_HEIGHT;

            Pane root = new Pane();
            root.getStyleClass().add("root");

            Pane content = new Pane();
            content.setTranslateX(0);
            content.setTranslateY(0);
            root.getChildren().add(content);

            Scene scene = new Scene(root,1536,864);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("SemSync - Log In");
            primaryStage.setMaximized(true);
            primaryStage.show();

            // Scaling and centering logic
            Runnable updateLayout = () -> {
                double scaleX = scene.getWidth() / baseWidth;
                double scaleY = scene.getHeight() / baseHeight;
                double scale = Math.max(scaleX, scaleY);

                content.setScaleX(scale);
                content.setScaleY(scale);

                content.setTranslateX(baseWidth * (scale - 1) / 2);
                content.setTranslateY(baseHeight * (scale - 1) / 2);
            };

            // Apply initially and on resize
            updateLayout.run();
            scene.widthProperty().addListener((obs, old, newVal) -> updateLayout.run());
            scene.heightProperty().addListener((obs, old, newVal) -> updateLayout.run());
            
            javafx.application.Platform.runLater(() -> updateLayout.run());

            // Load paper image
            ImageView background = new ImageView(new Image("file:Elements/LoginPaper.png"));
            background.setPreserveRatio(false);
            background.setFitWidth(baseWidth + 8);
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
            spriteView.setFitWidth(180);
            spriteView.setLayoutX(1360);
            spriteView.setLayoutY(-0);
            spriteView.setRotate(70);
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
                                spriteView.setRotate(75);
                            } else {
                                logoView.setImage(logo1);
                                starView1.setImage(star1);
                                starView2.setImage(star2);
                                spriteView.setImage(sprite1);
                                // restore default rotation
                                spriteView.setRotate(70);
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

            final String defaultTagline = "Scheduling has never been this easy!";
            Label tagline = new Label(defaultTagline);
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
            loginButton.setOnAction(e -> {
                try {
                    String uname = usernameField.getText().trim();
                    String pass = passwordField.getText().trim();
                    User currentUser = null;

                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

                    // Blank inputs
                    if (uname.isBlank() || pass.isEmpty()) {
                        errorAlert.setHeaderText("Missing Fields!");
                        errorAlert.setContentText("Please enter both username and password.");
                        registerWrongAttempt(root, tagline);
                        errorAlert.showAndWait();
                        return;
                    }

                    if (data == null) {
                        errorAlert.setHeaderText("Error!");
                        errorAlert.setContentText("Could not load user data.");
                        registerWrongAttempt(root, tagline);
                        errorAlert.showAndWait();
                        return;
                    }

                    for (User user : data) {
                        if (user.getUsername().equals(uname)) {
                            if (user.getPassword().equals(pass)) {
                                currentUser = user;
                            }
                            break;
                        }
                    }

                    if (currentUser != null) {
                        // successful login: reset attempts and background; restore tagline; remove overlay if present
                        wrongAttempts = 0;
                        root.setStyle("");
                        tagline.setText(defaultTagline);
                        tagline.setStyle("-fx-text-fill: black;");
                        tagline.setVisible(true);
                        // remove overlay if present
                        javafx.scene.Node toRemove = null;
                        for (javafx.scene.Node n : root.getChildren()) {
                            if (n instanceof javafx.scene.image.ImageView && "loginErrorOverlay".equals(n.getId())) {
                                toRemove = n;
                                break;
                            }
                        }
                        if (toRemove != null) root.getChildren().remove(toRemove);
                        Dashboard db = new Dashboard();
                        db.showDashboard(primaryStage, currentUser);
                    } else {
                        errorAlert.setHeaderText("Login Failed!");
                        errorAlert.setContentText("Wrong account details.");
                        registerWrongAttempt(root, tagline);
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
    }
     // Correct closing brace for start method

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
