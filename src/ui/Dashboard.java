package ui;

import calendar.Calendar;
import application.Fonts;
import application.Main;
import courses.Course;
import courses.Laboratory;
import courses.Lecture;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;
import students.Curriculum;
import students.CurriculumLoader;
import students.DegreeLookup;
import students.DegreeProgram;
import students.Students;
import javafx.scene.input.MouseEvent;

public class Dashboard {

    private static final boolean DEBUG_VALIDATION = false;

    private Map<Lecture, Laboratory> lectureToAddedLab = new HashMap<>();
    // Show dashboard for a specific user (so we can display degree greeting)
    public void showDashboard(Stage primaryStage, Students user) {
        Pane root = new Pane();
        root.getStyleClass().add("root");
        Scene scene = new Scene(root, 1536, 864);
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("SemSync - Dashboard"); // Window title
        primaryStage.setX(visualBounds.getMinX());
        primaryStage.setY(visualBounds.getMinY());
        primaryStage.setWidth(visualBounds.getWidth());
        primaryStage.setHeight(visualBounds.getHeight());

        // NOTE TO SELF: CHANGE FOR BOTTOM PADDING
        final double BOTTOM_PADDING = 100; // pixels


        // menu button 
        Image menuImage = null;
        ImageView menuBtn = new ImageView();
        File menuFile = new File("Elements/Menu.png");
        menuImage = new Image(menuFile.toURI().toString());
        menuBtn.setImage(menuImage);
        menuBtn.setFitWidth(130);
        menuBtn.setFitHeight(130);
        menuBtn.setPreserveRatio(true);
        menuBtn.setLayoutX(12);
        menuBtn.setLayoutY(10);


        // sidebar left 
        Pane sidebar = new Pane();
        sidebar.setLayoutX(0);
        sidebar.setLayoutY(0);
        double sidebarWidth = 370;
        double contentShift = 200;   // control this if masyadong malayo yung animation nya sa right
        sidebar.setPrefWidth(sidebarWidth);
        sidebar.setPrefHeight(visualBounds.getHeight());
        double sidebarScrollExtraLimit = 100; // extra space for horizontal scroll 
        // hidden by default; menu button will toggle it
        sidebar.setVisible(false);
        sidebar.setTranslateX(-sidebarWidth);

        // load 
        File imgFile = new File("Elements/Sidebar.png");
        Image sidebarImg = new Image(imgFile.toURI().toString());
        ImageView bg = new ImageView(sidebarImg);
        bg.setFitWidth(sidebarWidth);
        bg.setFitHeight(visualBounds.getHeight());
        bg.setPreserveRatio(false);
        bg.setLayoutX(0);
        bg.setLayoutY(0);
        sidebar.getChildren().add(bg);


        // Sidebar content
        int buttonXPos = 30;
        Label sideTitle = new Label("MENU");
        sideTitle.setStyle("-fx-text-fill: white; -fx-letter-spacing: 1px;");
        sideTitle.setFont(Fonts.loadSensaSerif(110));
        sideTitle.setLayoutX(40);
        sideTitle.setLayoutY(10);
        sideTitle.setStyle("-fx-text-fill: black; -fx-letter-spacing: 1px;");


        Button btnAbout = new Button("ABOUT");
        btnAbout.setLayoutX(buttonXPos);
        btnAbout.setLayoutY(140);
        btnAbout.setPrefWidth(sidebarWidth - 80);
        btnAbout.setPrefHeight(48);
        btnAbout.setFont(Fonts.loadSensaWild(22));
        btnAbout.getStyleClass().add("btn-about");
        btnAbout.getStyleClass().add("sidebar-pill");
        btnAbout.setOnAction(ev -> new About().showAbout(primaryStage));

        Button btnProfile = new Button("PROFILE");
        btnProfile.setLayoutX(buttonXPos);
        btnProfile.setLayoutY(200);
        btnProfile.setPrefWidth(sidebarWidth - 80);
        btnProfile.setPrefHeight(48);
        btnProfile.setFont(Fonts.loadSensaWild(22));
        btnProfile.getStyleClass().add("btn-register");
        btnProfile.getStyleClass().add("sidebar-pill");
        btnProfile.setOnAction(ev -> {  });

        Button btnDashboard = new Button("DASHBOARD");
        btnDashboard.setLayoutX(buttonXPos);
        btnDashboard.setLayoutY(260);
        btnDashboard.setPrefWidth(sidebarWidth - 80);
        btnDashboard.setPrefHeight(48);
        btnDashboard.setFont(Fonts.loadSensaWild(22));
        btnDashboard.getStyleClass().add("btn-create");
        btnDashboard.getStyleClass().add("sidebar-pill");
        btnDashboard.setOnAction(ev -> {  });

        // eye image
        File eyelidFile = new File("Elements/eyelid.png");
        Image eyelidImg = new Image(eyelidFile.toURI().toString());
        ImageView eyelidView = new ImageView(eyelidImg);
        eyelidView.setPreserveRatio(true);
        eyelidView.setFitWidth(sidebarWidth - 80); // for aesthetic
        eyelidView.setLayoutX(buttonXPos);
        eyelidView.setLayoutY(360);

        File eyeFile = new File("Elements/eye.png");
        Image eyeImg = new Image(eyeFile.toURI().toString());
        ImageView eyeView = new ImageView(eyeImg);
        eyeView.setPreserveRatio(true);
        eyeView.setFitWidth(sidebarWidth - 80);
        eyeView.setLayoutX(buttonXPos);
        eyeView.setLayoutY(360);
        

        // Anger overlay image testtest
        final Image angerImg = new Image(new File("Elements/yourenotsupposedtobehere/ANGER.png").toURI().toString());
        ImageView angerView = new ImageView(angerImg);
        angerView.setPreserveRatio(true);
        angerView.setFitWidth(sidebarWidth - 80);
        angerView.setLayoutX(buttonXPos);
        angerView.setLayoutY(360);
        angerView.setVisible(false);

        // NIGHT image
        Image nightImg = new Image(new File("Elements/yourenotsupposedtobehere/NIGHT.png").toURI().toString());
        ImageView nightView = new ImageView(nightImg);
        nightView.setPreserveRatio(false);
        nightView.fitWidthProperty().bind(scene.widthProperty());
        nightView.fitHeightProperty().bind(scene.heightProperty());
        nightView.setLayoutX(0);
        nightView.setLayoutY(0);
        nightView.setOpacity(0);
        nightView.setMouseTransparent(true);

        Button btnCredits = new Button("CREDITS");
        btnCredits.setLayoutX(buttonXPos);
        btnCredits.setLayoutY(570);
        btnCredits.setPrefWidth(sidebarWidth - 80);
        btnCredits.setPrefHeight(48);
        btnCredits.setFont(Fonts.loadSensaWild(22));
        btnCredits.getStyleClass().add("btn-about");
        btnCredits.setOnAction(ev -> new Credits().showCredits(primaryStage));
        btnCredits.getStyleClass().add("sidebar-pill");

        Button btnReferences = new Button("REFERENCES");
        btnReferences.setLayoutX(buttonXPos);
        btnReferences.setLayoutY(630);
        btnReferences.setPrefWidth(sidebarWidth - 80);
        btnReferences.setPrefHeight(48);
        btnReferences.setFont(Fonts.loadSensaWild(22));
        btnReferences.getStyleClass().add("btn-register");
        btnReferences.getStyleClass().add("sidebar-pill");
        btnReferences.setOnAction(ev -> new References().showReferences(primaryStage));

        // Logout button: 
        Button btnLogout = new Button("LOG OUT");
        btnLogout.setLayoutX(buttonXPos);
        btnLogout.setLayoutY(700);
        btnLogout.setPrefWidth(sidebarWidth - 80);
        btnLogout.setPrefHeight(58); 
        btnLogout.setFont(Fonts.loadMontserratBold(22));
        btnLogout.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #ff827f, #ff4c4c);"
            + " -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 22px;"
            + " -fx-border-radius: 28; -fx-background-radius: 28; -fx-padding: 12 18 12 18;"
            + " -fx-effect: none; -fx-border-color: rgba(255,255,255,0.12); -fx-border-width: 1;"
            + " -fx-cursor: default; -fx-min-height: 56px;");
        // when hover hide eyelid eand eye.png
        btnLogout.setOnMouseEntered(ev -> {
            try {
                eyelidView.setVisible(false);
                eyeView.setVisible(false);
                angerView.setVisible(true);
            } catch (Exception ex) {
            }
                try {
                    // make night overlay above everything
                    nightView.toFront();
                    javafx.animation.FadeTransition fin = new javafx.animation.FadeTransition(Duration.millis(220), nightView);
                    fin.setFromValue(nightView.getOpacity());
                    fin.setToValue(1.0);
                    fin.play();
                } catch (Exception ignored) {}
        });
        btnLogout.setOnMouseExited(ev -> {
            try {
                angerView.setVisible(false);
                eyeView.setVisible(true);
                eyelidView.setVisible(true);
            } catch (Exception ex) {
            }
                try {
                    nightView.toFront();
                    javafx.animation.FadeTransition fout = new javafx.animation.FadeTransition(Duration.millis(220), nightView);
                    fout.setFromValue(nightView.getOpacity());
                    fout.setToValue(0.0);
                    fout.play();
                } catch (Exception ignored) {}
        });
        btnLogout.setFocusTraversable(false);

        btnLogout.setOnAction(ev -> {
            try {
                Main.isLoggedIn = false;
                Main.loggedInUser = null;
                Main main = new Main();
                main.start(primaryStage); // go back to login screen
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Pane sidebarContent = new Pane();
        sidebarContent.getChildren().addAll(sideTitle, btnAbout, btnProfile, btnDashboard, eyeView, eyelidView, angerView, btnCredits, btnReferences, btnLogout);

        javafx.scene.control.ScrollPane sidebarScroll = new javafx.scene.control.ScrollPane(sidebarContent);
        sidebarScroll.setLayoutX(0);
        sidebarScroll.setLayoutY(0);
        sidebarScroll.setPrefWidth(sidebarWidth);
        sidebarScroll.setPrefHeight(visualBounds.getHeight());
        sidebarScroll.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sidebarScroll.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        sidebarScroll.setPannable(true);
        sidebarScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // add bg and scroll pane to sidebar
        sidebar.getChildren().addAll(sidebarScroll);


        // --- Contents ---
        Pane content = new Pane();
        content.setStyle("-fx-background-color: transparent;");
        content.setLayoutX(0);
        content.setLayoutY(0);
        content.setPrefWidth(visualBounds.getWidth());

        // Put content to make scrollable
        javafx.scene.control.ScrollPane contentScroll = new javafx.scene.control.ScrollPane(content);
        contentScroll.setLayoutX(0);
        contentScroll.setLayoutY(0);
        contentScroll.setPrefWidth(visualBounds.getWidth());
        contentScroll.setPrefHeight(visualBounds.getHeight());
        contentScroll.setFitToWidth(true);
        contentScroll.setPannable(true);
        contentScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        root.getChildren().addAll(sidebar, contentScroll, menuBtn);

        // When sidebar opens,  horizontal scrolling since nag-ooverflow 
        sidebar.visibleProperty().addListener((obs, oldV, newV) -> {
            try {
                if (newV) {
                    contentScroll.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
                    contentScroll.setFitToWidth(false);
                    // increase content width so horizontal scrollbar appears and you can scroll
                    try {
                        double extra = Math.min(sidebarWidth, sidebarScrollExtraLimit);
                        content.setPrefWidth(scene.getWidth() + extra);
                    } catch (Exception ex) {}
                } else {
                    contentScroll.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
                    contentScroll.setFitToWidth(true);
                    try {
                        content.setPrefWidth(scene.getWidth());
                    } catch (Exception ex) {}
                }
            } catch (Exception ignored) {}
        });

        // (dashboard state to profile state)
        try {
                btnDashboard.setOnAction(ev -> {
                try {
                    Object openFlag = content.getProperties().get("profileOpen");
                    boolean open = openFlag instanceof Boolean && (Boolean) openFlag;
                    if (open) {
                        new Profile().showInContent(content, scene, user, contentScroll);
                    }
                } catch (Exception ex) {
                }
            });
        } catch (Exception ignore) {}

        // go to top of page
        try {
            javafx.application.Platform.runLater(() -> {
                try { contentScroll.setVvalue(0.0); } catch (Exception ignore) {}
            });
        } catch (Exception ignore) {}

        // profile state (switch to dashboard staet)
        try {
                btnProfile.setOnAction(ev -> {
                try {
                    Object openFlag = content.getProperties().get("profileOpen");
                    boolean open = openFlag instanceof Boolean && (Boolean) openFlag;
                    if (!open) {
                        new Profile().showInContent(content, scene, user, contentScroll);
                    }
                } catch (Exception ex) {
                }
            });
        } catch (Exception ignored) {}

        // --- Profile image ---
        final Image profile1 = new Image(new File("Elements/Profile1.png").toURI().toString());
        final Image profile2 = new Image(new File("Elements/Profile2.png").toURI().toString());
        final ImageView profileView = new ImageView(profile1);
        profileView.setPreserveRatio(true);
        profileView.setFitWidth(200);
        profileView.layoutXProperty().bind(scene.widthProperty().subtract(profileView.fitWidthProperty()).subtract(10));
        profileView.setLayoutY(10);
        profileView.getStyleClass().add("profile-image");

        // profile hover animation 
        final Timeline profileTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), ev -> {
            if (profileView.getImage() == profile1) {
                profileView.setImage(profile2);
            } else {
                profileView.setImage(profile1);
            }
        }));
        profileTimeline.setCycleCount(Timeline.INDEFINITE);

        profileTimeline.stop();

        profileView.setOnMouseEntered(ev -> profileTimeline.play());
        profileView.setOnMouseExited(ev -> {
            try {
                profileTimeline.stop();
                profileView.setImage(profile1);
            } catch (Exception ignored) {}
        });
        profileView.setOnMouseClicked(ev -> {
            try {
                new Profile().showInContent(content, scene, user, contentScroll);
                profileView.toFront();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        

        // Make the eye follow the mouse cursor by rotating the eye image around its center
        final javafx.scene.transform.Rotate eyeRotate = new javafx.scene.transform.Rotate(0, 0, 0);
        eyeView.getTransforms().add(eyeRotate);
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, ev -> {
            try {
                javafx.geometry.Bounds localBounds = eyeView.getBoundsInLocal();
                double pivotLocalX = localBounds.getWidth() * 0.5;
                double pivotLocalY = localBounds.getHeight() * 0.5;
                eyeRotate.setPivotX(pivotLocalX);
                eyeRotate.setPivotY(pivotLocalY);

                javafx.geometry.Point2D pivotScene = eyeView.localToScene(pivotLocalX, pivotLocalY);
                double dx = ev.getSceneX() - pivotScene.getX();
                double dy = ev.getSceneY() - pivotScene.getY();
                double angle = Math.toDegrees(Math.atan2(dy, dx));
                // rotate to retain original position (since for some reason nasa right side ang default)
                final double ROTATION_OFFSET = -90.0; // subtract 90 degrees
                eyeRotate.setAngle(angle + ROTATION_OFFSET);
            } catch (Exception ex) {
                // none
            }
        });

        // Menu hover rotation every 0.5 seconds
        final Timeline menuTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), ev -> {
            if (menuBtn.getRotate() == 3) {
                menuBtn.setRotate(-3);
            } else {
                menuBtn.setRotate(3);
            }
        }));
        menuTimeline.setCycleCount(Timeline.INDEFINITE);

        menuBtn.setOnMouseEntered(e -> menuTimeline.play());
        menuBtn.setOnMouseExited(e -> {
            menuTimeline.stop();
            menuBtn.setRotate(0);
        });

        // Toggle sidebar animation 
        SimpleBooleanProperty open = new SimpleBooleanProperty(false);
        final double menuNudge = 185; // how far the menu button moves right when sidebar opens
        menuBtn.setOnMouseClicked(e -> {
            if (!open.get()) {
                // when sidebar clicked: make visible, slide in, shift content right
                sidebar.setVisible(true);
                sidebar.toFront(); // always sidebar to front so all buttosn will always be clickable
                TranslateTransition tSidebar = new TranslateTransition(Duration.millis(260), sidebar);
                tSidebar.setFromX(-sidebarWidth);
                tSidebar.setToX(0);
                TranslateTransition tContent = new TranslateTransition(Duration.millis(260), content);
                tContent.setToX(contentShift);
                TranslateTransition tMenu = new TranslateTransition(Duration.millis(260), menuBtn);
                tMenu.setToX(menuNudge);
                tSidebar.play(); tContent.play(); tMenu.play();
                menuBtn.toFront();
                open.set(true);
            } else {
                // if close then slide sidebar out, shift content back, then hide
                TranslateTransition tSidebar = new TranslateTransition(Duration.millis(260), sidebar);
                tSidebar.setFromX(0);
                tSidebar.setToX(-sidebarWidth);
                TranslateTransition tContent = new TranslateTransition(Duration.millis(260), content);
                tContent.setToX(0);
                TranslateTransition tMenu = new TranslateTransition(Duration.millis(260), menuBtn);
                tMenu.setToX(0);
                tSidebar.setOnFinished(ev -> { sidebar.setVisible(false); menuBtn.toFront(); });
                tSidebar.play(); tContent.play(); tMenu.play();
                open.set(false);
            }
        });

        // Greeting
        String firstName = (user != null) ? user.getFirstName() : "";
        String displayName = firstName == null ? "" : firstName;
        if (displayName.length() > 15) displayName = displayName.substring(0, 20) + "...";
        String greeting = "Hello, " + displayName + "!";

        String degreeCode = (user != null) ? user.getDegree() : null;
        DegreeProgram program = DegreeLookup.fromCode(degreeCode);

        Label greetLabel = new Label(greeting);
        Font greetFont = Font.loadFont(Fonts.SENSA_SERIF, 80);
        greetLabel.setFont(greetFont);
        greetLabel.setLayoutX(160);
        greetLabel.setLayoutY(60);
        greetLabel.setStyle("-fx-text-fill: black;");
        content.getChildren().add(greetLabel);


        // calendar
        double calX = 160;
        double calY = 160;
        double calWidth = 1200;
        double calHeight = 560; 

        // Header for calendar
        double calHeaderHeight = 72;
        double calHeaderMargin = 12;
        double calendarTopY = calY + calHeaderHeight + calHeaderMargin;

        Label calendarLabel = new Label("Calendar");
        calendarLabel.setFont(Fonts.loadMontserratRegular(28)); 
        calendarLabel.setWrapText(true);
        calendarLabel.setLayoutX(calX);
        calendarLabel.setLayoutY(calY);
        calendarLabel.setPrefWidth(calWidth);
        calendarLabel.setPrefHeight(calHeaderHeight);
        calendarLabel.getStyleClass().add("fixed-header");

        Calendar calendar = new Calendar(calWidth, calHeight); // call the calendar from the calendar package and make a new calendar
        Pane calendarPane = calendar.getView();
        calendarPane.setLayoutX(calX);
        calendarPane.setLayoutY(calendarTopY);

        // header add Classes section 
        Label manageLabel = new Label("Add Classes");
        manageLabel.setFont(Fonts.loadMontserratRegular(28));
        manageLabel.setLayoutX(160);
        manageLabel.setLayoutY(calendarTopY + calHeight + 16);
        manageLabel.setPrefWidth(calWidth);
        manageLabel.setPrefHeight(48);
        manageLabel.getStyleClass().add("fixed-header");
        
        // test merge conflict

        // Search fields (placeholders - not wired yet)
        Label lblSearchCode = new Label("Search by Course Code");
        lblSearchCode.setLayoutX(160);
        lblSearchCode.setLayoutY(calendarTopY + calHeight + 100);
        TextField txtSearchCode = new TextField();
        txtSearchCode.setLayoutX(360);
        txtSearchCode.setLayoutY(1);
        txtSearchCode.setPrefWidth(220);

        Label lblSearchSection = new Label("Search by Section");
        lblSearchSection.setLayoutX(160);
        lblSearchSection.setLayoutY(calendarTopY + calHeight + 100);
        TextField txtSearchSection = new TextField();
        txtSearchSection.setLayoutX(360);
        txtSearchSection.setLayoutY(1);
        txtSearchSection.setPrefWidth(220);

        /*
        // -- List of items added by user --
        Label itemsLabel = new Label("Your Items");
        itemsLabel.setFont(Fonts.loadMontserratRegular(28));
        itemsLabel.setLayoutX(160);
        itemsLabel.setLayoutY(calendarTopY + calHeight + 180);
        itemsLabel.setStyle("-fx-text-fill: black;");

        ObservableList<Course> listItems = FXCollections.observableArrayList();
        ListView<Course> listView = new ListView<>(listItems);
        listView.setLayoutX(160);
        listView.setLayoutY(calendarTopY + calHeight + 220);
        listView.setPrefWidth(1200);
        listView.setPrefHeight(260);   
        
        listView.setCellFactory(lv -> new ListCell<Course>() {
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCourseCode() + " - " + item.getCourseTitle() + 
                            " - Units: " + item.getUnits() + 
                            " - Section: " + item.getSection() + 
                            " - " + item.getTimes() + 
                            " - " + item.getDays());
                }
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        });
        
        ObservableList<Course> list = FXCollections.<Course>observableArrayList();
        ObservableList<Laboratory> labSections = FXCollections.<Laboratory>observableArrayList(); // Added if ever needed
        ObservableList<Lecture> lecSections = FXCollections.<Lecture>observableArrayList(); // Added if ever needed

        // Build a list of allowed curriculum course codes (used as reference)
        */

        ObservableList<Course> list = FXCollections.<Course>observableArrayList();
        ObservableList<Laboratory> labSections = FXCollections.<Laboratory>observableArrayList(); // Added if ever needed
        ObservableList<Lecture> lecSections = FXCollections.<Lecture>observableArrayList(); // Added if ever needed
        List<String> allowedCourseCodes = new ArrayList<>();
        if (program != null && program.getCurriculumCSV() != null) {
            try {
                List<Curriculum> curr = CurriculumLoader.loadfromCSV(program.getCurriculumCSV());
                for (Curriculum c : curr) {
                    if (c != null && c.getCourseCode() != null) {
                        allowedCourseCodes.add(normalizeCourseCode(c.getCourseCode()));
                    }
                }
            } catch (Exception ex) {
                
            }
        }

        // Always read the course_offerings.csv and add offerings that match the allowed curriculum codes.
        Path folder = Paths.get("Database/ICS_Dataset");
        Path file = folder.resolve("course_offerings.csv");
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 7) continue; // malformed
                String courseCode = parts[0].trim();
                String courseName = parts[1].trim();
                int units = parseUnits(parts.length > 2 ? parts[2].trim() : "");
                String section = parts[3].trim();
                String times = parts[4].trim();
                String days = parts[5].trim();
                String rooms = parts[6].trim();                            
                

                boolean matches = false;
                if (!allowedCourseCodes.isEmpty()) {
                    String norm = normalizeCourseCode(courseCode);
                    if (allowedCourseCodes.contains(norm)) matches = true;
                }

                // If no allowed codes available, ignore, else only add matches
                if (allowedCourseCodes.isEmpty() || matches) {
                	
                    if(!isLabSection(section)) {
                    	Lecture lec = new Lecture(courseCode, courseName, units, section, times, days, rooms);
                    	lecSections.add(lec);
                    	list.add(lec);
                    	
                    } else {
                        Laboratory lab = new Laboratory(courseCode, courseName, units, section, times, days, rooms);
                        labSections.add(lab);
                        list.add(lab); 
                        
                        String[] labSecParts = section.split("-");
                        String lec = labSecParts[0].trim();
                        String labCourseNorm = normalizeCourseCode(courseCode).toUpperCase();

                    	for(Lecture l : lecSections) {
                    		if (l == null) continue;
                    		String lecSection = l.getSection() == null ? "" : l.getSection().trim();
                    		String lecCourseNorm = normalizeCourseCode(l.getCourseCode() == null ? "" : l.getCourseCode()).toUpperCase();

                    		if (lecSection.equalsIgnoreCase(lec) && !lecCourseNorm.isEmpty() && lecCourseNorm.equals(labCourseNorm)) {
                    			l.addLabSection(lab);
                    			lab.setlectureSection(l);
                    			lectureToAddedLab.put(l, lab);
                    		}
                    	}

                    }   
                }
            }
        } catch (IOException error) {
            System.out.print("Error reading course_offerings.csv");
        }
        // lec/la pairings
        try {
            for (Laboratory lab : labSections) {
                if (lab.getlectureSection() != null) continue;
                String lecSec = lab.getSection();
                if (lecSec == null) continue;
                String[] parts = lecSec.split("-");
                if (parts.length > 0) lecSec = parts[0].trim();

                String labCourseNorm = normalizeCourseCode(lab.getCourseCode() == null ? "" : lab.getCourseCode()).toUpperCase();

                for (Lecture L : lecSections) {
                    if (L == null) continue;
                    String Lsec = L.getSection() == null ? "" : L.getSection();
                    String lecCourseNorm = normalizeCourseCode(L.getCourseCode() == null ? "" : L.getCourseCode()).toUpperCase();
                    if (Lsec != null && lecSec.equalsIgnoreCase(Lsec) && !labCourseNorm.isEmpty() && lecCourseNorm.equals(labCourseNorm)) {
                        L.addLabSection(lab);
                        lab.setlectureSection(L);
                        break;
                    }
                }
            }
        } catch (Exception ignored) {}
        
     
        
        // Add to pane
        /*
        Pane bottomSpacer = new Pane();
        bottomSpacer.setLayoutX(160);
        bottomSpacer.setLayoutY(listView.getLayoutY() + listView.getPrefHeight() + 10);
        bottomSpacer.setPrefWidth(1);
        bottomSpacer.setPrefHeight(400);

        // catalogue button
        Button btnCatalogueMain = new Button("CATALOGUE");
        btnCatalogueMain.setLayoutX(160);
        btnCatalogueMain.setLayoutY(listView.getLayoutY() + listView.getPrefHeight() + 14);
        btnCatalogueMain.setPrefWidth(220);
        btnCatalogueMain.setPrefHeight(48);
        btnCatalogueMain.setFont(Fonts.loadSensaWild(22));
        btnCatalogueMain.getStyleClass().add("btn-create");
        btnCatalogueMain.getStyleClass().add("sidebar-pill");
        btnCatalogueMain.setOnAction(ev -> {
            System.out.println("WALA PAAAAAA <WOIJOIDJASOIDJ");

        });
        */

        // Search areas
        double searchBoxWidth = 600;
        double searchBoxHeight = 96;
        double searchGap = 16; // reduced gap so fields are closer together

        Pane searchArea = new Pane();
        searchArea.layoutYProperty().bind(lblSearchCode.layoutYProperty().add(lblSearchCode.heightProperty()).add(4));
        searchArea.setPrefWidth(searchBoxWidth * 2 + searchGap);
        searchArea.setPrefHeight(searchBoxHeight + 80);

        // center the search horizontally 
        searchArea.layoutXProperty().bind(Bindings.createDoubleBinding(() ->
            (scene.getWidth() - searchArea.getPrefWidth()) / 2.0,
            scene.widthProperty()));

        lblSearchCode.setLayoutX(0);
        lblSearchCode.setLayoutY(0);
        lblSearchCode.setFont(Fonts.loadComingSoon(34));
        lblSearchCode.setStyle("-fx-text-fill: black;");

        lblSearchSection.setLayoutX(searchBoxWidth + searchGap);
        lblSearchSection.setLayoutY(0);
        lblSearchSection.setFont(Fonts.loadComingSoon(34));
        lblSearchSection.setStyle("-fx-text-fill: black;");

        Pane courseBox = new Pane();
        courseBox.setLayoutX(0);
        // reduce internal top offset so the input sits nearer the label
        courseBox.setLayoutY(8);
        courseBox.setPrefWidth(searchBoxWidth);
        courseBox.setPrefHeight(searchBoxHeight);
        courseBox.setStyle("-fx-background-color: linear-gradient(#FFD54F, #ffb91a); -fx-background-radius: 28; -fx-padding: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0.5, 0, 2);");

        txtSearchCode.setLayoutX(18);
        txtSearchCode.setLayoutY(16);
        txtSearchCode.setPrefWidth(searchBoxWidth - 36);
        txtSearchCode.setPrefHeight(searchBoxHeight - 32);
        txtSearchCode.setFont(Fonts.loadMontserratRegular(24));
        txtSearchCode.setStyle("-fx-background-color: #ebebeb; -fx-border-color: black; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-fill: black; -fx-border-width: 4;");
        courseBox.getChildren().add(txtSearchCode);

        Pane sectionBox = new Pane();
        sectionBox.setLayoutX(searchBoxWidth + searchGap);
        sectionBox.setLayoutY(8);
        sectionBox.setPrefWidth(searchBoxWidth);
        sectionBox.setPrefHeight(searchBoxHeight);
        sectionBox.setStyle("-fx-background-color: linear-gradient(#FFD54F, #ffb91a); -fx-background-radius: 28; -fx-padding: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0.5, 0, 2);");

        txtSearchSection.setLayoutX(18);
        txtSearchSection.setLayoutY(16);
        txtSearchSection.setPrefWidth(searchBoxWidth - 36);
        txtSearchSection.setPrefHeight(searchBoxHeight - 32);
        txtSearchSection.setFont(Fonts.loadMontserratRegular(24));
        txtSearchSection.setStyle("-fx-background-color: #ebebeb; -fx-border-color: black; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-fill: black; -fx-border-width: 4;");
        sectionBox.getChildren().add(txtSearchSection);

        // Search and show all buttons
        Button btnSearch = new Button("SEARCH");
        btnSearch.setPrefWidth(420);
        btnSearch.setPrefHeight(64);
        btnSearch.setFont(Fonts.loadSensaWild(22));
        btnSearch.getStyleClass().add("btn-create");
        btnSearch.getStyleClass().add("sidebar-pill");

        Button btnShowAll = new Button("SHOW ALL");
        btnShowAll.setPrefWidth(420);
        btnShowAll.setPrefHeight(64);
        btnShowAll.setFont(Fonts.loadSensaWild(22));
        btnShowAll.getStyleClass().add("btn-create");
        btnShowAll.getStyleClass().add("sidebar-pill");

        // center the buttons
        btnSearch.layoutXProperty().bind(Bindings.createDoubleBinding(() ->
            (searchArea.getPrefWidth() - (btnSearch.getPrefWidth() + 16 + btnShowAll.getPrefWidth())) / 2.0,
            searchArea.widthProperty()));
        btnSearch.layoutYProperty().set(courseBox.getLayoutY() + searchBoxHeight + 20);

        btnShowAll.layoutXProperty().bind(Bindings.createDoubleBinding(() ->
            btnSearch.getLayoutX() + btnSearch.getPrefWidth() + 16,
            btnSearch.layoutXProperty()));
        btnShowAll.layoutYProperty().bind(btnSearch.layoutYProperty());

        btnShowAll.setOnAction(ev -> {
            // will be wired after table/pagination is created
        });

        searchArea.getChildren().addAll(courseBox, sectionBox, btnSearch, btnShowAll);

        // Table for add classes
        final TableView<Course>[] editTableRef = new TableView[]{null};
        class PairView {
            private Lecture lecture;
            private Laboratory lab;
            public PairView(Lecture lecture, Laboratory lab) { this.lecture = lecture; this.lab = lab; }
            public Lecture getLecture() { return lecture; }
            public Laboratory getLab() { return lab; }
            public String getCourseCode() { if (lecture != null) return lecture.getCourseCode(); if (lab != null) return lab.getCourseCode(); return ""; }
        }

        TableView<PairView> classTable = new TableView<>();
        classTable.prefWidthProperty().bind(searchArea.prefWidthProperty());
        classTable.setPrefHeight(420);
        classTable.setFixedCellSize(200);
        classTable.layoutXProperty().bind(searchArea.layoutXProperty());

        classTable.layoutYProperty().bind(Bindings.createDoubleBinding(() ->
            searchArea.getLayoutY() + btnSearch.getLayoutY() + btnSearch.getPrefHeight() + 36,
            searchArea.layoutYProperty(), btnSearch.layoutYProperty(), btnSearch.prefHeightProperty()));

        // Columns
        TableColumn<PairView, String> codeCol = new TableColumn<>("Code");
        codeCol.setPrefWidth(180);
        codeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue() == null ? "" : c.getValue().getCourseCode()));
        codeCol.setCellFactory(col -> new javafx.scene.control.TableCell<PairView, String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setGraphic(null);} else {
                    setText(item);
                    if (!getStyleClass().contains("code-cell-font")) getStyleClass().add("code-cell-font");
                }
            }
        });

        TableColumn<PairView, PairView> detailsCol = new TableColumn<>("Class Details");
        // Make details column responsive to table width
        detailsCol.prefWidthProperty().bind(classTable.widthProperty().subtract(codeCol.prefWidthProperty()).subtract(160));
        detailsCol.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue()));
        detailsCol.setCellFactory(col -> new javafx.scene.control.TableCell<PairView, PairView>() {
            @Override protected void updateItem(PairView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setGraphic(null); return; }

                // design for lecture UI in table
                VBox leftCard = new VBox();
                leftCard.setPrefWidth((detailsCol.getWidth() - 24) / 2.0);
                Label leftHeader = new Label("Lecture/Main");
                leftHeader.setFont(Fonts.loadMontserratRegular(16));
                leftHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                Label leftUnits = new Label( (item.getLecture() != null ? String.valueOf(item.getLecture().getUnits()) : "") + " units");
                leftUnits.setFont(Fonts.loadMontserratRegular(14));
                leftUnits.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                HBox leftHeaderBox = new HBox();
                leftHeaderBox.getStyleClass().add("pair-card-header");
                leftHeaderBox.setPrefWidth(leftCard.getPrefWidth());
                leftHeaderBox.getChildren().addAll(leftHeader);
                leftHeaderBox.setSpacing(8);
                Region leftSpacer = new Region();
                HBox.setHgrow(leftSpacer, javafx.scene.layout.Priority.ALWAYS);
                leftHeaderBox.getChildren().addAll(leftSpacer, leftUnits);
                Label leftBody = new Label();
                leftBody.setFont(Fonts.loadMontserratRegular(14));
                leftBody.getStyleClass().add("pair-card-body");
                leftBody.setWrapText(true);
                if (item.getLecture() != null) {
                    Lecture lec = item.getLecture();
                    leftBody.setText(lec.getSection() + " - (" + lec.getTimes() + ")\nLocation: " + (lec.getRooms() == null ? "TBA" : lec.getRooms()));
                    Label leftDays = createDaysBox(lec.getDays());
                    leftCard.getChildren().addAll(leftHeaderBox, leftBody, leftDays);
                } else {
                    leftBody.setText("-- No lecture --");
                    leftCard.getChildren().addAll(leftHeaderBox, leftBody);
                }

                // design for lab ui intable
                VBox rightCard = new VBox();
                rightCard.setPrefWidth((detailsCol.getWidth() - 24) / 2.0);
                Label rightHeader = new Label("Lab/Recit");
                rightHeader.setFont(Fonts.loadMontserratRegular(16));
                rightHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                Label rightUnits = new Label( (item.getLab() != null ? String.valueOf(item.getLab().getUnits()) : "") + " units");
                rightUnits.setFont(Fonts.loadMontserratRegular(14));
                rightUnits.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                HBox rightHeaderBox = new HBox();
                rightHeaderBox.getStyleClass().add("pair-card-header");
                rightHeaderBox.setPrefWidth(rightCard.getPrefWidth());
                rightHeaderBox.getChildren().addAll(rightHeader);
                rightHeaderBox.setSpacing(8);
                Region rightSpacer = new Region();
                HBox.setHgrow(rightSpacer, javafx.scene.layout.Priority.ALWAYS);
                rightHeaderBox.getChildren().addAll(rightSpacer, rightUnits);
                Label rightBody = new Label();
                rightBody.setFont(Fonts.loadMontserratRegular(14));
                rightBody.getStyleClass().add("pair-card-body");
                rightBody.setWrapText(true);
                if (item.getLab() != null) {
                    Laboratory lab = item.getLab();
                    rightBody.setText(lab.getSection() + " - (" + lab.getTimes() + ")\nLocation: " + (lab.getRooms() == null ? "TBA" : lab.getRooms()));
                    Label rightDays = createDaysBox(lab.getDays());
                    rightCard.getChildren().addAll(rightHeaderBox, rightBody, rightDays);
                } else {
                    rightBody.setText("-- No corresponding class --");
                    rightCard.getChildren().addAll(rightHeaderBox, rightBody);
                }

                HBox container = new HBox(12);
                container.getChildren().addAll(leftCard, rightCard);
                container.getStyleClass().add("pair-card-container");
                setText(null);
                setGraphic(container);
            }
        });

        TableColumn<PairView, PairView> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(160);
        actionCol.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue()));
        actionCol.setCellFactory(col -> new javafx.scene.control.TableCell<PairView, PairView>() {
            private final Button btn = new Button("ADD");
            {
                btn.getStyleClass().add("add-btn-green");
                btn.setPrefWidth(120);
                btn.setPrefHeight(36);
            }
            @Override protected void updateItem(PairView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);                                                           
                } else {
                    btn.setFont(Fonts.loadMontserratRegular(16));
                    Lecture copyLec = item.getLecture();
                    Laboratory copyLab = item.getLab();
                    
                    Course rep = (copyLab != null) ? copyLab : copyLec;

                    boolean already = isCourseAdded(rep, user);

                    if (already) {
                        btn.setDisable(true);
                        btn.setText("ADDED");
                    } else {
                        btn.setDisable(false);
                        btn.setText("ADD");
                    }                                       
                    
                    btn.setOnAction(ev -> {
                        try {
                        	boolean addedAny = false;
                        	String failReason = null;
                        	Lecture lec = item.getLecture();
                        	Laboratory lab = item.getLab();
                        	String lecReason = null;
                        	String labReason = null;

                            if (lec != null) lecReason = validateCourseForUser(lec, user, lab == null ? null : java.util.Collections.singletonList(lab));

                            if (lab != null) {
                                labReason = validateCourseForUser(lab, user, lec == null ? null : java.util.Collections.singletonList(lec));
                                if (labReason == null && lec != null && lecReason == null) {
                                    // additionally fail the lab if it overlaps with the lecture we're about to add
                                    if (overlaps(lab, lec)) {
                                        labReason = "Time overlap with the lecture section " + lec.getCourseCode() + " " + lec.getSection();
                                    }
                                }

                                // Extra explicit check: ensure the lab does not overlap ANY existing course (including other labs)
                                if (labReason == null) {
                                    for (Course existing : user.getCourses()) {
                                        if (existing == null) continue;
                                        if (existing.equals(lec)) continue; // skip the lecture we're adding together
                                        if (overlaps(lab, existing)) {
                                            labReason = "Time overlap with " + existing.getCourseCode() + " " + existing.getSection();
                                            break;
                                        }
                                    }
                                }
                            }
                            // add lec/lab to edit table
                            if (lec != null) {
                                if (lab != null) {
                                    // Both lecture and lab present in the pair: require both to be valid before adding
                                    if (lecReason == null && labReason == null) {
                                        user.addCourse(lec);
                                        calendar.addCourse(lec, FXCollections.observableArrayList(user.getCourses()));
                                        user.addCourse(lab);
                                        calendar.addCourse(lab, FXCollections.observableArrayList(user.getCourses()));
                                        // remember which lab was actually added for this lecture
                                        try { if (lec instanceof Lecture) lectureToAddedLab.put((Lecture)lec, lab); } catch (Exception ignore) {}
                                        addedAny = true;
                                        if (editTableRef[0] != null) editTableRef[0].getItems().add(lec);
                                    } else {
                                        // do not add either if the lab conflicts; report the reasons
                                        if (lecReason != null) failReason = (failReason == null) ? ("Lecture not added: " + lecReason) : (failReason + "; Lecture not added: " + lecReason);
                                        if (labReason != null) failReason = (failReason == null) ? ("Lab not added: " + labReason) : (failReason + "; Lab not added: " + labReason);
                                    }
                                } else {
                                    // only lecture present
                                    if (lecReason == null) {
                                        user.addCourse(lec);
                                        calendar.addCourse(lec, FXCollections.observableArrayList(user.getCourses()));
                                        addedAny = true;
                                        if (editTableRef[0] != null) editTableRef[0].getItems().add(lec);
                                    } else {
                                        failReason = (failReason == null) ? ("Lecture not added: " + lecReason) : (failReason + "; Lecture not added: " + lecReason);
                                    }
                                }
                            } else if (lab != null && labReason == null) {
                                // lecture either not present or couldn't be added, but lab can be (standalone lab addition)
                                if(user.getCourses().contains(lab.getlectureSection())) {
                                    user.addCourse(lab);
                                    calendar.addCourse(lab, FXCollections.observableArrayList(user.getCourses()));
                                    addedAny = true;
                                    // record mapping from its lecture to this lab so edit table shows correct pairing
                                    try { Lecture parent = lab.getlectureSection(); if (parent != null) lectureToAddedLab.put(parent, lab); } catch (Exception ignore) {}
                                    if (editTableRef[0] != null) editTableRef[0].getItems().add(lab);
                                } else {
                                    failReason = "Lecture not added: " + lecReason + " Lab not added: Lecture section \"" + lab.getlectureSection().getSection() + "\" does not exist";
                                }
                            } else {
                                // neither added
                                if (lec != null && lecReason != null)
                                    failReason = (failReason == null) ? ("Lecture not added: " + lecReason) : (failReason + "; Lecture not added: " + lecReason);
                                if (lab != null && labReason != null)
                                    failReason = (failReason == null) ? ("Lab not added: " + labReason) : (failReason + "; Lab not added: " + labReason);
                            }

                            if (addedAny) {
                                btn.setDisable(true);
                                btn.setText("ADDED");
                                showToast(root, "Added to your schedule", 2200);
                            }
                            if (!addedAny && failReason != null) {
                                showToast(root, failReason, 3200);
                            } else if (!addedAny && failReason == null) {
                                showToast(root, "Nothing added", 1800);
                            }
                            
                            classTable.refresh();
                            if (editTableRef[0] != null) editTableRef[0].refresh();
                            
                        } catch (Exception ex) { ex.printStackTrace(); }
                    });
                    setGraphic(btn);
                }
            }
        });

        classTable.getColumns().addAll(codeCol, detailsCol, actionCol);
        classTable.setItems(FXCollections.observableArrayList());

        // Placeholder for empty table 
        Label placeholder = new Label("NO DATA AVAILABLE");
        placeholder.setFont(Fonts.loadComingSoon(30));
        placeholder.setStyle("-fx-text-fill: black;");
        placeholder.setAlignment(javafx.geometry.Pos.CENTER);
        placeholder.prefWidthProperty().bind(classTable.widthProperty());
        classTable.setPlaceholder(placeholder);

        // pagination of 5 per section
        final ObservableList<PairView> fullResults = FXCollections.observableArrayList();
        final int pageSize = 5;
        final int[] currentPage = new int[]{1};


        final Runnable[] updatePaginationUI = new Runnable[1];
        Runnable updatePage = () -> {
            int total = fullResults.size();
            int totalPages = Math.max(1, (int) Math.ceil(total / (double) pageSize));
            if (currentPage[0] > totalPages) currentPage[0] = totalPages;
            if (currentPage[0] < 1) currentPage[0] = 1;
            int from = (currentPage[0] - 1) * pageSize;
            int to = Math.min(from + pageSize, total);
            List<PairView> pageItems = new ArrayList<>();
            if (from < to) pageItems.addAll(fullResults.subList(from, to));
            classTable.setItems(FXCollections.observableArrayList(pageItems));
            double rows = Math.max(1, pageItems.size());
            double header = 38; 
            double newH = header + (classTable.getFixedCellSize() * rows);
            classTable.setPrefHeight(Math.max(newH, 120));
            try { if (updatePaginationUI[0] != null) updatePaginationUI[0].run(); } catch (Exception ignore) {}
        };

        // pagination controls
        Button btnPrevPage = new Button("<");
        Button btnNextPage = new Button(">");
        Label lblPage = new Label();
        btnPrevPage.setPrefWidth(48);
        btnNextPage.setPrefWidth(48);
        lblPage.setFont(Fonts.loadMontserratRegular(18));

        // do nothing if max left and right 

        btnPrevPage.setOnAction(ev2 -> {
            if (btnPrevPage.isDisabled()) return;
            if (currentPage[0] > 1) {
                currentPage[0]--;
                updatePage.run();
                classTable.refresh();              
            }
            
        });
        btnNextPage.setOnAction(ev2 -> {
            if (btnNextPage.isDisabled()) return;
            int total = fullResults.size();
            int totalPages = Math.max(1, (int) Math.ceil(total / (double) pageSize));
            if (currentPage[0] < totalPages) {
                currentPage[0]++;
                updatePage.run();
                classTable.refresh();   
            }
        });

        Pane paginationPane = new Pane();
        paginationPane.getStyleClass().add("pagination-pane");
        paginationPane.setPrefHeight(64);
        paginationPane.setPrefWidth(searchArea.getPrefWidth());
        paginationPane.layoutXProperty().bind(searchArea.layoutXProperty());
        paginationPane.layoutYProperty().bind(classTable.layoutYProperty().add(classTable.prefHeightProperty()).add(12));

        HBox paginationBox = new HBox(12);
        paginationBox.getStyleClass().add("pagination-hbox");
        paginationBox.prefWidthProperty().bind(paginationPane.widthProperty());
        paginationBox.setPrefHeight(56);
        // buttons style classes
        btnPrevPage.getStyleClass().add("pagination-btn");
        btnNextPage.getStyleClass().add("pagination-btn");
        lblPage.getStyleClass().add("pagination-label");

        // center horizontally
        paginationBox.layoutXProperty().bind(paginationPane.widthProperty().subtract(paginationBox.widthProperty()).divide(2));
        paginationBox.setLayoutY(8);
        paginationBox.getChildren().addAll(btnPrevPage, lblPage, btnNextPage);
        paginationPane.getChildren().add(paginationBox);
        // initially hidden until results exist
        paginationPane.setVisible(false);
        paginationPane.setManaged(false);

        updatePaginationUI[0] = () -> {
            int total = fullResults.size();
            int totalPages = Math.max(1, (int) Math.ceil(total / (double) pageSize));
            boolean has = total > 0;
            paginationPane.setVisible(has);
            paginationPane.setManaged(has);
            lblPage.setText(currentPage[0] + " / " + totalPages + "   (" + total + " results)");
            btnPrevPage.setDisable(currentPage[0] <= 1);
            btnNextPage.setDisable(currentPage[0] >= totalPages);
        };

        

        // populate table
        java.util.function.BiConsumer<Boolean, String[]> populate = (showAllFlag, params) -> {
            fullResults.clear();
            
            if(showAllFlag == false) {
            	fullResults.clear();
                currentPage[0] = 1;
                updatePage.run();              
                try { if (updatePaginationUI[0] != null) updatePaginationUI[0].run(); } catch (Exception ignore) {}
            	return;
            }
            
            String codeParam = params != null && params.length > 0 ? params[0] : "";
            String sectionParam = params != null && params.length > 1 ? params[1] : "";
            String codeNorm = normalizeCourseCode(codeParam == null ? "" : codeParam).toUpperCase();
            String sectionNorm = sectionParam == null ? "" : sectionParam.trim().toUpperCase();

            // include lectures and pair with their lab sections if available
            for (Lecture l : lecSections) {
                boolean matchesCode = codeNorm.isEmpty() || normalizeCourseCode(l.getCourseCode()).contains(codeNorm);
                if (!matchesCode) continue;

                if (l.getLabSections() == null || l.getLabSections().isEmpty()) {
                    // if no lab
                    if (sectionNorm.isEmpty() || (l.getSection() != null && l.getSection().toUpperCase().contains(sectionNorm))) {
                        fullResults.add(new PairView(l, null));
                    }
                } else {
                    boolean anyAdded = false;
                    for (Laboratory lab : l.getLabSections()) {
                        boolean matchesSection = sectionNorm.isEmpty() || (lab.getSection() != null && lab.getSection().toUpperCase().contains(sectionNorm)) || (l.getSection() != null && l.getSection().toUpperCase().contains(sectionNorm));
                        if (matchesSection) {
                            fullResults.add(new PairView(l, lab));
                            anyAdded = true;
                        }
                    }
                    if (!anyAdded && sectionNorm.isEmpty()) {
                        fullResults.add(new PairView(l, null));
                    }
                }
            }

            
            for (Laboratory lab : labSections) {
                if (lab.getlectureSection() != null) continue;
                boolean matchesCode = codeNorm.isEmpty() || normalizeCourseCode(lab.getCourseCode()).contains(codeNorm);
                if (!matchesCode) continue;
                if (sectionNorm.isEmpty() || (lab.getSection() != null && lab.getSection().toUpperCase().contains(sectionNorm))) {
                    fullResults.add(new PairView(null, lab));
                }
            }

            currentPage[0] = 1;
            updatePage.run();   
            int total = fullResults.size();
            int totalPages = Math.max(1, (int) Math.ceil(total / (double) pageSize));
            lblPage.setText(currentPage[0] + " / " + totalPages + "   (" + total + " results)");
            btnPrevPage.setDisable(currentPage[0] <= 1);
            btnNextPage.setDisable(currentPage[0] >= totalPages);
        };

        // wire search buttons 
        btnShowAll.setOnAction(ev -> {
            populate.accept(true, new String[]{"",""});
        });
        btnSearch.setOnAction(ev -> {
            String code = txtSearchCode.getText() == null ? "" : txtSearchCode.getText().trim();
            String section = txtSearchSection.getText() == null ? "" : txtSearchSection.getText().trim();
            // if both empty, show nothing
            if (code.equals("") && section.equals("")) {
                populate.accept(false, new String[]{"",""});
            } else {
                populate.accept(true, new String[]{code, section});
            }
        });
        

        // Edit classes section
        Label editLabel = new Label("Edit Classes");
        editLabel.setFont(Fonts.loadMontserratRegular(28));
        editLabel.setPrefWidth(calWidth);
        editLabel.setPrefHeight(48);
        editLabel.layoutXProperty().bind(searchArea.layoutXProperty());
        editLabel.layoutYProperty().bind(Bindings.createDoubleBinding(() ->
            classTable.getLayoutY() + classTable.getPrefHeight() + (paginationPane == null ? 0 : paginationPane.getPrefHeight()) + 24,
            classTable.layoutYProperty(), classTable.prefHeightProperty(), paginationPane.prefHeightProperty()));
       editLabel.getStyleClass().add("fixed-header");

        // Table for editing classes with class and action column
        TableView<Course> editTable = new TableView<>();
        editTable.prefWidthProperty().bind(searchArea.prefWidthProperty());
        editTable.setPrefHeight(360);
        // allow cell factories above to reference this table
        try { editTableRef[0] = editTable; } catch (Exception ignore) {}
        editTable.layoutXProperty().bind(searchArea.layoutXProperty());
        editTable.layoutYProperty().bind(Bindings.createDoubleBinding(() ->
            editLabel.getLayoutY() + editLabel.getPrefHeight() + 24,
            editLabel.layoutYProperty(), editLabel.prefHeightProperty()));

        TableColumn<Course, String> classColSmall = new TableColumn<>("Class");
        // class column take remaining width
        classColSmall.prefWidthProperty().bind(editTable.widthProperty().subtract(160));
        classColSmall.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue() == null ? "" : (c.getValue().getCourseCode() + " — " + c.getValue().getCourseTitle() + " (Sec " + c.getValue().getSection() + ")")
        ));
        classColSmall.setCellFactory(col -> new javafx.scene.control.TableCell<Course, String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Course c = getTableView().getItems().get(getIndex());
                    if (c == null) { setText(null); setGraphic(null); return; }

                    HBox card = new HBox();
                    card.setSpacing(12);
                    card.getStyleClass().add("edit-row-card");

                    // Lecture card LGFT
                    VBox leftCard = new VBox();
                    leftCard.setSpacing(6);
                    leftCard.getStyleClass().add("edit-side-card");
                    Label leftHeader = new Label("Lecture");
                    leftHeader.setFont(Fonts.loadMontserratRegular(14));
                    leftHeader.setStyle("-fx-text-fill: #0b2545; -fx-font-weight: bold;");

                    Course lecture = null;
                    Course lab = null;
                    if (c instanceof Lecture) {
                        lecture = c;
                    } else if (c instanceof Laboratory) {
                        lab = c;
                        Laboratory L = (Laboratory) c;
                        lecture = L.getlectureSection();
                    }

                    if (lecture == null && c instanceof Lecture) lecture = c; // safety

                    Label leftTitle = new Label(lecture != null ? lecture.getCourseCode() + " — " + lecture.getCourseTitle() : "-- No corresponding class --");
                    leftTitle.setFont(Fonts.loadMontserratRegular(16));
                    leftTitle.setStyle("-fx-text-fill: #0b2545; -fx-font-weight: bold;");
                    Label leftSub = new Label(lecture != null ? ("Sec " + (lecture.getSection() == null ? "" : lecture.getSection()) + "    " + (lecture.getTimes() == null ? "" : lecture.getTimes())) : "");
                    leftSub.setFont(Fonts.loadMontserratRegular(13));
                    leftSub.setStyle("-fx-text-fill: #2b4865;");
                    Label leftLoc = new Label(lecture != null ? ("Location: " + (lecture.getRooms() == null ? "TBA" : lecture.getRooms())) : "");
                    leftLoc.setFont(Fonts.loadMontserratRegular(12));
                    leftLoc.setStyle("-fx-text-fill: #3b556b;");
                    Label leftDays = createDaysBox(lecture != null ? lecture.getDays() : null);
                    leftCard.getChildren().addAll(leftHeader, leftTitle, leftSub, leftLoc, leftDays);

                    // Lab card RIGHT
                    VBox rightCard = new VBox();
                    rightCard.setSpacing(6);
                    rightCard.getStyleClass().add("edit-side-card");
                    Label rightHeader = new Label("Lab/Recit");
                    rightHeader.setFont(Fonts.loadMontserratRegular(14));
                    rightHeader.setStyle("-fx-text-fill: #0b2545; -fx-font-weight: bold;");

                    if (lab == null) {
                        // prefer the lab the user actually added (if any)
                        if (lecture instanceof Lecture) {
                            Lecture L = (Lecture) lecture;
                            Laboratory mapped = lectureToAddedLab.get(L);
                            if (mapped != null && user.getCourses().contains(mapped)) {
                                lab = mapped;
                            } else {
                                // otherwise prefer any lab from L.getLabSections() that the user has added
                                for (Laboratory candidate : L.getLabSections()) {
                                    if (user.getCourses().contains(candidate)) { lab = candidate; break; }
                                }
                                // fallback to the first lab offering if nothing added
                                if (lab == null && !L.getLabSections().isEmpty()) lab = L.getLabSections().get(0);
                            }
                        }
                    }

                    Label rightTitle = new Label(lab != null ? lab.getCourseCode() + " — " + lab.getCourseTitle() : "-- No corresponding class --");
                    rightTitle.setFont(Fonts.loadMontserratRegular(16));
                    rightTitle.setStyle("-fx-text-fill: #0b2545; -fx-font-weight: bold;");
                    Label rightSub = new Label(lab != null ? ("Sec " + (lab.getSection() == null ? "" : lab.getSection()) + "    " + (lab.getTimes() == null ? "" : lab.getTimes())) : "");
                    rightSub.setFont(Fonts.loadMontserratRegular(13));
                    rightSub.setStyle("-fx-text-fill: #2b4865;");
                    Label rightLoc = new Label(lab != null ? ("Location: " + (lab.getRooms() == null ? "TBA" : lab.getRooms())) : "");
                    rightLoc.setFont(Fonts.loadMontserratRegular(12));
                    rightLoc.setStyle("-fx-text-fill: #3b556b;");
                    Label rightDays = createDaysBox(lab != null ? lab.getDays() : null);
                    rightCard.getChildren().addAll(rightHeader, rightTitle, rightSub, rightLoc, rightDays);

                    // Make the two cards share available width
                    leftCard.setPrefWidth((classColSmall.getWidth() - 24) / 2.0);
                    rightCard.setPrefWidth((classColSmall.getWidth() - 24) / 2.0);

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

                    card.getChildren().addAll(leftCard, spacer, rightCard);
                    setText(null);
                    setGraphic(card);
                }
            }
        });

        // for action column
        TableColumn<Course, Course> editActionCol = new TableColumn<>("Action");
        editActionCol.setPrefWidth(160); // smaller
        editActionCol.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue()));

        editActionCol.setCellFactory(col -> new javafx.scene.control.TableCell<Course, Course>() {

            private final Button btn = new Button("DELETE");

            {
                btn.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #ff827f, #ff4c4c); -fx-border-color: transparent; -fx-text-fill: white; -fx-background-radius: 8;");
                btn.setPrefWidth(120);
                btn.setPrefHeight(36);
            }

            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    btn.setFont(Fonts.loadMontserratRegular(16));

                    btn.setOnAction(ev -> {
                        try {
                            getTableView().getItems().remove(item);
                            calendar.removeCourse(item);
                            editTable.getItems().remove(item);

                            if (item instanceof Laboratory) {
                                Laboratory cLab = (Laboratory) item;
                                Lecture pairedLec = cLab.getlectureSection();

                                if (user.getCourses().contains(cLab)) {
                                    user.getCourses().remove(cLab);
                                    editTable.getItems().removeIf(c -> c instanceof Laboratory && c.equals(cLab));
                                    calendar.removeCourse(cLab);
                                }

                                System.out.print("lab removed");

                            } else if (item instanceof Lecture) {
                                Lecture cLec = (Lecture) item;
                                Laboratory paired = lectureToAddedLab.remove(cLec);

                                for (Laboratory lab : cLec.getLabSections()) {
                                    if (user.getCourses().contains(lab)) {
                                        user.getCourses().remove(lab);
                                        getTableView().getItems().remove(lab);
                                        calendar.removeCourse(lab);
                                        editTable.getItems().removeIf(c -> c instanceof Laboratory && c.equals(lab));
                                    }
                                }


                                user.getCourses().remove(cLec);
                                System.out.print("lec removed");

                            } else {
                                user.getCourses().remove(item);
                                System.out.print("course removed");
                            }

                            classTable.refresh();
                            if (editTableRef[0] != null) editTableRef[0].refresh();

                            showToast(root, "Removed from your schedule", 1800);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    setGraphic(btn);
                }
            }
        }); 


        editTable.getColumns().addAll(classColSmall, editActionCol);
        editTable.setItems(FXCollections.observableArrayList());

        // Placeholder for empty edit table
        Label editPlaceholder = new Label("NO DATA AVAILABLE");
        editPlaceholder.setFont(Fonts.loadComingSoon(30));
        editPlaceholder.setStyle("-fx-text-fill: black;");
        editPlaceholder.setAlignment(javafx.geometry.Pos.CENTER);
        editPlaceholder.prefWidthProperty().bind(editTable.widthProperty());
        editTable.setPlaceholder(editPlaceholder);

        // Add botoom padding to avoid abrtupt end of 
        Pane bottomSpacer = new Pane();
        bottomSpacer.layoutXProperty().bind(searchArea.layoutXProperty());
        bottomSpacer.layoutYProperty().bind(Bindings.createDoubleBinding(() ->
            editTable.getLayoutY() + editTable.getPrefHeight() + 12,
            editTable.layoutYProperty(), editTable.prefHeightProperty()));
        bottomSpacer.setPrefWidth(1);
        bottomSpacer.setPrefHeight(BOTTOM_PADDING); // NOTE TO SELF: ADJUST THIS IF EVER

        // View Catalogue button 
        Button btnViewCatalogue = new Button("VIEW CATALOGUE");
        btnViewCatalogue.setPrefWidth(220);
        btnViewCatalogue.setPrefHeight(48);
        btnViewCatalogue.setFont(Fonts.loadSensaWild(22));
        btnViewCatalogue.getStyleClass().add("btn-create");
        btnViewCatalogue.getStyleClass().add("sidebar-pill");
        btnViewCatalogue.layoutXProperty().bind(searchArea.layoutXProperty());
        btnViewCatalogue.layoutYProperty().bind(Bindings.createDoubleBinding(() ->
            editTable.getLayoutY() + editTable.getPrefHeight() + 18,
            editTable.layoutYProperty(), editTable.prefHeightProperty()));
        btnViewCatalogue.setOnAction(ev -> {
            try {
                System.out.println("test");
            } catch (Exception ex) { ex.printStackTrace(); }
        });
        btnViewCatalogue.setFocusTraversable(false);

        lblSearchCode.layoutXProperty().bind(searchArea.layoutXProperty().add(0));
        lblSearchCode.layoutYProperty().bind(Bindings.createDoubleBinding(() ->
            manageLabel.getLayoutY() + manageLabel.getPrefHeight() + 24,
            manageLabel.layoutYProperty()));

        lblSearchSection.layoutXProperty().bind(searchArea.layoutXProperty().add(searchBoxWidth + searchGap));
        lblSearchSection.layoutYProperty().bind(Bindings.createDoubleBinding(() ->
            manageLabel.getLayoutY() + manageLabel.getPrefHeight() + 24,
            manageLabel.layoutYProperty()));

        content.getChildren().addAll(calendarLabel, calendarPane, manageLabel, lblSearchCode, lblSearchSection, searchArea, classTable, paginationPane, editLabel, editTable, btnViewCatalogue, bottomSpacer);
        // 
        try {
            profileView.layoutXProperty().bind(scene.widthProperty().subtract(profileView.fitWidthProperty()).subtract(10));
            profileView.setLayoutY(10);
            root.getChildren().add(profileView);
            profileView.toFront();
        } catch (Exception ignore) {}

        // add night overlay 
        root.getChildren().add(nightView);
        nightView.toBack();


        primaryStage.show();
    }

    // for formatting well
    private static int parseUnits(String raw) {
        if (raw == null) return 0;
        raw = raw.trim();
        if (raw.isEmpty()) return 0;
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(raw);
        if (m.find()) {
            try {
                return Integer.parseInt(m.group());
            } catch (NumberFormatException nfe) {
                return 0;
            }
        }
        return 0;
    }

    // normalize course code for comparison with specific course dataset and general course dataset
    private static String normalizeCourseCode(String raw) {
        if (raw == null) return "";
        String s = raw.trim().toUpperCase();
        s = s.replaceAll("\\s+", " ");
        return s;
    }
    
    private boolean isLabSection(String text) {
        return text != null && text.contains("-");
    }
    
    private boolean daysOverlap(Set<String> a, Set<String> b) {
        if (a == null || b == null) return false;
        for (String d : a) {
            if (b.contains(d)) return true;
        }
        return false;
    }

    private int parseHour(String raw) {
        if (raw == null) return 0;
        String s = raw.trim();
        if (s.isEmpty()) return 0;

        int colon = s.indexOf(':');
        String hourPart = (colon >= 0) ? s.substring(0, colon) : s;
        hourPart = hourPart.replaceAll("[^0-9]", "");
        if (hourPart.isEmpty()) return 0;

        try {
            int h = Integer.parseInt(hourPart);
            if (h <= 0) return 0;
            if (h > 12) h = h % 12 == 0 ? 12 : h % 12;
            return h;
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private int[] parseHourRange(String raw) {
        if (raw == null) return new int[]{0, 0};
        String[] parts = raw.split("-");
        if (parts.length < 2) {
            int v = parseHour(raw.trim());
            return new int[]{v, v};
        }

        int start = parseHour(parts[0].trim());
        int end = parseHour(parts[1].trim());

        if (start <= 0) start = 0;
        if (end <= 0) end = 0;

        if (end <= start) {
            end += 12;
        }

        if (start >= 4 && start <= 11) {
            if (start <= 12) start += 12;
            if (end <= 12) end += 12;
        }

        return new int[]{start, end};
    }

    // Parse a time string into decimals
    /*private double parseTimeDecimal(String raw) {
        if (raw == null) return 0.0;
        raw = raw.trim();
        if (raw.isEmpty()) return 0.0;

        try {
            String[] parts = raw.split(":");
            int h = 0;
            int m = 0;
            if (parts.length > 0) {
                String hs = parts[0].replaceAll("[^0-9]", "");
                if (!hs.isEmpty()) h = Integer.parseInt(hs);
            }
            if (parts.length > 1) {
                String ms = parts[1].replaceAll("[^0-9]", "");
                if (!ms.isEmpty()) {
                    if (ms.length() > 2) ms = ms.substring(0,2);
                    m = Integer.parseInt(ms);
                }
            }
            double dec = h + (m / 60.0);
            if (dec < 7) dec += 12; 
            return dec;
        } catch (Exception ex) {
            return 0.0;
        }
    }*/

    /*private double[] parseTimeRangeDecimal(String raw) {
        if (raw == null) return new double[]{0.0, 0.0};
        String[] parts = raw.split("-");
        if (parts.length < 2) {
            double v = parseTimeDecimal(raw.trim());
            return new double[]{v, v};
        }
        double start = parseTimeDecimal(parts[0].trim());
        double end = parseTimeDecimal(parts[1].trim());
        if (end <= start) end += 12;
        
        if (start >= 4 && start <= 11) {
            if (start <= 12) start += 12;
            if (end <= 12) end += 12;
        }

        return new double[]{start, end};
    }*/

    private boolean courseChecker(Course selectedCourse, Students user) {
        if (selectedCourse == null || user == null) return false;

        // Exact duplicate (same course code + section)
        for (Course c : user.getCourses()) {
            if (c.getCourseCode().equals(selectedCourse.getCourseCode())
                    && c.getSection().equals(selectedCourse.getSection())) {
                System.out.println("error: duplicate course/section -> " + selectedCourse.getCourseCode() + " " + selectedCourse.getSection());
                return false;
            }
        }

        // Exact same times & days string
        for (Course c : user.getCourses()) {
            if (c.getTimes().equals(selectedCourse.getTimes())
                    && c.getDays().equals(selectedCourse.getDays())) {
                System.out.println("error: exact same time & days -> selected=" + selectedCourse.getCourseCode() + " " + selectedCourse.getTimes() + " " + selectedCourse.getDays()
                    + " existing=" + c.getCourseCode() + " " + c.getTimes() + " " + c.getDays());
                return false;
            }
        }

        double[] newRange = parseTimeRangeDecimal(selectedCourse.getTimes());
        double newStart = newRange[0];
        double newEnd = newRange[1];
        Set<String> newDays = parseDays(selectedCourse.getDays());

        if (newDays.isEmpty() || (newStart == 0 && newEnd == 0)) return true;

        for (Course c : user.getCourses()) {
            Set<String> existDays = parseDays(c.getDays());
            if (!daysOverlap(newDays, existDays)) continue;

            double[] existRange = parseTimeRangeDecimal(c.getTimes());
            double existStart = existRange[0];
            double existEnd = existRange[1];

            if (newStart < existEnd && newEnd > existStart) {
                System.out.println("error: time overlap -> selected=" + selectedCourse.getCourseCode() + " " + selectedCourse.getTimes() + " " + selectedCourse.getDays()
                    + " existing=" + c.getCourseCode() + " " + c.getTimes() + " " + c.getDays());
                return false;
            }
        }

        return true;
    }
    
    // di ko natest to
    private String validateCourseForUser(Course selectedCourse, Students user) {
        return validateCourseForUser(selectedCourse, user, null);
    }

    // Overload that allows passing a collection of courses to ignore during validation
    private String validateCourseForUser(Course selectedCourse, Students user, java.util.Collection<Course> ignore) {
        if (selectedCourse == null || user == null) return "Invalid selection";

        // duplicate course/section 
        for (Course c : user.getCourses()) {
            if (ignore != null && ignore.contains(c)) continue;
            if (Objects.equals(c.getCourseCode(), selectedCourse.getCourseCode())
                    && Objects.equals(c.getSection(), selectedCourse.getSection())) {
                return "Duplicate course/section";
            }
        }

        // Prevent adding another section of the same course code 
        String selNorm = normalizeCourseCode(selectedCourse.getCourseCode()).toUpperCase();
        for (Course c : user.getCourses()) {
            if (ignore != null && ignore.contains(c)) continue;
            String exNorm = normalizeCourseCode(c.getCourseCode()).toUpperCase();
            if (selNorm.isEmpty() || exNorm.isEmpty()) continue;
            if (selNorm.equals(exNorm)) {
                boolean sameSection = Objects.equals(c.getSection(), selectedCourse.getSection());
                if (sameSection) {
                    return "Duplicate course/section";
                }

                // If candidate is a laboratory and existing is its paired Lecture, allow it
                if (selectedCourse instanceof Laboratory && c instanceof Lecture) {
                    Laboratory lab = (Laboratory) selectedCourse;
                    Lecture lec = (Lecture) c;
                    try {
                        if (lab.getlectureSection() != null && lab.getlectureSection().equals(lec)) {
                            continue;
                        }
                    } catch (Exception ignoreEx) {}
                }

                // If candidate is a Lecture and existing is a Laboratory paired to it, allow it
                if (selectedCourse instanceof Lecture && c instanceof Laboratory) {
                    Laboratory lab = (Laboratory) c;
                    Lecture lec = (Lecture) selectedCourse;
                    try {
                        if (lab.getlectureSection() != null && lab.getlectureSection().equals(lec)) {
                            continue;
                        }
                    } catch (Exception ignoreEx) {}
                }

                // else, block adding another section of same course code
                return "Course already added (different section)";
            }
        }

        // exact same times & days string 
        for (Course c : user.getCourses()) {
            if (ignore != null && ignore.contains(c)) continue;
            if (Objects.equals(c.getTimes(), selectedCourse.getTimes())
                    && Objects.equals(c.getDays(), selectedCourse.getDays())) {
                return "Time overlap with " + c.getCourseCode() + " " + c.getSection();
            }
        }

        //  strict check normalize time ranges and day sets and compare for exact match
        String selCanon = canonicalTimeDays(selectedCourse);
        if (selCanon != null && !selCanon.isEmpty()) {
            for (Course c : user.getCourses()) {
                if (ignore != null && ignore.contains(c)) continue;
                String exCanon = canonicalTimeDays(c);
                if (exCanon != null && exCanon.equals(selCanon)) {
                    return "Time overlap with " + c.getCourseCode() + " " + c.getSection();
                }
            }
        }

        // parse new course time and days
        double[] newRange = parseTimeRangeDecimal(selectedCourse.getTimes());
        double newStart = newRange[0];
        double newEnd = newRange[1];
        Set<String> newDays = parseDays(selectedCourse.getDays());

        // if no schedule info, allow
        if (newDays == null || newDays.isEmpty() || (newStart == 0 && newEnd == 0)) return null;

        for (Course c : user.getCourses()) {
            if (ignore != null && ignore.contains(c)) continue;
            Set<String> existDays = parseDays(c.getDays());
            double[] existRange = parseTimeRangeDecimal(c.getTimes());
            double existStart = existRange[0];
            double existEnd = existRange[1];

            if (DEBUG_VALIDATION) {
                System.out.println("[VALIDATION] Checking: Selected=" + (selectedCourse == null ? "null" : (selectedCourse.getCourseCode() + " " + selectedCourse.getSection()))
                    + " times='" + selectedCourse.getTimes() + "' days='" + selectedCourse.getDays() + "' -> range=[" + newStart + "," + newEnd + "] days=" + newDays);
                System.out.println("[VALIDATION]              Against: Existing=" + (c == null ? "null" : (c.getCourseCode() + " " + c.getSection()))
                    + " times='" + c.getTimes() + "' days='" + c.getDays() + "' -> range=[" + existStart + "," + existEnd + "] days=" + existDays);
            }

            if (existDays == null || !daysOverlap(newDays, existDays)) continue;

            boolean overlap = (newStart < existEnd && newEnd > existStart);
            if (overlap) {
                if (DEBUG_VALIDATION) System.out.println("[VALIDATION] => Overlap TRUE for selected " + selectedCourse.getCourseCode() + " and existing " + c.getCourseCode());
                return "Time overlap with " + c.getCourseCode() + " " + c.getSection();
            } else {
                if (DEBUG_VALIDATION) System.out.println("[VALIDATION] => Overlap FALSE for selected " + selectedCourse.getCourseCode() + " and existing " + c.getCourseCode());
            }
        }

        return null;
    }

    /**
     * Parse "H:mm" or "HH:mm" into decimal hours (e.g. "9:30" -> 9.5).
     * Returns 0.0 on parse failure.
     */
    private double parseTimeDecimal(String raw) {
        if (raw == null) return 0.0;
        String s = raw.trim();
        if (s.isEmpty()) return 0.0;

        try {
            // Expect exactly "H:mm" or "HH:mm"
            String[] parts = s.split(":");
            if (parts.length == 0) return 0.0;
            int h = Integer.parseInt(parts[0].replaceAll("\\D", ""));
            int m = 0;
            if (parts.length > 1) {
                String ms = parts[1].replaceAll("\\D", "");
                if (!ms.isEmpty()) {
                    // if user somehow provided more than two digits, take first two
                    if (ms.length() > 2) ms = ms.substring(0, 2);
                    m = Integer.parseInt(ms);
                }
            }
            if (m < 0) m = 0;
            if (m > 59) m = 59;
            // Return decimal hours (24-hour scale expected)
            return h + (m / 60.0);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    // gawing time hour and mins
    private double[] parseTimeRangeDecimal(String raw) {
        if (raw == null) return new double[]{0.0, 0.0};
        String[] parts = raw.split("-");
        if (parts.length < 1) return new double[]{0.0, 0.0};

        double start = parseTimeDecimal(parts[0].trim());
        double end = (parts.length >= 2) ? parseTimeDecimal(parts[1].trim()) : start;

        // if parsing failed for both, return zeroes
        if (start == 0 && end == 0) return new double[]{0.0, 0.0};

        // If end is not later than start, assume it's in the PM next block (add 12)
        if (end <= start) {
            end += 12.0;
        }

        return new double[]{start, end};
    }

    // method for success message
    private void showToast(Pane parent, String message, int durationMs) {
        if (parent == null || message == null) return;
        Label toast = new Label(message);
        String lower = message.toLowerCase();
        // detect explicit error/negative messages first
        boolean isError = lower.contains("not added") || lower.contains("nothing added") || lower.contains("does not exist") || lower.contains("error") || lower.contains("fail") || lower.contains("failed") || lower.contains("overlap") || lower.contains("cannot") || lower.contains("can't") || lower.contains("conflict") || lower.contains("overlaps");
        boolean isSuccess = lower.contains("added") || lower.contains("removed");

        if (isError) {
            toast.getStyleClass().add("toast-error");
            toast.setFont(Fonts.loadMontserratRegular(16));
        } else if (isSuccess) {
            toast.getStyleClass().add("toast-success");
            toast.setFont(Fonts.loadMontserratRegular(18));
        } else {
            toast.getStyleClass().add("toast-default");
            toast.setFont(Fonts.loadMontserratRegular(16));
        }
        toast.setOpacity(0);
        parent.getChildren().add(toast);
        // place on top
        toast.layoutXProperty().bind(parent.widthProperty().subtract(toast.widthProperty()).divide(2));
        toast.setLayoutY(48);

        javafx.animation.FadeTransition fin = new javafx.animation.FadeTransition(Duration.millis(220), toast);
        fin.setFromValue(0.0);
        fin.setToValue(1.0);
        fin.play();

        javafx.animation.PauseTransition wait = new javafx.animation.PauseTransition(Duration.millis(durationMs));
        wait.setOnFinished(ev -> {
            javafx.animation.FadeTransition fout = new javafx.animation.FadeTransition(Duration.millis(360), toast);
            fout.setFromValue(1.0);
            fout.setToValue(0.0);
            fout.setOnFinished(ev2 -> { parent.getChildren().remove(toast); });
            fout.play();
        });
        wait.play();
    }
    
    public Set<String> parseDays(String raw) {
        Set<String> days = new HashSet<>();
        if (raw == null) return days;
        String s = raw.toUpperCase();
        s = s.replaceAll("[,/\\\\-]", " ").trim();
        if (s.isEmpty()) return days;

        String[] tokens = s.split("\\s+");
        for (String tok : tokens) {
            if (tok == null || tok.isEmpty()) continue;
            String t = tok.replaceAll("[^A-Z]", "");
            if (t.isEmpty()) continue;
            
            if(t.contains("TBA")) {
            	days.add("TBA");
            }

            // Handle Thursday first so it's not mistaken for "T"
            if (t.contains("TH")) {
                days.add("Th");
                // If token also contains T separate from TH (e.g., TTH), include T as well
                if (t.contains("T") && !t.equals("TH")) days.add("T");
            }

            if (t.contains("M")) days.add("M");
            if (t.contains("T") && !t.contains("TH") && !t.contains("TBA")) days.add("T");
            if (t.contains("W")) days.add("W");
            if (t.contains("SA")) days.add("Sa");
            if (t.contains("F")) days.add("F");
        }

        return days;
    }    

    // small yellow-styled Label showing days (ordered: M T W Th F Sa)
    private Label createDaysBox(String raw) {
        Label lbl = new Label("");
        if (raw == null) return lbl;
        Set<String> set = parseDays(raw);
        if (set.isEmpty()) return lbl;

        List<String> order = Arrays.asList("M","TBA","T","W","Th","F","Sa");
        List<String> present = new ArrayList<>();
        for (String o : order) if (set.contains(o)) present.add(o);
        String text = String.join(" ", present);

        lbl.setText(text);
        lbl.setFont(Fonts.loadMontserratRegular(12));
        lbl.setStyle("-fx-background-color: linear-gradient(#FFD54F, #ffb91a); -fx-background-radius: 8; -fx-padding: 6 10 6 10; -fx-text-fill: #0b2545; -fx-font-weight: bold;");
        return lbl;
    }
    
    private boolean isCourseAdded(Course course, Students user) {
        if (course == null || user == null) return false;
        for (Course c : user.getCourses()) {
            if (c == null) continue;
            String cc1 = c.getCourseCode() == null ? "" : c.getCourseCode().trim();
            String s1  = c.getSection()    == null ? "" : c.getSection().trim();
            String t1  = c.getCourseTitle()== null ? "" : c.getCourseTitle().trim();
            String cc2 = course.getCourseCode() == null ? "" : course.getCourseCode().trim();
            String s2  = course.getSection()    == null ? "" : course.getSection().trim();
            String t2  = course.getCourseTitle()== null ? "" : course.getCourseTitle().trim();

            // Normalize course codes 
            String ncc1 = normalizeCourseCode(cc1).toUpperCase();
            String ncc2 = normalizeCourseCode(cc2).toUpperCase();

            // For some sections that have same naming and errors
            boolean codesEqual = !ncc1.isEmpty() && !ncc2.isEmpty() ? ncc1.equals(ncc2) : cc1.equalsIgnoreCase(cc2);
            boolean sectionsEqual = s1.equalsIgnoreCase(s2);
            boolean titlesEqualWhenNeeded = !(ncc1.isEmpty() || ncc2.isEmpty()) ? true : t1.equalsIgnoreCase(t2);

            if (codesEqual && sectionsEqual && titlesEqualWhenNeeded) return true;
        }
        return false;
    }
    
    private boolean overlaps(Course a, Course b) {
        if (a == null || b == null) return false;
        Set<String> daysA = parseDays(a.getDays());
        Set<String> daysB = parseDays(b.getDays());
        if (daysA == null || daysB == null) return false;
        boolean daysOverlap = false;
        for (String d : daysA) {
            if (daysB.contains(d)) { daysOverlap = true; break; }
        }
        if (!daysOverlap) return false;

        double[] rA = parseTimeRangeDecimal(a.getTimes());
        double[] rB = parseTimeRangeDecimal(b.getTimes());
        double sA = rA[0], eA = rA[1], sB = rB[0], eB = rB[1];
        if ((sA == 0 && eA == 0) || (sB == 0 && eB == 0)) return false;
        return sA < eB && eA > sB;
    }

    // Create a canonical representation of time range + sorted day tokens for exact comparisons
    private String canonicalTimeDays(Course c) {
        if (c == null) return "";
        String times = c.getTimes();
        String days = c.getDays();
        if (times == null) times = "";
        if (days == null) days = "";
        double[] range = parseTimeRangeDecimal(times);
        double s = range[0], e = range[1];
        Set<String> daySet = parseDays(days);
        if (daySet == null || daySet.isEmpty()) return "";
        List<String> ds = new ArrayList<>(daySet);
        Collections.sort(ds);
        String dayStr = String.join("|", ds);
        // use fixed precision to avoid minor float diffs
        String rangeStr = String.format("%.2f-%.2f", s, e);
        return rangeStr + "@" + dayStr;
    }

    
        
    }
