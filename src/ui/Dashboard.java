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
import javafx.scene.control.ListCell;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import users.DegreeLookup;
import users.DegreeProgram;
import users.User;
import users.CurriculumLoader;
import users.Curriculum;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.text.Font;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;

public class Dashboard {

    // Show dashboard for a specific user (so we can display degree greeting)
    public void showDashboard(Stage primaryStage, User user) {
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

        Button btnTutorial = new Button("PROFILE");
        btnTutorial.setLayoutX(buttonXPos);
        btnTutorial.setLayoutY(200);
        btnTutorial.setPrefWidth(sidebarWidth - 80);
        btnTutorial.setPrefHeight(48);
        btnTutorial.setFont(Fonts.loadSensaWild(22));
        btnTutorial.getStyleClass().add("btn-register");
        btnTutorial.getStyleClass().add("sidebar-pill");
        // actual action attached after `content` is created so it can call Profile.showInContent
        btnTutorial.setOnAction(ev -> { /* attached after content creation */ });

        Button btnCatalogue = new Button("DASHBOARD");
        btnCatalogue.setLayoutX(buttonXPos);
        btnCatalogue.setLayoutY(260);
        btnCatalogue.setPrefWidth(sidebarWidth - 80);
        btnCatalogue.setPrefHeight(48);
        btnCatalogue.setFont(Fonts.loadSensaWild(22));
        btnCatalogue.getStyleClass().add("btn-create");
        btnCatalogue.getStyleClass().add("sidebar-pill");
        // Action will be attached later once `content` is initialized.
        btnCatalogue.setOnAction(ev -> { /* attached after content creation */ });

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

        // Anger overlay image 
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
        sidebarContent.getChildren().addAll(sideTitle, btnAbout, btnTutorial, btnCatalogue, eyeView, eyelidView, angerView, btnCredits, btnReferences, btnLogout);

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

        // a
        try {
            btnCatalogue.setOnAction(ev -> {
                try {
                    Object openFlag = content.getProperties().get("profileOpen");
                    boolean open = openFlag instanceof Boolean && (Boolean) openFlag;
                    if (open) {
                        new Profile().showInContent(content, scene, user, contentScroll);
                    }
                } catch (Exception ex) {
                }
            });
        } catch (Exception ignored) {}

        // attach Profile button action (previously Tutorial) now that content exists
        try {
            btnTutorial.setOnAction(ev -> {
                try {
                    new Profile().showInContent(content, scene, user, contentScroll);
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
        profileView.layoutXProperty().bind(content.widthProperty().subtract(profileView.fitWidthProperty()).subtract(10));
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

        profileView.setOnMouseEntered(ev -> profileTimeline.play());
        profileView.setOnMouseExited(ev -> {
            profileTimeline.stop();
            profileView.setImage(profile1);
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
        greetLabel.setLayoutX(220);
        greetLabel.setLayoutY(60);
        greetLabel.setStyle("-fx-text-fill: black;");
        content.getChildren().add(greetLabel);

        // -- Dropdown --
        ComboBox<Course> dropdown = new ComboBox<>();
        dropdown.setPromptText("Select course:");
        dropdown.setLayoutX(220);
        dropdown.setLayoutY(180);
        dropdown.setPrefWidth(400);
        dropdown.setStyle("-fx-font-size: 18px;"); 
        
        dropdown.setCellFactory(listView -> new ListCell<Course>() {
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getCourseCode() + " - " + item.getCourseTitle() + " - Units: " + item.getUnits() + " - Section: " + item.getSection() + " - " + item.getTimes() + " - " + item.getDays());

            }
        });

        dropdown.setButtonCell(new ListCell<Course>() {
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getCourseCode() + " - " + item.getCourseTitle() + " - Units: " + item.getUnits() + " - Section: " + item.getSection() + " - " + item.getTimes() + " - " + item.getDays());
            }
        });
        
        // -- Buttons --
        Button btnAdd = new Button("Add");
        btnAdd.setLayoutX(220);
        btnAdd.setLayoutY(260);
        btnAdd.setPrefWidth(120);
        btnAdd.setFont(Fonts.loadSensaWild(24));

        Button btnDelete = new Button("Delete");
        btnDelete.setLayoutX(360);
        btnDelete.setLayoutY(260);
        btnDelete.setPrefWidth(120);
        btnDelete.setFont(Fonts.loadSensaWild(24));

        // calendar
        double calX = 220;
        double calY = 330;
        double calWidth = 1200;
        double calHeight = 560; 

        Calendar calendar = new Calendar(calWidth, calHeight); // call the calendar from the calendar package and make a new calendar
        Pane calendarPane = calendar.getView();
        calendarPane.setLayoutX(calX);
        calendarPane.setLayoutY(calY);

        // -- List of items added by user --
        Label itemsLabel = new Label("Your Items");
        itemsLabel.setFont(Fonts.loadMontserratRegular(28));
        itemsLabel.setLayoutX(220);
        itemsLabel.setLayoutY(calY + calHeight + 24);
        itemsLabel.setStyle("-fx-text-fill: black;");

        ObservableList<Course> listItems = FXCollections.observableArrayList();
        ListView<Course> listView = new ListView<>(listItems);
        listView.setLayoutX(220);
        listView.setLayoutY(calY + calHeight + 90);
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
                        
                    	for(Lecture l : lecSections) {               		
                    		if(lec.equals(l.getSection())) {
                    			l.addLabSection(lab);
                    			lab.setlectureSection(l);
                    		}
                    	}

                    }   
                }
            }
        } catch (IOException error) {
            System.out.print("Error reading course_offerings.csv");
        }
        
        dropdown.getItems().addAll(list);             
        
        btnAdd.setOnMouseClicked(e -> {
            Course selectedCourse = dropdown.getValue();

            if (selectedCourse == null) {
                return;
            }

            boolean exists = dropdown.getItems().stream()
                .anyMatch(c -> c.getCourseCode().equals(selectedCourse.getCourseCode())
                            && c.getSection().equals(selectedCourse.getSection()));

            if (!exists) {
                return;
            }

            if (selectedCourse instanceof Lecture) {
                Course copyselectedCourse = (Course) selectedCourse;
                boolean ok = courseChecker(copyselectedCourse, user);
         
                if (!ok) return;

                listItems.add(selectedCourse);
                user.addCourse(selectedCourse);
                calendar.addCourse(selectedCourse, listItems);

            } else if (selectedCourse instanceof Laboratory) {
                Laboratory lab = (Laboratory) selectedCourse;
                Course copyselectedCourse = (Course) selectedCourse;

                Lecture parent = lab.getlectureSection();
                Course copyparent = (Course) parent;

                boolean labInListItems = listItems.stream()
                    .anyMatch(c -> c.getCourseCode().equals(lab.getCourseCode()) && c.getSection().equals(lab.getSection()));
                boolean labInUser = user.getCourses().stream()
                    .anyMatch(c -> c.getCourseCode().equals(lab.getCourseCode()) && c.getSection().equals(lab.getSection()));

                boolean ok = courseChecker(copyselectedCourse, user);
              
                if (!ok) return;

                if (!labInListItems) listItems.add(lab);
                if (!labInUser) user.addCourse(lab);
                calendar.addCourse(lab, listItems);

                boolean labAdded = true;

                if (parent != null && labAdded) {
                    boolean parentInListItems = listItems.stream()
                        .anyMatch(c -> c.getCourseCode().equals(parent.getCourseCode()) && c.getSection().equals(parent.getSection()));
                    boolean parentInUser = user.getCourses().stream()
                        .anyMatch(c -> c.getCourseCode().equals(parent.getCourseCode()) && c.getSection().equals(parent.getSection()));

                    boolean okParent = courseChecker(copyparent, user);
                    if (!okParent) return;

                    if (!parentInListItems) listItems.add(parent);
                    if (!parentInUser) user.addCourse(parent);
                    calendar.addCourse(parent, listItems);
                } else {
                    return;
                }

            } else if (selectedCourse instanceof Course) {
                Course copyselectedCourse = (Course) selectedCourse;
                boolean ok = courseChecker(copyselectedCourse, user);
                if (!ok) return;

                listItems.add(selectedCourse);
                user.addCourse(selectedCourse);
                calendar.addCourse(selectedCourse, listItems);
            }
        });

        
        btnDelete.setOnMouseClicked( e-> {
        	Course selected = listView.getSelectionModel().getSelectedItem();
        	
        	if(selected == null) {
        		System.out.println("error"); //Placeholder error
        		return;
        	}
        	
        	if(selected instanceof Laboratory) {
        		Laboratory selectedL = (Laboratory) selected;
        		Lecture lec = selectedL.getlectureSection();
        		
        		listItems.remove(selectedL);
        		listItems.remove(lec);
        		user.getCourses().remove(selectedL);
        		user.getCourses().remove(lec);
        		
        		calendar.removeCourse(selected);
        		calendar.removeCourse(lec);    
        		
        		return;
        		
        	} else if(selected instanceof Lecture) {
        		Lecture selectedLec = (Lecture) selected;
        		
        		for(Laboratory l: selectedLec.getLabSections()) {
        		    boolean existsInUser = user.getCourses().stream().anyMatch(c ->
        		                c.getCourseCode().equals(l.getCourseCode()) &&
        		                c.getSection().equals(l.getSection())
        		            );
        		    
        		    if(existsInUser) {
        		    		listItems.remove(l);
        		    		user.getCourses().remove(l);
        		    		calendar.removeCourse(l);
        		    }
        		}
        	}
        	
        	listItems.remove(selected);
        	user.getCourses().remove(selected);
        calendar.removeCourse(selected);    
        });
        
        // Add to pane
        Pane bottomSpacer = new Pane();
        bottomSpacer.setLayoutX(220);
        bottomSpacer.setLayoutY(listView.getLayoutY() + listView.getPrefHeight() + 10);
        bottomSpacer.setPrefWidth(1);
        bottomSpacer.setPrefHeight(400);

        // catalogue button
        Button btnCatalogueMain = new Button("CATALOGUE");
        btnCatalogueMain.setLayoutX(220);
        btnCatalogueMain.setLayoutY(listView.getLayoutY() + listView.getPrefHeight() + 14);
        btnCatalogueMain.setPrefWidth(220);
        btnCatalogueMain.setPrefHeight(48);
        btnCatalogueMain.setFont(Fonts.loadSensaWild(22));
        btnCatalogueMain.getStyleClass().add("btn-create");
        btnCatalogueMain.getStyleClass().add("sidebar-pill");
        btnCatalogueMain.setOnAction(ev -> {
            System.out.println("WALA PAAAAAA <WOIJOIDJASOIDJ");

        });

        content.getChildren().addAll(dropdown, btnAdd, btnDelete, calendarPane, itemsLabel, listView, btnCatalogueMain, bottomSpacer);
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

    private boolean courseChecker(Course selectedCourse, User user) {
        if (selectedCourse == null || user == null) return false;

        // Exact duplicate (same course code + section)
        for (Course c : user.getCourses()) {
            if (c.getCourseCode().equals(selectedCourse.getCourseCode())
                    && c.getSection().equals(selectedCourse.getSection())) {
                System.out.println("error: duplicate course/section");
                return false;
            }
        }

        // Exact same times & days string
        for (Course c : user.getCourses()) {
            if (c.getTimes().equals(selectedCourse.getTimes())
                    && c.getDays().equals(selectedCourse.getDays())) {
                System.out.println("error: exact same time & days");
                return false;
            }
        }

        int[] newRange = parseHourRange(selectedCourse.getTimes());
        int newStart = newRange[0];
        int newEnd = newRange[1];
        Set<String> newDays = parseDays(selectedCourse.getDays());

        if (newDays.isEmpty() || (newStart == 0 && newEnd == 0)) return true;

        for (Course c : user.getCourses()) {
            Set<String> existDays = parseDays(c.getDays());
            if (!daysOverlap(newDays, existDays)) continue;

            int[] existRange = parseHourRange(c.getTimes());
            int existStart = existRange[0];
            int existEnd = existRange[1];

            if (newStart < existEnd && newEnd > existStart) {
                System.out.println("error: time overlap with " + c.getCourseCode() + " " + c.getTimes());
                return false;
            }
        }

        return true;
    }
    
    public Set<String> parseDays(String raw) {
        Set<String> days = new HashSet<>();
        if (raw == null) return days;
        String s = raw.toUpperCase();
        s = s.replaceAll("[,/\\\\-]", " ").trim();
        if (s.isEmpty()) return days;

        String[] tokens = s.split("\\s+");

        // clean thursday
        for (String tok : tokens) {
            String t = tok.replaceAll("[^A-Z]", "");
            if (t.equals("TH")) days.add("Th");
        }

        // clean saturday
        for (String tok : tokens) {
            String t = tok.replaceAll("[^A-Z]", "");
            if (t.equals("SA")) days.add("Sa");
        }

        // the rest of the weeks that are not double letters
        for (String tok : tokens) {
            String t = tok.replaceAll("[^A-Z]", "");
            if (t.length() == 1) {
                switch (t.charAt(0)) {
                    case 'M': days.add("M"); break;
                    case 'T': days.add("T"); break;
                    case 'W': days.add("W"); break;
                    case 'F': days.add("F"); break;
                }
            }
        }

        return days;
    }    

    
        
    }
