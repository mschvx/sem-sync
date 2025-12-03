package students;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
				int units = parseUnits(parts.length > 2 ? parts[2].trim() : "");
				String desc = parts.length > 3 ? parts[3].trim() : ""; // omits desc if there is none
				
				list.add(new Curriculum(code, name, units, desc));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Parse units field robustly. Accepts plain integers or ranges like "1-3".
	private static int parseUnits(String raw) {
		if (raw == null) return 0;
		raw = raw.trim();
		if (raw.isEmpty()) return 0;
		// Find the first integer in the string (handles ranges like "1-3", "3 (lab)")
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(raw);
		if (m.find()) {
			try {
				return Integer.parseInt(m.group());
			} catch (NumberFormatException nfe) {
				return 0;
			}
		}
		return 0;
	}
}
