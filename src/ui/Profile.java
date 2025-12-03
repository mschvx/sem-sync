package ui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import students.Students;
import application.Fonts;

import java.util.ArrayList;
import java.util.List;

public class Profile {


    @SuppressWarnings("unchecked")
    public void showInContent(Pane content, Scene scene, Students user, ScrollPane contentScroll) {
        Object openFlag = content.getProperties().get("profileOpen");
        boolean open = openFlag instanceof Boolean && (Boolean) openFlag;

        if (!open) {
            // save current chidldren of the root so it will still retain when switching to profile
            List<Node> saved = new ArrayList<>(content.getChildren());
            content.getProperties().put("profileSaved", saved);

            Double prevW = content.getPrefWidth() <= 0 ? null : content.getPrefWidth();
            Double prevH = content.getPrefHeight() <= 0 ? null : content.getPrefHeight();
            content.getProperties().put("profilePrevPrefW", prevW);
            content.getProperties().put("profilePrevPrefH", prevH);

            // disable scrollbars while profile is visible
            contentScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            contentScroll.setPannable(false);
            content.getChildren().clear();
            try {
                Image img = new Image(new java.io.File("Elements/ProfileElements.png").toURI().toString());
                ImageView iv = new ImageView(img);
                iv.setLayoutX(0);
                iv.setLayoutY(0);
                iv.setOnMouseClicked(ev -> showInContent(content, scene, user, contentScroll));
                content.getChildren().add(iv);

                // student information text
                Label heading = new Label("STUDENT INFORMATION");
                heading.setFont(Fonts.loadSensaSerif(100));
                heading.setLayoutX(570);
                heading.setLayoutY(20);
                heading.setTextFill(Color.BLACK);
                heading.setStyle("-fx-text-fill: black;");
                heading.setOpacity(1.0);
                content.getChildren().add(heading);
                heading.toFront();

                // Student image
                Image studentImg = new Image(new java.io.File("Elements/Student.png").toURI().toString());
                ImageView studentView = new ImageView(studentImg);
                studentView.setPreserveRatio(true);
                studentView.setFitWidth(200);
                studentView.setLayoutX(360);
                studentView.setLayoutY(395);
                content.getChildren().add(studentView);
                studentView.toFront();

                // Bulb image
                Image bulb1Img = new Image(new java.io.File("Elements/Bulb1.png").toURI().toString());
                Image bulb2Img = new Image(new java.io.File("Elements/Bulb2.png").toURI().toString());

                ImageView bulbView = new ImageView(bulb1Img);
                bulbView.setPreserveRatio(true);
                bulbView.setFitWidth(300);
                bulbView.setLayoutX(1070);
                bulbView.setLayoutY(110);
                bulbView.setRotate(-3);
                content.getChildren().add(bulbView);
                bulbView.toFront();

                // Timeline bulb animation
                Timeline bulbTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), ev -> {
                    try {
                        if (bulbView.getImage() == bulb1Img) {
                            bulbView.setImage(bulb2Img);
                            bulbView.setRotate(3);
                        } else {
                            bulbView.setImage(bulb1Img);
                            bulbView.setRotate(-3);
                        }
                    } catch (Exception ignored) {}
                }));
                bulbTimeline.setCycleCount(Timeline.INDEFINITE);
                bulbTimeline.play();




                // First name 
                Label firstNameLabel = new Label(truncStr(user.getFirstName()));
                firstNameLabel.setFont(application.Fonts.loadMontserratRegular(30));
                firstNameLabel.setLayoutX(740);
                firstNameLabel.setLayoutY(300);
                firstNameLabel.setTextFill(Color.BLACK);
                firstNameLabel.setStyle("-fx-text-fill: black;");
                firstNameLabel.setOpacity(1.0);
                content.getChildren().add(firstNameLabel);
                firstNameLabel.toFront();

                // Last name 
                Label lastNameLabel = new Label(truncStr(user.getLastName()));
                lastNameLabel.setFont(application.Fonts.loadMontserratRegular(30));
                lastNameLabel.setLayoutX(740);
                lastNameLabel.setLayoutY(420);
                lastNameLabel.setTextFill(Color.BLACK);
                lastNameLabel.setStyle("-fx-text-fill: black;");
                lastNameLabel.setOpacity(1.0);
                content.getChildren().add(lastNameLabel);
                lastNameLabel.toFront();

                // Username
                Label usernameLabel = new Label(truncStr(user.getUsername()));
                usernameLabel.setFont(application.Fonts.loadMontserratRegular(30));
                usernameLabel.setLayoutX(740);
                usernameLabel.setLayoutY(540);
                usernameLabel.setTextFill(Color.BLACK);
                usernameLabel.setStyle("-fx-text-fill: black;");
                usernameLabel.setOpacity(1.0);
                content.getChildren().add(usernameLabel);
                usernameLabel.toFront();

                // Degree Program: determine readable string and acronym
                String degreeReadable = "";
                String degreeAcr = "";
                if (user.getDegreeProgram() != null) {
                    degreeAcr = user.getDegreeProgram().getCourseCode();
                    switch (degreeAcr) {
                        case "BSCS": degreeReadable = "BS Computer Science"; break;
                        case "MSCS": degreeReadable = "MS Computer Science"; break;
                        case "MIT": degreeReadable = "Master of Information Technology"; break;
                        case "PHD": degreeReadable = "PhD Computer Science"; break;
                        default: degreeReadable = degreeAcr; break;
                    }
                } else {
                    String raw = user.getDegree();
                    if (raw == null) raw = "";
                    degreeAcr = raw;
                    switch (raw) {
                        case "BachelorCS": degreeReadable = "BS Computer Science"; degreeAcr = "BSCS"; break;
                        case "MasterCS": degreeReadable = "MS Computer Science"; degreeAcr = "MSCS"; break;
                        case "MasterIT": degreeReadable = "Master of Information Technology"; degreeAcr = "MIT"; break;
                        case "PHDCS": degreeReadable = "PhD Computer Science"; degreeAcr = "PHD"; break;
                        default: degreeReadable = raw; break;
                    }
                }

                if ("Master of Information Technology".equals(degreeReadable)) {
                    Label degLine1 = new Label("Master of Information");
                    degLine1.setFont(application.Fonts.loadMontserratRegular(30));
                    degLine1.setLayoutX(740);
                    degLine1.setLayoutY(670);
                    degLine1.setTextFill(Color.BLACK);
                    degLine1.setStyle("-fx-text-fill: black;");
                    degLine1.setOpacity(1.0);
                    content.getChildren().add(degLine1);
                    degLine1.toFront();

                    Label degLine2 = new Label("Technology");
                    degLine2.setFont(application.Fonts.loadMontserratRegular(30));
                    degLine2.setLayoutX(740);
                    degLine2.setLayoutY(710);
                    degLine2.setTextFill(Color.BLACK);
                    degLine2.setStyle("-fx-text-fill: black;");
                    degLine2.setOpacity(1.0);
                    content.getChildren().add(degLine2);
                    degLine2.toFront();
                } else {
                    Label degreeLabel = new Label(degreeReadable);
                    degreeLabel.setFont(application.Fonts.loadMontserratRegular(30));
                    degreeLabel.setLayoutX(740);
                    degreeLabel.setLayoutY(670);
                    degreeLabel.setTextFill(Color.BLACK);
                    degreeLabel.setStyle("-fx-text-fill: black;");
                    degreeLabel.setOpacity(1.0);
                    content.getChildren().add(degreeLabel);
                    degreeLabel.toFront();
                }

                // introductions
                String article = "a";
                if (degreeAcr != null && !degreeAcr.isEmpty()) {
                    char first = Character.toUpperCase(degreeAcr.charAt(0));
                    if ("M".indexOf(first) >= 0) article = "an";
                }

                Label intro1 = new Label("Hi I'm " + article);
                intro1.setFont(application.Fonts.loadMontserratRegular(40));
                intro1.setLayoutX(380);
                intro1.setLayoutY(590);
                intro1.setTextFill(Color.BLACK);
                intro1.setAlignment(Pos.CENTER);
                intro1.setStyle("-fx-text-fill: black;");
                content.getChildren().add(intro1);
                intro1.toFront();

                Label intro2 = new Label(degreeAcr);
                intro2.setFont(application.Fonts.loadMontserratBold(40));
                intro2.setLayoutX(380);
                intro2.setLayoutY(640);
                intro2.setAlignment(Pos.CENTER);
                intro2.setTextFill(Color.BLACK);
                intro2.setStyle("-fx-text-fill: black;");
                content.getChildren().add(intro2);
                intro2.toFront();

                Label intro3 = new Label("student!");
                intro3.setFont(application.Fonts.loadMontserratRegular(40));
                intro3.setLayoutX(380);
                intro3.setLayoutY(690);
                intro3.setAlignment(Pos.CENTER);
                intro3.setTextFill(Color.BLACK);
                intro3.setStyle("-fx-text-fill: black;");
                content.getChildren().add(intro3);
                intro3.toFront();
            } catch (Exception ex) {
            }

            content.getProperties().put("profileOpen", true);
        } else {
            // restore after
            Object savedObj = content.getProperties().get("profileSaved");
            if (savedObj instanceof List) {
                List<Node> saved = (List<Node>) savedObj;
                content.getChildren().setAll(saved);
            }
            contentScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            contentScroll.setPannable(true);

            // restore previous pref sizes if available
            Object prevW = content.getProperties().get("profilePrevPrefW");
            Object prevH = content.getProperties().get("profilePrevPrefH");
            if (prevW instanceof Double) content.setPrefWidth((Double) prevW); else content.setPrefWidth(Region.USE_COMPUTED_SIZE);
            if (prevH instanceof Double) content.setPrefHeight((Double) prevH); else content.setPrefHeight(Region.USE_COMPUTED_SIZE);

            content.getProperties().remove("profileSaved");
            content.getProperties().remove("profilePrevPrefW");
            content.getProperties().remove("profilePrevPrefH");
            content.getProperties().put("profileOpen", false);
        }
    }

    // truncate long strings with ...
    private static String truncStr(String s) {
        if (s == null) return "";
        if (s.length() > 20) return s.substring(0, 20) + "...";
        return s;
    }
}
