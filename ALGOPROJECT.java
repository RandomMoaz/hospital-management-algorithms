ackage com.mycompany.algoproject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/* 
 * Three different sorting algorithms + binary search:
 *   - Hospitals : Merge Sort  (sort by name)      -> O(n log n)
 *   - Doctors   : Insertion Sort (sort by ID)     -> O(n^2)
 *   - Patients  : Quick Sort  (sort by ID)         -> O(n log n) avg / O(n^2) worst
 *   - Search doctors by specialty : Linear scan collecting ALL matches -> O(n)
 *   - Search patient by ID : Binary Search (list auto-sorted first)     -> O(log n)

 */

class Hospital {
    String name;
    String location;

    Hospital(String name, String location) {
        this.name = name;
        this.location = location;
    }
}

class Doctor {
    int id;
    String phone;          
    String name;
    String specialization;

    Doctor(int id, String name, String phone, String specialization) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.specialization = specialization;
    }
}

class Patient {
    int id;
    String phone;          // phone as String (see note above)
    int age;
    String name;

    Patient(int id, String name, String phone, int age) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.age = age;
    }
}

public class ALGOPROJECT {

    private static final List<Hospital> hospitals = new ArrayList<>();
    private static final List<Doctor> doctors = new ArrayList<>();
    private static final List<Patient> patients = new ArrayList<>();

    // ===================== DATA + ALGORITHMS (no GUI) =====================

    // General Rule #3: insert some data at the beginning by calling functions in main.
    private static void seedData() {
        hospitals.add(new Hospital("Cleopatra", "Alexandria"));
        hospitals.add(new Hospital("As-Salam", "Cairo"));
        hospitals.add(new Hospital("Dar Al Fouad", "Giza"));
        hospitals.add(new Hospital("Bahya", "Cairo"));

        doctors.add(new Doctor(205, "Dr. Ahmed", "01012345678", "Cardiology"));
        doctors.add(new Doctor(101, "Dr. Sara",  "01198765432", "Neurology"));
        doctors.add(new Doctor(150, "Dr. Omar",  "01234567890", "Cardiology"));
        doctors.add(new Doctor(120, "Dr. Mona",  "01555555555", "Pediatrics"));
        doctors.add(new Doctor(180, "Dr. Hana",  "01099887766", "Cardiology"));

        patients.add(new Patient(3003, "Youssef", "01000000001", 30));
        patients.add(new Patient(1001, "Laila",   "01000000002", 25));
        patients.add(new Patient(2002, "Karim",   "01000000003", 45));
        patients.add(new Patient(1500, "Nour",    "01000000004", 12));
    }

    // ---- Hospitals: Merge Sort by name ----
    private static void sortHospitalsByName() {
        mergeSortHospitals(hospitals, 0, hospitals.size() - 1);
    }

    private static void mergeSortHospitals(List<Hospital> a, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;
            mergeSortHospitals(a, left, middle);
            mergeSortHospitals(a, middle + 1, right);
            merge(a, left, middle, right);
        }
    }

    private static void merge(List<Hospital> a, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;
        Hospital[] L = new Hospital[n1];
        Hospital[] R = new Hospital[n2];
        for (int i = 0; i < n1; ++i) L[i] = a.get(left + i);
        for (int j = 0; j < n2; ++j) R[j] = a.get(middle + 1 + j);
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i].name.compareToIgnoreCase(R[j].name) <= 0) {
                a.set(k++, L[i++]);
            } else {
                a.set(k++, R[j++]);
            }
        }
        while (i < n1) a.set(k++, L[i++]);
        while (j < n2) a.set(k++, R[j++]);
    }

    // ---- Doctors: Insertion Sort by ID ----
    private static void sortDoctorsByID() {
        for (int i = 1; i < doctors.size(); ++i) {
            Doctor key = doctors.get(i);
            int j = i - 1;
            while (j >= 0 && doctors.get(j).id > key.id) {
                doctors.set(j + 1, doctors.get(j));
                j--;
            }
            doctors.set(j + 1, key);
        }
    }

    // ---- Patients: Quick Sort by ID ----
    private static void sortPatientsByID() {
        quickSortPatients(patients, 0, patients.size() - 1);
    }

    private static void quickSortPatients(List<Patient> a, int low, int high) {
        if (low < high) {
            int pi = partition(a, low, high);
            quickSortPatients(a, low, pi - 1);
            quickSortPatients(a, pi + 1, high);
        }
    }

    private static int partition(List<Patient> a, int left, int right) {
        int pivot = a.get(right).id;
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (a.get(j).id < pivot) {
                i++;
                Patient tmp = a.get(i);
                a.set(i, a.get(j));
                a.set(j, tmp);
            }
        }
        Patient tmp = a.get(i + 1);
        a.set(i + 1, a.get(right));
        a.set(right, tmp);
        return i + 1;
    }

    // ---- Search ALL doctors with a given specialty (linear search) ----
    private static List<Doctor> searchDoctorsBySpecialty(String specialty) {
        List<Doctor> result = new ArrayList<>();
        for (Doctor d : doctors) {
            if (d.specialization.equalsIgnoreCase(specialty.trim())) {
                result.add(d);
            }
        }
        return result;
    }

    // ---- Search patient by ID (binary search; list sorted first) ----
    private static int binarySearchPatients(int id) {
        sortPatientsByID(); // guarantee the precondition for binary search
        int left = 0, right = patients.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (patients.get(mid).id == id) return mid;
            else if (patients.get(mid).id < id) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    //GUI

    public static void main(String[] args) {
        seedData(); // General Rule #3: insert data by calling a function in main
        SwingUtilities.invokeLater(ALGOPROJECT::buildAndShowGUI);
    }

    private static JTextArea output;

    private static void buildAndShowGUI() {
        JFrame frame = new JFrame("Hospital Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(760, 620);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Hospitals", hospitalPanel());
        tabs.addTab("Doctors", doctorPanel());
        tabs.addTab("Patients", patientPanel());

        output = new JTextArea(12, 60);
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        output.setBorder(BorderFactory.createTitledBorder("Output"));
        JScrollPane outScroll = new JScrollPane(output);
        outScroll.setPreferredSize(new Dimension(740, 240));

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel title = new JLabel("Hospital Management System", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        root.add(title, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);
        root.add(outScroll, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);

        show("Welcome! Seed data loaded: " + hospitals.size() + " hospitals, "
                + doctors.size() + " doctors, " + patients.size() + " patients.");
    }

    // ----- Hospitals tab -----
    private static JPanel hospitalPanel() {
        JTextField name = new JTextField(16);
        JTextField location = new JTextField(16);

        JButton add = new JButton("Add Hospital");
        add.addActionListener(e -> {
            if (name.getText().trim().isEmpty()) { warn("Name is required."); return; }
            hospitals.add(new Hospital(name.getText().trim(), location.getText().trim()));
            show("Hospital added: " + name.getText().trim());
            name.setText(""); location.setText("");
        });

        JButton sort = new JButton("Sort by Name (Merge Sort)");
        sort.addActionListener(e -> {
            sortHospitalsByName();
            StringBuilder sb = new StringBuilder("Hospitals sorted by name:\n");
            for (Hospital h : hospitals) sb.append("  ").append(h.name).append("  -  ").append(h.location).append("\n");
            show(sb.toString());
        });

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Hospital name:")); form.add(name);
        form.add(new JLabel("Location:"));      form.add(location);
        return wrap(form, add, sort);
    }

    // ----- Doctors tab -----
    private static JPanel doctorPanel() {
        JTextField id = new JTextField(16);
        JTextField name = new JTextField(16);
        JTextField phone = new JTextField(16);
        JTextField spec = new JTextField(16);
        JTextField searchSpec = new JTextField(16);

        JButton add = new JButton("Add Doctor");
        add.addActionListener(e -> {
            Integer parsed = parseInt(id.getText(), "Doctor ID");
            if (parsed == null) return;
            if (name.getText().trim().isEmpty()) { warn("Name is required."); return; }
            doctors.add(new Doctor(parsed, name.getText().trim(), phone.getText().trim(), spec.getText().trim()));
            show("Doctor added: " + name.getText().trim() + " (" + spec.getText().trim() + ")");
            id.setText(""); name.setText(""); phone.setText(""); spec.setText("");
        });

        JButton sort = new JButton("Sort by ID (Insertion Sort)");
        sort.addActionListener(e -> {
            sortDoctorsByID();
            StringBuilder sb = new StringBuilder("Doctors sorted by ID:\n");
            for (Doctor d : doctors)
                sb.append("  ID ").append(d.id).append("  |  ").append(d.name)
                  .append("  |  ").append(d.specialization).append("  |  ").append(d.phone).append("\n");
            show(sb.toString());
        });

        JButton search = new JButton("Search ALL by Specialty");
        search.addActionListener(e -> {
            String q = searchSpec.getText().trim();
            if (q.isEmpty()) { warn("Enter a specialty to search."); return; }
            List<Doctor> found = searchDoctorsBySpecialty(q);
            if (found.isEmpty()) {
                show("No doctors found with specialty '" + q + "'.");
            } else {
                StringBuilder sb = new StringBuilder("Doctors with specialty '" + q + "' (" + found.size() + " found):\n");
                for (Doctor d : found)
                    sb.append("  ID ").append(d.id).append("  |  ").append(d.name)
                      .append("  |  ").append(d.phone).append("\n");
                show(sb.toString());
            }
        });

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Doctor ID:"));        form.add(id);
        form.add(new JLabel("Name:"));             form.add(name);
        form.add(new JLabel("Phone:"));            form.add(phone);
        form.add(new JLabel("Specialization:"));   form.add(spec);
        form.add(new JLabel("Search specialty:")); form.add(searchSpec);
        return wrap(form, add, sort, search);
    }

    // ----- Patients tab -----
    private static JPanel patientPanel() {
        JTextField id = new JTextField(16);
        JTextField name = new JTextField(16);
        JTextField phone = new JTextField(16);
        JTextField age = new JTextField(16);
        JTextField searchId = new JTextField(16);

        JButton add = new JButton("Add Patient");
        add.addActionListener(e -> {
            Integer pid = parseInt(id.getText(), "Patient ID");
            if (pid == null) return;
            Integer a = parseInt(age.getText(), "Age");
            if (a == null) return;
            if (name.getText().trim().isEmpty()) { warn("Name is required."); return; }
            patients.add(new Patient(pid, name.getText().trim(), phone.getText().trim(), a));
            show("Patient added: " + name.getText().trim());
            id.setText(""); name.setText(""); phone.setText(""); age.setText("");
        });

        JButton sort = new JButton("Sort by ID (Quick Sort)");
        sort.addActionListener(e -> {
            sortPatientsByID();
            StringBuilder sb = new StringBuilder("Patients sorted by ID:\n");
            for (Patient p : patients)
                sb.append("  ID ").append(p.id).append("  |  ").append(p.name)
                  .append("  |  age ").append(p.age).append("  |  ").append(p.phone).append("\n");
            show(sb.toString());
        });

        JButton search = new JButton("Search by ID (Binary Search)");
        search.addActionListener(e -> {
            Integer key = parseInt(searchId.getText(), "Patient ID");
            if (key == null) return;
            int idx = binarySearchPatients(key);
            if (idx == -1) {
                show("Patient not found with ID: " + key);
            } else {
                Patient p = patients.get(idx);
                show("Patient found (at sorted index " + idx + "):\n"
                        + "  ID " + p.id + "  |  " + p.name + "  |  age " + p.age + "  |  " + p.phone);
            }
        });

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Patient ID:"));  form.add(id);
        form.add(new JLabel("Name:"));        form.add(name);
        form.add(new JLabel("Phone:"));       form.add(phone);
        form.add(new JLabel("Age:"));         form.add(age);
        form.add(new JLabel("Search ID:"));   form.add(searchId);
        return wrap(form, add, sort, search);
    }

    // ----- small GUI helpers -----
    private static JPanel wrap(JPanel form, JButton... buttons) {
        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.Y_AXIS));
        for (JButton b : buttons) {
            b.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(260, 30));
            buttonBar.add(b);
            buttonBar.add(javax.swing.Box.createVerticalStrut(6));
        }
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(form, BorderLayout.CENTER);
        panel.add(buttonBar, BorderLayout.EAST);
        return panel;
    }

    private static Integer parseInt(String text, String field) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException ex) {
            warn(field + " must be a valid whole number.");
            return null;
        }
    }

    private static void warn(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Invalid input", JOptionPane.WARNING_MESSAGE);
    }

    private static void show(String msg) {
        output.setText(msg);
        output.setCaretPosition(0);
    }
}
