// MAIN: Consists of Login and Information

package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 1536, 864);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("SemSync"); // Method to set window title
            primaryStage.setMaximized(true); // Method to dynamically fit the window to user's screen size
            primaryStage.show();

            // Load the logo pics
            Image logo1 = new Image("file:Elements/Logo1.png");
            Image logo2 = new Image("file:Elements/Logo2.png");

            // Place and size the logo to the screen
            ImageView logoView = new ImageView(logo1);
            logoView.setPreserveRatio(true);
            logoView.setFitWidth(900); // Size
            logoView.setTranslateX(-350); // Shift left
            
            // Combine logo together
            StackPane logoPane = new StackPane();
            logoPane.getChildren().addAll(logoView);
            root.setCenter(logoPane);

            // Animation that swaps logo images every 0.5 seconds
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    // Swap the logo images
                    if (logoView.getImage() == logo1) {
                        logoView.setImage(logo2);
                    } else {
                        logoView.setImage(logo1);
                    }
                })
            );

            timeline.setCycleCount(Timeline.INDEFINITE); // Method to loop forever as long as the program runs
            timeline.play(); // Start

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
