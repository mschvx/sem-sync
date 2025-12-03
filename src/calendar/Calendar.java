// CALENDAR LOGIC
package calendar;

import application.Fonts;
import courses.Course;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.Cursor;
import javafx.scene.effect.BlurType;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calendar {

    private final Pane root;

    // width is passed to constructor but not stored (cellWidth uses it via constructor calculation)
    private final double height;
    private final double timeColWidth = 110;

    private final int dayCount = 6;

    private final int rows = 12;

    private final double headerHeight = 56;

    private final double cellWidth;

    private final double rowHeight;
    
    // Record of the courses added to the calendar
    private ObservableList<Course> calendarCourses = FXCollections.<Course>observableArrayList();

    // Keeps track of rendered course blocks so they can be removed later
    private final Map<Course, List<Node>> blocks = new HashMap<>();

    // Initialize calendar and build its sizes
    public Calendar(double width, double height) {
        this.height = height;
        this.root = new Pane();
        this.root.setPrefWidth(width);
        this.root.setPrefHeight(height);

        this.cellWidth = (width - timeColWidth) / dayCount;
        this.rowHeight = (height - headerHeight) / rows;

        buildGrid();
    }

    // time column, day headers, and hourly grid lines
    private void buildGrid() {
        // Draw the  time column on the left
        Rectangle timeCol = new Rectangle(0, 0, timeColWidth, height);
        timeCol.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#439fd0")), new Stop(1, Color.web("#318fb8"))));
        root.getChildren().add(timeCol);

        // Add day header rectangles and labels
        String[] dayNames = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        for (int d = 0; d < dayCount; d++) {
            double x = timeColWidth + d * cellWidth;

                Rectangle r = new Rectangle(x, 0, cellWidth, headerHeight);
                r.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#FFD54F")), new Stop(1, Color.web("#ffb91a"))));
                r.setStroke(Color.web("#318fb8"));
            r.setArcWidth(8);
            r.setArcHeight(8);
            root.getChildren().add(r);

            Label dayLabel = new Label(dayNames[d]);
            dayLabel.setTextFill(Color.web("#000000"));
            javafx.scene.text.Font dayFont = Fonts.loadSensaWild(22);
            dayLabel.setFont(dayFont);
            String dayFamily = dayFont != null ? dayFont.getFamily() : "Sensa Wild";
            dayLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: '" + dayFamily + "';");
            dayLabel.setLayoutX(x + 12);
            dayLabel.setLayoutY(10);
            root.getChildren().add(dayLabel);
        }

        // shadow for table
        double tableX = timeColWidth;
        double tableWidth = cellWidth * dayCount;
        double tableHeight = height;
        Rectangle tableBg = new Rectangle(tableX, 0, tableWidth, tableHeight);
        tableBg.setFill(Color.TRANSPARENT);
        tableBg.setStroke(Color.TRANSPARENT);
        DropShadow tableDrop = new DropShadow();
        tableDrop.setBlurType(BlurType.GAUSSIAN);
        tableDrop.setRadius(18);
        tableDrop.setOffsetY(4);
        tableDrop.setColor(Color.color(0,0,0,0.28));
        tableBg.setEffect(tableDrop);
        root.getChildren().add(0, tableBg);

        // dotted lines of table
        double yStart = headerHeight;
        for (int rr = 0; rr <= rows; rr++) {
            double y = headerHeight + rr * rowHeight;
            Line h = new Line(timeColWidth, y, timeColWidth + cellWidth * dayCount, y);
            h.setStroke(Color.web("#318fb8"));
            h.setStrokeWidth(1.0);
            h.getStrokeDashArray().addAll(4.0, 6.0);
            root.getChildren().add(h);
        }

        for (int c = 0; c <= dayCount; c++) {
            double x = timeColWidth + c * cellWidth;
            Line v = new Line(x, yStart, x, height);
            v.setStroke(Color.web("#318fb8"));
            v.setStrokeWidth(1.0);
            v.getStrokeDashArray().addAll(4.0, 6.0);
            root.getChildren().add(v);
        }

        // Draw time labels for each hour row
        for (int r = 0; r < rows; r++) {
            int hourStart = 7 + r;
            String labelText = hourStart + "-" + (hourStart + 1);

            Label tlabel = new Label(labelText);
            tlabel.setTextFill(Color.web("#FFFFFF"));
            javafx.scene.text.Font timeFont = Fonts.loadSensaWild(26);
            tlabel.setFont(timeFont);
            String timeFamily = timeFont != null ? timeFont.getFamily() : "Sensa Wild";
            tlabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 26px; -fx-font-weight: bold; -fx-font-family: '" + timeFamily + "';");
            tlabel.setLayoutX(8);
            tlabel.setLayoutY(headerHeight + r * rowHeight + (rowHeight / 2.0) - 12);
            root.getChildren().add(tlabel);
        }
    }

    // Returns the fully constructed calendar pane to be displayed on screen
    public Pane getView() {
        return root;
    }

    // Adds a block representing a course to the calendar
    public void addCourse(Course selectedCourse, ObservableList<Course> list) {
        if (selectedCourse == null) return;
        
        
        // Checking if there is a duplicate course inside the calendar
        boolean exists = calendarCourses.stream()
                .anyMatch(c -> c.getCourseCode().equals(selectedCourse.getCourseCode()) &&
                               c.getSection().equals(selectedCourse.getSection()));
        
        if(exists) {
        		return;
        }

        String times = selectedCourse.getTimes();
        String daysStr = selectedCourse.getDays();

        // Ignore courses without proper schedule information
        if (times == null || daysStr == null) return;
        if (times.equalsIgnoreCase("TBA") || daysStr.equalsIgnoreCase("TBA")) return;

        List<Node> created = new ArrayList<>();

        // Only process the first time range and trim the data
        String timeSeg = times.split(",")[0].trim();
        String[] hs = timeSeg.split("-");

        int[] startParts = parseTime(hs.length > 0 ? hs[0].trim() : ""); 
        int[] endParts = parseTime(hs.length > 1 ? hs[1].trim() : "");


        double startDecimal = startParts[0] + (startParts[1] / 60.0);
        double endDecimal = endParts[0] + (endParts[1] / 60.0);

        if (startDecimal < 7) startDecimal += 12;
        if (endDecimal <= startDecimal) endDecimal += 12;

        int gridStart = 7;
        double y = headerHeight + Math.max(0, (startDecimal - gridStart) * rowHeight);
        double duration = endDecimal - startDecimal;
        int hoursCeil = Math.max(1, (int) Math.ceil(duration));
        double heightPx = Math.max(rowHeight * hoursCeil, rowHeight);

        // Determine which days the course applies to
        boolean[] dayFlags = parseDays(daysStr);

        // Create a block for each day where the course meets
        for (int d = 0; d < dayCount; d++) {
            if (!dayFlags[d]) continue;

            double x = timeColWidth + d * cellWidth;

            StackPane block = new StackPane();
            double blockWidth = cellWidth - 12;
            double blockHeight = Math.max(28, heightPx - 8);
            block.setPrefWidth(blockWidth);
            block.setPrefHeight(blockHeight);

                Rectangle baseRect = new Rectangle(blockWidth, Math.max(24, blockHeight));
                baseRect.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#439fd0")), new Stop(1, Color.web("#318fb8"))));
            baseRect.setArcWidth(12);
            baseRect.setArcHeight(12);

            DropShadow drop = new DropShadow();
            drop.setBlurType(BlurType.GAUSSIAN);
            drop.setRadius(8);
            drop.setSpread(0.14);
            drop.setOffsetY(2);
            drop.setColor(Color.color(0,0,0,0.28));
            baseRect.setEffect(drop);

            Node bgNode = baseRect;

            // custom block shape for the nag-iisang 1hr and 30 minute class 
            int endMinute = endParts[1];
            if (endMinute == 30) {
                // if 1.5 hours
                if (duration >= 1.5) {
                    try {
                        double baseH = baseRect.getHeight();
                        double triTopY = baseH - rowHeight; // top edge of second hour
                        double triBottomY = baseH;

                        // make triangle that will cut through the existing block
                        Polygon tri = new Polygon();
                        tri.getPoints().addAll(
                            blockWidth, triTopY,
                            blockWidth, triBottomY,
                            0.0, triBottomY
                        );

                        
                        Shape cut = Shape.subtract(baseRect, tri); // subtract the rectangle to the right traignel
                        cut.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.web("#439fd0")), new Stop(1, Color.web("#318fb8"))));
                        cut.setStroke(Color.web("#ffb91a"));
                        cut.setStrokeWidth(2);
                        cut.setEffect(drop);
                        bgNode = cut;
                    } catch (Exception ex) {
                        bgNode = baseRect;
                    }
                } else {
                    // sometimes it errors a smaller diagonal with extra borders (based on screen size)
                    try {
                        double triSize = Math.min(18, blockHeight / 2.0);
                        Polygon triSmall = new Polygon();
                        triSmall.getPoints().addAll(
                            blockWidth, blockHeight,
                            blockWidth - triSize, blockHeight,
                            blockWidth, blockHeight - triSize
                        );

                        Shape cutSmall = Shape.subtract(baseRect, triSmall);
                        cutSmall.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.web("#439fd0")), new Stop(1, Color.web("#318fb8"))));
                        cutSmall.setStroke(Color.web("#ffb91a"));
                        cutSmall.setStrokeWidth(2);
                        cutSmall.setEffect(drop);
                        bgNode = cutSmall;
                    } catch (Exception ex) {
                        bgNode = baseRect;
                    }
                }
            }

            // Course text
            Label info = new Label(
                selectedCourse.getCourseCode() + " " + selectedCourse.getSection() +
                "\n" + selectedCourse.getRooms()
            );
            info.setTextFill(Color.web("#FFFFFF"));
            javafx.scene.text.Font infoFont = Fonts.loadMontserratRegular(16);
            info.setFont(infoFont);
            String infoFamily = infoFont != null ? infoFont.getFamily() : "Montserrat";
            info.getStyleClass().add("calendar-block-label");
            info.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: '" + infoFamily + "';");
            info.setWrapText(true);
            info.setMaxWidth(cellWidth - 28);

            // for border handling of the 1hr and 30 minute block
            if (bgNode == baseRect) {
                baseRect.setStroke(Color.web("#ffb91a"));
                baseRect.setStrokeWidth(2);
            }

            block.getChildren().addAll(bgNode, info);
            // sched blocks to pop out when hivered
            ScaleTransition stEnter = new ScaleTransition(Duration.millis(120), block);
            stEnter.setToX(1.06);
            stEnter.setToY(1.06);
            ScaleTransition stExit = new ScaleTransition(Duration.millis(120), block);
            stExit.setToX(1.0);
            stExit.setToY(1.0);

            // add shadow make pretty
            DropShadow hoverShadow = new DropShadow();
            hoverShadow.setBlurType(BlurType.GAUSSIAN);
            hoverShadow.setRadius(12);
            hoverShadow.setSpread(0.16);
            hoverShadow.setOffsetY(4);
            hoverShadow.setColor(Color.color(0,0,0,0.36));

            block.setOnMouseEntered(ev -> {
                stExit.stop();
                stEnter.playFromStart();
                block.setEffect(hoverShadow);
                block.setCursor(Cursor.HAND);
                block.toFront();
            });
            block.setOnMouseExited(ev -> {
                stEnter.stop();
                stExit.playFromStart();
                block.setEffect(null);
            });
            block.setLayoutX(x + 6);
            block.setLayoutY(y + 6);

            root.getChildren().add(block);
            created.add(block);
        }

	        if (!created.isEmpty()) {
	    		blocks.put(selectedCourse, created);
	    		calendarCourses.add(selectedCourse);
	    }
    }

    // Removes all course blocks associated with the given course
    public void removeCourse(Course c) {
        if (c == null) return;
        List<Node> nodes = blocks.remove(c);
        if (nodes == null) return;
        for (Node n : nodes) root.getChildren().remove(n);
        
        calendarCourses.removeIf(cc ->
            cc.getCourseCode().equals(c.getCourseCode()) &&
            cc.getSection().equals(c.getSection())
        );
    }

    // Attempts to extract a valid integer hour from a time string
    private static int parseHour(String raw) {
        if (raw == null || raw.isEmpty()) return 0;
        raw = raw.trim();

        try {
            String[] parts = raw.split(":");
            int h = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
            return h;
        } catch (Exception ex) {
            Matcher m = Pattern.compile("\\d+").matcher(raw);
            if (m.find()) {
                try { return Integer.parseInt(m.group()); } catch (Exception ignored) {}
            }
        }
        return 0;
    }

    // clean time string into hour and minute parts
    private static int[] parseTime(String raw) {
        if (raw == null || raw.isEmpty()) return new int[]{0,0};
        raw = raw.trim();

        try {
            String[] parts = raw.split(":");
            String hStr = parts[0].replaceAll("[^0-9]", "");
            int h = hStr.isEmpty() ? 0 : Integer.parseInt(hStr);
            int m = 0;
            if (parts.length > 1) {
                String minPart = parts[1].replaceAll("[^0-9]", "");
                if (!minPart.isEmpty()) {
                    if (minPart.length() > 2) minPart = minPart.substring(0, 2);
                    m = Integer.parseInt(minPart);
                }
            }
            return new int[]{h, m};
        } catch (Exception ex) {
            Matcher mm = Pattern.compile("(\\d{1,2})(?:[:.](\\d{1,2}))?").matcher(raw);
            if (mm.find()) {
                try {
                    int hh = Integer.parseInt(mm.group(1));
                    int mmn = mm.group(2) != null ? Integer.parseInt(mm.group(2)) : 0;
                    return new int[]{hh, mmn};
                } catch (Exception ignored) {}
            }
        }
        return new int[]{0,0};
    }

    // Converts day strings into a dayCount-sized boolean array
    private boolean[] parseDays(String raw) {
        boolean[] flags = new boolean[dayCount];
        if (raw == null) return flags;

        String s = raw.toUpperCase();
        s = s.replaceAll("[,/\\\\-]", " ").trim();
        if (s.isEmpty()) return flags;

        // more cleaning
        String[] tokens = s.split("\\s+");
        for (String tok : tokens) {
            if (tok == null || tok.isEmpty()) continue;

            String t = tok.replaceAll("[^A-Z]", "");
            if (t.isEmpty()) continue;

            // Handle Thursday first so it's not mistaken for "T"
            if (t.contains("TH")) {
                flags[3] = true;
                if (t.contains("T") && !t.equals("TH")) flags[1] = true;
            }

            if (t.contains("M")) flags[0] = true;
            if (t.contains("T") && !t.contains("TH")) flags[1] = true;
            if (t.contains("W")) flags[2] = true;
            if (t.contains("SA")) flags[5] = true;
            if (t.contains("F")) flags[4] = true;

        }

        return flags;
    }
}
