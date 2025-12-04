package ui;

import application.Fonts;
import application.Main;
import students.Students;
import courses.Course;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Catalogue {

	public void showCatalogue(Stage primaryStage, Students user) {
		Pane root = new Pane();
		Scene scene = new Scene(root, 1536, 864);
		scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());

		Label title = new Label("Catalogue");
		title.setFont(Fonts.loadSensaSerif(88));
		title.setStyle("-fx-text-fill: black;");
		title.setLayoutY(24);
		title.layoutXProperty().bind(scene.widthProperty().subtract(320));

		TextField txtSearchCode = new TextField();
		txtSearchCode.setPromptText("Search by Course Code (e.g. CMSC 12)");
		txtSearchCode.setPrefWidth(720);
		txtSearchCode.setFont(Fonts.loadMontserratRegular(22));
		txtSearchCode.prefWidthProperty().bind(scene.widthProperty().subtract(960));

		Button btnSearch = new Button("SEARCH");
		btnSearch.getStyleClass().add("btn-create");
		btnSearch.getStyleClass().add("sidebar-pill");
		btnSearch.setFont(Fonts.loadSensaWild(18));
		btnSearch.setMinWidth(120);

		Button btnShowAll = new Button("SHOW ALL");
		btnShowAll.getStyleClass().add("btn-create");
		btnShowAll.getStyleClass().add("sidebar-pill");
		btnShowAll.setFont(Fonts.loadSensaWild(18));
		btnShowAll.setMinWidth(120);

		// search field and buttons 
		HBox searchBox = new HBox(12);
		searchBox.setLayoutX(160);
		searchBox.setLayoutY(116);
		searchBox.setAlignment(Pos.CENTER_LEFT);
		searchBox.getChildren().addAll(txtSearchCode, btnSearch, btnShowAll);

		Button goBackButton = new Button("Go Back");
		goBackButton.getStyleClass().add("btn-back");
		goBackButton.setPrefWidth(260);
		goBackButton.setPrefHeight(56);
		goBackButton.setFont(Fonts.loadSensaWild(40));
		goBackButton.layoutXProperty().bind(scene.widthProperty().subtract(300));
		goBackButton.layoutYProperty().bind(scene.heightProperty().subtract(120));
		goBackButton.setOnAction(e -> {
			if (Main.isLoggedIn) {
				new Dashboard().showDashboard(primaryStage, user, false);
			} else {
				Main main = new Main();
				main.start(primaryStage);
			}
		});

		root.getChildren().addAll(title, searchBox, goBackButton);

		List<Course> allOfferings = new ArrayList<>();
		Path folder = Paths.get("Database/ICS_Dataset");
		Path offeringsFile = folder.resolve("course_offerings.csv");
		try (BufferedReader reader = Files.newBufferedReader(offeringsFile, StandardCharsets.UTF_8)) {
			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) continue;
				String[] parts = line.split(",");
				if (parts.length < 7) continue;
				String courseCode = parts[0].trim();
				String courseName = parts[1].trim();
				int units = 0; try { units = Integer.parseInt(parts[2].trim()); } catch (Exception ex) {}
				String section = parts[3].trim();
				String times = parts[4].trim();
				String days = parts[5].trim();
				String rooms = parts[6].trim();
				Course c = new Course(courseCode, courseName, units, section, times, days, rooms);
				allOfferings.add(c);
			}
		} catch (IOException ignored) {}

		Map<String, String> descMap = new LinkedHashMap<>();
		try {
			if (Files.isDirectory(folder)) {
				Files.list(folder).filter(p -> p.getFileName().toString().startsWith("ics_") && p.getFileName().toString().endsWith("_courses.csv")).forEach(p -> {
					try (BufferedReader r = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
						r.readLine();
						String l;
						while ((l = r.readLine()) != null) {
							l = l.trim(); if (l.isEmpty()) continue;
							String[] parts = l.split(",", 4);
							if (parts.length >= 4) {
								String code = normalizeCourseCode(parts[0]);
								String desc = parts[3].trim();
								if (desc.startsWith("\"") && desc.endsWith("\"")) desc = desc.substring(1, desc.length()-1);
								if (!descMap.containsKey(code)) descMap.put(code, desc);
							}
						}
					} catch (IOException ignored) {}
				});
			}
		} catch (Exception ignored) {}

        // get the course codes load the datasets
		Map<String, List<Course>> grouped = new LinkedHashMap<>();
		List<String> courseOrder = new ArrayList<>();
		for (Course c : allOfferings) {
			String key = normalizeCourseCode(c.getCourseCode());
			grouped.computeIfAbsent(key, k -> new ArrayList<>());
			grouped.get(key).add(c);
			if (!courseOrder.contains(key)) courseOrder.add(key);
		}

		ObservableList<String> fullCourseCodes = FXCollections.observableArrayList(courseOrder);
		final int pageSize = 6;
		final int[] currentPage = new int[]{1};

		VBox contentBox = new VBox(18);
		contentBox.setPrefWidth(980);
		contentBox.setLayoutX(0);
		contentBox.setLayoutY(0);

		ScrollPane sp = new ScrollPane(contentBox);
		sp.setLayoutX(160);
		sp.setLayoutY(180);

		// for sizing
		sp.prefWidthProperty().bind(scene.widthProperty().subtract(480));
		sp.prefHeightProperty().bind(scene.heightProperty().subtract(260));
		sp.setFitToWidth(true);
		sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // for pagination
		Button btnPrev = new Button("<");
		Button btnNext = new Button(">");
		Label lblPage = new Label();
		btnPrev.getStyleClass().add("pagination-btn"); btnNext.getStyleClass().add("pagination-btn");

		HBox paginationBox = new HBox(12);
		paginationBox.setAlignment(Pos.CENTER);
		paginationBox.getStyleClass().add("pagination-hbox");
		paginationBox.getChildren().addAll(btnPrev, lblPage, btnNext);

		// content in the box is dynamically resizable
		contentBox.prefWidthProperty().bind(sp.widthProperty().subtract(20));
		root.getChildren().addAll(sp);

		Runnable updatePage = () -> {
			int total = fullCourseCodes.size();
			int totalPages = Math.max(1, (int)Math.ceil(total / (double)pageSize));
			if (currentPage[0] > totalPages) currentPage[0] = totalPages;
			if (currentPage[0] < 1) currentPage[0] = 1;
			int from = (currentPage[0]-1) * pageSize;
			int to = Math.min(from + pageSize, total);
			List<String> pageCodes = new ArrayList<>();
			if (from < to) pageCodes.addAll(fullCourseCodes.subList(from, to));

            // formatting the table
			contentBox.getChildren().clear();
			for (String codeKey : pageCodes) {
				List<Course> items = grouped.getOrDefault(codeKey, Collections.emptyList());
				if (items.isEmpty()) continue;
				Course sample = items.get(0);
				String displayCode = sample.getCourseCode();
				String displayTitle = sample.getCourseTitle();

				Label hdr = new Label(displayCode + " — " + displayTitle);
				hdr.setFont(Fonts.loadSensaWild(28));
				contentBox.getChildren().add(hdr);

                
				String desc = descMap.getOrDefault(codeKey, "");
				if (!desc.isEmpty()) {
					Label descLbl = new Label(desc);
					descLbl.setFont(Fonts.loadMontserratRegular(18));
					descLbl.setWrapText(true);
					descLbl.maxWidthProperty().bind(contentBox.widthProperty().subtract(20));
					contentBox.getChildren().add(descLbl);
				}

                // columns and labels
				TableView<Course> tv = new TableView<>();
				TableColumn<Course, String> colSection = new TableColumn<>("Section");
				colSection.setCellValueFactory(new PropertyValueFactory<>("section"));
				TableColumn<Course, String> colTimes = new TableColumn<>("Times");
				colTimes.setCellValueFactory(new PropertyValueFactory<>("times"));
				TableColumn<Course, String> colDays = new TableColumn<>("Days");
				colDays.setCellValueFactory(new PropertyValueFactory<>("days"));
				TableColumn<Course, String> colRooms = new TableColumn<>("Rooms");
				colRooms.setCellValueFactory(new PropertyValueFactory<>("rooms"));
				TableColumn<Course, Integer> colUnits = new TableColumn<>("Units");
				colUnits.setCellValueFactory(new PropertyValueFactory<>("units"));

				tv.getColumns().add(colSection);
				tv.getColumns().add(colTimes);
				tv.getColumns().add(colDays);
				tv.getColumns().add(colRooms);
				tv.getColumns().add(colUnits);
				tv.setItems(FXCollections.observableArrayList(items));
				// row size controls
				tv.setFixedCellSize(34);
				tv.prefWidthProperty().bind(contentBox.widthProperty().subtract(20));
				tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				colSection.prefWidthProperty().bind(tv.widthProperty().multiply(0.18));
				colTimes.prefWidthProperty().bind(tv.widthProperty().multiply(0.22));
				colDays.prefWidthProperty().bind(tv.widthProperty().multiply(0.14));
				colRooms.prefWidthProperty().bind(tv.widthProperty().multiply(0.32));
				colUnits.prefWidthProperty().bind(tv.widthProperty().multiply(0.14));
				tv.setStyle("-fx-font-size: 16px;");
				double h = 48 + Math.max(1, items.size()) * tv.getFixedCellSize();
				tv.setPrefHeight(Math.max(h, 100));
				contentBox.getChildren().add(tv);
			}

			// pagination appended to content so it scrolls with groups
			contentBox.getChildren().add(paginationBox);

			lblPage.setText(currentPage[0] + " / " + totalPages + "   (" + total + " courses)");
			btnPrev.setDisable(currentPage[0] <= 1);
			btnNext.setDisable(currentPage[0] >= totalPages);
		};

		btnPrev.setOnAction(e -> { if (currentPage[0] > 1) currentPage[0]--; updatePage.run(); });
		btnNext.setOnAction(e -> { int total = fullCourseCodes.size(); int totalPages = Math.max(1, (int)Math.ceil(total / (double)pageSize)); if (currentPage[0] < totalPages) currentPage[0]++; updatePage.run(); });

		btnShowAll.setOnAction(e -> { fullCourseCodes.setAll(courseOrder); currentPage[0] = 1; updatePage.run(); });
		btnSearch.setOnAction(e -> { String q = txtSearchCode.getText(); String qn = normalizeCourseCode(q); List<String> filtered = courseOrder.stream().filter(cc -> cc.contains(qn.toUpperCase())).collect(Collectors.toList()); fullCourseCodes.setAll(filtered); currentPage[0] = 1; updatePage.run(); });

		btnShowAll.fire();

		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setScene(scene);
		primaryStage.setTitle("SemSync - Catalogue");
		primaryStage.setX(visualBounds.getMinX());
		primaryStage.setY(visualBounds.getMinY());
		primaryStage.setWidth(visualBounds.getWidth());
		primaryStage.setHeight(visualBounds.getHeight());
		primaryStage.show();
	}

    // for cleaning course codes
	private static String normalizeCourseCode(String raw) {
		if (raw == null) return "";
		String s = raw.trim().toUpperCase();
		s = s.replaceAll("\\s+", " ");
		return s;
	}

}
