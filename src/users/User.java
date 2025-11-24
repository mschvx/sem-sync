package users;

import java.util.ArrayList;
import java.util.List;

import courses.Course;

public class User {

    private String username;
    private String password;
    // store the raw degree code (e.g. "BachelorCS") so callers that expect a string can use it
    private String degreeCode;
    // resolved DegreeProgram (may be null if code is invalid)
    private DegreeProgram degreeProgram;
    private List<Curriculum> curricCourses;
    // the user's selected/added courses during a session
    private List<Course> userCourses;

    // Constructor used by loaders which store the degree as a string code
    public User(String username, String password, String degreeCode) {
        this.username = username;
        this.password = password;
        this.degreeCode = degreeCode;
        this.degreeProgram = DegreeLookup.fromCode(degreeCode);
        if (this.degreeProgram != null && this.degreeProgram.getCurriculumCSV() != null && !this.degreeProgram.getCurriculumCSV().isEmpty()) {
            this.curricCourses = CurriculumLoader.loadfromCSV(this.degreeProgram.getCurriculumCSV());
        } else {
            this.curricCourses = new ArrayList<>();
        }
        this.userCourses = new ArrayList<>();
    }

    // Optional constructor if a DegreeProgram object is already available
    public User(String username, String password, DegreeProgram degree) {
        this.username = username;
        this.password = password;
        this.degreeProgram = degree;
        this.degreeCode = (degree != null) ? degree.getClass().getSimpleName() : null;
        if (this.degreeProgram != null && this.degreeProgram.getCurriculumCSV() != null && !this.degreeProgram.getCurriculumCSV().isEmpty()) {
            this.curricCourses = CurriculumLoader.loadfromCSV(this.degreeProgram.getCurriculumCSV());
        } else {
            this.curricCourses = new ArrayList<>();
        }
        this.userCourses = new ArrayList<>();
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    // Keep compatibility with code that expects a String degree code
    public String getDegree() {
        return this.degreeCode;
    }

    // Access to the resolved DegreeProgram if needed
    public DegreeProgram getDegreeProgram() {
        return this.degreeProgram;
    }

    public List<Curriculum> getCurriculumCourses(){
        return this.curricCourses;
    }

    // Course management used by UI (Dashboard expects these)
    public List<Course> getCourses() {
        return this.userCourses;
    }

    public void addCourse(Course c) {
        if (c != null) this.userCourses.add(c);
    }

}
