package ui;

import application.Fonts;
import application.Main;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import users.DegreeLookup;
import users.DegreeProgram;
import users.User;
import javafx.scene.text.Font;

public class Dashboard {

    // Show dashboard for a specific user (so we can display degree greeting)
    public void showDashboard(Stage primaryStage, User user) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1536, 864);
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("SemSync - Dashboard"); // Window title
        primaryStage.setX(visualBounds.getMinX());
        primaryStage.setY(visualBounds.getMinY());
        primaryStage.setWidth(visualBounds.getWidth());
        primaryStage.setHeight(visualBounds.getHeight());

        // Determine greeting from degree
        String degreeCode = (user != null) ? user.getDegree() : null;
        DegreeProgram program = DegreeLookup.fromCode(degreeCode);
        String shortName = (program != null) ? program.getCourseCode() : "";

        String greeting = "Hello" + (shortName.isEmpty() ? "" : " " + shortName) + ",";

        Label greetLabel = new Label(greeting);
        Font greetFont = Font.loadFont(Fonts.SENSA_SERIF, 80);
        greetLabel.setFont(greetFont);
        greetLabel.setLayoutX(60);
        greetLabel.setLayoutY(60);
        root.getChildren().add(greetLabel);

        primaryStage.show();
    }

}
