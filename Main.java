// Main.java
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DATA_FILE = "patients.csv";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PatientManager manager = new PatientManager(DATA_FILE);

        // try load existing data
        try {
            manager.loadFromFile();
            System.out.println("Loaded existing patients (if any) from " + DATA_FILE);
        } catch (Exception e) {
            System.out.println("Could not load data: " + e.getMessage());
        }

        boolean exit = false;
        while (!exit) {
            showMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": addPatientCLI(sc, manager); break;
                case "2": listPatients(manager); break;
                case "3": searchPatientCLI(sc, manager); break;
                case "4": updatePatientCLI(sc, manager); break;
                case "5": deletePatientCLI(sc, manager); break;
                case "6": saveCLI(manager); break;
                case "0": exit = confirmAndExit(sc, manager); break;
                default: System.out.println("Invalid choice. Try again."); break;
            }
        }
        sc.close();
    }

    private static void showMenu() {
        System.out.println("\n--- Leucoderma Patient Manager ---");
        System.out.println("1. Add patient");
        System.out.println("2. List all patients");
        System.out.println("3. Search patient by ID");
        System.out.println("4. Update patient by ID");
        System.out.println("5. Delete patient by ID");
        System.out.println("6. Save to file");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void addPatientCLI(Scanner sc, PatientManager manager) {
        System.out.println("\n-- Add Patient --");
        System.out.print("ID: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) { System.out.println("ID cannot be empty."); return; }
        if (manager.findById(id) != null) { System.out.println("ID already exists."); return; }

        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Age: ");
        int age = readIntSafe(sc);
        System.out.print("Gender: ");
        String gender = sc.nextLine().trim();
        System.out.print("Contact: ");
        String contact = sc.nextLine().trim();
        System.out.print("Diagnosis date (YYYY-MM-DD): ");
        LocalDate diagDate = readDateSafe(sc);
        System.out.print("Severity (Mild/Moderate/Severe): ");
        String severity = sc.nextLine().trim();
        System.out.print("Notes: ");
        String notes = sc.nextLine().trim();

        Patient p = new Patient(id, name, age, gender, contact, diagDate, severity, notes);
        manager.addPatient(p);
        System.out.println("Patient added.");
    }

    private static void listPatients(PatientManager manager) {
        List<Patient> all = manager.listPatients();
        if (all.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }
        System.out.println("\n--- Patients ---");
        for (Patient p : all) {
            System.out.println(p);
        }
    }

    private static void searchPatientCLI(Scanner sc, PatientManager manager) {
        System.out.print("Enter ID to search: ");
        String id = sc.nextLine().trim();
        Patient p = manager.findById(id);
        if (p == null) System.out.println("Patient not found.");
        else System.out.println("\n" + p);
    }

    private static void updatePatientCLI(Scanner sc, PatientManager manager) {
        System.out.print("Enter ID to update: ");
        String id = sc.nextLine().trim();
        Patient p = manager.findById(id);
        if (p == null) { System.out.println("Patient not found."); return; }

        System.out.println("Leave blank to keep existing value.");
        System.out.print("New name: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) p.setName(name);

        System.out.print("New age: ");
        String ageStr = sc.nextLine().trim();
        if (!ageStr.isEmpty()) {
            try { p.setAge(Integer.parseInt(ageStr)); } catch (NumberFormatException e) { System.out.println("Invalid age ignored."); }
        }

        System.out.print("New gender: ");
        String gender = sc.nextLine().trim();
        if (!gender.isEmpty()) p.setGender(gender);

        System.out.print("New contact: ");
        String contact = sc.nextLine().trim();
        if (!contact.isEmpty()) p.setContact(contact);

        System.out.print("New diagnosis date (YYYY-MM-DD): ");
        String dateStr = sc.nextLine().trim();
        if (!dateStr.isEmpty()) {
            try { p.setDiagnosisDate(LocalDate.parse(dateStr)); } catch (DateTimeParseException e) { System.out.println("Invalid date ignored."); }
        }

        System.out.print("New severity: ");
        String severity = sc.nextLine().trim();
        if (!severity.isEmpty()) p.setSeverity(severity);

        System.out.print("New notes: ");
        String notes = sc.nextLine().trim();
        if (!notes.isEmpty()) p.setNotes(notes);

        System.out.println("Patient updated.");
    }

    private static void deletePatientCLI(Scanner sc, PatientManager manager) {
        System.out.print("Enter ID to delete: ");
        String id = sc.nextLine().trim();
        System.out.print("Are you sure? (y/N): ");
        String confirm = sc.nextLine().trim();
        if (!confirm.equalsIgnoreCase("y")) { System.out.println("Delete cancelled."); return; }
        boolean deleted = manager.deleteById(id);
        System.out.println(deleted ? "Deleted." : "ID not found.");
    }

    private static void saveCLI(PatientManager manager) {
        try {
            manager.saveToFile();
            System.out.println("Saved to file.");
        } catch (Exception e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    private static boolean confirmAndExit(Scanner sc, PatientManager manager) {
        System.out.print("Save before exit? (Y/n): ");
        String ans = sc.nextLine().trim();
        if (ans.isEmpty() || ans.equalsIgnoreCase("y")) {
            try { manager.saveToFile(); System.out.println("Saved."); } catch (Exception e) { System.out.println("Save failed: " + e.getMessage()); }
        }
        System.out.println("Goodbye.");
        return true;
    }

    private static int readIntSafe(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, try again: ");
            }
        }
    }

    private static java.time.LocalDate readDateSafe(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return java.time.LocalDate.parse(s);
            } catch (Exception e) {
                System.out.print("Invalid date format. Use YYYY-MM-DD: ");
            }
        }
    }
}// Main.java
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DATA_FILE = "patients.csv";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PatientManager manager = new PatientManager(DATA_FILE);

        // try load existing data
        try {
            manager.loadFromFile();
            System.out.println("Loaded existing patients (if any) from " + DATA_FILE);
        } catch (Exception e) {
            System.out.println("Could not load data: " + e.getMessage());
        }

        boolean exit = false;
        while (!exit) {
            showMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": addPatientCLI(sc, manager); break;
                case "2": listPatients(manager); break;
                case "3": searchPatientCLI(sc, manager); break;
                case "4": updatePatientCLI(sc, manager); break;
                case "5": deletePatientCLI(sc, manager); break;
                case "6": saveCLI(manager); break;
                case "0": exit = confirmAndExit(sc, manager); break;
                default: System.out.println("Invalid choice. Try again."); break;
            }
        }
        sc.close();
    }

    private static void showMenu() {
        System.out.println("\n--- Leucoderma Patient Manager ---");
        System.out.println("1. Add patient");
        System.out.println("2. List all patients");
        System.out.println("3. Search patient by ID");
        System.out.println("4. Update patient by ID");
        System.out.println("5. Delete patient by ID");
        System.out.println("6. Save to file");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void addPatientCLI(Scanner sc, PatientManager manager) {
        System.out.println("\n-- Add Patient --");
        System.out.print("ID: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) { System.out.println("ID cannot be empty."); return; }
        if (manager.findById(id) != null) { System.out.println("ID already exists."); return; }

        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Age: ");
        int age = readIntSafe(sc);
        System.out.print("Gender: ");
        String gender = sc.nextLine().trim();
        System.out.print("Contact: ");
        String contact = sc.nextLine().trim();
        System.out.print("Diagnosis date (YYYY-MM-DD): ");
        LocalDate diagDate = readDateSafe(sc);
        System.out.print("Severity (Mild/Moderate/Severe): ");
        String severity = sc.nextLine().trim();
        System.out.print("Notes: ");
        String notes = sc.nextLine().trim();

        Patient p = new Patient(id, name, age, gender, contact, diagDate, severity, notes);
        manager.addPatient(p);
        System.out.println("Patient added.");
    }

    private static void listPatients(PatientManager manager) {
        List<Patient> all = manager.listPatients();
        if (all.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }
        System.out.println("\n--- Patients ---");
        for (Patient p : all) {
            System.out.println(p);
        }
    }

    private static void searchPatientCLI(Scanner sc, PatientManager manager) {
        System.out.print("Enter ID to search: ");
        String id = sc.nextLine().trim();
        Patient p = manager.findById(id);
        if (p == null) System.out.println("Patient not found.");
        else System.out.println("\n" + p);
    }

    private static void updatePatientCLI(Scanner sc, PatientManager manager) {
        System.out.print("Enter ID to update: ");
        String id = sc.nextLine().trim();
        Patient p = manager.findById(id);
        if (p == null) { System.out.println("Patient not found."); return; }

        System.out.println("Leave blank to keep existing value.");
        System.out.print("New name: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) p.setName(name);

        System.out.print("New age: ");
        String ageStr = sc.nextLine().trim();
        if (!ageStr.isEmpty()) {
            try { p.setAge(Integer.parseInt(ageStr)); } catch (NumberFormatException e) { System.out.println("Invalid age ignored."); }
        }

        System.out.print("New gender: ");
        String gender = sc.nextLine().trim();
        if (!gender.isEmpty()) p.setGender(gender);

        System.out.print("New contact: ");
        String contact = sc.nextLine().trim();
        if (!contact.isEmpty()) p.setContact(contact);

        System.out.print("New diagnosis date (YYYY-MM-DD): ");
        String dateStr = sc.nextLine().trim();
        if (!dateStr.isEmpty()) {
            try { p.setDiagnosisDate(LocalDate.parse(dateStr)); } catch (DateTimeParseException e) { System.out.println("Invalid date ignored."); }
        }

        System.out.print("New severity: ");
        String severity = sc.nextLine().trim();
        if (!severity.isEmpty()) p.setSeverity(severity);

        System.out.print("New notes: ");
        String notes = sc.nextLine().trim();
        if (!notes.isEmpty()) p.setNotes(notes);

        System.out.println("Patient updated.");
    }

    private static void deletePatientCLI(Scanner sc, PatientManager manager) {
        System.out.print("Enter ID to delete: ");
        String id = sc.nextLine().trim();
        System.out.print("Are you sure? (y/N): ");
        String confirm = sc.nextLine().trim();
        if (!confirm.equalsIgnoreCase("y")) { System.out.println("Delete cancelled."); return; }
        boolean deleted = manager.deleteById(id);
        System.out.println(deleted ? "Deleted." : "ID not found.");
    }

    private static void saveCLI(PatientManager manager) {
        try {
            manager.saveToFile();
            System.out.println("Saved to file.");
        } catch (Exception e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    private static boolean confirmAndExit(Scanner sc, PatientManager manager) {
        System.out.print("Save before exit? (Y/n): ");
        String ans = sc.nextLine().trim();
        if (ans.isEmpty() || ans.equalsIgnoreCase("y")) {
            try { manager.saveToFile(); System.out.println("Saved."); } catch (Exception e) { System.out.println("Save failed: " + e.getMessage()); }
        }
        System.out.println("Goodbye.");
        return true;
    }

    private static int readIntSafe(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, try again: ");
            }
        }
    }

    private static java.time.LocalDate readDateSafe(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return java.time.LocalDate.parse(s);
            } catch (Exception e) {
                System.out.print("Invalid date format. Use YYYY-MM-DD: ");
            }
        }
    }
}
