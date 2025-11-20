package courses;

public class Course {
	private String courseCode;
	private String courseTitle;
	private int units;
	private String section;
	private String times;
	private String days;
	private String rooms;
	
	public Course(String cC, String cT, int u, String s, String t, String d, String r) {
		this.courseCode = cC;
		this.courseTitle = cT;
		this.units = u;
		this.section = s;
		this.times = t;
		this.days = d;
		this.rooms = r;
	}
	
	
	public String getCourseCode() {
		return this.courseCode;
	}
	
	public String getCourseTitle() {
		return this.courseTitle;
	}
	
	public int getUnits() {
		return this.units;
	}
	
	public String getSection() {
		return this.section;
	}
	
	public String getTimes() {
		return this.times;
	}
	
	public String getDays() {
		return this.days;
	}
	
	public String getRooms() {
		return this.rooms;
	}
}
