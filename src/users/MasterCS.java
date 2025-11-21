package users;

public class MasterCS implements DegreeProgram {
    @Override
    public String getCourseCode() {
        return "MSCS";
    }
    
    @Override
    public String getCurriculumCSV() {
    	return "Database/ICS_Dataset/ics_mscs_courses.csv";
    }
}
