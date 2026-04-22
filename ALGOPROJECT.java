package com.mycompany.algoproject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// 12300453  مهند ماجد عبد العظيم, class 9

// 16 Class , معاذ هشام , 12300396 


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
    int phone;
    String name;
    String specialization;

    Doctor(int id, String name, int phone, String specialization) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.specialization = specialization;
    }
}

class Patient {
    int id;
    int phone;
    int age;
    String name;
    Patient(int id, String name, int phone, int age) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.age = age;
    }
}

public class ALGOPROJECT {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Hospital> hospitals = new ArrayList<>();
    private static final List<Doctor> doctors = new ArrayList<>();
    private static final List<Patient> patients = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("------------- Hospital Management System Menu --------------");
            System.out.println("1. Add Hospital");
            System.out.println("2. Add Doctor");
            System.out.println("3. Add Patient");
            System.out.println("4. Sort Hospitals by Name");
            System.out.println("5. Sort Doctors by ID");
            System.out.println("6. Sort Patients by ID");
            System.out.println("7. Search Doctors by Specialty");
            System.out.println("8. Search Patient by ID");
            System.out.println("0. Exit");
            System.out.println("------------------------------------------------------------");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addHospital();
                    break;
                case 2:
                    addDoctor();
                    break;
                case 3:
                    addPatient();
                    break;
                case 4:
                    sortHospitalsByName();
                    break;
                case 5:
                    sortDoctorsByID();
                    break;
                case 6:
                    sortPatientsByID();
                    break;
                case 7:
                    searchDoctorsBySpecialty();
                    break;
                case 8:
                    searchPatientByID();
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    
    
    
    
    private static void addHospital() {
        System.out.print("Enter hospital name : ");
        String name = scanner.nextLine();
        System.out.print("Enter hospital location : ");
        String location = scanner.nextLine();
        hospitals.add(new Hospital(name, location));
        System.out.println("Hospital added successfully.");
    }

    private static void sortHospitalsByName() {
        mergeSortHospitals(hospitals, 0, hospitals.size() - 1);
        System.out.println("Hospitals sorted by name:");
        hospitals.forEach(h -> System.out.println("Name: " + h.name + ", Location: " + h.location));
    }
    
    private static void mergeSortHospitals(List<Hospital> hospitals, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;
            mergeSortHospitals(hospitals, left, middle);
            mergeSortHospitals(hospitals, middle + 1, right);
            merge(hospitals, left, middle, right);
        }
    }
    
    private static void merge(List<Hospital> hospitals, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;
        Hospital[] L = new Hospital[n1];
        Hospital[] R = new Hospital[n2];
        for (int i = 0; i < n1; ++i)
            L[i] = hospitals.get(left + i);
        for (int j = 0; j < n2; ++j)
            R[j] = hospitals.get(middle + 1 + j);
        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i].name.compareToIgnoreCase(R[j].name) <= 0) {
                hospitals.set(k, L[i]);
                i++;
            } else {
                hospitals.set(k, R[j]);
                j++;
            }
            k++;
        }
        while (i < n1) {
            hospitals.set(k, L[i]);
            i++;
            k++;
        }
        while (j < n2) {
            hospitals.set(k, R[j]);
            j++;
            k++;
        }
    }

    
    
    
    
    
    
    private static void addDoctor() {
        System.out.print("Enter doctor ID : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter doctor name : ");
        String name = scanner.nextLine();
        System.out.print("Enter doctor phone : ");
        int phone = scanner.nextInt();
        System.out.print("Enter doctor specialization : ");
        String specialization = scanner.nextLine();
        doctors.add(new Doctor(id, name, phone , specialization));
        System.out.println("Doctor added successfully.");
    }
    
    private static void sortDoctorsByID() {
        insertionSortDoctors();
        System.out.println("Doctors sorted by ID:");
        doctors.forEach(d -> System.out.println("ID: " + d.id + ", Name: " + d.name + ", Specialization: " + d.specialization));
    }
    
    private static void searchDoctorsBySpecialty() {
        scanner.nextLine(); 
        System.out.print("Enter specialty to search: ");
        String specialty = scanner.nextLine();
        binarySearchDoctors(specialty);
    }
    
    private static void insertionSortDoctors() {
        for (int i = 1; i < doctors.size(); ++i) {
            Doctor key = doctors.get(i);
            int j = i - 1;
            while (j >= 0 && doctors.get(j).id > key.id) {
                doctors.set(j + 1, doctors.get(j));
                j = j - 1;
            }
            doctors.set(j + 1, key);
        }
    }
    
    private static void binarySearchDoctors(String specialty) {
        int left = 0;
        int right = doctors.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (doctors.get(mid).specialization.equalsIgnoreCase(specialty)) {
                System.out.println("Doctor found, and his information is :");
                Doctor doctor = doctors.get(mid);
                System.out.println("ID: " + doctor.id + ", Name: " + doctor.name);
                return;
            } else if (doctors.get(mid).specialization.compareToIgnoreCase(specialty) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        System.out.println("Doctor with specialty '" + specialty + "' not found.");
    }
    
    
    
    
    
    
    
    
    private static void addPatient() {
        System.out.print("Enter patient ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();
        System.out.print("Enter patient phone: ");
        int phone = scanner.nextInt();
        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();
        patients.add(new Patient(id, name, phone, age));
        System.out.println("Patient added successfully.");
    }
    
    private static void sortPatientsByID() {
        quickSortPatients(patients, 0, patients.size() - 1);
        System.out.println("Patients sorted by ID:");
        patients.forEach(p -> System.out.println("ID: " + p.id + ", Name: " + p.name + ", Age: " + p.age));
    }

    private static void searchPatientByID() {
        System.out.print("Enter patient ID to search: ");
        int id = scanner.nextInt();
        int index = binarySearchPatients(id);
        if (index != -1) {
            System.out.println("Patient found:");
            Patient patient = patients.get(index);
            System.out.println("ID: " + patient.id + ", Name: " + patient.name + ", Age: " + patient.age);
        } else {
            System.out.println("Patient not found with ID: " + id);
        }
    }

    private static void quickSortPatients(List<Patient> patients, int low, int high) {
        if (low < high) {
            int pi = quick(patients, low, high);
            quickSortPatients(patients, low, pi - 1);
            quickSortPatients(patients, pi + 1, high);
        }
    }

    private static int quick(List<Patient> patients, int left, int right) {
        int pivot = patients.get(right).id;
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (patients.get(j).id < pivot) {
                i++;
                Patient temp = patients.get(i);
                patients.set(i, patients.get(j));
                patients.set(j, temp);
            }
        }
        Patient temp = patients.get(i + 1);
        patients.set(i + 1, patients.get(right));
        patients.set(right, temp);
        return i + 1;
    }

    private static int binarySearchPatients(int id) {
        int left = 0;
        int right = patients.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (patients.get(mid).id == id) {
                return mid;
            } else if (patients.get(mid).id < id) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
}

