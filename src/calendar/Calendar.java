// CALENDAR LOGIC
package calendar;

import application.Fonts;
import courses.Course;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.BlurType;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calendar {

    private final Pane root;

    private final double width;
    private final double height;
    private final double timeColWidth = 110;

    private final int dayCount = 5;

    private final int rows = 12;

    private final double headerHeight = 56;

    private final double cellWidth;

    private final double rowHeight;
    
    private ObservableList<Course> calendarCourses = FXCollections.<Course>observableArrayList();

    // Keeps track of rendered course blocks so they can be removed later
    private final Map<Course, List<Node>> blocks = new HashMap<>();

    // Initialize calendar and build its sizes
    public Calendar(double width, double height) {
        this.width = width;
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
        timeCol.setFill(Color.web("#1565C0"));
        root.getChildren().add(timeCol);

        // Add day header rectangles and labels
        String[] dayNames = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
        for (int d = 0; d < dayCount; d++) {
            double x = timeColWidth + d * cellWidth;

            Rectangle r = new Rectangle(x, 0, cellWidth, headerHeight);
            r.setFill(Color.web("#FFD54F"));
            r.setStroke(Color.web("#1565C0"));
            r.setArcWidth(8);
            r.setArcHeight(8);
            root.getChildren().add(r);

            Label dayLabel = new Label(dayNames[d]);
            dayLabel.setTextFill(Color.web("#1565C0"));
            dayLabel.setFont(Fonts.loadMontserratRegular(16));
            dayLabel.setLayoutX(x + 12);
            dayLabel.setLayoutY(10);
            root.getChildren().add(dayLabel);
        }

        // Draw time labels for each hour row
        for (int r = 0; r < rows; r++) {
            int hourStart = 7 + r;
            String labelText = hourStart + "-" + (hourStart + 1);

            Label tlabel = new Label(labelText);
            tlabel.setTextFill(Color.WHITE);
            tlabel.setFont(Fonts.loadMontserratRegular(13));
            tlabel.setLayoutX(8);
            tlabel.setLayoutY(headerHeight + r * rowHeight + (rowHeight / 2.0) - 9);
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

        int startHour = parseHour(hs.length > 0 ? hs[0].trim() : "");
        int endHour = parseHour(hs.length > 1 ? hs[1].trim() : "");

        // normalize hours 
        if (endHour <= startHour) endHour += 12;

        int gridStart = 7;
        double y = headerHeight + Math.max(0, (startHour - gridStart) * rowHeight);
        double heightPx = Math.max(rowHeight * (endHour - startHour), rowHeight);

        // Determine which days the course applies to
        boolean[] dayFlags = parseDays(daysStr);

        // Create a block for each day where the course meets
        for (int d = 0; d < dayCount; d++) {
            if (!dayFlags[d]) continue;

            double x = timeColWidth + d * cellWidth;

            StackPane block = new StackPane();
            block.setPrefWidth(cellWidth - 12);
            block.setPrefHeight(Math.max(28, heightPx - 8));

            Rectangle bgRect = new Rectangle(cellWidth - 12, Math.max(24, heightPx - 8));
            bgRect.setFill(Color.web("#1976D2"));
            bgRect.setArcWidth(12);
            bgRect.setArcHeight(12);
            bgRect.setStroke(Color.web("#FFD54F"));
            bgRect.setStrokeWidth(2);

            DropShadow drop = new DropShadow();
            drop.setBlurType(BlurType.GAUSSIAN);
            drop.setRadius(8);
            drop.setSpread(0.14);
            drop.setOffsetY(2);
            drop.setColor(Color.color(0,0,0,0.28));
            bgRect.setEffect(drop);

            // Course text
            Label info = new Label(
                selectedCourse.getCourseCode() + " " + selectedCourse.getSection() +
                "\n" + selectedCourse.getRooms()
            );
            info.setTextFill(Color.web("#FFFDE7"));
            info.setFont(Fonts.loadMontserratRegular(14));
            info.setWrapText(true);
            info.setMaxWidth(cellWidth - 28);

            block.getChildren().addAll(bgRect, info);
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
            if (t.contains("F")) flags[4] = true;

        }

        return flags;
    }
}
