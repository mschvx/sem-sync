package application.models;
// for the codes to determine which course code the user has
public class DegreeLookup {
    public static DegreeProgram fromCode(String code) {
        if (code == null) return null;
        switch (code) {
            case "BachelorCS":
                return new BachelorCS();
            case "MasterCS":
                return new MasterCS();
            case "MasterIT":
                return new MasterIT();
            case "PHDCS":
                return new PHDCS();
            default:
                return null;
        }
    }
}
