package users;

import java.util.ArrayList;
import java.util.List;

import courses.Course;

public class User {

    private String username;
    private String password;
    private DegreeProgram degree;
    private List<Curriculum> curricCourses;

    public User(String username, String password, DegreeProgram degree) {
        this.username = username;
        this.password = password;
        this.degree = degree;
        this.curricCourses = CurriculumLoader.loadfromCSV(degree.getCurriculumCSV());
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }

    public DegreeProgram getDegree() {
        return this.degree;
    }
    
    public List<Curriculum> getCurriculumCourses(){
    	return this.curricCourses;
    }
    
}
