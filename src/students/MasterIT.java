package students;

public class MasterIT implements DegreeProgram {
    @Override
    public String getCourseCode() {
        return "MIT";
    }
    
    @Override
    public String getCurriculumCSV() {
    	return "Database/ICS_Dataset/ics_mit_courses.csv";
    }
}
