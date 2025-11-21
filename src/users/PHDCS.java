package users;

public class PHDCS implements DegreeProgram {
    @Override
    public String getCourseCode() {
        return "PHD";
    }
    
    @Override
    public String getCurriculumCSV() {
    	return "Database/ICS_Dataset/ics_phd_courses.csv";
    }
}
