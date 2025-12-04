package students;
import java.nio.file.*;
import java.util.*;
import courses.Course;
import courses.Lecture;
import courses.Laboratory;
import java.util.stream.Collectors;
import application.Main;
public class UserCourseStorage {
	private static final Path PATH = Paths.get("Database/UserCourses.csv");
	
	/// Save the courses for the given user. Keeps other users intact.
   public static void saveCourses(Students user) {
       if (user == null) {
           System.out.println("[UserCourseStorage] saveCourses: user is null");
           return;
       }
       try {
           if (PATH.getParent() != null) Files.createDirectories(PATH.getParent());
           // Read existing lines (if any)
           List<String> existing = Files.exists(PATH) ? Files.readAllLines(PATH) : new ArrayList<>();
           // Filter out users old data (sa csv)
           List<String> updated = existing.stream()
                   .filter(line -> {
                       if (line == null) return false;
                       String l = line.trim();
                       if (l.isEmpty()) return false;
                       String[] p = l.split(",", 3);
                       if (p.length < 1) return false;
                       String uname = p[0].trim();
                       return !uname.equals(user.getUsername());
                   })
                   .collect(Collectors.toList());
           // username,courseCode,section1
           if (user.getCourses() != null) {
               // preserve insertion order of first appearance
               Map<String, LinkedHashSet<String>> grouped = new LinkedHashMap<>();
               for (Course c : user.getCourses()) {
                   if (c == null) continue;
                   String code = c.getCourseCode() == null ? "" : c.getCourseCode().trim();
                   if (code.isEmpty()) continue;
                   grouped.putIfAbsent(code, new LinkedHashSet<>());
                   if (c instanceof Lecture) {
                       Lecture lec = (Lecture) c;
                       String lecSec = lec.getSection() == null ? "" : lec.getSection().trim();
                       if (!lecSec.isEmpty()) grouped.get(code).add(lecSec);
                       // include sections from attached labs on the corresponding lecture
                       try {
                           for (Laboratory lab : lec.getLabSections()) {
                               if (lab == null) continue;
                               String lsec = lab.getSection() == null ? "" : lab.getSection().trim();
                               if (!lsec.isEmpty()) grouped.get(code).add(lsec);
                           }
                       } catch (Exception ignored) {}
                   } else if (c instanceof Laboratory) {
                       Laboratory lab = (Laboratory) c;
                       String lsec = lab.getSection() == null ? "" : lab.getSection().trim();
                       if (!lsec.isEmpty()) grouped.get(code).add(lsec);
                   } else {
                       String section = c.getSection() == null ? "" : c.getSection().trim();
                       if (!section.isEmpty()) grouped.get(code).add(section);
                   }
               }
               for (Map.Entry<String, LinkedHashSet<String>> e : grouped.entrySet()) {
                   String code = e.getKey();
                   // join sections with semicolon
                   String sections = String.join(";", e.getValue());
                   updated.add(user.getUsername() + "," + code + "," + sections);
               }
           }
           // Write atomically (truncate)
           Files.write(PATH, updated, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
           int savedCount = 0;
           if (user.getCourses() != null) {
               // count unique course codes
               Set<String> uniq = new HashSet<>();
               for (Course c : user.getCourses()) if (c != null && c.getCourseCode() != null && !c.getCourseCode().trim().isEmpty()) uniq.add(c.getCourseCode().trim().toUpperCase());
               savedCount = uniq.size();
           }
           System.out.println("[UserCourseStorage] Saved " + savedCount + " courses for " + user.getUsername()
                              + " -> " + PATH.toAbsolutePath());
       } catch (Exception e) {
           System.out.println("[UserCourseStorage] Error saving courses: " + e.getMessage());
           e.printStackTrace();
       }
   }
	
// Load the user's courses into the Students object (clears existing courses first)
   public static void loadCourses(Students user) {
       if (user == null) return;
       try {
           if (!Files.exists(PATH)) {
               System.out.println("[UserCourseStorage] loadCourses: file not found -> " + PATH.toAbsolutePath());
               return;
           }
           // Clear existing courses on the user model to avoid duplicates
           if (user.getCourses() != null) user.getCourses().clear();
           List<String> lines = Files.readAllLines(PATH);
           Map<String, LinkedHashSet<String>> grouped = new LinkedHashMap<>();
           for (String line : lines) {
               if (line == null) continue;
               String l = line.trim();
               if (l.isEmpty()) continue;
               String[] parts = l.split(",", 3);
               if (parts.length < 2) continue;
               String uname = parts[0].trim();
               String courseCode = parts[1].trim();
               String section = (parts.length > 2) ? parts[2].trim() : "";
               if (!uname.equals(user.getUsername())) continue;
               if (courseCode == null || courseCode.trim().isEmpty()) continue;
               grouped.putIfAbsent(courseCode, new LinkedHashSet<>());
               if (section == null || section.isEmpty()) {
                   grouped.get(courseCode).add("");
               } else {
                   String[] sections = section.split(";");
                   for (String s : sections) grouped.get(courseCode).add(s == null ? "" : s.trim());
               }
           }
           // add courses for each grouped course code and its sections
           for (Map.Entry<String, LinkedHashSet<String>> entry : grouped.entrySet()) {
               String courseCode = entry.getKey();
               Set<String> savedSecs = new HashSet<>();
               for (String s : entry.getValue()) savedSecs.add(s == null ? "" : s.trim().toUpperCase());

               // find a base lectur
               Course baseLecture = Main.allCourseOfferings.stream()
                       .filter(Objects::nonNull)
                       .filter(c -> c instanceof Lecture)
                       .filter(c -> {
                           String cc = c.getCourseCode() == null ? "" : c.getCourseCode().trim().toUpperCase();
                           String sec = c.getSection() == null ? "" : c.getSection().trim().toUpperCase();
                           return !cc.isEmpty() && cc.equals(courseCode.toUpperCase()) && savedSecs.contains(sec);
                       }).findFirst().orElse(null);

               if (baseLecture == null) {
                   baseLecture = Main.allCourseOfferings.stream()
                           .filter(Objects::nonNull)
                           .filter(c -> c instanceof Lecture)
                           .filter(c -> {
                               String cc = c.getCourseCode() == null ? "" : c.getCourseCode().trim().toUpperCase();
                               return !cc.isEmpty() && cc.equals(courseCode.toUpperCase());
                           }).findFirst().orElse(null);
               }

               if (baseLecture != null && baseLecture instanceof Lecture) {
                   Lecture b = (Lecture) baseLecture;
                   Lecture userLec = new Lecture(b.getCourseCode(), b.getCourseTitle(), b.getUnits(), b.getSection(), b.getTimes(), b.getDays(), b.getRooms());
                   // attach only saved labs
                   for (String sec : savedSecs) {
                       if (sec == null) continue;
                       String secTrim = sec.trim();
                       if (secTrim.isEmpty()) continue;
                       String lecSec = userLec.getSection() == null ? "" : userLec.getSection().trim();
                       if (!lecSec.isEmpty() && lecSec.equalsIgnoreCase(secTrim)) continue;
                       for (Course mc : Main.allCourseOfferings) {
                           if (mc == null) continue;
                           if (!(mc instanceof Laboratory)) continue;
                           String cc = mc.getCourseCode() == null ? "" : mc.getCourseCode().trim();
                           String ms = mc.getSection() == null ? "" : mc.getSection().trim();
                           if (cc.equalsIgnoreCase(courseCode) && ms.equalsIgnoreCase(secTrim)) {
                               Laboratory labSrc = (Laboratory) mc;
                               Laboratory labCopy = new Laboratory(labSrc.getCourseCode(), labSrc.getCourseTitle(), labSrc.getUnits(), labSrc.getSection(), labSrc.getTimes(), labSrc.getDays(), labSrc.getRooms());
                               userLec.addLabSection(labCopy);
                               break;
                           }
                       }
                   }
                   user.addCourse(userLec);
                   continue;
               }

               // if no lec found collect lab matches and wrap them to calendar
               List<Laboratory> foundLabs = new ArrayList<>();
               for (String sec : savedSecs) {
                   if (sec == null) continue;
                   String secTrim = sec.trim();
                   if (secTrim.isEmpty()) continue;
                   for (Course mc : Main.allCourseOfferings) {
                       if (mc == null) continue;
                       if (!(mc instanceof Laboratory)) continue;
                       String cc = mc.getCourseCode() == null ? "" : mc.getCourseCode().trim();
                       String ms = mc.getSection() == null ? "" : mc.getSection().trim();
                       if (cc.equalsIgnoreCase(courseCode) && ms.equalsIgnoreCase(secTrim)) {
                           Laboratory labSrc = (Laboratory) mc;
                           Laboratory labCopy = new Laboratory(labSrc.getCourseCode(), labSrc.getCourseTitle(), labSrc.getUnits(), labSrc.getSection(), labSrc.getTimes(), labSrc.getDays(), labSrc.getRooms());
                           foundLabs.add(labCopy);
                           break;
                       }
                   }
               }
               if (!foundLabs.isEmpty()) {
                   Laboratory first = foundLabs.get(0);
                   Lecture wrap = new Lecture(first.getCourseCode(), first.getCourseTitle(), first.getUnits(), "", first.getTimes(), first.getDays(), first.getRooms());
                   for (Laboratory lab : foundLabs) wrap.addLabSection(lab);
                   user.addCourse(wrap);
                   continue;
               }

               System.out.println("[UserCourseStorage] loadCourses: master course not found: " + courseCode);
           }
           System.out.println("[UserCourseStorage] Loaded courses for " + user.getUsername());
       } catch (Exception e) {
           System.out.println("[UserCourseStorage] Error loading courses: " + e.getMessage());
           e.printStackTrace();
       }
   }
}

