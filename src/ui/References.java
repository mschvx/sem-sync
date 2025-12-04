package ui;

import application.Fonts;
import application.Main;
import application.Sizing;
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
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;

public class References {

    // REFERENCES PAGE
    public void showReferences(Stage primaryStage) {
        double baseWidth = Sizing.BASE_WIDTH;
        double baseHeight = Sizing.BASE_HEIGHT;
        
        Pane root = new Pane();
        Scene scene = new Scene(root, 1536, 864);
        scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
        // tutorial images 1, 2, 3, and text
        final Timeline[] tutTimeline = new Timeline[1];
        final ImageView[] tutViews = new ImageView[3];
        final Image[] origTutImages = new Image[3];
        try {
            Image tut1 = new Image("file:Elements/1.png");
            Image tut2 = new Image("file:Elements/2.png");
            Image tut3 = new Image("file:Elements/3.png");
            Image arrowImg = new Image("file:Elements/arrow.png");
            ImageView tut1View = new ImageView(tut1);
            tut1View.setFitWidth(200);
            tut1View.setPreserveRatio(true);
            tut1View.setLayoutX(870);
            tut1View.setLayoutY(200);

            ImageView arrowViewImg1 = new ImageView(arrowImg);
            arrowViewImg1.setFitWidth(180);
            arrowViewImg1.setPreserveRatio(true);
            arrowViewImg1.setLayoutX(1150);
            arrowViewImg1.setLayoutY(300);
            
            ImageView arrowViewImg2 = new ImageView(arrowImg);
            arrowViewImg2.setFitWidth(180);
            arrowViewImg2.setPreserveRatio(true);
            arrowViewImg2.setLayoutX(1090);
            arrowViewImg2.setLayoutY(480);
            arrowViewImg2.setScaleX(-1);

            ImageView tut2View = new ImageView(tut2);
            tut2View.setFitWidth(250);
            tut2View.setPreserveRatio(true);
            tut2View.setLayoutX(1300);
            tut2View.setLayoutY(360);

            ImageView tut3View = new ImageView(tut3);
            tut3View.setFitWidth(200);
            tut3View.setPreserveRatio(true);
            tut3View.setLayoutX(900);
            tut3View.setLayoutY(570);

            Label tutText1 = new Label("Browse courses");
            tutText1.setFont(Fonts.loadComingSoon(50));
            tutText1.setLayoutX(1060);
            tutText1.setLayoutY(240);

            Label tutText2 = new Label("Click to add");
            tutText2.setFont(Fonts.loadComingSoon(50));
            tutText2.setLayoutX(1080);
            tutText2.setLayoutY(420);

            Label tutText3 = new Label("Instant results!");
            tutText3.setFont(Fonts.loadComingSoon(50));
            tutText3.setLayoutX(1100);
            tutText3.setLayoutY(600);

            //arrays for animation
            tutViews[0] = tut1View;
            tutViews[1] = tut2View;
            tutViews[2] = tut3View;
            origTutImages[0] = tut1;
            origTutImages[1] = tut2;
            origTutImages[2] = tut3;

            root.getChildren().addAll(tut1View, arrowViewImg1, arrowViewImg2, tut2View, tut3View, tutText1, tutText2, tutText3);

            // animation for 1 2 3
            tutTimeline[0] = new Timeline(new KeyFrame(Duration.seconds(0.5), ev -> {
                if (tut1View.getRotate() == 3) {
                    tut1View.setRotate(0);
                } else {
                    tut1View.setRotate(3);
                }
                if (tut3View.getRotate() == 3) {
                    tut3View.setRotate(0);
                } else {
                    tut3View.setRotate(3);
                }
                if (tut2View.getRotate() == -3) {
                    tut2View.setRotate(0);
                } else {
                    tut2View.setRotate(-3);
                }
            }));
            tutTimeline[0].setCycleCount(Timeline.INDEFINITE);
            tutTimeline[0].play();
        } catch (Exception ex) {
        }

    
    // Notebook image
    Image detailsImage = new Image("file:Elements/Details.png", false);
    ImageView iv = new ImageView(detailsImage);
    iv.setPreserveRatio(true);
    iv.setFitHeight(900); 
    iv.setLayoutX(-20);
    iv.setLayoutY(40);

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
    // NIGHT overlay image (full-screen)
    Image nightImg = new Image("file:Elements/yourenotsupposedtobehere/NIGHT.png");
    ImageView nightView = new ImageView(nightImg);
    nightView.setPreserveRatio(false);
    nightView.fitWidthProperty().bind(scene.widthProperty());
    nightView.fitHeightProperty().bind(scene.heightProperty());
    nightView.setLayoutX(0);
    nightView.setLayoutY(0);
    nightView.setOpacity(0);
    nightView.setMouseTransparent(true);

    bandView.setOnMouseEntered(e -> {
        bandTimeline.pause();
        bandView.setImage(consume);
        // replace tutorial images with once/twice/thrice variants (creepy)
        try {
            Image once = new Image("file:Elements/yourenotsupposedtobehere/once.png");
            Image twice = new Image("file:Elements/yourenotsupposedtobehere/twice.png");
            Image thrice = new Image("file:Elements/yourenotsupposedtobehere/thrice.png");
            if (tutViews[0] != null) tutViews[0].setImage(once);
            if (tutViews[1] != null) tutViews[1].setImage(twice);
            if (tutViews[2] != null) tutViews[2].setImage(thrice);
        } catch (Exception ex) {
            
        }
        try {
            FadeTransition fin = new FadeTransition(Duration.millis(220), nightView);
            fin.setFromValue(nightView.getOpacity());
            fin.setToValue(1.0);
            fin.play();
        } catch (Exception ignored) {}
    });
    bandView.setOnMouseExited(e -> {
        bandTimeline.play();
        // restore original tutorial images
        try {
            if (tutViews[0] != null && origTutImages[0] != null) tutViews[0].setImage(origTutImages[0]);
            if (tutViews[1] != null && origTutImages[1] != null) tutViews[1].setImage(origTutImages[1]);
            if (tutViews[2] != null && origTutImages[2] != null) tutViews[2].setImage(origTutImages[2]);
        } catch (Exception ex) {
            
        }
        try {
            FadeTransition fout = new FadeTransition(Duration.millis(220), nightView);
            fout.setFromValue(nightView.getOpacity());
            fout.setToValue(0.0);
            fout.play();
        } catch (Exception ignored) {}
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
    title.setRotate(3);
    title.setLayoutX(220);
    title.setLayoutY(100);

    // QR code
    Image qrImage = new Image("file:Elements/REFERENCES_QR.png", false);
    ImageView qrView = new ImageView(qrImage);
    qrView.setPreserveRatio(true);
    qrView.setFitWidth(500); 
    qrView.setLayoutX(220);
    qrView.setLayoutY(200);
    DropShadow qrShadow = new DropShadow(18, Color.rgb(0,0,0,0.35));
    qrView.setEffect(qrShadow);

    Label scanLabel = new Label("Scan me!");
    scanLabel.setFont(Fonts.loadComingSoon(35));
    scanLabel.setLayoutX(400);
    scanLabel.setLayoutY(700);

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
    // add the QR and caption above other content so it appears centered
    root.getChildren().addAll(qrView, scanLabel);

    // Go Back Button
    Button goBackButton = new Button("Go Back");
    goBackButton.getStyleClass().add("btn-back");
    goBackButton.setLayoutX(1220);
    goBackButton.setLayoutY(700);
    goBackButton.setPrefWidth(250);
    goBackButton.setPrefHeight(50);
    goBackButton.setFont(Fonts.loadSensaWild(40));
    root.getChildren().add(goBackButton);

    goBackButton.setOnAction(e -> {
        if (Main.isLoggedIn) {
            new Dashboard().showDashboard(primaryStage, Main.loggedInUser, false);
        } else {
            Main main = new Main();
            main.start(primaryStage);
        }
    });

    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        root.getChildren().add(nightView);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SemSync - References");
        primaryStage.setX(visualBounds.getMinX());
        primaryStage.setY(visualBounds.getMinY());
        primaryStage.setWidth(visualBounds.getWidth());
        primaryStage.setHeight(visualBounds.getHeight());
        primaryStage.show();
        
        
        // Scaling and centering logic
        Runnable updateLayout = () -> {
            double scaleX = scene.getWidth() / baseWidth;
            double scaleY = scene.getHeight() / baseHeight;
            double scale = Math.max(scaleX, scaleY);

            root.setScaleX(scale);
            root.setScaleY(scale);

            root.setTranslateX(baseWidth * (scale - 1) / 2);
            root.setTranslateY(baseHeight * (scale - 1) / 2);
        };

        // Apply initially and on resize
        updateLayout.run();
        scene.widthProperty().addListener((obs, old, newVal) -> updateLayout.run());
        scene.heightProperty().addListener((obs, old, newVal) -> updateLayout.run());
        
        javafx.application.Platform.runLater(() -> updateLayout.run());
        
        
    }

    // Backwards-compatibility wrapper for older callers
    public void showWalkthrough(Stage primaryStage) {
        showReferences(primaryStage);
    }
}
