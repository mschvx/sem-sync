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

public class Credits {

    // CREDITS WINDOW
    public void showCredits(Stage primaryStage) {
    	
        double baseWidth = Sizing.BASE_WIDTH;
        double baseHeight = Sizing.BASE_HEIGHT;
        
        Pane root = new Pane();
        Scene scene = new Scene(root, 1536, 864);
        scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());

    // Notebook image
    Image detailsImage = new Image("file:Elements/Creds-page.png", false);
    ImageView iv = new ImageView(detailsImage);
    iv.setPreserveRatio(true);
    iv.setFitHeight(900); 
    iv.setLayoutX(-20);
    iv.setLayoutY(40);
    // Clips 
    Image clipsImg = new Image("file:Elements/Clips.png", false);
    ImageView clipsView = new ImageView(clipsImg);
    clipsView.setPreserveRatio(true);
    clipsView.setFitHeight(900);
    clipsView.setLayoutX(-100);
    clipsView.setLayoutY(40);
    
    // stars
    Image star1 = new Image("file:Elements/Star1.png");
    Image star2 = new Image("file:Elements/Star2.png");

    ImageView starA = new ImageView(star1);
    starA.setPreserveRatio(true); starA.setFitWidth(200); starA.setLayoutX(670); starA.setLayoutY(230); starA.setRotate(10);

    ImageView starB = new ImageView(star2);
    starB.setPreserveRatio(true); starB.setFitWidth(200); starB.setLayoutX(260); starB.setLayoutY(400); starB.setRotate(130);

    ImageView starC = new ImageView(star1);
    starC.setPreserveRatio(true); starC.setFitWidth(200); starC.setLayoutX(1170); starC.setLayoutY(250); starC.setRotate(150);
    // Animation for stars
    final Timeline starTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), ev -> {
        starA.setImage(starA.getImage() == star1 ? star2 : star1);
        starB.setImage(starB.getImage() == star1 ? star2 : star1);
        starC.setImage(starC.getImage() == star1 ? star2 : star1);
    }));
    starTimeline.setCycleCount(Timeline.INDEFINITE);
    starTimeline.play();
    
    // Credit images
    Image antonioImg = new Image("file:Elements/Creds/Antonio.png", false);
    Image castroImg = new Image("file:Elements/Creds/Castro.png", false);
    Image legastoImg = new Image("file:Elements/Creds/Legasto.png", false);
    ImageView antonio = new ImageView(antonioImg);
    ImageView castro = new ImageView(castroImg);
    ImageView legasto = new ImageView(legastoImg);
    antonio.setPreserveRatio(true); antonio.setFitWidth(550); antonio.setLayoutX(0); antonio.setLayoutY(460);
    castro.setPreserveRatio(true); castro.setFitWidth(550); castro.setLayoutX(380); castro.setLayoutY(220);
    legasto.setPreserveRatio(true); legasto.setFitWidth(550); legasto.setLayoutX(1100); legasto.setLayoutY(300);

  

    // Name and course text
    Label castroText = new Label("Allane Lee Castro");
    castroText.setFont(Fonts.loadComingSoon(40));
    castroText.setLayoutX(210);
    castroText.setLayoutY(270);

    Label castroCourse = new Label("BS Computer Science");
    castroCourse.setFont(Fonts.loadMontserratRegular(25));
    castroCourse.setLayoutX(210);
    castroCourse.setLayoutY(330);

    Label antonioText = new Label("Juan Marco Antonio");
    antonioText.setFont(Fonts.loadComingSoon(40));
    antonioText.setLayoutX(390);
    antonioText.setLayoutY(570);

    Label antonionCourse = new Label("BS Applied Mathematics");
    antonionCourse.setFont(Fonts.loadMontserratRegular(25));
    antonionCourse.setLayoutX(390);
    antonionCourse.setLayoutY(630);

    Label legastoText1 = new Label("John Christian");
    legastoText1.setFont(Fonts.loadComingSoon(40));
    legastoText1.setLayoutX(970);
    legastoText1.setLayoutY(400);

    Label legastoText2 = new Label("Legasto");
    legastoText2.setFont(Fonts.loadComingSoon(40));
    legastoText2.setLayoutX(1100);
    legastoText2.setLayoutY(445);

    Label legastoCourse = new Label("BS Computer Science");
    legastoCourse.setFont(Fonts.loadMontserratRegular(25));
    legastoCourse.setLayoutX(970);
    legastoCourse.setLayoutY(510);
    
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
    // show CONSUME.png while hovered, pause swapping
    Image consume = new Image("file:Elements/yourenotsupposedtobehere/CONSUME.png");
    bandView.setCursor(Cursor.HAND);
    // NIGHT overlay image 
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
        try {
            FadeTransition fin = new FadeTransition(Duration.millis(220), nightView);
            fin.setFromValue(nightView.getOpacity());
            fin.setToValue(1.0);
            fin.play();
        } catch (Exception ignored) {}
        // speed up star animation while hovere
        starTimeline.setRate(2.5);
    });
    bandView.setOnMouseExited(e -> {
        bandTimeline.play();
        try {
            FadeTransition fout = new FadeTransition(Duration.millis(220), nightView);
            fout.setFromValue(nightView.getOpacity());
            fout.setToValue(0.0);
            fout.play();
        } catch (Exception ignored) {}
        // restore normal star animation rate
        starTimeline.setRate(1.0);
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

    // hover effefcts
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
    Label title = new Label("CREDITS");
    Font titleFont = Fonts.loadSensaWild(64);
    title.setFont(titleFont);
    title.setLayoutX(200);
    title.setLayoutY(90);
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

    root.getChildren().addAll(cYellow, cBlue, cRed, hYellow, hBlue, hRed,
        clipsView,
        iv,
        starA, starB, starC,
        antonio, castro, legasto,
        castroText, castroCourse, antonioText, antonionCourse, legastoText1, legastoText2, legastoCourse,
        title);

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

        // Ruler 
        Image rulerImg = new Image("file:Elements/Ruler.png");
        ImageView rulerView = new ImageView(rulerImg);
        rulerView.setPreserveRatio(false);
        rulerView.fitWidthProperty().bind(scene.widthProperty());
        rulerView.fitHeightProperty().bind(scene.heightProperty());
        rulerView.setLayoutX(0);
        rulerView.setLayoutY(50);
        rulerView.setRotate(0);
        rulerView.setMouseTransparent(true);
        root.getChildren().add(rulerView);
        nightView.toFront();
        primaryStage.setScene(scene);
        primaryStage.setTitle("SemSync - Credits");
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
}
