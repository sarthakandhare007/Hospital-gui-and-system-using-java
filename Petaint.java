// Patient.java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Patient {
    private String id;
    private String name;
    private int age;
    private String gender;
    private String contact;
    private LocalDate diagnosisDate;
    private String severity; // e.g., Mild / Moderate / Severe
    private String notes;

    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_LOCAL_DATE;

    public Patient(String id, String name, int age, String gender, String contact,
                   LocalDate diagnosisDate, String severity, String notes) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.diagnosisDate = diagnosisDate;
        this.severity = severity;
        this.notes = notes;
    }

    // parse CSV line -> Patient
    public static Patient fromCSV(String csvLine) {
        // CSV format: id,name,age,gender,contact,diagnosisDate,severity,notes
        // notes may contain commas, so we assume it's the last field (simple approach).
        String[] parts = csvLine.split(",", 8);
        String id = parts[0];
        String name = parts[1];
        int age = Integer.parseInt(parts[2]);
        String gender = parts[3];
        String contact = parts[4];
        LocalDate diagnosisDate = LocalDate.parse(parts[5], DF);
        String severity = parts[6];
        String notes = parts.length > 7 ? parts[7] : "";
        return new Patient(id, name, age, gender, contact, diagnosisDate, severity, notes);
    }

    // convert to CSV
    public String toCSV() {
        // ensure no newlines in notes
        String sanitizedNotes = notes == null ? "" : notes.replace("\n", " ").replace("\r", " ");
        return String.join(",", id, name, String.valueOf(age), gender, contact,
                diagnosisDate.format(DF), severity, sanitizedNotes);
    }

    @Override
    public String toString() {
        return "ID: " + id +
               "\nName: " + name +
               "\nAge: " + age +
               "\nGender: " + gender +
               "\nContact: " + contact +
               "\nDiagnosis Date: " + diagnosisDate.format(DF) +
               "\nSeverity: " + severity +
               "\nNotes: " + (notes == null ? "" : notes) + "\n";
    }

    // getters & setters
    public String getId() { return id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setContact(String contact) { this.contact = contact; }
    public void setDiagnosisDate(LocalDate diagnosisDate) { this.diagnosisDate = diagnosisDate; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setNotes(String notes) { this.notes = notes; }
}
