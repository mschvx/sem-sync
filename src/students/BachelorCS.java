package students;

public class BachelorCS implements DegreeProgram {
    @Override
    public String getCourseCode() {
        return "BSCS";
    }
    
    @Override
    public String getCurriculumCSV() {
    	return "Database/ICS_Dataset/ics_cmsc_courses.csv";
    }
}
