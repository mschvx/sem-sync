package users;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CurriculumLoader {

	public static List<Curriculum> loadfromCSV(String csvPath) {
		List<Curriculum> list = new ArrayList<>();
		
		try {
			List<String> lines = Files.readAllLines(Paths.get(csvPath));
			
			// skip header row
			for (int i = 1; i < lines.size(); i++) {
				String[] parts = lines.get(i).split(",");
				
				String code = parts[0].trim();
				String name = parts[1].trim();
				int units = Integer.parseInt(parts[2].trim());
				String desc = parts.length > 3 ? parts[3].trim() : ""; // omits desc if there is none
				
				list.add(new Curriculum(code, name, units, desc));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
