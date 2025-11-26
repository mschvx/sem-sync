package courses;

import java.util.ArrayList;

public class Lecture extends Course{
	
	private ArrayList<Laboratory> labSections = new ArrayList<>();
	
	public Lecture(String cC, String cT, int u, String s, String t, String d, String r) {
		super(cC, cT, u,s,t,d,r);
	}
	
	public ArrayList<Laboratory> getLabSections() {
		return this.labSections;
	}
	
	public void addLabSection(Laboratory l) {
		labSections.add(l);
		l.setlectureSection(this);
	}
	
}
