package users;

import java.util.ArrayList;

import courses.Course;

public class User {

    private String username;
    private String password;
    private String degree;
    private ArrayList<Course> courses = new ArrayList<>();
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.degree = "";
    }

    public User(String username, String password, String degree) {
        this.username = username;
        this.password = password;
        this.degree = degree;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }

    public String getDegree() {
        return this.degree;
    }
    
    public void addCourse(Course course) {    	
    	courses.add(course);
    }
    
    public ArrayList<Course> getCourses() {
    	return this.courses;
    }
}
