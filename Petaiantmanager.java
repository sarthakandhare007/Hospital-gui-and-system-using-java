// PatientManager.java
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class PatientManager {
    private List<Patient> patients = new ArrayList<>();
    private final String filename;

    public PatientManager(String filename) {
        this.filename = filename;
    }

    public void addPatient(Patient p) {
        patients.add(p);
    }

    public List<Patient> listPatients() {
        return new ArrayList<>(patients);
    }

    public Patient findById(String id) {
        for (Patient p : patients) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public boolean deleteById(String id) {
        Iterator<Patient> it = patients.iterator();
        while (it.hasNext()) {
            if (it.next().getId().equals(id)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public void saveToFile() throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Patient p : patients) {
                pw.println(p.toCSV());
            }
        }
    }

    public void loadFromFile() throws IOException {
        patients.clear();
        File f = new File(filename);
        if (!f.exists()) return; // nothing to load
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    Patient p = Patient.fromCSV(line);
                    patients.add(p);
                } catch (Exception e) {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        }
    }
}
