package courses;

public class Laboratory extends Course{

	private Lecture lectureSection;
	
	public Laboratory(String cC, String cT, int u, String s, String t, String d, String r) {
		super(cC, cT, u,s,t,d,r);			
	}
	
	public Lecture getlectureSection() {
		return this.lectureSection;
	}
	
	public void setlectureSection(Lecture lec) {
		this.lectureSection = lec;
	}
}
