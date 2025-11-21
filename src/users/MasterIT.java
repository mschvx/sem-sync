package users;

public class MasterIT implements DegreeProgram {
    @Override
    public String getCourseCode() {
        return "MSIT";
    }
    
    @Override
    public String getCurriculumCSV() {
    	return "Database/ICS_Dataset/ics_mit_courses.csv";
    }
}
