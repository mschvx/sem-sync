package ui;

import application.Fonts;
import application.Main;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.animation.ScaleTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;

public class References {

    // REFERENCES PAGE
    public void showReferences(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1536, 864);
        scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());

    
    // Notebook image
    Image detailsImage = new Image("file:Elements/Details.png", false);
    ImageView iv = new ImageView(detailsImage);
    iv.setPreserveRatio(true);
    iv.setFitHeight(900); 
    iv.setLayoutX(20);
    iv.setLayoutY(20);

    // Banderitas
    Image band1 = new Image("file:Elements/Banderitas1.png");
    Image band2 = new Image("file:Elements/Banderitas2.png");
    ImageView bandView = new ImageView(band1);
    bandView.setPreserveRatio(true);
    bandView.setFitWidth(750);
    bandView.setLayoutX(790);
    bandView.setLayoutY(-20);
    bandView.setRotate(4);
    root.getChildren().add(bandView);
    Timeline bandTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), ev -> {
        if (bandView.getImage() == band1) {
            bandView.setImage(band2);
        } else {
            bandView.setImage(band1);
        }
    }));
    bandTimeline.setCycleCount(Timeline.INDEFINITE);
    bandTimeline.play();

    // Hover effect skeri banderitas :>
    Image consume = new Image("file:Elements/yourenotsupposedtobehere/CONSUME.png");
    bandView.setCursor(Cursor.HAND);
    bandView.setOnMouseEntered(e -> {
        bandTimeline.pause();
        bandView.setImage(consume);
    });
    bandView.setOnMouseExited(e -> {
        bandTimeline.play();
    });

    // 3D EFFECT FOR THREE BUTTONS
    RadialGradient yellowGrad = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE,
        new Stop(0, Color.web("#fff7d1")), new Stop(0.5, Color.web("#f4c842")), new Stop(1, Color.web("#d69a2b")));
    RadialGradient blueGrad = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE,
        new Stop(0, Color.web("#eaf8ff")), new Stop(0.5, Color.web("#57b7e3")), new Stop(1, Color.web("#2a95c5")));
    RadialGradient redGrad = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE,
        new Stop(0, Color.web("#ffdede")), new Stop(0.5, Color.web("#b23b3b")), new Stop(1, Color.web("#7f2626")));

    // 3 BUTTONS (THE SCRAPBOOK BOOKMARK STYLE)
    Circle cYellow = new Circle(150, 180, 72);
    cYellow.setFill(yellowGrad);
    Circle cBlue = new Circle(140, 300, 72);
    cBlue.setFill(blueGrad);
    Circle cRed = new Circle(130, 420, 72);
    cRed.setFill(redGrad);

    DropShadow ds = new DropShadow(18, Color.rgb(0,0,0,0.35));
    InnerShadow is = new InnerShadow(8, Color.rgb(0,0,0,0.18));
    cYellow.setEffect(ds);
    cBlue.setEffect(ds);
    cRed.setEffect(ds);
    cYellow.setOnMouseEntered(e -> cYellow.setEffect(is));
    cBlue.setOnMouseEntered(e -> cBlue.setEffect(is));
    cRed.setOnMouseEntered(e -> cRed.setEffect(is));
    cYellow.setOnMouseExited(e -> cYellow.setEffect(ds));
    cBlue.setOnMouseExited(e -> cBlue.setEffect(ds));
    cRed.setOnMouseExited(e -> cRed.setEffect(ds));

    // hover effects
    cYellow.setCursor(Cursor.HAND);
    cBlue.setCursor(Cursor.HAND);
    cRed.setCursor(Cursor.HAND);
    cYellow.setOnMouseEntered(e -> { cYellow.setScaleX(1.08); cYellow.setScaleY(1.08); });
    cYellow.setOnMouseExited(e -> { cYellow.setScaleX(1); cYellow.setScaleY(1); });
    cBlue.setOnMouseEntered(e -> { cBlue.setScaleX(1.08); cBlue.setScaleY(1.08); });
    cBlue.setOnMouseExited(e -> { cBlue.setScaleX(1); cBlue.setScaleY(1); });
    cRed.setOnMouseEntered(e -> { cRed.setScaleX(1.08); cRed.setScaleY(1.08); });
    cRed.setOnMouseExited(e -> { cRed.setScaleX(1); cRed.setScaleY(1); });

    cYellow.setOnMouseClicked((MouseEvent me) -> {
        ScaleTransition st = new ScaleTransition(Duration.millis(180), cYellow);
        st.setFromX(1); st.setFromY(1); st.setToX(1.25); st.setToY(1.25);
        st.setAutoReverse(true); st.setCycleCount(2);
        st.play();
        new About().showAbout(primaryStage);
    });
    cBlue.setOnMouseClicked((MouseEvent me) -> {
        ScaleTransition st = new ScaleTransition(Duration.millis(180), cBlue);
        st.setFromX(1); st.setFromY(1); st.setToX(1.25); st.setToY(1.25);
        st.setAutoReverse(true); st.setCycleCount(2);
        st.play();
        new References().showReferences(primaryStage);
    });
    cRed.setOnMouseClicked((MouseEvent me) -> {
        ScaleTransition st = new ScaleTransition(Duration.millis(180), cRed);
        st.setFromX(1); st.setFromY(1); st.setToX(1.25); st.setToY(1.25);
        st.setAutoReverse(true); st.setCycleCount(2);
        st.play();
        new Credits().showCredits(primaryStage);
    });

    // Title 
    Label title = new Label("REFERENCES");
    Font titleFont = Fonts.loadSensaWild(64);
    title.setFont(titleFont);
    title.setLayoutX(220);
    title.setLayoutY(100);
    title.setRotate(3);



    Circle hYellow = new Circle(150 - 18, 180 - 22, 42, Color.WHITE);
    hYellow.setOpacity(0.26);
    hYellow.setMouseTransparent(true);
    Circle hBlue = new Circle(140 - 18, 300 - 22, 42, Color.WHITE);
    hBlue.setOpacity(0.26);
    hBlue.setMouseTransparent(true);
    Circle hRed = new Circle(130 - 18, 420 - 22, 42, Color.WHITE);
    hRed.setOpacity(0.26);
    hRed.setMouseTransparent(true);

    root.getChildren().addAll(cYellow, cBlue, cRed, hYellow, hBlue, hRed, iv, title);

    // Go Back Button
    Button goBackButton = new Button("Go Back");
    goBackButton.getStyleClass().add("btn-back");
    goBackButton.setLayoutX(1220);
    goBackButton.setLayoutY(650);
    goBackButton.setPrefWidth(250);
    goBackButton.setPrefHeight(50);
    goBackButton.setFont(Fonts.loadSensaWild(40));
    root.getChildren().add(goBackButton);

    goBackButton.setOnAction(e -> {
        if (Main.isLoggedIn) {
            new Dashboard().showDashboard(primaryStage, Main.loggedInUser);
        } else {
            Main main = new Main();
            main.start(primaryStage);
        }
    });

    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setScene(scene);
        primaryStage.setTitle("SemSync - References");
        primaryStage.setX(visualBounds.getMinX());
        primaryStage.setY(visualBounds.getMinY());
        primaryStage.setWidth(visualBounds.getWidth());
        primaryStage.setHeight(visualBounds.getHeight());
        primaryStage.show();
    }

    // Backwards-compatibility wrapper for older callers
    public void showWalkthrough(Stage primaryStage) {
        showReferences(primaryStage);
    }
}
