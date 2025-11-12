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
import javafx.scene.text.Font;

public class Register {	
	
	public void showRegister(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1536, 864);      
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        
        // Load CSS
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

        // Username texxt
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

        // Select Degree Program label
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
        Font degreeFont = Font.font("Montserrat", 30); // Same as main class for programs
        for (String degree : degrees) {
            Label deg = new Label(degree);
            deg.setFont(degreeFont);
            deg.setLayoutX(830
            		);
            deg.setLayoutY(startY);
            root.getChildren().add(deg);
            startY += 70; // spacing between options
        }
        
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
