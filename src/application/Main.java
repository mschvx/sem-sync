// MAIN: Consists of Login and Information

package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

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

            // Load logo images 
            Image logo1 = new Image("file:Elements/Logo1.png");
            Image logo2 = new Image("file:Elements/Logo2.png");

            // Positioning animated logo
            ImageView logoView = new ImageView(logo1);
            logoView.setPreserveRatio(true);
            logoView.setFitWidth(900);
            logoView.setLayoutX(-20); 
            logoView.setLayoutY(100); 
            root.getChildren().add(logoView);

            // Timeline to swap logo images every 0.5 seconds
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    if (logoView.getImage() == logo1) {
                        logoView.setImage(logo2);
                    } else {
                        logoView.setImage(logo1);
                    }
                })
            );
            timeline.setCycleCount(Timeline.INDEFINITE); // Run forever while program is running
            timeline.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
