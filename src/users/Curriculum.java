package users;

import courses.Course;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Curriculum {

	private String courseCode;
	private String courseName;
	private int units;
	private String description;
	
	public Curriculum(String cC, String cN, int units, String desc) {
		this.courseCode = cC;
		this.courseName = cN;
		this.units = units;
		this.description = desc;
	}
	
	public String getCourseCode() {
		return this.courseCode;
	}
	
	public String getCourseName() {
		return this.courseName;
	}
	
	public int getUnits() {
		return this.units;
	}
	
	public String getDescription() {
		return this.description;
	}
}
