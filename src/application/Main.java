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
            Image pencil1 = new Image("file:Elements/Pencil1.png");
            Image pencil2 = new Image("file:Elements/Pencil2.png");
            
            Image letter1 = new Image("file:Elements/Letter1.png");
            Image letter2 = new Image("file:Elements/Letter2.png");
            
            Image sync1 = new Image("file:Elements/Sync1.png");
            Image sync2 = new Image("file:Elements/Sync2.png");

            // Place and size the logo to the screen
            ImageView pencilView = new ImageView(pencil1);
            pencilView.setPreserveRatio(true);
            pencilView.setFitWidth(600); // Size
            pencilView.setTranslateX(-450); // Shift left
            
            // Place and size the logo's other letters to the screen
            ImageView letterView = new ImageView(letter1);
            letterView.setPreserveRatio(true);
            letterView.setFitWidth(450); // Size
            letterView.setTranslateX(-240); // Shift left  
            letterView.setTranslateY(-100); // Shift up

            ImageView syncView = new ImageView(sync2);
            syncView.setPreserveRatio(true);
            syncView.setFitWidth(550); // Size
            syncView.setTranslateX(-420); // Shift left  
            syncView.setTranslateY(0); // Shift up
            
            // Combine pencil and letters together
            StackPane logoPane = new StackPane();
            logoPane.getChildren().addAll(pencilView, letterView, syncView);
            root.setCenter(logoPane);

            // Animation that swaps logo images every 0.5 seconds
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    // Swap all the logo images
                    if (pencilView.getImage() == pencil1 && letterView.getImage() == letter1 && syncView.getImage() == sync2) {
                        pencilView.setImage(pencil2);
                        letterView.setImage(letter2);
                        syncView.setImage(sync1);
                    } else {
                        pencilView.setImage(pencil1);
                        letterView.setImage(letter1);
                        syncView.setImage(sync2);
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
